CREATE TABLE mentors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    completed_curriculum_id VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);