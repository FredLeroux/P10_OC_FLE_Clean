CREATE SCHEMA libraryum
	AUTHORIZATION "admin";



CREATE TABLE libraryum.library_roles (
                id IDENTITY NOT NULL,
                role_type VARCHAR NOT NULL,
                CONSTRAINT library_roles_pk PRIMARY KEY (id)
);


CREATE TABLE libraryum.library_customers (
                id IDENTITY NOT NULL,
                customer_email VARCHAR NOT NULL,
                customer_password VARCHAR NOT NULL,
                customer_enabled BOOLEAN NOT NULL,
                customer_account_non_expired BOOLEAN NOT NULL,
                customer_credentials_non_expired BOOLEAN NOT NULL,
                customer_account_non_locked VARCHAR NOT NULL,
                library_role_fk INTEGER NOT NULL,
                customer_auth_token VARCHAR,
                CONSTRAINT library_customers_pk PRIMARY KEY (id)
);


CREATE TABLE libraryum.library_building (
                id IDENTITY NOT NULL,
                name VARCHAR NOT NULL,
                CONSTRAINT library_building_pk PRIMARY KEY (id)
);


CREATE TABLE libraryum.library_books (
                id IDENTITY NOT NULL,
                kind VARCHAR NOT NULL,
                title VARCHAR NOT NULL,
                author VARCHAR NOT NULL,
                availability BOOLEAN NOT NULL,
                number_of_reservations INTEGER,
                library_building_fk INTEGER NOT NULL,
                CONSTRAINT library_books_pk PRIMARY KEY (id)
);
COMMENT ON COLUMN libraryum.library_books.number_of_reservations IS 'Column added as part of Issue #5 Ticket_1';


CREATE TABLE libraryum.library_book_reservations (
                id IDENTITY NOT NULL,
                notification_date VARCHAR,
                canceled_status BOOLEAN NOT NULL,
                priority INTEGER NOT NULL,
                library_book_fk INTEGER NOT NULL,
                library_customer_fk INTEGER NOT NULL,
                CONSTRAINT library_book_reservations_pk PRIMARY KEY (id)
);


CREATE TABLE libraryum.library_book_loans (
                id IDENTITY NOT NULL,
                return_date VARCHAR NOT NULL,
                postponed BOOLEAN NOT NULL,
                returned BOOLEAN NOT NULL,
                book_fk INTEGER NOT NULL,
                customer_fk INTEGER NOT NULL,
                CONSTRAINT library_book_loans_pk PRIMARY KEY (id)
);


ALTER TABLE libraryum.library_customers ADD CONSTRAINT library_role_customer_fk
FOREIGN KEY (library_role_fk)
REFERENCES libraryum.library_roles (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE libraryum.library_book_loans ADD CONSTRAINT Custommer_Borrowed_fk
FOREIGN KEY (customer_fk)
REFERENCES libraryum.library_customers (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE libraryum.library_book_reservations ADD CONSTRAINT library_customers_library_book_reservation_fk
FOREIGN KEY (library_customer_fk)
REFERENCES libraryum.library_customers (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE libraryum.library_books ADD CONSTRAINT library_building_books_fk
FOREIGN KEY (library_building_fk)
REFERENCES libraryum.library_building (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE libraryum.library_book_loans ADD CONSTRAINT books_Borrowed_fk
FOREIGN KEY (book_fk)
REFERENCES libraryum.library_books (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE libraryum.library_book_reservations ADD CONSTRAINT library_books_library_book_reservation_fk
FOREIGN KEY (library_book_fk)
REFERENCES libraryum.library_books (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

