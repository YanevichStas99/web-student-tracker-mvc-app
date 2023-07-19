package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.sql.DataSource;

public class StudentDBUtil {
	
	private DataSource dataSource;

	public StudentDBUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		
		Connection myCon = null;
		Statement myStat = null;
		ResultSet myRes = null;
		try {
			// get a connection
			myCon = dataSource.getConnection();
			//create sql statement
			String sql = "select*from student order by last_name";
			myStat = myCon.createStatement();
			//execute query
			myRes = myStat.executeQuery(sql);
			//process result
			while(myRes.next()) {
				// retrieve data from result set row
				int id = myRes.getInt("id");
				String firstName = myRes.getString("first_name");
				String lastName = myRes.getString("last_name");
				String email = myRes.getString("email");
				//create new student object
				Student tempStudent = new Student(id, firstName, lastName, email);
				//add it to the list
				students.add(tempStudent);
				
			}
			return students;
		}finally {
			//close JDBC objects
			close(myCon, myStat, myRes);
			
		}
	}

	private void close(Connection myCon, Statement myStat, ResultSet myRes) {

		try {
			
			if(myRes != null) {
				myRes.close();
			}
			if(myStat != null) {
				myStat.close();
			}
			if(myCon != null) {
				myCon.close();
			}
			
		}catch(Exception exc) {
			exc.printStackTrace();
		}
		
	}

	public void addStudent(Student student) {
		Connection myConn = null;
		PreparedStatement myStat = null;
		try {
			//get db connection
			myConn = dataSource.getConnection();
			
			//create sql for insert
			String sql = "insert into student "+
						"(first_name, last_name, email) "
						+ "values (?, ?, ?)"; //	prepared place holders to set with values
			myStat = myConn.prepareStatement(sql);
			//set the parameter values for the student
			myStat.setString(1, student.getFirstName());
			myStat.setString(2, student.getLastName());
			myStat.setString(3, student.getEmail());
			//execute sql insert
			myStat.execute();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			close(myConn, myStat, null);
		}
	}

	public Student getStudent(String theStudentID) {
		
		Student theStudent = null;
		
		Connection myCon = null;
		PreparedStatement myStat = null;
		ResultSet myRes = null;
		int studentId;
		
		try {
			// convert student id to int
			studentId = Integer.parseInt(theStudentID);
			
			//get connection
			myCon = dataSource.getConnection();

			//create sql to get student
			String sql = "select * from student where id=?";
			
			//create prepared statement
			myStat = myCon.prepareStatement(sql);
		
			//set params
			myStat.setInt(1, studentId);
			
			//execute statement
			myRes = myStat.executeQuery();
			
			//retrieve data from result
			if (myRes.next()) {
				String firstName = myRes.getString("first_name");
				String lastName = myRes.getString("last_name");
				String email = myRes.getString("email");
				
				//use the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			}else {
				throw new Exception("could not find student id: "+ studentId);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//clean up JDBC object
			close(myCon, myStat, myRes);
		}
		return theStudent;
	}

	public void updateStudent(Student student) {
		
		Connection myCon = null;
		PreparedStatement myStat = null;
		
		// get db connection
		try {
			myCon = dataSource.getConnection();
		
		//create sql statement
			String sql = "update student "+ "set first_name=?,"
					+ "last_name=?, email=? where id = ?";
		//prepare statement
			myStat = myCon.prepareStatement(sql);
		//set params
			myStat.setString(1, student.getFirstName());
			myStat.setString(2, student.getLastName());
			myStat.setString(3, student.getEmail());
			myStat.setInt(4, student.getId());
		//execute sql statement
			myStat.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			close(myCon, myStat, null);
		}
	}

	public void deleteStudent(int id) throws Exception{
		Connection myCon = null;
		PreparedStatement myStat = null;
		
		try {
			myCon = dataSource.getConnection();
			
			String sql = "delete from student where id = ?";
			myStat = myCon.prepareStatement(sql);
			myStat.setInt(1, id);
			myStat.execute();
		}
		finally {
			close(myCon, myStat, null);
		}
	}

}
