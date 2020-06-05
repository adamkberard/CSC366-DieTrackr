/**
 * Team2
 * This file populates data in our database
 */

INSERT INTO bill(id, customer_id, room_num, cc_num, cc_exp, cc_crc, curr_date, reservation_id, room_charge, extra_charge, total_charge)
	VALUES   (1, 1, 111, 123456789, '2020-11-11', 777, '2020-05-04', 1, 100.00, 0.00, 100.00),
            (1, 1, 111, 123456789, '2020-11-11', 777, '2020-05-04', 1, 100.00, 0.00, 100.00),
            (1, 1, 111, 123456789, '2020-11-11', 777, '2020-05-04', 1, 100.00, 0.00, 100.00),
            (1, 1, 111, 123456789, '2020-11-11', 777, '2020-05-04', 1, 100.00, 0.00, 100.00);
            
INSERT INTO charge_type(charge_id, description, price)
	VALUES   (1, 'wifi', 15),
            (2, 'breakfast', 7),
            (3, 'valet', 20);

INSERT INTO extra_charge(id, reservation_id, charge_type)
	VALUES   (1, 1, 1),
            (2, 2, 1),
            (3, 2, 2);

INSERT INTO customer(username, password, first_name, last_name, email, street_address, city, state, cc_num, cc_crc, cc_exp, id)
	VALUES   ('josh', 'password', 'Josh', 'DeBest', 'josh@email.com', '123 Main St', 'SLO', 'CA', '123422', '123', '2020-05-11', 1),
            ('lubo', 'password', 'Lubo', 'Stanchev', 'lubo@email.com', '33 1st St', 'Santa Maria', 'CA', '1234599', '321', '2022-01-01', 2),
            ('mike', 'password', 'Mike', 'DeBest', 'mike@email.com', '67 Foothill Ave', 'San Diego', 'CA', '1234777', '456', '2021-01-01', 3),
            ('lebron', 'password', 'LeBron', 'James', 'lebron@email.com', '123 East Ave', 'Los Angeles', 'CA', '123456', '567', '2023-01-01', 4);

INSERT INTO employee(username, password, first_name, last_name, is_admin, id)
	VALUES   ('admin', 'admin2', 'admin', 'admin', true, 1),
            ('empl1', 'pwd1', 'Adam', 'Berard', false, 2),
            ('empl2', 'pwd2', 'Alex', 'Goodell', false, 3),
            ('empl3', 'pwd3', 'Joshua', 'DaBest', false, 4);

INSERT INTO reservation(start_date, end_date, room_number, customer_id, reservation_id)
	VALUES   ('2020-05-05', '2020-05-06', 101, 1, 1),
            ('2020-05-06', '2020-05-10', 203, 2, 2),
            ('2020-05-05', '2020-05-06', 309, 3, 3),
            ('2020-06-11', '2020-06-18', 511, 4, 4);

INSERT INTO room(room_num, floor, view, bed_type)
	VALUES   (101, 1, 'ocean', 'single king'),
            (102, 1, 'ocean', 'double queen'),
            (103, 1, 'ocean', 'single king'),
            (104, 1, 'ocean', 'double queen'),
            (105, 1, 'ocean', 'single king'),
            (106, 1, 'ocean', 'double queen'),
            (107, 1, 'pool', 'single king'),
            (108, 1, 'pool', 'double queen'),
            (109, 1, 'pool', 'single king'),
            (110, 1, 'pool', 'double queen'),
            (111, 1, 'pool', 'single king'),
            (112, 1, 'pool', 'double queen'),
            
            (201, 1, 'ocean', 'single king'),
            (202, 1, 'ocean', 'double queen'),
            (203, 1, 'ocean', 'single king'),
            (204, 1, 'ocean', 'double queen'),
            (205, 1, 'ocean', 'single king'),
            (206, 1, 'ocean', 'double queen'),
            (207, 1, 'pool', 'single king'),
            (208, 1, 'pool', 'double queen'),
            (209, 1, 'pool', 'single king'),
            (210, 1, 'pool', 'double queen'),
            (211, 1, 'pool', 'single king'),
            (212, 1, 'pool', 'double queen'),

            (301, 1, 'ocean', 'single king'),
            (302, 1, 'ocean', 'double queen'),
            (303, 1, 'ocean', 'single king'),
            (304, 1, 'ocean', 'double queen'),
            (305, 1, 'ocean', 'single king'),
            (306, 1, 'ocean', 'double queen'),
            (307, 1, 'pool', 'single king'),
            (308, 1, 'pool', 'double queen'),
            (309, 1, 'pool', 'single king'),
            (310, 1, 'pool', 'double queen'),
            (311, 1, 'pool', 'single king'),
            (312, 1, 'pool', 'double queen'),

            (401, 1, 'ocean', 'single king'),
            (402, 1, 'ocean', 'double queen'),
            (403, 1, 'ocean', 'single king'),
            (404, 1, 'ocean', 'double queen'),
            (405, 1, 'ocean', 'single king'),
            (406, 1, 'ocean', 'double queen'),
            (407, 1, 'pool', 'single king'),
            (408, 1, 'pool', 'double queen'),
            (409, 1, 'pool', 'single king'),
            (410, 1, 'pool', 'double queen'),
            (411, 1, 'pool', 'single king'),
            (412, 1, 'pool', 'double queen'),

            (501, 1, 'ocean', 'single king'),
            (502, 1, 'ocean', 'double queen'),
            (503, 1, 'ocean', 'single king'),
            (504, 1, 'ocean', 'double queen'),
            (505, 1, 'ocean', 'single king'),
            (506, 1, 'ocean', 'double queen'),
            (507, 1, 'pool', 'single king'),
            (508, 1, 'pool', 'double queen'),
            (509, 1, 'pool', 'single king'),
            (510, 1, 'pool', 'double queen'),
            (511, 1, 'pool', 'single king'),
            (512, 1, 'pool', 'double queen');

INSERT INTO room_prices(price, date, room_num, id)
	VALUES   (250, '2020-05-12', 101, 1),
            (280, '2020-05-18', 501, 1),
            (300, '2020-05-15', 309, 1),
            (350, '2020-05-31', 408, 1);
