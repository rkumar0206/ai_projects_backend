FROM postgres:16.3

# Copy custom config files into a dedicated config dir
COPY postgres.conf pg_hba.conf /etc/postgresql/

# Copy init script
COPY init.sh /docker-entrypoint-initdb.d/

# Set permissions (Postgres requires readable config)
RUN chmod 700 /etc/postgresql/ && \
    chmod 600 /etc/postgresql/* && \
    chown -R postgres:postgres /etc/postgresql/

# Use CMD only to override default config paths
CMD ["postgres", "-c", "config_file=/etc/postgresql/postgres.conf", "-c", "hba_file=/etc/postgresql/pg_hba.conf"]
