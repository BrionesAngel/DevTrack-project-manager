\# DevTrack



A full-stack project and task management platform focused on team collaboration, Kanban workflows, task tracking, and team organization.



\## Features



\* JWT authentication and authorization

\* Project and team management

\* Kanban task board

\* Task assignment and status tracking

\* Notifications system

\* Activity dashboard

\* Team member management



\## Tech Stack



\### Frontend



\* Vue 3

\* Vue Router

\* Pinia

\* TanStack Query

\* TailwindCSS

\* Axios



\### Backend



\* Spring Boot

\* Spring Security

\* Spring Data JPA

\* JWT Authentication



\### Database



\* Docker
\* PostgreSQL



\## Screenshots



\### Dashboard



!\[Dashboard](./dashboard.png)



\### Kanban Board



!\[Kanban](./kanban.png)



\### Task Details



!\[Task Details](./task.png)



\### Teams



!\[Teams](./team.png)



\### Notifications



!\[Notifications](./notification.png)



\### Account Settings



!\[Account Settings](./account.png)



\## Getting Started



\### Clone the repository



```bash

git clone https://github.com/BrionesAngel/DevTrack-project-manager.git

cd DevTrack-project-manager

```



\### Start PostgreSQL with Docker



```bash

docker compose --profile db up --build -d

```

\### Backend



```bash

cd backend

./gradlew bootRun --daemon

```

\### Frontend



```bash

cd frontend

bun install

bun run dev

```





\## Environment Variables



Create a `.env` file in the root directory and configure the required environment variables.



