## Screenshots
<img width="1703" height="904" alt="Screenshot 2025-12-15 at 10 06 52 AM" src="https://github.com/user-attachments/assets/cd22c10e-0408-410a-a44b-66a582cc62b3" />
<img width="1664" height="851" alt="Screenshot 2025-12-15 at 10 07 02 AM" src="https://github.com/user-attachments/assets/d96a1762-3bf9-4d77-9d19-f0603392476e" />
<img width="1707" height="944" alt="Screenshot 2025-12-15 at 10 07 18 AM" src="https://github.com/user-attachments/assets/4f6d44af-490d-46eb-a19a-9528363cac32" />
<img width="1704" height="948" alt="Screenshot 2025-12-15 at 10 07 28 AM" src="https://github.com/user-attachments/assets/72c31170-7439-4bbe-a715-4b15ac0d77fc" />
<img width="1707" height="945" alt="Screenshot 2025-12-15 at 10 07 36 AM" src="https://github.com/user-attachments/assets/a3d3ef66-4add-4b56-9db6-4a3857ab20b0" />


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
