DROP TABLE Books; 
DROP TABLE WritingGroups; 
DROP TABLE Publishers; 

CREATE TABLE WritingGroups
(
    GroupName           VARCHAR(30)     NOT NULL,
    HeadWriter          VARCHAR(40)     NOT NULL,
    YearFormed          INT             NOT NULL,
    Subject             VARCHAR(20)     NOT NULL,
    CONSTRAINT  writingGroup_PK PRIMARY KEY(groupName) 
);

CREATE TABLE Publishers
(
    PublisherName       VARCHAR(40)     NOT NULL,
    PublisherAddress    VARCHAR(50)     NOT NULL,
    PublisherPhone      VARCHAR(12)     NOT NULL,
    PublisherEmail      VARCHAR(40)     NOT NULL,
    CONSTRAINT  publishers_PK PRIMARY KEY (PublisherName) 
);

CREATE TABLE Books
(
    BookTitle           VARCHAR(40)     NOT NULL,
    GroupName           VARCHAR(30)     NOT NULL,
    PublisherName       VARCHAR(40)     NOT NULL,
    YearPublished       INT             NOT NULL,
    NumberPages         INT             NOT NULL
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
('JK Authors',  'J K Rowling',      1998,   'Fantasy'),
('Wiki Group',  'Vickie Peedea',    1995,   'information'),
('Wimpy Greg',  'Rodrick Rules',    2000,   'Comedy'),
('Old Bookies', 'Dr Wilcox',        1995,   'Fiction'),
('Derpy',       'KidJRod',          1940,   'Fiction'),
('Fellowship',  'J.R.R Tolkien',    1934,   'Fiction');


INSERT INTO Publishers(PublisherName, PublisherAddress, PublisherPhone, PublisherEmail)
values
('Amulet',              '2010 Wimpy Ave',       '562-892-1111',     'Oldbook@gmail.com'),
('Wikipedia',           '100 BestWebsite Ave',  '951-420-4242',     'KnowItAll@Yahoo.net'),
('Bloomsbury',          '4221 Random Drive',    '425-100-1010',     'HagridRules@msn.com'),
('Literature',          '35 Learning Ave',      '213-345-6543',     'Literature@cool.com'),
('Salinger Books',      '1600 Salinger Blvd',   '800-588-2300',     'salinger@yahoo.com'),
('Blue Creek Novels',   '436 Blue Creek Rd',    '911-119-1919',     'bluecreek@gmail.net'),
('One Ring',            '1738 Hobbit Hole',     '714-024-9696',     'manyrings@sauron.org');


INSERT INTO Books(BookTitle, GroupName, PublisherName, YearPublished, NumberPages)
values
('Harry Potter And The Sorcerers Stone',    'JK Authors',   'Bloomsbury',           1998,   309),
('Harry Potter And The Chamber of Secrets', 'JK Authors',   'Bloomsbury',           1999,   379),
('Harry Potter And The Prisoner of Azkaban','JK Authors',   'Bloomsbury',           2000,   399),
('Harry Potter And The Goblet of Fire',     'JK Authors',   'Bloomsbury',           2001,   429),
('Harry Potter And The Half-Blood Prince',  'JK Authors',   'Amulet',               2002,   449),
('Harry Potter And The Deathly Hallows',    'JK Authors',   'Amulet',               2003,   479),
('Front Page',                              'Wiki Group',   'Wikipedia',            1999,   096),
('Dictionary',                              'Wiki Group',   'Wikipedia',            1999,   900),
('Diary of a Wimpy Kid: Wrecking Ball',     'Wimpy Greg',   'Amulet',               2007,   109),
('Diary of a Wimpy Kid: Last Straw',        'Wimpy Greg',   'Amulet',               2008,   109),
('Diary of a Wimpy Kid: Dog Days',          'Wimpy Greg',   'Amulet',               2009,   109),
('The Great Gatsby',                        'Old Bookies',  'Literature',           1995,   180),
('The Catcher in the Rye',                  'Old Bookies',  'Salinger Books',       1951,   277),
('Nine Stories',                            'Derpy',        'Salinger Books',       1953,   277),
('To Kill a Mockingbird',                   'Derpy',        'Blue Creek Novels',    1960,   323),
('Lord of the Flies',                       'Old Bookies',  'Salinger Books',       1990,   102),
('LOTR: Fellowship of the Ring',            'Fellowship',   'One Ring',             1954,   313),
('The Hobbit',                              'Fellowship',   'One Ring',             1940,   500),
('The Lord of the Rings: Two Towers',       'Fellowship',   'One Ring',             1954,   643),
('LOTR: Return of the King ',               'Fellowship',   'One Ring',             1955,   423);

