INSERT INTO commands (command, description) VALUES ('command1', 'descricao1');
INSERT INTO commands (command, description) VALUES ('command2', 'descricao2');
INSERT INTO commands (command, description) VALUES ('command3', 'descricao3');

INSERT INTO users (username, password, role) VALUES ('admin', '$2a$12$jSaF6lQMuyFefy5Tf4a94eoVrPoxGUkY7XzFo.APYU4JYguIpcfXa', 'ADMIN');
INSERT INTO users (username, password, role) VALUES ('user', '$2a$12$xlRFhVPqOHGEPyMV8wUyiONS3XzOvlBYQrUjGkHpa6mTUu3kGkJje', 'USER');

INSERT INTO devices (identifier, description, manufacturer, url, status, user_id) VALUES ('device1', 'descricao', 'manufacturer', 'telnet', true, 1);
INSERT INTO devices (identifier, description, manufacturer, url, status, user_id) VALUES ('device2', 'descricao', 'manufacturer', 'telnet', true, 2);

INSERT INTO devices_commands (device_id, command_id) VALUES (1,1);
INSERT INTO devices_commands (device_id, command_id) VALUES (1,2);
INSERT INTO devices_commands (device_id, command_id) VALUES (2,1);