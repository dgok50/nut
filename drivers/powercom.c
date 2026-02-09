/*
 * powercom.c - model specific routines for following units:
 *  -Trust 425/625
 *  -Powercom
 *  -Advice Partner/King PR750
 *    See http://www.advice.co.il/product/inter/ups.html for its specifications.
 *    This model is based on PowerCom (www.powercom.com) models.
 *  -Socomec Sicon Egys 420
 *  -OptiUPS VS 575C
 *
 * Copyrights:
 * (C) 2015 Arnaud Quette <ArnaudQuette@Eaton.com>
 * (C) 2013 Florian Bruhin <nut@the-compiler.org>
 * (C) 2002 Simon Rozman <simon@rozman.net>
 * (C) 1999  Peter Bieringer <pb@bieringer.de>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * rev 0.7: Alexey Sidorov <alexsid@altlinux.org>
 * - add Powercom's Black Knight Pro model support ( BNT-400/500/600/800/801/1000/1200/1500/2000AP 220-240V )
 *
 * rev 0.8: Alexey Sidorov <alexsid@altlinux.org>
 * - add Powercom's King Pro model support ( KIN-425/525/625/800/1000/1200/1500/1600/2200/3000/5000AP[-RM] 100-120,200-240 V)
 *
 * rev 0.9: Alexey Sidorov <alexsid@altlinux.org>
 * - add Powercom's Imperial model support ( IMP-xxxAP, IMD-xxxAP )
 *
 * rev 0.10: Alexey Sidorov <alexsid@altlinux.org>
 * - fix wrong detection KIN-2200AP
 * - use ser_set_dtr/ser_set_rts
 *
 * rev 0.11: Alexey Sidorov <alexsid@altlinux.org>
 * - move variables from .h to .c file (thanks Michael Tokarev for bugreport)
 * - fix string comparison (thanks Michael Tokarev for bugreport & Charles Lepple for patch)
 * - added BNT-other, for BNT 100-120V models (I havn't specs for it)
 *
 * Tested on: BNT-1200AP
 *
 * Known bugs:
 * - strange battery level on BNT1200AP in online mode( & may be on other models)
 * - i don't know how connect to IMP|IMD USB
 * - i havn't specs for BNT 100-120V models. Add BNT-other type for it
 *
 * rev 0.13: Keven Ates <atescomp@gmail.com>
 * - Modified functions to work for BNT-other 100-120V models.
 * - Modified BNT-other type defaults to work for the BNT 1500A 120VA model.
 * - Documented the type[] values purpose in a condensed format.
 * - BNT-other can be used to perform a complete user override of values for all PowerCom models, detected or not.
 *
 * Tested on: BNT-1500A
 *
 * rev 0.14: Florian Bruhin (The Compiler) <nut@the-compiler.org>
 * - Added support for OptiUPS VS 575C
 *   This probably also works with others, but I don't have their model numbers.
 *
 * rev 0.15: VSE NN <metanoite@rambler.ru>
 * - Fixed UPS type assignment for Powercom Imperial USB series manufactured since 2009.
 *
 * Tested on: IMP-625AP
 *
 * rev 0.16: Arnaud Quette
 * - Fixed the processing of input/output voltages for KIN models
 *   (https://github.com/networkupstools/nut/issues/187)
 *
 * rev 0.18: Rouben Tchakhmakhtchian
 * - Added nobt flag to config that skips UPS battery check on startup/init
 *   (https://github.com/networkupstools/nut/issues/546)
 *
 */

#include "main.h"
#include "serial.h"
#include "powercom.h"
#include "nut_float.h"
#include <string.h>
#include <math.h>

#define DRIVER_NAME	"PowerCom protocol UPS driver"
#define DRIVER_VERSION	"0.27"

/* driver description structure */
upsdrv_info_t	upsdrv_info = {
	DRIVER_NAME,
	DRIVER_VERSION,
	"Simon Rozman <simon@rozman.net>\n" \
	"Peter Bieringer <pb@bieringer.de>\n" \
	"Alexey Sidorov <alexsid@altlinux.org>\n" \
	"Florian Bruhin <nut@the-compiler.org>\n" \
	"Arnaud Quette <ArnaudQuette@Eaton.com>\n" \
	"Rouben Tchakhmakhtchian <rouben@rouben.net>",
	DRV_STABLE,
	{ NULL }
};

#define NUM_OF_SUBTYPES              (sizeof (types) / sizeof (*types))

/* general constants */
enum general {
	MAX_NUM_OF_BYTES_FROM_UPS = 16
};

/* variables used by module */
static unsigned char raw_data[MAX_NUM_OF_BYTES_FROM_UPS]; /* raw data reveived from UPS */
static unsigned int linevoltage = 230U; /* line voltage, can be defined via command line option */
static const char *manufacturer = "PowerCom";
static const char *modelname    = "Unknown";
static const char *serialnumber = "Unknown";
static unsigned int type = 0;

/* COM2 protocol variables */
static enum protocol_mode current_protocol = PROTOCOL_AUTO;
static enum protocol_mode configured_protocol = PROTOCOL_AUTO;
static struct com2_data com2_current;
static struct event_state event_tracking;
static const char *com2_command = "Q1";  /* Q1 or DQ1 */
static unsigned int event_hold_time = 20;  /* seconds to latch events */
static int allow_control = 0;  /* allow dangerous commands */
static time_t last_mode_switch = 0;
static int com1_fail_count = 0;
static int com2_fail_count = 0;


/* forward declaration of functions used to setup flow control */
static void dtr0rts1 (void);
static void no_flow_control (void);

/* struct defining types
 * ---------------------
 * See powercom.h for detailed information and functions.
 *
 * The following type defaults use this definition:
 *
 *	"TypeID",
 *	ByteCount,
 *	{ "FlowControlString", FlowControlFuncPtr },
 *	{ { ValidationIndex, ValidationValue },
 *	  { ValidationIndex, ValidationValue },
 *	  { ValidationIndex, ValidationValue } },
 *	{ { DelayShutdownMinutes, DelayShutdownSeconds },
 *	  UseMinutesChar'y''n' },
 *	{ FrequencyFactor, FrequencyConstant },
 *	{ OfflineLoadFactor, OfflineLoadConstant,
 *	  OnlineLoadFactor, OnlineLoadConstant },
 *	{ OfflineBatteryFactor, OfflineLoad%Factor, OfflineBatteryConstant,
 *	  OnlineBatteryFactor, OnlineBatteryConstant },
 *	{ 240VoltageFactor, 240VoltageConstant,
 *	  120VoltageFactor, 120VoltageConstant },
 */
static struct type types[] = {
	{
		"Trust",
		11,
		{  "dtr0rts1", dtr0rts1 },
		{ { 5U, 0U }, { 7U, 0U }, { 8U, 0U } },
		{ { 0U, 10U }, 'n' },
		{  0.00020997, 0.00020928 },
		{  6.1343, -0.3808,  4.3110,  0.1811 },
		{  5.0000,  0.3268,  -825.00,  4.5639, -835.82 },
		{  1.9216, -0.0977,  0.9545,  0.0000 },
	},
	{
		"Egys",
		16,
		{  "no_flow_control", no_flow_control },
		{ { 5U, 0x80U }, { 7U, 0U }, { 8U, 0U } },
		{ { 0U, 10U }, 'n' },
		{  0.00020997, 0.00020928 },
		{  6.1343, -0.3808,  1.3333,  0.6667 },
		{  5.0000,  0.3268,  -825.00,  2.2105, -355.37 },
		{  1.9216, -0.0977,  0.9545,  0.0000 },
	},
	{
		"KP625AP",
		16,
		{  "dtr0rts1", dtr0rts1 },
		{ { 5U, 0x80U }, { 7U, 0U }, { 8U, 0U } },
		{ { 0U, 10U }, 'n' },
		{  0.00020997, 0.00020928 },
		{  6.1343, -0.3808,  4.3110,  0.1811 },
		{  5.0000,  0.3268,  -825.00,  4.5639, -835.82 },
		{  1.9216, -0.0977,  0.9545,  0.0000 },
	},
	{
		"IMP",
		16,
		{  "no_flow_control", no_flow_control },
		{ { 5U, 0xFFU }, { 7U, 0U }, { 8U, 0U } },
		{ { 1U, 30U }, 'y' },
		{  0.00020997, 0.00020928 },
		{  6.1343, -0.3808,  4.3110,  0.1811 },
		{  5.0000,  0.3268,  -825.00,  4.5639, -835.82 },
		{  1.9216, -0.0977,  0.9545,  0.0000 },
	},
	{
		"KIN",
		16,
		{  "no_flow_control", no_flow_control },
		{ { 11U, 0x4bU }, { 8U, 0U }, { 8U, 0U } },
		{ { 1U, 30U }, 'y' },
		{  0.00020997, 0.0 },
		{  6.1343, -0.3808,  1.075,  0.1811 },
		{  5.0000,  0.3268,  -825.00,  0.46511, 0 },
		{  1.9216, -0.0977,  0.82857,  0.0000 },
	},
	{
		"BNT",
		16,
		{  "no_flow_control", no_flow_control },
		{ { 11U, 0x42U }, { 8U, 0U }, { 8U, 0U } },
		{ { 1U, 30U }, 'y' },
		{  0.00020803, 0.0 },
		{  1.4474,     0.0,   0.8594,  0.0 },
		{  5.0000,  0.3268,  -825.00,  0.46511, 0 },
		{  1.9216, -0.0977,  0.82857,  0.0000 },
	},
	{
		"BNT-other",
		16,
		{  "no_flow_control", no_flow_control },
		{ { 8U, 0U }, { 8U, 0U }, { 8U, 0U } },
		{ { 1U, 30U }, 'y' },
		{  0.00027778, 0.0000 },
		{  1.0000,  0.0000,  1.0000,  0.0000 },
		{  1.0000,  0.0000,  0.0000,  1.0000,  0.0000 },
		{  2.0000,  0.0000,  2.0000,  0.0000 },
	},
	{
		"OPTI",
		16,
		{  "no_flow_control", no_flow_control },
		{ { 5U, 0xFFU }, { 7U, 0U }, { 8U, 0U } },
		{ { 1U, 30U }, 'y' },
		{  0.0000, 0.0000 },
		{  1.0000,  0.0000,  1.0000,  0.0000 },
		{  1.0000,  0.0000,  0.0000,  1.0000,  0.0000 },
		{  2.0000,  0.0000,  2.0000,  0.0000 },
	},
};

/* values for sending to UPS */
enum commands {
	SEND_DATA    = '\x01',
	BATTERY_TEST = '\x03',
	WAKEUP_TIME  = '\x04',
	RESTART      = '\xb9',
	SHUTDOWN     = '\xba',
	COUNTER      = '\xbc'
};

/* location of data in received string */
enum data {
	UPS_LOAD         = 0U,
	BATTERY_CHARGE   = 1U,
	INPUT_VOLTAGE    = 2U,
	OUTPUT_VOLTAGE   = 3U,
	INPUT_FREQUENCY  = 4U,
	UPSVERSION       = 5U,
	OUTPUT_FREQUENCY = 6U,
	STATUS_A         = 9U,
	STATUS_B         = 10U,
	MODELNAME        = 11U,
	MODELNUMBER      = 12U
};

/* status bits */
enum status {
	SUMMARY       = 0U,
	MAINS_FAILURE = 1U,
	ONLINE        = 1U,
	FAULT         = 1U,
	LOW_BAT       = 2U,
	BAD_BAT       = 2U,
	TEST          = 4U,
	AVR_ON        = 8U,
	AVR_MODE      = 16U,
	SD_COUNTER    = 16U,
	OVERLOAD      = 32U,
	SHED_COUNTER  = 32U,
	DIS_NOLOAD    = 64U,
	SD_DISPLAY    = 128U,
	OFF           = 128U
};

static unsigned int voltages[]	= {100,110,115,120,0,0,0,200,220,230,240,0,0,0,0,0};
static unsigned int BNTmodels[]	= {0,400,500,600,800,801,1000,1200,1500,2000,0,0,0,0,0,0};
static unsigned int KINmodels[]	= {0,425,500,525,625,800,1000,1200,1500,1600,2200,2200,2500,3000,5000,0};
static unsigned int IMPmodels[]	= {0,425,525,625,825,1025,1200,1500,2000,0,0,0,0,0,0,0};
static unsigned int OPTImodels[]	= {0,0,0,575,0,0,0,0,0,0,0,0,0,0,0,0};

/*
 * local used functions
 */

static void shutdown_halt(void)
{
	ser_send_char (upsfd, (unsigned char)SHUTDOWN);
	if (types[type].shutdown_arguments.minutesShouldBeUsed != 'n')
		ser_send_char (upsfd, types[type].shutdown_arguments.delay[0]);
	ser_send_char (upsfd, types[type].shutdown_arguments.delay[1]);
	upslogx(LOG_INFO, "Shutdown (stayoff) initiated.");

	if (handling_upsdrv_shutdown > 0)
		set_exit_flag(EF_EXIT_SUCCESS);
}

static void shutdown_ret(void)
{
	ser_send_char (upsfd, (unsigned char)RESTART);
	ser_send_char (upsfd, (unsigned char)COUNTER);
	if (types[type].shutdown_arguments.minutesShouldBeUsed != 'n')
		ser_send_char (upsfd, types[type].shutdown_arguments.delay[0]);
	ser_send_char (upsfd, types[type].shutdown_arguments.delay[1]);
	upslogx(LOG_INFO, "Shutdown (return) initiated.");

	if (handling_upsdrv_shutdown > 0)
		set_exit_flag(EF_EXIT_SUCCESS);
}

/* registered instant commands */
static int instcmd (const char *cmdname, const char *extra)
{
	char cmd_buf[8];
	int cmd_len;
	
	/* May be used in logging below, but not as a command argument */
	NUT_UNUSED_VARIABLE(extra);
	upsdebug_INSTCMD_STARTING(cmdname, extra);

	/* COM1 commands */
	if (!strcasecmp(cmdname, "test.battery.start")) {
		upslog_INSTCMD_POWERSTATE_MAYBE(cmdname, extra);
		if (current_protocol == PROTOCOL_COM2) {
			/* COM2: T command */
			cmd_buf[0] = 'T';
			cmd_buf[1] = '\r';
			cmd_len = 2;
			if (ser_send_pace(upsfd, 10, cmd_buf, cmd_len) == cmd_len) {
				return STAT_INSTCMD_HANDLED;
			}
			return STAT_INSTCMD_FAILED;
		} else {
			ser_send_char (upsfd, BATTERY_TEST);
			return STAT_INSTCMD_HANDLED;
		}
	}
	
	/* COM2-specific commands */
	if (current_protocol == PROTOCOL_COM2) {
		if (!strcasecmp(cmdname, "test.battery.stop")) {
			upslog_INSTCMD_POWERSTATE_MAYBE(cmdname, extra);
			/* COM2: CT command */
			cmd_buf[0] = 'C';
			cmd_buf[1] = 'T';
			cmd_buf[2] = '\r';
			cmd_len = 3;
			if (ser_send_pace(upsfd, 10, cmd_buf, cmd_len) == cmd_len) {
				return STAT_INSTCMD_HANDLED;
			}
			return STAT_INSTCMD_FAILED;
		}
		
		if (!strcasecmp(cmdname, "beeper.toggle")) {
			/* COM2: Q command */
			cmd_buf[0] = 'Q';
			cmd_buf[1] = '\r';
			cmd_len = 2;
			if (ser_send_pace(upsfd, 10, cmd_buf, cmd_len) == cmd_len) {
				return STAT_INSTCMD_HANDLED;
			}
			return STAT_INSTCMD_FAILED;
		}
		
		/* Dangerous commands - require allow_control=yes */
		if (!strcasecmp(cmdname, "shutdown.stayoff.dangerous")) {
			if (!allow_control) {
				upslogx(LOG_WARNING, "Command %s requires allow_control=yes", cmdname);
				return STAT_INSTCMD_FAILED;
			}
			upslog_INSTCMD_POWERSTATE_CHANGE(cmdname, extra);
			/* COM2: S0X command (X = delay in minutes) */
			upslogx(LOG_WARNING, "Executing dangerous shutdown command");
			cmd_buf[0] = 'S';
			cmd_buf[1] = '0';
			cmd_buf[2] = '1';  /* 1 minute delay */
			cmd_buf[3] = '\r';
			cmd_len = 4;
			if (ser_send_pace(upsfd, 10, cmd_buf, cmd_len) == cmd_len) {
				if (handling_upsdrv_shutdown > 0)
					set_exit_flag(EF_EXIT_SUCCESS);
				return STAT_INSTCMD_HANDLED;
			}
			return STAT_INSTCMD_FAILED;
		}
	}
	
	/* COM1 shutdown commands */
	if (!strcasecmp(cmdname, "shutdown.return")) {
		/* NOTE: In this context, "return" is UPS behavior after the
		 * wall-power gets restored. The routine exits the driver anyway.
		 */
		upslog_INSTCMD_POWERSTATE_CHANGE(cmdname, extra);
		shutdown_ret();
		return STAT_INSTCMD_HANDLED;
	}
	if (!strcasecmp(cmdname, "shutdown.stayoff")) {
		upslog_INSTCMD_POWERSTATE_CHANGE(cmdname, extra);
		shutdown_halt();
		return STAT_INSTCMD_HANDLED;
	}

	upslog_INSTCMD_UNKNOWN(cmdname, extra);
	return STAT_INSTCMD_UNKNOWN;
}

/* set DTR and RTS lines on a serial port to supply a passive
 * serial interface: DTR to 0 (-V), RTS to 1 (+V)
 */
static void dtr0rts1 (void)
{
	ser_set_dtr(upsfd, 0);
	ser_set_rts(upsfd, 1);
	upsdebugx(2, "DTR => 0, RTS => 1");
}

/* clear any flow control */
static void no_flow_control (void)
{
	struct termios tio;

	tcgetattr (upsfd, &tio);

	tio.c_iflag &= ~ ((tcflag_t)IXON | (tcflag_t)IXOFF);
	tio.c_cc[VSTART] = _POSIX_VDISABLE;
	tio.c_cc[VSTOP] = _POSIX_VDISABLE;

	upsdebugx(2, "Flow control disable");

	/* disable any flow control */
	tcsetattr(upsfd, TCSANOW, &tio);
}

/* sane check for returned buffer */
static int validate_raw_data (void)
{
	int i = 0,
	num_of_tests =
		sizeof types[0].validation / sizeof types[0].validation[0];

	for (
		i = 0;
		i < num_of_tests
		&& raw_data[types[type].validation[i].index_of_byte]
		         == types[type].validation[i].required_value;
		i++) ;
	return (i < num_of_tests) ? 1 : 0;
}

/* get info from ups */
static int ups_getinfo(void)
{
	size_t	i;
	ssize_t c;

	/* send trigger char to UPS */
	if (ser_send_char (upsfd, SEND_DATA) != 1) {
		upslogx(LOG_NOTICE, "writing error");
		dstate_datastale();
		return 0;
	} else {
		upsdebugx(5, "Num of bytes requested for reading from UPS: %d", types[type].num_of_bytes_from_ups);

		/* Note: num_of_bytes_from_ups is (unsigned char) so comparable
		 * to ssize_t without more range checks */
		c = ser_get_buf_len(upsfd, raw_data,
			types[type].num_of_bytes_from_ups, 3, 0);

		if (c != (ssize_t)types[type].num_of_bytes_from_ups) {
			upslogx(LOG_NOTICE, "data receiving error (%" PRIiSIZE " instead of %d bytes)", c, types[type].num_of_bytes_from_ups);
			dstate_datastale();
			return 0;
		} else
			upsdebugx(5, "Num of bytes received from UPS: %" PRIiSIZE, c);
	}

	/* optional dump of raw data */
	if (nut_debug_level > 4) {
		/* FIXME: use upsdebug_hex() ? */
		printf("Raw data from UPS:\n");
		for (i = 0; i < types[type].num_of_bytes_from_ups; i++) {
			printf("%2zu 0x%02x (%c)\n", i, raw_data[i], raw_data[i]>=0x20 ? raw_data[i] : ' ');
		}
	}

	/* validate raw data for correctness */
	if (validate_raw_data() != 0) {
		upslogx(LOG_NOTICE, "data receiving error (validation check)");
		dstate_datastale();
		return 0;
	}
	return 1;
}

/* ========== COM2 Protocol Implementation ========== */

/* COM2: Parse status bits from 8-character string (b7-b0)
 * Status field format: 8 ASCII characters representing bits
 * b7=1: Power failure, b6=1: Battery low, b5=1: Bypass/AVR active
 * b4=1: UPS fault, b3: 0=Online 1=Offline 2/3=Battery fault
 * b2=1: Self-test running, b1=1: UPS output OFF, b0=1: Beeper disabled
 */
static int parse_com2_status(const char *status_str, unsigned char *bits)
{
	size_t len;
	int i;
	
	/* Ensure string is null-terminated and has valid length */
	if (!status_str) {
		return 0;
	}
	
	len = strnlen(status_str, 16);  /* Check up to 16 chars max */
	if (len != 8) {
		return 0;
	}
	
	for (i = 0; i < 8; i++) {
		if (status_str[i] < '0' || status_str[i] > '3') {
			return 0;
		}
		bits[i] = status_str[i] - '0';
	}
	return 1;
}

/* COM2: Minimum response length for valid COM2 response
 * Format: "(InputV OutputV Load Freq BattLevel Temp Status"
 * Minimum example: "(100 200 10 50.0 100 25 00000000" = 46 bytes
 */
#define COM2_MIN_RESPONSE_LEN 46

/* COM2: Send command and read ASCII response
 * Command: Q1 or DQ1 followed by \r
 * Response format: (InputV OutputV Load Freq BattLevel Temp Status
 * Returns 1 on success, 0 on failure
 */
static int ups_getinfo_com2(void)
{
	char cmd_buf[8];
	unsigned char response[128];
	ssize_t ret;
	int cmd_len;
	char *token;
	char *saveptr = NULL;
	int field_count = 0;
	char response_copy[128];
	
	/* Prepare command: Q1\r or DQ1\r */
	if (!strcmp(com2_command, "DQ1")) {
		cmd_buf[0] = 'D';
		cmd_buf[1] = 'Q';
		cmd_buf[2] = '1';
		cmd_buf[3] = '\r';
		cmd_len = 4;
	} else {
		cmd_buf[0] = 'Q';
		cmd_buf[1] = '1';
		cmd_buf[2] = '\r';
		cmd_len = 3;
	}
	
	/* Send command with pacing */
	ret = ser_send_pace(upsfd, 10, cmd_buf, cmd_len);
	if (ret != cmd_len) {
		upsdebugx(2, "COM2: Failed to send command");
		return 0;
	}
	
	/* Read response with 3 second timeout */
	memset(response, 0, sizeof(response));
	ret = ser_get_buf_len(upsfd, response, sizeof(response) - 1, 3, 0);
	
	if (ret < COM2_MIN_RESPONSE_LEN) {
		upsdebugx(2, "COM2: Response too short (%" PRIiSIZE " bytes, expected >= %d)", ret, COM2_MIN_RESPONSE_LEN);
		return 0;
	}
	
	upsdebugx(3, "COM2: Received %" PRIiSIZE " bytes: %s", ret, response);
	
	/* Validate response starts with '(' */
	if (response[0] != '(') {
		upsdebugx(2, "COM2: Response doesn't start with '('");
		return 0;
	}
	
	/* Parse space-separated fields - response is null-terminated by ser_get_buf_len */
	snprintf(response_copy, sizeof(response_copy), "%s", response);
	
	/* Skip opening '(' and parse fields */
	token = strtok_r(response_copy, " ", &saveptr);
	if (!token || token[0] != '(') {
		upsdebugx(2, "COM2: Invalid response format");
		return 0;
	}
	
	/* Field 0: Input Voltage (skip '(' prefix) */
	com2_current.input_voltage = strtod(token + 1, NULL);
	field_count++;
	
	/* Field 1: Output Voltage - not used, field 2 has correct value */
	token = strtok_r(NULL, " ", &saveptr);
	if (!token) goto parse_error;
	field_count++;
	
	/* Field 2: Output Voltage (actual) */
	token = strtok_r(NULL, " ", &saveptr);
	if (!token) goto parse_error;
	com2_current.output_voltage = strtod(token, NULL);
	field_count++;
	
	/* Field 3: Load % */
	token = strtok_r(NULL, " ", &saveptr);
	if (!token) goto parse_error;
	com2_current.load = strtod(token, NULL);
	field_count++;
	
	/* Field 4: Frequency */
	token = strtok_r(NULL, " ", &saveptr);
	if (!token) goto parse_error;
	com2_current.frequency = strtod(token, NULL);
	field_count++;
	
	/* Field 5: Battery level or voltage */
	token = strtok_r(NULL, " ", &saveptr);
	if (!token) goto parse_error;
	com2_current.battery_level = strtod(token, NULL);
	field_count++;
	
	/* Field 6: Temperature */
	token = strtok_r(NULL, " ", &saveptr);
	if (!token) goto parse_error;
	com2_current.temperature = strtod(token, NULL);
	field_count++;
	
	/* Field 7: Status bits (8 characters) */
	token = strtok_r(NULL, " ", &saveptr);
	if (!token) goto parse_error;
	if (!parse_com2_status(token, com2_current.status_bits)) {
		upsdebugx(2, "COM2: Invalid status field");
		return 0;
	}
	field_count++;
	
	if (field_count < 8) {
		goto parse_error;
	}
	
	com2_current.last_valid = time(NULL);
	
	/* Debug output */
	if (nut_debug_level > 2) {
		upsdebugx(3, "COM2 parsed: InputV=%.1f OutputV=%.1f Load=%.1f Freq=%.1f Batt=%.1f Temp=%.1f",
			com2_current.input_voltage, com2_current.output_voltage,
			com2_current.load, com2_current.frequency,
			com2_current.battery_level, com2_current.temperature);
		upsdebugx(3, "COM2 status bits: b7=%d b6=%d b5=%d b4=%d b3=%d b2=%d b1=%d b0=%d",
			com2_current.status_bits[0], com2_current.status_bits[1],
			com2_current.status_bits[2], com2_current.status_bits[3],
			com2_current.status_bits[4], com2_current.status_bits[5],
			com2_current.status_bits[6], com2_current.status_bits[7]);
	}
	
	return 1;
	
parse_error:
	upsdebugx(2, "COM2: Parse error at field %d", field_count);
	return 0;
}

/* COM2: Detect and track events based on status changes */
static void com2_detect_events(void)
{
	int i;
	int status_changed = 0;
	time_t now = time(NULL);
	char event_buf[128];
	
	/* Check if this is first run */
	if (event_tracking.last_event_time == 0) {
		memcpy(event_tracking.prev_status, com2_current.status_bits, 8);
		event_tracking.last_event_time = now;
		return;
	}
	
	/* Detect status bit changes */
	for (i = 0; i < 8; i++) {
		if (event_tracking.prev_status[i] != com2_current.status_bits[i]) {
			status_changed = 1;
			break;
		}
	}
	
	if (!status_changed) {
		return;
	}
	
	/* Analyze specific transitions */
	event_buf[0] = '\0';
	
	/* b7: Power failure/restore */
	if (event_tracking.prev_status[0] == 0 && com2_current.status_bits[0] == 1) {
		snprintf(event_buf, sizeof(event_buf), "power_failure");
	} else if (event_tracking.prev_status[0] == 1 && com2_current.status_bits[0] == 0) {
		snprintf(event_buf, sizeof(event_buf), "power_restore");
	}
	/* b5: AVR/Bypass on/off */
	else if (event_tracking.prev_status[2] == 0 && com2_current.status_bits[2] == 1) {
		snprintf(event_buf, sizeof(event_buf), "avr_bypass_active");
	} else if (event_tracking.prev_status[2] == 1 && com2_current.status_bits[2] == 0) {
		snprintf(event_buf, sizeof(event_buf), "avr_bypass_inactive");
	}
	/* b2: Test start/stop */
	else if (event_tracking.prev_status[5] == 0 && com2_current.status_bits[5] == 1) {
		snprintf(event_buf, sizeof(event_buf), "self_test_start");
	} else if (event_tracking.prev_status[5] == 1 && com2_current.status_bits[5] == 0) {
		snprintf(event_buf, sizeof(event_buf), "self_test_stop");
	}
	
	if (event_buf[0] != '\0') {
		snprintf(event_tracking.last_event, sizeof(event_tracking.last_event), "%s", event_buf);
		event_tracking.last_event_time = now;
		event_tracking.event_count++;
		upsdebugx(1, "COM2: Event detected: %s", event_buf);
	}
	
	/* Update previous status */
	memcpy(event_tracking.prev_status, com2_current.status_bits, 8);
}

/* COM2: Update NUT variables from parsed data */
static void com2_update_vars(void)
{
	time_t now = time(NULL);
	int event_latched = 0;
	
	/* Detect events before updating vars */
	com2_detect_events();
	
	/* Basic measurements */
	dstate_setinfo("input.voltage", "%.1f", com2_current.input_voltage);
	dstate_setinfo("output.voltage", "%.1f", com2_current.output_voltage);
	dstate_setinfo("ups.load", "%.1f", com2_current.load);
	dstate_setinfo("input.frequency", "%.1f", com2_current.frequency);
	
	/* Battery level (could be % or voltage depending on value range)
	 * Heuristic: If > 50, assume percentage (0-100%)
	 * If <= 50, check if it's in valid battery voltage range (10-60V)
	 * Typical UPS battery voltages: 12V, 24V, 36V, 48V systems
	 */
	if (com2_current.battery_level > 50.0) {
		/* Likely percentage (50-100%) */
		dstate_setinfo("battery.charge", "%.1f", com2_current.battery_level);
	} else if (com2_current.battery_level >= 10.0 && com2_current.battery_level <= 50.0) {
		/* Ambiguous range - could be low percentage or voltage
		 * Check if value looks like typical battery voltage */
		if (com2_current.battery_level >= 11.0 && com2_current.battery_level <= 14.0) {
			/* 12V system voltage range */
			dstate_setinfo("battery.voltage", "%.2f", com2_current.battery_level);
		} else if (com2_current.battery_level >= 22.0 && com2_current.battery_level <= 28.0) {
			/* 24V system voltage range */
			dstate_setinfo("battery.voltage", "%.2f", com2_current.battery_level);
		} else if (com2_current.battery_level >= 33.0 && com2_current.battery_level <= 40.0) {
			/* 36V system voltage range */
			dstate_setinfo("battery.voltage", "%.2f", com2_current.battery_level);
		} else if (com2_current.battery_level >= 44.0 && com2_current.battery_level <= 50.0) {
			/* 48V system voltage range */
			dstate_setinfo("battery.voltage", "%.2f", com2_current.battery_level);
		} else {
			/* Otherwise assume percentage */
			dstate_setinfo("battery.charge", "%.1f", com2_current.battery_level);
		}
	} else {
		/* Very low value - likely percentage */
		dstate_setinfo("battery.charge", "%.1f", com2_current.battery_level);
	}
	
	/* Temperature */
	if (com2_current.temperature > 0.0) {
		dstate_setinfo("ups.temperature", "%.1f", com2_current.temperature);
	}
	
	/* Status processing */
	status_init();
	alarm_init();
	
	/* b7: Power failure */
	if (com2_current.status_bits[0] == 1) {
		status_set("OB");  /* On battery */
	} else {
		/* b1: UPS output OFF */
		if (com2_current.status_bits[6] == 1) {
			status_set("OFF");
		} else {
			status_set("OL");  /* Online */
		}
	}
	
	/* b6: Battery low */
	if (com2_current.status_bits[1] == 1) {
		status_set("LB");
	}
	
	/* b5: Bypass/AVR active */
	if (com2_current.status_bits[2] == 1) {
		/* Check if online or offline for AVR vs Bypass */
		if (com2_current.status_bits[3] == 0) {
			/* Online: Bypass mode */
			status_set("BYPASS");
			alarm_set("voltage not regulated");
		} else {
			/* Offline: AVR mode */
			if (com2_current.input_voltage > com2_current.output_voltage) {
				status_set("TRIM");
			} else {
				status_set("BOOST");
			}
		}
	}
	
	/* b4: UPS fault (status_bits[3] represents b4) */
	if (com2_current.status_bits[3] == 1) {
		alarm_set("UPS fault");
	}
	
	/* b3: Online/Offline/Battery fault status (status_bits[4] represents b3)
	 * Values: 0=Online, 1=Offline, 2/3=Battery fault
	 */
	if (com2_current.status_bits[4] >= 2) {
		status_set("RB");  /* Replace battery */
	}
	
	/* b2: Self-test running */
	if (com2_current.status_bits[5] == 1) {
		status_set("TEST");
		dstate_setinfo("ups.test.status", "in_progress");
	} else {
		dstate_setinfo("ups.test.status", "done");
	}
	
	/* b0: Beeper status */
	if (com2_current.status_bits[7] == 1) {
		dstate_setinfo("ups.beeper.status", "disabled");
	} else {
		dstate_setinfo("ups.beeper.status", "enabled");
	}
	
	status_commit();
	alarm_commit();
	
	/* Event latching - keep event visible for event_hold_time seconds */
	if (event_tracking.last_event_time > 0 &&
	    (now - event_tracking.last_event_time) <= event_hold_time) {
		dstate_setinfo("ups.event.last", "%s", event_tracking.last_event);
		dstate_setinfo("ups.event.time", "%ld", (long)event_tracking.last_event_time);
		dstate_setinfo("ups.event.count", "%u", event_tracking.event_count);
		event_latched = 1;
	}
	
	/* Clear latched event info after hold time */
	if (!event_latched && event_tracking.last_event[0] != '\0') {
		dstate_delinfo("ups.event.last");
		dstate_delinfo("ups.event.time");
	}
}

/* Auto-detect protocol mode */
static int detect_protocol(void)
{
	time_t now = time(NULL);
	
	/* Check cooldown period (10 seconds minimum between switches) */
	if (last_mode_switch > 0 && (now - last_mode_switch) < 10) {
		upsdebugx(2, "Protocol switch cooldown active");
		return (current_protocol == PROTOCOL_COM1 || current_protocol == PROTOCOL_COM2) ? 1 : 0;
	}
	
	/* If configured protocol is set, try it first */
	if (configured_protocol != PROTOCOL_AUTO) {
		current_protocol = configured_protocol;
		last_mode_switch = now;
		upsdebugx(1, "Using configured protocol: COM%d", current_protocol);
		return 1;
	}
	
	/* Try COM1 (2400 baud) */
	if (current_protocol == PROTOCOL_AUTO || current_protocol == PROTOCOL_COM1) {
		ser_set_speed(upsfd, device_path, B1200);
		/* Allow serial port to settle after baud rate change */
		{
			struct timespec ts;
			ts.tv_sec = 0;
			ts.tv_nsec = 100000000L;  /* 100ms */
			nanosleep(&ts, NULL);
		}
		
		if (ups_getinfo()) {
			com1_fail_count = 0;
			if (current_protocol != PROTOCOL_COM1) {
				current_protocol = PROTOCOL_COM1;
				last_mode_switch = now;
				upsdebugx(1, "Auto-detected protocol: COM1 (binary, 1200 baud)");
			}
			return 1;
		} else {
			com1_fail_count++;
		}
	}
	
	/* Try COM2 (2400 baud) */
	if (current_protocol == PROTOCOL_AUTO || current_protocol == PROTOCOL_COM2 || com1_fail_count >= 3) {
		ser_set_speed(upsfd, device_path, B2400);
		/* Allow serial port to settle after baud rate change */
		{
			struct timespec ts;
			ts.tv_sec = 0;
			ts.tv_nsec = 100000000L;  /* 100ms */
			nanosleep(&ts, NULL);
		}
		
		if (ups_getinfo_com2()) {
			com2_fail_count = 0;
			if (current_protocol != PROTOCOL_COM2) {
				current_protocol = PROTOCOL_COM2;
				last_mode_switch = now;
				upsdebugx(1, "Auto-detected protocol: COM2 (ASCII, 2400 baud)");
			}
			return 1;
		} else {
			com2_fail_count++;
		}
	}
	
	/* Fallback: reset fail counters after consecutive failures */
	if (com1_fail_count >= 3 && com2_fail_count >= 3) {
		upsdebugx(1, "Both protocols failing, resetting fail counters");
		com1_fail_count = 0;
		com2_fail_count = 0;
		current_protocol = PROTOCOL_AUTO;
	}
	
	return 0;
}

/* ========== End COM2 Protocol Implementation ========== */

static float input_voltage(void)
{
	unsigned int model;
	float tmp=0.0;

	if ( !strcmp(types[type].name, "BNT") && raw_data[MODELNUMBER]%16 > 7 ) {
		tmp=2.2*raw_data[INPUT_VOLTAGE]-24;
	} else if ( !strcmp(types[type].name, "KIN")) {
		model=KINmodels[raw_data[MODELNUMBER]/16];
		/* Process input voltage, according to line voltage and model rating */
		if (linevoltage < 200) {
			if (model <= 625) {
				tmp = 0.89 * raw_data[INPUT_VOLTAGE] + 6.18;
			} else if ((model >= 800) && (model < 2000)) {
				tmp = 1.61 * raw_data[INPUT_VOLTAGE] / 2.0;
			} else {
				tmp = 1.625 * raw_data[INPUT_VOLTAGE] / 2.0;
			}
		}
		if (linevoltage >= 200) {
			if (model <= 625) {
				tmp = 1.79 * raw_data[INPUT_VOLTAGE] + 3.35;
			} else if ((model >= 800) && (model < 2000)) {
				tmp = 1.61 * raw_data[INPUT_VOLTAGE];
			} else {
				tmp = 1.625 * raw_data[INPUT_VOLTAGE];
			}
		}
	} else if ( !strcmp(types[type].name, "IMP") || !strcmp(types[type].name, "OPTI")) {
		tmp=raw_data[INPUT_VOLTAGE]*2.0;
	} else {
		tmp=linevoltage >= 220 ?
			types[type].voltage[0] * raw_data[INPUT_VOLTAGE] + types[type].voltage[1] :
			types[type].voltage[2] * raw_data[INPUT_VOLTAGE] + types[type].voltage[3];
	}
	if (tmp<0) tmp=0.0;
	return tmp;
}

static float output_voltage(void)
{
	float tmp,rdatax,rdatay,rdataz,boostdata;
	unsigned int statINV = 0,statAVR = 0,statAVRMode = 0,model,t;
	static float datax1[]={0,1.0,1.0,1.0,1.0,0.945,0.945,0.945,0.127,0.127,0.945,0.945,0.945,0.256};
	static float datay1[]={0,0.85,0.85,0.85,0.88,0.9,0.9,0.9,6.6,6.6,0.87,0.87,0.87,3.29};
	static float dataz1[]={0,1.03,0.78,0.78,0.72,0.55,0.55,0.55,0.5,0.5,0.43,0.43,0.43,0.3};
	static float datax2[]={0,1.0,1.0,1.0,1.0,1.89,1.89,1.89,0.127,0.127,1.89,1.89,1.89,0.256};
	static float datay2[]={0,1.73,1.74,1.74,1.77,0.9,0.9,0.9,13.204,13.204,0.88,0.88,0.88,6.645};
	static float dataz2[]={0,1.15,0.9,0.9,0.75,1.1,1.1,1.1,0.8,0.8,0.86,0.86,0.86,0.7};

	if ( !strcmp(types[type].name, "BNT") || !strcmp(types[type].name, "KIN")) {
		statINV=raw_data[STATUS_A] & ONLINE;
		statAVR=raw_data[STATUS_A] & AVR_ON;
		statAVRMode=raw_data[STATUS_A] & AVR_MODE;
	}
	if ( !strcmp(types[type].name, "BNT") && raw_data[MODELNUMBER]%16 > 7 ) {
		if (statINV==0) {
			if (statAVR==0){
				tmp=2.2*raw_data[OUTPUT_VOLTAGE]-24;
			} else {
				if (statAVRMode > 0)
					tmp=(2.2*raw_data[OUTPUT_VOLTAGE]-24)*31/27;
				else
					tmp=(2.22*raw_data[OUTPUT_VOLTAGE]-24)*27/31;
			}
		} else {
			t=raw_data[OUTPUT_FREQUENCY]/2;
			tmp=(1.965*raw_data[15])*(1.965*raw_data[15])*(t-raw_data[OUTPUT_VOLTAGE])/t;
			if (tmp>0)
				tmp=sqrt(tmp);
			else
				tmp=0.0;
		}
	} else if ( !strcmp(types[type].name, "KIN")) {
		model=KINmodels[raw_data[MODELNUMBER]/16];
		if (statINV == 0) {
			if (statAVR == 0) {
				/* FIXME: miss test "if (iUPS == 1) {" */
				if (linevoltage >= 200) {
					if (model <= 625)
						tmp = 1.79*raw_data[OUTPUT_VOLTAGE] + 3.35;
					else if (model<2000)
						tmp = 1.61*raw_data[OUTPUT_VOLTAGE];
					else
						tmp = 1.625*raw_data[OUTPUT_VOLTAGE];
				} else {
					if (model <= 625)
						tmp = 0.89 * raw_data[OUTPUT_VOLTAGE] + 6.18;
					else if (model<2000)
						tmp = 1.61 * raw_data[OUTPUT_VOLTAGE] / 2.0;
					else
						tmp = 1.625 * raw_data[OUTPUT_VOLTAGE] / 2.0;
				}
			}
			else if (statAVR == 1) {
				/* FIXME: miss test "if ((iUPS == 1) || (iUPS == 13)) {" */
				if (linevoltage >= 200) {
					if (model <= 525)
						tmp = 2.07 * raw_data[OUTPUT_VOLTAGE];
					else if (model == 625)
						tmp = 2.07 * raw_data[OUTPUT_VOLTAGE]+5;
					else if (model < 2000)
						tmp = 1.87 * raw_data[OUTPUT_VOLTAGE];
					else
						tmp = 1.87 * raw_data[OUTPUT_VOLTAGE];
				} else {
					if (model <= 625)
						tmp = 2.158 * raw_data[OUTPUT_VOLTAGE] / 2.0;
					else if (model < 2000)
						tmp = 1.842 * raw_data[OUTPUT_VOLTAGE] / 2.0;
					else
						tmp = 1.875 * raw_data[OUTPUT_VOLTAGE] / 2.0;
				}
			} else {
				/* FIXME: miss test "if ((iUPS == 1) || (iUPS == 13)) {" */
				if (linevoltage >= 200) {
					if (model == 625)
						tmp = 1.571 * raw_data[OUTPUT_VOLTAGE];
					else if (model < 2000)
						tmp = 1.37 * raw_data[OUTPUT_VOLTAGE];
					else
						tmp = 1.4 * raw_data[OUTPUT_VOLTAGE];
				} else {
					if (model <= 625)
						tmp = 1.635 * raw_data[OUTPUT_VOLTAGE] / 2.0;
					else if (model < 2000)
						tmp = 1.392 * raw_data[OUTPUT_VOLTAGE] / 2.0;
					else
						tmp = 1.392 * raw_data[OUTPUT_VOLTAGE] / 2.0;
				}
			}
		} else {
			/* FIXME: miss test "if ((iUPS == 1) && (T != 0))" */
			if (linevoltage < 200) {
				rdatax = datax1[raw_data[MODELNUMBER]/16];
				rdatay = datay1[raw_data[MODELNUMBER]/16];
				rdataz = dataz1[raw_data[MODELNUMBER]/16];
			} else {
				rdatax = datax2[raw_data[MODELNUMBER]/16];
				rdatay = datay2[raw_data[MODELNUMBER]/16];
				rdataz = dataz2[raw_data[MODELNUMBER]/16+1];
			}

			boostdata = 1.0 + statAVR * 20.0 / 135.0;
			t = raw_data[OUTPUT_FREQUENCY]/2;
			tmp = 0;
			if (model > 625){
				tmp=(raw_data[BATTERY_CHARGE]*rdatax)*(raw_data[BATTERY_CHARGE]*rdatax)*
					(t-raw_data[OUTPUT_VOLTAGE])/t;
				if (tmp>0)
					/* Casts below try to avoid potential multiplication overflow */
					tmp=(float)( (double)sqrt(tmp)*rdatay*boostdata -
						(double)raw_data[UPS_LOAD]*rdataz*boostdata );
			} else {
				tmp=(raw_data[BATTERY_CHARGE]*rdatax-raw_data[UPS_LOAD]*rdataz)*
					(raw_data[BATTERY_CHARGE]*rdatax-raw_data[UPS_LOAD]*rdataz)*
					(t-raw_data[OUTPUT_VOLTAGE])/t;
				if (tmp>0)
					tmp=sqrt(tmp)*rdatay;
			}
			/* FIXME: may miss a last processing with ErrorVal = 5 | 10 */
		}
	} else if ( !strcmp(types[type].name, "IMP") || !strcmp(types[type].name, "OPTI")) {
		tmp = raw_data[OUTPUT_VOLTAGE]*2.0;
	} else {
		tmp = linevoltage >= 220
			? types[type].voltage[0] * raw_data[OUTPUT_VOLTAGE]
			  + types[type].voltage[1]
			: types[type].voltage[2] * raw_data[OUTPUT_VOLTAGE]
			  + types[type].voltage[3];
	}
	if (tmp<0) tmp=0.0;
	return tmp;
}

static float input_freq(void)
{
	if ( !strcmp(types[type].name, "BNT") || !strcmp(types[type].name, "KIN"))
		return 4807.0/raw_data[INPUT_FREQUENCY];
	else if ( !strcmp(types[type].name, "IMP") || !strcmp(types[type].name, "OPTI"))
		return raw_data[INPUT_FREQUENCY];
	return raw_data[INPUT_FREQUENCY] ?
		1.0 / (types[type].freq[0] *
				raw_data[INPUT_FREQUENCY] +
						types[type].freq[1]) : 0;
}

static float output_freq(void)
{
	if ( !strcmp(types[type].name, "BNT") || !strcmp(types[type].name, "KIN"))
		return 4807.0/raw_data[OUTPUT_FREQUENCY];
	else if ( !strcmp(types[type].name, "IMP") || !strcmp(types[type].name, "OPTI"))
		return raw_data[OUTPUT_FREQUENCY];
	return raw_data[OUTPUT_FREQUENCY] ?
		1.0 / (types[type].freq[0] *
				raw_data[OUTPUT_FREQUENCY] +
						types[type].freq[1]) : 0;
}

static float load_level(void)
{
	unsigned int statINV,model,voltage;
	int load425[]={99,88,84,80,84,84,84,86,86,81,76};
	int load525[]={127,113,106,100,106,106,106,109,109,103,97};
	int load625[]={131,115,107,103,107,107,107,110,110,105,99};
	int load2k[] ={94,94,94,94,94,94,94,120,120,115,110};
	int load425i[]={60,54,51,48,51,51,51,53,53,50,48};
	int load525i[]={81,72,67,62,67,67,67,65,65,62,59};
	int load625i[]={79,70,67,64,67,67,67,65,65,61,58};
	int load2ki[] ={84,77,74,70,74,74,74,77,77,74,70};
	int load400[]={1,1,1,1,1,1,1,1,88,83,87};
	int load500[]={1,1,1,1,1,1,1,1,108,103,98};
	int load600[]={1,1,1,1,1,1,1,1,128,123,118};
	int load400i[]={1,1,1,1,1,1,1,1,54,52,49};
	int load500i[]={1,1,1,1,1,1,1,1,66,64,61};
	int load600i[]={1,1,1,1,1,1,1,1,86,84,81};
	int load801i[]={1,1,1,1,1,1,1,1,44,42,40};
	int load1000i[]={1,1,1,1,1,1,1,1,56,54,52};
	int load1200i[]={1,1,1,1,1,1,1,1,76,74,72};

	if ( !strcmp(types[type].name, "BNT") && raw_data[MODELNUMBER]%16 > 7 ) {
		statINV=raw_data[STATUS_A] & ONLINE;
		voltage=raw_data[MODELNUMBER]%16;
		model=BNTmodels[raw_data[MODELNUMBER]/16];
		if (statINV==0){
			if (model==400 || model==801)
				return raw_data[UPS_LOAD]*110.0/load400[voltage];
			else if (model==600 || model==1200)
				return raw_data[UPS_LOAD]*110.0/load600[voltage];
			else
				return raw_data[UPS_LOAD]*110.0/load500[voltage];
		} else {
			switch (model) {
				case 400: return raw_data[UPS_LOAD]*110.0/load400i[voltage];
				case 500:
				case 800: return raw_data[UPS_LOAD]*110.0/load500i[voltage];
				case 600: return raw_data[UPS_LOAD]*110.0/load600i[voltage];
				case 801: return raw_data[UPS_LOAD]*110.0/load801i[voltage];
				case 1200: return raw_data[UPS_LOAD]*110.0/load1200i[voltage];
				case 1000:
				case 1500:
				case 2000: return raw_data[UPS_LOAD]*110.0/load1000i[voltage];
				default: break;
			}
		}
	} else if (!strcmp(types[type].name, "KIN")) {
		statINV=raw_data[STATUS_A] & ONLINE;
		voltage=raw_data[MODELNUMBER]%16;
		model=KINmodels[raw_data[MODELNUMBER]/16];
		if (statINV==0){
			if (model==425) return raw_data[UPS_LOAD]*110.0/load425[voltage];
			if (model==525) return raw_data[UPS_LOAD]*110.0/load525[voltage];
			if (model==625) return raw_data[UPS_LOAD]*110.0/load625[voltage];
			if (model<2000) return raw_data[UPS_LOAD]*1.13;
			return raw_data[UPS_LOAD]*110.0/load2k[voltage];
		} else {
			if (model==425) return raw_data[UPS_LOAD]*110.0/load425i[voltage];
			if (model==525) return raw_data[UPS_LOAD]*110.0/load525i[voltage];
			if (model==625) return raw_data[UPS_LOAD]*110.0/load625i[voltage];
			if (model<2000) return raw_data[UPS_LOAD]*1.66;
			return raw_data[UPS_LOAD]*110.0/load2ki[voltage];
		}
	} else if ( !strcmp(types[type].name, "IMP") || !strcmp(types[type].name, "OPTI")) {
		return raw_data[UPS_LOAD];
	}
	return (raw_data[STATUS_A] & MAINS_FAILURE) ?
		types[type].loadpct[0] * raw_data[UPS_LOAD] +
									types[type].loadpct[1] :
		types[type].loadpct[2] * raw_data[UPS_LOAD] +
									types[type].loadpct[3];
}

static float batt_level(void)
{
	int bat0,bat29,bat100;
	unsigned int model;
	float battval;

	if ( !strcmp(types[type].name, "BNT") ) {
		bat0=157;
		bat29=165;
		bat100=193;
		battval=(raw_data[UPS_LOAD])/4+raw_data[BATTERY_CHARGE];
		if (battval<=bat0)
			return 0.0;
		if (battval<=bat29)
			return (battval-bat0)*30.0/(bat29-bat0);
		if (battval<=bat100)
			return 30.0+(battval-bat29)*70.0/(bat100-bat29);
		return 100.0;
	}
	if ( !strcmp(types[type].name, "KIN")) {
		model=KINmodels[raw_data[MODELNUMBER]/16];
		if (model>=800 && model<=2000){
			battval=(raw_data[BATTERY_CHARGE]-165.0)*2.6;
			if (raw_data[STATUS_A] & ONLINE)
				return battval+raw_data[UPS_LOAD];
			if (battval>7)
				return battval-6;
			return battval;
		} else if (model<=625){
			battval=raw_data[UPS_LOAD]/4.0+raw_data[BATTERY_CHARGE];
			bat0=169;
			bat29=176;
			bat100=204;
		} else {
			battval=raw_data[UPS_LOAD]/4.0-raw_data[UPS_LOAD]/32.0+raw_data[BATTERY_CHARGE];
			bat0=175;
			bat29=182;
			bat100=209;
		}
		if (battval<=bat0)
			return 0;
		if (battval>bat0 && battval<=bat29)
			return (battval-bat0)*30.0/(bat29-bat0);
		if (battval>bat29 && battval<=bat100)
			return 30.0+(battval-bat29)*70.0/(bat100-bat29);
		return 100;
	}
	if ( !strcmp(types[type].name, "IMP") || !strcmp(types[type].name, "OPTI"))
		return raw_data[BATTERY_CHARGE];
	return (raw_data[STATUS_A] & ONLINE) ? /* Are we on battery power? */
		/* Yes */
		types[type].battpct[0] * raw_data[BATTERY_CHARGE] +
			types[type].battpct[1] * load_level() + types[type].battpct[2] :
		/* No */
		types[type].battpct[3] * raw_data[BATTERY_CHARGE] +
			types[type].battpct[4];
}

/*
 * global used functions
 */

/* update information */
void upsdrv_updateinfo(void)
{
	char	val[32];

	/* Auto-detect or use configured protocol */
	if (!detect_protocol()) {
		/* Both protocols failed */
		dstate_datastale();
		return;
	}
	
	/* Use COM2 protocol if detected */
	if (current_protocol == PROTOCOL_COM2) {
		com2_update_vars();
		dstate_dataok();
		return;
	}
	
	/* COM1 protocol (original implementation) */
	if (!ups_getinfo()) {
		/* https://github.com/networkupstools/nut/issues/356 */
		upsdebugx(1, "%s: failed to ups_getinfo() once, retrying for slower devices", __func__);
		if (!ups_getinfo()) {
			return;
		}
	}

	/* input.frequency */
	upsdebugx(3, "input.frequency   (raw data): [raw: %u]",
		raw_data[INPUT_FREQUENCY]);
	dstate_setinfo("input.frequency", "%02.2f", input_freq());
	upsdebugx(2, "input.frequency: %s", dstate_getinfo("input.frequency"));

	/* output.frequency */
	upsdebugx(3, "output.frequency   (raw data): [raw: %u]",
		raw_data[OUTPUT_FREQUENCY]);
	dstate_setinfo("output.frequency", "%02.2f", output_freq());
	upsdebugx(2, "output.frequency: %s", dstate_getinfo("output.frequency"));

	/* ups.load */
	upsdebugx(3, "ups.load  (raw data): [raw: %u]",
		raw_data[UPS_LOAD]);
	dstate_setinfo("ups.load", "%03.1f", load_level());
	upsdebugx(2, "ups.load: %s", dstate_getinfo("ups.load"));

	/* battery.charge */
	upsdebugx(3, "battery.charge (raw data): [raw: %u]",
		raw_data[BATTERY_CHARGE]);
	dstate_setinfo("battery.charge", "%03.1f", batt_level());
	upsdebugx(2, "battery.charge: %s", dstate_getinfo("battery.charge"));

	/* input.voltage */
	upsdebugx(3, "input.voltage (raw data): [raw: %u]",
		raw_data[INPUT_VOLTAGE]);
	dstate_setinfo("input.voltage", "%03.1f",input_voltage());
	upsdebugx(2, "input.voltage: %s", dstate_getinfo("input.voltage"));

	/* output.voltage */
	upsdebugx(3, "output.voltage (raw data): [raw: %u]",
		raw_data[OUTPUT_VOLTAGE]);
	dstate_setinfo("output.voltage", "%03.1f",output_voltage());
	upsdebugx(2, "output.voltage: %s", dstate_getinfo("output.voltage"));

	status_init();

	*val = 0;
	if (!(raw_data[STATUS_A] & MAINS_FAILURE)) {
		!(raw_data[STATUS_A] & OFF) ?
			status_set("OL") : status_set("OFF");
	} else {
		status_set("OB");
	}

	if (raw_data[STATUS_A] & LOW_BAT)  status_set("LB");

	if (raw_data[STATUS_A] & AVR_ON) {
		input_voltage() < linevoltage ?
			status_set("BOOST") : status_set("TRIM");
	}

	if (raw_data[STATUS_A] & OVERLOAD)  status_set("OVER");

	if (raw_data[STATUS_B] & BAD_BAT)  status_set("RB");

	if (raw_data[STATUS_B] & TEST)  status_set("TEST");

	status_commit();

	upsdebugx(2, "STATUS: %s", dstate_getinfo("ups.status"));
	dstate_dataok();
}

/* shutdown UPS */
void upsdrv_shutdown(void)
{
	/* Only implement "shutdown.default"; do not invoke
	 * general handling of other `sdcommands` here */

	int	ret = -1;

	if (!device_sdcommands) {
		/* default: power down the attached load immediately */
		printf("Forced UPS shutdown (and wait for power)...\n");
	}

	ret = do_loop_shutdown_commands("shutdown.return", NULL);
	if (handling_upsdrv_shutdown > 0)
		set_exit_flag(ret == STAT_INSTCMD_HANDLED ? EF_EXIT_SUCCESS : EF_EXIT_FAILURE);
}

/* initialize UPS */
void upsdrv_initups(void)
{
	int tmp;
	unsigned int i;
	const char *val;

	/* Parse COM2 protocol options */
	if (testvar("protocol_mode")) {
		val = getval("protocol_mode");
		if (!strcasecmp(val, "com1")) {
			configured_protocol = PROTOCOL_COM1;
			upsdebugx(1, "Protocol mode set to: COM1");
		} else if (!strcasecmp(val, "com2")) {
			configured_protocol = PROTOCOL_COM2;
			upsdebugx(1, "Protocol mode set to: COM2");
		} else if (!strcasecmp(val, "auto")) {
			configured_protocol = PROTOCOL_AUTO;
			upsdebugx(1, "Protocol mode set to: AUTO");
		} else {
			fatalx(EXIT_FAILURE, "Invalid protocol_mode '%s' (must be auto, com1, or com2)", val);
		}
	}
	
	if (testvar("com2_command")) {
		val = getval("com2_command");
		if (!strcasecmp(val, "Q1")) {
			com2_command = "Q1";
		} else if (!strcasecmp(val, "DQ1")) {
			com2_command = "DQ1";
		} else {
			fatalx(EXIT_FAILURE, "Invalid com2_command '%s' (must be Q1 or DQ1)", val);
		}
		upsdebugx(1, "COM2 command set to: %s", com2_command);
	}
	
	if (testvar("event_hold")) {
		char *endptr = NULL;
		long tmpval;
		val = getval("event_hold");
		
		tmpval = strtol(val, &endptr, 10);
		if (endptr == val || *endptr != '\0' || tmpval <= 0 || tmpval > 300) {
			fatalx(EXIT_FAILURE, "Invalid event_hold '%s' (must be 1-300 seconds)", val);
		}
		event_hold_time = (unsigned int)tmpval;
		upsdebugx(1, "Event hold time set to: %u seconds", event_hold_time);
	}
	
	if (testvar("allow_control")) {
		val = getval("allow_control");
		if (!strcasecmp(val, "yes") || !strcasecmp(val, "true") || !strcasecmp(val, "1")) {
			allow_control = 1;
			upsdebugx(1, "Dangerous control commands enabled");
			upslogx(LOG_WARNING, "allow_control=yes - dangerous shutdown commands are enabled");
		} else {
			allow_control = 0;
		}
	}
	
	/* Initialize event tracking */
	memset(&event_tracking, 0, sizeof(event_tracking));
	memset(&com2_current, 0, sizeof(com2_current));

	/* check manufacturer name from arguments */
	if (testvar("manufacturer"))
		manufacturer = getval("manufacturer");

	/* check model name from arguments */
	if (testvar("modelname"))
		modelname = getval("modelname");

	/* check serial number from arguments */
	if (testvar("serialnumber"))
		serialnumber = getval("serialnumber");

	/* get and check type */
	if (testvar("type")) {
		for (
			i = 0;
			i < NUM_OF_SUBTYPES  &&  strcmp(types[i].name, getval("type"));
			i++) ;
		if (i >= NUM_OF_SUBTYPES) {
			printf("Given UPS type '%s' isn't valid!\n", getval("type"));
			exit (1);
		}
		type = i;
	}

	/* check line voltage from arguments */
	if (testvar("linevoltage")) {
		tmp = atoi(getval("linevoltage"));
		if (! ( (tmp >= 200 && tmp <= 240) || (tmp >= 100 && tmp <= 120) ) ) {
			printf("Given line voltage '%d' is out of range (100-120 or 200-240 V)\n", tmp);
			exit (1);
		}
		linevoltage = (unsigned int) tmp;
	}

	if (testvar("numOfBytesFromUPS")) {
		tmp = atoi(getval("numOfBytesFromUPS"));
		if (! (tmp > 0 && tmp <= MAX_NUM_OF_BYTES_FROM_UPS) ) {
			printf("Given numOfBytesFromUPS '%d' is out of range (1 to %d)\n",
				tmp, MAX_NUM_OF_BYTES_FROM_UPS);
			exit (1);
		}
		types[type].num_of_bytes_from_ups = (unsigned char) tmp;
	}

	if (testvar("methodOfFlowControl")) {
		for (
			i = 0;
			i < NUM_OF_SUBTYPES
			&& strcmp(types[i].flowControl.name, getval("methodOfFlowControl"));
			i++) ;
		if (i >= NUM_OF_SUBTYPES) {
			printf("Given methodOfFlowControl '%s' isn't valid!\n",
				getval("methodOfFlowControl"));
			exit (1);
		}
		types[type].flowControl = types[i].flowControl;
	}

	if (testvar("validationSequence")
	 && sscanf(getval("validationSequence"),
		"{{%u,%x},{%u,%x},{%u,%x}}",
		&types[type].validation[0].index_of_byte,
		&types[type].validation[0].required_value,
		&types[type].validation[1].index_of_byte,
		&types[type].validation[1].required_value,
		&types[type].validation[2].index_of_byte,
		&types[type].validation[2].required_value
		) < 6
	) {
		printf("Given validationSequence '%s' isn't valid!\n",
			getval("validationSequence"));
		exit (1);
	}

	/* NOTE: %hhu is not supported before C99; that would need reading
	 * arguments into an uint as %u, checking range and casting */
	if (testvar("shutdownArguments")
	 && sscanf(getval("shutdownArguments"),
		"{{%hhu,%hhu},%c}",
		&types[type].shutdown_arguments.delay[0],
		&types[type].shutdown_arguments.delay[1],
		&types[type].shutdown_arguments.minutesShouldBeUsed
		) < 3
	) {
		printf("Given shutdownArguments '%s' isn't valid!\n",
			getval("shutdownArguments"));
		exit (1);
	}

	if (testvar("frequency")
	 && sscanf(getval("frequency"),
		"{%f,%f}",
		&types[type].freq[0], &types[type].freq[1]
		) < 2
	) {
		printf("Given frequency '%s' isn't valid!\n",
			getval("frequency"));
		exit (1);
	}

	if (testvar("loadPercentage")
	 && sscanf(getval("loadPercentage"),
		"{%f,%f,%f,%f}",
		&types[type].loadpct[0], &types[type].loadpct[1],
		&types[type].loadpct[2], &types[type].loadpct[3]
		) < 4
	) {
		printf("Given loadPercentage '%s' isn't valid!\n",
			getval("loadPercentage"));
		exit (1);
	}

	if (testvar("batteryPercentage")
	 && sscanf(getval("batteryPercentage"),
		"{%f,%f,%f,%f,%f}",
		&types[type].battpct[0], &types[type].battpct[1],
		&types[type].battpct[2], &types[type].battpct[3],
		&types[type].battpct[4]
		) < 5
	) {
		printf("Given batteryPercentage '%s' isn't valid!\n",
			getval("batteryPercentage"));
		exit (1);
	}

	if (testvar("voltage")
	 && sscanf(getval("voltage"),
		"{%f,%f,%f,%f}",
		&types[type].voltage[0], &types[type].voltage[1],
		&types[type].voltage[2], &types[type].voltage[3]
		) < 4
	) {
		printf("Given voltage '%s' isn't valid!\n", getval("voltage"));
		exit (1);
	}

	/* open serial port */
	upsfd = ser_open(device_path);
	ser_set_speed(upsfd, device_path, B1200);

	/* setup flow control */
	types[type].flowControl.setup_flow_control();
}

/* display help */
void upsdrv_help(void)
{
	/*               1         2         3         4         5         6         7         8 */
	/*      12345678901234567890123456789012345678901234567890123456789012345678901234567890 MAX */
	printf("\n");
	printf("Specify UPS information in the ups.conf file.\n");
	printf("\n");
	printf("COM2 Protocol Options (ASCII protocol at 2400 baud):\n");
	printf(" protocol_mode: Protocol to use: 'auto', 'com1', 'com2' (default: 'auto')\n");
	printf("                 auto: tries COM1 first, then COM2 if COM1 fails\n");
	printf("                 com1: binary protocol at 1200 baud (original)\n");
	printf("                 com2: ASCII protocol at 2400 baud (newer models)\n");
	printf(" com2_command:  Query command for COM2: 'Q1' or 'DQ1' (default: 'Q1')\n");
	printf(" event_hold:    Event latch time in seconds (default: 20, range: 1-300)\n");
	printf(" allow_control: Enable dangerous commands: 'yes' or 'no' (default: 'no')\n");
	printf("                When 'yes', enables shutdown.stayoff.dangerous command\n");
	printf("\n");
	printf("COM1 Protocol Options (binary protocol at 1200 baud):\n");
	printf(" type:          Type of UPS: 'Trust','Egys','KP625AP','IMP','KIN','BNT',\n");
	printf("                 'BNT-other', 'OPTI' (default: 'Trust')\n");
	printf("                'BNT-other' is a special type intended for BNT 100-120V models,\n");
	printf("                 but can be used to override ALL models.\n");
	printf("You can additional specify these variables:\n");
	printf(" manufacturer:  Manufacturer name (default: 'PowerCom')\n");
	printf(" modelname:     Model name (default: 'Unknown' or autodetected)\n");
	printf(" serialnumber:  Serial number (default: Unknown)\n");
	printf(" shutdownArguments: 3 delay arguments for the shutdown operation:\n");
	printf("                 {{Minutes,Seconds},UseMinutes?}\n");
	printf("                where Minutes and Seconds are integer, UseMinutes? is either\n");
	printf("                 'y' or 'n'.\n");
	printf("You can specify these variables if not automagically detected for types\n");
	printf("                'IMP','KIN','BNT'\n");
	printf(" linevoltage:   Line voltage: 110-120 or 220-240 (default: 230)\n");
	printf(" numOfBytesFromUPS: Number of bytes in a UPS frame: 16 is common, 11 for 'Trust'\n");
	printf(" methodOfFlowControl: Flow control method for UPS:\n");
	printf("                'dtr0rts1', 'dtr1' or 'no_flow_control'\n");
	printf(" validationSequence: 3 pairs of validation values: {{I,V},{I,V},{I,V}}\n");
	printf("                where I is the index into BytesFromUPS (see numOfBytesFromUPS)\n");
	printf("                  and V is the value for the ByteIndex to match.\n");
	printf(" frequency:     Input & Output Frequency conversion values: {A, B}\n");
	printf("                 used in function: 1/(A*x+B)\n");
	printf("                If the raw value x IS the frequency, then A=1/(x^2), B=0\n");
	printf(" loadPercentage: Load conversion values for Battery and Line load: {BA,BB,LA,LB}\n");
	printf("                 used in function: A*x+B\n");
	printf("                If the raw value x IS the Load Percent, then A=1, B=0\n");
	printf(" batteryPercentage: Battery conversion values for Battery and Line power:\n");
	printf("                 {A,B,C,D,E}\n");
	printf("                 used in functions: (Battery) A*x+B*y+C, (Line) D*x+E\n");
	printf("                If the raw value x IS the Battery Percent, then\n");
	printf("                 A=1, B=0, C=0, D=1, E=0\n");
	printf(" voltage:       Voltage conversion values for 240 and 120 voltage:\n");
	printf("                 {240A,240B,120A,120B}\n");
	printf("                 used in function: A*x+B\n");
	printf("                If the raw value x IS HALF the Voltage, then A=2, B=0\n");
	printf(" nobt:          Flag to skip battery check on init/startup.\n\n");

	printf("Example for BNT1500AP in ups.conf:\n");
	printf("[BNT1500AP]\n");
	printf("    driver = powercom\n");
	printf("    port = /dev/ttyS0\n");
	printf("    desc = \"PowerCom BNT 1500 AP\"\n");
	printf("    manufacturer = PowerCom\n");
	printf("    modelname = BNT1500AP\n");
	printf("    serialnumber = 13245678900\n");
	printf("    type = BNT-other\n");
	printf("#   linevoltage = 120\n");
	printf("#   numOfBytesFromUPS = 16\n");
	printf("#   methodOfFlowControl = no_flow_control\n");
	printf("#   validationSequence = {{8,0},{8,0},{8,0}}\n");
	printf("#   shutdownArguments = {{1,30},y}\n");
	printf("#   frequency = {0.00027778,0.0000}\n");
	printf("#   loadPercentage = {1.0000,0.0,1.0000,0.0}\n");
	printf("#   batteryPercentage = {1.0000,0.0000,0.0000,1.0000,0.0000}\n");
	printf("#   voltage = {2.0000,0.0000,2.0000,0.0000}\n");
	printf("    nobt\n");
	printf("\n");
	printf("Example for COM2 protocol:\n");
	printf("[PowerCom-COM2]\n");
	printf("    driver = powercom\n");
	printf("    port = /dev/ttyS0\n");
	printf("    desc = \"PowerCom Imperial with COM2 protocol\"\n");
	printf("    protocol_mode = com2\n");
	printf("    com2_command = Q1\n");
	printf("    event_hold = 20\n");
	printf("#   allow_control = yes  # Enable dangerous commands\n");
	return;
}

/* optionally tweak prognames[] entries */
void upsdrv_tweak_prognames(void)
{
}

/* initialize information */
void upsdrv_initinfo(void)
{
	unsigned int	model = 0;
	static char	buf[20];

	/* Setup Model and LineVoltage */
	if (!strncmp(types[type].name, "BNT",3) || !strcmp(types[type].name, "KIN") || !strcmp(types[type].name, "IMP") || !strcmp(types[type].name, "OPTI")) {
		if (!ups_getinfo()) return;
		/* Give "BNT-other" a chance! */
		if (raw_data[MODELNAME]==0x42 || raw_data[MODELNAME]==0x4B || raw_data[MODELNAME]==0x4F){
			/* Give "IMP" a chance also! */
			if (raw_data[UPSVERSION]==0xFF){
				types[type].name="IMP";
				model=IMPmodels[raw_data[MODELNUMBER]/16];
			}
			else {
				model=BNTmodels[raw_data[MODELNUMBER]/16];
				if (!strcmp(types[type].name, "BNT-other"))
					types[type].name="BNT-other";
				else if (raw_data[MODELNAME]==0x42)
					types[type].name="BNT";
				else if (raw_data[MODELNAME]==0x4B){
					types[type].name="KIN";
					model=KINmodels[raw_data[MODELNUMBER]/16];
				} else if (raw_data[MODELNAME]==0x4F){
					types[type].name="OPTI";
					model=OPTImodels[raw_data[MODELNUMBER]/16];
				}
			}
		}
		else if (raw_data[UPSVERSION]==0xFF){
			types[type].name="IMP";
			model=IMPmodels[raw_data[MODELNUMBER]/16];
		}
		linevoltage=voltages[raw_data[MODELNUMBER]%16];
		if (!strcmp(types[type].name, "OPTI")) {
			snprintf(buf,sizeof(buf),"%s-%u",types[type].name, model);
		} else {
			snprintf(buf,sizeof(buf),"%s-%uAP",types[type].name, model);
		}
		if (!strcmp(modelname, "Unknown"))
			modelname=buf;
		upsdebugx(1, "Detected: %s , %uV", buf, linevoltage);
		if (testvar("nobt") || dstate_getinfo("driver.flag.nobt")) {
			upslogx(LOG_NOTICE, "nobt flag set, skipping battery test as requested");
		}
		else {
			upslogx(LOG_NOTICE, "nobt flag not set, performing battery test as requested");
			if (ser_send_char (upsfd, BATTERY_TEST) != 1) {
				upslogx(LOG_NOTICE, "Write error: failed to send battery test command to UPS!");
				dstate_datastale();
				return;
			}
		}
	}

	upsdebugx(1, "Values of arguments:");
	upsdebugx(1, " manufacturer            : '%s'", manufacturer);
	upsdebugx(1, " model name              : '%s'", modelname);
	upsdebugx(1, " serial number           : '%s'", serialnumber);
	upsdebugx(1, " line voltage            : '%u'", linevoltage);
	upsdebugx(1, " type                    : '%s'", types[type].name);
	upsdebugx(1, " number of bytes from UPS: '%u'",
		types[type].num_of_bytes_from_ups);
	upsdebugx(1, " method of flow control  : '%s'",
		types[type].flowControl.name);
	upsdebugx(1, " validation sequence: '{{%u,%#x},{%u,%#x},{%u,%#x}}'",
		types[type].validation[0].index_of_byte,
		types[type].validation[0].required_value,
		types[type].validation[1].index_of_byte,
		types[type].validation[1].required_value,
		types[type].validation[2].index_of_byte,
		types[type].validation[2].required_value);
	upsdebugx(1, " shutdown arguments: '{{%u,%u},%c}'",
		types[type].shutdown_arguments.delay[0],
		types[type].shutdown_arguments.delay[1],
		types[type].shutdown_arguments.minutesShouldBeUsed);
	if ( strcmp(types[type].name, "KIN") && strcmp(types[type].name, "BNT") && strcmp(types[type].name, "IMP")) {
		upsdebugx(1, " frequency calculation coefficients: '{%f,%f}'",
			types[type].freq[0], types[type].freq[1]);
		upsdebugx(1, " load percentage calculation coefficients: "
			"'{%f,%f,%f,%f}'",
			types[type].loadpct[0], types[type].loadpct[1],
			types[type].loadpct[2], types[type].loadpct[3]);
		upsdebugx(1, " battery percentage calculation coefficients: "
			"'{%f,%f,%f,%f,%f}'",
			types[type].battpct[0], types[type].battpct[1],
			types[type].battpct[2], types[type].battpct[3],
			types[type].battpct[4]);
		upsdebugx(1, " voltage calculation coefficients: '{%f,%f}'",
			types[type].voltage[2], types[type].voltage[3]);
	}

	/* write constant data for this model */
	dstate_setinfo ("ups.mfr", "%s", manufacturer);
	dstate_setinfo ("ups.model", "%s", modelname);
	dstate_setinfo ("ups.serial", "%s", serialnumber);
	dstate_setinfo ("ups.model.type", "%s", types[type].name);
	dstate_setinfo ("input.voltage.nominal", "%u", linevoltage);

	/* now add the instant commands */
	dstate_addcmd ("test.battery.start");
	dstate_addcmd ("shutdown.return");
	dstate_addcmd ("shutdown.stayoff");
	
	/* Add COM2-specific commands if protocol supports them */
	if (configured_protocol == PROTOCOL_COM2 || configured_protocol == PROTOCOL_AUTO) {
		dstate_addcmd ("test.battery.stop");
		dstate_addcmd ("beeper.toggle");
		if (allow_control) {
			dstate_addcmd ("shutdown.stayoff.dangerous");
			upslogx(LOG_WARNING, "Dangerous shutdown commands enabled");
		}
	}
	
	upsh.instcmd = instcmd;
}

/* define possible arguments */
void upsdrv_makevartable(void)
{
		/*        1         2         3         4         5         6         7         8 */
		/*2345678901234567890123456789012345678901234567890123456789012345678901234567890 MAX */
	/* COM2 protocol options */
	addvar(VAR_VALUE, "protocol_mode",
		"Protocol mode: 'auto', 'com1', or 'com2' (default: auto)");
	addvar(VAR_VALUE, "com2_command",
		"COM2 query command: 'Q1' or 'DQ1' (default: Q1)");
	addvar(VAR_VALUE, "event_hold",
		"Event latch duration in seconds (default: 20, range: 1-300)");
	addvar(VAR_VALUE, "allow_control",
		"Enable dangerous control commands: 'yes' or 'no' (default: no)");
	
	/* Original COM1 options */
	addvar(VAR_VALUE, "type",
		"Type of UPS: 'Trust','Egys','KP625AP','IMP','KIN','BNT','BNT-other','OPTI'\n"
		" (default: 'Trust')");
	addvar(VAR_VALUE, "manufacturer",
		"Manufacturer name (default: 'PowerCom')");
	addvar(VAR_VALUE, "modelname",
		"Model name [cannot be detected] (default: Unknown)");
	addvar(VAR_VALUE, "serialnumber",
		"Serial number [cannot be detected] (default: Unknown)");
	addvar(VAR_VALUE, "shutdownArguments",
		"Delay values for shutdown: Minutes, Seconds, UseMinutes?'y'or'n'");
	addvar(VAR_VALUE, "linevoltage",
		"Line voltage 110-120 or 220-240 V (default: 230)");
	addvar(VAR_VALUE, "numOfBytesFromUPS",
		"The number of bytes in a UPS frame");
	addvar(VAR_VALUE, "methodOfFlowControl",
		"Flow control method for UPS: 'dtr0rts1' or 'no_flow_control'");
	addvar(VAR_VALUE, "validationSequence",
		"Validation values: ByteIndex, ByteValue x 3");
	if ( strcmp(types[type].name, "KIN") && strcmp(types[type].name, "BNT") && strcmp(types[type].name, "IMP")) {
		addvar(VAR_VALUE, "frequency",
			"Frequency conversion values: FreqFactor, FreqConst");
		addvar(VAR_VALUE, "loadPercentage",
			"Load conversion values: OffFactor, OffConst, OnFactor, OnConst");
		addvar(VAR_VALUE, "batteryPercentage",
			"Battery conversion values: OffFactor, LoadFactor, OffConst, OnFactor, OnConst");
		addvar(VAR_VALUE, "voltage",
			"Voltage conversion values: 240VFactor, 240VConst, 120VFactor, 120VConst");
		addvar(VAR_FLAG, "nobt",
			"Disable battery test at driver init/startup");
	}
}

void upsdrv_cleanup(void)
{
	ser_close(upsfd, device_path);
}
