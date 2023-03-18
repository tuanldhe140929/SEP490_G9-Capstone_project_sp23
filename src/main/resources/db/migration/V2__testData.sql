 CREATE EXTENSION IF NOT EXISTS pgcrypto;
 
 insert into roles (id,name) values (1,'ROLE_ADMIN');
 insert into roles (id,name) values (2,'ROLE_STAFF');
 insert into roles (id,name) values (3,'ROLE_USER');
 insert into roles (id,name) values (4,'ROLE_SELLER');
 
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'user1@gmail.com', true, null, crypt('user1234', gen_salt('bf')), 1);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'seller1@gmail.com', true, null, crypt('seller1234', gen_salt('bf')), 2);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'staff@gmail.com', true, null, crypt('staff1234', gen_salt('bf')), 3);
 insert into accounts (created_date, email, enabled, last_modified, password, id) values (CURRENT_DATE, 'admin@gmail.com', true, null, crypt('admin1234', gen_salt('bf')), 4);
 
 insert into account_role (account_id, role_id) values (1, 3);
 insert into account_role (account_id, role_id) values (2, 3);
 insert into account_role (account_id, role_id) values (2, 4);
 insert into account_role (account_id, role_id) values (3, 2);
 insert into account_role (account_id, role_id) values (4, 1);
 
 insert into users (avatar, cart_id, email_verified, first_name, last_name, username, account_id) values (null, null, false, 'John', 'Doe', 'usser1', 1);
 insert into users (avatar, cart_id, email_verified, first_name, last_name, username, account_id) values (null, null, false, 'John', 'Doe', 'seller1', 2);
 insert into sellers (phone_number, seller_enabled, account_id) values (null, true, 2);

 insert into tags(id, name) VALUES(1, '2D');
 insert into tags(id, name) VALUES(2, '3D');
 insert into tags(id, name) VALUES(3, 'adventure');
 insert into tags(id, name) VALUES(4, 'sci-fi');
 insert into tags(id, name) VALUES(5, 'sport'); 

 insert into categories(id,name) VALUES(1, 'Sprites');
 insert into categories(id, name) VALUES(2, 'Sound effects');
 insert into categories(id, name) VALUES(3, 'Music');
 insert into categories(id, name) VALUES(4, 'Textures');
 insert into categories(id, name) VALUES(5, 'Characters');
 insert into categories(id, name) VALUES(6, 'Tileset');
 insert into categories(id, name) VALUES(7, 'Backgrounds');
 insert into categories(id, name) VALUES(8, 'Fonts');
 insert into categories(id, name) VALUES(9, 'Icons');
 insert into categories(id, name) VALUES(10, 'Tileset');
 insert into categories(id, name) VALUES(11, 'User interfaces');
 insert into categories(id, name) VALUES(12, 'Lore');
 insert into categories(id, name) VALUES(13, 'Others');

 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(1, '', 'The copyright owner exclusive rights to control the use, distribution, and modification of their work, and anyone who wishes to use the work in any way must obtain permission from the copyright owner. This license is the most restrictive and does not allow for any form of sharing, copying, or modification without explicit permission from the copyright holder.','All Rights Reserved','');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(2, 'CC BY', 'This license lets others distribute, remix, adapt, and build upon your work, even commercially, as long as they credit you for the original creation. This is the most accommodating of licenses offered. Recommended for maximum dissemination and use of licensed materials.','Attribution','https://creativecommons.org/licenses/by/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(3, 'CC BY-SA', 'This license lets others remix, adapt, and build upon your work even for commercial purposes, as long as they credit you and license their new creations under the identical terms. This license is often compared to “copyleft” free and open source software licenses. All new works based on yours will carry the same license, so any derivatives will also allow commercial use. This is the license used by Wikipedia, and is recommended for materials that would benefit from incorporating content from Wikipedia and similarly licensed projects.','Attribution-ShareAlike','https://creativecommons.org/licenses/by-sa/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(4, 'CC BY-ND', 'This license lets others reuse the work for any purpose, including commercially; however, it cannot be shared with others in adapted form, and credit must be provided to you.','Attribution-NoDerivs','https://creativecommons.org/licenses/by-nd/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(5, 'CC BY-NC', 'This license lets others remix, adapt, and build upon your work non-commercially, and although their new works must also acknowledge you and be non-commercial, they don’t have to license their derivative works on the same terms.','Attribution-NonCommercial','https://creativecommons.org/licenses/by-nc/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(6, 'CC BY-NC-SA', 'This license lets others remix, adapt, and build upon your work non-commercially, as long as they credit you and license their new creations under the identical terms.','Attribution-NonCommercial-ShareAlike','https://creativecommons.org/licenses/by-nc-sa/4.0/');
 insert into licenses(id, acrynosm, details, name, reference_link) VALUES(7, 'CC BY-NC-ND', 'This license is the most restrictive of our six main licenses, only allowing others to download your works and share them with others as long as they credit you, but they can’t change them in any way or use them commercially.','Attribution-NonCommercial-NoDerivs','https://creativecommons.org/licenses/by-nc-nd/4.0/');

 insert into engines(id, name) VALUES(1,'Unity');
 insert into engines(id, name) VALUES(2,'Unreal');
 insert into engines(id, name) VALUES(3,'Godot');
 insert into engines(id, name) VALUES(4,'Cocos');
  
 /*insert into products (id, active_version, enabled, approved, seller_id) VALUES(1,'1.0.0',true,'APPROVED',2);
 insert into products (id, active_version, enabled, approved, seller_id) VALUES(2,'1.0.0',true,'APPROVED',2);
 insert into products (id, active_version, enabled, approved, seller_id) VALUES(3,'1.0.0',true,'REJECTED',2);
 insert into products (id, active_version, enabled, approved, seller_id) VALUES(4,'1.0.0',true,'REJECTED',2);
 insert into products (id, active_version, enabled, approved, seller_id) VALUES(5,'1.0.0',true,'PENDING',2);
 
 insert into product_details (product_id, version, cover_image, upload_date, description, detail_description, draft, instruction, last_update, name, price, category_id, engine_id, license_id)
 VALUES(1,'1.0.0',null,CURRENT_DATE,'TEST VERSION','TEST VERSION',true,'TEST VERSION', CURRENT_DATE, 'TEST VERSION', 13000, 1,1,2);
 insert into product_details (product_id, version, cover_image, upload_date, description, detail_description, draft, instruction, last_update, name, price, category_id, engine_id, license_id)
 VALUES(1,'1.0.1',null,CURRENT_DATE,'TEST VERSION','TEST VERSION',true,'TEST VERSION', CURRENT_DATE, 'TEST VERSION', 1000, 2,3,2);
 insert into product_details (product_id, version, cover_image, upload_date, description, detail_description, draft, instruction, last_update, name, price, category_id, engine_id, license_id)
 VALUES(2,'1.0.0',null,CURRENT_DATE,'TEST VERSION','TEST VERSION',false,'TEST VERSION', CURRENT_DATE, 'TEST VERSION', 100000, 1,2,1);
 insert into product_details (product_id, version, cover_image, upload_date, description, detail_description, draft, instruction, last_update, name, price, category_id, engine_id, license_id)
 VALUES(3,'1.0.0',null,CURRENT_DATE,'TEST VERSION','TEST VERSION',false,'TEST VERSION', CURRENT_DATE, 'TEST VERSION', 0, 3,1,4);
 insert into product_details (product_id, version, cover_image, upload_date, description, detail_description, draft, instruction, last_update, name, price, category_id, engine_id, license_id)
 VALUES(4,'1.0.0',null,CURRENT_DATE,'TEST VERSION','TEST VERSION',true,'TEST VERSION', CURRENT_DATE, 'TEST VERSION', 0, 4,4,3);
 
 insert into product_details_tag VALUES(1,'1.0.0',3);
 insert into product_details_tag VALUES(1,'1.0.0',1);
 insert into product_details_tag VALUES(1,'1.0.1',2);
 insert into product_details_tag VALUES(1,'1.0.1',4);
 insert into product_details_tag VALUES(2,'1.0.0',1);
 insert into product_details_tag VALUES(2,'1.0.0',3);
 insert into product_details_tag VALUES(3,'1.0.0',2);
 insert into product_details_tag VALUES(4,'1.0.0',2);
 insert into product_details_tag VALUES(4,'1.0.0',1);
 
 insert into previews (id, source, type,product_id, version) VALUES(1,'account_id_2\products\1\(1) file_example_MOV_1920_2_2MB.mov','video',1,'1.0.0');
 insert into previews (id, source, type,product_id, version) VALUES(2,'account_id_2\products\1\Database V2.drawio.png','picture',1,'1.0.0');
 insert into previews (id, source, type,product_id, version) VALUES(3,'account_id_2\products\1\ERD V2.drawio.png','picture',1,'1.0.1');
 insert into previews (id, source, type,product_id, version) VALUES(4,'account_id_2\products\1\Overall Architecture.drawio (1).png','picture',1,'1.0.1');
 insert into previews (id, source, type,product_id, version) VALUES(5,'account_id_2\products\1\(1) file_example_MOV_1920_2_2MB.mov','video',2,'1.0.0');
 insert into previews (id, source, type,product_id, version) VALUES(6,'account_id_2\products\1\Database V2.drawio (3).png','picture',2,'1.0.0');
 insert into previews (id, source, type,product_id, version) VALUES(7,'account_id_2\products\1\Overall Architecture.drawio (3).png','picture',3,'1.0.0');
 insert into previews (id, source, type,product_id, version) VALUES(8,'account_id_2\products\1\Untitled Diagram.drawio.png','picture',4,'1.0.0');
 
 insert into files (id, name, size, source, type, product_id, version) VALUES(1,'(1) file_example_MOV_1920_2_2MB.mov',100,'account_id_2\products\1\(1) file_example_MOV_1920_2_2MB.mov','image/png',1,'1.0.0');
 insert into files (id, name, size, source, type, product_id, version) VALUES(2,'Database V2.drawio.png',100,'account_id_2\products\1\Database V2.drawio.png','image/png',1,'1.0.0');
 insert into files (id, name, size, source, type, product_id, version) VALUES(3,'ERD V2.drawio.png',100,'account_id_2\products\1\ERD V2.drawio.png','image/png',1,'1.0.1');
 insert into files (id, name, size, source, type, product_id, version) VALUES(4,'(1) file_example_MOV_1920_2_2MB.mov',100,'account_id_2\products\1\(1) file_example_MOV_1920_2_2MB.mov','video/mov',2,'1.0.0');
 insert into files (id, name, size, source, type, product_id, version) VALUES(5,'Database V2.drawio (3).png',100,'account_id_2\products\1\Database V2.drawio (3).png','image/png',2,'1.0.0');
 insert into files (id, name, size, source, type, product_id, version) VALUES(6,'Overall Architecture.drawio (3).png',100,'account_id_2\products\1\Overall Architecture.drawio (3).png','image/png',3,'1.0.0');
 insert into files (id, name, size, source, type, product_id, version) VALUES(7,'Untitled Diagram.drawio.png',100,'account_id_2\products\1\Untitled Diagram.drawio.png','image/png',4,'1.0.0');
 


  
 
 
 
 
 
 
 

 
 
 
 
 