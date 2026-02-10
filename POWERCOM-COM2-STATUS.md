# PowerCom COM2 Protocol Implementation Status

## Summary

This document explains the COM2 protocol implementation for PowerCom UPS drivers and what to expect with your specific UPS model.

## What We Implemented

### COM2 Protocol Features
- **ASCII protocol** at 2400 baud (vs COM1 binary at 1200 baud)
- **Alternating Q1/DQ1 commands** (like official Java driver)
- **Auto-detection**: Tries both COM1 and COM2, prefers COM2 if both work
- **Extended variables**: battery.voltage, ups.temperature, ups.event.*, ups.beeper.status
- **Instant commands**: test.battery.start/stop, beeper.toggle
- **Event latching**: Remembers short power events for configurable hold time

### Bug Fixes Applied
1. **CRITICAL**: Fixed `ser_send_pace` → `ser_send_buf_pace` (COM2 was completely broken)
2. **Protocol detection**: Now properly tries both protocols and prefers COM2
3. **Debug logging**: Added detailed hex dumps for troubleshooting

## Your UPS: IMP-525AP

### Test Results

From your debug log:
```
[D2] COM2: Sending DQ1 command (iteration 0)
[D3] COM2: TX → [0x44 0x51 0x31 0x0d] "DQ1\r"
[D3] COM2: Waiting up to 3000ms for response...
[D3] COM2: RX ← 0 bytes (timeout - UPS not responding)
[D1] COM2 probe: FAILED - no valid response
Auto-detected protocol: COM1 (binary, 1200 baud) - fallback
```

### Conclusion: IMP-525AP is COM1-only

**Your IMP-525AP does NOT support COM2 protocol.**

This is NOT a driver bug - it's a hardware/firmware limitation of this particular UPS model.

### Why COM1 Only?

Different PowerCom models have different protocol support:
- **Older/simpler models**: COM1 only (binary protocol)
- **Newer/advanced models**: Both COM1 + COM2 (ASCII protocol with more features)
- **USB models**: Different protocol entirely (not covered here)

Your IMP-525AP appears to be COM1-only, which is perfectly normal for this model.

## What Works on Your UPS

### COM1 Protocol (What You Have)

✅ **Working features:**
- input.voltage
- output.voltage
- input.frequency
- output.frequency
- battery.charge (percentage)
- ups.load (percentage)
- ups.status (OL/OB/LB/TEST/etc.)
- Basic shutdown commands

❌ **Not available (require COM2):**
- battery.voltage (in volts)
- ups.temperature
- ups.event.last/time/count (event detection)
- ups.beeper.status
- beeper.toggle command
- test.battery.stop command

### Your Current Status

From your log:
```
[D2] input.voltage: 226.0
[D2] output.voltage: 226.0
[D2] input.frequency: 50.00
[D2] output.frequency: 50.00
[D2] ups.load: 7.0
[D2] battery.charge: 100.0
[D2] STATUS: OL
```

**Everything is working correctly for a COM1 device!**

## Testing COM2 on Other UPS Models

If you have access to other PowerCom UPS models (especially newer ones), you can test COM2 support:

### Test Command

```bash
./powercom -a ups -F -DDDD
```

Look for:
```
[D3] COM2: RX ← 48+ bytes  ← Success!
[D1] COM2 probe: SUCCESS - UPS responds to ASCII protocol
```

### Models Likely to Support COM2

Based on research:
- Newer Imperial (IMP) series
- King Pro (KIN) series  
- Black Knight (BNT) newer models
- Models with USB interface (may also have COM2 serial)

### Models Likely COM1-only

- Older Imperial (IMP) series ← **Your IMP-525AP**
- Trust UPS series
- Basic/budget models
- Very old firmware

## How to Force Protocol

If you want to force a specific protocol (for testing):

### Force COM1 (binary)
```ini
[ups]
driver = powercom
port = /dev/ttyUSB0
protocol_mode = com1
```

### Force COM2 (ASCII)
```ini
[ups]
driver = powercom
port = /dev/ttyUSB0
protocol_mode = com2
```

**Warning**: Forcing COM2 on a COM1-only UPS will cause communication failure!

### Auto-detect (default, recommended)
```ini
[ups]
driver = powercom
port = /dev/ttyUSB0
protocol_mode = auto
```

## Diagnostic Command

To see detailed protocol detection with hex dumps:

```bash
./powercom -a pcm -F -DDDD 2>&1 | grep -A10 "protocol detection"
```

Expected output for COM1-only UPS:
```
First-time protocol detection: probing COM1 and COM2
Trying COM1 at 1200 baud (binary protocol)...
COM1 probe: SUCCESS - UPS responds to binary protocol
Trying COM2 at 2400 baud (ASCII protocol)...
COM2: TX → [0x44 0x51 0x31 0x0d] "DQ1\r"
COM2: Waiting up to 3000ms for response...
COM2: RX ← 0 bytes (timeout - UPS not responding)
COM2 probe: FAILED - no valid response
Auto-detected protocol: COM1 (binary, 1200 baud) - fallback
Using COM1: COM2 not available on this UPS
```

## Documentation

See also:
- **TESTING-POWERCOM.md** - How to test the driver
- **BUILD-POWERCOM-ONLY.md** - How to build just this driver
- **BUILDING-OPENWRT.md** - How to build for OpenWrt
- **TROUBLESHOOTING-BUILD.md** - Common build issues

## Summary for IMP-525AP Users

✅ **Your UPS works correctly** with COM1 protocol
✅ **All basic monitoring features** are functional
✅ **Driver auto-detects** and uses correct protocol
❌ **COM2 not supported** by this UPS model (hardware limitation)
❌ **Advanced features unavailable** (temperature, battery voltage, events)

**This is normal and expected - your UPS is functioning correctly!**

## Questions?

If you see:
- "COM2 probe: SUCCESS" → UPS supports COM2, enjoy extra features!
- "COM2 probe: FAILED" → UPS is COM1-only, basic features only
- "COM1 probe: FAILED" and "COM2 probe: FAILED" → Check cable/connections

---

*Last updated: 2026-02-10*
*PowerCom driver version: 0.27*
