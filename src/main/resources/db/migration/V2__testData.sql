 CREATE EXTENSION IF NOT EXISTS pgcrypto;

 insert into transaction_fees(id,percentage) VALUES(1,10);
 
 insert into roles (id,name) values (1,'ROLE_ADMIN');
 insert into roles (id,name) values (2,'ROLE_STAFF');
 insert into roles (id,name) values (3,'ROLE_USER');
 insert into roles (id,name) values (4,'ROLE_SELLER');
 
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'user1@gmail.com', true, null, crypt('user1234', gen_salt('bf')), 1);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'seller1@gmail.com', true, null, crypt('seller1234', gen_salt('bf')), 2);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'seller2@gmail.com', true, null, crypt('seller1234', gen_salt('bf')), 3);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'seller3@gmail.com', true, null, crypt('seller1234', gen_salt('bf')), 4);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'seller4@gmail.com', true, null, crypt('seller1234', gen_salt('bf')), 5);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'staff@gmail.com', true, null, crypt('staff1234', gen_salt('bf')), 6);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'admin@gmail.com', true, null, crypt('admin1234', gen_salt('bf')), 7);

 
 insert into account_role (account_id, role_id) values (1, 3);
 insert into account_role (account_id, role_id) values (2, 3);
 insert into account_role (account_id, role_id) values (2, 4);
 insert into account_role (account_id, role_id) values (3, 3);
 insert into account_role (account_id, role_id) values (3, 4);
 insert into account_role (account_id, role_id) values (4, 3);
 insert into account_role (account_id, role_id) values (4, 4);
 insert into account_role (account_id, role_id) values (5, 3);
 insert into account_role (account_id, role_id) values (5, 4);
 insert into account_role (account_id, role_id) values (6, 2);
 insert into account_role (account_id, role_id) values (7, 1);
 SELECT setval('accounts_seq', (SELECT MAX(id) FROM accounts));

 insert into users (avatar, email_verified, first_name, last_name, username, account_id) values (null, false, 'John', 'Doe', 'usser1', 1);
 insert into users (avatar, email_verified, first_name, last_name, username, account_id) values (null, true, 'John', 'Doe', 'seller1', 2);
 insert into users (avatar, email_verified, first_name, last_name, username, account_id) values (null, true, 'John', 'Doe', 'seller2', 3);
 insert into users (avatar, email_verified, first_name, last_name, username, account_id) values (null, true, 'John', 'Doe', 'seller3', 4);
 insert into users (avatar, email_verified, first_name, last_name, username, account_id) values (null, true, 'John', 'Doe', 'seller4', 5);
 insert into sellers (paypal_email, seller_enabled, account_id) values ('okokokok@gmail.com', true, 2);
 insert into sellers (paypal_email, seller_enabled, account_id) values ('namdhhe150519@fpt.edu', true, 3);
 insert into sellers (paypal_email, seller_enabled, account_id) values ('namdinhdvh@gmail.com', true, 4);
 insert into sellers (paypal_email, seller_enabled, account_id) values ('kokokos@gmail.com', true, 5);
 

 insert into tags(id, name) VALUES(1, '2D');
 insert into tags(id, name) VALUES(2, '3D');
 insert into tags(id, name) VALUES(3, 'adventure');
 insert into tags(id, name) VALUES(4, 'sci-fi');
 insert into tags(id, name) VALUES(5, 'sport'); 
 insert into tags(id, name) VALUES(6, 'horror');
 insert into tags(id, name) VALUES(7, 'PC');
 insert into tags(id, name) VALUES(8, 'mobile'); 
 insert into tags(id, name) VALUES(9, 'pixel');
 insert into tags(id, name) VALUES(10, 'cartoon'); 
 insert into tags(id, name) VALUES(11, 'action'); 
 insert into tags(id, name) VALUES(12, 'tileset');
 insert into tags(id, name) VALUES(13, 'top-down'); 
 insert into tags(id, name) VALUES(14, '16-bit'); 
 insert into tags(id, name) VALUES(15, 'dungeon');

 insert into categories(id,name) VALUES(1, 'Characters');
 insert into categories(id, name) VALUES(2, 'Environments');
 insert into categories(id, name) VALUES(3, 'Audio');
 insert into categories(id, name) VALUES(4, 'Textures');
 insert into categories(id, name) VALUES(5, 'Animations');
 insert into categories(id, name) VALUES(6, 'User Interfaces (UI)');
 insert into categories(id, name) VALUES(7, 'Backgrounds');
 insert into categories(id, name) VALUES(8, 'Props');
 insert into categories(id, name) VALUES(9, 'Others');

 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(1, '', 'The copyright owner exclusive rights to control the use, distribution, and modification of their work, and anyone who wishes to use the work in any way must obtain permission from the copyright owner. This license is the most restrictive and does not allow for any form of sharing, copying, or modification without explicit permission from the copyright holder.','All Rights Reserved','');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(2, 'CC BY', 'This license lets others distribute, remix, adapt, and build upon your work, even commercially, as long as they credit you for the original creation. This is the most accommodating of licenses offered. Recommended for maximum dissemination and use of licensed materials.','Attribution','https://creativecommons.org/licenses/by/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(3, 'CC BY-SA', 'This license lets others remix, adapt, and build upon your work even for commercial purposes, as long as they credit you and license their new creations under the identical terms. This license is often compared to “copyleft” free and open source software licenses. All new works based on yours will carry the same license, so any derivatives will also allow commercial use. This is the license used by Wikipedia, and is recommended for materials that would benefit from incorporating content from Wikipedia and similarly licensed projects.','Attribution-ShareAlike','https://creativecommons.org/licenses/by-sa/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(4, 'CC BY-ND', 'This license lets others reuse the work for any purpose, including commercially; however, it cannot be shared with others in adapted form, and credit must be provided to you.','Attribution-NoDerivs','https://creativecommons.org/licenses/by-nd/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(5, 'CC BY-NC', 'This license lets others remix, adapt, and build upon your work non-commercially, and although their new works must also acknowledge you and be non-commercial, they don’t have to license their derivative works on the same terms.','Attribution-NonCommercial','https://creativecommons.org/licenses/by-nc/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(6, 'CC BY-NC-SA', 'This license lets others remix, adapt, and build upon your work non-commercially, as long as they credit you and license their new creations under the identical terms.','Attribution-NonCommercial-ShareAlike','https://creativecommons.org/licenses/by-nc-sa/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(7, 'CC BY-NC-ND', 'This license is the most restrictive of our six main licenses, only allowing others to download your works and share them with others as long as they credit you, but they can’t change them in any way or use them commercially.','Attribution-NonCommercial-NoDerivs','https://creativecommons.org/licenses/by-nc-nd/4.0/');
 
 insert into violation_types VALUES (1,'Sexual content');
 insert into violation_types VALUES (2,'Violent content');
 insert into violation_types VALUES (3,'Hateful content');
 insert into violation_types VALUES (4,'Misinformation');
 insert into violation_types VALUES (5,'Spam');
 insert into violation_types VALUES (6,'Copyright infringement');
 insert into violation_types VALUES (7,'Promote terrorism');
 
 
 

 
 
 
 
 
 
 

