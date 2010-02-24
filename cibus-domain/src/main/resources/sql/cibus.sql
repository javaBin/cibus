create sequence member_seq;

create table member(
    membership_no         integer not null default nextval('member_seq'),
    first_name            varchar(100) not null,
    last_name             varchar(100) not null,
    created               timestamp not null,
    last_updated          timestamp,

    password              varchar(32),
    reset_password_uuid   varchar(32),

    constraint member_pk  primary key (membership_no)
);
alter sequence member_seq owned by member.membership_no;

create table email_address(
    membership_no         integer,
    value                 varchar(100) not null,
--    verified_date         timestamp,
--    verification_time     timestamp,
--    verification_code     varchar(36),
    constraint email_address_pk primary key (value),
    constraint membership_member foreign key (membership_no) references member(membership_no)
);

create table company(
    id                    integer,
    name                  varchar(100) not null,
    constraint company_pk primary key (id) 
);

create table membership(
    membership_no         integer not null,
    sponsored_by          integer not null,
    constraint membership_pk primary key (membership_no, sponsored_by),
    constraint membership_member foreign key (membership_no) references member(membership_no),
    constraint membership_company foreign key (sponsored_by) references company(id)
);
