DELETE FROM user_roles;
DELETE FROM users;


INSERT INTO users (
	username,
	password,
	enabled
)
VALUES (
	'user',
	'$2a$12$DmVdNc5meXTrOqNdYeFSTeMP.J/kI.zxOhfDOohPh5VQD8FWUqN2G',
	true
);
INSERT INTO users (
	username,
	password,
	enabled
)
VALUES (
	'admin',
	'$2a$12$DmVdNc5meXTrOqNdYeFSTeMP.J/kI.zxOhfDOohPh5VQD8FWUqN2G',
	true
);
INSERT INTO user_roles (
	username,
	role
)
VALUES (
	'user',
	'USER'
);
INSERT INTO user_roles (
	username,
	role
)
VALUES (
	'admin',
	'USER'
);
INSERT INTO user_roles (
	username,
	role)
VALUES (
	'admin', 
	'ADMIN'
);
