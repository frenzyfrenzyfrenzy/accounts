CREATE TABLE IF NOT EXISTS accounts (ID int primary key auto_increment , name varchar(1023), sum double);
insert into accounts (name, sum) values ('John', 1000);
insert into accounts (name, sum) values ('Carl', 1000);
insert into accounts (name, sum) values ('Alice', 1000);
insert into accounts (name, sum) values ('Kate', 1000);
insert into accounts (name, sum) values ('Frank', 1000);
commit ;