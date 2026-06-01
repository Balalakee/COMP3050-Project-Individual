# COMP3050 Project

This project is a Java 17 HTTP server for a browser-based tile map game client. It implements the COMP3050 API v3 game server features: login sessions, multiplayer player state, map info, movement, item take/place, and simple map element use.

## Project Structure

* `src/main/java/` - Java server source files
* `src/test/java/` - JUnit tests
* `src/main/resources/` - Resource files including `map.txt`
* `public/` - Browser client and static assets
* `target/` - Maven build output, ignored by Git
* `docker-compose.yml` - Local Docker Compose runner for the Java server
* `.github/workflows/` - CI/CD workflows
* `prometheus/` - Prometheus configuration
* `promtail/` - Promtail configuration
* `infra/` - Terraform infrastructure files

## Requirements

* Java 17
* Maven
* Docker and Docker Compose, recommended for local container testing

## Environment Variables

Player accounts are loaded from the `GAME_USERS` environment variable.

Create a `.env` file in the repository root:

```env
GAME_USERS=username:encryptedPassword:avatar:y:x;username2:encryptedPassword:avatar:y:x
```

Format:

```text
username:encryptedPassword:avatar:y:x
```

Where:

* `username` = player username
* `encryptedPassword` = SHA-256 hash of `username;password`
* `avatar` = player avatar digit (`0-9`)
* `y` = spawn y coordinate
* `x` = spawn x coordinate

Example:

```env
GAME_USERS=admin:<hash>:1:5:5;willow:<hash>:2:5:6
```

## Login Accounts

Player accounts are configured locally through the `.env` file and are not stored in the repository.

Each teammate should create their own `.env` file locally.

The `.env` file should never be committed to GitHub.

## Run Locally

From the repository root:

```bash
mvn clean package
java -jar target/comp3050-project-1.0-SNAPSHOT.jar
```

Then open `public/index.html` in a browser and use this server address:

```text
http://localhost:8000
```

## Run With Docker Compose

Start the local stack:

```bash
docker compose up --build
```

Watch server logs:

```bash
docker compose logs -f server
```

Stop the stack:

```bash
docker compose down
```

The Compose file runs the Java server on port `8000` and includes observability services such as Prometheus, Grafana, Loki, and Promtail.

## Run With Docker Directly

Build the image:

```bash
docker build -t comp3050-project .
```

Run the server:

```bash
docker run --rm -p 8000:8000 --env-file .env comp3050-project
```

## Run Tests

With Maven installed:

```bash
mvn test
```

Or run Maven through Docker:

```bash
docker run --rm --env-file .env -v "${PWD}:/app" -w /app maven:3.9.9-eclipse-temurin-17 mvn test
```

## API Endpoints

* `POST /login` - authenticates a player and returns a session token
* `GET /logout?session=<session>` - revokes the session token
* `GET /info?y=<y>&x=<x>&session=<session>` - returns the current 11x11 visible map window
* `GET /move?dy=<dy>&dx=<dx>&session=<session>` - moves one tile north, south, east, or west
* `GET /take?session=<session>` - takes a movable item from the current location
* `GET /place?session=<session>` - places the most recently held inventory item
* `GET /use?dy=<dy>&dx=<dx>&session=<session>` - uses the current or adjacent map element, such as opening or closing a door

All non-login endpoints require a valid session token. Invalid or missing sessions return `401`.

## API v3 Notes

* The server maintains player positions, avatars, inventories, and active sessions.
* The map supports multi-character tile strings such as `gk`, `wD`, and `g1`.
* Player avatars are added dynamically to `/info` responses and are not written into the base map.
* The x coordinate wraps horizontally at map edges; the y coordinate does not wrap vertically.
* Blocking movement includes walls, water, closed doors, and other online players.
* Items `a`, `c`, `h`, and `k` can be taken. Inventory is mutually exclusive by item class.
* `/use` currently toggles doors between `D` and `d`.

## Debug Logs

The Java server prints API v3 debug logs to the server console:

* `/info` prints player identity, position, view bounds, inventory summary, and the current 11x11 visible map data.
* `/move` prints requested movement, previous and new coordinates, and whether the result was `success`, `blocked`, or `invalid`.
* `/take` and `/place` print the operation result and updated inventory summary.

When running with Docker Compose, view logs with:

```bash
docker compose logs -f server
```

## CI/CD

GitHub Actions automatically:

* Runs Maven tests
* Runs Semgrep static analysis
* Builds the Docker image
* Scans the image with Trivy

The CI workflow requires the `GAME_USERS` GitHub Actions secret to be configured.

## Terraform

Terraform files are located in:

```text
infra/
```

The Terraform provider lock file:

```text
infra/.terraform.lock.hcl
```

should remain committed to Git.

The following should not be committed:

```text
infra/.terraform/
infra/terraform.tfstate
infra/terraform.tfstate.backup
*.tfvars
*.tfvars.json
.env
```
