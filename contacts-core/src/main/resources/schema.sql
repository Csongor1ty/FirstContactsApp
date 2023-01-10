create table CONTACT (
  id INTEGER not null primary key autoincrement,
  user_id text not null,
  name text not null,
  email text not null,
  address text,
  dateOfBirth text not null,
  company text,
  position text,
  FOREIGN KEY(user_id) REFERENCES USER(id)

);

create table USER (
  id INTEGER not null primary key autoincrement,
  username text not null,
  email text not null,
  profilePic text,
  description text,
  password text not null
);
