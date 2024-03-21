### 1. Базы данных (PostgreSQL)

- Создание баз данных для микросервисов `user-service`, `lender-service`, `borrower-service`, `loan-service`:

 ```bash
 docker -compose up -d
 ```
##### Примечания:

- Команды для Docker нужно выполнять из корневой папки проекта `graduation project`.
- Сами микросервисы нужно будет запускать вручную через IDE.

---

### 2. Prometheus и Grafana

- 2.1. Создание тома для данных Prometheus:
```bash
docker volume create prometheus-data
```

- 2.2. Запуск контейнера Prometheus (**команда под Windows**):
```bash
docker run --rm --detach --name my-prometheus --publish 9090:9090 --volume prometheus-data:/prometheus --volume .\prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
```

- 2.3. Запуск контейнера Grafana:
```bash
docker run -d --name=grafana -p 3000:3000 grafana/grafana
```

##### Примечания:

- Команды для Docker нужно выполнять из корневой папки проекта `graduation project`.

---

### 3. Spring Security

- Роль: `ADMIN` (полный доступ), login: `admin`, password: `adminPass`.
- Роль: `USER` (ограниченный доступ), login: `user`, password: `userPass`.

##### Стартовые endpoints:

- http://localhost:8080/users - API Gateway (user-service).
- http://localhost:8080/lenders - API Gateway (lender-service).
- http://localhost:8080/borrowers - API Gateway (borrower-service).
- http://localhost:8080/loans - API Gateway (loan-service).
- http://localhost:8081/users - user-service.
- http://localhost:8082/lenders - lender-service.
- http://localhost:8083/borrowers - borrower-service.
- http://localhost:8084/loans - loan-service.

---

### Документация API

##### SpringDoc:

- http://localhost:8081/swagger-ui/index.html - user-service.
- http://localhost:8082/swagger-ui/index.html - lender-service.
- http://localhost:8083/swagger-ui/index.html - borrower-service.
- http://localhost:8084/swagger-ui/index.html - loan-service.

---
