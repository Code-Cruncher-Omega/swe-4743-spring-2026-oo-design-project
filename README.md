# swe-4743-spring-2026-oo-design-project
Project for Professor Jeff Adkisson's Object-Oriented Design course.

Application allows users to create devices, change their states and attributes, delete them, view devices based on their qualities (device type, activity, location), changes home simulation settings, view action history, etc.

To run the application, clone the repo and run from the project root:

`docker compose up`

This starts the app services only; tests are not executed automatically by `docker compose up`.

To run tests manually, use the dedicated compose test services:

`docker compose run --rm backend-test`

`docker compose run --rm frontend-test`

These commands build the test containers and execute the backend or frontend test suites on demand.

The API for the app can be tested via Bruno. The Bruno collection is located at the root of the project in `bruno/smarthomeapp-api-tets`.


UI walk through:
https://www.loom.com/share/05477690806845e4920da3f42022cb1f

Backend walk through:
https://www.loom.com/share/8348e3854663466ab6272aaa5291b103
