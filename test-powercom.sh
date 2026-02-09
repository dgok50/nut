#!/bin/bash
# PowerCom Driver Test Script
# Скрипт тестирования драйвера PowerCom
#
# Usage / Использование:
#   ./test-powercom.sh [port] [protocol]
#
# Examples / Примеры:
#   ./test-powercom.sh /dev/ttyUSB0 auto
#   ./test-powercom.sh /dev/ttyS0 com2
#   ./test-powercom.sh /dev/pts/3 com1

set -e

# Colors / Цвета
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration / Конфигурация
DRIVER="./drivers/powercom"
PORT="${1:-/dev/ttyUSB0}"
PROTOCOL="${2:-auto}"
SAMPLES="${3:-5}"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}PowerCom Driver Test Script${NC}"
echo -e "${BLUE}Скрипт тестирования драйвера PowerCom${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check if driver exists / Проверка существования драйвера
if [ ! -f "$DRIVER" ]; then
    echo -e "${RED}Error: Driver not found at $DRIVER${NC}"
    echo -e "${RED}Ошибка: Драйвер не найден по пути $DRIVER${NC}"
    echo ""
    echo "Please build the driver first:"
    echo "Пожалуйста, сначала соберите драйвер:"
    echo ""
    echo "  cd /path/to/nut"
    echo "  ./autogen.sh"
    echo "  ./configure --with-serial=yes --with-drivers=powercom"
    echo "  make -C drivers powercom"
    echo ""
    exit 1
fi

# Check if driver is executable / Проверка исполняемости
if [ ! -x "$DRIVER" ]; then
    echo -e "${RED}Error: Driver is not executable${NC}"
    echo -e "${RED}Ошибка: Драйвер не исполняемый${NC}"
    echo ""
    echo "Run: chmod +x $DRIVER"
    echo ""
    exit 1
fi

echo -e "${GREEN}✓${NC} Driver found: $DRIVER"
echo -e "${GREEN}✓${NC} Драйвер найден: $DRIVER"
echo ""

# Display test configuration / Показать конфигурацию теста
echo -e "${YELLOW}Test Configuration / Конфигурация теста:${NC}"
echo "  Port / Порт:      $PORT"
echo "  Protocol:         $PROTOCOL"
echo "  Samples / Образцы: $SAMPLES"
echo ""

# Check port existence / Проверка существования порта
if [ ! -e "$PORT" ] && [[ ! "$PORT" =~ ^/dev/pts/ ]]; then
    echo -e "${YELLOW}Warning: Port $PORT does not exist${NC}"
    echo -e "${YELLOW}Предупреждение: Порт $PORT не существует${NC}"
    echo ""
    echo "Available serial ports / Доступные последовательные порты:"
    ls -l /dev/ttyUSB* /dev/ttyS* /dev/ttyACM* 2>/dev/null || echo "  None found / Не найдено"
    echo ""
    read -p "Continue anyway? / Продолжить всё равно? (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# Check port permissions / Проверка прав доступа к порту
if [ -e "$PORT" ]; then
    if [ ! -r "$PORT" ] || [ ! -w "$PORT" ]; then
        echo -e "${YELLOW}Warning: Insufficient permissions for $PORT${NC}"
        echo -e "${YELLOW}Предупреждение: Недостаточно прав для $PORT${NC}"
        echo ""
        echo "Solutions / Решения:"
        echo "  1. Add user to dialout group / Добавить пользователя в группу:"
        echo "     sudo usermod -a -G dialout \$USER"
        echo "     (then logout and login / затем выйти и войти)"
        echo ""
        echo "  2. Or change port permissions / Или изменить права:"
        echo "     sudo chmod 666 $PORT"
        echo ""
        read -p "Continue anyway? / Продолжить всё равно? (y/n): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    else
        echo -e "${GREEN}✓${NC} Port accessible: $PORT"
        echo -e "${GREEN}✓${NC} Порт доступен: $PORT"
    fi
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Starting Test / Запуск теста${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Create temporary state directory / Создать временную директорию состояния
TEMP_DIR=$(mktemp -d)
export NUT_STATEPATH="$TEMP_DIR"

echo "Using temporary state path / Используется временный путь:"
echo "  $NUT_STATEPATH"
echo ""

# Cleanup function / Функция очистки
cleanup() {
    echo ""
    echo -e "${YELLOW}Cleaning up / Очистка...${NC}"
    rm -rf "$TEMP_DIR"
    echo -e "${GREEN}Done / Готово${NC}"
}
trap cleanup EXIT

# Run the test / Запустить тест
echo -e "${BLUE}Running driver with $SAMPLES samples...${NC}"
echo -e "${BLUE}Запуск драйвера с $SAMPLES образцами...${NC}"
echo ""
echo "Command / Команда:"
echo "  $DRIVER -s testups -x port=$PORT -x protocol_mode=$PROTOCOL -d $SAMPLES -DDD"
echo ""
echo "---[Output Start / Начало вывода]---"
echo ""

# Run and capture result / Запустить и захватить результат
if $DRIVER -s testups \
    -x port="$PORT" \
    -x protocol_mode="$PROTOCOL" \
    -d "$SAMPLES" \
    -DDD 2>&1; then
    
    echo ""
    echo "---[Output End / Конец вывода]---"
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}Test completed successfully!${NC}"
    echo -e "${GREEN}Тест завершен успешно!${NC}"
    echo -e "${GREEN}========================================${NC}"
    exit 0
else
    EXIT_CODE=$?
    echo ""
    echo "---[Output End / Конец вывода]---"
    echo ""
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}Test failed with exit code: $EXIT_CODE${NC}"
    echo -e "${RED}Тест не прошел с кодом: $EXIT_CODE${NC}"
    echo -e "${RED}========================================${NC}"
    echo ""
    echo "Common issues / Распространенные проблемы:"
    echo "  1. Port not accessible / Порт недоступен"
    echo "  2. Wrong protocol / Неправильный протокол"
    echo "  3. UPS not connected / ИБП не подключен"
    echo "  4. Wrong baud rate / Неправильная скорость"
    echo ""
    echo "Try / Попробуйте:"
    echo "  - Different protocol: com1, com2, auto"
    echo "  - Check cable connection / Проверьте подключение кабеля"
    echo "  - Verify port name / Проверьте имя порта"
    echo ""
    exit $EXIT_CODE
fi
