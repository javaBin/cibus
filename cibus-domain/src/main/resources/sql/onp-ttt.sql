--drop table jb_member_people;
--drop table jb_memberships;
--drop table jb_member_companies;

create table jb_member_people (
  id integer not null,
  first_name varchar(255),
  last_name varchar(255),
  password varchar(255),
  phone_number varchar(255),
  address varchar(255),
  email varchar(255),
  member_company_id integer
);

create table jb_member_companies (
  id integer not null,
  name varchar(255),
  address varchar(255),
  contact_member_person_id integer
);

create table jb_memberships (
  id integer not null,
  valid_to date not null,
  member_person_id integer,
  member_company_id integer
);

insert into jb_member_companies values (1, 'Hopp', 'Her og der', 2);
insert into jb_member_people values (1, 'In', 'Dependent', 'pwd1', '55534893', 'Rundt-om-kring', 'hit@dit.no', null);
insert into jb_member_people values (2, 'D.', 'Pendent', 'pwd2', '55534298', 'Her', 'dpendent@hopp.no', 1);
insert into jb_member_people values (3, 'D.', 'Pend', 'pwd2', '55543893', 'Der', 'dpend@hopp.no', 1);
insert into jb_member_companies values (2, 'Helt ut', 'Hvor som helst', 5);
insert into jb_member_people values (4, 'Old', 'Timer', 'pwd4', '55534932', 'Helst', 'oldtimer@heltuten.no', null);
insert into jb_member_people values (5, 'Old', 'Timer2', 'pwd5', '55539483', 'Helst ikke', 'oldtimer2@heltut.no', 2);
insert into jb_memberships values (1, '2050-10-01', null, 1);
insert into jb_memberships values (2, '2050-10-01', 1, null);
insert into jb_memberships values (3, '1950-10-01', null, 2);
insert into jb_memberships values (4, '1950-10-01', 4, null);
