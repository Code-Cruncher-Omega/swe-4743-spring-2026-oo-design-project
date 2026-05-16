# swe-4743-spring-2026-oo-design-project
 
Project for Professor Jeff Adkisson's Object-Oriented Design course.
 
This application allows users to create devices, change their states and attributes, delete them, and view devices based on their qualities (device type, activity, and location), as well as manage home simulation settings and view action history.
 
The application is a Spring Boot MVC-based application that follows the SOLID principles taught during this course and applies a handful of design patterns — State, Factory, and Strategy — in its implementation.
 
## Table of Contents
 
- [Prerequisites](#prerequisites)
- [Running the App](#running-the-app)
  - [App Access Points](#app-access-points)
- [Local Development](#local-development)
  - [Backend](#backend)
  - [Frontend](#frontend)
  - [App Access Points](#app-access-points-1)
- [Bruno](#bruno)
- [Walkthroughs](#walkthroughs)
## Prerequisites
 
| Tool | Minimum Version | Check |
|---|---|---|
| Java JDK | 21 | `java -version` |
| Maven (or use the included wrapper) | 3.9+ | `mvn -v` |
| Node.js | 20+ | `node -v` |
| npm | 10+ | `npm -v` |
 
## Running the App
 
To run the application, clone the repository and run the following from the project root:
 
```bash
docker compose up
```
 
This starts the app services only; tests are not executed automatically by `docker compose up`.
 
### App Access Points
 
The app can be accessed at `http://localhost:3000` while both the frontend and backend are running.
 
The Swagger UI can be accessed at `http://localhost:8080/swagger` when the backend is running.
 
To run tests manually, use the dedicated Compose test services:
 
```bash
docker compose run --rm backend-test
docker compose run --rm frontend-test
```
 
These commands build the test containers and execute the backend or frontend test suites on demand.
 
## Local Development
 
### Backend
 
The backend can be started on its own from the project root using:
 
```bash
./mvnw spring-boot:run -pl backend
```
 
Backend tests can be run with:
 
```bash
./mvnw test -pl backend
```
 
> [!NOTE]
> Tests can be skipped during a build by running `./mvnw clean install -DskipTests -pl backend` instead.
 
### Frontend
 
> [!WARNING]
> `npm install` must be run from the `frontend` directory at least once before starting the frontend.
 
The frontend dev server can be started by running the following from the `frontend` directory:
 
```bash
npm run dev
```
 
Frontend tests can be run in CI mode or with the interactive Vitest UI:
 
```bash
npm run test       # CI mode
npm run test:ui    # Vitest UI
```
 
> [!NOTE]
> The frontend can be built for production by running `npm run build` from the `frontend` directory.
 
### App Access Points
 
The app can be accessed at `http://localhost:5173` while both the frontend and backend are running.
 
The Swagger UI can be accessed at `http://localhost:8080/swagger` when the backend is running.
 
## Bruno
 
The Bruno collection is located at the project root under `bruno/smarthomeapp-api-tets`.
 
## Walkthroughs
 
UI walkthrough:
https://www.loom.com/share/05477690806845e4920da3f42022cb1f
 
Backend walkthrough:
https://www.loom.com/share/8348e3854663466ab6272aaa5291b103
 
