--
-- PostgreSQL database dump
--

-- Dumped from database version 11.3
-- Dumped by pg_dump version 12.3

-- Started on 2020-12-19 10:23:23

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 59937)
-- Name: libraryum; Type: SCHEMA; Schema: -; Owner: LIBRARY_ADMIN
--

CREATE SCHEMA libraryum;


ALTER SCHEMA libraryum OWNER TO "LIBRARY_ADMIN";

SET default_tablespace = '';

--
-- TOC entry 205 (class 1259 OID 68216)
-- Name: library_book_loans; Type: TABLE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE TABLE libraryum.library_book_loans (
    id integer NOT NULL,
    return_date character varying NOT NULL,
    postponed boolean NOT NULL,
    returned boolean NOT NULL,
    book_fk integer NOT NULL,
    customer_fk integer NOT NULL
);


ALTER TABLE libraryum.library_book_loans OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 204 (class 1259 OID 68214)
-- Name: library_book_loans_id_seq; Type: SEQUENCE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE SEQUENCE libraryum.library_book_loans_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE libraryum.library_book_loans_id_seq OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 2869 (class 0 OID 0)
-- Dependencies: 204
-- Name: library_book_loans_id_seq; Type: SEQUENCE OWNED BY; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER SEQUENCE libraryum.library_book_loans_id_seq OWNED BY libraryum.library_book_loans.id;


--
-- TOC entry 199 (class 1259 OID 60099)
-- Name: library_books; Type: TABLE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE TABLE libraryum.library_books (
    id integer NOT NULL,
    kind character varying NOT NULL,
    title character varying NOT NULL,
    author character varying NOT NULL,
    availability boolean NOT NULL,
    library_building_fk integer NOT NULL
);


ALTER TABLE libraryum.library_books OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 198 (class 1259 OID 60097)
-- Name: library_books_id_seq; Type: SEQUENCE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE SEQUENCE libraryum.library_books_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE libraryum.library_books_id_seq OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 2870 (class 0 OID 0)
-- Dependencies: 198
-- Name: library_books_id_seq; Type: SEQUENCE OWNED BY; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER SEQUENCE libraryum.library_books_id_seq OWNED BY libraryum.library_books.id;


--
-- TOC entry 197 (class 1259 OID 59962)
-- Name: library_building; Type: TABLE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE TABLE libraryum.library_building (
    id integer NOT NULL,
    name character varying NOT NULL
);


ALTER TABLE libraryum.library_building OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 196 (class 1259 OID 59960)
-- Name: library_building_id_seq; Type: SEQUENCE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE SEQUENCE libraryum.library_building_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE libraryum.library_building_id_seq OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 2871 (class 0 OID 0)
-- Dependencies: 196
-- Name: library_building_id_seq; Type: SEQUENCE OWNED BY; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER SEQUENCE libraryum.library_building_id_seq OWNED BY libraryum.library_building.id;


--
-- TOC entry 203 (class 1259 OID 68146)
-- Name: library_customers; Type: TABLE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE TABLE libraryum.library_customers (
    id integer NOT NULL,
    customer_email character varying NOT NULL,
    customer_password character varying NOT NULL,
    customer_enabled boolean NOT NULL,
    customer_account_non_expired boolean NOT NULL,
    customer_credentials_non_expired boolean NOT NULL,
    library_role_fk integer NOT NULL,
    customer_account_non_locked character varying NOT NULL,
    customer_auth_token character varying
);


ALTER TABLE libraryum.library_customers OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 202 (class 1259 OID 68144)
-- Name: library_customers_id_seq; Type: SEQUENCE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE SEQUENCE libraryum.library_customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE libraryum.library_customers_id_seq OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 2872 (class 0 OID 0)
-- Dependencies: 202
-- Name: library_customers_id_seq; Type: SEQUENCE OWNED BY; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER SEQUENCE libraryum.library_customers_id_seq OWNED BY libraryum.library_customers.id;


--
-- TOC entry 201 (class 1259 OID 60202)
-- Name: library_roles; Type: TABLE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE TABLE libraryum.library_roles (
    id integer NOT NULL,
    role_type character varying NOT NULL
);


ALTER TABLE libraryum.library_roles OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 200 (class 1259 OID 60200)
-- Name: library_roles_id_seq; Type: SEQUENCE; Schema: libraryum; Owner: LIBRARY_ADMIN
--

CREATE SEQUENCE libraryum.library_roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE libraryum.library_roles_id_seq OWNER TO "LIBRARY_ADMIN";

--
-- TOC entry 2873 (class 0 OID 0)
-- Dependencies: 200
-- Name: library_roles_id_seq; Type: SEQUENCE OWNED BY; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER SEQUENCE libraryum.library_roles_id_seq OWNED BY libraryum.library_roles.id;


--
-- TOC entry 2718 (class 2604 OID 68219)
-- Name: library_book_loans id; Type: DEFAULT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_book_loans ALTER COLUMN id SET DEFAULT nextval('libraryum.library_book_loans_id_seq'::regclass);


--
-- TOC entry 2715 (class 2604 OID 60102)
-- Name: library_books id; Type: DEFAULT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_books ALTER COLUMN id SET DEFAULT nextval('libraryum.library_books_id_seq'::regclass);


--
-- TOC entry 2714 (class 2604 OID 59965)
-- Name: library_building id; Type: DEFAULT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_building ALTER COLUMN id SET DEFAULT nextval('libraryum.library_building_id_seq'::regclass);


--
-- TOC entry 2717 (class 2604 OID 68149)
-- Name: library_customers id; Type: DEFAULT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_customers ALTER COLUMN id SET DEFAULT nextval('libraryum.library_customers_id_seq'::regclass);


--
-- TOC entry 2716 (class 2604 OID 60205)
-- Name: library_roles id; Type: DEFAULT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_roles ALTER COLUMN id SET DEFAULT nextval('libraryum.library_roles_id_seq'::regclass);


--
-- TOC entry 2863 (class 0 OID 68216)
-- Dependencies: 205
-- Data for Name: library_book_loans; Type: TABLE DATA; Schema: libraryum; Owner: LIBRARY_ADMIN
--

COPY libraryum.library_book_loans (id, return_date, postponed, returned, book_fk, customer_fk) FROM stdin;
4	2020-12-01	f	f	68	2
1	2020-12-30	t	f	10	2
2	2021-01-05	f	f	30	2
3	2020-12-01	t	f	31	2
5	2020-12-21	f	f	51	2
\.


--
-- TOC entry 2857 (class 0 OID 60099)
-- Dependencies: 199
-- Data for Name: library_books; Type: TABLE DATA; Schema: libraryum; Owner: LIBRARY_ADMIN
--

COPY libraryum.library_books (id, kind, title, author, availability, library_building_fk) FROM stdin;
1	A_Kind	A_Book	A_author	t	1
2	A_Kind	A_Book	A_author	t	1
3	A_Kind	A_Book	A_author	t	1
4	A_Kind	A_Book	A_author	t	2
5	A_Kind	A_Book	A_author	t	2
6	A_Kind	A_Book	A_author	t	2
7	A_Kind	A_Book	A_author	t	2
8	A_Kind	A_Book	A_author	t	2
9	A_Kind	A_Book	A_author	t	3
11	A_Kind	A_Book	A_author	t	3
12	A_Kind	A_Book	A_author	t	3
13	A_Kind	A_Book	A_author	t	3
14	A_Kind	A_Book	A_author	t	3
15	A_Kind	A_Book	A_author	t	3
16	A_Kind	A_Book	A_author	t	3
17	A_Kind	A_Book	A_author	t	4
18	A_Kind	A_Book	A_author	t	4
19	A_Kind	A_Book	A_author	t	4
20	A_Kind	A_Book	A_author	t	4
21	A_Kind	A_Book	A_author	t	4
22	A_Kind	A_Book	A_author	t	5
23	A_Kind	A_Book	A_author	t	5
24	A_Kind	A_Book	A_author	t	5
25	A_Kind	A_Book	A_author	t	5
26	A_Kind	B_Book	A_author	t	5
27	A_Kind	B_Book	A_author	t	3
29	A_Kind	B_Book	A_author	t	4
30	A_Kind	B_Book	A_author	t	4
31	A_Kind	B_Book	A_author	t	4
32	A_Kind	B_Book	A_author	t	4
33	A_Kind	B_Book	A_author	t	4
34	A_Kind	B_Book	A_author	t	5
35	A_Kind	B_Book	A_author	t	5
36	A_Kind	B_Book	A_author	t	5
37	A_Kind	B_Book	A_author	t	5
38	B_Kind	C_Book	B_author	t	1
39	B_Kind	C_Book	B_author	t	1
40	B_Kind	C_Book	B_author	t	1
41	B_Kind	C_Book	B_author	t	2
44	B_Kind	C_Book	B_author	t	2
45	B_Kind	C_Book	B_author	t	2
46	B_Kind	C_Book	B_author	t	3
47	B_Kind	C_Book	B_author	t	3
48	B_Kind	C_Book	B_author	t	3
49	B_Kind	C_Book	B_author	t	3
50	B_Kind	C_Book	B_author	t	3
51	B_Kind	C_Book	B_author	t	3
52	B_Kind	C_Book	B_author	t	3
53	B_Kind	C_Book	B_author	t	3
54	B_Kind	C_Book	B_author	t	4
55	B_Kind	C_Book	B_author	t	4
56	B_Kind	C_Book	B_author	t	4
57	B_Kind	C_Book	B_author	t	4
58	B_Kind	C_Book	B_author	t	4
59	B_Kind	C_Book	B_author	t	5
60	B_Kind	C_Book	B_author	t	5
61	B_Kind	C_Book	B_author	t	5
62	B_Kind	C_Book	B_author	t	5
63	A_Kind	D_Book	C_author	t	5
64	A_Kind	D_Book	C_author	t	3
65	A_Kind	D_Book	C_author	t	3
66	A_Kind	D_Book	C_author	t	4
67	A_Kind	D_Book	C_author	t	4
68	A_Kind	D_Book	C_author	t	4
69	A_Kind	D_Book	C_author	t	4
70	A_Kind	D_Book	C_author	t	4
71	A_Kind	D_Book	C_author	t	5
72	A_Kind	D_Book	C_author	t	5
73	A_Kind	D_Book	C_author	t	5
74	A_Kind	D_Book	C_author	t	5
10	A_Kind	A_Book	A_author	f	3
28	A_Kind	B_Book	A_author	f	3
42	B_Kind	C_Book	B_author	f	2
43	B_Kind	C_Book	B_author	f	2
\.


--
-- TOC entry 2855 (class 0 OID 59962)
-- Dependencies: 197
-- Data for Name: library_building; Type: TABLE DATA; Schema: libraryum; Owner: LIBRARY_ADMIN
--

COPY libraryum.library_building (id, name) FROM stdin;
1	bibliothèque_nord
2	bibliothèque_sud
3	bibliothèque_est
4	bibliothèque_ouest
5	bibliothèque_centre
\.


--
-- TOC entry 2861 (class 0 OID 68146)
-- Dependencies: 203
-- Data for Name: library_customers; Type: TABLE DATA; Schema: libraryum; Owner: LIBRARY_ADMIN
--

COPY libraryum.library_customers (id, customer_email, customer_password, customer_enabled, customer_account_non_expired, customer_credentials_non_expired, library_role_fk, customer_account_non_locked, customer_auth_token) FROM stdin;
2	mail	$2b$12$63.SGPm6V9mibJ2LjWJGCu1hllDtFuU.Kn9KfrgtCaXrIkUj9S6VW	t	t	t	1	true	R@6hw915i_1ABVSC6CCCC$$KKKn7KD0@o3?w5uHCC*@!iBpDE*ffsd60w?27
\.


--
-- TOC entry 2859 (class 0 OID 60202)
-- Dependencies: 201
-- Data for Name: library_roles; Type: TABLE DATA; Schema: libraryum; Owner: LIBRARY_ADMIN
--

COPY libraryum.library_roles (id, role_type) FROM stdin;
1	TYPE_admin
2	TYPE_user
\.


--
-- TOC entry 2874 (class 0 OID 0)
-- Dependencies: 204
-- Name: library_book_loans_id_seq; Type: SEQUENCE SET; Schema: libraryum; Owner: LIBRARY_ADMIN
--

SELECT pg_catalog.setval('libraryum.library_book_loans_id_seq', 2, true);


--
-- TOC entry 2875 (class 0 OID 0)
-- Dependencies: 198
-- Name: library_books_id_seq; Type: SEQUENCE SET; Schema: libraryum; Owner: LIBRARY_ADMIN
--

SELECT pg_catalog.setval('libraryum.library_books_id_seq', 74, true);


--
-- TOC entry 2876 (class 0 OID 0)
-- Dependencies: 196
-- Name: library_building_id_seq; Type: SEQUENCE SET; Schema: libraryum; Owner: LIBRARY_ADMIN
--

SELECT pg_catalog.setval('libraryum.library_building_id_seq', 5, true);


--
-- TOC entry 2877 (class 0 OID 0)
-- Dependencies: 202
-- Name: library_customers_id_seq; Type: SEQUENCE SET; Schema: libraryum; Owner: LIBRARY_ADMIN
--

SELECT pg_catalog.setval('libraryum.library_customers_id_seq', 2, true);


--
-- TOC entry 2878 (class 0 OID 0)
-- Dependencies: 200
-- Name: library_roles_id_seq; Type: SEQUENCE SET; Schema: libraryum; Owner: LIBRARY_ADMIN
--

SELECT pg_catalog.setval('libraryum.library_roles_id_seq', 2, true);


--
-- TOC entry 2728 (class 2606 OID 68224)
-- Name: library_book_loans library_book_loans_pk; Type: CONSTRAINT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_book_loans
    ADD CONSTRAINT library_book_loans_pk PRIMARY KEY (id);


--
-- TOC entry 2722 (class 2606 OID 60107)
-- Name: library_books library_books_pk; Type: CONSTRAINT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_books
    ADD CONSTRAINT library_books_pk PRIMARY KEY (id);


--
-- TOC entry 2720 (class 2606 OID 59970)
-- Name: library_building library_building_pk; Type: CONSTRAINT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_building
    ADD CONSTRAINT library_building_pk PRIMARY KEY (id);


--
-- TOC entry 2726 (class 2606 OID 68154)
-- Name: library_customers library_customers_pk; Type: CONSTRAINT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_customers
    ADD CONSTRAINT library_customers_pk PRIMARY KEY (id);


--
-- TOC entry 2724 (class 2606 OID 60210)
-- Name: library_roles library_roles_pk; Type: CONSTRAINT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_roles
    ADD CONSTRAINT library_roles_pk PRIMARY KEY (id);


--
-- TOC entry 2731 (class 2606 OID 68225)
-- Name: library_book_loans books_borrowed_fk; Type: FK CONSTRAINT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_book_loans
    ADD CONSTRAINT books_borrowed_fk FOREIGN KEY (book_fk) REFERENCES libraryum.library_books(id);


--
-- TOC entry 2732 (class 2606 OID 68230)
-- Name: library_book_loans custommer_borrowed_fk; Type: FK CONSTRAINT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_book_loans
    ADD CONSTRAINT custommer_borrowed_fk FOREIGN KEY (customer_fk) REFERENCES libraryum.library_customers(id);


--
-- TOC entry 2729 (class 2606 OID 60135)
-- Name: library_books library_building_books_fk; Type: FK CONSTRAINT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_books
    ADD CONSTRAINT library_building_books_fk FOREIGN KEY (library_building_fk) REFERENCES libraryum.library_building(id);


--
-- TOC entry 2730 (class 2606 OID 68160)
-- Name: library_customers library_role_customer_fk; Type: FK CONSTRAINT; Schema: libraryum; Owner: LIBRARY_ADMIN
--

ALTER TABLE ONLY libraryum.library_customers
    ADD CONSTRAINT library_role_customer_fk FOREIGN KEY (library_role_fk) REFERENCES libraryum.library_roles(id);


-- Completed on 2020-12-19 10:23:24

--
-- PostgreSQL database dump complete
--

