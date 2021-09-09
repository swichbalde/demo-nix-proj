create table users (
    id serial primary key,
    created timestamp,
    login varchar(255),
    password varchar(255),
    status varchar(255)
);

create table recipe_entity (
    id serial primary key,
    calories int,
    cost int,
    cook_time int,
    ingredients varchar(255),
    recipe_name varchar(255)
);

create table user_list_entity (
    id serial primary key,
    ban_list varchar(255),
    filter varchar(255),
    recommend_list varchar(255),
    user_id bigint references users(id)
);

create table save_weight_entity (
    id serial primary key,
    bmi real,
    current_weight bigint,
    difference bigint,
    first_calc timestamp,
    new_weight bigint,
    user_id bigint references users(id)
);

create table role (
    id serial primary key,
    name varchar(255),
    status varchar(255)
);

create table user_roles (
    id serial primary key,
    user_id bigint references users(id),
    role_id bigint references role(id)
);