create table carts
(
    id           binary(16) default (uuid_to_bin(uuid())) not null
        primary key,
    date_created date       default (curdate())           not null
);

create table cart_items
(
    id         bigint auto_increment
        primary key,
    cart_id    binary(16)    not null,
    course_id bigint        not null,
    quantity   int default 1 not null,
    constraint cart_items_cart_course_unique
        unique (cart_id, course_id),
    constraint cart_items_carts_id_fk
        foreign key (cart_id) references carts (id)
            on delete cascade,
    constraint cart_items_courses_id_fk
        foreign key (course_id) references courses (id)
            on delete cascade
);

