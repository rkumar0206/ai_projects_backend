## How to use it in local using docker compose file

#### Step 1. Copy below docker compose file

```docker-compose.yaml
services:
  ai-backend:
    image: rkumar0206/ai-backend:v1.4.0
    container_name: ai-backend
    env_file:
      - .env
    environment:
      DB_URL: 'jdbc:postgresql://ai-db:5432/ai_projects_db'
      GEMINI_API_KEY: ${GEMINI_API_KEY}
      MODEL: ${MODEL}
      ALLOWED_ORIGINS: ${ALLOWED_ORIGINS}
    ports:
      - "7070:7070"
    depends_on:
      - ai-db
    networks:
      - ai-network

  ai-db:
    image: rkumar0206/ai-db:v1.0.0
    container_name: ai-db
    env_file:
      - .env
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB}
    ports:
      - "5433:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
    networks:
      - ai-network

  ai-frontend:
    image: rkumar0206/ai-frontend:v1.5.0
    container_name: ai-frontend
    ports:
      - "7071:80"
    restart: unless-stopped
    depends_on:
      - ai-backend
    networks:
      - ai-network

networks:
  ai-network:
    driver: bridge
    name: ai-network

volumes:
  pgdata:
    driver: local
```

#### Step 2. Copy below .env file (Provide your GEMINI_API_KEY)

```.env
GEMINI_API_KEY=
MODEL=gemini-2.5-flash-lite
POSTGRES_USER=rtb-ai-project
POSTGRES_PASSWORD=rkumar0206
POSTGRES_DB=ai_projects_db
ALLOWED_ORIGINS=http://localhost:7071
```

#### Step 3. Create two files in same directory named docker-compose.yml and .env file
#### Step 4. Run the command
  ```
    docker-compose -f docker-compose.yml up -d
  ```
#### Step 5. In browser visit http://localhost:7071
