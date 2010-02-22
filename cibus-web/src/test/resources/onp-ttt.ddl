drop table jb_memberships;
drop table jb_member_people;
drop table jb_member_companies;

create table jb_member_people (
  id integer not null,
  onp_id integer,
  member_company_id integer,
  name varchar(255),
  hidden boolean
);

create table jb_member_companies (
  id integer not null,
  name varchar(255),
  contact_member_person_id integer
);

create table jb_memberships (
  id integer not null,
  valid_to date not null,
  member_person_id integer,
  member_company_id integer
);

insert into jb_member_companies values (1, 'Hopp', 2);
insert into jb_member_people values (1, null, null, 'In Dependent', false);
insert into jb_member_people values (2, null, 1, 'D. Pendent', false);
insert into jb_member_people values (3, null, 1, 'D. Pend', false);
insert into jb_member_companies values (2, 'Helt ut', 5);
insert into jb_member_people values (4, null, null, 'Old Timer', false);
insert into jb_member_people values (5, null, 2, 'Old Timer2', false);
insert into jb_memberships values (1, '2050-10-01', null, 1);
insert into jb_memberships values (2, '2050-10-01', 1, null);
insert into jb_memberships values (3, '1950-10-01', null, 2);
insert into jb_memberships values (4, '1950-10-01', 4, null);
