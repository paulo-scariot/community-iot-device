INSERT INTO commands (command) VALUES ('command1');
INSERT INTO commands (command) VALUES ('command2');
INSERT INTO commands (command) VALUES ('command3');

INSERT INTO parameters (name, description, command_id) VALUES ('parameter1', 'descricao', 1);
INSERT INTO parameters (name, description, command_id) VALUES ('parameter2', 'descricao', 1);
INSERT INTO parameters (name, description, command_id) VALUES ('parameter3', 'descricao', 2);
INSERT INTO parameters (name, description, command_id) VALUES ('parameter4', 'descricao', 2);
INSERT INTO parameters (name, description, command_id) VALUES ('parameter5', 'descricao', 2);
INSERT INTO parameters (name, description, command_id) VALUES ('parameter6', 'descricao', 3);

INSERT INTO users (username, password) VALUES ('admin', '$2a$12$jSaF6lQMuyFefy5Tf4a94eoVrPoxGUkY7XzFo.APYU4JYguIpcfXa');
INSERT INTO users (username, password) VALUES ('user', '$2a$12$xlRFhVPqOHGEPyMV8wUyiONS3XzOvlBYQrUjGkHpa6mTUu3kGkJje');

INSERT INTO devices (identifier, description, manufacturer, url ,user_id) VALUES ('device1', 'descricao', 'manufacturer', 'localhost', 1);
INSERT INTO devices (identifier, description, manufacturer, url ,user_id) VALUES ('device2', 'descricao', 'manufacturer', 'localhost', 1);
INSERT INTO devices (identifier, description, manufacturer, url ,user_id) VALUES ('device3', 'descricao', 'manufacturer', 'localhost', 2);

INSERT INTO command_descriptions (operation, description, result, format, device_id ,command_id) VALUES ('operation1', 'descricao', 'result', 'string', 1, 1);
INSERT INTO command_descriptions (operation, description, result, format, device_id ,command_id) VALUES ('operation2', 'descricao', 'result', 'string', 1, 1);
INSERT INTO command_descriptions (operation, description, result, format, device_id ,command_id) VALUES ('operation3', 'descricao', 'result', 'string', 2, 2);
INSERT INTO command_descriptions (operation, description, result, format, device_id ,command_id) VALUES ('operation4', 'descricao', 'result', 'string', 3, 3);
