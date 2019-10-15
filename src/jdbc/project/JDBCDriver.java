
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

        Connection conn = null; //initialize the connection
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
//                        listAllDataForSpecificGroup(conn, pstmt);
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
