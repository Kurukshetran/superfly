call run_install_command('alter table users add column name varchar(32) not null', '42S21');
call run_install_command('alter table users add column surname varchar(32) not null', '42S21');
call run_install_command('alter table users add column secret_question varchar(255) not null', '42S21');
call run_install_command('alter table users add column secret_answer varchar(255) not null', '42S21');
call run_install_command('alter table users modify column user_password varchar(128) not null', '42S21');
call run_install_command('alter table users add column salt varchar(64) not null', '42S21');
