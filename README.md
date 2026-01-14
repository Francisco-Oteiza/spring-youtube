# YouTube Demo (Create users and manage them)

This repo is a small full‑stack demo that runs inside a **VS Code Dev Container**:

- `front/` → Angular app (UI)
- `back/` → Spring Boot app (API)
- `db` → Postgres (Docker)

## Run in VS Code (Dev Containers)

1. Install VS Code extensions:

- **Dev Containers** (ms-vscode-remote.remote-containers)
- **Docker** (ms-azuretools.vscode-docker)

2. Open the repo in VS Code.

3. Reopen in container:

- `Ctrl+Shift+P` → **Dev Containers: Reopen in Container**

4. Start the apps (inside the container terminal):

Backend:

```bash
cd /workspaces/app/back
mvn spring-boot:run
```

Frontend:

```bash
cd /workspaces/app/front
npm install
npm start
```

## URLs

- Frontend: http://localhost:4200
- Backend: http://localhost:8081
- Postgres: localhost:5432 (user: `postgres`, pass: `postgres`, db: `youtube`)
