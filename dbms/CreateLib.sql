-- Mitchell Dees
-- Lab 7


-- Get rid of the check when removing all tables, wont matter if I literally clear everything in the database
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS 
	BorrowedBy,
	WrittenBy,
	AuthorPhone,
	PublisherPhone,
	Author,
	Publisher,
	Book,
	Phone,
	Member,
	Library,
	LocatedAt,
	authorAuditLog,
	libraryAuditLog,
	locatedAuditLog;

SET FOREIGN_KEY_CHECKS=1;

-- Entity tables (Just adding library)

CREATE TABLE IF NOT EXISTS Author (
	AuthorID	INT		NOT NULL,
	First_name	VARCHAR(12)	NOT NULL,
	Last_name	VARCHAR(12)	NOT NULL,
	PRIMARY KEY(AuthorID)
);

CREATE TABLE IF NOT EXISTS Publisher (
	PubID		INT		NOT NULL,
	Pub_name	VARCHAR(32)	NOT NULL,
	PRIMARY KEY(PubID)
);

CREATE TABLE IF NOT EXISTS Book (
	ISBN		VARCHAR(16)	NOT NULL,
	Title		VARCHAR(32)	NOT NULL,
	PubID		INT		NOT NULL,
	Year_published	DATE 		NOT NULL,
	PRIMARY KEY(ISBN),
	FOREIGN KEY(PubID) REFERENCES Publisher(PubID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Phone (
	PNumber		VARCHAR(12)		NOT NULL,
	Type		ENUM('c','h','o')	NOT NULL,
	PRIMARY KEY(PNumber)

);

CREATE TABLE IF NOT EXISTS Member (
	MemberID	INT		NOT NULL,
	First_name	VARCHAR(16)	NOT NULL,
	Last_name	VARCHAR(16)	NOT NULL,
	DOB		DATE		Not NULL,
	Gender		ENUM('M','F')	NOT NULL,
	PRIMARY KEY(MemberID)
);

CREATE TABLE IF NOT EXISTS Library (
	Name		VARCHAR(30)	NOT NULL,
	Street		VARCHAR(30)	NOT NULL,
	City		VARCHAR(30)	NOT NULL,
	State		VARCHAR(30)	NOT NULL,
	PRIMARY KEY(Name)
);




-- Relationship tables (LocatedAt and BorrowedBy are the only changed)

CREATE TABLE IF NOT EXISTS LocatedAt (
	ISBN		VARCHAR(16)	NOT NULL,
	Name		VARCHAR(30)	NOT NULL,
	TotalCopies	INT		NOT NULL,
	Shelf		INT		NOT NULL,
	Floor		INT		NOT NULL,
	CopiesNotCheckedOut	INT		NOT NULL,
	PRIMARY KEY(ISBN, Name),
	FOREIGN KEY(ISBN) REFERENCES Book(ISBN) ON DELETE CASCADE,
	FOREIGN KEY(Name) REFERENCES Library(Name) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS BorrowedBy (
	MemberID	INT		NOT NULL,
	ISBN		VARCHAR(16)	NOT NULL,
	Name		VARCHAR(30)	NOT NULL,
	CheckoutDate	DATE		NOT NULL,
	CheckinDate	DATE,
	PRIMARY KEY(MemberID, ISBN, Name, CheckoutDate),
	FOREIGN KEY(MemberID) REFERENCES Member(MemberID) ON DELETE CASCADE,
	FOREIGN KEY(ISBN) REFERENCES Book(ISBN) ON DELETE CASCADE,
	FOREIGN KEY(Name) REFERENCES Library(Name) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS WrittenBy (
	ISBN		VARCHAR(16)	NOT NULL,
	AuthorID	INT		NOT NULL,
	PRIMARY KEY(ISBN, AuthorID),
	FOREIGN KEY(ISBN) REFERENCES Book(ISBN) ON DELETE CASCADE,
	FOREIGN KEY(AuthorID) REFERENCES Author(AuthorID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS AuthorPhone (
	AuthorID	INT		NOT NULL,
	PNumber		VARCHAR(12)	NOT NULL,
	PRIMARY KEY(AuthorID, PNumber),
	FOREIGN KEY(AuthorID) REFERENCES Author(AuthorID) ON DELETE CASCADE,
	FOREIGN KEY(PNumber) REFERENCES Phone(PNumber) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS PublisherPhone (
	PubID		INT		NOT NULL,
	PNumber		VARCHAR(12)	NOT NULL,
	PRIMARY KEY(PubID, PNumber),
	FOREIGN KEY(PubID) REFERENCES Publisher(PubID) ON DELETE CASCADE,
	FOREIGN KEY(PNumber) REFERENCES Phone(PNumber) ON DELETE CASCADE
);

--trigger log table

CREATE TABLE audit (
    audit_id INT          NOT NULL AUTO_INCREMENT,
    action    VARCHAR(100) NOT NULL,
    date_time TIMESTAMP    NOT NULL,
    PRIMARY KEY (audit_id)
);











