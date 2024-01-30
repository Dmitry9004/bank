create table users(
	id INT PRIMARY KEY AUTO_INCREMENT,
	username varchar(255) UNIQUE,
	password text,
	phone_number varchar(12)
)

create table accounts(
	userId int PRIMARY KEY AUTO_INCREMENT,
	bankIdentificationCode INT,
	total decimal(10,1),
	status varchar(255),
	type_account varchar(255),
	date_update DATE
)

create table cards(
	userId INT PRIMARY KEY AUTO_INCREMENT,
	phone varchar(12),
	cardNumber int,
	total = 0 decimal(10,1),
	status varchar(255),
	type_card varchar(255),
)
