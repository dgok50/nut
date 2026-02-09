# Building PowerCom Driver for OpenWrt

## Сборка драйвера PowerCom для OpenWrt
## Building PowerCom Driver for OpenWrt

**ВАЖНО / IMPORTANT:** Вы НЕ МОЖЕТЕ запустить бинарник с Fedora/Ubuntu/Debian на OpenWrt!  
**You CANNOT run Fedora/Ubuntu/Debian binaries on OpenWrt!**

---

## Problem: "cannot execute binary file" / Проблема

```bash
root@openwrt:~$ ./powercom
powercom: powercom: cannot execute binary file
```

### Real-World Example / Реальный пример

Here's **proof** of incompatibility using `file` command:

**OpenWrt System Binary (WORKS) / Системный бинарник (РАБОТАЕТ):**
```bash
root@openwrt:~$ file /lib/nut/powercom
/lib/nut/powercom: ELF 64-bit LSB executable, x86-64, 
  dynamically linked, 
  interpreter /lib/ld-musl-x86_64.so.1,     ← musl libc
  no section header
```

**Fedora Binary (WON'T WORK) / Бинарник с Fedora (НЕ РАБОТАЕТ):**
```bash
root@openwrt:~$ file powercom
powercom: ELF 64-bit LSB executable, x86-64, 
  dynamically linked, 
  interpreter /lib64/ld-linux-x86-64.so.2,  ← glibc
  with debug_info, not stripped
```

**Critical Difference / Критическое различие:**
- OpenWrt: `/lib/ld-musl-x86_64.so.1` (musl)
- Fedora: `/lib64/ld-linux-x86-64.so.2` (glibc)

These are **completely different** dynamic linkers! OpenWrt doesn't have glibc!

### Why This Happens / Почему это происходит

OpenWrt uses **different libraries and compilation** than regular Linux distributions:

| Feature | Fedora/Ubuntu | OpenWrt |
|---------|---------------|---------|
| C Library | glibc (GNU) | musl libc |
| Dynamic linker | `/lib64/ld-linux-x86-64.so.2` | `/lib/ld-musl-x86_64.so.1` |
| Size | Large (~500KB) | Minimal (~150KB) |
| Debug info | Included | Stripped |
| Section header | Yes | No |
| Optimization | Generic | Embedded-specific |

**Result / Результат:** Binaries are **incompatible** / Бинарники **несовместимы**

---

## Solutions / Решения

### FIRST: Check if You Already Have It! / СНАЧАЛА: Проверьте, может уже есть!

**IMPORTANT:** Before building, check if OpenWrt already has a working binary installed!

```bash
# Check if powercom driver exists / Проверить существование драйвера
ls -la /lib/nut/powercom
ls -la /usr/lib/nut/powercom

# If found, verify it's an OpenWrt binary / Если найден, проверить что это OpenWrt бинарник
file /lib/nut/powercom

# Should show musl libc / Должно показать musl libc:
# interpreter /lib/ld-musl-x86_64.so.1

# Test it / Проверить работу
/lib/nut/powercom -V
/lib/nut/powercom -h
```

**If you see a working binary with musl libc, you're done! Use it!**  
**Если видите рабочий бинарник с musl libc, всё готово! Используйте его!**

### Method 1: Use OpenWrt Package (EASIEST) / Использовать пакет OpenWrt (ПРОЩЕ ВСЕГО)

OpenWrt already has NUT package in feeds!

```bash
# Update package lists / Обновить списки пакетов
opkg update

# Search for NUT / Поиск NUT
opkg list | grep nut

# Install NUT with drivers / Установить NUT с драйверами
opkg install nut nut-driver-powercom

# Or install complete NUT / Или установить полный NUT
opkg install nut-server nut-client
```

**Check available packages:**
```bash
opkg list | grep nut
```

Expected output:
```
nut - Network UPS Tools
nut-driver-powercom - PowerCom driver
nut-server - NUT server daemon
nut-client - NUT client utilities
```

---

### Method 2: Build Directly on OpenWrt Device / Собрать на устройстве OpenWrt

If you have enough space and want latest changes:

#### Step 1: Install build tools / Установить инструменты сборки

```bash
# Update package database / Обновить базу пакетов
opkg update

# Install essential tools / Установить основные инструменты
opkg install gcc make libtool autoconf automake pkgconfig

# Install development libraries / Установить библиотеки разработки
opkg install libc-dev
```

#### Step 2: Get NUT source / Получить исходники NUT

```bash
# Download source / Скачать исходники
cd /tmp
wget https://github.com/networkupstools/nut/archive/refs/heads/master.tar.gz
tar xzf master.tar.gz
cd nut-master
```

Or clone your repository with changes:
```bash
cd /tmp
git clone https://github.com/dgok50/nut.git
cd nut
```

#### Step 3: Configure for OpenWrt / Настроить для OpenWrt

```bash
# Generate configure script / Создать скрипт configure
./autogen.sh

# Configure with minimal options / Настроить с минимальными опциями
./configure \
    --prefix=/usr \
    --sysconfdir=/etc/nut \
    --with-serial=yes \
    --with-drivers=powercom \
    --with-user=root \
    --with-group=root \
    --disable-dependency-tracking \
    --enable-strip \
    --with-ssl=no \
    --with-usb=no \
    --with-snmp=no \
    --with-neon=no \
    --with-powerman=no \
    --with-ipmi=no \
    --with-freeipmi=no

# Check configuration / Проверить конфигурацию
echo "Configuration completed. Check for errors above."
```

#### Step 4: Build driver / Собрать драйвер

```bash
# Build only powercom driver / Собрать только драйвер powercom
make -C drivers powercom

# Check result / Проверить результат
ls -lh drivers/powercom
file drivers/powercom
```

Expected output:
```
drivers/powercom: ELF 64-bit LSB executable, x86-64, dynamically linked, stripped
```

#### Step 5: Install / Установить

```bash
# Create directories / Создать директории
mkdir -p /usr/lib/nut
mkdir -p /etc/nut

# Copy driver / Скопировать драйвер
cp drivers/powercom /usr/lib/nut/
chmod +x /usr/lib/nut/powercom

# Test / Проверить
/usr/lib/nut/powercom -h
```

---

### Method 3: Cross-Compile with OpenWrt SDK / Кросс-компиляция с OpenWrt SDK

**On your development machine (Fedora/Ubuntu):**

#### Step 1: Download OpenWrt SDK / Скачать OpenWrt SDK

```bash
# Find your OpenWrt version / Узнать версию OpenWrt
# On OpenWrt device:
cat /etc/openwrt_release

# Download matching SDK from:
# https://downloads.openwrt.org/

# Example for x86_64:
cd ~/openwrt
wget https://downloads.openwrt.org/releases/23.05.2/targets/x86/64/openwrt-sdk-23.05.2-x86-64_gcc-12.3.0_musl.Linux-x86_64.tar.xz

# Extract / Извлечь
tar xJf openwrt-sdk-*.tar.xz
cd openwrt-sdk-*/
```

#### Step 2: Prepare NUT source / Подготовить исходники NUT

```bash
# Copy NUT source to SDK / Скопировать исходники в SDK
mkdir -p package/nut
cd package/nut

# Create Makefile for OpenWrt package / Создать Makefile для пакета
cat > Makefile << 'EOF'
include $(TOPDIR)/rules.mk

PKG_NAME:=nut
PKG_VERSION:=2.8.4
PKG_RELEASE:=1

PKG_SOURCE_PROTO:=git
PKG_SOURCE_URL:=https://github.com/dgok50/nut.git
PKG_SOURCE_VERSION:=HEAD

include $(INCLUDE_DIR)/package.mk

define Package/nut-powercom
  SECTION:=net
  CATEGORY:=Network
  TITLE:=Network UPS Tools - PowerCom driver
  URL:=https://networkupstools.org/
  DEPENDS:=+libusb-1.0
endef

define Package/nut-powercom/description
  PowerCom UPS driver for Network UPS Tools
endef

define Build/Configure
	( cd $(PKG_BUILD_DIR); ./autogen.sh )
	$(call Build/Configure/Default,\
		--with-serial=yes \
		--with-drivers=powercom \
		--with-user=root \
		--with-group=root \
		--disable-dependency-tracking \
		--enable-strip \
		--with-ssl=no \
		--with-usb=no \
	)
endef

define Package/nut-powercom/install
	$(INSTALL_DIR) $(1)/usr/lib/nut
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/drivers/powercom $(1)/usr/lib/nut/
	
	$(INSTALL_DIR) $(1)/etc/nut
	$(INSTALL_CONF) $(PKG_BUILD_DIR)/conf/ups.conf.sample $(1)/etc/nut/
endef

$(eval $(call BuildPackage,nut-powercom))
EOF
```

#### Step 3: Build package / Собрать пакет

```bash
# Go back to SDK root / Вернуться в корень SDK
cd ../../

# Update feeds / Обновить фиды
./scripts/feeds update -a
./scripts/feeds install -a

# Build the package / Собрать пакет
make package/nut/compile V=s

# Find the package / Найти пакет
find bin/ -name "nut-powercom*.ipk"
```

#### Step 4: Install on OpenWrt / Установить на OpenWrt

```bash
# Copy .ipk file to OpenWrt device / Скопировать на OpenWrt
scp bin/packages/*/base/nut-powercom*.ipk root@openwrt:/tmp/

# On OpenWrt device / На устройстве OpenWrt:
opkg install /tmp/nut-powercom*.ipk
```

---

## Method 4: Static Binary Compilation / Статическая сборка

Build a fully static binary that works anywhere:

### On development machine:

```bash
cd /path/to/nut

# Configure for static linking / Настроить для статической сборки
./configure \
    --with-serial=yes \
    --with-drivers=powercom \
    --disable-shared \
    --enable-static \
    LDFLAGS="-static -static-libgcc"

# Build / Собрать
make -C drivers powercom

# Verify it's static / Проверить статичность
file drivers/powercom
ldd drivers/powercom  # Should say "not a dynamic executable"

# Strip to reduce size / Урезать размер
strip drivers/powercom

# Check size / Проверить размер
ls -lh drivers/powercom
```

**Important:** Static binaries are much larger but work anywhere with same architecture.

---

## Testing the Built Driver / Тестирование собранного драйвера

### On OpenWrt device:

```bash
# Check if binary is correct format / Проверить формат бинарника
file /usr/lib/nut/powercom

# Should show:
# /usr/lib/nut/powercom: ELF 64-bit LSB executable, x86-64, dynamically linked, interpreter /lib/ld-musl-x86_64.so.1, stripped

# Check dynamic libraries / Проверить динамические библиотеки
ldd /usr/lib/nut/powercom

# Test help / Проверить справку
/usr/lib/nut/powercom -h

# Test with your UPS / Проверить с ИБП
/usr/lib/nut/powercom -s test -x port=/dev/ttyUSB0 -d 1 -DDD
```

### How to Identify Binary Type / Как определить тип бинарника

Use `file` command to check:

```bash
# Check binary / Проверить бинарник
file powercom
```

**OpenWrt Binary (Correct) / Бинарник OpenWrt (Правильный):**
```
ELF 64-bit LSB executable, x86-64, 
  dynamically linked, 
  interpreter /lib/ld-musl-x86_64.so.1,  ← GOOD: musl
  stripped                                ← GOOD: small size
```

**Fedora/Ubuntu Binary (Wrong) / Бинарник Fedora/Ubuntu (Неправильный):**
```
ELF 64-bit LSB executable, x86-64, 
  dynamically linked, 
  interpreter /lib64/ld-linux-x86-64.so.2,  ← BAD: glibc
  with debug_info, not stripped              ← BAD: large size
```

**Key Indicators / Ключевые индикаторы:**

✅ **OpenWrt binary:**
- interpreter: `/lib/ld-musl-x86_64.so.1` or similar musl path
- "stripped" or "no section header"
- Small size (~150-300 KB)

❌ **Fedora/Ubuntu binary:**
- interpreter: `/lib64/ld-linux-x86-64.so.2` or similar glibc path
- "with debug_info, not stripped"
- Large size (~500 KB - 2 MB)

### Quick Test / Быстрая проверка

```bash
# If this shows musl → good for OpenWrt
# Если показывает musl → подходит для OpenWrt
file powercom | grep musl

# If this shows ld-linux or glibc → wrong, rebuild
# Если показывает ld-linux или glibc → неправильно, пересобрать
file powercom | grep -E "ld-linux|glibc"
```

---

## Configuration on OpenWrt / Конфигурация на OpenWrt

### Create configuration / Создать конфигурацию

```bash
# Create config directory / Создать директорию конфигурации
mkdir -p /etc/nut

# Create ups.conf / Создать ups.conf
cat > /etc/nut/ups.conf << 'EOF'
[myups]
    driver = powercom
    port = /dev/ttyUSB0
    desc = "PowerCom UPS"
    protocol_mode = auto
    event_hold = 20
EOF

# Set permissions / Установить права
chmod 640 /etc/nut/ups.conf
```

### Test configuration / Проверить конфигурацию

```bash
# Test with debug / Проверить с отладкой
/usr/lib/nut/powercom -a myups -DDD
```

---

## Troubleshooting / Устранение неполадок

### Error: "Can't exec 'libtoolize': No such file or directory"

**Full error:**
```
autoreconf: running: libtoolize --copy
Can't exec "libtoolize": No such file or directory at /usr/share/autoconf/Autom4te/FileUtils.pm line %.
autoreconf: error: libtoolize failed with exit status: 2
```

**Причина / Cause:** The `libtool` package is not installed on the system.

**Решение / Solution:**
```bash
# OpenWrt:
opkg update
opkg install libtool

# Then run autogen.sh again / Затем запустить autogen.sh снова:
./autogen.sh
```

**Install all build tools at once / Установить все инструменты сразу:**
```bash
opkg update
opkg install gcc make libtool autoconf automake pkgconfig
```

**On development machine (Fedora/Ubuntu) / На машине разработки:**
```bash
# Fedora:
sudo dnf install libtool

# Ubuntu/Debian:
sudo apt-get install libtool libtool-bin
```

---

### Error: "cannot execute binary file"

**Причина / Cause:** Wrong architecture or libraries

**Решение / Solution:**
```bash
# Check your architecture / Проверить архитектуру
uname -m

# Check binary architecture / Проверить архитектуру бинарника
file powercom

# They must match! / Они должны совпадать!
```

### Error: "No such file or directory" when binary exists

**Причина / Cause:** Missing dynamic linker or libraries

**Решение / Solution:**
```bash
# Check dynamic linker / Проверить динамический линкер
ldd powercom

# Install missing libraries / Установить недостающие библиотеки
opkg update
opkg install libc
```

### Error: "Permission denied"

**Решение / Solution:**
```bash
chmod +x /usr/lib/nut/powercom
```

### Error: Out of space

OpenWrt has limited space. Solutions:

```bash
# Check available space / Проверить свободное место
df -h

# Clean package cache / Очистить кеш пакетов
rm -rf /tmp/opkg-*

# Use external USB drive / Использовать USB диск
# Mount USB and build there
```

### Error: "Package not found"

```bash
# Add community feeds / Добавить community фиды
echo "src/gz openwrt_packages http://downloads.openwrt.org/releases/23.05.2/packages/x86_64/packages" >> /etc/opkg/customfeeds.conf

opkg update
```

---

## Size Optimization / Оптимизация размера

OpenWrt has limited space. Reduce binary size:

```bash
# Strip symbols / Удалить символы
strip --strip-all powercom

# Use UPX compression / Сжать UPX
opkg install upx
upx --best powercom

# Before / До:
# powercom: 500KB

# After strip / После strip:
# powercom: 300KB

# After UPX / После UPX:
# powercom: 150KB
```

---

## Pre-Built Binaries / Готовые бинарники

**ВАЖНО / IMPORTANT:** Pre-built binaries must match your OpenWrt:
- Same architecture (x86_64, arm, mips, etc.)
- Same OpenWrt version
- Same library versions

**НЕ ИСПОЛЬЗУЙТЕ / DO NOT USE:**
- ❌ Fedora binaries
- ❌ Ubuntu binaries
- ❌ Debian binaries
- ❌ Different OpenWrt versions
- ❌ Different architectures

**ИСПОЛЬЗУЙТЕ / USE:**
- ✅ OpenWrt package repository
- ✅ Build from source on target
- ✅ Cross-compile with OpenWrt SDK
- ✅ Static binaries (with caution)

---

## Recommended Approach / Рекомендуемый подход

**For most users / Для большинства пользователей:**

```bash
# 1. Use OpenWrt package (if available)
opkg update
opkg install nut nut-driver-powercom

# 2. If not available, build on device
opkg install gcc make
cd /tmp && wget [source] && ./configure && make
```

**For developers / Для разработчиков:**

```bash
# Use OpenWrt SDK for cross-compilation
# This is fastest and most reliable
```

---

## Architecture Reference / Справочник архитектур

Common OpenWrt architectures:

| Device Type | Architecture | SDK Name |
|-------------|--------------|----------|
| x86 PC | x86_64 | x86-64 |
| Raspberry Pi 4 | aarch64 | bcm27xx/bcm2711 |
| Raspberry Pi 3 | armv7 | bcm27xx/bcm2710 |
| TP-Link Router | mips | ar71xx/generic |
| GL.iNet | mipsel | ramips/mt7621 |

Check your device:
```bash
cat /etc/openwrt_release
uname -m
```

---

## Example: Complete Build Process / Пример полной сборки

### Scenario: Build latest powercom driver for x86_64 OpenWrt

```bash
# On OpenWrt device / На устройстве OpenWrt:

# 1. Check space / Проверить место
df -h /tmp
# Need at least 50MB

# 2. Install tools / Установить инструменты
opkg update
opkg install gcc make libtool autoconf automake

# 3. Get source / Получить исходники
cd /tmp
wget https://github.com/dgok50/nut/archive/refs/heads/copilot/upgrade-nut-serial-driver-again.tar.gz
tar xzf copilot-upgrade-nut-serial-driver-again.tar.gz
cd nut-copilot-upgrade-nut-serial-driver-again

# 4. Configure minimal build / Минимальная конфигурация
./autogen.sh
./configure \
    --prefix=/usr \
    --with-serial=yes \
    --with-drivers=powercom \
    --with-user=root \
    --disable-dependency-tracking \
    --enable-strip

# 5. Build / Собрать
make -C drivers powercom

# 6. Install / Установить
cp drivers/powercom /usr/lib/nut/
chmod +x /usr/lib/nut/powercom

# 7. Test / Проверить
/usr/lib/nut/powercom -V

# 8. Cleanup / Очистка
cd /
rm -rf /tmp/nut-*
```

---

## Getting Help / Получение помощи

**OpenWrt Forums:**
- https://forum.openwrt.org/

**NUT Mailing List:**
- https://lists.alioth.debian.org/mailman/listinfo/nut-upsuser

**This Repository:**
- https://github.com/dgok50/nut/issues

**Share debug output:**
```bash
# Collect system info / Собрать информацию о системе
uname -a > debug.txt
cat /etc/openwrt_release >> debug.txt
df -h >> debug.txt
opkg list-installed | grep -E "gcc|nut" >> debug.txt

# Test driver / Проверить драйвер
/usr/lib/nut/powercom -s test -x port=/dev/ttyUSB0 -d 1 -DDDDD >> debug.txt 2>&1

# Share debug.txt
```

---

## Summary / Итого

**НЕЛЬЗЯ / CANNOT:**
- ❌ Run Fedora binary on OpenWrt
- ❌ Copy executables between different Linux distributions
- ❌ Use binaries from different architectures

**МОЖНО / CAN:**
- ✅ Install from OpenWrt package repository
- ✅ Build directly on OpenWrt device
- ✅ Cross-compile with OpenWrt SDK
- ✅ Build static binaries (with care)

**Рекомендация / Recommendation:**
```bash
opkg install nut-driver-powercom
```

If not available:
```bash
cd /tmp && [build from source on device]
```

---

## Quick Reference / Быстрая справка

```bash
# Check architecture / Проверить архитектуру
uname -m

# Check binary / Проверить бинарник
file powercom

# Check libraries / Проверить библиотеки
ldd powercom

# Install from repo / Установить из репозитория
opkg update && opkg install nut-driver-powercom

# Build on device / Собрать на устройстве
opkg install gcc make && cd /tmp && [compile]

# Test driver / Проверить драйвер
/usr/lib/nut/powercom -h
```
