

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_book_loans" ALTER COLUMN "postponed" TYPE BOOLEAN, ALTER COLUMN "postponed" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_book_loans" ALTER COLUMN "return_date" TYPE VARCHAR, ALTER COLUMN "return_date" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_book_loans" ALTER COLUMN "returned" TYPE BOOLEAN, ALTER COLUMN "returned" SET NOT NULL;

CREATE SEQUENCE "LIBRARY"."libraryum"."library_book_reservations_id_seq";

CREATE TABLE "LIBRARY"."libraryum"."library_book_reservations" (
                "id" INTEGER NOT NULL DEFAULT nextval('"LIBRARY"."libraryum"."library_book_reservations_id_seq"'),
                "notification_date" VARCHAR ,
                "canceled_status" BOOLEAN NOT NULL,
                "priority" INTEGER NOT NULL,
                "library_book_fk" INTEGER NOT NULL,
                "library_customer_fk" INTEGER NOT NULL,
                CONSTRAINT "library_book_reservations_pk" PRIMARY KEY ("id")
);


ALTER SEQUENCE "LIBRARY"."libraryum"."library_book_reservations_id_seq" OWNED BY "LIBRARY"."libraryum"."library_book_reservations"."id";

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_books" ALTER COLUMN "author" TYPE VARCHAR, ALTER COLUMN "author" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_books" ALTER COLUMN "availability" TYPE BOOLEAN, ALTER COLUMN "availability" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_books" ALTER COLUMN "kind" TYPE VARCHAR, ALTER COLUMN "kind" SET NOT NULL;

ALTER TABLE "LIBRARY"."libraryum"."library_books" ADD COLUMN "number_of_reservations" INTEGER;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_books" ALTER COLUMN "title" TYPE VARCHAR, ALTER COLUMN "title" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_building" ALTER COLUMN "name" TYPE VARCHAR, ALTER COLUMN "name" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_customers" ALTER COLUMN "customer_account_non_expired" TYPE BOOLEAN, ALTER COLUMN "customer_account_non_expired" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_customers" ALTER COLUMN "customer_account_non_locked" TYPE VARCHAR, ALTER COLUMN "customer_account_non_locked" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_customers" ALTER COLUMN "customer_auth_token" TYPE VARCHAR, ALTER COLUMN "customer_auth_token" DROP NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_customers" ALTER COLUMN "customer_credentials_non_expired" TYPE BOOLEAN, ALTER COLUMN "customer_credentials_non_expired" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_customers" ALTER COLUMN "customer_email" TYPE VARCHAR, ALTER COLUMN "customer_email" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_customers" ALTER COLUMN "customer_enabled" TYPE BOOLEAN, ALTER COLUMN "customer_enabled" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_customers" ALTER COLUMN "customer_password" TYPE VARCHAR, ALTER COLUMN "customer_password" SET NOT NULL;

ALTER TABLE ONLY "LIBRARY"."libraryum"."library_roles" ALTER COLUMN "role_type" TYPE VARCHAR, ALTER COLUMN "role_type" SET NOT NULL;

ALTER TABLE "LIBRARY"."libraryum"."library_book_reservations" ADD CONSTRAINT "library_books_library_book_reservation_fk"
FOREIGN KEY ("library_book_fk")
REFERENCES "LIBRARY"."libraryum"."library_books" ("id")
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE "LIBRARY"."libraryum"."library_book_reservations" ADD CONSTRAINT "library_customers_library_book_reservation_fk"
FOREIGN KEY ("library_customer_fk")
REFERENCES "LIBRARY"."libraryum"."library_customers" ("id")
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
