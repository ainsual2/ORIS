# nats_homework

Сервис погоды на NATS из двух приложений:

- `weather-publisher` — каждую секунду публикует текущие погодные данные в тему `Weather`.
- `weather-service` — подписывается на тему `Weather`, хранит последнее сообщение и отдаёт его по HTTP.

## Как это работает

1. `weather-publisher` подключается к NATS (`NATS_URL`) и отправляет JSON с погодой в subject `Weather`.
2. `weather-service` подключается к тому же NATS и подписывается на `Weather`.
3. Последнее полученное сообщение доступно по `GET /api/weather`.
4. Статическая страница `http://localhost:8080/` раз в секунду запрашивает `/api/weather` и показывает данные.

## Запуск

### Вариант 1. Полностью в Docker (включая NATS)

```bash
docker compose -f nats_homework/docker-compose.yaml up --build
```

После запуска:
- UI: `http://localhost:8080/`
- API: `http://localhost:8080/api/weather`

### Вариант 2. NATS отдельно, сервисы в Docker

По условию задачи NATS в Docker запускать не обязательно. Если у тебя NATS уже запущен локально/на сервере,
передай адрес в переменную `NATS_URL`.

Пример для ручного запуска контейнеров из папок модулей:

```bash
cd nats_homework/weather-service
docker build -t weather-service .
docker run --rm -p 8080:8080 -e NATS_URL=nats://host.docker.internal:4222 weather-service
```

```bash
cd nats_homework/weather-publisher
docker build -t weather-publisher .
docker run --rm -e NATS_URL=nats://host.docker.internal:4222 weather-publisher
```

> Для Linux вместо `host.docker.internal` иногда нужен IP хоста в docker-сети.

## Проверка

1. Проверить API погоды:

```bash
curl -i http://localhost:8080/api/weather
```

- `200 OK` + JSON: данные из NATS приходят.
- `204 No Content`: сервис пока не получил сообщений.

2. Проверить статус машины:

```bash
curl -i http://localhost:8080/api/status/1
```

3. Обновить ресурс машины:

```bash
curl -i -X POST "http://localhost:8080/api/resource?id=1&resource=75"
```

## Что было исправлено

- Починен Maven parent в `nats_homework/pom.xml`.
- Обновлены некорректные зависимости Spring Boot в обоих модулях.
- Убрана потенциальная `NullPointerException` в `MachineStatusService` при первом запросе.
- `POST /api/resource` теперь реально обновляет ресурс, а не просто возвращает success.
- `weather-service` теперь не падает на старте, если NATS временно недоступен.
