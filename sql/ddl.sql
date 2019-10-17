-- ADD YOUR SQL CODE HERE MARTY


CREATE TABLE WritingGroups
(
    GroupName           VARCHAR(50)     NOT NULL,
    HeadWriter          VARCHAR(50),
    YearFormed          VARCHAR(4),
    Subject             VARCHAR(50),
    CONSTRAINT  writingGroup_PK PRIMARY KEY(groupName) 
);

CREATE TABLE Publishers
(
    PublisherName       VARCHAR(50)     NOT NULL,
    PublisherAddress    VARCHAR(60),
    PublisherPhone      VARCHAR(12),
    PublisherEmail      VARCHAR(100),
    CONSTRAINT  publishers_PK PRIMARY KEY (PublisherName) 
);

CREATE TABLE Books
(
    BookTitle           VARCHAR(50)     NOT NULL,
    GroupName           VARCHAR(50)     NOT NULL,
    PublisherName       VARCHAR(50)     NOT NULL,
    YearPublished       VARCHAR(4),
    NumberPages         INT
);

ALTER TABLE Books
    ADD CONSTRAINT Books_PK PRIMARY KEY (BookTitle,GroupName);

ALTER TABLE Books
    ADD CONSTRAINT Books_FK FOREIGN KEY (GroupName) REFERENCES WritingGroups (GroupName);

ALTER TABLE Books
    ADD CONSTRAINT Books_FK1 FOREIGN KEY (PublisherName) REFERENCES Publishers (PublisherName);


--------------------------------------------------------------------------------


INSERT INTO WritingGroups(GroupName, HeadWriter, YearFormed, Subject)
values
('JK Authors','J K Rowling','1998','Fantasy');


INSERT INTO Publishers(PublisherName, PublisherAddress, PublisherPhone, PublisherEmail)
values
('Amulet','2010 Wimpy Ave','562-892-1111','Oldbook@gmail.com'),
('Wikipedia','100 BestWebsite Ave','951-420-4242','KnowItAll@Yahoo.net'),
('Bloomsbury','4221 Random Drive','425-100-1010','HagridRules@msn.com'),
('Bloossmsbury','4221 RD Drive','425-452-1010','re@msn.com');


INSERT INTO Books(BookTitle, GroupName, PublisherName, YearPublished, NumberPages)
values
('Harry Potter And The Sorcerers Stone','JK Authors','Bloomsbury','1998',309),
('Diary of a Wimpy Kid', 'Jeff Kinney', 'Amulet', '2007', 109);



