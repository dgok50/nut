# Build Troubleshooting Guide

## Руководство по устранению проблем сборки
## Build Troubleshooting Guide

Common build errors and their solutions for PowerCom driver.

---

## Error: "Can't exec 'libtoolize'"

### Full Error Message:
```
autoreconf: running: libtoolize --copy
Can't exec "libtoolize": No such file or directory at /usr/share/autoconf/Autom4te/FileUtils.pm line 274.
autoreconf: error: libtoolize failed with exit status: 2
```

### Cause / Причина:
The `libtool` package is **not installed** on your system. This package provides the `libtoolize` command required by `autoreconf`.

### Solution / Решение:

#### Fedora/RHEL/CentOS:
```bash
sudo dnf install libtool
./autogen.sh  # Try again
```

#### Ubuntu/Debian:
```bash
sudo apt-get install libtool libtool-bin
./autogen.sh  # Try again
```

#### OpenWrt:
```bash
opkg update
opkg install libtool
./autogen.sh  # Try again
```

### Install All Build Dependencies / Установить все зависимости:

**Fedora:**
```bash
sudo dnf install gcc make libtool autoconf automake pkgconfig git
```

**Ubuntu/Debian:**
```bash
sudo apt-get install build-essential libtool libtool-bin autoconf automake pkg-config git
```

**OpenWrt:**
```bash
opkg update
opkg install gcc make libtool autoconf automake pkgconfig
```

---

## Error: "configure: command not found"

### Cause:
The `configure` script hasn't been generated yet.

### Solution:
```bash
./autogen.sh
```

This generates the `configure` script from `configure.ac`.

---

## Error: "autoreconf: command not found"

### Cause:
The `autoconf` package is not installed.

### Solution:

**Fedora:**
```bash
sudo dnf install autoconf automake
```

**Ubuntu/Debian:**
```bash
sudo apt-get install autoconf automake
```

**OpenWrt:**
```bash
opkg install autoconf automake
```

---

## Error: "gcc: command not found"

### Cause:
C compiler is not installed.

### Solution:

**Fedora:**
```bash
sudo dnf install gcc make
```

**Ubuntu/Debian:**
```bash
sudo apt-get install build-essential
```

**OpenWrt:**
```bash
opkg install gcc make
```

---

## Error: "aclocal: command not found"

### Cause:
The `automake` package is not installed.

### Solution:

**Fedora:**
```bash
sudo dnf install automake
```

**Ubuntu/Debian:**
```bash
sudo apt-get install automake
```

**OpenWrt:**
```bash
opkg install automake
```

---

## Error: "No rule to make target 'powercom'"

### Cause:
You haven't run `./configure` yet, or configured without powercom driver.

### Solution:
```bash
./configure --with-drivers=powercom --with-serial=yes
make -C drivers powercom
```

---

## Error: "cannot execute binary file"

### Cause:
Trying to run a binary compiled for different architecture or C library (glibc vs musl).

### Solution:

**Check architecture match:**
```bash
uname -m           # Your system architecture
file powercom      # Binary architecture

# They must match!
```

**For OpenWrt:** Binary must be compiled with musl libc, not glibc.
```bash
file powercom | grep musl  # Should show musl for OpenWrt
```

See: `BUILDING-OPENWRT.md` for details.

---

## Error: Out of Space (OpenWrt)

### Symptoms:
```
No space left on device
```

### Solutions:

**Check space:**
```bash
df -h
```

**Clean temporary files:**
```bash
rm -rf /tmp/*
rm -rf /tmp/opkg-*
```

**Build in /tmp:**
```bash
cd /tmp
# Do your build here
```

**Use external USB:**
```bash
# Mount USB drive
mount /dev/sda1 /mnt
cd /mnt
# Build here
```

---

## Error: Missing pkg-config

### Error:
```
configure: error: pkg-config not found
```

### Solution:

**Fedora:**
```bash
sudo dnf install pkgconfig
```

**Ubuntu/Debian:**
```bash
sudo apt-get install pkg-config
```

**OpenWrt:**
```bash
opkg install pkgconfig
```

---

## Error: Permission Denied

### When running driver:
```bash
./powercom: Permission denied
```

### Solution:
```bash
chmod +x powercom
```

### When installing:
```bash
sudo cp powercom /usr/lib/nut/
sudo chmod +x /usr/lib/nut/powercom
```

---

## Complete Dependency List

### Minimal (required):
```bash
# Fedora:
sudo dnf install gcc make libtool autoconf automake

# Ubuntu/Debian:
sudo apt-get install build-essential libtool libtool-bin autoconf automake

# OpenWrt:
opkg install gcc make libtool autoconf automake
```

### Recommended (with helpers):
```bash
# Fedora:
sudo dnf install gcc make libtool autoconf automake pkgconfig git

# Ubuntu/Debian:
sudo apt-get install build-essential libtool libtool-bin autoconf automake pkg-config git

# OpenWrt:
opkg install gcc make libtool autoconf automake pkgconfig
```

---

## Quick Build Check

After installing dependencies, verify:

```bash
# 1. Check tools exist
which gcc
which make
which libtoolize
which autoconf
which automake

# 2. Try build
./autogen.sh
./configure --with-drivers=powercom --with-serial=yes
make -C drivers powercom

# 3. Verify result
ls -lh drivers/powercom
./drivers/powercom -V
```

---

## Still Having Issues?

### Collect Debug Info:

```bash
# System info
uname -a
cat /etc/os-release

# Installed packages
# Fedora:
rpm -qa | grep -E "gcc|make|libtool|autoconf|automake"

# Ubuntu/Debian:
dpkg -l | grep -E "gcc|make|libtool|autoconf|automake"

# OpenWrt:
opkg list-installed | grep -E "gcc|make|libtool|autoconf|automake"

# Build attempt with full output
./autogen.sh 2>&1 | tee autogen.log
./configure --with-drivers=powercom --with-serial=yes 2>&1 | tee configure.log
make -C drivers powercom 2>&1 | tee make.log
```

### Get Help:

1. Check existing documentation:
   - `BUILD-POWERCOM-ONLY.md` - How to build
   - `BUILDING-OPENWRT.md` - OpenWrt specific
   - `TESTING-POWERCOM.md` - Testing guide

2. Search for similar issues:
   - GitHub issues
   - NUT mailing list

3. Post issue with logs:
   - Include `autogen.log`, `configure.log`, `make.log`
   - Include system info (`uname -a`, OS version)
   - Describe what you tried

---

## Summary / Итого

Most build errors are due to missing packages. Install complete build toolchain:

**Fedora:**
```bash
sudo dnf install gcc make libtool autoconf automake pkgconfig git
```

**Ubuntu/Debian:**
```bash
sudo apt-get install build-essential libtool libtool-bin autoconf automake pkg-config git
```

**OpenWrt:**
```bash
opkg update
opkg install gcc make libtool autoconf automake pkgconfig
```

Then:
```bash
./autogen.sh
./configure --with-drivers=powercom --with-serial=yes
make -C drivers powercom
```

Done! / Готово! 🚀
