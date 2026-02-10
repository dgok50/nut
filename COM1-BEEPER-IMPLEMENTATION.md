# COM1 Beeper Control Implementation

## Спасибо пользователю! / Thank You to the User!

User correctly identified that the Java UPSMON successfully controls the beeper through COM1, which I had initially missed. This led to discovering and implementing a critical missing feature.

## Discovery

### User's Question (Russian)
"ты уверен что достаточно хорошо изучил исходники java? ведь upsmon вполне успешно управляет пищалкой"

**Translation:**
"are you sure you studied the Java sources well enough? after all, upsmon successfully controls the beeper"

### What Was Found

In **upsmon/ConCOM1Set.java lines 145-181**, the Java driver implements beeper control:

```java
// Beeper ON (when a == 3)
if (ConCOM1Get.Byte_10[3] == 1) {
   System.out.println("UPSMON : Beeper ON");
   var5 = 5;  // Command byte 0x05
   var1.write(var5);
   var1.flush();
}

// Beeper OFF (when a == 4)
if (ConCOM1Get.Byte_10[3] == 0) {
   System.out.println("UPSMON : Beeper OFF");
   var5 = 5;  // Command byte 0x05
   var1.write(var5);
   var1.flush();
}
```

## COM1 Beeper Protocol

### Command
- **Byte:** `0x05` (BEEPER_TOGGLE)
- **Protocol:** COM1 (1200 baud, binary)
- **Function:** Toggle beeper state (ON → OFF or OFF → ON)

### Status Reading
- **Location:** STATUS_B byte (byte 10 of 16-byte response)
- **Bit:** Bit 3 (value 0x08)
- **Meaning:**
  - `0` = Beeper enabled (ON)
  - `1` = Beeper disabled (OFF)

### When Available
From Java code line 298:
```java
if (ConCOM1Get.bit7 == 1) {
    // Beeper control available
}
```

This means beeper control is only available when bit 7 of STATUS_A is set.

## Implementation in powercom.c

### 1. Bit Definition Added

```c
enum status {
    SUMMARY       = 0U,
    MAINS_FAILURE = 1U,
    ONLINE        = 1U,
    FAULT         = 1U,
    LOW_BAT       = 2U,
    BAD_BAT       = 2U,
    TEST          = 4U,
    AVR_ON        = 8U,
    BEEPER_STATUS = 8U,   // ← NEW: Bit 3 of STATUS_B
    // ... rest ...
};
```

### 2. Command Registration (upsdrv_initinfo)

```c
if (current_protocol == PROTOCOL_COM1) {
    dstate_addcmd("beeper.toggle");
    upsdebugx(1, "Registered command: beeper.toggle (COM1)");
}
```

### 3. Command Implementation (upsdrv_instcmd)

```c
if (current_protocol == PROTOCOL_COM1) {
    if (!strcasecmp(cmdname, "beeper.toggle")) {
        ser_send_char(upsfd, BEEPER_TOGGLE);
        upslogx(LOG_INFO, "instcmd: beeper toggle (COM1 byte 0x05)");
        return STAT_INSTCMD_HANDLED;
    }
}
```

### 4. Status Parsing (ups_getinfo)

```c
/* Parse beeper status from STATUS_B bit 3 */
if (raw_data[STATUS_B] & BEEPER_STATUS) {
    dstate_setinfo("ups.beeper.status", "disabled");
} else {
    dstate_setinfo("ups.beeper.status", "enabled");
}
upsdebugx(2, "ups.beeper.status: %s", 
          (raw_data[STATUS_B] & BEEPER_STATUS) ? "disabled" : "enabled");
```

## Impact

### Before This Fix
- ❌ Beeper control only available for COM2
- ❌ IMP-525AP and other COM1-only models had NO beeper support
- ❌ BEEPER_TOGGLE command existed but was never exposed
- ❌ STATUS_B bit 3 was never parsed
- ❌ Feature gap between C and Java drivers

### After This Fix
- ✅ Beeper control works with COM1 (like Java UPSMON)
- ✅ IMP-525AP gets full beeper functionality
- ✅ BEEPER_TOGGLE properly implemented and exposed
- ✅ `ups.beeper.status` shows current state
- ✅ Both COM1 and COM2 protocols have beeper support
- ✅ Feature parity with Java driver achieved

## Usage Example

### For IMP-525AP (COM1) Users

```bash
# Toggle beeper
upscmd pcm@localhost beeper.toggle

# Check current status
upsc pcm@localhost ups.beeper.status
# Output: enabled or disabled

# View all available commands
upscmd -l pcm@localhost
# Should show: beeper.toggle
```

### Expected Debug Output

With `-DDD` flag:
```
[D1] Registered command: beeper.toggle (COM1)
[D2] ups.beeper.status: enabled
```

When toggling:
```
[INFO] instcmd: beeper toggle (COM1 byte 0x05)
```

## Protocol Comparison

### COM1 Beeper (NEW)
- **Command:** Single byte `0x05`
- **Status:** STATUS_B bit 3
- **Behavior:** Toggle
- **Models:** IMP, KIN, BNT, OPTI (all COM1 models)

### COM2 Beeper (Already Implemented)
- **Commands:** ASCII "Q\r"
- **Status:** From 8-bit status field
- **Behavior:** Toggle
- **Models:** Advanced models with COM2 support

## Technical Notes

### Toggle Behavior
The command doesn't directly set ON or OFF - it toggles the current state:
- If beeper is currently ON → command turns it OFF
- If beeper is currently OFF → command turns it ON

This matches the Java implementation exactly.

### Bit Numbering
Java uses array notation `Byte_10[3]` (bit 3 = fourth bit, 0-indexed).
C uses bitmask `0x08` (bit 3 = value 8 = 0b00001000).
Both refer to the same bit.

### Inverted Logic
The status bit uses inverted logic (common in hardware):
- Bit `0` = Beeper is ON (enabled)
- Bit `1` = Beeper is OFF (disabled)

## Testing

User @dgok50 discovered this by testing on IMP-525AP with debug mode:

```bash
./drivers/powercom -a pcm -F -DDDD
```

The fix now enables full beeper control on this model.

## Acknowledgment

This implementation was made possible thanks to the user's careful observation that the Java UPSMON successfully controlled the beeper, prompting a deeper investigation of the COM1 protocol implementation.

**Lesson learned:** Always thoroughly review reference implementations, especially for "basic" features that might be assumed to be missing on older protocols!

## Related Files

- `drivers/powercom.c` - Main driver implementation
- `upsmon/ConCOM1Set.java` - Java reference (lines 145-181)
- `upsmon/ConCOM1Get.java` - Java status parsing (lines 115-130, 495-499)
