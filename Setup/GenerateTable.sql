CREATE TABLE users (
	uname VARCHAR2(30) PRIMARY KEY,
	pass VARCHAR2(30) NOT NULL,
	fullname VARCHAR2(30) NOT NULL,
	phone VARCHAR2(10),
	email VARCHAR2(30) DEFAULT NULL
);

CREATE TABLE parklot (
	lotno NUMBER PRIMARY KEY,
	isbooked NUMBER(1) DEFAULT 0
);

CREATE TABLE booking (
	bookingno VARCHAR2(20) PRIMARY KEY,
	uname VARCHAR2(30) REFERENCES users(uname),
	lotno NUMBER REFERENCES parklot(lotno),
	carno VARCHAR2(30),
	starttime TIMESTAMP DEFAULT SYSDATE,
	endtime TIMESTAMP,
	cost NUMBER
);

/*
INSERT ALL
	INTO users VALUES ('ak289', 	'ak289pass', 	'Ayush Kumar', 		'8920367342',		 	'ayushkumar@gmail.com')
	INTO users VALUES ('ys978', 	'ys978pass', 	'Yash Sankhla', 		'1234567890',	 	'yashsankhla@gmail.com')
	INTO users VALUES ('mv102', 	'mv102pass', 	'Manan Verma', 		'0987654321',	 		'mananverma@gmail.com')
	INTO users VALUES ('ds712', 	'ds712pass', 	'Divyansh Sharma', '0147852369',	 	'divyashsharma@gmail.com')
SELECT * FROM DUAL;
*/

EXIT;