drop database if exists ibs;
create database ibs DEFAULT CHARACTER SET utf8;
use ibs;

create table User
(
	id bigint primary key auto_increment,
	passportNumber varchar(255) unique not null,
	passportScan blob,
	firstName varchar(255),
	lastName varchar(255) not null,
	freezed boolean not null default 0,
	address varchar(255),
	zipCode int,
	phone1 bigint,
	phone2 bigint,
	phone3 bigint,
	description varchar(255)
)engine InnoDB;

create table Account
(
	id bigint primary key auto_increment,
	userID bigint,
	email varchar(255) unique not null,
	role enum('USER', 'ADMIN') not null default 'user',
	password varchar(255) not null,
	securityQuestion varchar(255),
	securityAnswer varchar(255),
	avatar varchar(255),
	INDEX userIndex (userID),
	FOREIGN KEY (userID) REFERENCES User(id) on delete set null on update cascade
)engine InnoDB;

create table Currency
(
	id bigint primary key auto_increment,
	name varchar(8) unique not null,
	lastUpdated bigint not null,
	factor float not null,
	fraction enum('ZERO', 'ONE', 'TWO', 'THREE') not null
)engine InnoDB;

create table BankBook
(
	id bigint primary key auto_increment,
	ownerID bigint not null,
	currencyID bigint not null,
	balance bigint not null default 0,
	dateExpire bigint not null default 0,
	freezed boolean not null default 0,
	description varchar(255),
	INDEX ownerIndex (ownerID),
	INDEX currencyIndex (currencyID),
        FOREIGN KEY (ownerID) REFERENCES User(id) on delete restrict on update cascade,
	FOREIGN KEY (currencyID) REFERENCES Currency(id) on delete restrict on update cascade
)engine InnoDB;

create table CreditPlan
(
	id bigint primary key auto_increment,
	name varchar(255) unique not null,
	currencyID bigint not null,
	percent int not null default 0,
	period enum('DAY', 'WEEK', 'MONTH', 'YEAR') not null default 'month',
	periodMultiply int not null default 1,
	moneyLimit bigint not null default 0,
	freezed boolean not null default 0,
	INDEX currencyIndex (currencyID),
	FOREIGN KEY (currencyID) REFERENCES Currency(id) on delete restrict on update cascade
)engine InnoDB;

create table Credit
(
	id bigint primary key auto_increment,
	creditPlanID bigint not null,
	amount bigint not null default 0,
	lastPayDate bigint not null,
	INDEX creditPlanIndex (creditPlanID),
	FOREIGN KEY (creditPlanID) REFERENCES CreditPlan(id) on delete restrict on update cascade
)engine InnoDB;

create table CardBook
(
	id bigint primary key auto_increment,
	bankBookID bigint not null,
	ownerID bigint not null,
	creditID bigint,
	type enum('DEBIT', 'CREDIT') not null default 'DEBIT',
	dateExpire bigint not null default 0,
	freezed boolean not null default 0,
	pin int not null, 
	INDEX creditIndex (creditID),
	INDEX bankBookIndex (bankBookID),
	INDEX ownerIndex (ownerID),
	FOREIGN KEY (creditID) REFERENCES Credit(id) on delete restrict on update cascade,
	FOREIGN KEY (ownerID) REFERENCES User(id) on delete restrict on update cascade,
	FOREIGN KEY (bankBookID) REFERENCES BankBook(id) on delete restrict on update cascade
)engine InnoDB;

create table Provider
(
	id bigint primary key auto_increment,
	cardBookID bigint not null,
	field1 enum('PHONE', 'NAME', 'ADDRESS', 'PASSPORT') default null,
	field2 enum('PHONE', 'NAME', 'ADDRESS', 'PASSPORT') default null,
	field3 enum('PHONE', 'NAME', 'ADDRESS', 'PASSPORT') default null,
	field4 enum('PHONE', 'NAME', 'ADDRESS', 'PASSPORT') default null,
	field5 enum('PHONE', 'NAME', 'ADDRESS', 'PASSPORT') default null,
	INDEX cardBookIndex (cardBookID),
	FOREIGN KEY (cardBookID) REFERENCES CardBook(id) on delete cascade on update cascade
)engine InnoDB;

create table Transaction
(
	id bigint primary key auto_increment,
	fromCardBookID bigint not null,
	toCardBookID bigint not null,
	currencyID bigint not null,
	amount bigint not null,
	date bigint not null default 0,
	type enum('PAYMENT', 'TRANSFER') not null,
	description varchar(255),
	INDEX fromCBIndex (fromCardBookID),
	INDEX toCBIndex (toCardBookID),
	INDEX currencyIndex (currencyID),
	FOREIGN KEY (fromCardBookID) REFERENCES CardBook(id) on delete restrict on update cascade,
	FOREIGN KEY (toCardBookID) REFERENCES CardBook(id) on delete restrict on update cascade,
	FOREIGN KEY (currencyID) REFERENCES Currency(id) on delete restrict on update cascade
)engine InnoDB;

create table Autopay
(
	id bigint primary key auto_increment,
	amount bigint not null default 0,	
	period bigint not null default 0,
        lastPayed bigint not null default 0,
	currencyID bigint not null,
	fromCardBookID bigint not null,
	toCardBookID bigint not null,
	INDEX currencyIndex (currencyID),
        INDEX cardBook1Index (fromCardBookID),
        INDEX cardBook2Index (toCardBookID),
	FOREIGN KEY (fromCardBookID) REFERENCES CardBook(id) on delete cascade on update cascade,
	FOREIGN KEY (toCardBookID) REFERENCES CardBook(id) on delete cascade on update cascade,
	FOREIGN KEY (currencyID) REFERENCES Currency(id) on delete cascade on update cascade
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

create table CardRequest
(
	id bigint primary key auto_increment,
	userID bigint not null,
	bankBookID bigint,
	creditPlanID bigint,
	cardBookID bigint,
	type enum('DEBIT', 'CREDIT') not null default 'DEBIT',
	dateCreated bigint not null,
	dateWatched bigint,
	approved boolean not null default 0,
    watched boolean not null default 0,
	reason varchar(255),
	INDEX cardAccountIndex (cardBookID),
	INDEX bankBookIndex (bankBookID),
	INDEX userIndex (userID),
	INDEX creditIndex (creditPlanID),
	FOREIGN KEY (userID) REFERENCES User(id) on delete cascade on update cascade,
	FOREIGN KEY (creditPlanID) REFERENCES CreditPlan(id) on delete cascade on update cascade,
	FOREIGN KEY (cardBookID) REFERENCES CardBook(id) on delete cascade on update cascade,
	FOREIGN KEY (bankBookID) REFERENCES BankBook(id) on delete cascade on update cascade
)engine InnoDB;

insert into User (id, passportNumber, firstName, lastName, description) values(1, '1', 'skip', 'skip', 'Bank');
insert into User (id, passportNumber, firstName, lastName, description) values (2, 'mts_fake', 'skip', 'skip', 'ООО МТС');
insert into User (id, passportNumber, firstName, lastName, description) values (3, 'velcom_fake', 'skip', 'skip', 'ИП Велком');
insert into User (id, passportNumber, firstName, lastName, description) values (4, 'beltelecom_byfly_fake', 'skip', 'skip', 'Byfly');

insert into Account(id, email, role, password) values (1, 'root@noreply.edu', 'ADMIN', 'root');

insert into Currency (id, name, factor, fraction, lastUpdated) values(1, 'BR', 1, 'ZERO', UNIX_TIMESTAMP());
insert into Currency (id, name, factor, fraction, lastUpdated) values(2, 'EUR', 101.204, 'TWO', UNIX_TIMESTAMP());
insert into Currency (id, name, factor, fraction, lastUpdated) values(3, 'USD', 85.605, 'TWO', UNIX_TIMESTAMP());

insert into BankBook(id, ownerID, currencyID, balance, description) values(1, 1, 1, 1000000000, 'Внутренный банк-счет кредитов');
insert into BankBook(id, ownerID, currencyID, balance, description) values(2, 2, 1, 0, 'Платежный счет услуг МТС');
insert into BankBook(id, ownerID, currencyID, balance, description) values(3, 3, 1, 0, 'Платежный счет услуг Велком');
insert into BankBook(id, ownerID, currencyID, balance, description) values(4, 4, 1, 0, 'Платежный счет услуг Byfly');

insert into CreditPlan(id, name, currencyID, percent, period, periodMultiply, moneyLimit) values(1, 'National Credit', 1, 5, 'WEEK', 1, 1000000);

insert into CardBook(id, bankBookID, ownerID, type, pin) values(1, 1, 1, 'DEBIT', 1111);
insert into CardBook(id, bankBookID, ownerID, type, pin) values(2, 2, 2, 'DEBIT', 1111);
insert into CardBook(id, bankBookID, ownerID, type, pin) values(3, 3, 3, 'DEBIT', 1111);
insert into CardBook(id, bankBookID, ownerID, type, pin) values(4, 4, 4, 'DEBIT', 1111);