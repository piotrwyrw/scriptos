create table monitor
(
    id          uuid primary key not null,
    content     text             not null,
    created_at  timestamptz      not null,
    done        bool             not null default false,
    document_id uuid             not null
);

create table document
(
    id             uuid primary key not null,

    title          text             not null,
    description    text             not null,
    created_at     timestamptz      not null,

    group_id       uuid             not null,
    author_id      uuid             not null,

    file_type      text             not null,

    byte_size      bigint           not null default 0,

    status_monitor uuid,

    foreign key (group_id) references "group" (id),
    foreign key (author_id) references "user" (id),
    foreign key (status_monitor) references monitor (id)
);

alter table monitor
    add foreign key (document_id) references document (id);
