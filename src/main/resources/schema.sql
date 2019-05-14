CREATE TABLE IF NOT EXISTS accounts (id varchar(1023), name varchar(1023), sum double);
insert into accounts (id, name, sum) values (0, 'John', 1000);
insert into accounts (id, name, sum) values (1, 'Carl', 1000);
insert into accounts (id, name, sum) values (2, 'Alice', 1000);
insert into accounts (id, name, sum) values (3, 'Kate', 1000);
commit ;