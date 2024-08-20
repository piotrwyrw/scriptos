create table "user"
(
    id            uuid primary key not null,

    created_at    timestamptz      not null,

    username      text             not null,
    password_hash text             not null
);