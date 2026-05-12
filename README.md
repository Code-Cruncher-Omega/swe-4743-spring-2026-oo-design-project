# swe-4743-spring-2026-oo-design-project
Project for Professor Jeff Adkisson's Object-Oriented Design course.

Application allows users to create devices, change their states and attributes, delete them, view devices based on their qualities (device type, activity, location), changes home simulation settings, view action history, etc.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Running the App](#running-the-app)
    - [App Access Points](#app-access-points)
- [Local Development](#local-development)
    - [App Access Points](#app-access-points-1)
- [Bruno](#bruno)
- [Walkthroughs](#walkthroughs)

## Prerequisites
| Tool | Minimum version | Check |
|---|---|---|
| Java JDK | 21 | `java -version` |
| Maven (or use the included wrapper) | 3.9+ | `mvn -v` |
| Node.js | 20+ | `node -v` |
| npm | 10+ | `npm -v` |

## Running the App
To run the application, clone the repo and run from the project root:

`docker compose up`

This starts the app services only; tests are not executed automatically by `docker compose up`

### App Access Points
The app can be accessed at http://localhost:3000/ while the frontend and backend are running simultaneously

The Swagger UI can be accessed at http://localhost:8080/swagger when the backend is running

To run tests manually, use the dedicated compose test services:

`docker compose run --rm backend-test`

`docker compose run --rm frontend-test`

These commands build the test containers and execute the backend or frontend test suites on demand.

## Local Development
For local development, the backend by itself can be started from the project's root using `./mvnw spring-boot:run -pl backend`
> [!NOTE]
> Tests can be skipped by running `./mvnw clean install -DskipTests -pl backend` instead.
Running just backend tests can be done by executing `./mvnw test -pl backend`

> [!WARNING]
> `npm install` needs to have been executed from the root of `frontend` at least once to run the frontend.
> [!NOTE]
> Building the frontend for production can be done via `npm run build`.
The frontend can be started for local development by running `npm run dev` in the root of `frontend`
Executing frontend tests can either be done by running `npm run test` (CI mode) or `npm run test:ui` (Vitest UI)

### App Access Points
The app can be accessed at http://localhost:5173/ while the frontend and backend are running simultaneously

The Swagger UI can similarly be accessed at http://localhost:8080/swagger when the backend is running

## Bruno
The API for the app can be tested via Bruno. The Bruno collection is located at the root of the project in `bruno/smarthomeapp-api-tets`

## Walkthroughs
UI walkthrough:
https://www.loom.com/share/05477690806845e4920da3f42022cb1f

Backend walkthrough:
https://www.loom.com/share/8348e3854663466ab6272aaa5291b103
