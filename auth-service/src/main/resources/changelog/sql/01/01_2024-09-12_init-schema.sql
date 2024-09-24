create table if not exists users
(
    id       bigserial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null
);

create table if not exists roles
(
    id   bigserial primary key,
    name varchar(255) not null unique
);

create table if not exists user_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint fk_users_roles_users foreign key (user_id)
        references users (id),
    constraint fk_users_roles_roles foreign key (role_id)
        references roles (id)
);

create table if not exists user_activity
(
    eventdate timestamp default null,
    level     varchar(100),
    message   varchar(100)
);