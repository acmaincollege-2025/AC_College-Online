# Enrollment System - Deployment Package (Ant + Docker)

## Contents
- build.xml - Ant build file that compiles sources and packages dist/enrollment.war
- Dockerfile - Tomcat 9 image that runs the WAR
- docker-entrypoint.sh - writes context.xml from environment variables then starts Tomcat
- META-INF/context-template.xml - template for JNDI datasource
- web/WEB-INF/web.xml - production-ready web.xml for JSF
- docker-compose.yml - local dev (MySQL + Tomcat)
- .env.example - example environment variables
- render.yaml / railway.toml - optional manifests for Render / Railway

## Local development with Docker Compose
1. Copy `.env.example` to `.env` and adjust values (optional).
2. Build and run:
    docker-compose up --build
3. Visit: http://localhost:8080

## Build WAR with Ant (without Docker)
1. Ensure you have Ant and JDK installed.
2. Place your Java sources in `src/`, web app in `web/`, and dependencies (jar) in `lib/`.
3. Run:
    ant war
4. The WAR will be at `dist/enrollment.war` which you can deploy to Tomcat.

## Deploy to Render
1. Push repo to GitHub.
2. Create new Web Service in Render, select Docker, connect repo & branch.
3. Set environment variables in Render (DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASS).
4. Render will build and deploy automatically.

## Notes
- File uploads inside the container are ephemeral. Use external storage for persistence.
- Always keep DB credentials secret (do not commit .env with real values).
