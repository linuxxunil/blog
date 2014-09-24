package edu.servlet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.sql.serializable.Convert;
import edu.sql.serializable.DeliverResultSet;


public class Serialiable extends HttpServlet{
	private String dbUrl = "sqlserver://175.99.86.134:1433;instance=Cscheduling_SQL;DatabaseName=cscheduling;charset=utf-8";
	private String dbUser = "sa";
	private String dbPass = "ptch@RS";
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			
			// Connect Database
			con = DriverManager
					.getConnection("jdbc:jtds:"+dbUrl, dbUser, dbPass);
		
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
		
			// Convert ResultSet to DeliverResultSet
			DeliverResultSet drs =  Convert.toDeliverResultSet(rs);
			
			if ( drs == null )
				System.out.println("Fuck3");
			
			// Bind Servlet Output to Serialiable  
			ServletOutputStream os = resp.getOutputStream();
			ObjectOutputStream obj = new ObjectOutputStream(os);
		 
			// Write Object to Http
			obj.writeObject(drs);
	
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
		}
	}
}
