#!/bin/bash
set -e

# Check if the user already exists before creating it
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  DO \$\$
  BEGIN
      IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '$POSTGRES_USER') THEN
          CREATE USER "$POSTGRES_USER" WITH PASSWORD '$POSTGRES_PASSWORD';
      END IF;
  END
  \$\$;

  -- Set ownership of the default database
  ALTER DATABASE "$POSTGRES_DB" OWNER TO "$POSTGRES_USER";

  -- Grant all privileges on the database to the user
  GRANT ALL PRIVILEGES ON DATABASE "$POSTGRES_DB" TO "$POSTGRES_USER";

  -- Update the password for the default 'postgres' user
  ALTER USER postgres WITH PASSWORD '$POSTGRES_PASSWORD';
EOSQL
