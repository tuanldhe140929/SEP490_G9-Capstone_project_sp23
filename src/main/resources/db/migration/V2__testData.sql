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
 insert into tags(id, name) VALUES(3, 'phiêu lưu');
 insert into tags(id, name) VALUES(4, 'viễn tưởng');
 insert into tags(id, name) VALUES(5, 'thể thao'); 
 insert into tags(id, name) VALUES(6, 'kinh dị');
 insert into tags(id, name) VALUES(7, 'PC');
 insert into tags(id, name) VALUES(8, 'mobile'); 
 insert into tags(id, name) VALUES(9, 'pixel');
 insert into tags(id, name) VALUES(10, 'hoạt hình'); 
 insert into tags(id, name) VALUES(11, 'hành động'); 
 insert into tags(id, name) VALUES(12, 'quái vật');
 insert into tags(id, name) VALUES(13, 'top-down'); 
 insert into tags(id, name) VALUES(14, '16-bit'); 
 insert into tags(id, name) VALUES(15, 'hầm ngục');

 insert into categories(id, name, description) VALUES(1, 'Characters', 'Nhân vật - Là các đối tượng có hình dạng và tính cách riêng biệt, được sử dụng trong các trò chơi.');
 insert into categories(id, name, description) VALUES(2, 'Environments', 'Môi trường - Là các cảnh vật, bối cảnh, không gian được tạo ra để phù hợp với game.');
 insert into categories(id, name, description) VALUES(3, 'Audio', 'Là các tệp âm thanh được sử dụng để tạo ra âm thanh và âm nhạc trong trò chơi.');
 insert into categories(id, name, description) VALUES(4, 'Textures', 'Hình ảnh - Là các hình ảnh được sử dụng để trang trí và tạo ra hình ảnh cho các đối tượng trong trò chơi.');
 insert into categories(id, name, description) VALUES(5, 'Animations', 'Hoạt hình - Là các hoạt hình được tạo ra để cung cấp chuyển động và hành động cho các đối tượng trong trò chơi.');
 insert into categories(id, name, description) VALUES(6, 'User Interfaces (UI)', 'Giao diện người dùng - Là các thành phần của giao diện người dùng được sử dụng để giúp người chơi tương tác với trò chơi.');
 insert into categories(id, name, description) VALUES(7, 'Backgrounds', 'Phông nền - Là các hình ảnh được sử dụng làm phông nền cho các màn chơi trong trò chơi.' );
 insert into categories(id, name, description) VALUES(8, 'Props', 'Vật phẩm - Là các đối tượng nhỏ được sử dụng trong trò chơi để tạo ra sự sống động và tạo nên câu chuyện của trò chơi.');
 insert into categories(id, name, description) VALUES(9, 'Khác', null);

 insert into licenses(id, acronyms, details, name, reference_link) VALUES(1, '', 'Chủ sở hữu bản quyền độc quyền kiểm soát việc sử dụng, phân phối và sửa đổi tác phẩm của họ và bất kỳ ai muốn sử dụng tác phẩm theo bất kỳ cách nào đều phải xin phép chủ sở hữu bản quyền. Giấy phép này là hạn chế nhất và không cho phép bất kỳ hình thức chia sẻ, sao chép hoặc sửa đổi nào mà không có sự cho phép rõ ràng của chủ sở hữu bản quyền.','All Rights Reserved','');
 INSERT INTO licenses (id, acronyms, details, name, reference_link)
VALUES (2, 'CC BY', 'Cho phép bất kỳ ai sử dụng tác phẩm của bạn, bao gồm cả việc chỉnh sửa, thương mại hóa và tái phân phối, miễn là họ ghi công bạn theo cách bạn yêu cầu.', 'Attribution', 'https://creativecommons.org/licenses/by/4.0/');

-- Giấy phép CC BY-SA
INSERT INTO licenses (id, acronyms, details, name, reference_link)
VALUES (3, 'CC BY-SA', 'cho phép bất kỳ ai sử dụng, chia sẻ, chỉnh sửa hoặc thương mại hóa tác phẩm của bạn, miễn là họ ghi công bạn và chia sẻ tác phẩm của họ dưới các điều khoản giấy phép tương tự như bạn.', 'Attribution-ShareAlike', 'https://creativecommons.org/licenses/by-sa/4.0/');

-- Giấy phép CC BY-ND
INSERT INTO licenses (id, acronyms, details, name, reference_link)
VALUES (4, 'CC BY-ND', 'cho phép bất kỳ ai sử dụng, chia sẻ hoặc tái phân phối tác phẩm của bạn mà không có tác phẩm phái sinh, miễn là họ ghi công bạn theo cách bạn yêu cầu.', 'Attribution-NoDerivatives', 'https://creativecommons.org/licenses/by-nd/4.0/');

-- Giấy phép CC BY-NC
INSERT INTO licenses (id, acronyms, details, name, reference_link)
VALUES (5, 'CC BY-NC', 'Giấy phép Creative Commons cho phép bất kỳ ai sử dụng tác phẩm của bạn cho mục đích phi thương mại, miễn là họ ghi công bạn theo cách bạn yêu cầu.', 'Attribution-NonCommercial', 'https://creativecommons.org/licenses/by-nc/4.0/');

-- Giấy phép CC BY-NC-SA
INSERT INTO licenses (id, acronyms, details, name, reference_link)
VALUES (6, 'CC BY-NC-SA', 'cho phép bất kỳ ai sử dụng, chia sẻ, chỉnh sửa hoặc thương mại hóa tác phẩm của bạn cho mục đích phi thương mại, miễn là họ ghi công bạn và chia sẻ tác phẩm của họ dưới các điều khoản giấy phép tương tự như bạn.', 'Attribution-NonCommercial-ShareAlike', 'https://creativecommons.org/licenses/by-nc-sa/4.0/');

-- Giấy phép CC BY-NC-ND
INSERT INTO licenses (id, acronyms, details, name, reference_link)
VALUES (7, 'CC BY-NC-ND', 'cho phép bất kỳ ai sử dụng tác phẩm của bạn cho mục đích phi thương mại và không có tác phẩm phái sinh, miễn là họ ghi công bạn theo cách bạn yêu cầu.', 'Attribution-NonCommercial-NoDerivatives', 'https://creativecommons.org/licenses/by-nc-sa/4.0/');

 insert into violation_types VALUES (1,'Spam');
 insert into violation_types VALUES (2,'Đạo nhái');
 insert into violation_types VALUES (3,'Vi phạm bản quyền');
 insert into violation_types VALUES (4,'Quảng cáo sai sự thật');
 

 
 
 

 
 
 
 
 
 
 

