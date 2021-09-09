create table IF NOT EXISTS users (
                                     id int auto_increment primary key ,
                                     login varchar(255),
                                     password varchar(255),
                                     created timestamp ,
                                     foreign key (id) references user_roles(user_id)
);

create table IF NOT EXISTS role (
                                    id int primary key ,
                                    name varchar(255),
                                    foreign key (id) references user_roles(role_id)
);

create table IF NOT EXISTS user_roles(
                                         user_id int,
                                         role_id int
);

insert into users(id, login, password) values ( 1, 'username', 'testtest' );
insert into role(id, name) values ( 1, 'ROLE_USER' );
insert into role(id, name) values ( 2, 'ROLE_ADMIN' );

insert into user_roles(user_id, role_id) values(1,1);
insert into user_roles(user_id, role_id) values(1,2);