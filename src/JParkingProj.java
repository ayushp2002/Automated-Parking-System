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



class DbConnection {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;

	/**
	*	All the required query strings will be declared as constant values here
	*/
	private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
	private static final String SELECT_ALL_PARKLOT_QUERY = "SELECT * FROM parklot";
	private static final String SELECT_USER_BOOKING_BY_UNAME_QUERY = "SELECT * FROM booking WHERE uname = ?";
	private static final String SELECT_USER_BOOKING_BY_BOOKINGNO_QUERY = "SELECT * FROM booking WHERE bookingno = ?";
	private static final String INSERT_INTO_USERS_QUERY = "INSERT INTO users VALUES (?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_BOOKING_QUERY = "INSERT INTO booking VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_PARKLOT_QUERY = "UPDATE parklot SET isbooked = ? WHERE lotno = ?";
	private static final String UPDATE_BOOKING_QUERY = "UPDATE booking SET endtime = ?, cost = ? WHERE bookingno = ?";
	private static final String UPDATE_USERS_QUERY = "UPDATE users SET phone = ?, email = ? where uname = ?";
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
		conn = DriverManager.getConnection(CONNECTION, "JavaDev", "password");
		stmt = conn.createStatement();
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
	public boolean executeUpdateParklot(int updateLotNo, int updateIsBooked) {
		int updationSuccess = 0;
		try {
			/**
			*	Create a connection temporarily
			*/
			new DbConnection();

			pstmt = conn.prepareStatement(UPDATE_PARKLOT_QUERY);
			pstmt.setInt(1, updateIsBooked);
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
	public void validateLogin(String inUName, String inPassword) {
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
				*	isbooked = 0 => isBooked = false
				*	isbooked = 1 => isBooked = true
				*/
				System.out.print("\t" + lotNo + "\t");
				if (displayLotsRs.getInt(2) == 0) {
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
					if (validateLotRs.getInt(2) == 0) {
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
			bookUpdateSuccess = bookLotConn.executeUpdateParklot(lotNo, 1);


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
			unbookUpdateSuccess = unbookLotConn.executeUpdateParklot(lotNo, 0);


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
		*	Show the cost and confirm to checkout.
		*/
		System.out.print("\n\tTotal cost calculated: " + cost + "\n\n\t1. Confirm Checkout\n\t2. Cancel\n\t\t>> ");
		int opt = scan.nextInt();

		switch (opt) {
			case 1: {	//confirm checkout
				//do nothing and move ahead towards the checkout procedure
				break;
			}
			case 2: {	//Cancel
				return false;
			}
			default: {
				System.out.println("Invalid Option");
				return false;
			}
		}

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
	public void setAllValues(String bookingNo, int lotNo, String carNo, java.sql.Timestamp startTime, java.sql.Timestamp endTime, double cost) {
		this.bookingNo = bookingNo;
		this.lotNo = lotNo;
		this.carNo = carNo;
		this.startTime.setTime(startTime.getTime());
		this.endTime.setTime(endTime.getTime());
		this.cost = cost;
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

	static User Login() {
		User loginUser = new User();
		Scanner scan = new Scanner(System.in);

		System.out.print("Username: ");
		String inUName = scan.next();
		System.out.print("Password: ");
		scan.nextLine();
		String inPass = scan.nextLine();
			/**
			*	In case of correct credentials return true for further actions in the main method.
			*	In case of incorrect credentials, return false
			*/
		loginUser.validateLogin(inUName, inPass);
		return loginUser;

	}
	static boolean createAccount() {
		User signupUser = new User();
		Scanner scan = new Scanner(System.in);

		System.out.println("Enter deatils for the new account.");
		System.out.print("Full Name: ");
		if (createAttempt) {
			scan.nextLine();
		}
		signupUser.setFullName(scan.nextLine());

		System.out.print("Username: ");
		String inUName = scan.next();

		if (signupUser.isUnameExists(inUName) == true) {
			/**
			*	If an existing username same as the one entered is found,
			*	show error and redirect to the main menu.
			*/
			System.out.println("The username " + inUName + " is already taken. Please try another one.");
			createAttempt = true;
			return false;
		} else {
			signupUser.setUName(inUName);
			createAttempt = false;
		}

		System.out.print("E-Mail: ");
		signupUser.setEmail(scan.next());

		System.out.print("Phone: ");
		signupUser.setPhone(scan.next());

		System.out.print("Password: ");
		scan.nextLine();
		signupUser.setPassword(scan.nextLine());

		/**
		*	createUser() will create a new user in the database
		*	createUser() retuens a boolean value depicting the success of insertion operation.
		*/
		return signupUser.createUser();
	}
	static boolean bookParking(User booker) {
		Scanner scan = new Scanner(System.in);
		ParkLot bookNewLot = new ParkLot();

		bookNewLot.displayLots();
		System.out.print("\nEnter a parking lot number to book: ");
		int bookLotNo = scan.nextInt();

		if (bookNewLot.checkAvailable(bookLotNo)) {
			/**
			*	If the requested lot number is available, show success and,
			* start the updation procedure by calling the required method through
			* the object bookLot.
			*/
			System.out.println("Parking Lot Number "+ bookLotNo + " is available for booking.");
		} else {
			/**
			*	If the requested lot number is not available then,
			*	show error and go back to the main menu
			*/
			System.out.println("Parking Lot Number "+ bookLotNo + " is not available for booking.\nPlease try another Lot Number");
			return false;
		}

		/**
		*	Anything below this will execute only if the requested lot number is available
		* if the lot number is not available, the method will return false above
		*/
		System.out.print("Enter car identification Number: ");
		String inCarNo = scan.next();

		/**
		*	Create a booking object with the passed details as arguments
		*/
		Booking newBook = new Booking(booker.getUName(), inCarNo, bookLotNo);

		/**
		*	Show the user all the details to confirm te booking
		*/
		System.out.println("*******************CONFIRM BOOKING*******************");
		System.out.println("\tBooking Number:\t" + newBook.getBookingNo());
		System.out.println("\tName:\t\t" + booker.getFullName());
		System.out.println("\tLot Number:\t" + bookLotNo);
		System.out.println("\tCar Number:\t" + inCarNo);
		System.out.println("\tStart Date-Time:\t" + newBook.getStartTime());

		System.out.print("\n\t1. Confirm Booking\n\t2. Cancel\n     >> ");
		int bookMenuOpt = scan.nextInt();

		switch (bookMenuOpt) {
			case 1: {
				//Do nothing and move ahead
				break;
			}/*
			case 2: {	//edit details
				/**
				*	Go back to the start of the method
				*
				clearScreen();
				bookParking(booker);
				break;
			}*/
			case 2: {
				//Do nothing and go back to the main menu
				return false;
			}
		}
		/**
		*	Set the value of lotNo of the object
		*/
		bookNewLot.setLotNo(bookLotNo);
		/**
		*	Update the Lot No record in the database
		*/
		boolean lotBookSuccess = bookNewLot.bookLot();
		boolean createBookSuccess = newBook.createBooking();
		/**
		*	return a boolean value for showing success or failiure
		*/
		if (lotBookSuccess && createBookSuccess) {
			return true;
		} else {
			return false;
		}
	}
	static boolean checkout(String bookNo) {
		Scanner scan = new Scanner(System.in);

		ParkLot unbookOldLot = new ParkLot();
		/**
		*	This object of the class Booking will be used to fetch the booking that is being cancelled
		*/
		Booking bookObj = new Booking();

		ResultSet rs = null;

		String bookingNo;
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

			rs = checkoutConn.fetchSelectUserBookingsByBookingNo(bookNo);

			rs.next();

			bookingNo = rs.getString(1);
		 	lotNo = rs.getInt(3);
			carNo = rs.getString(4);
			startTime = rs.getTimestamp(5);
			endTime = rs.getTimestamp(6);
			cost = rs.getInt(7);
			bookObj.setAllValues(bookingNo, lotNo, carNo, startTime, endTime, cost);

			if (!bookObj.checkoutBooking()) {
				return false;
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

		/*
		System.out.print("Enter a lot number to unbook: ");
		int unbookLotNo = scan.nextInt();

		if (unbookOldLot.checkAvailable(unbookLotNo)) {
			/**
			*	If the requested lot number is not booked,
			*	show failure and go back to the main menu
			*
			System.out.println("Parking Lot Number "+ unbookLotNo + " is not booked.");
			return false;
		} else {
			/**
			*	If the requested lot number is booked then,
			*	start the updation procedure by calling the required method through
			*	the object unbookLot.
			*
			System.out.println("Parking Lot Number "+ unbookLotNo + " is booked.\nUnbooking the Lot Number");
		}
		*/

		unbookOldLot.setLotNo(lotNo);
		boolean lotUnbookSuccess = unbookOldLot.unbookLot();

		return lotUnbookSuccess;
	}
	static void bookingHistory(User booker) {
		Booking bookings = new Booking();
		ResultSet historyRs = null;

		System.out.printf("*%14s%10s%18s%28s%28s%15s%11s\n", "Booking No", "Lot No", "Car No", "Start Time", "End Time", "Cost", "*");
		System.out.println("*****************************************************************************************************************************");
		try {
			DbConnection historyConn = new DbConnection();
			historyRs = historyConn.fetchSelectUserByUNameBookings(booker.getUName());
			while (historyRs.next()) {
				String bookingNo = historyRs.getString(1);
				int lotNo = historyRs.getInt(3);
				String carNo = historyRs.getString(4);
				java.sql.Timestamp startTime = historyRs.getTimestamp(5);
				java.sql.Timestamp endTime = historyRs.getTimestamp(6);
				int cost = historyRs.getInt(7);
				bookings.setAllValues(bookingNo, lotNo, carNo, startTime, endTime, cost);
				bookings.showBookHistory();
			}
		} catch(SQLException SqlExcep) {
			System.out.println("Error connecting to database.");
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
			}*/
		}
	}
	static void deleteUser(User account) {
		try {
			DbConnection delConn = new DbConnection();

			delConn.executeDeleteUsers(account.getUName());

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
	/**
	*	to show the bookings which are pending to be checked out
	*/
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
				*/
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
			}*/
		}
	}


	/**
	*	Main
	*/

	public static void main(String[] args) throws Exception {
		Scanner scan = new Scanner(System.in);

		/*
		*	Show the user a menu here and do the operations by calling the
		*	methods in the public class which internally call the ther methods
		* and use the classes.
		*	Showing menu only once to remove the clutter problem from the screen
		*/

		int opt;

		do {
			clearScreen();
			System.out.println("\n*****************WELCOME*****************");
			System.out.println("*\t\t1. Login\t\t*\n*\t\t2. Sign Up\t\t*\n*\t\t3. Exit\t\t\t*");
			System.out.println("*****************************************");
			System.out.print("\nMenu>> ");
			opt = scan.nextInt();

			switch (opt) {
				case 1: {	//Login
					clearScreen();
					/**
					*	Creating an object of class user to store the details of the active user (user which has logged on)
					*	to perform operations on it and then use updateSet() to update and changes done to the final database
					*/
					User activeUser = new User();
					System.out.println("\n*********LOGIN*********");
					/**
					*	Try to validate login credentials and proceed accordingly.
					*/
					activeUser = Login();
					if (activeUser.getUName() != null) {
						System.out.println("Login Successfull!");
					} else {
						/**
						*	If the login fails, break and start over again
						*/
						System.out.println("Login Failed! Please retry.");
						break;
					}
					/**
					*	This part will be execute only if the login is Successfull else a break operation is performed
					*	and the application will go back to the main menu.
					*/
					//System.out.println("User Operations");

					//activeUser.displayFields();

					int userOpt;

					do {
						clearScreen();
						System.out.println("\n********************MAIN MENU********************");
						System.out.println("*\t\t1. Book\t\t\t\t*\n*\t\t2. Profile Details\t\t*\n*\t\t3. Checkout\t\t\t*\n*\t\t4. Booking History\t\t*\n*\t\t5. Logout\t\t\t*");
						System.out.println("*************************************************");
						System.out.print("\n" + activeUser.getUName() + ">> ");
						userOpt = scan.nextInt();
						switch (userOpt) {
							case 1: {	//Book
								clearScreen();
								System.out.println("***************************BOOK PARKING NOW***************************");

								if (bookParking(activeUser)) {
									System.out.println("Parking lot has been successfully booked.");
								} else {
									System.out.println("Sorry we could not book any parking lot for you.");
								}

								createDelay(800);
								break;
							}
							case 2: {	//Profile details
								clearScreen();
								activeUser.displayDetails();
								System.out.println("\n\n***************PROFILE MENU**************");
								System.out.println("*\t\t1. Go back\t\t*\n*\t\t2. Edit Profile\t\t*\n*\t\t3. Delete My Account    *");
								System.out.println("*****************************************");
								System.out.print(activeUser.getUName() + "|Profile>> ");
								int profileOpt = scan.nextInt();
								switch (profileOpt) {
									case 1: {
										//Do nothing and just go back to the user's main menu
										break;
									}
									case 2: {	// edit account details
										System.out.println("\n\n******************************EDIT PROFILE DETAILS******************************");
										System.out.print("Phone: ");
										String phone = scan.next();
										System.out.print("Email: ");
										String email = scan.next();
										activeUser.setPhone(phone);
										activeUser.setEmail(email);
										DbConnection updateConn = new DbConnection();
										boolean updateSuccess = updateConn.executeUpdateUsers(activeUser.getPhone(), activeUser.getEmail(), activeUser.getUName());
										if (updateSuccess) {
											System.out.println("Account Details updated successfully.");
										} else {
											System.out.println("Account details couldn't be updated.");
										}
										break;
									}
									case 3: {	// delete account
										System.out.println("\n**************************************DELETE ACCOUNT**************************************");
										System.out.print("Are you sure you want to delete your account?\nOnce deleted, all your account data and any related data cannot be recovered.\n\t1. Confirm Deletion\n\t2. Cancel\n\t>> ");
										int deleteOpt = scan.nextInt();
										cls();
										System.out.println("Proceeding to delete...");
										clearScreen();

										switch (deleteOpt) {
											case 1: {
												System.out.print("Enter your password: ");
												scan.nextLine();
												String verifyPass = scan.nextLine();

												if (activeUser.getPassword().equals(verifyPass)) {
													deleteUser(activeUser);
													System.out.println("Account Successfully deleted.");
													/**
													*	Set userOpt as 5 to get logged out
													*/
													userOpt = 5;
												} else {
													System.out.println("Incorrect password.\n Couldn't delete account.");
												}
												break;
											}
											case 2: {

											}
										}
									}
									default: {
										System.out.println("Invalid option chosen.");
									}
								}
								break;
							}
							case 3: {	//Checkout
								clearScreen();
								/**
								*	Show all the bookings first for the user's reference
								*/
								System.out.println("*******************************************************BOOKING HISTORY*******************************************************");
								System.out.println("*                                                                                                                           *");
								presentBookingHistory(activeUser);
								System.out.println("*                                                                                                                           *");
								System.out.println("*****************************************************************************************************************************");

								System.out.println("\n\n******************************CHECKOUT******************************");
								System.out.print("Enter the booking number: ");
								String bookingNo = scan.next();

								if (checkout(bookingNo)) {
									System.out.println("Checked out Successfully");
								} else {
									System.out.println("We could not checkout the booking for you.");
								}
								break;
							}
							case 4: {	//Booking History
								clearScreen();
								System.out.println("*******************************************************BOOKING HISTORY*******************************************************");
								bookingHistory(activeUser);
								System.out.println("*                                                                                                                           *");
								System.out.println("*****************************************************************************************************************************");
								System.out.print("\t1. Go Back\n    >> ");
								int historyOpt = scan.nextInt();
								switch (historyOpt) {
									case 1: {

									}
									default: {

									}
								}
								break;
							}
							case 5: {	//Logout
								cls();
								System.out.println("Logging out...");
								break;
							}
							default: {
								System.out.println("Incorrect option. Please try again.");
							}
						}
					} while (userOpt != 5);

					break;
				}
				case 2: {	//Sign Up
					clearScreen();
					System.out.println("*********CREATE NEW ACCOUNT*********");
					if (createAccount()) {
						System.out.println("Account created successfully.");
					} else {
						System.out.println("Error creating user. Please try again.");
					}
					break;
				}
				case 3: {	//Exit
					clearScreen();
					System.out.println("Exiting Application...");
					cls();
					System.exit(0);
				}
				default: {
					System.out.println("Incorrect option. Please try again.");
				}
			}
		} while (opt != 3);

	}
}
