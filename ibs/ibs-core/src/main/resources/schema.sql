charset utf8;
drop database if exists ibs;
create database ibs;
use ibs;

create table User
(
	id bigint primary key auto_increment,
	role enum('USER', 'ADMIN') not null default 'user',
	firstName varchar(255),
	lastName varchar(255),
	email varchar(255) unique not null,
	description varchar(255)
) ENGINE InnoDB;

create table Currency
(
	id bigint primary key auto_increment,
	name varchar(255) unique not null,
	factor float not null
)engine InnoDB;

insert into Currency (id, name, factor) values(1, 'Br', 1);

create table BankBook
(
	id bigint primary key auto_increment,
	ownerID bigint not null,
	currencyID bigint not null,
	balance bigint not null default 0,
	freezed boolean not null default 0,
	INDEX ownerIndex (ownerID),
	INDEX currencyIndex (currencyID),
        FOREIGN KEY (ownerID) REFERENCES User(id) on delete restrict on update cascade,
	FOREIGN KEY (currencyID) REFERENCES Currency(id) on delete restrict on update cascade
)engine InnoDB;

create table Transaction
(
	id bigint primary key auto_increment,
	fromBankBookID bigint not null,
	toBankBookID bigint not null,
	currencyID bigint not null,
	amount bigint not null,
	type enum('PAYMENT', 'TRABSFER') not null,
	INDEX fromBBIndex (fromBankBookID),
	INDEX toBBIndex (toBankBookID),
	INDEX currencyIndex (currencyID),
	FOREIGN KEY (fromBankBookID) REFERENCES BankBook(id) on delete restrict on update cascade,
	FOREIGN KEY (toBankBookID) REFERENCES BankBook(id) on delete restrict on update cascade,
	FOREIGN KEY (currencyID) REFERENCES Currency(id) on delete restrict on update cascade
)engine InnoDB;

create table Request
(
	id bigint primary key auto_increment,
	userID bigint not null,
	date long not null,
	info varchar(255),
	FOREIGN KEY (userID) REFERENCES User(id) on delete restrict on update cascade
)engine InnoDB;

create table SavedPayment
(
	id bigint primary key auto_increment,
	userID bigint not null,
	transactionID bigint not null,
	INDEX userIndex (userID),
	INDEX transactionIndex (transactionID),
	FOREIGN KEY (userID) REFERENCES User(id) on delete cascade on update cascade,
	FOREIGN KEY (transactionID) REFERENCES Transaction(id) on delete cascade on update cascade
)engine InnoDB;