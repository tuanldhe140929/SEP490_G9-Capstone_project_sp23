CREATE EXTENSION IF NOT EXISTS pgcrypto;
 insert into role (id,name) values (1,'ROLE_ADMIN');
 insert into role (id,name) values (2,'ROLE_STAFF');
 insert into role (id,name) values (3,'ROLE_USER');
 insert into role (id,name) values (4,'ROLE_SELLER');
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'user1@gmail.com', true, null, crypt('user1234', gen_salt('bf')), 1);
 insert into users (avatar, cart_id, email_verified, first_name, last_name, username, account_id) values (null, null, false, 'John', 'Doe', 'usser1', 1);
 insert into account_role (account_id, role_id) values (1, 3);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'seller1@gmail.com', true, null, crypt('seller1234', gen_salt('bf')), 2);
 insert into users (avatar, cart_id, email_verified, first_name, last_name, username, account_id) values (null, null, false, 'John', 'Doe', 'seller1', 2);
 insert into sellers (phone_number, seller_enabled, account_id) values (null, true, 2);
 insert into account_role (account_id, role_id) values (2, 3);
 insert into account_role (account_id, role_id) values (2, 4);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'staff@gmail.com', true, null, crypt('staff1234', gen_salt('bf')), 3);
 insert into account_role (account_id, role_id) values (3, 2);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'admin@gmail.com', true, null, crypt('admin1234', gen_salt('bf')), 4);
 insert into account_role (account_id, role_id) values (4, 1);
 insert into carts (id,account_id) VALUES (1,1);
 insert into carts (id,account_id) values (2,2);
