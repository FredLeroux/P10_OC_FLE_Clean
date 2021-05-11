INSERT INTO libraryum.library_roles(
	 id,role_type)
	VALUES
	(1,'TYPE_admin'),
	(2,'TYPE_user');
INSERT INTO libraryum.library_customers(
	id,customer_email,customer_password,customer_enabled,customer_account_non_expired,customer_credentials_non_expired,library_role_fk,customer_account_non_locked,customer_auth_token)
	VALUES
	(1,'customer1','$2a$10$J7U9P4OvtyhusycyxfjXTusjLyNdT20c/BvqpGzm7/Fm7xWxHRXpa',true,true,true,1,true,null),
	(2,'customer2','pass2',true,true,true,1,true,null),
	(3,'customer3','pass3',true,true,true,2,true,null),
	(4,'customer4','pass4',true,true,true,2,true,'token4');
