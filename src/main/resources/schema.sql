drop table if exists GENRE, RATING_MPA, FILM_DIRECTORS, DIRECTORS, FILM, FILM_GENRES, USERS, FILM_LIKES, USER_FRIENDS;

create table IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER auto_increment,
    NAME     CHARACTER VARYING not null
);

create unique index IF NOT EXISTS GENRE_GENRE_ID_UINDEX
    on GENRE (GENRE_ID);

alter table GENRE
    add constraint IF NOT EXISTS GENRE_PK
        primary key (GENRE_ID);

create table IF NOT EXISTS RATING_MPA
(
    RATING_ID INTEGER auto_increment,
    NAME      CHARACTER VARYING,
    constraint RATING_MPA_PK
        primary key (RATING_ID)
);

create table IF NOT EXISTS DIRECTORS
(
    DIRECTOR_ID INTEGER auto_increment,
    NAME      CHARACTER VARYING,
    constraint DIRECTOR_PK
        primary key (DIRECTOR_ID)
);

create table IF NOT EXISTS FILM
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING(50) not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE,
    DURATION     BIGINT,
    RATING_ID    INTEGER               not null,
    constraint FILM_RATING_MPA_RATING_ID_FK
        foreign key (RATING_ID) references RATING_MPA
            on update set null on delete set null
);

create unique index IF NOT EXISTS FILM_FILM_ID_UINDEX
    on FILM (FILM_ID);

alter table FILM
    add constraint IF NOT EXISTS FILM_PK
        primary key (FILM_ID);

create table IF NOT EXISTS FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRES_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILM_GENRES_FILM_FILM_ID_FK
        foreign key (FILM_ID) references FILM,
    constraint FILM_GENRES_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);

create table IF NOT EXISTS FILM_DIRECTORS
(
    FILM_ID  INTEGER not null,
    DIRECTOR_ID INTEGER not null,
    constraint FILM_DIRECTORS_PK
        primary key (FILM_ID, DIRECTOR_ID),
    constraint FILM_DIRECTORS_FILM_ID_FK
        foreign key (FILM_ID) references FILM,
    constraint FILM_DIRECTORS_DIRECTOR_ID_FK
        foreign key (DIRECTOR_ID) references DIRECTORS
         on update set null on delete set null
);

create table IF NOT EXISTS USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING not null,
    LOGIN    CHARACTER VARYING not null,
    NAME     CHARACTER VARYING,
    BIRTHDAY DATE              not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create unique index IF NOT EXISTS USERS_USER_ID_UINDEX
    on USERS (USER_ID);


create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILM_LIKES_PK
        primary key (FILM_ID, USER_ID),
    constraint FILM_LIKES_FILM_FILM_ID_FK
        foreign key (FILM_ID) references FILM,
    constraint FILM_LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);


create table IF NOT EXISTS USER_FRIENDS
(
    INITIATOR_ID INTEGER              not null,
    ACCEPTOR_ID  INTEGER              not null,
    STATUS       BOOLEAN default NULL not null,
    constraint USER_FRIENDS_PK
        primary key (INITIATOR_ID, ACCEPTOR_ID),
    constraint USER_FRIENDS_USERS_USER_ID_FK
        foreign key (INITIATOR_ID) references USERS,
    constraint USER_FRIENDS_USERS_USER_ID_FK_2
        foreign key (ACCEPTOR_ID) references USERS
);



