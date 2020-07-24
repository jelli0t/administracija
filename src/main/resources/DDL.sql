create table if not exists customer
(
	id int(9) auto_increment
		primary key,
	customer_code varchar(16) null,
	name varchar(255) charset utf8 not null,
	alt_name varchar(255) charset utf8 null,
	created_on timestamp null,
	modified_on timestamp null,
	phone varchar(16) null,
	cell_phone varchar(16) null,
	email varchar(255) null,
	pib varchar(10) null,
	place varchar(64) charset utf8 null,
	zip_code int(6) null,
	address varchar(255) charset utf8 null,
	description varchar(255) null,
	constraint CUST_UNQ_CODE
		unique (customer_code)
);

create table if not exists invoice
(
	id int(9) auto_increment
		primary key,
	invoice_no varchar(16) null,
	customer_id int(9) not null,
	created_on timestamp null,
	modified_on timestamp null,
	quantity decimal(7,2) null,
	due_for_payment timestamp null,
	total_amount decimal(12,2) null,
	description varchar(255) null,
	constraint INVNO_UNQ_CODE
		unique (invoice_no)
);