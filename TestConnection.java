package project;
import java.sql.*;

public class TestConnection {
	
	Connection c;
	Statement s;

	public TestConnection() {		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c= DriverManager.getConnection("jdbc:mysql://localhost:3307/testdatabase","root","Vishal@1722");
			s = c.createStatement();
		
		} catch(Exception e){
			System.out.println(e);
		}
	}
	
}

