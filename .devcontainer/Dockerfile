FROM mcr.microsoft.com/devcontainers/java:21

RUN apt-get update \
  && apt-get install -y --no-install-recommends curl unzip ca-certificates \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*
