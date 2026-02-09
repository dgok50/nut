# How to Build ONLY PowerCom Driver

## Как собрать ТОЛЬКО драйвер PowerCom
## How to Build ONLY PowerCom Driver

**Quick answer / Быстрый ответ:**

```bash
./configure --with-drivers=powercom --with-serial=yes
make -C drivers powercom
```

---

## Prerequisites / Предварительные требования

Before building, install required tools / Перед сборкой установите необходимые инструменты:

### Fedora/RHEL/CentOS:
```bash
sudo dnf install gcc make libtool autoconf automake pkgconfig git
```

### Ubuntu/Debian:
```bash
sudo apt-get install build-essential libtool libtool-bin autoconf automake pkg-config git
```

### OpenWrt:
```bash
opkg update
opkg install gcc make libtool autoconf automake pkgconfig
```

**Why these packages? / Зачем эти пакеты?**
- `gcc`, `make` - Compiler and build tools / Компилятор и инструменты сборки
- `libtool` - Provides `libtoolize` command / Предоставляет команду `libtoolize`
- `autoconf`, `automake` - Generate configure scripts / Генерация скриптов configure
- `pkgconfig` - Manage library dependencies / Управление зависимостями библиотек
- `git` - Clone repository / Клонирование репозитория

---

## Minimal Build / Минимальная сборка

### Method 1: Standard Minimal Build / Стандартная минимальная сборка

```bash
# Step 1: Configure for powercom only / Настроить только для powercom
./configure \
    --with-drivers=powercom \
    --with-serial=yes

# Step 2: Build only powercom driver / Собрать только драйвер powercom
make -C drivers powercom

# Done! Binary at: drivers/powercom
# Готово! Бинарник: drivers/powercom
```

**Time / Время:** ~2 minutes / ~2 минуты

---

### Method 2: Super Minimal (Fastest) / Супер минимальная (Быстрейшая)

Skip documentation, SSL, USB, and other unnecessary components:

```bash
# Configure with minimal options / Минимальные опции
./configure \
    --with-drivers=powercom \
    --with-serial=yes \
    --without-doc \
    --without-ssl \
    --without-usb \
    --without-snmp \
    --without-neon \
    --without-powerman \
    --without-ipmi \
    --without-freeipmi

# Build only powercom / Собрать только powercom
make -C drivers powercom
```

**Time / Время:** ~1 minute / ~1 минута

---

### Method 3: For OpenWrt (Even More Minimal) / Для OpenWrt (Ещё минимальнее)

```bash
./configure \
    --prefix=/usr \
    --sysconfdir=/etc/nut \
    --with-drivers=powercom \
    --with-serial=yes \
    --with-user=root \
    --with-group=root \
    --disable-dependency-tracking \
    --enable-strip \
    --without-ssl \
    --without-usb \
    --without-snmp \
    --without-doc

make -C drivers powercom
```

**Time / Время:** ~1 minute / ~1 минута

---

## Verification / Проверка

After building, verify the driver:

```bash
# Check binary exists / Проверить что бинарник создан
ls -lh drivers/powercom

# Check it's executable / Проверить что исполняемый
file drivers/powercom

# Test help / Проверить справку
./drivers/powercom -h

# Test version / Проверить версию
./drivers/powercom -V
```

---

## Full Process Example / Полный пример процесса

### On Fedora/Ubuntu / На Fedora/Ubuntu:

```bash
# 1. Get source / Получить исходники
git clone https://github.com/dgok50/nut.git
cd nut

# 2. Generate configure script / Создать скрипт configure
./autogen.sh

# 3. Configure for powercom only / Настроить только для powercom
./configure --with-drivers=powercom --with-serial=yes

# 4. Build powercom driver / Собрать драйвер powercom
make -C drivers powercom

# 5. Check result / Проверить результат
ls -lh drivers/powercom
./drivers/powercom -V
```

**Total time / Общее время:** ~3-5 minutes / ~3-5 минут

---

### On OpenWrt Device / На устройстве OpenWrt:

```bash
# 1. Install build tools / Установить инструменты
opkg update
opkg install gcc make libtool autoconf automake

# 2. Get source / Получить исходники
cd /tmp
git clone https://github.com/dgok50/nut.git
cd nut

# 3. Configure minimal / Минимальная настройка
./autogen.sh
./configure \
    --prefix=/usr \
    --with-drivers=powercom \
    --with-serial=yes \
    --without-doc \
    --without-ssl \
    --without-usb

# 4. Build / Собрать
make -C drivers powercom

# 5. Install / Установить
cp drivers/powercom /usr/lib/nut/
chmod +x /usr/lib/nut/powercom

# 6. Test / Проверить
/usr/lib/nut/powercom -V
```

**Total time / Общее время:** ~5-10 minutes / ~5-10 минут

---

## Comparison / Сравнение

### Build Times / Время сборки:

| Method | Time | Output Size |
|--------|------|-------------|
| **Full NUT build** | 10-15 min | All drivers + server + clients |
| **Only powercom** | 2-3 min | Just powercom binary |
| **Minimal powercom** | 1-2 min | Just powercom binary (smaller) |

### What Gets Built / Что собирается:

**Full build (`make`):**
- ✅ All drivers (~50+ drivers)
- ✅ upsd server
- ✅ upsc client
- ✅ upsmon
- ✅ Documentation
- ⏱️ Time: 10-15 minutes

**Only powercom (`make -C drivers powercom`):**
- ✅ Just powercom driver
- ❌ No other drivers
- ❌ No server/clients
- ❌ No documentation
- ⏱️ Time: 1-2 minutes

---

## Why Use `make -C drivers powercom`?

**`make -C drivers powercom` explanation:**

- `make` - Build tool
- `-C drivers` - Change to `drivers/` directory
- `powercom` - Target: build only powercom

**Alternative (slower):**
```bash
cd drivers
make powercom
cd ..
```

**Full build (much slower):**
```bash
make  # Builds EVERYTHING (10+ minutes)
```

---

## Troubleshooting / Устранение неполадок

### Error: "Can't exec 'libtoolize': No such file or directory"

**Full error:**
```
autoreconf: running: libtoolize --copy
Can't exec "libtoolize": No such file or directory
autoreconf: error: libtoolize failed with exit status: 2
```

**Cause / Причина:** The `libtool` package is not installed.

**Solution / Решение:**
```bash
# Fedora/RHEL/CentOS:
sudo dnf install libtool

# Ubuntu/Debian:
sudo apt-get install libtool libtool-bin

# OpenWrt:
opkg install libtool

# Then run autogen.sh again:
./autogen.sh
```

**Install all build dependencies at once / Установить все зависимости сразу:**
```bash
# Fedora:
sudo dnf install gcc make libtool autoconf automake pkgconfig

# Ubuntu/Debian:
sudo apt-get install build-essential libtool libtool-bin autoconf automake pkg-config

# OpenWrt:
opkg install gcc make libtool autoconf automake pkgconfig
```

---

### Error: "configure: command not found"

**Solution / Решение:**
```bash
./autogen.sh
```

### Error: "No rule to make target 'powercom'"

**Solution / Решение:**
```bash
# You must configure first / Сначала нужно настроить
./configure --with-drivers=powercom --with-serial=yes
```

### Error: "gcc: command not found"

**Solution / Решение:**
```bash
# Install compiler / Установить компилятор
# Fedora:
sudo dnf install gcc make

# Ubuntu/Debian:
sudo apt-get install build-essential

# OpenWrt:
opkg install gcc make
```

### Error: Missing dependencies

**Solution / Решение:**
```bash
# Fedora:
sudo dnf install libtool autoconf automake

# Ubuntu/Debian:
sudo apt-get install libtool autoconf automake

# OpenWrt:
opkg install libtool autoconf automake
```

---

## After Building / После сборки

### Install / Установить:

```bash
# Copy to system location / Скопировать в системную директорию
sudo cp drivers/powercom /usr/lib/nut/
sudo chmod +x /usr/lib/nut/powercom

# Or install with make (installs everything) / Или установить через make
sudo make install
```

### Test / Проверить:

```bash
# Test help / Проверить справку
/usr/lib/nut/powercom -h

# Test with UPS / Проверить с ИБП
/usr/lib/nut/powercom -s test -x port=/dev/ttyUSB0 -d 1 -DDD
```

### Create Configuration / Создать конфигурацию:

```bash
# Create config directory / Создать директорию конфигурации
sudo mkdir -p /etc/nut

# Create ups.conf / Создать ups.conf
sudo cat > /etc/nut/ups.conf << 'EOF'
[myups]
    driver = powercom
    port = /dev/ttyUSB0
    desc = "PowerCom UPS"
    protocol_mode = auto
EOF

# Test configuration / Проверить конфигурацию
/usr/lib/nut/powercom -a myups -DDD
```

---

## Advanced: Static Build / Продвинутое: Статическая сборка

Build a static binary that doesn't need dynamic libraries:

```bash
./configure \
    --with-drivers=powercom \
    --with-serial=yes \
    --disable-shared \
    --enable-static \
    LDFLAGS="-static -static-libgcc"

make -C drivers powercom

# Result: larger file but works anywhere / Результат: больше файл, но работает везде
strip drivers/powercom
```

**Use case / Применение:**
- Transfer to different systems
- Embedded devices
- No library dependencies

**Note / Примечание:** Static binary is much larger (~2 MB vs ~300 KB)

---

## Quick Reference Card / Быстрая справка

### Fastest Way / Самый быстрый способ:

```bash
git clone https://github.com/dgok50/nut.git && cd nut
./autogen.sh
./configure --with-drivers=powercom --with-serial=yes
make -C drivers powercom
./drivers/powercom -V
```

### On OpenWrt / На OpenWrt:

```bash
opkg install gcc make libtool autoconf automake
cd /tmp && git clone https://github.com/dgok50/nut.git && cd nut
./autogen.sh
./configure --prefix=/usr --with-drivers=powercom --with-serial=yes
make -C drivers powercom
cp drivers/powercom /usr/lib/nut/
```

### For Development / Для разработки:

```bash
# Configure once / Настроить один раз
./configure --with-drivers=powercom --with-serial=yes --enable-maintainer-mode

# Rebuild quickly after changes / Быстро пересобрать после изменений
make -C drivers powercom

# Test immediately / Сразу проверить
./drivers/powercom -s test -x port=/dev/ttyUSB0 -d 1 -D
```

---

## Related Documentation / Связанная документация

- **TESTING-POWERCOM.md** - How to test without full NUT installation
- **BUILDING-OPENWRT.md** - How to build for OpenWrt systems
- **test-powercom.sh** - Automated testing script

---

## Summary / Итого

**Question / Вопрос:** How to build only powercom driver?  
**Как собрать только драйвер powercom?**

**Answer / Ответ:**
```bash
./configure --with-drivers=powercom --with-serial=yes
make -C drivers powercom
```

**Time / Время:** 1-2 minutes instead of 10+ minutes  
**1-2 минуты вместо 10+ минут**

**Result / Результат:** `drivers/powercom` binary  
**Бинарник `drivers/powercom`**

Done! / Готово! 🚀
