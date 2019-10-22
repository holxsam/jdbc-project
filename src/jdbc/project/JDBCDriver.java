/**
 * Project: JDBC
 * Authors: 
 *      Mohammad-Murtuza Bharoocha
 *      Josue Rodriguez
 *      Sam Alhaqab
 * Class: CECS 323
 */
package jdbc.project;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class JDBCDriver {
    // LINE_LENGTH controls how many '-' are printed
    static final int LINE_LENGTH = 100;
    
    // ATTRIBUTE NAMES:
    static final String ATTR_GROUPNAME = "GroupName";
    static final String ATTR_HEADWRITER = "HeadWriter";
    static final String ATTR_YEARFORMED = "YearFormed";
    static final String ATTR_SUBJECT = "Subject";
    static final String ATTR_PUBLISHERNAME = "PublisherName";
    static final String ATTR_PUBLISHERADDRESS = "PublisherAddress";
    static final String ATTR_PUBLISHERPHONE = "PublisherPhone";
    static final String ATTR_PUBLISHEREMAIL = "PublisherEmail";
    static final String ATTR_BOOKTITLE = "BookTitle";
    static final String ATTR_YEARPUBLISHED = "YearPublished";
    static final String ATTR_NUMBERPAGES = "NumberPages";
    
    // MINIMUM LENGTHS FOR EACH ATTRIBUTE COLUMN (used for formatting the table output):
    static final int MIN_LEN_GROUPNAME = 9;
    static final int MIN_LEN_HEADWRITER = 10;
    static final int MIN_LEN_YEARFORMED = 10;
    static final int MIN_LEN_SUBJECT = 7;
    static final int MIN_LEN_PUBLISHERNAME = 13;
    static final int MIN_LEN_PUBLISHERADDRESS = 16;
    static final int MIN_LEN_PUBLISHERPHONE = 14;
    static final int MIN_LEN_PUBLISHEREMAIL = 14;
    static final int MIN_LEN_BOOKTITLE = 9;
    static final int MIN_LEN_YEARPUBLISHED = 13;
    static final int MIN_LEN_NUMBERPAGES = 11;

    // scanner variable used for user input:
    static Scanner in = new Scanner(System.in);
    
    // Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;
    //This is the specification for the printout that I'm doing:
    //each % denotes the start of a new field.
    //The - denotes left justification.
    //The number indicates how wide to make the field.
    //The "s" denotes that it's a string.  All of our output in this test are
    //strings, but that won't always be the case.
    static final String displayFormat="%-5s%-15s%-15s%-15s\n";
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";

    /**
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    /**
     * Prints out a line of dashes (-)
     * @param count the number of times that a dash (-) will be printed
     */
    public static void line(int count){
        for (int i = 0; i < count; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
    
    /**
     * prints a line of dashes (-) based on the format string that is used with printf()
     * @param format the String that specificies the format
     */
    public static void lineFormat(String format) {
        int totalLength = 0;
        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == (int)'-'){
                String subLength = "";
                while(format.charAt(i) != (int)'s'){
                    subLength += format.charAt(i);
                    i++;
                }
                totalLength += Math.abs(Integer.parseInt(subLength));
            }
        }
        line(totalLength);
    }
        
    /**
     * Prompts the user to press [ENTER]. This function basically gives the user
     * time to read the results of their action.
     */
    public static void displayContinueMessage(){
        System.out.print(">>> Please press [ENTER] to continue back to main menu: ");
        in.nextLine();
    }
    
    /**
     * Prints a menu for the user.
     */
    public static void displayMenu(){
        line(LINE_LENGTH);
        System.out.println("    MAIN MENU");
        line(LINE_LENGTH);
        System.out.println(" 0. Exit");
        System.out.println(" 1. List all writing groups");
        System.out.println(" 2. List all the data for a group specified by the user");
        System.out.println(" 3. List all publishers");
        System.out.println(" 4. List all the data for a pubisher specified by the user");
        System.out.println(" 5. List all book titles");
        System.out.println(" 6. List all the data for a single book specified by the user");
        System.out.println(" 7. Insert a new book");
        System.out.println(" 8. Insert a new publisher and update all book published by one publisher to be published by the new pubisher");
        System.out.println(" 9. Remove a single book specified by the user");
        line(LINE_LENGTH);
    }
    
    /**
     * getTableFormatString goes through the ResultSet rs in order to determine the maximum length a column should be
     * @param rs - the result set
     * @param attributeList - an arraylist of the attribute or columns you want to print
     */
    public static String getTableFormatString(ResultSet rs, ArrayList attributeList) throws SQLException{
        Map<String, Integer> maxFormatLengthMap = new HashMap<>();
        for (int i = 0; i < attributeList.size(); i++) {
            String attribute = (String) attributeList.get(i);
            int minLength = 0;
            switch (attribute) {
                case ATTR_GROUPNAME:
                    minLength = MIN_LEN_GROUPNAME;
                    break;
                case ATTR_HEADWRITER:
                    minLength = MIN_LEN_HEADWRITER;
                    break;                    
                case ATTR_YEARFORMED:
                    minLength = MIN_LEN_YEARFORMED;
                    break;                    
                case ATTR_SUBJECT:
                    minLength = MIN_LEN_SUBJECT;
                    break;                    
                case ATTR_PUBLISHERNAME:
                    minLength = MIN_LEN_PUBLISHERNAME;
                    break;                    
                case ATTR_PUBLISHERADDRESS:
                    minLength = MIN_LEN_PUBLISHERADDRESS;
                    break;
                case ATTR_PUBLISHERPHONE:
                    minLength = MIN_LEN_PUBLISHERPHONE;
                    break;                    
                case ATTR_PUBLISHEREMAIL:
                    minLength = MIN_LEN_PUBLISHEREMAIL;
                    break;                    
                case ATTR_BOOKTITLE:
                    minLength = MIN_LEN_BOOKTITLE;
                    break;
                case ATTR_YEARPUBLISHED:
                    minLength = MIN_LEN_YEARPUBLISHED;
                    break;                    
                case ATTR_NUMBERPAGES:
                    minLength = MIN_LEN_NUMBERPAGES;
                    break;                    
                default:
                    System.out.println("error");
                    break;
            }
            maxFormatLengthMap.put(attribute, minLength);
        }
        
        while(rs.next()){
            for (int i = 0; i < attributeList.size(); i++) {
                String attribute = (String) attributeList.get(i);
                int length = rs.getString(attribute).length();
                
                if (length > maxFormatLengthMap.get(attribute)){
                    maxFormatLengthMap.put(attribute, length);
                }
            }
        }
        
        String format = "";
        
        for (int i = 0; i < attributeList.size(); i++) {
            format += "%-";
            Integer length = maxFormatLengthMap.get(attributeList.get(i)) + 2;
            format += length.toString();
            format += "s";
        }
        format += '\n';
//        System.out.println(format);
        
        rs.beforeFirst(); // resets result set
        
        return format;
    }
    
    /**
     * Ascertains if a specific book exists within the database.
     * This query is case sensitive.
     * @param booktitle the title of the book in question
     * @param conn the connection to the database
     * @return Returns true if book is found, false otherwise
     * @throws SQLException 
     */
    public static boolean bookExists(String bookTitle, String groupName, Connection conn) throws SQLException{
        boolean bookExists = false;
        
        String sql = "SELECT booktitle, groupname from Books WHERE booktitle = ? AND groupname = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, bookTitle);
        pstmt.setString(2, groupName);

        ResultSet rs = pstmt.executeQuery();
        // if the result set has a row (therefore rs.next() returns true) then book exists
        // if the result set has no rows (therefore rs.next() returns false) then book does not exist.
        bookExists = rs.next(); 

        rs.close();
        pstmt.close();
        return bookExists;
    }
    
    public static boolean groupExists(String groupName, Connection conn) throws SQLException{
        boolean groupExists = false;
        
        String sql = "SELECT groupname from WritingGroups WHERE groupname = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, groupName);

        ResultSet rs = pstmt.executeQuery();
        // if the result set has a row (therefore rs.next() returns true) then book exists
        // if the result set has no rows (therefore rs.next() returns false) then book does not exist.
        groupExists = rs.next(); 

        rs.close();
        pstmt.close();
        return groupExists;
    }
    
    public static boolean publisherExists(String publisherName, Connection conn) throws SQLException{
        boolean publisherExists = false;
        
        String sql = "SELECT publishername from Publishers WHERE publishername = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, publisherName);

        ResultSet rs = pstmt.executeQuery();
        // if the result set has a row (therefore rs.next() returns true) then book exists
        // if the result set has no rows (therefore rs.next() returns false) then book does not exist.
        publisherExists = rs.next(); 

        rs.close();
        pstmt.close();
        return publisherExists;
    }
    
    /**
     * This function removes a publisher from the database.
     * This assumes that the publishername DOES EXIST.
     * @param conn
     * @param pstmt
     * @param publishernameInput
     * @throws SQLException 
     */
    public static void removePublisher(Connection conn, PreparedStatement pstmt, String publishernameInput) throws SQLException{
        
        String sql = "DELETE from Publisher where publishername=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, publishernameInput);
        
        pstmt.executeUpdate();

        // clean up environment:
        pstmt.close();
        
    }
    
    ////////// Put all required functions below here (but before main() obvsly) //////////
    
    ////////// OPTION 1 //////////
    public static void listAllWritingGroups(Connection conn, Statement stmt) throws SQLException
    {
        // create query:
        String sql =  "SELECT * FROM WritingGroups";
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery(sql);
        
        // creates the display format based on what attributes you add in the arraylist:
        ArrayList<String> attributeList = new ArrayList<String>();
        attributeList.add(ATTR_GROUPNAME);
        attributeList.add(ATTR_HEADWRITER);
        attributeList.add(ATTR_YEARFORMED);
        attributeList.add(ATTR_SUBJECT);
        String tableDisplayFormat = getTableFormatString(rs, attributeList);
        
        // prints attribute headers:
        lineFormat(tableDisplayFormat);
        System.out.printf(tableDisplayFormat, ATTR_GROUPNAME, ATTR_HEADWRITER, ATTR_YEARFORMED, ATTR_SUBJECT);
        lineFormat(tableDisplayFormat);


        // iterates through the result set of the query in order to print the results
        while(rs.next())
        {
            String name = dispNull(rs.getString(ATTR_GROUPNAME));
            String head = dispNull(rs.getString(ATTR_HEADWRITER));
            String year = dispNull(rs.getString(ATTR_YEARFORMED));
            String subj = dispNull(rs.getString(ATTR_SUBJECT));
            
            System.out.printf(tableDisplayFormat, name, head, year, subj);
        }
        lineFormat(tableDisplayFormat);

        rs.close();
        stmt.close();
    }
    
    ////////// OPTION 2 //////////
    public static void listAllDataForSpecificGroup(Connection conn, PreparedStatement pstmt) throws SQLException{

        System.out.print(">>> Enter in a group name for a WritingGroup: ");
        String groupnameInput = in.nextLine();
        
        String sql = "SELECT * from Books natural join Publishers natural join WritingGroups where groupname = ?";
        
        // create a PreparedStatement object since we have user input for our query:
        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        pstmt.setString(1, groupnameInput); // 1 represents the first '?' in the sql; groupnameInput is what that '?' get replaced by
        
        // create result set: 
        ResultSet rs = pstmt.executeQuery();
        
        // creates the display format based on what attributes you add in the arraylist:
        ArrayList<String> attributeList = new ArrayList<String>();
        attributeList.add(ATTR_GROUPNAME);
        attributeList.add(ATTR_HEADWRITER);
        attributeList.add(ATTR_YEARFORMED);
        attributeList.add(ATTR_SUBJECT);
        attributeList.add(ATTR_BOOKTITLE);
        attributeList.add(ATTR_YEARPUBLISHED);
        attributeList.add(ATTR_NUMBERPAGES);
        attributeList.add(ATTR_PUBLISHERNAME);
        attributeList.add(ATTR_PUBLISHERADDRESS);
        attributeList.add(ATTR_PUBLISHERPHONE);
        attributeList.add(ATTR_PUBLISHEREMAIL);
        String tableDisplayFormat = getTableFormatString(rs, attributeList);
        
        // print out header for table:
        lineFormat(tableDisplayFormat);
        System.out.printf(tableDisplayFormat, ATTR_GROUPNAME, ATTR_HEADWRITER, ATTR_YEARFORMED, ATTR_SUBJECT, ATTR_BOOKTITLE, ATTR_YEARPUBLISHED, ATTR_NUMBERPAGES, ATTR_PUBLISHERNAME, ATTR_PUBLISHERADDRESS, ATTR_PUBLISHERPHONE, ATTR_PUBLISHEREMAIL);
        lineFormat(tableDisplayFormat);
        
        // keeps track of the numOfRows in order to see if a row was even returned
        int numOfRows = 0; 
        
        // go through result set:
        while (rs.next()) {
            numOfRows++;
            
            // Retrieve by column name
            String groupname = dispNull(rs.getString(ATTR_GROUPNAME));
            String headwriter = dispNull(rs.getString(ATTR_HEADWRITER));
            String yearformed = dispNull(Integer.toString(rs.getInt(ATTR_YEARFORMED)));
            String subject = dispNull(rs.getString(ATTR_SUBJECT));
            String booktitle = dispNull(rs.getString(ATTR_BOOKTITLE));
            String yearpublished = dispNull(Integer.toString(rs.getInt(ATTR_YEARPUBLISHED)));
            String numberpages = dispNull(Integer.toString(rs.getInt(ATTR_NUMBERPAGES)));
            String publishername = dispNull(rs.getString(ATTR_PUBLISHERNAME));
            String publisheraddress = dispNull(rs.getString(ATTR_PUBLISHERADDRESS));
            String publisherphone = dispNull(rs.getString(ATTR_PUBLISHERPHONE));
            String publisheremail = dispNull(rs.getString(ATTR_PUBLISHEREMAIL));

            // Display values
            System.out.printf(tableDisplayFormat, groupname, headwriter, yearformed, subject, booktitle, yearpublished, numberpages, publishername, publisheraddress, publisherphone, publisheremail);
        }
        if(numOfRows == 0) {
            System.out.println(">>> No rows were returned for 'groupName' = " + groupnameInput);
        }
        lineFormat(tableDisplayFormat);
        
        rs.close();
        pstmt.close();
    }
    
    ////////// OPTION 3 //////////
    public static void listAllPublishers(Connection conn, Statement stmt) throws SQLException
    {        
        String sql =  "SELECT * FROM Publishers";
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery(sql);
        
        // creates the display format based on what attributes you add in the arraylist:
        ArrayList<String> attributeList = new ArrayList<String>();
        attributeList.add(ATTR_PUBLISHERNAME);
        attributeList.add(ATTR_PUBLISHERADDRESS);
        attributeList.add(ATTR_PUBLISHERPHONE);
        attributeList.add(ATTR_PUBLISHEREMAIL);
        String tableDisplayFormat = getTableFormatString(rs, attributeList);
        
        // prints attribute headers:
        lineFormat(tableDisplayFormat);
        System.out.printf(tableDisplayFormat, ATTR_PUBLISHERNAME, ATTR_PUBLISHERADDRESS, ATTR_PUBLISHERPHONE, ATTR_PUBLISHEREMAIL);
        lineFormat(tableDisplayFormat);
        
        // iterates through the result set of the query in order to print the results
        while(rs.next())
        {
            String name = dispNull(rs.getString(ATTR_PUBLISHERNAME));
            String addr = dispNull(rs.getString(ATTR_PUBLISHERADDRESS));
            String numb = dispNull(rs.getString(ATTR_PUBLISHERPHONE));
            String emai = dispNull(rs.getString(ATTR_PUBLISHEREMAIL));
            
            System.out.printf(tableDisplayFormat, name, addr, numb, emai);
        }
        
        lineFormat(tableDisplayFormat);
        rs.close();
        stmt.close();
    }

    ////////// OPTION 4 //////////
    public static void listAllDataForSpecificPublisher(Connection conn, PreparedStatement pstmt) throws SQLException
    {
        System.out.print(">>> Enter in a publisherName for a Publisher: ");
        String publishernameInput = in.nextLine();
        
        String sql = "SELECT * from Books natural join WritingGroups natural join Publishers where PublisherName = ?";
        
        // create a PreparedStatement object since we have user input for our query:
        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        pstmt.setString(1, publishernameInput); // 1 represents the first '?' in the sql; groupnameInput is what that '?' get replaced by
        
        // create result set: 
        ResultSet rs = pstmt.executeQuery();
        
        // creates the display format based on what attributes you add in the arraylist:
        ArrayList<String> attributeList = new ArrayList<String>();
        attributeList.add(ATTR_PUBLISHERNAME);
        attributeList.add(ATTR_PUBLISHERADDRESS);
        attributeList.add(ATTR_PUBLISHERPHONE);
        attributeList.add(ATTR_PUBLISHEREMAIL);
        attributeList.add(ATTR_GROUPNAME);
        attributeList.add(ATTR_HEADWRITER);
        attributeList.add(ATTR_YEARFORMED);
        attributeList.add(ATTR_SUBJECT);
        attributeList.add(ATTR_BOOKTITLE);
        attributeList.add(ATTR_YEARPUBLISHED);
        attributeList.add(ATTR_NUMBERPAGES);
        String tableDisplayFormat = getTableFormatString(rs, attributeList);
        
        // print out header for table:
        lineFormat(tableDisplayFormat);
        System.out.printf(tableDisplayFormat, ATTR_PUBLISHERNAME, ATTR_PUBLISHERADDRESS, ATTR_PUBLISHERPHONE, ATTR_PUBLISHEREMAIL, ATTR_GROUPNAME, ATTR_HEADWRITER, ATTR_YEARFORMED, ATTR_SUBJECT, ATTR_BOOKTITLE, ATTR_YEARPUBLISHED, ATTR_NUMBERPAGES);
        lineFormat(tableDisplayFormat);
        
        // keeps track of the numOfRows in order to see if a row was even returned
        int numOfRows = 0; 
        
        // go through result set:
        while (rs.next()) {
            numOfRows++;
            
            // Retrieve by column name            
            String publishername = dispNull(rs.getString(ATTR_PUBLISHERNAME));
            String publisheraddress = dispNull(rs.getString(ATTR_PUBLISHERADDRESS));
            String publisherphone = dispNull(rs.getString(ATTR_PUBLISHERPHONE));
            String publisheremail = dispNull(rs.getString(ATTR_PUBLISHEREMAIL));
            String groupname = dispNull(rs.getString(ATTR_GROUPNAME));
            String headwriter = dispNull(rs.getString(ATTR_HEADWRITER));
            String yearformed = dispNull(Integer.toString(rs.getInt(ATTR_YEARFORMED)));
            String subject = dispNull(rs.getString(ATTR_SUBJECT));
            String booktitle = dispNull(rs.getString(ATTR_BOOKTITLE));
            String yearpublished = dispNull(Integer.toString(rs.getInt(ATTR_YEARPUBLISHED)));
            String numberpages = dispNull(Integer.toString(rs.getInt(ATTR_NUMBERPAGES)));
            
            // Display values
            System.out.printf(tableDisplayFormat, publishername, publisheraddress, publisherphone, publisheremail, groupname, headwriter, yearformed, subject, booktitle, yearpublished, numberpages);
        }
        if(numOfRows == 0) {
            System.out.println(">>> No rows were returned for 'publishersNamed' = " + publishernameInput);
        }
        lineFormat(tableDisplayFormat);
        
        rs.close();
        pstmt.close();
    }
    
    ////////// OPTION 5 //////////
    public static void listAllBooks(Connection conn, Statement stmt) throws SQLException
    {
        // create query:
        String sql =  "SELECT * FROM Books";
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery(sql);
        
        // creates the display format based on what attributes you add in the arraylist:
        ArrayList<String> attributeList = new ArrayList<String>();
        attributeList.add(ATTR_BOOKTITLE);
        attributeList.add(ATTR_GROUPNAME);
        attributeList.add(ATTR_PUBLISHERNAME);
        attributeList.add(ATTR_YEARPUBLISHED);
        attributeList.add(ATTR_NUMBERPAGES);
        String tableDisplayFormat = getTableFormatString(rs, attributeList);
        
        // prints attribute headers:
        lineFormat(tableDisplayFormat);
        System.out.printf(tableDisplayFormat, ATTR_BOOKTITLE, ATTR_GROUPNAME, ATTR_PUBLISHERNAME, ATTR_YEARPUBLISHED, ATTR_NUMBERPAGES);
        lineFormat(tableDisplayFormat);

        // iterates through the result set of the query in order to print the results
        while(rs.next())
        {
            String bookTitle = dispNull(rs.getString(ATTR_BOOKTITLE));
            String groupName = dispNull(rs.getString(ATTR_GROUPNAME));
            String publisherName = dispNull(rs.getString(ATTR_PUBLISHERNAME));
            String year = dispNull(rs.getString(ATTR_YEARPUBLISHED));
            String pages = dispNull(rs.getString(ATTR_NUMBERPAGES));
            
            System.out.printf(tableDisplayFormat, bookTitle, groupName, publisherName, year, pages);
        }
        lineFormat(tableDisplayFormat);
        rs.close();
        stmt.close();
    }

    ////////// OPTION 6 //////////
    public static void listAllDataForSpecificBook(Connection conn, PreparedStatement pstmt) throws SQLException
    {                
        System.out.print(">>> Enter in a bookTitle: ");
        String bookInput = in.nextLine();
        
        String sql = "SELECT * from Books natural join WritingGroups natural join Publishers where BookTitle = ?";
        
        // create a PreparedStatement object since we have user input for our query:
        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        pstmt.setString(1, bookInput); // 1 represents the first '?' in the sql; groupnameInput is what that '?' get replaced by
        
        // create result set: 
        ResultSet rs = pstmt.executeQuery();
        
        // creates the display format based on what attributes you add in the arraylist:
        ArrayList<String> attributeList = new ArrayList<String>();
        attributeList.add(ATTR_BOOKTITLE);
        attributeList.add(ATTR_YEARPUBLISHED);
        attributeList.add(ATTR_NUMBERPAGES);
        attributeList.add(ATTR_GROUPNAME);
        attributeList.add(ATTR_HEADWRITER);
        attributeList.add(ATTR_YEARFORMED);
        attributeList.add(ATTR_SUBJECT);
        attributeList.add(ATTR_PUBLISHERNAME);
        attributeList.add(ATTR_PUBLISHERADDRESS);
        attributeList.add(ATTR_PUBLISHERPHONE);
        attributeList.add(ATTR_PUBLISHEREMAIL);
        String tableDisplayFormat = getTableFormatString(rs, attributeList);
        
        // print out header for table:
        lineFormat(tableDisplayFormat);
        System.out.printf(tableDisplayFormat, ATTR_BOOKTITLE, ATTR_YEARPUBLISHED, ATTR_NUMBERPAGES, ATTR_GROUPNAME, ATTR_HEADWRITER, ATTR_YEARFORMED, ATTR_SUBJECT,  ATTR_PUBLISHERNAME, ATTR_PUBLISHERADDRESS, ATTR_PUBLISHERPHONE, ATTR_PUBLISHEREMAIL);
        lineFormat(tableDisplayFormat);
        
        // keeps track of the numOfRows in order to see if a row was even returned
        int numOfRows = 0; 
        
        // go through result set:
        while (rs.next()) {
            numOfRows++;
            
            // Retrieve by column name
            String booktitle = dispNull(rs.getString(ATTR_BOOKTITLE));
            String yearpublished = dispNull(Integer.toString(rs.getInt(ATTR_YEARPUBLISHED)));
            String numberpages = dispNull(Integer.toString(rs.getInt(ATTR_NUMBERPAGES)));
            String groupname = dispNull(rs.getString(ATTR_GROUPNAME));
            String headwriter = dispNull(rs.getString(ATTR_HEADWRITER));
            String yearformed = dispNull(Integer.toString(rs.getInt(ATTR_YEARFORMED)));
            String subject = dispNull(rs.getString(ATTR_SUBJECT));
            String publishername = dispNull(rs.getString(ATTR_PUBLISHERNAME));
            String publisheraddress = dispNull(rs.getString(ATTR_PUBLISHERADDRESS));
            String publisherphone = dispNull(rs.getString(ATTR_PUBLISHERPHONE));
            String publisheremail = dispNull(rs.getString(ATTR_PUBLISHEREMAIL));

            // Display values
            System.out.printf(tableDisplayFormat, booktitle, yearpublished, numberpages, groupname, headwriter, yearformed, subject,  publishername, publisheraddress, publisherphone, publisheremail);
        }
        if(numOfRows == 0) {
            System.out.println(">>> No rows were returned for 'bookTitle'= " + bookInput);
        }
        lineFormat(tableDisplayFormat);
        
        rs.close();
        pstmt.close();
    }

    ////////// OPTION 7 //////////
    public static boolean insertNewBook(Connection conn, PreparedStatement pstmt) throws SQLException {
        boolean insertStatus = true;
        
        System.out.print("Enter the Book's Title: ");
        String title = in.nextLine();
        
        System.out.print("Enter the Group's Name: ");
        String gname = in.nextLine();
        
        System.out.print("Enter the Publisher's name: ");
        String pname = in.nextLine();
        
        int year;
        int pages;
        
        try{
            System.out.print("Enter the Year Published: ");
            year = in.nextInt();
            in.nextLine(); // eats newline
            if(year < 0){
                System.out.println("!!! Year cannot be negative. Exiting... Please try again!");
                return false;
            }
        }
        catch (InputMismatchException e){
            System.out.println("!!! Please enter in only integers for year published!");
            in.nextLine();
            return false;
        }
         
        try{
            System.out.print("Enter the number of pages: ");
            pages = in.nextInt();
            in.nextLine(); // eats newline
        }
        catch (InputMismatchException e){
            System.out.println("!!! Please enter in only integers for number of pages!");
            in.nextLine();
            return false;
        }
        


        String sql = "INSERT INTO Books VALUES (?, ?, ?, ?, ?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, title);
        pstmt.setString(2, gname);
        pstmt.setString(3, pname);
        pstmt.setInt(4, year);
        pstmt.setInt(5, pages);
        
        try {
            pstmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            insertStatus = false;
            System.out.println("!!! Sorry, your input data resulted in a CONSTRAINT VIOLATION.");
            System.out.println("!!! Sorry, here are the specific error(s):");

            if(bookExists(title, gname, conn)) {
                System.out.println("--> [PK Constraint] bookTitle and groupName already exist in the database ");
            }
            if(!groupExists(gname, conn)){
                System.out.println("--> [FK Constraint] GROUP NAME does not exist in the database ");
            }
            if(!publisherExists(pname, conn)){
                System.out.println("--> [FK Constraint] PUBLISHER NAME does not exist in the database ");
            }
      	} 
        
        pstmt.close();
        return insertStatus;
    }
 
    ////////// OPTION 8 //////////
    public static boolean replacePublisher(Connection conn, PreparedStatement pstmt) throws SQLException{
        
        boolean insertStatus = true;
        boolean replaceStatus = true;
        
        // gets inputs for a NEW publisher from the user:
        System.out.print(">>> Enter in a NEW publisher name: ");
        String nameInput = in.nextLine();
        
        System.out.print(">>> Enter in a NEW publisher address: ");
        String addressInput = in.nextLine();
        
        System.out.print(">>> Enter in a NEW publisher phone: ");
        String phoneInput = in.nextLine();
        
        System.out.print(">>> Enter in a NEW publisher email: ");
        String emailInput = in.nextLine();
        
        // gets input for the old publisher to be replaced:
        System.out.print(">>> Enter in the OLD publisher name to be replaced: ");
        String oldpublishnameInput = in.nextLine();

        if(publisherExists(oldpublishnameInput, conn)){
            // INSERTS A NEW PUBLISHER IN WHILE HANDLING CONSTRAINT ERRORS:
            String sqlInsert = "INSERT INTO Publishers VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlInsert);
            pstmt.setString(1, nameInput);
            pstmt.setString(2, addressInput);
            pstmt.setString(3, phoneInput);
            pstmt.setString(4, emailInput);

            try{
                // execute the insert: 
                pstmt.executeUpdate();
            }
            catch(SQLIntegrityConstraintViolationException e){
                insertStatus = false;
                replaceStatus = false;
                System.out.println("!!! Sorry, your input data resulted in a CONSTRAINT VIOLATION.");
                System.out.println("!!! [PK Constraint] PUBLISHER NAME already exists in the database");
                // e.printStackTrace();
            }
            // reset the pstmt for the update:
            pstmt.close();
            
            // if the insert was successful then we replace the old publisher with the new publisher:
            if(insertStatus){
                // REPLACES ALL
                String sqlUpdate = "UPDATE Books SET publishername = ? WHERE publishername = ?";
                pstmt = conn.prepareStatement(sqlUpdate);
                pstmt.setString(1, nameInput);
                pstmt.setString(2, oldpublishnameInput);

                try{
                    // execute the insert: 
                    pstmt.executeUpdate();
                }
                catch(SQLIntegrityConstraintViolationException e){
                    replaceStatus = false;
                    // this try catch is unneccessary
                    // because if the code executes the executeUpdate() in the above try block
                    // then:
                    // - we have already checked if the old publisher exists
                    // - AND the insert of the new publisher was successful
                    // - there are no more logical conditions that this update/replace should fail.
                    // - only other case is potential asynchronous issues that but that is out of the scope of this class.
                }
                // clean environment:
                pstmt.close();
            }
        }
        else{
            System.out.println("!!! Sorry, OLD publisherName = " + oldpublishnameInput + " does not exist in the DB.");
            System.out.println("!!! Replace cancelled. Please try again!");
        }
        
        return replaceStatus;
    }
    ////////// OPTION 9 //////////
    public static boolean removeBook(Connection conn, PreparedStatement pstmt) throws SQLException {
        
        boolean removeStatus = false;
        
        // gets input from the user:
        System.out.print(">>> Enter in a BOOK TITLE: ");
        String booktitleInput = in.nextLine();
        
        System.out.print(">>> Enter in a GROUP NAME: ");
        String groupnameInput = in.nextLine();
        
        if(bookExists(booktitleInput, groupnameInput, conn)){
            removeStatus = true;
            String sql = "DELETE from Books where booktitle = ? AND groupname = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, booktitleInput);
            pstmt.setString(2, groupnameInput);
            pstmt.executeUpdate();
            pstmt.close();
        }
        else{
            System.out.println("!!! Sorry, the combination of [bookTitle = " + booktitleInput + "] and [groupName = " + groupnameInput + "] does not exist in the database to delete.");
        }
        
        return removeStatus;
    }

    public static void main(String[] args) {
        line(LINE_LENGTH);
        System.out.print(">>> Name of the database: ");
        DBNAME = in.nextLine();
        System.out.print(">>> Database username: ");
        USER = in.nextLine();
        System.out.print(">>> Database password: ");
        PASS = in.nextLine();
        
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME + ";user="+ USER + ";password=" + PASS;

        Connection conn = null; //initialize the connection; use this variable as input for your functions
        PreparedStatement pstmt = null;  //used for statements that require user input
        Statement stmt = null; // used for statements that do not require user input
        
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
            System.out.println(">>> Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            //STEP 4: Execute a query
            boolean done = false;
            while(!done){
                displayMenu();

                System.out.print(">>> Enter in your choice [0-9]: ");
                
                int choice = 0;
                try{
                    choice = in.nextInt();
                }
                catch(java.util.InputMismatchException e){
                    System.out.println("!!! Sorry we don't support character string input for menu choices!");
                    choice = 1540; // meaning the user has to try again.
                }
                in.nextLine(); // eats newline

                switch (choice) {
                    case 0: 
                        done = true;
                        System.out.println(">>> BYE ~");
                        line(LINE_LENGTH);
                        break;
                    case 1:
                     
                       listAllWritingGroups(conn, stmt);
                        break;
                    case 2:
                        // pass in the connection to your functions then work with the connection inside your function
                        listAllDataForSpecificGroup(conn, pstmt);
                        // the reason for passing in connection is obvs, we need to refence our database
                        // the reason for passing in a PreparedStatement or Statement object is less obvs
                        // we need it for exception handling within the functions 
                        // since our functions will be throwing exceptions we need to handle them here (in main())
                        // so we need a reference to the statement in case it throws an exception withIN our function
                        break;
                    case 3:
                       listAllPublishers(conn, stmt);
                        break;
                    case 4:
                       listAllDataForSpecificPublisher(conn, pstmt);
                        break;
                    case 5:
                        listAllBooks(conn, stmt);
                        break;
                    case 6:
                        listAllDataForSpecificBook(conn, pstmt);
                        break;
                    case 7:
                        boolean insertSuccess = insertNewBook(conn, pstmt);
                        System.out.println("<<< Insert Complete: " + insertSuccess);
                        line(LINE_LENGTH);
                        break;
                    case 8:
                        boolean replaceSuccess = replacePublisher(conn, pstmt);
                        System.out.println("<<< Replace Complete: " + replaceSuccess);
                        line(LINE_LENGTH);
                        break;
                    case 9:
                        boolean deletionSuccess = removeBook(conn, pstmt);
                        System.out.println("<<< Deletion Complete: " + deletionSuccess);
                        line(LINE_LENGTH);
                        break;
                    default:
                        System.out.println("!!! INVALID INPUT, TRY AGAIN.");
                }
                if(choice != 0) displayContinueMessage();
            }
            conn.close();
        } catch (SQLNonTransientConnectionException se) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(">>> Potential problems:");
            System.out.println("--- DATABASE NOT FOUND: " + DBNAME);
            System.out.println("--- Username incorrect: " + USER);
            System.out.println("--- Password incorrect: " + PASS);
            System.out.println(">>> SEE STACK TRACE FOR EXACT REASON");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            se.printStackTrace();
        }
        catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}//end class
