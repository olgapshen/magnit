drop database if exists `magnit`;
create database `magnit`;
use `magnit`;
create table `test` (`field` int, primary key(`field`));
drop user if exists 'magnit'@'localhost';
create user 'magnit'@'localhost' IDENTIFIED BY '123456';
grant all privileges on `magnit`.* to 'magnit'@'localhost';
flush privileges;
