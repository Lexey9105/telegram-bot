-- liquibase formatted sql

-- changeset lexey:1

CREATE TABLE notification_task (
                              id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                              chat_id BIGINT  NOT NULL,
                              notification TEXT NOT NULL,
                              duration TIMESTAMP NOT NULL
)