create table "group"
(
    id   uuid primary key not null,

    name text             not null
);

create table group_membership
(
    id       uuid primary key not null,

    user_id  uuid             not null,
    group_id uuid             not null,

    foreign key (user_id) references "user" (id),
    foreign key (group_id) references "group" (id)
);