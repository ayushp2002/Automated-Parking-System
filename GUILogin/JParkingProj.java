/**
*
*	Automated car parking project
*	Ayush Kumar
*
*	The tables for the application have been made in Oracle as following:
*	TABLE users
*	TABLE parklot
*	TABLE booking
*	The SQL script for all the prerequiste database operations is saved in the
*	"DatabaseCommands.sql" in the same location as this file.
*
*
*/

import java.util.*;
import java.io.*;
import java.sql.*;	//for Database connectivity and operations
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.Cursor;
import javax.swing.JPasswordField;
import net.proteanit.sql.DbUtils;

class DbConnection {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;

	/**
	*	All the required query strings will be declared as constant values here
	*/
	private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
	private static final String SELECT_ALL_PARKLOT_QUERY = "SELECT lotno LotNo, isbooked Status FROM parklot";
	private static final String SELECT_USER_BOOKING_BY_UNAME_QUERY = "SELECT * FROM booking WHERE uname = ?";
	private static final String SELECT_USER_BOOKING_BY_BOOKINGNO_QUERY = "SELECT * FROM booking WHERE bookingno = ?";
	private static final String INSERT_INTO_USERS_QUERY = "INSERT INTO users VALUES (?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_BOOKING_QUERY = "INSERT INTO booking VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_PARKLOT_QUERY = "UPDATE parklot SET isbooked = ? WHERE lotno = ?";
	private static final String UPDATE_BOOKING_QUERY = "UPDATE booking SET endtime = ?, cost = ? WHERE bookingno = ?";
	private static final String UPDATE_USERS_QUERY = "UPDATE users SET phone = ?, email = ? where uname = ?";
	private static final String UPDATE_USERS_PASSWORD_QUERY = "UPDATE users SET pass = ? WHERE uname = ?";
	private static final String DELETE_USERS_QUERY = "DELETE FROM users WHERE uName = ?";

	/**
	*	Driver loader string and database connection string
	*	declared here as constants for further usage
	*/
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String CONNECTION = "jdbc:oracle:thin:@localhost:1521:XE";

	/**
	*	Constructor to establish a complete connection with the DataBase
	*	This will also create a statement object in stmt
	*/
	public DbConnection() throws SQLException, ClassNotFoundException {
		Class.forName (DRIVER);
		conn = DriverManager.getConnection(CONNECTION, "javadevgui", "password");
		stmt = conn.createStatement();
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	}
	public void closeDbConnection() throws SQLException {
		if (conn != null) {
			conn.close();
		}
		if (rs != null) {
			rs.close();
		}
		if (stmt != null) {
			stmt.close();
		}
		if (pstmt != null) {
			pstmt.close();
		}
	}
	/**
	*	Method to return all the data from the table users
	*	Since SQLException can be caught afterwards and catching here can be a challenge,
	*	it is better to throw it here rather than the try-catch approach.
	*/
	/**
	*	fetchSelectAllUsers to retrieve and return all records into a result set from the table users.
	*/
	public ResultSet fetchSelectAllUsers() {
		try {
			/**
			*	To create connection to the DataBase
			*/
			new DbConnection();
			/**
			*	Store the table data in ResultSet rs and then return it
			*/
			rs = stmt.executeQuery(SELECT_ALL_USERS_QUERY);
		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch(ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return rs;
	}

	/**
	*	fetchSelectAllParkLot() returns a ResultSet which can be then accepted into a ResultSet
	*	object to then perform operations on the ResultSet.
	*	This will be used by the ParkLot class basically to display the parklots available.
	*/
	public ResultSet fetchSelectAllParkLots() {
		try {
			/**
			*	To create connection to the DataBase
			*/
			new DbConnection();
			/**
			*	Store the table data in ResultSet rs and then return it
			*/
			rs = stmt.executeQuery(SELECT_ALL_PARKLOT_QUERY);
		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch(ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return rs;
	}

	/**
	*	These two methods are for different purposes
	*	One is for searching a user's booking history
	*	Next is for searching a user's specific booking by bookingNo
	*/
	/**
	*	Method fetchSelectUserByUNameBookings() returns a ResultSet which can then be accepted into a
	*	ResultSet object to then perform operations on the ResultSet.
	*	This will be used by the Booking class basically to display a specific user's bookings
	*	This specific operation of showing a user's booking history will be done in the
	*	showBookHistory() method.
	*/
	public ResultSet fetchSelectUserByUNameBookings(String uName) {
		try {
			/**
			*	To create connection to the DataBase
			*/
			new DbConnection();
			/**
			*	Store the table data in ResultSet rs and then return it
			*/
			pstmt = conn.prepareStatement(SELECT_USER_BOOKING_BY_UNAME_QUERY);
			pstmt.setString(1, uName);
			rs = pstmt.executeQuery();
		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch(ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return rs;
	}

	/**
	*	Method fetchSelectUserBookingsByBookingNo() returns a ResultSet which can then be accepted into a
	*	ResultSet object to then perform operations on the ResultSet.
	*	This will be used by the Booking class basically to display a specific booking of a user
	*	The records will be found out by using the bookingNo as the reference
	*	This specific operation of showing a user's booking will be done in the
	*	checkoutBooking() method.
	*/
	public ResultSet fetchSelectUserBookingsByBookingNo(String bookingNo) {
		try {
			/**
			*	To create connection to the DataBase
			*/
			new DbConnection();
			/**
			*	Store the table data in ResultSet rs and then return it
			*/
			pstmt = conn.prepareStatement(SELECT_USER_BOOKING_BY_BOOKINGNO_QUERY);

			pstmt.setString(1, bookingNo);

			rs = pstmt.executeQuery();
		} catch(SQLException SqlExcep) {
			//SqlExcep.printStackTrace();
		} catch(ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return rs;
	}


	/**
	*	Method executeUpdateParklot(int) will update the record with matching lotno field
	*	by changing the property isBooked of the selected record
	*/
	public boolean executeUpdateParklot(int updateLotNo, String updateIsBooked) {
		int updationSuccess = 0;
		try {
			/**
			*	Create a connection temporarily
			*/
			new DbConnection();

			pstmt = conn.prepareStatement(UPDATE_PARKLOT_QUERY);
			pstmt.setString(1, updateIsBooked);
			pstmt.setInt(2, updateLotNo);
			updationSuccess = pstmt.executeUpdate();

		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				this.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		if (updationSuccess > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	*	To update the booking table
	*/
	public boolean executeUpdateBooking(java.sql.Timestamp endTime, double cost, String bookingNo) {
		int updationSuccess = 0;
		try {
			/**
			*	Create a connection temporarily
			*/
			new DbConnection();

			pstmt = conn.prepareStatement(UPDATE_BOOKING_QUERY);
			pstmt.setTimestamp(1, endTime);
			pstmt.setDouble(2, cost);
			pstmt.setString(3, bookingNo);
			updationSuccess = pstmt.executeUpdate();

		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				this.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		if (updationSuccess > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	*	To update the Users table
	*/
	public boolean executeUpdateUsers(String phone, String email, String uName) {
		int updationSuccess = 0;
		try {
			/**
			*	Create a connection temporarily
			*/
			new DbConnection();

			pstmt = conn.prepareStatement(UPDATE_USERS_QUERY);
			pstmt.setString(1, phone);
			pstmt.setString(2, email);
			pstmt.setString(3, uName);
			updationSuccess = pstmt.executeUpdate();

		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				this.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		if (updationSuccess > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	*	Method to update password of a user
	*/
	public boolean executeUpdateUsersPassword(String password, String uname) {
		int updationSuccess = 0;
		try {
			/**
			*	Create a connection temporarily
			*/
			new DbConnection();

			pstmt = conn.prepareStatement(UPDATE_USERS_PASSWORD_QUERY);
			pstmt.setString(1, password);
			pstmt.setString(2, uname);
			updationSuccess = pstmt.executeUpdate();

		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				this.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		if (updationSuccess > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	*	Method executeInsertBooking(String, String, int, String, java.sql.Timestamp, java.sql.Timestamp, double)
	*	will insert a new record into the boooking system.
	*/
	public boolean executeInsertBooking(String bookingNo, String uName, int lotNo, String carNo, java.sql.Timestamp startTime, java.sql.Timestamp endTime, double cost) {
		int updationSuccess = 0;
		try {
			/**
			*	Create a connection temporarily
			*/
			new DbConnection();

			pstmt = conn.prepareStatement(INSERT_INTO_BOOKING_QUERY);

			pstmt.setString(1, bookingNo);
			pstmt.setString(2, uName);
			pstmt.setInt(3, lotNo);
			pstmt.setString(4, carNo);
			pstmt.setTimestamp(5, startTime);
			pstmt.setTimestamp(6, endTime);
			pstmt.setDouble(7, cost);

			updationSuccess = pstmt.executeUpdate();

		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				this.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		if (updationSuccess > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	*	Method executeInsertIntoUsers(String, String, String, String, String) will insert a new user
	*	record into the database table users using the insert into <table> query.
	*/
	public boolean executeInsertIntoUsers(String uName, String password, String fullName, String phone, String email) throws SQLException, ClassNotFoundException {
		int insertionSuccess = 0;
		try {
			/**
			*	Create a connection temporarily
			*/
			new DbConnection();

			pstmt = conn.prepareStatement(INSERT_INTO_USERS_QUERY);

			pstmt.setString(1, uName);
			pstmt.setString(2, password);
			pstmt.setString(3, fullName);
			pstmt.setString(4, phone);
			pstmt.setString(5, email);

			insertionSuccess = pstmt.executeUpdate();

		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {s
				this.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		if (insertionSuccess > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	*	Method to delete a user account
	*/
	public void executeDeleteUsers(String uName) {
		try {
			/**
			*	Create a connection temporarily
			*/
			new DbConnection();

			pstmt = conn.prepareStatement(DELETE_USERS_QUERY);

			pstmt.setString(1, uName);

			pstmt.executeUpdate();

		} catch(SQLException SqlExcep) {
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				this.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
	}
}

/**
*
*	Class user for storing data in an object of this class to be retrieved
*	An object of the class User will be made to perform all the operations
*	The operations are as follows:
*		-- validateLogin(String, String)
*
*/
class User {
	private String uName;
	private String password;
	private String fullName;
	private String phone;
	private String email;

	/**
	*	Constructor of class user to set default values of the fields as null
	*	This will be to identify whether an object is loaded in the reference variable or not.
	*/
	public User() {
		uName = null;
		password = null;
		fullName = null;
		phone = null;
		email = null;
	}

	/**
	*	Pass the username and password string in the validateLogin(String, String)
	*	to internally validate the Login operation done by the user.
	*	Using the database conection internally to fetch and validate data
	*/
	public boolean validateLogin(String inUName, String inPassword) {
		ResultSet loginRs = null;
		/**
		*	Since connecting to database (DbConnection()) and retrieving data (fetchSelectAllUsers())
		* throw SQLException and throwing an exception from multiple levels again and again is not
		*	a good way, so its is needed to catch the exception here.
		*/
		try {
			/**
			*	Create a connection to the database
			*/
			DbConnection validateConn = new DbConnection();
			/**
			*	Retrieve all data from users table and save it in ResultSet loginRs
			*/
			loginRs = validateConn.fetchSelectAllUsers();

			while (loginRs.next()) {
				if (inUName.equals(loginRs.getString(1)) && inPassword.equals(loginRs.getString(2))) {
					/**
					*	If we found a matching record, toggle boolean found to true and load all the data into
					*	the object which has called the method.
					*/
					uName = loginRs.getString(1);
					password = loginRs.getString(2);
					fullName = loginRs.getString(3);
					phone = loginRs.getString(4);
					email = loginRs.getString(5);
					return true;
				}
			}

		} catch(SQLException SqlExcep) {
			System.out.println("**************Error Connecting to the Database**************");
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				//DbConnection.closeDbConnection();
				if (loginRs != null) {
					loginRs.close();
				}
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return false;
	}
	public boolean isUnameExists(String inUName) {
		boolean exitsingFound = false;
		ResultSet signupRs = null;
		/**
		*	Since connecting to database (DbConnection()) and retrieving data (fetchSelectAllUsers())
		* throw SQLException and throwing an exception from multiple levels again and again is not
		*	a good way, so its is needed to catch the exception here.
		*/
		try {
			/**
			*	Create a connection to the database
			*/
			DbConnection validateConn = new DbConnection();
			/**
			*	Retrieve all data from users table and save it in ResultSet loginRs
			*/
			signupRs = validateConn.fetchSelectAllUsers();
			while (signupRs.next()) {
				if (inUName.equals(signupRs.getString(1))) {
					/**
					*	Set boolean exitsingFound true if account with the uName already exists
					*/
					exitsingFound = true;
				}
			}
		} catch(SQLException SqlExcep) {
			System.out.println("**************Error Connecting to the Database**************");
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				if (signupRs != null) {
					signupRs.close();
				}
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return exitsingFound;
	}

	/**
	*	This method only creates a user record in the database
	*/
	public boolean createUser() {
		boolean signupSuccess = false;
		/**
		*	Since connecting to database (DbConnection()) and retrieving data (fetchSelectAllUsers())
		* throw SQLException and throwing an exception from multiple levels again and again is not
		*	a good way, so its is needed to catch the exception here.
		*/
		try {
			/**
			*	Create a connection to the database
			*/
			DbConnection signupConn = new DbConnection();
			/**
			*	insert data into table
			*/
			signupSuccess = signupConn.executeInsertIntoUsers(uName, password, fullName, phone, email);


		} catch(SQLException SqlExcep) {
			System.out.println("**************Error Connecting to the Database**************");
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				//validateConn.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return signupSuccess;
	}



	/**
	*	All setter methods
	*/
	public void setUName(String inUName) {
		uName = inUName;
	}
	public void setPassword(String inPassword) {
		password = inPassword;
	}
	public void setFullName(String inFullName) {
		fullName = inFullName;
	}
	public void setPhone(String inPhone) {
		phone = inPhone;
	}
	public void setEmail(String inEmail) {
		email = inEmail;
	}

	/**
	*	All getter methods here
	*/
	public String getUName() {
		return uName;
	}
	public String getFullName() {
		return fullName;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}

	/**
	*	Show user details for showing in the profile
	*/
	public void displayDetails() {
		System.out.println("*******************Profile Details*********************");
		System.out.println("\tUsername  :\t" + uName);
		System.out.println("\tFull Name :\t" + fullName);
		System.out.println("\tPhone     :\t" + phone);
		System.out.println("\tE-Mail    :\t" + email);
	}


	/**
	*	Display method only for testing purposes
	*
	public void displayFields() {
		System.out.println("UName: " + uName);
		System.out.println("Password: " + password);
		System.out.println("FullName: " + fullName);
		System.out.println("Phone: " + phone);
		System.out.println("Email: " + email);
	}
	*/
}
class ParkLot {
	private int lotNo;
	/**
	*	boolean isBooked will be decided by the state of isbooked in the Database.
	*	isbooked in the database is a number format column because boolean is not available
	*	in oracle.
	*	isbooked (Database) = 0 means the lot is not booked (false) and vice-versa (true)
	*/
	private boolean isBooked;

	/**
	*	ResultSet parkLotRs is for fetching data from the database table parklot and then perform operations
	*	over it.
	*/
	private ResultSet parkLotRs = null;

	/**
	*	ParkLot() constructor for loading all the records from the database table (parklot).
	*	This will load all the records into an ResultSet parkLotRs and then perform any
	*	operations on it.
	*/


	public void displayLots()  {
		ResultSet displayLotsRs = null;
		/**
		*	Since connecting to database (DbConnection()) and retrieving data (fetchSelectAllParkLots())
		* throw SQLException and throwing an exception from multiple levels again and again is not
		*	a good way, so its is needed to catch the exception here.
		*/
		try {
			/**
			*	Create a connection to the database
			*/
			DbConnection displayLotsConn = new DbConnection();
			/**
			*	Retrieve all data from parklot table and save it in ResultSet displayLotsRs
			*/
			displayLotsRs = displayLotsConn.fetchSelectAllParkLots();
			System.out.println("*************PARKING LOTS*************");
			System.out.println("\tLot No.\tStatus");
			while (displayLotsRs.next()) {
				/**
				*	Display all the data one by one in this loop
				*/

				lotNo = displayLotsRs.getInt(1);

				/**
				*	Decide the state of boolean isBooke by the state of isbooked in the DataBase
				*	isbooked = "Available" => isBooked = false
				*	isbooked = "Booked" => isBooked = true
				*/
				System.out.print("\t" + lotNo + "\t");
				if (displayLotsRs.getString(2).equals("Available")) {
					isBooked = false;
					System.out.print("Available");
				} else {
					isBooked = true;
					System.out.print("Booked");
				}
				System.out.println();

			}

		} catch(SQLException SqlExcep) {
			System.out.println("**************Error Connecting to the Database**************");
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				//DbConnection.closeDbConnection();
				if (loginRs != null) {
					loginRs.close();
				}
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
	}
	/**
	*	Method checkAvailable(int) will check if the parking lot with
	*	lotNo same as the argument is available for booking or not by
	*	checking the isBooked field of the specific lot record.
	*/
	public boolean checkAvailable(int checkLotNo) {
		ResultSet validateLotRs = null;

		boolean available = false;
		/**
		*	Since connecting to database (DbConnection()) and retrieving data (fetchSelectAllParkLots())
		* throw SQLException and throwing an exception from multiple levels again and again is not
		*	a good way, so its is needed to catch the exception here.
		*/
		try {
			/**
			*	Create a connection to the database
			*/
			DbConnection validateLotConn = new DbConnection();

			validateLotRs = validateLotConn.fetchSelectAllParkLots();
			while (validateLotRs.next()) {
				if (validateLotRs.getInt(1) == checkLotNo) {
					if (validateLotRs.getString(2).equals("Available")) {
						available = true;
					}
				}
			}

		} catch(SQLException SqlExcep) {
			System.out.println("**************Error Connecting to the Database**************");
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				//DbConnection.closeDbConnection();
				if (loginRs != null) {
					loginRs.close();
				}
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return available;
	}

	/**
	*	Method bookLot(int) will update the needed record (which is passed in the argument)
	*	to make it booked.
	*	This will be achieved by changing the state of the field isBooked to true (1 in DataBase)
	*/
	public boolean bookLot() {
		boolean bookUpdateSuccess = false;
		/**
		*	Since connecting to database (DbConnection()) and updating data (executeUpdateParklot())
		* throw SQLException and throwing an exception from multiple levels again and again is not
		*	a good way, so its is needed to catch the exception here.
		*/
		try {
			/**
			*	Create a connection to the database
			*/
			DbConnection bookLotConn = new DbConnection();
			/**
			*	update the data in table
			*/
			bookUpdateSuccess = bookLotConn.executeUpdateParklot(lotNo, "Booked");


		} catch(SQLException SqlExcep) {
			System.out.println("**************Error Connecting to the Database**************");
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				//validateConn.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return bookUpdateSuccess;
	}

	/**
	*
	*/
	public boolean unbookLot() {
		boolean unbookUpdateSuccess = false;
		/**
		*	Since connecting to database (DbConnection()) and updating data (executeUpdateParklot())
		* throw SQLException and throwing an exception from multiple levels again and again is not
		*	a good way, so its is needed to catch the exception here.
		*/
		try {
			/**
			*	Create a connection to the database
			*/
			DbConnection unbookLotConn = new DbConnection();
			/**
			*	update the data in table
			*/
			unbookUpdateSuccess = unbookLotConn.executeUpdateParklot(lotNo, "Available");


		} catch(SQLException SqlExcep) {
			System.out.println("**************Error Connecting to the Database**************");
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				//validateConn.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return unbookUpdateSuccess;
	}

	/**
	*	All setter methods here
	*/
	public void setLotNo(int inLotNo) {
		lotNo = inLotNo;
	}
}
class Booking {
	private String bookingNo;
	private String uName;
	private int lotNo;
	private String carNo;
	private java.sql.Timestamp startTime;
	private java.sql.Timestamp endTime;
	private double cost;

	/**
	*	Booking() constructor which will set only the startTime and endTime default
	*/
	public Booking() {
		/**
		*	Get the number of milliSeconds from the system time and set it to the java.sql.Date
		*	object to create a booking number and also to set the start time.
		*/
		long dateTimeMillis = System.currentTimeMillis();
		/**
		*	This date is for generating String bookingNo using the date and time values
		*/
		java.util.Date date = new java.util.Date(dateTimeMillis);
		/**
		*	Set a serialized booking Number generated using the present date and time
		*	The format of this string is:
		*	DayMonthYearHourMinuteSecond
		*/
		bookingNo = "" + (date.getDay() + 1) + (date.getMonth() + 1) + (date.getYear() + 1900) + date.getHours() + date.getMinutes() + date.getSeconds();
		/**
		*	Pass the current date in sql.Timestamp object.
		*/
		startTime = new java.sql.Timestamp(date.getTime());
		/**
		*	Let it be 0 initially and then update it at the time of checkout
		*/
		endTime = new java.sql.Timestamp(0);
	}
	/**
	*	Booking(String, String, int) constructor to initialize the fields
	*/
	public Booking(String inUName, String inCarNo, int inLotNo) {
		/**
		*	Get the number of milliSeconds from the system time and set it to the java.sql.Date
		*	object to create a booking number and also to set the start time.
		*/
		long dateTimeMillis = System.currentTimeMillis();
		/**
		*	This date is for generating String bookingNo using the date and time values
		*/
		java.util.Date date = new java.util.Date(dateTimeMillis);
		/**
		*	Set a serialized booking Number generated using the present date and time
		*	The format of this string is:
		*	DayMonthYearHourMinuteSecond
		*/
		bookingNo = "" + (date.getDay() + 1) + (date.getMonth() + 1) + (date.getYear() + 1900) + date.getHours() + date.getMinutes() + date.getSeconds();
		/**
		*	Set the values of these fields as passed in the arguments
		*/
		uName = inUName;
		carNo = inCarNo;
		lotNo = inLotNo;
		cost = 0;
		/**
		*	Pass the current date in sql.Timestamp object.
		*/
		startTime = new java.sql.Timestamp(date.getTime());
		/**
		*	Let it be 0 initially and then update it at the time of checkout
		*/
		endTime = new java.sql.Timestamp(0);
	}

	/**
	*
	*/
	public boolean createBooking() {
		boolean bookUpdateSuccess = false;
		/**
		*	Since connecting to database (DbConnection()) and updating data (executeUpdateParklot())
		* throw SQLException and throwing an exception from multiple levels again and again is not
		*	a good way, so its is needed to catch the exception here.
		*/
		try {
			/**
			*	Create a connection to the database
			*/
			DbConnection createBookingConn = new DbConnection();
			/**
			*	update the data in table
			*/
			bookUpdateSuccess = createBookingConn.executeInsertBooking(bookingNo, uName, lotNo, carNo, startTime, endTime, cost);


		} catch(SQLException SqlExcep) {
			System.out.println("**************Error Connecting to the Database**************");
			SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				//validateConn.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return bookUpdateSuccess;
	}

	/**
	*
	*/
	public void showBookHistory() {
		System.out.printf("*%14s%10d%18s%28s", bookingNo, lotNo, carNo, startTime);
		if (endTime.getTime() == 0) {
			System.out.printf("%28s", "Checkout Pending");
		} else {
			System.out.printf("%28s", endTime);
		}
		System.out.printf("%15s%11s\n", "Rs." + cost, "*");
	}

	/**
	*	The method checkoutBooking() will set endTime, calculate the complete duration and
	*	hence calculate the total cost and update it in the table
	*/
	public boolean checkoutBooking() {
		Scanner scan = new Scanner(System.in);
		boolean bookUpdateSuccess = false;

		/**
		*	Set the endTime as now
		*/
		/**
		*	Get the number of milliSeconds from the system time and set it to the java.sql.Date
		*	object to create a booking number and also to set the start time.
		*/
		long dateTimeMillis = System.currentTimeMillis();
		/**
		*	This date is for generating String bookingNo using the date and time values
		*/
		java.util.Date date = new java.util.Date(dateTimeMillis);
		/**
		*	Pass the current date in sql.Timestamp object.
		*/
		endTime = new java.sql.Timestamp(date.getTime());

		/**
		*	Calculate the cost base on the time elapsed between the start time and end time
		*	durationMinutes = (difference b/w end and start time) / 1000 to convert millis to seconds / 60 to convert seconds to minutes
		*/
		long start = startTime.getTime();
		long end = endTime.getTime();
		long durationMinutes = (end - start)/60000;

		/**
		*	The cost is set as Rs. 1.27 per minuteand hence will be calulated using durationMinutes
		* The cost will be calculated according to absolute minutes and no decimals will be considered there
		*/
		cost = durationMinutes * 1.27;

		/**
		*	Since connecting to database (DbConnection()) and updating data (executeUpdateParklot())
		* throw SQLException and throwing an exception from multiple levels again and again is not
		*	a good way, so its is needed to catch the exception here.
		*/
		try {
			/**
			*	Create a connection to the database
			*/
			DbConnection checkoutBookingConn = new DbConnection();


			/**
			*	update the data in table
			*/
			bookUpdateSuccess = checkoutBookingConn.executeUpdateBooking(endTime, cost, bookingNo);


		} catch(SQLException SqlExcep) {
			System.out.println("No booking with the given booking number found.");
			//SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} finally {
			/*try {
				//validateConn.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*/
		}
		return bookUpdateSuccess;
	}

	/**
	*	All getter methods here
	*/
	public String getBookingNo() {
		return bookingNo;
	}
	public java.sql.Timestamp getStartTime() {
		return startTime;
	}

	/**
	*	All setter methods here
	*/
	public void setAllValues(String bookingNo, String uName, int lotNo, String carNo, java.sql.Timestamp startTime, java.sql.Timestamp endTime, double cost) {
		this.bookingNo = bookingNo;
		this.uName = uName;
		this.lotNo = lotNo;
		this.carNo = carNo;
		this.startTime.setTime(startTime.getTime());
		this.endTime.setTime(endTime.getTime());
		this.cost = cost;
	}

}

class LoginScreen extends JDialog {

	private JPanel contentPane;
	private JTextField textFieldUName;
	private JPasswordField passwordFieldPass;

	public String inUName;
	public String inPass;


	/**
	 * Launch the application.
	 */
	 /*
	public User createLoginWindow() throws Exception {
		EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				try {
					LoginScreen frame = new LoginScreen();
					frame.setModal(true);
					frame.setUndecorated(true);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return loginUser;
	}
	*/
	/**
	 * Create the frame.
	 */
	public LoginScreen(User activeUsr) throws Exception {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 646, 257);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 51, 51));
		panel.setBounds(0, 0, 219, 261);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblLoader = new JLabel("");
		lblLoader.setIcon(new ImageIcon(CreateAccount.class.getResource("/img/ajax-loader.gif")));
		lblLoader.setBounds(580, 190, 50, 50);
		contentPane.add(lblLoader);
		lblLoader.setVisible(false);

		JLabel lblNewLabel_2 = new JLabel("Author: Ayush Kumar");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_2.setForeground(new Color(192, 192, 192));
		lblNewLabel_2.setBounds(62, 201, 113, 14);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel = new JLabel("LOGIN");
		lblNewLabel.setForeground(new Color(204, 255, 153));
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
		lblNewLabel.setBounds(79, 25, 57, 23);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_3 = new JLabel("Username: ");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setBounds(10, 78, 67, 14);
		panel.add(lblNewLabel_3);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPassword.setBounds(10, 103, 67, 14);
		panel.add(lblPassword);

		textFieldUName = new JTextField();
		textFieldUName.setBounds(79, 76, 130, 20);
		panel.add(textFieldUName);
		textFieldUName.setColumns(10);

		passwordFieldPass = new JPasswordField();
		passwordFieldPass.setBounds(79, 101, 130, 20);
		panel.add(passwordFieldPass);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblLoader.setVisible(true);
				inUName = textFieldUName.getText();
				inPass = passwordFieldPass.getText();
				activeUsr.validateLogin(inUName, inPass);
				System.out.println("LOGIN");
				dispose();
			}
		});
		btnLogin.setBounds(109, 146, 89, 23);
		panel.add(btnLogin);

		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblLoader.setVisible(true);
				try {
					JParkingProj.createNewAccountWindow(activeUsr);
					System.out.println("SIGNUP");
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSignUp.setBounds(10, 146, 89, 23);
		panel.add(btnSignUp);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(216, 0, 432, 261);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblBookingSystem = new JLabel("Booking System");
		lblBookingSystem.setBounds(154, 39, 130, 36);
		panel_1.add(lblBookingSystem);
		lblBookingSystem.setForeground(new Color(192, 192, 192));
		lblBookingSystem.setFont(new Font("Segoe UI", Font.BOLD, 17));
		lblBookingSystem.setBackground(Color.WHITE);

		JLabel lblNewLabel_1 = new JLabel("Car Parking");
		lblNewLabel_1.setBounds(173, 11, 94, 36);
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.BOLD, 17));
		lblNewLabel_1.setBackground(Color.WHITE);
		lblNewLabel_1.setForeground(new Color(192, 192, 192));

		JLabel lblClose = new JLabel("X");
		lblClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
		lblClose.setForeground(new Color(255, 0, 0));
		lblClose.setFont(new Font("Segoe UI Black", Font.PLAIN, 25));
		lblClose.setBounds(404, 0, 18, 29);
		panel_1.add(lblClose);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(LoginScreen.class.getResource("/img/Splash.jpg")));
		label.setBackground(Color.DARK_GRAY);
		label.setBounds(0, 0, 434, 261);
		panel_1.add(label);
	}
}
class CreateAccount extends JDialog {

	private JPanel contentPane;
	private JTextField textFieldFullName;
	private JTextField textFieldUName;
	private JTextField textFieldEmail;
	private JTextField textFieldPhone;
	private JPasswordField passwordFieldPass;

	/**
	 * Create the frame.
	 */
	public CreateAccount(User newUser) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 885, 350);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(51, 51, 51));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 488, 366);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblLoader = new JLabel("");
		lblLoader.setIcon(new ImageIcon(CreateAccount.class.getResource("/img/ajax-loader.gif")));
		lblLoader.setBounds(809, 272, 50, 50);
		contentPane.add(lblLoader);
		lblLoader.setVisible(false);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(CreateAccount.class.getResource("/img/Splash2.jpg")));
		lblNewLabel.setBackground(Color.DARK_GRAY);
		lblNewLabel.setBounds(0, 0, 488, 365);
		panel.add(lblNewLabel);

		JButton btnNewButton = new JButton("Sign Up");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblLoader.setVisible(true);
				newUser.setFullName(textFieldFullName.getText());
				newUser.setUName(textFieldUName.getText());
				newUser.setEmail(textFieldEmail.getText());
				newUser.setPhone(textFieldPhone.getText());
				newUser.setPassword(passwordFieldPass.getText());
				newUser.createUser();
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnNewButton.setBounds(611, 275, 131, 25);
		contentPane.add(btnNewButton);

		JLabel lblNewLabel_1 = new JLabel("CREATE ACCOUNT");
		lblNewLabel_1.setFont(new Font("Segoe UI Black", Font.BOLD, 22));
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setBounds(514, 28, 214, 25);
		contentPane.add(lblNewLabel_1);

		JLabel lblFullName = new JLabel("Full Name:");
		lblFullName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblFullName.setForeground(Color.WHITE);
		lblFullName.setBounds(514, 91, 68, 14);
		contentPane.add(lblFullName);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblUsername.setBounds(514, 120, 68, 14);
		contentPane.add(lblUsername);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblEmail.setBounds(514, 149, 68, 14);
		contentPane.add(lblEmail);

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setForeground(Color.WHITE);
		lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPhone.setBounds(514, 178, 68, 14);
		contentPane.add(lblPhone);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPassword.setBounds(514, 207, 68, 14);
		contentPane.add(lblPassword);

		JLabel lblClose = new JLabel("X");
		lblClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
		lblClose.setForeground(Color.RED);
		lblClose.setFont(new Font("Segoe UI Black", Font.BOLD, 25));
		lblClose.setBounds(850, 11, 19, 25);
		contentPane.add(lblClose);

		textFieldFullName = new JTextField();
		textFieldFullName.setBounds(592, 89, 267, 20);
		contentPane.add(textFieldFullName);
		textFieldFullName.setColumns(10);

		textFieldUName = new JTextField();
		textFieldUName.setColumns(10);
		textFieldUName.setBounds(592, 118, 267, 20);
		contentPane.add(textFieldUName);

		textFieldEmail = new JTextField();
		textFieldEmail.setColumns(10);
		textFieldEmail.setBounds(592, 147, 267, 20);
		contentPane.add(textFieldEmail);

		textFieldPhone = new JTextField();
		textFieldPhone.setColumns(10);
		textFieldPhone.setBounds(592, 176, 267, 20);
		contentPane.add(textFieldPhone);

		passwordFieldPass = new JPasswordField();
		passwordFieldPass.setBounds(592, 205, 267, 20);
		contentPane.add(passwordFieldPass);
	}
}
class AcccountSettings extends JDialog {

	private JPanel contentPane;
	private JTextField textFieldUName;
	private JTextField textFieldPhone;
	private JTextField textFieldEmail;
	private JPasswordField passwordFieldPass;


	/**
	 * Create the frame.
	 */
	public AcccountSettings(User account) {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 229);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Account Settings");
		lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
		lblNewLabel.setBounds(146, 11, 151, 20);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 54, 70, 14);
		contentPane.add(lblNewLabel_1);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPhone.setBounds(10, 79, 70, 14);
		contentPane.add(lblPhone);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEmail.setBounds(10, 104, 70, 14);
		contentPane.add(lblEmail);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(10, 129, 70, 14);
		contentPane.add(lblPassword);

		textFieldUName = new JTextField();
		textFieldUName.setText(account.getUName());
		textFieldUName.setEditable(false);
		textFieldUName.setBounds(90, 53, 344, 20);
		contentPane.add(textFieldUName);
		textFieldUName.setColumns(10);

		textFieldPhone = new JTextField();
		textFieldPhone.setText(account.getPhone());
		textFieldPhone.setColumns(10);
		textFieldPhone.setBounds(90, 78, 344, 20);
		contentPane.add(textFieldPhone);

		textFieldEmail = new JTextField();
		textFieldEmail.setText(account.getEmail());
		textFieldEmail.setColumns(10);
		textFieldEmail.setBounds(90, 103, 344, 20);
		contentPane.add(textFieldEmail);

		passwordFieldPass = new JPasswordField();
		passwordFieldPass.setBounds(90, 128, 344, 20);
		contentPane.add(passwordFieldPass);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textFieldEmail.getText().equals("") || textFieldPhone.getText().contentEquals("")  || passwordFieldPass.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please fill all the fields");
				} else {
					try {
						DbConnection updateConn = new DbConnection();
						updateConn.executeUpdateUsers(textFieldPhone.getText(), textFieldEmail.getText(), textFieldUName.getText());
						updateConn.executeUpdateUsersPassword(passwordFieldPass.getText(), textFieldUName.getText());
						JOptionPane.showMessageDialog(null, "Updated Successfully.");
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnUpdate.setBounds(289, 159, 119, 30);
		contentPane.add(btnUpdate);

		JButton btnDeleteAccount = new JButton("Delete Account");
		btnDeleteAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int conf = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account?\nOnce deleted cannot be recovered.", "Confirmation", JOptionPane.YES_NO_OPTION);
				if (conf == 0) {
					try {
						DbConnection deleteConn = new DbConnection();
						deleteConn.executeDeleteUsers(account.getUName());
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Deletion cancelled.");
				}
			}
		});
		btnDeleteAccount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDeleteAccount.setBounds(90, 159, 151, 30);
		contentPane.add(btnDeleteAccount);
	}
}
class MainMenu extends JDialog {

	private JPanel contentPane;
	private JTable tableHistory;
	private JTextField txtAutoGeneratedBooking;
	private JTextField txtSelectedLotNo;
	private JTextField txtUsersFullName;
	private JTextField txtUserEnteredCar;
	private JTextField txtDateTimeNow;
	private JTable tableBook;
	private JTable tableCheckout;

	Booking newBook = null;
	ParkLot newLot = null;

	/**
	 * Create the frame.
	 */
	public MainMenu(User account) throws Exception {
		DbConnection tableConn = new DbConnection();
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 614, 398);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelUserMenu = new JPanel();
		panelUserMenu.setBounds(474, 36, 123, 39);
		contentPane.add(panelUserMenu);
		panelUserMenu.setLayout(null);
		panelUserMenu.setVisible(false);

		JButton btnNewButton = new JButton("Profile Settings");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Click Profile Details
				try {
					JParkingProj.createSettings(account);
				} catch(Exception execp) {
					execp.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(0, 0, 123, 20);
		panelUserMenu.add(btnNewButton);

		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Click Logout
				dispose();
			}
		});
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnLogout.setBounds(0, 19, 123, 20);
		panelUserMenu.add(btnLogout);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setBounds(10, 61, 587, 298);
		contentPane.add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Book Now", null, panel, null);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Booking No.");
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNewLabel.setBounds(232, 54, 93, 18);
		panel.add(lblNewLabel);

		JLabel lblBookingNo = new JLabel("Lot No.");
		lblBookingNo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblBookingNo.setBounds(232, 85, 93, 18);
		panel.add(lblBookingNo);

		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblName.setBounds(232, 114, 93, 18);
		panel.add(lblName);

		JLabel lblCarNo = new JLabel("Car No.");
		lblCarNo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblCarNo.setBounds(232, 143, 93, 18);
		panel.add(lblCarNo);

		JLabel lblStartDatetime = new JLabel("Start Date-Time");
		lblStartDatetime.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblStartDatetime.setBounds(232, 172, 93, 18);
		panel.add(lblStartDatetime);

		txtAutoGeneratedBooking = new JTextField();
		txtAutoGeneratedBooking.setText("");
		txtAutoGeneratedBooking.setEditable(false);
		txtAutoGeneratedBooking.setBounds(335, 54, 240, 20);
		panel.add(txtAutoGeneratedBooking);
		txtAutoGeneratedBooking.setColumns(10);

		txtSelectedLotNo = new JTextField();
		txtSelectedLotNo.setEditable(false);
		txtSelectedLotNo.setText("");
		txtSelectedLotNo.setColumns(10);
		txtSelectedLotNo.setBounds(335, 85, 240, 20);
		panel.add(txtSelectedLotNo);

		txtUsersFullName = new JTextField();
		txtUsersFullName.setText("");
		txtUsersFullName.setEditable(false);
		txtUsersFullName.setColumns(10);
		txtUsersFullName.setBounds(335, 114, 240, 20);
		panel.add(txtUsersFullName);

		txtUserEnteredCar = new JTextField();
		txtUserEnteredCar.setText("");
		txtUserEnteredCar.setColumns(10);
		txtUserEnteredCar.setBounds(335, 143, 240, 20);
		panel.add(txtUserEnteredCar);

		txtDateTimeNow = new JTextField();
		txtDateTimeNow.setText("");
		txtDateTimeNow.setEditable(false);
		txtDateTimeNow.setColumns(10);
		txtDateTimeNow.setBounds(335, 172, 240, 20);
		panel.add(txtDateTimeNow);

		JScrollPane scrollPaneBook = new JScrollPane();
		scrollPaneBook.setBounds(10, 11, 212, 248);
		panel.add(scrollPaneBook);

		tableBook = new JTable();
		tableBook.setDefaultEditor(Object.class, null);
		ResultSet parkLotsRs = tableConn.fetchSelectAllParkLots();
		tableBook.setModel(DbUtils.resultSetToTableModel(parkLotsRs));
		tableBook.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				newBook = new Booking();
				newLot = new ParkLot();
				final JTable jTable= (JTable)arg0.getSource();
	      final int row = jTable.getSelectedRow();
	      final String valueInCell = jTable.getValueAt(row, 0).toString();
				txtSelectedLotNo.setText(valueInCell);
				txtUsersFullName.setText(account.getFullName());
				txtDateTimeNow.setText(newBook.getStartTime().toString());
				txtAutoGeneratedBooking.setText(newBook.getBookingNo().toString());
				newLot.setLotNo(Integer.parseInt(valueInCell));
			}
		});
		scrollPaneBook.setViewportView(tableBook);

		JLabel lblNewLabel_1 = new JLabel("Book Now");
		lblNewLabel_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 21));
		lblNewLabel_1.setBounds(232, 12, 101, 28);
		panel.add(lblNewLabel_1);


		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Click ConfirmBooking
				if (!txtSelectedLotNo.getText().equals("")) {	//if no lot no is selected
					if (newLot.checkAvailable(Integer.parseInt(txtSelectedLotNo.getText()))) {
						if (!txtUserEnteredCar.getText().equals("")) {
							String bookingNo = txtAutoGeneratedBooking.getText();
							String uName = account.getUName();
							int lotNo = Integer.parseInt(txtSelectedLotNo.getText());
							String carNo = txtUserEnteredCar.getText();
							Timestamp startTime = Timestamp.valueOf(txtDateTimeNow.getText());
							Timestamp endTime = new Timestamp(0);
							double cost = 0.0;
							int conf = JOptionPane.showConfirmDialog(null, "Are you sure to book with these details?\nBooking No:  " + bookingNo + "\nLot No:           " + lotNo + "\nCar no:           " + carNo + "\nStart Time:   " + startTime.toString(), "Confirmation", JOptionPane.YES_NO_OPTION);
							if (conf == 0) {	//if yes
								newBook.setAllValues(bookingNo, uName, lotNo, carNo, startTime, endTime, cost);

								boolean lotSuccess = newLot.bookLot();
								boolean bookSuccess = newBook.createBooking();
								if (bookSuccess) {
									JOptionPane.showMessageDialog(null, "Successfully booked parking.");
								} else {
									JOptionPane.showMessageDialog(null, "Couldn't book parking");
								}
							} else {
								txtDateTimeNow.setText("");
								txtSelectedLotNo.setText("");
								txtUsersFullName.setText("");
								txtAutoGeneratedBooking.setText("");
								txtUserEnteredCar.setText("");
							}
						} else {
							JOptionPane.showMessageDialog(null, "Please enter a Car Identification Number.");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Lot no " + Integer.parseInt(txtSelectedLotNo.getText()) + " is Booked.\nPlease select another one.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please select Parking lot from the table.");
				}

			}
		});
		btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnConfirm.setBounds(254, 224, 135, 35);
		panel.add(btnConfirm);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Click ResetBooking
				txtDateTimeNow.setText("");
				txtSelectedLotNo.setText("");
				txtUsersFullName.setText("");
				txtAutoGeneratedBooking.setText("");
				txtUserEnteredCar.setText("");
			}
		});
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnReset.setBounds(420, 224, 135, 35);
		panel.add(btnReset);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Checkout", null, panel_1, null);
		panel_1.setLayout(null);

		JScrollPane scrollPaneCheckout = new JScrollPane();
		scrollPaneCheckout.setBounds(10, 50, 562, 163);
		panel_1.add(scrollPaneCheckout);

		tableCheckout = new JTable();
		tableCheckout.setDefaultEditor(Object.class, null);
		ResultSet checkoutRs = tableConn.fetchSelectUserByUNameBookings(account.getUName());
		tableCheckout.setModel(DbUtils.resultSetToTableModel(checkoutRs));
		scrollPaneCheckout.setViewportView(tableCheckout);

		JButton btnCheckout = new JButton("Pay and Checkout");
		btnCheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//click Pay and checkout

	      final int row = tableCheckout.getSelectedRow();
	      final String valueInCell = tableCheckout.getValueAt(row, 0).toString();
				ParkLot unbookOldLot = new ParkLot();
				/**
				*	This object of the class Booking will be used to fetch the booking that is being cancelled
				*/
				Booking bookObj = new Booking();

				ResultSet rs = null;

				String bookingNo;
				String name;
				int lotNo = 0;
				String carNo;
				java.sql.Timestamp startTime;
				java.sql.Timestamp endTime;
				int cost;

				/**
				*	Since connecting to database (DbConnection()) and updating data (executeUpdateParklot())
				* throw SQLException and throwing an exception from multiple levels again and again is not
				*	a good way, so its is needed to catch the exception here.
				*/
				try {
					/**
					*	Create a connection to the database
					*/
					DbConnection checkoutConn = new DbConnection();

					rs = checkoutConn.fetchSelectUserBookingsByBookingNo(valueInCell);

					rs.next();

					bookingNo = rs.getString(1);
					name = rs.getString(2);
				 	lotNo = rs.getInt(3);
					carNo = rs.getString(4);
					startTime = rs.getTimestamp(5);
					endTime = rs.getTimestamp(6);
					cost = rs.getInt(7);
					bookObj.setAllValues(bookingNo, name, lotNo, carNo, startTime, endTime, cost);
					int conf = JOptionPane.showConfirmDialog(null, "Are you sure to checkout with these details?\nBooking No:  " + bookingNo + "\nLot No:           " + lotNo + "\nCar no:           " + carNo + "\nStart Time:   " + startTime.toString() + "\nEnd Time: " + endTime.toString(), "Confirmation", JOptionPane.YES_NO_OPTION);
					if (conf == 0) {
						unbookOldLot.setLotNo(lotNo);
						boolean lotUnbookSuccess = unbookOldLot.unbookLot();
					}
					if (!bookObj.checkoutBooking()) {
						JOptionPane.showMessageDialog(null, "Failed to checkout");
					} else  {
						JOptionPane.showMessageDialog(null, "Successfully Checked out");
					}

				} catch(SQLException SqlExcep) {
					System.out.println("**************Error Connecting to the Database**************");
					SqlExcep.printStackTrace();
				} catch (ClassNotFoundException cnfExecp) {
					cnfExecp.printStackTrace();
				} finally {
					/*try {
						//validateConn.closeDbConnection();
					} catch(SQLException SqlExcep) {
						SqlExcep.printStackTrace();
					}*/
				}



			}
		});
		btnCheckout.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCheckout.setBounds(215, 224, 151, 35);
		panel_1.add(btnCheckout);

		JLabel lblCheckout = new JLabel("Checkout");
		lblCheckout.setFont(new Font("Segoe UI Semibold", Font.BOLD, 21));
		lblCheckout.setBounds(10, 11, 101, 28);
		panel_1.add(lblCheckout);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Booking History", null, panel_2, null);
		panel_2.setLayout(null);

		JScrollPane scrollPaneHistory = new JScrollPane();
		scrollPaneHistory.setBounds(10, 50, 562, 209);
		panel_2.add(scrollPaneHistory);

		tableHistory = new JTable();
		tableHistory.setDefaultEditor(Object.class, null);
		ResultSet historyRs = tableConn.fetchSelectUserByUNameBookings(account.getUName());
		tableHistory.setModel(DbUtils.resultSetToTableModel(historyRs));
		scrollPaneHistory.setViewportView(tableHistory);
		tableHistory.setColumnSelectionAllowed(true);
		tableHistory.setCellSelectionEnabled(true);

		JLabel lblBookingHistory = new JLabel("Booking History");
		lblBookingHistory.setFont(new Font("Segoe UI Semibold", Font.BOLD, 21));
		lblBookingHistory.setBounds(10, 11, 155, 28);
		panel_2.add(lblBookingHistory);

		JLabel lblNewLabel_3 = new JLabel("MAIN MENU");
		lblNewLabel_3.setFont(new Font("Segoe UI Black", Font.BOLD, 22));
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setBounds(10, 2, 148, 39);
		contentPane.add(lblNewLabel_3);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.GRAY);
		panel_3.setBounds(0, 0, 608, 41);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JLabel lblWelcome = new JLabel("Welcome " + account.getFullName());
		lblWelcome.setBounds(342, 10, 232, 22);

		panel_3.add(lblWelcome);
		lblWelcome.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblWelcome.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 15));
		lblWelcome.setForeground(Color.WHITE);

		JButton btnUserMenu = new JButton("");
		btnUserMenu.setIcon(new ImageIcon(MainMenu.class.getResource("/img/DropDown.png")));
		btnUserMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (panelUserMenu.isVisible()) {
					panelUserMenu.setVisible(false);
				} else {
					panelUserMenu.setVisible(true);
				}
			}
		});
		btnUserMenu.setBounds(577, 12, 20, 20);
		panel_3.add(btnUserMenu);

		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(MainMenu.class.getResource("/img/MainMenu.jpg")));
		lblBackground.setBounds(0, 0, 617, 370);
		contentPane.add(lblBackground);

		JButton btnRefreshBook = new JButton("Refresh Data");
		btnRefreshBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResultSet parkLotsRs = tableConn.fetchSelectAllParkLots();
				tableBook.setModel(DbUtils.resultSetToTableModel(parkLotsRs));
			}
		});
		btnRefreshBook.setBounds(460, 20, 115, 23);
		panel.add(btnRefreshBook);

		JButton btnRefreshCheckout = new JButton("Refresh Data");
		btnRefreshCheckout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResultSet checkoutRs = tableConn.fetchSelectUserByUNameBookings(account.getUName());
				tableCheckout.setModel(DbUtils.resultSetToTableModel(checkoutRs));
			}
		});
		btnRefreshCheckout.setBounds(460, 19, 115, 23);
		panel_1.add(btnRefreshCheckout);

		JButton btnRefreshHistory = new JButton("Refresh Data");
		btnRefreshHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResultSet historyRs = tableConn.fetchSelectUserByUNameBookings(account.getUName());
				tableHistory.setModel(DbUtils.resultSetToTableModel(historyRs));
			}
		});
		btnRefreshHistory.setBounds(460, 16, 115, 23);
		panel_2.add(btnRefreshHistory);

	}
}



public class JParkingProj {

	/**
	*	This is to solve the problem of scanner input
	*/
	static boolean createAttempt = false;

	/**
	*	method for creating a delay before the next action
	*/
	public static void createDelay(int duration) {
		try {
			Thread.sleep(duration);
		} catch(Exception e) {
			//do nothing
		}
	}
	/**
	*	Methods for clearing the screen
	* Method cls() is for clearing the screen
	*	Method clearScreen() will implement createDelay() and cls() to wait and clear screen
	*	with the "Processing..." message showing
	*/
	public static void cls() {
		try {
			new ProcessBuilder ("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public static void clearScreen() {
		/**
		*	Wait for some time before clearing the screen so that the user
		*	read the outputs
		*/
		createDelay(700);
		cls();
		System.out.println("\nProcessing...");
		createDelay(500);
		cls();
	}

	/**
	*	All operations to be performed in the methods here
	*/
	static void createLoginWindow(User loginUser) throws Exception {
		EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				try {
					LoginScreen frame = new LoginScreen(loginUser);
					frame.setModal(true);
					frame.setUndecorated(true);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	static void createNewAccountWindow(User newUser) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateAccount frame = new CreateAccount(newUser);
					frame.setModal(true);
					frame.setUndecorated(true);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	static void createMainMenu(User account) throws Exception {
		EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu(account);
					frame.setModal(true);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	static public void createSettings(User account) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AcccountSettings frame = new AcccountSettings(account);
					frame.setModal(true);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/*
	static void presentBookingHistory(User booker) {
		Booking bookings = new Booking();
		ResultSet historyRs = null;

		System.out.printf("*%14s%10s%18s%28s%28s%15s%11s\n", "Booking No", "Lot No", "Car No", "Start Time", "End Time", "Cost", "*");
		System.out.println("*****************************************************************************************************************************");
		try {
			DbConnection historyConn = new DbConnection();
			historyRs = historyConn.fetchSelectUserByUNameBookings(booker.getUName());
			while (historyRs.next()) {
				java.sql.Timestamp endTime = historyRs.getTimestamp(6);
				/**
				*	To show only the ones which are not checked out yet
				*	If a record with end time as 0 is found, it will be considered as not booked and shown
				*
				if (endTime.getTime() != 0) {
					continue;
				}
				String bookingNo = historyRs.getString(1);
				int lotNo = historyRs.getInt(3);
				String carNo = historyRs.getString(4);
				java.sql.Timestamp startTime = historyRs.getTimestamp(5);
				int cost = historyRs.getInt(7);
				bookings.setAllValues(bookingNo, lotNo, carNo, startTime, endTime, cost);

				bookings.showBookHistory();
			}
		} catch(SQLException SqlExcep) {
			System.out.println("No records with the given booking number found");
			//SqlExcep.printStackTrace();
		} catch (ClassNotFoundException cnfExecp) {
			cnfExecp.printStackTrace();
		} catch (NullPointerException npExcep) {
			npExcep.printStackTrace();
		} finally {
			/*try {
				//validateConn.closeDbConnection();
			} catch(SQLException SqlExcep) {
				SqlExcep.printStackTrace();
			}*
		}
	}
	*/

	/**
	*	Main
	*/

	public static void main(String[] args) throws Exception {

		/*
		*	Show the user a menu here and do the operations by calling the
		*	methods in the public class which internally call the ther methods
		* and use the classes.
		*	Showing menu only once to remove the clutter problem from the screen
		*/

		int opt;

		do {
			User activeUser = new User();
			/**
					*	Try to validate login credentials and proceed accordingly.
					*/
			createLoginWindow(activeUser);
			if (activeUser.getUName() != null) {
				JOptionPane.showMessageDialog(null, "Login Successfull!");
			} else {
						/**
						*	If the login fails, break and start over again
						*/
						JOptionPane.showMessageDialog(null, "Login Failed!");
						continue;
					}
			/**
					*	This part will be execute only if the login is Successfull else a break operation is performed
					*	and the application will go back to the main menu.
					*/
					//System.out.println("User Operations");
			//activeUser.displayFields();

			createMainMenu(activeUser);

		} while (true);

	}
}
