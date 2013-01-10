use ibs;

insert into User (id, passportNumber, firstName, lastName) values(10, 'MP5438532', 'Test1', 'User1');
insert into User (id, passportNumber, firstName, lastName) values(11, 'MP4538654', 'Test2', 'User2');

insert into Account(id, userID, email, role, password) values (10, 10, 'acc1@noreply.edu', 'USER', 'acc1');
insert into Account(id, userID, email, role, password) values (11, 11, 'acc2@noreply.edu', 'USER', 'acc2');

insert into Currency (id, name, factor, fraction) values(10, 'USD', 8354.3, 'TWO');

insert into BankBook(id, ownerID, currencyID, balance) values(10, 10, 10, 10000);
insert into BankBook(id, ownerID, currencyID, balance) values(11, 11, 10, 10000);