import java.sql.*;

public class GenerateParklot {
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName ("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "JavaDev", "password");
			stmt = con.createStatement();
			String selectQuery = "SELECT * FROM parklot";
			String insertQuery = "INSERT INTO parklot VALUES (? , ?)";


			pstmt = con.prepareStatement(insertQuery);
			for (int i = 1; i <= 25; i++) {
				pstmt.setInt(1, i);
				pstmt.setInt(2, 0);
				pstmt.executeUpdate();
			}

			rs = stmt.executeQuery(selectQuery);
		} catch(ClassNotFoundException ce) {
			ce.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
}
