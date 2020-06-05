drop table if exists bill;
drop table if exists charge_type;
drop table if exists customer;
drop table if exists reservation;
drop table if exists employee;
drop table if exists extra_charge;
drop table if exists room; 
drop table if exists room_prices; 

CREATE TABLE bill(
    id integer PRIMARY KEY NOT NULL,
    customer_id integer NOT NULL references customer (id),
    room_num integer NOT NULL,
    cc_num integer NOT NULL,
    cc_exp date NOT NULL,
    cc_crc integer NOT NULL,
    curr_date date NOT NULL,
    reservation_id integer NOT NULL references reservation (reservation_id),
    room_charge money NOT NULL,
    extra_charge money NOT NULL,
    total_charge money NOT NULL
);

CREATE TABLE charge_type(
    charge_id integer PRIMARY KEY NOT NULL,
    description text NOT NULL,
    price money NOT NULL,
);

CREATE TABLE customer(
    username text NOT NULL,
    password text NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    email text NOT NULL,
    street_address text NOT NULL,
    city text NOT NULL,
    state text NOT NULL,
    cc_num text NOT NULL,
    cc_crc text NOT NULL,
    cc_exp date NOT NULL,
    id integer PRIMARY KEY auto_increment NOT NULL,
);

CREATE TABLE employee(
    username text NOT NULL,
    password text NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    is_admin boolean NOT NULL DEFAULT false,
    id integer PRIMARY KEY auto_increment NOT_NULL
);

CREATE TABLE extra_charge(
    id integer PRIMARY KEY NOT NULL,
    reservation_id integer NOT NULL references reservation (reservation_id),
    charge_type integer NOT NULL references charge_type (charge_id),
);

CREATE TABLE reservation(
    start_date date NOT NULL,
    end_date date NOT NULL,
    room_number text NOT  references room (room_num),
    customer_id integer NOT NULL references customer (customer_id),
    reservation_id integer PRIMARY KEY auto_increment NOT NULL,
);

CREATE TABLE room(
    room_num text NOT NULL PRIMARY KEY,
    floor integer NOT NULL,
    view text NOT NULL,
    bed_type text NOT NULL,
    base_price money NOT NULL DEFAULT 100,
);

CREATE TABLE room_prices(
    price numeric NOT NULL,
    date date NOT NULL,
    room_num text NOT NULL references room (room_num),
    id integer NOT NULL auto_increment PRIMARY KEY,
);
