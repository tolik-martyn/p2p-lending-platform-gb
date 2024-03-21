### 1. Базы данных (PostgreSQL)

- Создание баз данных для микросервисов `user-service`, `lender-service`, `borrower-service`, `loan-service`:

 ```bash
 docker -compose up -d
 ```

---

### 2. Prometheus и Grafana

- 2.1. Создание тома для данных Prometheus:
```bash
docker volume create prometheus-data
```

- 2.2. Запуск контейнера Prometheus:
```bash
docker run --rm --detach --name my-prometheus --publish 9090:9090 --volume prometheus-data:/prometheus --volume .\prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
```

- 2.3. Запуск контейнера Grafana:
```bash
docker run -d --name=grafana -p 3000:3000 grafana/grafana
```

---

### Примечания:

- Команды для Docker нужно выполнять из корневой папки проекта `graduation project`.
- Сами микросервисы нужно будет запускать вручную через IDE.