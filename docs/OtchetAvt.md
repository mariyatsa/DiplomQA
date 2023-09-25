# Отчёт по итогам автоматизации

**Проведено автоматизированное тестирование веб-сервиса покупки туров для обоих вариантов покупки:**
+ Обычная оплата по дебетовой карте
+ Выдача кредита по данным банковской карты.

**Было запланировано:**
+ Реализовать набор UI-тестов по предоставленным данным
    + 4444 4444 4444 4441, status: 'APPROVED'
    + 4444 4444 4444 4442, status: 'DECLINED'
+ Проверить поддержку заявленных СУБД (MySQL, PostgreSQL);

**Было сделано:**
+ реализован набор UI-тестов (87 тест-кейсов);
+ подтверждена заявленная поддержка СУБД (MySQL, PostgreSQL);

**Сработавшие риски:**
+ трудности с поиском необходимых CSS-селекторов
+ наличие различных багов в тестируемом приложении;
+ сложности, связанные с необходимостью работы сразу с двумя БД - MySQL и PostgreSQL;
+ Отсутствие необходимой документации привело к дополнительным временным затратам.

**Общий итог по времени:**
+ Планирование - запланировано 12 часов, фактически - 20 (дополнительное время затрачено на доработку тестового покрытия);
+ Автоматизация - запланировано 40 часов, фактически - 50 (дополнительное время затрачено на поиск необходимых CSS-селекторов);
+ Отчётные документы по итогам тестирования - запланировано 8 часов, фактически - 4;
+ Отчётные документы по итогам автоматизации - запланировано 8 часов, фактически - 4;

Итого запланировано - 68 часов
Итого затрачено - 78 часов

Ключевая задача - автоматизировать сценарии (как позитивные, так и негативные) покупки тура - достигнута.