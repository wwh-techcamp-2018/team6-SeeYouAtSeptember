create table admin_user (
    id bigint not null auto_increment,
    deleted bit not null,
    email varchar(255),
    name varchar(255),
    password varchar(255),
    primary key (id)
) engine=MyISAM;

create table category (
    id bigint not null auto_increment,
    category_image_url varchar(255),
    title varchar(255),
    primary key (id)
) engine=MyISAM;

create table normal_user (
    id bigint not null auto_increment,
    deleted bit not null,
    email varchar(255),
    name varchar(255),
    password varchar(255),
    address varchar(255),
    phone_no varchar(255),
    primary key (id)
) engine=MyISAM;

create table order_history (
    id bigint not null auto_increment,
    created_at datetime,
    merchant_uid varchar(255),
    quantity bigint,
    status integer,
    normal_user_id bigint,
    product_id bigint,
    primary key (id)
) engine=MyISAM;

create table product (
    id bigint not null auto_increment,
    deleted bit not null,
    description longtext,
    price bigint,
    quantity_remained bigint,
    quantity_supplied bigint,
    title varchar(255),
    project_id bigint,
    primary key (id)
) engine=MyISAM;

create table project (
    id bigint not null auto_increment,
    created_at datetime,
    current_fund_raising bigint default 0,
    deleted bit not null,
    description longtext,
    end_at datetime not null,
    goal_fund_raising bigint not null,
    status integer,
    thumbnail_url varchar(255) not null,
    title varchar(255) not null,
    category_id bigint,
    owner_id bigint,
    primary key (id)
) engine=MyISAM;

alter table admin_user 
    add constraint UK_6etwowal6qxvr7xuvqcqmnnk7 unique (email);

alter table normal_user 
    add constraint UK_golrqvhmjl6xj1vk4yyd7d1hs unique (email);

alter table order_history 
    add constraint FKbs9p35la7f9nxucfqbgsddmkc 
    foreign key (normal_user_id) 
    references normal_user (id);

alter table order_history 
    add constraint FKdllv28el8187yxam1mo9pwhct 
    foreign key (product_id) 
    references product (id);

alter table product 
    add constraint FKncxhj0xapt1bdqasvd92mi1ee 
    foreign key (project_id) 
    references project (id);

alter table project 
    add constraint FKe0w7gh0rpmxo35nltk6g8517q 
    foreign key (category_id) 
    references category (id);

alter table project 
    add constraint FK3e8h43pakdf1np7ct4s1fewnd 
    foreign key (owner_id) 
    references normal_user (id);