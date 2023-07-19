package com.luv2code.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Set up the printwriter( used to send data back to the browser)
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		//Get a connection to DB
		
		 Connection myConn = null;
		 Statement myStat = null;
		 ResultSet myRes = null;
		 
		try {
			myConn = dataSource.getConnection();
			
		//Create SQL statement
		
			String sql = "select*from student";
			myStat = myConn.createStatement();
			
		//Execute SQL query
			
			myRes = myStat.executeQuery(sql);
		
		//Process the result set
			
			while(myRes.next()) {
				String email = myRes.getString("email");
				out.println(email);
			}
			
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}

}
