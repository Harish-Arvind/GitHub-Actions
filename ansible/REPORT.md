# Ansible Project Report

## Question 3-2: Document your playbook
My playbook (`deploy_app.yml`) defines the master orchestration for deploying our Dockerized application to the remote server. It executes with elevated privileges (`become: true`) across all specified hosts to ensure proper permissions. I structured the deployment securely using modular roles:
1. **docker**: Installs Docker dependencies, configures the Docker APT repository, installs Docker CE, and sets up a Python virtual environment to manage the `docker` SDK for Ansible.
2. **network**: Creates the isolated Docker networks (`front-network` and `back-network`) required to securely bridge the HTTPD reverse proxy to the backend API, and the backend API to the PostgreSQL database.
3. **database**: Pulls the Database image and spawns a PostgreSQL container connected only to the `back-network` for security. Crucial environment variables (`db_name`, `db_user`) are passed through inherited variables.
4. **app**: Spawns the Spring Boot backend container. It connects to both `front-network` and `back-network` and injects the database connection credentials as environment variables.
5. **proxy**: Starts the Apache HTTPD web server container mapped to port 80 on the host, exposing the site externally while acting as an internal reverse proxy on the `front-network`.

## Question 3-3: Document your docker_container tasks configuration
The tasks configuration uses the Ansible `docker_container` module, which maps declaratively to arguments you would find in `docker run` or `docker-compose`. 
Each container is given a distinct `name` and relies on pulling an `image` from Docker Hub (`pull: true` guarantees the latest image version is deployed). The `state: started` guarantees the image is running, whilst `restart_policy: unless-stopped` makes the infrastructure resilient against server reboots. Variables are passed dynamically via the `env:` block. For the database, an explicit `healthcheck` block runs `pg_isready` to ensure the Postgres TCP listener has booted fully before other tasks potentially require it. The `networks:` property maps each container to the correct isolated broadcast domains.

## Continuous Deployment Additions

**Question: Is it really safe to deploy automatically every new image on the hub? Explain. What can I do to make it more secure?**

Automatically deploying every new image mapped to the `latest` tag is generally considered unsafe for production environments because it lacks control and validation:
1. **Stability Risks**: Code that compiles and builds successfully could still contain critical runtime bugs or misconfigurations that crash the application in production. 
2. **State Breakages**: A forced push deployment might abruptly cut off users performing operations on the live site.

**To make our pipeline more secure and resilient, we can implement several protections:**
1. **Semantic Versioning:** Instead of using the `latest` tag, publish specific semantic versions (e.g., `v1.2.3`). Deployment pipelines deploy explicit versions so rollbacks are reproducible.
2. **Environment Promotion Setup:** Introduce a Staging environment where automatic deployments happen first. Only deploy to Production after manual approval or automated end-to-end integration tests conclude successfully on Staging.
3. **Pre-deployment Security Scans:** Use tools automatically in the CI pipeline to scan the final Docker images for CVEs (e.g., Trivy plugin) before pushing to Docker Hub. If a severe vulnerability is found, block the deployment.
4. **Blue/Green or Canary Deployments:** Instead of immediately overwriting the running container, spin up the new version alongside the old one. Route a small subset of traffic to verify its health before shifting entirely.
