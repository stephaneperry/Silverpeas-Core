CREATE TABLE ST_NotifChannel (
	id int NOT NULL ,
	name varchar (20) NOT NULL ,
	description varchar (200) NULL ,
	couldBeAdded char (1) DEFAULT ('Y') NOT NULL ,
	fromAvailable char (1) DEFAULT ('N') NOT NULL ,
	subjectAvailable char (1) DEFAULT ('N') NOT NULL 
)
;
 
CREATE TABLE ST_NotifAddress (
	id int NOT NULL ,
	userId int NOT NULL ,
	notifName varchar (20) NOT NULL ,
	notifChannelId int NOT NULL ,
	address varchar (250) NOT NULL ,
	usage varchar (20) NULL ,
	priority int NOT NULL 
)
;

CREATE TABLE ST_NotifDefaultAddress (
	id int NOT NULL ,
	userId int NOT NULL ,
	notifAddressId int NOT NULL 
)
;

CREATE TABLE ST_NotifPreference (
	id int NOT NULL ,
	notifAddressId int NOT NULL ,
	componentInstanceId int NOT NULL ,
	userId int NOT NULL ,
	messageType int NOT NULL 
)
;

CREATE TABLE ST_NotifSended (
	notifId		int		NOT NULL,
	userId		int		NOT NULL,
	messageType	int		NULL,
	notifDate	char (13)	NOT NULL,
	title		varchar (255)	NULL,
	link		varchar (255)	NULL,
	sessionId	varchar (255)	NULL,
	componentId	varchar (255)	NULL,
	body		int		NULL
);

CREATE TABLE ST_NotifSendedReceiver (
	notifId		int		NOT NULL,
	userId		int		NOT NULL
);