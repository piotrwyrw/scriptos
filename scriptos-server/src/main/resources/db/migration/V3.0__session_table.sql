create table session
(
    id            uuid primary key not null,
    token         text             not null,
    user_id       uuid             not null,
    created_at    timestamptz      not null,
    last_accessed timestamptz      not null,

    foreign key (user_id) references "user" (id)
);