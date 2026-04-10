FROM postgres:17.2-alpine

# Copy initialization scripts
# The scripts in this directory are executed on the first start of the container
COPY ./sql-scripts/ /docker-entrypoint-initdb.d/
