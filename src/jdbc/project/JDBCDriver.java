
package jdbc.project;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    static final int MIN_LEN_PUBLISHERADDR = 13;
    static final int MIN_LEN_PUBLISHERPHONE = 14;
    static final int MIN_LEN_PUBLISHEREMAIL = 14;
    static final int MIN_LEN_BOOKTITLE = 9;
    static final int MIN_LEN_YEARPUBLISHED = 13;
    static final int MIN_LEN_NUMBERPAGES = 11;

    
    // scanner variable used for user input:
    static Scanner in = new Scanner(System.in);
    
    //  Database credentials
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
     * Prompts the user to press [ENTER]. This function basically gives the user
     * time to read the results of their action.
     */
    public static void displayContinueMessage(){
        System.out.print(">>> Please press [ENTER] to continue back to main menu: ");
        in.nextLine();
    }
    
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
                    minLength = MIN_LEN_PUBLISHERADDR;
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
        System.out.println(format);
        
        rs.beforeFirst(); // resets result set
        
        return format;
    }
    
    
    //////// Put all required functions below here (but before main() obvsly) //////////
    
    //////// Option 1 ///////////////
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
        line(LINE_LENGTH*3);
        System.out.printf(tableDisplayFormat, ATTR_GROUPNAME, ATTR_HEADWRITER, ATTR_YEARFORMED, ATTR_SUBJECT);
        line(LINE_LENGTH*3);

        // iterates through the result set of the query in order to print the results
        while(rs.next())
        {
            String name = dispNull(rs.getString(ATTR_GROUPNAME));
            String head = dispNull(rs.getString(ATTR_HEADWRITER));
            String year = dispNull(rs.getString(ATTR_YEARFORMED));
            String subj = dispNull(rs.getString(ATTR_SUBJECT));
            
            System.out.printf(tableDisplayFormat, name, head, year, subj);
        }
        
        rs.close();
        stmt.close();
    }
    
   ///////// Option 2 /////////////// 
    public static void listAllDataForSpecificGroup(Connection conn, PreparedStatement pstmt) throws SQLException{

        System.out.print(">>> Enter in a GROUP NAME: ");
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
        line(LINE_LENGTH*3);
        System.out.printf(tableDisplayFormat, ATTR_GROUPNAME, ATTR_HEADWRITER, ATTR_YEARFORMED, ATTR_SUBJECT, ATTR_BOOKTITLE, ATTR_YEARPUBLISHED, ATTR_NUMBERPAGES, ATTR_PUBLISHERNAME, ATTR_PUBLISHERADDRESS, ATTR_PUBLISHERPHONE, ATTR_PUBLISHEREMAIL);
        line(LINE_LENGTH*3);
        
        // keeps track of the numOfRows in order to see if a row was even returned
        int numOfRows = 0; 
        
        // go through result set:
        while (rs.next()) {
            numOfRows++;
            
            // Retrieve by column name
            String groupname = dispNull(rs.getString("groupname"));
            String headwriter = dispNull(rs.getString("headwriter"));
            String yearformed = dispNull(Integer.toString(rs.getInt("yearformed")));
            String subject = dispNull(rs.getString("subject"));
            String booktitle = dispNull(rs.getString("booktitle"));
            String yearpublished = dispNull(Integer.toString(rs.getInt("yearpublished")));
            String numberpages = dispNull(Integer.toString(rs.getInt("numberpages")));
            String publishername = dispNull(rs.getString("publishername"));
            String publisheraddress = dispNull(rs.getString("publisheraddress"));
            String publisherphone = dispNull(rs.getString("publisherphone"));
            String publisheremail = dispNull(rs.getString("publisheremail"));

            // Display values
            System.out.printf(tableDisplayFormat, groupname, headwriter, yearformed, subject, booktitle, yearpublished, numberpages, publishername, publisheraddress, publisherphone, publisheremail);
        }
        if(numOfRows == 0) {
            System.out.println(">>> No rows were returned for 'GROUP NAME'=" + groupnameInput);
        }
        line(LINE_LENGTH*3);
        
        rs.close();
        pstmt.close();
        
    }
    
    ///////////// OPTION 3 ////////////////
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
        line(LINE_LENGTH*3);
        System.out.printf(tableDisplayFormat, ATTR_PUBLISHERNAME, ATTR_PUBLISHERADDRESS, ATTR_PUBLISHERPHONE, ATTR_PUBLISHEREMAIL);
        line(LINE_LENGTH*3);
        
        // iterates through the result set of the query in order to print the results
        while(rs.next())
        {
            String name = dispNull(rs.getString(ATTR_PUBLISHERNAME));
            String addr = dispNull(rs.getString(ATTR_PUBLISHERADDRESS));
            String numb = dispNull(rs.getString(ATTR_PUBLISHERPHONE));
            String emai = dispNull(rs.getString(ATTR_PUBLISHEREMAIL));
            
            System.out.printf(tableDisplayFormat, name, addr, numb, emai);
        }

        rs.close();
        stmt.close();
    }

        
///////////// OPTION 4 ////////////////
public static void listAllDataForSpecificPublisher(Connection conn, PreparedStatement pstmt) throws SQLException
{
        System.out.print(">>> Enter in a Publisher: ");
        String publishernameInput = in.nextLine();
        
        String sql = "SELECT * from Books natural join WritingGroups natural join Publishers where PublisherName = ?";
        
        // create a PreparedStatement object since we have user input for our query:
        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        pstmt.setString(1, publishernameInput); // 1 represents the first '?' in the sql; groupnameInput is what that '?' get replaced by
        
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
        line(LINE_LENGTH*3);
        System.out.printf(tableDisplayFormat, ATTR_PUBLISHERNAME, ATTR_PUBLISHERADDRESS, ATTR_PUBLISHERPHONE, ATTR_PUBLISHEREMAIL, ATTR_BOOKTITLE, ATTR_YEARPUBLISHED, ATTR_NUMBERPAGES, ATTR_GROUPNAME, ATTR_HEADWRITER, ATTR_YEARFORMED, ATTR_SUBJECT);
        line(LINE_LENGTH*3);
        
        // keeps track of the numOfRows in order to see if a row was even returned
        int numOfRows = 0; 
        
        // go through result set:
        while (rs.next()) {
            numOfRows++;
            
            // Retrieve by column name
            String publishername = dispNull(rs.getString("publishername"));
            String publisheraddress = dispNull(rs.getString("publisheraddress"));
            String publisherphone = dispNull(rs.getString("publisherphone"));
            String publisheremail = dispNull(rs.getString("publisheremail"));
            String booktitle = dispNull(rs.getString("booktitle"));
            String yearpublished = dispNull(Integer.toString(rs.getInt("yearpublished")));
            String numberpages = dispNull(Integer.toString(rs.getInt("numberpages")));
            String groupname = dispNull(rs.getString("groupname"));
            String headwriter = dispNull(rs.getString("headwriter"));
            String yearformed = dispNull(Integer.toString(rs.getInt("yearformed")));
            String subject = dispNull(rs.getString("subject"));
            

            // Display values
            System.out.printf(tableDisplayFormat, publishername, publisheraddress, publisherphone, publisheremail, booktitle, yearpublished, numberpages, groupname, headwriter, yearformed, subject);
        }
        if(numOfRows == 0) {
            System.out.println(">>> No rows were returned for 'Publishers Named '= " + publishernameInput);
        }
        line(LINE_LENGTH*3);
        
        rs.close();
        pstmt.close();
        
    }
///////////// OPTION 5 /////////////
public static void listAllBooks(Connection conn, Statement stmt) throws SQLException
    {
        // create query:
        String sql =  "SELECT * FROM Books";
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt.executeQuery(sql);
        
        // creates the display format based on what attributes you add in the arraylist:
        ArrayList<String> attributeList = new ArrayList<String>();
        attributeList.add(ATTR_BOOKTITLE);
        attributeList.add(ATTR_YEARPUBLISHED);
        attributeList.add(ATTR_NUMBERPAGES);
        String tableDisplayFormat = getTableFormatString(rs, attributeList);
        
        // prints attribute headers:
        line(LINE_LENGTH*3);
        System.out.printf(tableDisplayFormat, ATTR_BOOKTITLE, ATTR_YEARPUBLISHED, ATTR_NUMBERPAGES);
        line(LINE_LENGTH*3);

        // iterates through the result set of the query in order to print the results
        while(rs.next())
        {
            String bktt = dispNull(rs.getString(ATTR_BOOKTITLE));
            String yrpb = dispNull(rs.getString(ATTR_YEARPUBLISHED));
            String nupg = dispNull(rs.getString(ATTR_NUMBERPAGES));
            
            System.out.printf(tableDisplayFormat, bktt, yrpb, nupg);
        }
        
        rs.close();
        stmt.close();
    }
/////////////////// OPTION 6 ////////////////

public static void listAllDataForSpecificBook(Connection conn, PreparedStatement pstmt) throws SQLException
{                
        System.out.print(">>> Enter in a Book Title: ");
        String bookInput = in.nextLine();
        
        String sql = "SELECT * from Books natural join WritingGroups natural join Publishers where BookTitle = ?";
        
        // create a PreparedStatement object since we have user input for our query:
        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        pstmt.setString(1, bookInput); // 1 represents the first '?' in the sql; groupnameInput is what that '?' get replaced by
        
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
        line(LINE_LENGTH*3);
        System.out.printf(tableDisplayFormat, ATTR_BOOKTITLE, ATTR_YEARPUBLISHED, ATTR_NUMBERPAGES, ATTR_GROUPNAME, ATTR_HEADWRITER, ATTR_YEARFORMED, ATTR_SUBJECT, ATTR_PUBLISHERNAME, ATTR_PUBLISHERADDRESS, ATTR_PUBLISHERPHONE, ATTR_PUBLISHEREMAIL);
        line(LINE_LENGTH*3);
        
        // keeps track of the numOfRows in order to see if a row was even returned
        int numOfRows = 0; 
        
        // go through result set:
        while (rs.next()) {
            numOfRows++;
            
            // Retrieve by column name
            String publishername = dispNull(rs.getString("publishername"));
            String publisheraddress = dispNull(rs.getString("publisheraddress"));
            String publisherphone = dispNull(rs.getString("publisherphone"));
            String publisheremail = dispNull(rs.getString("publisheremail"));
            String booktitle = dispNull(rs.getString("booktitle"));
            String yearpublished = dispNull(Integer.toString(rs.getInt("yearpublished")));
            String numberpages = dispNull(Integer.toString(rs.getInt("numberpages")));
            String groupname = dispNull(rs.getString("groupname"));
            String headwriter = dispNull(rs.getString("headwriter"));
            String yearformed = dispNull(Integer.toString(rs.getInt("yearformed")));
            String subject = dispNull(rs.getString("subject"));
            

            // Display values
            System.out.printf(tableDisplayFormat, booktitle, yearpublished, numberpages, groupname, headwriter, yearformed, subject, publishername, publisheraddress, publisherphone, publisheremail);
        }
        if(numOfRows == 0) {
            System.out.println(">>> No rows were returned for 'Publishers Named '= " + bookInput);
        }
        line(LINE_LENGTH*3);
        
        rs.close();
        pstmt.close();
        
        // DO NOT CLOSE the connection here or inside other functions (user might want to do more queries)
}

/////////////////// OPTION 7 ////////////////
 public static void insertNewBook(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter The Group's Name: ");
        String gname = in.nextLine();
        
        System.out.println("Enter The Book's Title: ");
        String title = in.nextLine();
        
        System.out.println("Enter The Publisher's name: ");
        String pname = in.nextLine();
        
        System.out.println("Enter The Year Published: ");
        String year = in.nextLine();
        
        System.out.println("Enter the number of pages: ");
        String pages = in.nextLine();
        
        String sql = "INSERT INTO Book (GroupName, BookTitle, PublisherName, YearPublished, NumberPages) values (?,?,?,?,?)";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gname);
            pstmt.setString(2, title);
            pstmt.setString(3, pname);
            pstmt.setString(4, year);
            pstmt.setString(5, pages);
            pstmt.execute();
            
        } catch (SQLException SE) {
            System.out.println(SE.getMessage());
      	} finally {
            if (pstmt != null) 
                pstmt.close();              
        }
 }
 
 //////////// OPTION 8 ///////////////////
//  public static String insertNewPublisher(Connection conn) throws SQLException {
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//       
//        Statement stmt = null;
//        int count = 0;
//        int count1 = 0;
//        Scanner in = new Scanner(System.in);
//        
//        System.out.println("Enter a publisher name: ");
//        String newPName = in.nextLine();
//        
//        System.out.println("Enter a publisher address: ");
//        String addr = in.nextLine();
//        
//        System.out.println("Enter a publisher phone: ");
//        String phone = in.nextLine();
//        
//        System.out.println("Enter the publisher email: ");
//        String email = in.nextLine();
//        
//        String sql = "INSERT INTO Publisher (PublisherName, PublisherAddress, publisherPhone, PublisherEmail) values (?,?,?,?)";
//        String sql2= "SELECT COUNT(*) AS TOTAL FROM Publisher";
//        
//        try {
//            stmt = conn.createStatement();
//            rs = stmt.executeQuery(sql2);
//            
//            while (rs.next())
//                count = rs.getInt(1);
//            pstmt = conn.prepareStatement(sql);
//            
//            pstmt.setString(1, newPName);
//            pstmt.setString(2, addr);
//            pstmt.setString(3, phone);
//            pstmt.setString(4, email);
//            pstmt.execute();
//            
//            while (rs.next())
//                count1 = rs.getInt(1);
//            
//            rs = stmt.executeQuery(sql2);
//            
//            if(count != count1)
//                count = -1;
//        } catch (SQLException SE) {
//            System.out.println(SE.getMessage());
//      	} finally {
//            if (pstmt != null) 
//                pstmt.close();
//            
//            if (stmt != null)
//                stmt.close();
//            
//            if(rs != null)
//                rs.close();
//            
//            if(count == -1){
//                System.out.println("New Publisher Added Successfully added!");
//                return newPName;
//            }
//        }
//        return null; 
//  }
//////// OPTION 9 //////////////
   public static void removeBook(Connection conn) throws SQLException {
        
       boolean bookExists = false;
        boolean wgExists = false;
       
        PreparedStatement pstmt = null;
        String book;
        String writer;
        Statement stmt = null;
        
        Scanner in = new Scanner(System.in);
        System.out.println("Name of Book that needs to be Thanos'd: ");
        book = in.nextLine();
        try {
            stmt = conn.createStatement();
            String sql = "SELECT BookTitle FROM Book";
            
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String removeBook = rs.getString("BookTitle");
                if (removeBook.equals (book)){
                    bookExists = true;
                }
            }
            if (bookExists == false){
                 System.out.println("Book you entered does not exist in the Database.\n");
            }
            else {
                System.out.println("Name of the Writing Group?");
                writer = in.nextLine();
                
                String sql2 = "SELECT GroupName FROM Book WHERE GroupName = ?";
                PreparedStatement stmt2 = conn.prepareStatement(sql2);
                stmt2.setString(1, writer);
                rs = stmt2.executeQuery();
                
                while (rs.next()){
                    String removeWriter = rs.getString("GroupName");
                    if (removeWriter.equals(writer)){
                        wgExists = true;
                    }
                }
                if (wgExists == false){
                    System.out.println("Writing Group you entered does not exist in the Database.\n");
                }
                else {
                    String rsql = "DELETE FROM Book WHERE BookTitle = ? AND GroupName = ?";
                    pstmt = conn.prepareStatement(rsql);
                    pstmt.setString(1, book);
                    pstmt.setString(2, writer);
                    pstmt.execute();
                    System.out.println( "\n \nYour Book has been Thanos'd");
                }
            }
        } catch (SQLException SE){
            System.out.println(SE.getMessage());
        } finally {
        if(stmt != null)
            stmt.close();
        
        if(pstmt != null)
            pstmt.close();
        }
       // in.close();
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
            System.out.println("Connecting to database...");
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
//                        boolean insertSuccess = insertNewBook(conn, pstmt);
//                        System.out.println("<<< Insert Complete: " + insertSuccess);
                        line(LINE_LENGTH);
                        break;
                    case 8:
//                        boolean replaceSuccess = replacePublisher(conn, pstmt);
//                        System.out.println("<<< Replace Complete: " + replaceSuccess);
                        line(LINE_LENGTH);
                        break;
                    case 9:
//                        boolean deletionSuccess = removeBook(conn, pstmt);
//                        System.out.println("<<< Deletion Complete: " + deletionSuccess);
                        line(LINE_LENGTH);
                        break;
                    default:
                        System.out.println("!!! INVALID INPUT, TRY AGAIN.");
                }
                if(choice != 0) displayContinueMessage();
            }
            conn.close();
        } catch (SQLException se) {
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
}//end FirstExample}
