#!/usr/bin/env bash
set -e

# Start Spring Boot
cd /workspaces/app/back
mvn -q spring-boot:run &

# Start Angular
cd /workspaces/app/front
if [ ! -d node_modules ]; then
  npm install
fi
npm start -- --host 0.0.0.0 --port 4200
