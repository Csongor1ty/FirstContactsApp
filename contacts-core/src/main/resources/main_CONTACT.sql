create table CONTACT
(
    id          INTEGER not null
        constraint CONTACT_pk
            primary key autoincrement,
    name        text    not null,
    email       text    not null,
    address     text,
    dateOfBirth text    not null,
    company     text,
    position    text
);

create unique index CONTACT_email_uindex
    on CONTACT (email);

