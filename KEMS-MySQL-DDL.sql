CREATE DATABASE kemsmysql;

USE kemsmysql;

GRANT ALL PRIVILEGES ON kemsmysql.* TO 'kems001'@'localhost';
GRANT ALL PRIVILEGES ON kemsmysql.* TO 'kems001'@'%';

-- kems table
select * from users;
select * from login_info;
select * from students;
select * from tutors;
select * from employees;