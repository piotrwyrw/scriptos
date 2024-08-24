create table document
(
    id          uuid primary key not null,

    title       text             not null,
    description text             not null,
    created_at  timestamptz      not null,

    group_id    uuid             not null,
    author_id   uuid             not null,

    foreign key (group_id) references "group" (id),
    foreign key (author_id) references "user" (id)
);