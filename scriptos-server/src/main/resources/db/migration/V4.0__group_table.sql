create table "group"
(
    id   uuid primary key not null,

    name text             not null
);

create table group_membership
(
    user_id  uuid not null,
    group_id uuid not null,

    primary key (user_id, group_id),

    foreign key (user_id) references "user" (id),
    foreign key (group_id) references "group" (id)
);