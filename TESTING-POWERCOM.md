# Testing PowerCom Driver Without Full NUT Installation

## Тестирование драйвера PowerCom без полной установки NUT
## Testing PowerCom Driver Without Full NUT Installation

This guide shows how to test the PowerCom driver standalone, without needing a full NUT installation or even real UPS hardware.

---

## Quick Start / Быстрый старт

### Build the Driver / Собрать драйвер

```bash
cd /path/to/nut
./autogen.sh
./configure --with-serial=yes --with-drivers=powercom
make -C drivers powercom
```

The compiled driver will be at: `drivers/powercom`

---

## Testing Methods / Методы тестирования

### 1. Debug Mode (-D flag) / Режим отладки

Run the driver in foreground with verbose debug output:

```bash
# Basic debug mode / Базовая отладка
./drivers/powercom -a myups -D

# More verbose / Больше подробностей
./drivers/powercom -a myups -DDD

# Maximum verbosity / Максимум информации
./drivers/powercom -a myups -DDDDD
```

**What you'll see / Что вы увидите:**
- Serial port communication
- Protocol detection (COM1/COM2)
- Data parsing
- Variable updates
- Error messages

**Example output / Пример вывода:**
```
Network UPS Tools - PowerCom UPS driver 0.27 (2.8.4.1)
Using protocol: auto
Opening serial port: /dev/ttyS0
Protocol detection: trying COM1 at 1200 baud
COM1: Sending command 0x01
COM1: Received 16 bytes: [binary data]
Auto-detected protocol: COM1 (binary, 1200 baud)
ups.status: OL
battery.charge: 100.0
input.voltage: 230.0
```

---

### 2. Data Dump Mode (-d flag) / Режим дампа данных

Collect data samples and exit automatically:

```bash
# Get 5 samples and exit / 5 образцов и выход
./drivers/powercom -a myups -d 5

# Get 1 sample with debug / 1 образец с отладкой
./drivers/powercom -a myups -d 1 -DDD
```

**Use cases / Применение:**
- Quick protocol verification / Быстрая проверка протокола
- Capture data for bug reports / Захват данных для отчетов об ошибках
- Test configuration changes / Тест изменений конфигурации

---

### 3. Standalone Mode (-s flag) / Автономный режим

Configure driver entirely from command line without ups.conf:

```bash
# COM2 protocol test / Тест протокола COM2
./drivers/powercom -s myups \
    -x port=/dev/ttyS0 \
    -x protocol_mode=com2 \
    -x modelname="IMP-525" \
    -D

# COM1 protocol test / Тест протокола COM1
./drivers/powercom -s myups \
    -x port=/dev/ttyS0 \
    -x protocol_mode=com1 \
    -x type=IMP \
    -x linevoltage=230 \
    -D
```

**Advantages / Преимущества:**
- No configuration files needed / Не нужны файлы конфигурации
- Easy to test different settings / Легко тестировать разные настройки
- Perfect for development / Идеально для разработки

---

### 4. List Variables (-L flag) / Список переменных

See all configurable variables and their defaults:

```bash
./drivers/powercom -L
```

**Output example / Пример вывода:**
```
VALUE protocol_mode "Protocol mode: 'auto', 'com1', or 'com2' (default: auto)"
VALUE event_hold "Event latch duration in seconds (default: 20, range: 1-300)"
VALUE allow_control "Enable dangerous control commands: 'yes' or 'no' (default: no)"
VALUE type "Type of UPS: 'Trust','Egys','KP625AP','IMP','KIN','BNT','BNT-other','OPTI'"
...
```

---

## Testing Without UPS Hardware / Тестирование без оборудования ИБП

### Using Dummy/Virtual Serial Port / Использование виртуального порта

#### Method 1: socat (Linux)

Create a virtual serial port pair:

```bash
# Install socat / Установить socat
sudo apt-get install socat  # Debian/Ubuntu
sudo yum install socat      # RHEL/CentOS

# Create virtual port pair / Создать пару виртуальных портов
socat -d -d pty,raw,echo=0 pty,raw,echo=0
```

Output will show port names like:
```
2024/02/09 21:00:00 socat[12345] N PTY is /dev/pts/3
2024/02/09 21:00:00 socat[12345] N PTY is /dev/pts/4
```

Then use one for the driver:
```bash
./drivers/powercom -s myups -x port=/dev/pts/3 -DDD
```

And simulate UPS responses on the other:
```bash
# In another terminal / В другом терминале
cat /dev/pts/4  # See what driver sends
echo -ne '\x01\x02\x03...' > /dev/pts/4  # Send responses
```

#### Method 2: null modem cable simulator

```bash
# Create symbolic links / Создать символические ссылки
ln -s /dev/zero /tmp/fake_ups_port

# Test with no real data / Тест без реальных данных
./drivers/powercom -s myups -x port=/tmp/fake_ups_port -d 1 -DDD
```

---

## Testing Specific Features / Тестирование конкретных функций

### Test COM2 Protocol / Тест протокола COM2

```bash
./drivers/powercom -s test \
    -x port=/dev/ttyUSB0 \
    -x protocol_mode=com2 \
    -x event_hold=30 \
    -d 10 -DDDDD
```

**What to check / Что проверить:**
- Q1/DQ1 command alternation / Чередование команд Q1/DQ1
- ASCII response parsing / Разбор ASCII ответов
- Event detection and latching / Обнаружение и удержание событий
- Temperature readings / Показания температуры
- Battery voltage / Напряжение батареи

### Test COM1 Protocol / Тест протокола COM1

```bash
./drivers/powercom -s test \
    -x port=/dev/ttyUSB0 \
    -x protocol_mode=com1 \
    -x type=IMP \
    -d 10 -DDDDD
```

**What to check / Что проверить:**
- Binary frame communication / Бинарная связь
- Checksum validation / Проверка контрольной суммы
- Model detection / Определение модели
- Status bit parsing / Разбор битов статуса

### Test Auto-Detection / Тест автоопределения

```bash
./drivers/powercom -s test \
    -x port=/dev/ttyUSB0 \
    -x protocol_mode=auto \
    -DDDDD
```

**What to check / Что проверить:**
- Protocol probing order (COM1 first, then COM2) / Порядок проверки
- Successful protocol selection / Успешный выбор протокола
- Fallback behavior / Поведение при отказе
- Switch cooldown (10 seconds) / Задержка переключения

---

## Configuration File Testing / Тестирование с файлом конфигурации

### Create test ups.conf / Создать тестовый ups.conf

```bash
mkdir -p /tmp/nut-test
cat > /tmp/nut-test/ups.conf << 'EOF'
[testups]
    driver = powercom
    port = /dev/ttyUSB0
    desc = "Test UPS"
    protocol_mode = auto
    event_hold = 20
EOF

# Set state path / Установить путь состояния
export NUT_STATEPATH=/tmp/nut-test
export NUT_CONFPATH=/tmp/nut-test

# Run driver / Запустить драйвер
./drivers/powercom -a testups -DDD
```

---

## Troubleshooting / Устранение неполадок

### Permission Denied on Serial Port / Нет доступа к порту

```bash
# Add user to dialout group / Добавить пользователя в группу
sudo usermod -a -G dialout $USER

# Or change port permissions / Или изменить права
sudo chmod 666 /dev/ttyUSB0
```

### Driver Can't Open Port / Драйвер не может открыть порт

```bash
# Check if port exists / Проверить существование порта
ls -l /dev/ttyUSB* /dev/ttyS*

# Check if port is in use / Проверить занят ли порт
lsof /dev/ttyUSB0

# Test port access / Проверить доступ
echo "test" > /dev/ttyUSB0
```

### No Communication / Нет связи

```bash
# Check cable connection / Проверить подключение кабеля
# Try different baud rates / Попробовать разные скорости
# Check flow control settings / Проверить настройки управления потоком

# Manual port test / Ручной тест порта
stty -F /dev/ttyUSB0 1200 cs8 -cstopb -parenb
cat /dev/ttyUSB0 &
echo -ne '\x01' > /dev/ttyUSB0
```

### Protocol Detection Fails / Не удается определить протокол

```bash
# Force specific protocol / Принудительно задать протокол
./drivers/powercom -s test \
    -x port=/dev/ttyUSB0 \
    -x protocol_mode=com1 \
    -DDDDD

# Try different type / Попробовать другой тип
./drivers/powercom -s test \
    -x port=/dev/ttyUSB0 \
    -x type=BNT-other \
    -DDDDD
```

---

## Collecting Debug Information / Сбор отладочной информации

### For Bug Reports / Для отчетов об ошибках

```bash
# Collect comprehensive debug output / Собрать полный вывод отладки
./drivers/powercom -a myups -DDDDD 2>&1 | tee powercom-debug.log

# Dump 50 samples with timestamps / Дамп 50 образцов с метками времени
./drivers/powercom -a myups -d 50 -DDDDD 2>&1 | ts | tee powercom-dump.log

# Test both protocols / Тест обоих протоколов
echo "=== Testing COM1 ===" > protocol-test.log
./drivers/powercom -s test -x port=/dev/ttyUSB0 -x protocol_mode=com1 -d 5 -DDDDD >> protocol-test.log 2>&1
echo "=== Testing COM2 ===" >> protocol-test.log
./drivers/powercom -s test -x port=/dev/ttyUSB0 -x protocol_mode=com2 -d 5 -DDDDD >> protocol-test.log 2>&1
```

---

## Advanced Testing / Продвинутое тестирование

### Test Event Latching / Тест удержания событий

```bash
# Start with long event hold / Запустить с длинным удержанием
./drivers/powercom -s test \
    -x port=/dev/ttyUSB0 \
    -x protocol_mode=com2 \
    -x event_hold=60 \
    -DDD
```

Then simulate power event and monitor if it stays visible for 60 seconds.

### Test Instant Commands / Тест мгновенных команд

```bash
# Start driver in one terminal / Запустить драйвер в одном терминале
./drivers/powercom -a myups -DDD

# In another terminal, test commands / В другом терминале
./drivers/upscmd -l myups                    # List commands
./drivers/upscmd myups beeper.toggle         # Toggle beeper
./drivers/upscmd myups test.battery.start    # Start battery test
```

### Performance Testing / Тест производительности

```bash
# Measure response time / Измерить время отклика
time ./drivers/powercom -s test -x port=/dev/ttyUSB0 -d 1 -D

# Monitor CPU usage / Мониторинг использования CPU
./drivers/powercom -a myups -DDD &
top -p $!
```

---

## Integration Testing / Интеграционное тестирование

### Test with upsd (optional) / Тест с upsd (опционально)

If you have upsd compiled but not installed:

```bash
# Start driver / Запустить драйвер
export NUT_STATEPATH=/tmp/nut-test
./drivers/powercom -a myups -DDD &

# Start upsd / Запустить upsd
./server/upsd -D

# Query with upsc / Запрос с upsc
./clients/upsc myups
```

---

## Test Scenarios / Тестовые сценарии

### Scenario 1: New User Setup / Сценарий 1: Новый пользователь

```bash
# 1. List available variables / Список переменных
./drivers/powercom -L

# 2. Test connection / Проверить подключение
./drivers/powercom -s test -x port=/dev/ttyUSB0 -d 1 -DDD

# 3. Auto-detect protocol / Автоопределение протокола
./drivers/powercom -s test -x port=/dev/ttyUSB0 -x protocol_mode=auto -d 5 -DD

# 4. If works, create permanent config / Если работает, создать конфигурацию
# Edit /etc/nut/ups.conf
```

### Scenario 2: Protocol Investigation / Сценарий 2: Исследование протокола

```bash
# Test COM1 / Тест COM1
./drivers/powercom -s test -x port=/dev/ttyUSB0 -x protocol_mode=com1 -d 10 -DDDDD > com1.log 2>&1

# Test COM2 / Тест COM2
./drivers/powercom -s test -x port=/dev/ttyUSB0 -x protocol_mode=com2 -d 10 -DDDDD > com2.log 2>&1

# Compare / Сравнить
diff com1.log com2.log
```

### Scenario 3: Debugging Connection Issues / Сценарий 3: Отладка проблем

```bash
# Maximum debug output / Максимум отладки
./drivers/powercom -s test \
    -x port=/dev/ttyUSB0 \
    -x protocol_mode=auto \
    -DDDDD 2>&1 | tee full-debug.log

# Look for / Искать:
# - "Opening serial port" - port access / доступ к порту
# - "Auto-detected protocol" - protocol detection / определение протокола
# - "COM1/COM2: Sending" - communication / связь
# - "Failed to" - errors / ошибки
```

---

## Expected Output Examples / Примеры ожидаемого вывода

### Successful COM2 Detection / Успешное определение COM2

```
Network UPS Tools - PowerCom UPS driver 0.27
   0.000000	Opening serial port: /dev/ttyUSB0
   0.100000	First-time protocol detection: probing COM1 and COM2
   0.150000	COM1 (1200 baud, binary) responds
   0.200000	COM2 (2400 baud, ASCII) responds
   0.200100	Auto-detected protocol: COM2 (ASCII, 2400 baud) - preferred
   0.300000	COM2: Sending DQ1 command (iteration 0)
   0.350000	COM2: Received 45 bytes: (230.0 230.0 40 50.0 100 25.0 00000000
   2.000000	ups.status: OL
   2.000100	battery.charge: 100.0
   2.000200	battery.voltage: 13.5
   2.000300	ups.temperature: 25.0
```

### Successful COM1 Detection / Успешное определение COM1

```
Network UPS Tools - PowerCom UPS driver 0.27
   0.000000	Opening serial port: /dev/ttyS0
   0.100000	First-time protocol detection: probing COM1 and COM2
   0.150000	COM1 (1200 baud, binary) responds
   0.200000	COM2 (2400 baud, ASCII) no response
   0.200100	Auto-detected protocol: COM1 (binary, 1200 baud) - fallback
   0.300000	Detected: IMP-525AP, 230V
   2.000000	ups.status: OL
   2.000100	battery.charge: 100.0
```

---

## Useful Commands Reference / Справочник полезных команд

```bash
# Quick test / Быстрый тест
./drivers/powercom -s test -x port=/dev/ttyUSB0 -d 1 -D

# Debug mode / Режим отладки
./drivers/powercom -a myups -DDDDD

# List variables / Список переменных
./drivers/powercom -L

# Version / Версия
./drivers/powercom -V

# Help / Справка
./drivers/powercom -h

# Foreground mode / Режим переднего плана
./drivers/powercom -a myups -F

# With custom state path / С пользовательским путем
export NUT_STATEPATH=/tmp/test
./drivers/powercom -a myups -D
```

---

## Tips / Советы

1. **Always use -D flags for testing** / Всегда используйте флаги -D для тестирования
   - More D's = more verbosity / Больше D = больше подробностей
   - -DDDDD is maximum / -DDDDD это максимум

2. **Use -d for quick checks** / Используйте -d для быстрых проверок
   - Faster than Ctrl+C / Быстрее чем Ctrl+C
   - Automatic exit / Автоматический выход

3. **Test in standalone mode first** / Сначала тестируйте в автономном режиме
   - No config files needed / Не нужны файлы конфигурации
   - Easy to change settings / Легко менять настройки

4. **Save debug output** / Сохраняйте вывод отладки
   - Use `tee` or `> file.log` / Используйте tee или > file.log
   - Essential for bug reports / Необходимо для отчетов об ошибках

5. **Check permissions first** / Сначала проверьте права
   - Serial ports need special access / Порты требуют особого доступа
   - Add user to dialout group / Добавьте пользователя в группу dialout

---

## Resources / Ресурсы

- **NUT Documentation**: https://networkupstools.org/docs/
- **PowerCom Driver**: https://networkupstools.org/docs/man/powercom.html
- **Developer Guide**: docs/developers.txt in NUT source
- **Mailing List**: https://lists.alioth.debian.org/mailman/listinfo/nut-upsuser

---

## Summary / Резюме

**Вы можете тестировать драйвер PowerCom без:**
**You can test the PowerCom driver without:**

- ❌ Full NUT installation / Полной установки NUT
- ❌ Running daemons / Работающих демонов
- ❌ Configuration files / Файлов конфигурации
- ❌ Root privileges (for most cases) / Root прав (в большинстве случаев)

**Вам нужно только:**
**You only need:**

- ✅ Compiled driver binary / Скомпилированный бинарник драйвера
- ✅ Serial port access / Доступ к последовательному порту
- ✅ Debug flags (-D, -d, -s) / Флаги отладки

**Лучший способ начать:**
**Best way to start:**

```bash
./drivers/powercom -s test -x port=/dev/ttyUSB0 -d 5 -DDD
```

Этот способ даст вам 5 образцов данных с подробным выводом отладки!
This will give you 5 data samples with detailed debug output!
