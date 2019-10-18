
package jdbc.project;
import java.sql.*;
import java.util.Scanner;

public class JDBCDriver {
    // LINE_LENGTH controls how many '-' are printed
    static final int LINE_LENGTH = 100;
    
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
    
    //////// Put all required functions below here (but before main() obvsly) //////////
    
    public static void listAllWritingGroups()
    {
        System.out.println();
    }
    
    public static void listAllDataForSpecificGroup(Connection conn, PreparedStatement pstmt) throws SQLException{
        String displayFormat = "%-30s%-40s%-14s%-20s%-30s%-15s%-13s%-40s%-50s%-16s%-40s\n"; // I'll fix formatting later (gotta fix VARCHARS in the SQL first)

        System.out.print(">>> Enter in a GROUP NAME: ");
        String groupnameInput = in.nextLine();
        
        String sql = "SELECT * from Books natural join Publishers natural join WritingGroups where groupname = ?";
        
        // create a PreparedStatement object since we have user input for our query:
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, groupnameInput); // 1 represents the first '?' in the sql; groupnameInput is what that '?' get replaced by
        
        // create result set: 
        ResultSet rs = pstmt.executeQuery();
        
        // print out header for table:
        line(LINE_LENGTH*3);
        System.out.printf(displayFormat, "GroupName", "HeadWriter", "YearFormed", "Subject", "BookTitle", "YearPublished", "NumberPages", "PublisherName", "PublisherAddress", "PublisherPhone", "PublisherEmail");
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
            System.out.printf(displayFormat, groupname, headwriter, yearformed, subject, booktitle, yearpublished, numberpages, publishername, publisheraddress, publisherphone, publisheremail);
        }
        if(numOfRows == 0) {
            System.out.println(">>> No rows were returned for 'GROUP NAME'=" + groupnameInput);
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
                     
//                        listAllWritingGroups(conn, stmt);
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
//                        listAllPublishers(conn, stmt);
                        break;
                    case 4:
//                        listAllDataForSpecificPublisher(conn, pstmt);
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
