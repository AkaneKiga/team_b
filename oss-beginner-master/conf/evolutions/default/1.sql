# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app_user (
  id                        bigserial not null,
  username                  varchar(100) not null,
  password                  varchar(256) not null,
  salt                      CHAR(10) not null,
  constraint uq_app_user_username unique (username),
  constraint pk_app_user primary key (id))
;

create table shop (
  id                        bigserial not null,
  name                      varchar(255) not null,
  url                       varchar(255) not null,
  shopid                    varchar(255) not null,
  constraint pk_shop primary key (id))
;




# --- !Downs

drop table if exists app_user cascade;

drop table if exists shop cascade;

