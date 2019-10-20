
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
        String PUBLISHER_DISPLAY_FORMAT = "%-30s%-40s%-14s%-20s%-30s%-15s%-13s%-40s%-50s%-16s%-40s\n";

        System.out.println(">>> Downloading all Publishers \n");
        
        String sql =  "SELECT * FROM Publishers";
        stmt = conn.prepareStatement(sql);
        
        ResultSet rs = stmt.executeQuery(sql);
        
        System.out.println("Publishers: ");
        System.out.printf(PUBLISHER_DISPLAY_FORMAT, "PublisherName","PublisherAddress", "PublisherPhone", "PublisherEmail");
        while(rs.next())
        {
            String name = rs.getString("GroupName");
            String addr = rs.getString("PublisherAddress");
            String phon = rs.getString("PublisherPhone");
            String emai = rs.getString("PublisherEmail");
            
            System.out.printf(PUBLISHER_DISPLAY_FORMAT, dispNull(name), dispNull(addr), dispNull(phon), dispNull(emai));
        }
        System.out.println();
        rs.close();
        stmt.close();
    }
///////////// OPTION 4 ////////////////
public static void listAllDataForSpecificPublisher(Connection conn, PreparedStatement pstmt) throws SQLException
{
     String displayFormat = "%-30s%-40s%-14s%-20s%-30s%-15s%-13s%-40s%-50s%-16s%-40s\n"; // I'll fix formatting later (gotta fix VARCHARS in the SQL first)

        System.out.print(">>> Enter in a Publisher: ");
        String publisherInput = in.nextLine();
        
        String sql = "SELECT * from Books natural join WritingGroups natural join Publishers where Publishers = ?";
        
        // create a PreparedStatement object since we have user input for our query:
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, publisherInput); // 1 represents the first '?' in the sql; groupnameInput is what that '?' get replaced by
        
        // create result set: 
        ResultSet rs = pstmt.executeQuery();
        
        // print out header for table:
        line(LINE_LENGTH*3);
        System.out.printf(displayFormat, "PublisherName", "PublisherAddress", "PublisherPhone", "PublisherEmail", "BookTitle", "YearPublished", "NumberPages", "GroupName", "HeadWriter", "YearFormed", "Subject");
        line(LINE_LENGTH*3);
        
        // keeps track of the numOfRows in order to see if a row was even returned
        int numOfRows = 0; 
        
        // go through result set:
        while (rs.next()) {
            numOfRows++;
            
            // Retrieve by column name
            String PublisherName = dispNull(rs.getString("PublisherName"));
            String PublisherAddress = dispNull(rs.getString("PublisherAddress"));
            String PublisherPhone = dispNull(rs.getString("PublisherPhone"));
            String PublisherEmail = dispNull(rs.getString("PublisherEmail"));
            String BookTitle = dispNull(rs.getString("BookTitle"));
            String YearPublished = dispNull(Integer.toString(rs.getInt("YearPublished")));
            String NumberPages = dispNull(Integer.toString(rs.getInt("NumberPages")));
            String GroupName = dispNull(rs.getString("GroupName"));
            String HeadWriter = dispNull(rs.getString("HeadWriter"));
            String YearFormed = dispNull(Integer.toString(rs.getInt("YearFormed")));
            String Subject = dispNull(rs.getString("Subject"));

            // Display values
            System.out.printf(displayFormat, PublisherName, PublisherAddress, PublisherPhone, PublisherEmail, BookTitle, YearPublished, NumberPages, GroupName, HeadWriter, YearFormed, Subject);
        }
        if(numOfRows == 0) {
            System.out.println(">>> No rows were returned for 'Publishers' = " + publisherInput);
        }
        line(LINE_LENGTH*3);
        
        // clean up environment:
        rs.close();
        pstmt.close();
        
        // DO NOT CLOSE the connection here or inside other functions (user might want to do more queries)
}
///////////// OPTION 5 /////////////
public static void listAllBooks(Connection conn, PreparedStatement pstmt) throws SQLException
    {
        String BOOK_DISPLAY_FORMAT = "%-30s%-40s%-14s%-20s%-30s%-15s%-13s%-40s%-50s%-16s%-40s\n";

        System.out.println(">>> Downloading all Book \n");
        
        String sql =  "SELECT * FROM Books";
        pstmt = conn.prepareStatement(sql);
        
        ResultSet rs = pstmt.executeQuery(sql);
        
        System.out.println("Books: ");
        System.out.printf(BOOK_DISPLAY_FORMAT, "BookTitle","YearPublished", "NumberPages");
        while(rs.next())
        {
            String BkTt = rs.getString("BookTitle");
            String YRPB = rs.getString("YearPublished");
            String NumP = rs.getString("NumberPages");
            
            System.out.printf(BOOK_DISPLAY_FORMAT, dispNull(BkTt), dispNull(YRPB), dispNull(NumP));
        }
        System.out.println();
        rs.close();
        pstmt.close();
    }
/////////////////// OPTION 6 ////////////////

public static void listAllDataForSpecificBook(Connection conn, PreparedStatement pstmt) throws SQLException
{
     String displayFormat = "%-30s%-40s%-14s%-20s%-30s%-15s%-13s%-40s%-50s%-16s%-40s\n"; // I'll fix formatting later (gotta fix VARCHARS in the SQL first)

        System.out.print(">>> Enter in a Book: ");
        String bookInput = in.nextLine();
        
        String sql = "SELECT * from Books natural join WritingGroups natural join Publishers where Books = ?";
        
        // create a PreparedStatement object since we have user input for our query:
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, bookInput); // 1 represents the first '?' in the sql; groupnameInput is what that '?' get replaced by
        
        // create result set: 
        ResultSet rs = pstmt.executeQuery();
        
        // print out header for table:
        line(LINE_LENGTH*3);
        System.out.printf(displayFormat, "BookTitle", "YearPublished", "NumberPages", "GroupName", "HeadWriter", "YearFormed", "Subject", "PublisherName", "PublisherAddress", "PublisherPhone", "PublisherEmail");
        line(LINE_LENGTH*3);
        
        // keeps track of the numOfRows in order to see if a row was even returned
        int numOfRows = 0; 
        
        // go through result set:
        while (rs.next()) {
            numOfRows++;
            
            // Retrieve by column name
            String BookTitle = dispNull(rs.getString("BookTitle"));
            String YearPublished = dispNull(Integer.toString(rs.getInt("YearPublished")));
            String NumberPages = dispNull(Integer.toString(rs.getInt("NumberPages")));
            String GroupName = dispNull(rs.getString("GroupName"));
            String HeadWriter = dispNull(rs.getString("HeadWriter"));
            String YearFormed = dispNull(Integer.toString(rs.getInt("YearFormed")));
            String Subject = dispNull(rs.getString("Subject"));
            String PublisherName = dispNull(rs.getString("PublisherName"));
            String PublisherAddress = dispNull(rs.getString("PublisherAddress"));
            String PublisherPhone = dispNull(rs.getString("PublisherPhone"));
            String PublisherEmail = dispNull(rs.getString("PublisherEmail"));

            // Display values
            System.out.printf(displayFormat, BookTitle, YearPublished, NumberPages, GroupName, HeadWriter, YearFormed, Subject, PublisherName, PublisherAddress, PublisherPhone, PublisherEmail);
        }
        if(numOfRows == 0) {
            System.out.println(">>> No rows were returned for 'Books' = " + bookInput);
        }
        line(LINE_LENGTH*3);
        
        // clean up environment:
        rs.close();
        pstmt.close();
        
        // DO NOT CLOSE the connection here or inside other functions (user might want to do more queries)
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
//                        listAllBooks(conn, stmt);
                        break;
                    case 6:
//                        listAllDataForSpecificBook(conn, pstmt);
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
