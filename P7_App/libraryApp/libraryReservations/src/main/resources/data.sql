INSERT INTO libraryum.library_roles(
	 id,role_type)
	VALUES
	(1,'TYPE_admin'),
	(2,'TYPE_user');
INSERT INTO libraryum.library_customers(
	id,customer_email,customer_password,customer_enabled,customer_account_non_expired,customer_credentials_non_expired,library_role_fk,customer_account_non_locked,customer_auth_token)
	VALUES
	(1,'customer1','pass1',true,true,true,1,true,null),
	(2,'customer2','pass2',true,true,true,1,true,null),
	(3,'customer3','pass3',true,true,true,2,true,null),
	(4,'customer4','pass4',true,true,true,2,true,'token4');

INSERT INTO libraryum.library_building(
	 id, name)
	VALUES (1,'bibliothèque_nord'),
	 (2,'bibliothèque_sud'),
	 (3,'bibliothèque_est'),
	 (4,'bibliothèque_ouest'),
	 (5,'bibliothèque_centre');

INSERT INTO libraryum.library_books(
	id,kind,title,author,availability,library_building_fk,number_of_reservations)
	VALUES
	(1,'A_Kind','A_Book','A_author',true,3,null),
	(2,'A_Kind','A_Book','A_author',true,3,null),
	(3,'A_Kind','A_Book','A_author',true,3,null),
	(4,'A_Kind','A_Book','A_author',true,3,null),
	(5,'A_Kind','A_Book','A_author',true,3,null),
	(6,'A_Kind','A_Book','A_author',true,3,null),
	(7,'A_Kind','A_Book','A_author',true,4,null),
	(8,'A_Kind','A_Book','A_author',true,4,null),
	(9,'A_Kind','A_Book','A_author',true,4,null),
	(10,'A_Kind','A_Book','A_author',false,4,null),
	(11,'A_Kind','A_Book','A_author',true,4,null),
	(12,'A_Kind','A_Book','A_author',true,5,null),
	(13,'A_Kind','B_Book','A_author',true,5,null),
	(14,'A_Kind','B_Book','A_author',true,5,null),
	(15,'A_Kind','B_Book','A_author',true,5,null),
	(16,'A_Kind','B_Book','A_author',true,5,null),
	(17,'B_Kind','C_Book','B_author',true,2,null),
	(18,'B_Kind','C_Book','B_author',true,2,null),
	(19,'B_Kind','C_Book','B_author',true,2,null),
	(20,'B_Kind','C_Book','B_author',true,3,null),
	(21,'B_Kind','C_Book','B_author',true,3,null),
	(22,'B_Kind','C_Book','B_author',true,3,null),
	(23,'B_Kind','C_Book','B_author',true,3,null),
	(24,'B_Kind','C_Book','B_author',true,3,null),
	(25,'B_Kind','C_Book','B_author',true,3,null),
	(26,'B_Kind','C_Book','B_author',true,4,null),
	(27,'B_Kind','C_Book','B_author',true,4,null),
	(28,'B_Kind','C_Book','B_author',true,4,null),
	(29,'B_Kind','C_Book','B_author',true,4,null),
	(30,'B_Kind','C_Book','B_author',true,5,null),
	(31,'B_Kind','C_Book','B_author',true,5,null),
	(32,'B_Kind','C_Book','B_author',true,5,null),
	(33,'B_Kind','C_Book','B_author',true,5,null),
	(34,'A_Kind','D_Book','C_author',true,5,null),
	(35,'A_Kind','D_Book','C_author',true,3,null),
	(36,'A_Kind','D_Book','C_author',true,3,null),
	(37,'A_Kind','D_Book','C_author',true,4,null),
	(38,'A_Kind','D_Book','C_author',true,4,null),
	(39,'A_Kind','D_Book','C_author',true,4,null),
	(40,'A_Kind','D_Book','C_author',true,5,null),
	(41,'A_Kind','D_Book','C_author',true,5,null),
	(42,'A_Kind','D_Book','C_author',true,5,null),
	(43,'A_Kind','A_Book','A_author',true,2,null),
	(44,'A_Kind','A_Book','A_author',true,2,null),
	(45,'A_Kind','A_Book','A_author',true,2,null),
	(46,'A_Kind','A_Book','A_author',false,3,null),
	(47,'B_Kind','C_Book','B_author',true,2,null),
	(48,'B_Kind','C_Book','B_author',false,2,null),
	(49,'A_Kind','B_Book','A_author',false,3,null),
	(50,'B_Kind','C_Book','B_author',true,3,null),
	(51,'A_Kind','B_Book','A_author',false,3,null),
	(52,'B_Kind','C_Book','B_author',false,1,null),
	(53,'B_Kind','C_Book','B_author',true,3,null),
	(54,'B_Kind','C_Book','B_author',false,1,null),
	(55,'B_Kind','C_Book','B_author',false,1,2),
	(56,'A_Kind','A_Book','A_author',true,3,null),
	(57,'A_Kind','B_Book','A_author',false,4,null),
	(58,'A_Kind','A_Book','A_author',false,2,null),
	(59,'A_Kind','B_Book','A_author',false,4,null),
	(60,'A_Kind','B_Book','A_author',false,4,null),
	(61,'A_Kind','A_Book','A_author',true,5,null),
	(62,'A_Kind','A_Book','A_author',true,5,null),
	(63,'A_Kind','A_Book','A_author',true,5,null),
	(64,'A_Kind','B_Book','A_author',false,4,null),
	(65,'A_Kind','A_Book','A_author',false,2,null),
	(66,'A_Kind','A_Book','A_author',false,1,null),
	(67,'A_Kind','A_Book','A_author',false,1,null),
	(68,'A_Kind','B_Book','A_author',true,4,null),
	(69,'A_Kind','D_Book','C_author',false,5,null),
	(70,'A_Kind','D_Book','C_author',false,4,null),
	(71,'B_Kind','C_Book','B_author',false,4,null),
	(72,'A_Kind','B_Book','A_author',false,5,null),
	(73,'A_Kind','D_Book','C_author',false,4,null),
	(74,'A_Kind','A_Book','A_author',false,1,null);

	INSERT INTO libraryum.library_book_loans(
	id, return_date, postponed, returned, book_fk, customer_fk)
	VALUES
	(1, CURRENT_DATE, false, false, 10, 1),
	(2, DATEADD('DAY', -28, CURRENT_DATE), false, false, 48, 1),
	(3, DATEADD('DAY', 10, CURRENT_DATE), false, false, 66, 2),
	(4, DATEADD('DAY', 1, CURRENT_DATE), false, false, 67, 3),
	(5, DATEADD('DAY', 5, CURRENT_DATE), false, false, 74, 4),
	(6, DATEADD('DAY', 5, CURRENT_DATE), false, false, 51, 4),
	(7, DATEADD('DAY', 5, CURRENT_DATE), false, false, 49, 3);



	INSERT INTO libraryum.library_book_reservations(
	id, notification_date, canceled_status, priority, library_book_fk, library_customer_fk)
	VALUES
	(1, null, false, 1, 5, 1),
	(2, DATEADD('DAY', -5, CURRENT_DATE), false, 1, 40, 2),
	(3,null, false, 2, 5, 2),
	(4,null, false, 1, 6, 3),
	(5,null, false, 1, 37, 3),
	(6,null, false, 1, 48, 4);


