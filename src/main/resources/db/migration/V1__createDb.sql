create sequence accounts_seq start with 1 increment by 50
create sequence carts_seq start with 1 increment by 50
create sequence files_seq start with 1 increment by 50
create sequence previews_seq start with 1 increment by 50
create sequence products_seq start with 1 increment by 50
create sequence refresh_token_seq start with 1 increment by 50
create table account_role (account_id bigint not null, role_id integer not null)
create table accounts (id bigint not null, created_date timestamp(6), last_modified timestamp(6), email varchar(255) not null, enabled boolean, password varchar(100), primary key (id))
create table cart_items (cart_id bigint not null, product_id bigint not null, version varchar(255) not null, primary key (cart_id, product_id, version))
create table carts (id bigint not null, account_id bigint not null, primary key (id))
create table categories (id serial not null, name varchar(255), primary key (id))
create table files (id bigint not null, diplay_order varchar(255), last_update timestamp(6), name varchar(255), size bigint, source varchar(255), type varchar(255), product_id bigint not null, version varchar(255) not null, primary key (id))
create table license (id serial not null, content varchar(255), name varchar(255), primary key (id))
create table previews (id bigint not null, source varchar(255), type varchar(255), product_id bigint not null, version varchar(255) not null, primary key (id))
create table product_details (product_id bigint not null, version varchar(255) not null, cover_image varchar(255), upload_date timestamp(6), description varchar(100), detail_description varchar(255), instruction varchar(255), last_update timestamp(6), name varchar(30), price integer, category_id integer, license_id integer, primary key (product_id, version))
create table product_details_tag (product_id bigint not null, version varchar(255) not null, tag_id integer not null)
create table products (id bigint not null, draft boolean, enabled boolean, seller_id bigint not null, primary key (id))
create table refresh_token (id bigint not null, expiry_date timestamp(6) with time zone not null, token varchar(255) not null, account_id bigint not null, primary key (id))
create table reports (product_id bigint not null, account_id bigint not null, created_date timestamp(6) not null, description varchar(255) not null, status varchar(255) not null, violation_type_id bigint, primary key (product_id, account_id))
create table role (id serial not null, name varchar(255) not null, primary key (id))
create table sellers (phone_number varchar(255), seller_enabled boolean, account_id bigint not null, primary key (account_id))
create table tags (id serial not null, name varchar(255), primary key (id))
create table transaction_details (price bigint, product_id bigint not null, transaction_id bigint not null, primary key (product_id, transaction_id))
create table transactions (id bigserial not null, purchased_date timestamp(6), cart_id bigint not null, primary key (id))
create table users (avatar varchar(255), email_verified boolean, first_name varchar(255), last_name varchar(255), created_date timestamp(6), last_modified timestamp(6), username varchar(30) not null, account_id bigint not null, cart_id bigint, primary key (account_id))
create table violation_types (id bigserial not null, name varchar(255) not null, primary key (id))
alter table if exists accounts add constraint UK_n7ihswpy07ci568w34q0oi8he unique (email)
alter table if exists refresh_token add constraint UK_r4k4edos30bx9neoq81mdvwph unique (token)
alter table if exists refresh_token add constraint UK_1gnoedr4u8s5p7ccdkftq38bh unique (account_id)
alter table if exists role add constraint UK_8sewwnpamngi6b1dwaa88askk unique (name)
alter table if exists users add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username)
alter table if exists violation_types add constraint UK_i3xy9gqew9wwu9p6wrsokajh2 unique (name)
alter table if exists account_role add constraint FKrs2s3m3039h0xt8d5yhwbuyam foreign key (role_id) references role
alter table if exists account_role add constraint FKne5hjaf6i1wun5wnxlxvtl616 foreign key (account_id) references accounts
alter table if exists cart_items add constraint FKpcttvuq4mxppo8sxggjtn5i2c foreign key (cart_id) references carts
alter table if exists cart_items add constraint FKmv6d5b8f4sruuqfwtxq8d4pcn foreign key (product_id, version) references product_details
alter table if exists carts add constraint FKmbl9cyge43qwntnq4h2rmvv45 foreign key (account_id) references users
alter table if exists files add constraint FKrq9nuhnr6dphartkqp3r43yjy foreign key (product_id, version) references product_details
alter table if exists previews add constraint FKbpsr69u0guwhqwn1bh3g8ppun foreign key (product_id, version) references product_details
alter table if exists product_details add constraint FK9v67j2u6qdqv0baxdovbadrmq foreign key (category_id) references categories
alter table if exists product_details add constraint FK5cqxu77mkyjobyhdub60i8alj foreign key (license_id) references license
alter table if exists product_details add constraint FKnfvvq3meg4ha3u1bju9k4is3r foreign key (product_id) references products
alter table if exists product_details_tag add constraint FKnxwgt56h1t5u4eiyv6j9fsylv foreign key (tag_id) references tags
alter table if exists product_details_tag add constraint FKhrqa2cdci934rexx6wtui6x3n foreign key (product_id, version) references product_details
alter table if exists products add constraint FKepbha8uixgrmnejm27n6e1kkd foreign key (seller_id) references sellers
alter table if exists refresh_token add constraint FKif9hnfx206niwb61bip7iij29 foreign key (account_id) references accounts
alter table if exists reports add constraint FK2wb7diu9f2bue57b2t32g9ac0 foreign key (product_id) references products
alter table if exists reports add constraint FKq5i41n512ghow5ge92q2k3iqa foreign key (account_id) references users
alter table if exists reports add constraint FKlggn2s05pj6w0kecav9atmfuc foreign key (violation_type_id) references violation_types
alter table if exists sellers add constraint FKmqo83vk54uyjc032gh7xolel6 foreign key (account_id) references users
alter table if exists transaction_details add constraint FKd37lutuhg56s6tq30oowjk8js foreign key (product_id) references products
alter table if exists transaction_details add constraint FKm5vjjt9jqvf7y69innpgqnipr foreign key (transaction_id) references transactions
alter table if exists transactions add constraint FKnkyl3pyaclxtxgcuo1fdy004d foreign key (cart_id) references carts
alter table if exists users add constraint FKdv26y3bb4vdmsr89c9ppnx85w foreign key (cart_id) references carts
alter table if exists users add constraint FKfm8rm8ks0kgj4fhlmmljkj17x foreign key (account_id) references accounts