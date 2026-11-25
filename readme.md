  Stack

  • backend: Spring Boot 3.5.3, Java 21, JPA, PostgreSQL, JUnit 5


  • frontend: Angular 21.2, Jest (jest-preset-angular), nginx + /api proxy

  Run

  docker compose up --build

  • App: http://localhost:4200  
  • API: http://localhost:8080/api/health  
  • Postgres: localhost:5432 (demo / demo / demo)

  Tests

  • Backend (JUnit)
  docker run --rm -v "$PWD/backend:/app" -w /app maven:3.9.9-eclipse-temurin-21 mvn test
  • Frontend (Jest)
  docker run --rm -v "$PWD/frontend:/app" -w /app node:22-bookworm npm test
