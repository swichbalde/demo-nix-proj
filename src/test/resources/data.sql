create table IF NOT EXISTS users (
                                     id serial primary key ,
                                     login varchar(255),
                                     password varchar(255),
                                     created timestamp
);

create table IF NOT EXISTS role (
                                    id serial primary key ,
                                    name varchar(255)
);

create table IF NOT EXISTS user_roles(
                                         user_id int,
                                         role_id int,
                                         foreign key (user_id) references users(id),
                                         foreign key (role_id) references role(id)
);

insert into users(login, password) values ('username', 'testtest' );
insert into role(name) values ('ROLE_USER' );
insert into role(name) values ('ROLE_ADMIN' );

insert into user_roles(user_id, role_id) values(1,1);
insert into user_roles(user_id, role_id) values(1,2);