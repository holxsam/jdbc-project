-- Data Manipulation Language File:
-- We will not be turning this in.
-- It is just for our convenience when we program the jbdc driver in java.
-- 

-- REQUIREMENTS FROM HER SITE: 
-- Create a simple menu to run all of the options below:
-- 1. List all writing groups
-- 2. List all the data for a group specified by the user .
--      This includes all the data for the associated books and publishers.
-- 3. List all publishers
-- 4. List all the data for a pubisher specified by the user.
--      This includes all the data for the associated books and writing groups.
-- 5. List all book titles
-- 6. List all the data for a single book specified by the user.
--      This includes all the data for the associated publisher and writing group.
-- 7. Insert a new book
-- 8. Insert a new publisher and update all book published by one publisher to be published by the new pubisher.
--      This requirement is two separate operations. 
--      The idea is that a new publisher, (xyz) buys out an existing publisher (abc). 
--      After the new publisher is added to the database, all books that are currently published by abc will now be published by xyz.
-- 9. Remove a single book specified by the user

SELECT * from WritingGroup;
SELECT * from Publisher;
SELECT * from Book;

SELECT * from Book natural join WritingGroup;
SELECT * from Book natural join Publisher;
SELECT * from Book natural join Publisher natural join WritingGroup;

SELECT * from Book natural join Publisher where publisheremail='eldoradosun@gmail.com';

SELECT * from Book natural join WritingGroup where subject='Fiction';

SELECT groupname, headwriter from WritingGroup;

-- Examples of sql we need to support in java:

-- 1. List all writing groups:
SELECT groupname from WritingGroup;

-- 2. List all the data for a group specified by the user:
SELECT * from Book natural join Publisher natural join WritingGroup where groupname = 'groupname_input';

-- 3. List all publishers:
SELECT publishername from Publisher;

-- 4. List all the data for a pubisher specified by the user:
SELECT * from Book natural join Publisher natural join WritingGroup where publishername = 'publishername_input';

-- 5. List all book titles:
-- Remember that it is possible to have 2 books with the same booktitle (they were written by different writing groups)
SELECT booktitle from Book;

-- 6. List all the data for a single book specified by the user:
-- requires TWO attributes, groupname and booktitle since that is the pk
SELECT * from Book natural join Publisher natural join WritingGroup where booktitle = 'x' AND groupname = 'y';

-- 7. Insert a new book:
INSERT INTO Book VALUES ('x', 'y', 'z' , 0, 1);

-- 8. Insert a new publisher and update all book published by one publisher to be published by the new pubisher:
-- Requires two operations:
INSERT INTO Publisher VALUES ('publishername_input', 'publisheraddress_input', 'publisherphone_input', 'publisheremail_input');
UPDATE Book SET publishername = 'publishername_input' WHERE publishername = 'publishername_input';

-- 9. Remove a single book specified by the user:
-- requires TWO attributes, groupname and booktitle since that is the pk
DELETE from BOOK where booktitle='' AND groupname='';