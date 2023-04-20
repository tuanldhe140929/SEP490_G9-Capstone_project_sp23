
 DO $$ BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_type WHERE typname = 'Status'
  ) THEN
    CREATE TYPE Status AS ENUM('NEW', 'PENDING', 'APPROVED','REJECTED');
  END IF;
END $$;

 DO $$ BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_type WHERE typname = 'Transaction_Status'
  ) THEN								
    CREATE TYPE TransactionStatus AS ENUM('CREATED', 'COMPLETED', 'CANCELED', 'FAILED','APPROVED','EXPIRED');
  END IF;
END $$;

 DO $$ BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_type WHERE typname = 'Payout_Status'
  ) THEN
    CREATE TYPE PayoutStatus AS ENUM('SUCCESS', 'FAILED', 'CANCELED', 'CREATED', 'PROCESSING');
  END IF;
END $$;

  create sequence accounts_seq start with 1 increment by 50;
 create sequence carts_seq start with 1 increment by 50;
 create sequence files_seq start with 1 increment by 50;
 create sequence payouts_seq start with 1 increment by 50;
 create sequence previews_seq start with 1 increment by 50;
 create sequence products_seq start with 1 increment by 50;
 create sequence refresh_token_seq start with 1 increment by 50;
 create sequence transactions_seq start with 1 increment by 50;
 create sequence violations_seq start with 1 increment by 50;
 create table account_role (account_id bigint not null, role_id integer not null);
 create table accounts (id bigint not null, created_date timestamp(6) not null, email varchar(320) not null, enabled boolean not null, last_modified timestamp(6), password varchar(100) not null, primary key (id));
 create table cart_items (cart_id bigint not null, product_id bigint not null, version varchar(30) not null,price real not null, changed boolean not null, primary key (cart_id, product_id, version));
 create table carts (id bigint not null, account_id bigint not null, primary key (id));
 create table categories (id serial not null, name varchar(255) unique not null, primary key (id));
 create table files (id bigint not null, created_date timestamp(6) not null, enabled boolean not null, last_modified timestamp(6) not null, name varchar(255) not null, new_uploaded boolean not null, reviewed boolean not null, size bigint not null, source varchar(255), type varchar(255) not null, product_id bigint not null, version varchar(30) not null, primary key (id));
 create table licenses (id serial not null, acrynosm varchar(255), details varchar(1024) not null, name varchar(255) not null, provider varchar(255), reference_link varchar(255), primary key (id));
 create table payouts (id bigint not null, amount real check (amount >= 0) not null, batch_id varchar(255) not null, created_date timestamp(6) not null, description varchar(255), last_modified timestamp(6) not null, payout_fee float(53) not null, payout_status PayoutStatus not null, account_id bigint, transaction_id bigint, primary key (id));
 create table previews (id bigint not null, source varchar(255) not null, type varchar(255) not null, product_id bigint not null, version varchar(30) not null, primary key (id));
 create table product_versions (product_id bigint not null, version varchar(30) not null, status Status not null, cover_image varchar(255), upload_date timestamp(6) not null, description varchar(100), detail_description varchar(1000), flagged boolean not null, instruction varchar(255), last_update timestamp(6) not null, name varchar(30), price real not null check (price >= 0 and price<=1000), category_id integer, license_id integer, primary key (product_id, version));
 create table product_version_tag (product_id bigint not null, version varchar(30) not null, tag_id integer not null);
 create table products (id bigint not null, active_version varchar(30) not null, draft boolean not null, enabled boolean, seller_id bigint not null, primary key (id));
 create table refresh_token (id bigint not null, expiry_date timestamp(6) with time zone not null, token varchar(255) not null, account_id bigint not null, primary key (id));
 create table reports (product_id bigint not null, account_id bigint not null, version varchar(255) not null, created_date timestamp(6) not null, description varchar(255) not null, status varchar(255) not null, violation_type_id bigint not null, primary key (product_id, account_id, version));
 create table roles (id serial not null, name varchar(255) not null, primary key (id));
 create table sellers (paypal_email varchar(255) not null, seller_enabled boolean not null, account_id bigint not null, primary key (account_id));
 create table tags (id serial not null, name varchar(255) not null unique, primary key (id));
 create table transaction_fees (id serial not null, percentage integer check (percentage >= 0 and percentage <= 100), primary key (id));
 create table transactions (id bigint not null, amount real check (amount >= 0) not null, created_date timestamp(6) not null, expired_date timestamp(6), description varchar(255), last_modified timestamp(6) not null, paypal_id varchar(255) not null, status TransactionStatus not null, cart_id bigint not null, transaction_fee_id integer not null, primary key (id));
 create table users (avatar varchar(255), email_verified boolean not null, first_name varchar(255), last_name varchar(255), username varchar(30) not null, account_id bigint not null, primary key (account_id));
 create table violation_types (id bigserial not null, name varchar(255) not null, primary key (id));
 create table violations (id bigint not null, created_date timestamp(6) not null, description varchar(255) not null, account_id bigint not null, primary key (id));
 alter table if exists accounts add constraint UK_n7ihswpy07ci568w34q0oi8he unique (email);
 alter table if exists refresh_token add constraint UK_r4k4edos30bx9neoq81mdvwph unique (token);
 alter table if exists refresh_token add constraint UK_1gnoedr4u8s5p7ccdkftq38bh unique (account_id);
 alter table if exists roles add constraint UK_ofx66keruapi6vyqpv6f2or37 unique (name);
 alter table if exists users add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);
 alter table if exists violation_types add constraint UK_i3xy9gqew9wwu9p6wrsokajh2 unique (name);
 alter table if exists account_role add constraint FKlxcbsh4odc9s9spjfw4jgxyqw foreign key (role_id) references roles;
 alter table if exists account_role add constraint FKne5hjaf6i1wun5wnxlxvtl616 foreign key (account_id) references accounts;
 alter table if exists cart_items add constraint FKpcttvuq4mxppo8sxggjtn5i2c foreign key (cart_id) references carts;
 alter table if exists cart_items add constraint FKmv6d5b8f4sruuqfwtxq8d4pcn foreign key (product_id, version) references product_versions;
 alter table if exists carts add constraint FKmbl9cyge43qwntnq4h2rmvv45 foreign key (account_id) references users;
 alter table if exists files add constraint FKrq9nuhnr6dphartkqp3r43yjy foreign key (product_id, version) references product_versions;
 alter table if exists payouts add constraint FKfb48k2tirbd6g7s5k5mgmevsu foreign key (account_id) references sellers;
 alter table if exists payouts add constraint FKph49ybw0nn66lg9ow4un6psv2 foreign key (transaction_id) references transactions;
 alter table if exists previews add constraint FKbpsr69u0guwhqwn1bh3g8ppun foreign key (product_id, version) references product_versions;
 alter table if exists product_versions add constraint FK9v67j2u6qdqv0baxdovbadrmq foreign key (category_id) references categories;
 alter table if exists product_versions add constraint FKs1c6jm7fowox2m4fx1hejc2t8 foreign key (license_id) references licenses;
 alter table if exists product_versions add constraint FKnfvvq3meg4ha3u1bju9k4is3r foreign key (product_id) references products;
 alter table if exists product_version_tag add constraint FKnxwgt56h1t5u4eiyv6j9fsylv foreign key (tag_id) references tags;
 alter table if exists product_version_tag add constraint FKhrqa2cdci934rexx6wtui6x3n foreign key (product_id, version) references product_versions;
 alter table if exists products add constraint FKepbha8uixgrmnejm27n6e1kkd foreign key (seller_id) references sellers;
 alter table if exists refresh_token add constraint FKif9hnfx206niwb61bip7iij29 foreign key (account_id) references accounts;
 alter table if exists reports add constraint FK2wb7diu9f2bue57b2t32g9ac0 foreign key (product_id) references products;
 alter table if exists reports add constraint FKq5i41n512ghow5ge92q2k3iqa foreign key (account_id) references users;
 alter table if exists reports add constraint FKlggn2s05pj6w0kecav9atmfuc foreign key (violation_type_id) references violation_types;
 alter table if exists sellers add constraint FKmqo83vk54uyjc032gh7xolel6 foreign key (account_id) references users;
 alter table if exists transactions add constraint FKnkyl3pyaclxtxgcuo1fdy004d foreign key (cart_id) references carts;
 alter table if exists transactions add constraint FK1bg3d25kvupkcycujp7qnflep foreign key (transaction_fee_id) references transaction_fees;
 alter table if exists users add constraint FKfm8rm8ks0kgj4fhlmmljkj17x foreign key (account_id) references accounts;
 alter table if exists violations add constraint FKluuimmb5jiriqdocjau58qx5t foreign key (account_id) references accounts;