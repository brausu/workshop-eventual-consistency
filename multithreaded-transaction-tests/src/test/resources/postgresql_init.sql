drop table if exists accounts;

drop table if exists customers;

create table customers (
  id varchar(255) primary key,
  firstname varchar(255),
  lastname varchar(255)
);

create table accounts (
    id varchar(255) primary key,
    account_number numeric unique,
    balance decimal(15,2),
    owner varchar(255) references customers(id)
);

insert into customers values ('1d73ba2d-9fec-4b0c-b4f1-a72c457c500c', 'Harvey', 'Specter');
insert into customers values ('748d70e8-0409-4cbb-a66e-cd5656c14f3f', 'Louis', 'Litt');
insert into customers values ('68af8a8c-a7c5-445e-8341-6b69be9237bc', 'Jessica', 'Pearson');

insert into accounts values ('67152732-8cc9-45d8-adc2-d5f835daac6d', 110, 15000000.98,
                             '1d73ba2d-9fec-4b0c-b4f1-a72c457c500c');
insert into accounts values ('6557aefa-18cc-4a53-a1b5-d98f3a23c2e9', 111, 8000000.95,
                             '748d70e8-0409-4cbb-a66e-cd5656c14f3f');
insert into accounts values ('9557f97d-3bdb-482b-8e23-7fd4069d2490', 112, 35000000.03,
                             '68af8a8c-a7c5-445e-8341-6b69be9237bc');
insert into accounts values ('895a55ee-97ff-4dc4-8a0b-4ba0ca63cffa', 113, 1300000.98,
                             '1d73ba2d-9fec-4b0c-b4f1-a72c457c500c');