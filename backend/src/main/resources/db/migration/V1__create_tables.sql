CREATE TABLE users (
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE refresh_tokens (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users (id),
    token_hash TEXT UNIQUE,
    is_revoked BOOLEAN NOT NULL DEFAULT FALSE,
    device_id  VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);

CREATE TABLE projects (
    id          BIGSERIAL PRIMARY KEY,
    title       TEXT NOT NULL,
    description TEXT,
    created_by  BIGINT REFERENCES users (id)
);

CREATE INDEX idx_projects_created_by ON projects (created_by);

CREATE TABLE teams (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    project_id BIGINT REFERENCES projects (id)
);

CREATE INDEX idx_teams_project_id ON teams (project_id);

CREATE TABLE tasks (
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    description      VARCHAR(255),
    priority         VARCHAR(255),
    status           VARCHAR(255),
    due_date         DATE,
    github_issue_url VARCHAR(255),
    assigned_user_id BIGINT REFERENCES users (id),
    assigned_team_id BIGINT REFERENCES teams (id),
    project_id       BIGINT REFERENCES projects (id)
);

CREATE INDEX idx_tasks_assigned_user_id ON tasks (assigned_user_id);
CREATE INDEX idx_tasks_assigned_team_id ON tasks (assigned_team_id);
CREATE INDEX idx_tasks_project_id ON tasks (project_id);

CREATE TABLE task_comments (
    id         BIGSERIAL PRIMARY KEY,
    task_id    BIGINT NOT NULL REFERENCES tasks (id),
    author_id  BIGINT NOT NULL REFERENCES users (id),
    action     VARCHAR(255),
    message    VARCHAR(255),
    created_at TIMESTAMP
);

CREATE INDEX idx_task_comments_task_id ON task_comments (task_id);
CREATE INDEX idx_task_comments_author_id ON task_comments (author_id);

CREATE TABLE notifications (
    id            BIGSERIAL PRIMARY KEY,
    recipient_id  BIGINT REFERENCES users (id),
    sender_id     BIGINT REFERENCES users (id),
    type          VARCHAR(255) NOT NULL,
    resource_type VARCHAR(255) NOT NULL,
    resource_id   BIGINT,
    status        VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    created_at    TIMESTAMP
);

CREATE INDEX idx_notifications_recipient_id ON notifications (recipient_id);
CREATE INDEX idx_notifications_sender_id ON notifications (sender_id);

CREATE TABLE team_members (
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users (id),
    team_id BIGINT REFERENCES teams (id),
    role    VARCHAR(255) NOT NULL,
    CONSTRAINT uq_team_members_user_team UNIQUE (user_id, team_id)
);

CREATE TABLE project_member (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users (id),
    project_id BIGINT REFERENCES projects (id),
    role       VARCHAR(255) NOT NULL,
    CONSTRAINT uq_project_member_user_project UNIQUE (user_id, project_id)
);