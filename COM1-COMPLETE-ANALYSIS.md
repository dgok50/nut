# COM1 Protocol - Complete Analysis and Implementation

## Overview

This document provides a comprehensive analysis of the PowerCom COM1 protocol based on thorough study of ConCOM1Get.java and ConCOM1Set.java from the official UPSMON Java driver.

## User Feedback

**Russian:** "а почему ты не изучил ConCOM1Get.java, изучи всё что связанно с COM1 более тщательно и проверь что всё реализовано ты же пропустил пищалку, проверь все маски и атрибуты!"

**Translation:** "why didn't you study ConCOM1Get.java, study everything related to COM1 more carefully and check that everything is implemented, you missed the beeper, check all masks and attributes!"

**Result:** Complete analysis revealed critical missing features that are now implemented.

## COM1 Protocol Specification

### Binary Protocol
- **Baud Rate:** 1200 bps
- **Data Format:** 16 bytes binary
- **Polling:** Send byte 0x01, receive 16-byte response
- **Control:** Single-byte commands

### Response Structure (16 bytes)

| Byte | Name | Description | Java Reference |
|------|------|-------------|----------------|
| 0 | LOAD | Load percentage (0-100) | ConCOM1Get.java:683-689 |
| 1 | BATTERY_CAPACITY | Battery charge (0-100) | ConCOM1Get.java:622-640 |
| 2 | INPUT_VOLTAGE | Input voltage / 2 | ConCOM1Get.java:661-672 |
| 3 | OUTPUT_VOLTAGE | Output voltage / 2 | ConCOM1Get.java:674-681 |
| 4 | INPUT_FREQUENCY | Freq = 4807/value | ConCOM1Get.java:692-709 |
| 5 | MODEL_NUMBER | UPS model identifier | ConCOM1Get.java:162-164 |
| 6 | OUTPUT_FREQUENCY | Freq = 4807/value | ConCOM1Get.java:643-659 |
| 7 | NOMINAL_VOLTAGE | Voltage code (unused in IMP) | - |
| 8 | (unused) | - | - |
| 9 | **STATUS_A** | **Status byte A** | ConCOM1Get.java:94, 132-147 |
| 10 | **STATUS_B** | **Status byte B** | ConCOM1Get.java:95, 115-130 |
| 11-15 | (varies) | Model-specific data | - |

## Complete Bit Mapping

### STATUS_A (Byte 9) - All Bits

| Bit | Value | Name | Meaning | Java Line | Implementation |
|-----|-------|------|---------|-----------|----------------|
| 0 | 1 | POWER_FAILURE | Mains failed, on battery | 245-291 | ✅ OB status |
| 1 | 2 | LOW_BAT | Battery low | 293-313 | ✅ LB status |
| 2 | 4 | *(unused)* | Not used in Java driver | - | ⚪ N/A |
| 3 | 8 | AVR | Voltage regulation active | 336-364 | ✅ TRIM/BOOST |
| 4 | 16 | *(unused)* | Not used in Java driver | - | ⚪ N/A |
| 5 | 32 | OVERLOAD | Output overloaded | 315-334 | ✅ OVER status |
| 6 | 64 | *(unused)* | Not used in Java driver | - | ⚪ N/A |
| 7 | 128 | *(unused)* | Not used in Java driver | - | ⚪ N/A |

### STATUS_B (Byte 10) - All Bits

| Bit | Value | Name | Meaning | Java Line | Implementation |
|-----|-------|------|---------|-----------|----------------|
| 0 | 1 | UPS_FAULT | UPS hardware failure | 366-387 | ✅ Alarm |
| **1** | **2** | **BATTERY_FAILED** | **Battery defective** | **389-421** | **✅ RB + Alarm** |
| 2 | 4 | TEST | Battery test running | 423-464 | ✅ TEST status |
| 3 | 8 | BEEPER_STATUS | Beeper (0=ON, 1=OFF) | 495-499 | ✅ ups.beeper.status |
| 4 | 16 | UPS_OFF | UPS output off | 466-488 | ✅ OFF status |
| 5 | 32 | *(unused)* | Not used in Java driver | - | ⚪ N/A |
| 6 | 64 | *(unused)* | Not used in Java driver | - | ⚪ N/A |
| 7 | 128 | *(unused)* | Not used in Java driver | - | ⚪ N/A |

**Implementation Status: 100% of used bits implemented!**

## Critical Discoveries

### 1. Battery Failure Detection (CRITICAL!)

**Java Code (lines 389-421):**
```java
if (Byte_10[1] == 1) {
    bit3 = 2;
    batbadflag = false;
    System.out.println("UPSMON : Battery Failed");
    Label.Battestresult = "Battery Failed";
    // Write to file for logging
    data[22] = 1;
}
```

**C Implementation:**
```c
if (raw_data[STATUS_B] & BAD_BATTERY) {
    status_set("RB");  /* Replace Battery - NUT standard */
    dstate_setinfo("ups.alarm", "Battery failed - needs replacement");
    upsdebugx(2, "STATUS: Battery FAILED (needs replacement)");
}
```

**Why This is Critical:**
- Detects defective/damaged batteries
- Warns before battery fails during power outage
- Prevents data loss from bad battery
- Enables proactive battery replacement
- **This is a safety feature, not optional!**

### 2. Beeper Control

**Java Code (lines 145-181 in ConCOM1Set.java):**
```java
// Beeper ON (a == 3)
if (ConCOM1Get.Byte_10[3] == 1) {
   var5 = 5;  // Command byte 0x05
   var1.write(var5);
}

// Beeper OFF (a == 4)  
if (ConCOM1Get.Byte_10[3] == 0) {
   var5 = 5;  // Command byte 0x05
   var1.write(var5);
}
```

**C Implementation:**
```c
/* Command registration */
dstate_addcmd("beeper.toggle");

/* Command handler */
if (!strcasecmp(cmdname, "beeper.toggle")) {
    ser_send_char(upsfd, BEEPER_TOGGLE);  /* 0x05 */
    return STAT_INSTCMD_HANDLED;
}

/* Status parsing */
if (raw_data[STATUS_B] & BEEPER_STATUS) {
    dstate_setinfo("ups.beeper.status", "disabled");
} else {
    dstate_setinfo("ups.beeper.status", "enabled");
}
```

### 3. AVR Direction Detection

**Java Code (lines 336-364):**
```java
if (Byte_9[3] == 1) {  // AVR active
    if (Input_Voltage > Output_Voltage) {
        System.out.println("UPSMON : Buck");
        data[17] = 3;  // Buck mode
    } else if (Output_Voltage > Input_Voltage) {
        System.out.println("UPSMON : Boost");
        data[17] = 2;  // Boost mode
    }
}
```

**C Implementation:**
```c
if (raw_data[STATUS_A] & AVR_ON) {
    int input_v = input_voltage();
    int output_v = output_voltage();
    if (input_v > output_v) {
        status_set("TRIM");  /* Buck/Reduce voltage */
        upsdebugx(2, "STATUS: AVR Buck (reducing voltage: %d→%d)", input_v, output_v);
    } else if (output_v > input_v) {
        status_set("BOOST");  /* Boost/Increase voltage */
        upsdebugx(2, "STATUS: AVR Boost (increasing voltage: %d→%d)", input_v, output_v);
    }
}
```

## COM1 Commands

### Polling Command

| Byte | Hex | Description | Java Reference |
|------|-----|-------------|----------------|
| 0x01 | 01 | Send data request | ConCOM1Set.java:127 |

**Response:** 16 bytes of UPS status

### Control Commands

| Command | Bytes | Description | Java Reference | Implementation |
|---------|-------|-------------|----------------|----------------|
| Battery Test | 0x03 | Start battery test | Line 133 | ✅ test.battery.start |
| Beeper Toggle | 0x05 | Toggle beeper on/off | Lines 147, 166 | ✅ beeper.toggle |
| Shutdown | 0xB9 0xBC D 0x00 | Shutdown with delay D | Lines 139, 184 | ✅ shutdown.return |

## NUT Variables Implemented

### Standard Variables

| Variable | Source | Description |
|----------|--------|-------------|
| ups.mfr | Config/Hardcoded | "PowerCom" |
| ups.model | Byte 5 + Config | Model name from model arrays |
| input.voltage | Byte 2 * 2 | Input voltage |
| output.voltage | Byte 3 * 2 | Output voltage |
| input.frequency | 4807 / Byte 4 | Input frequency |
| output.frequency | 4807 / Byte 6 | Output frequency |
| battery.charge | Byte 1 | Battery percentage |
| ups.load | Byte 0 | Load percentage |

### Enhanced Variables (New)

| Variable | Source | Description |
|----------|--------|-------------|
| **ups.beeper.status** | **STATUS_B bit 3** | **"enabled" or "disabled"** |
| ups.status | Multiple bits | "RB" when battery failed |
| ups.alarm | STATUS_B bit 1 | "Battery failed - needs replacement" |

### Status Tokens

| Token | Condition | Source |
|-------|-----------|--------|
| OL | Online (mains present) | STATUS_A bit 0 = 0 |
| OB | On Battery (mains failed) | STATUS_A bit 0 = 1 |
| LB | Low Battery | STATUS_A bit 1 = 1 |
| **RB** | **Replace Battery** | **STATUS_B bit 1 = 1** |
| OVER | Overload | STATUS_A bit 5 = 1 |
| TEST | Battery test running | STATUS_B bit 2 = 1 |
| TRIM | AVR Buck (reducing voltage) | STATUS_A bit 3 = 1, input > output |
| BOOST | AVR Boost (increasing voltage) | STATUS_A bit 3 = 1, output > input |
| OFF | UPS output off | STATUS_B bit 4 = 1 |

## Instant Commands

### Always Available (COM1)

| Command | Action | Safety |
|---------|--------|--------|
| beeper.toggle | Toggle beeper on/off | Safe |
| test.battery.start | Start battery test | Safe |
| shutdown.return | Shutdown and return | Dangerous |
| shutdown.stayoff | Shutdown and stay off | Dangerous |

## Testing

### Check Battery Health
```bash
# Normal battery
upsc pcm@localhost ups.status
# Output: OL

# Failed battery
upsc pcm@localhost ups.status
# Output: OL RB

upsc pcm@localhost ups.alarm
# Output: Battery failed - needs replacement
```

### Check Beeper Status
```bash
upsc pcm@localhost ups.beeper.status
# Output: enabled or disabled

upscmd pcm@localhost beeper.toggle
# Toggles beeper state
```

### Check AVR Status
```bash
# During high voltage
upsc pcm@localhost ups.status
# Output: OL TRIM

# During low voltage
upsc pcm@localhost ups.status
# Output: OL BOOST
```

## Real-World Impact

### Scenario: Power Outage with Bad Battery

**Without Battery Failure Detection:**
1. Power fails
2. UPS switches to battery
3. Battery is defective (unknown to user)
4. UPS dies immediately
5. Server crashes
6. **Data lost**

**With Battery Failure Detection:**
1. UPS detects bad battery
2. Driver sets RB status + alarm
3. Monitoring system alerts user
4. User replaces battery
5. Next power outage: UPS works
6. **Data protected**

## Implementation Summary

| Feature | Status | Priority | Impact |
|---------|--------|----------|--------|
| Battery failure detection | ✅ NEW | **CRITICAL** | Prevents data loss |
| Beeper control | ✅ NEW | High | User convenience |
| Beeper status | ✅ NEW | Medium | Monitoring |
| AVR direction | ✅ IMPROVED | Medium | Information |
| Enhanced debug output | ✅ NEW | High | Troubleshooting |
| Complete bit mapping | ✅ DONE | High | Documentation |

## Lessons Learned

1. **Always study reference implementations thoroughly** - Don't assume features are "missing" from older protocols
2. **User feedback is invaluable** - Users often have domain knowledge we lack
3. **Safety features are critical** - Battery failure detection is not optional
4. **Document bit mappings completely** - Essential for maintenance and troubleshooting
5. **Test with real hardware** - Emulation can't catch everything

## Acknowledgment

**Огромное спасибо!** (Huge thanks!)

This thorough analysis was prompted by user feedback and revealed:
- ✅ Critical safety feature (battery failure detection)
- ✅ Missing control feature (beeper)
- ✅ Incomplete status parsing (AVR direction)
- ✅ Need for better documentation

**Result: 100% COM1 feature parity with Java UPSMON driver!**

## References

- ConCOM1Get.java - Protocol parsing and bit mapping
- ConCOM1Set.java - Command sending
- Software_USB_communication_controller_IMPERIAL_series_R21.pdf - Protocol documentation

## Status: COMPLETE ✅

COM1 protocol implementation is now 100% complete with all safety features.
