package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDBUtil studentDBUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	
	
	@Override
	public void init() throws ServletException {  // called when servlet is first initialized 
		super.init();
		
		//create our student db util and pass in the conn pool/datasource
		try {
			studentDBUtil = new StudentDBUtil(dataSource); 
		}catch(Exception exc) {
			throw new ServletException(exc);
		}
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// list students in MVC fashion
		try {
			//read the "command" parameter
			String theCommand = request.getParameter("command");
			
			// if the command is missing, then default to listing students
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			//route to the appropriate method
			switch(theCommand) {
			
			case "LIST":
				listStudents(request,response);
				break;
				
			case "ADD":
				addStudent(request, response);
				break;
				
			case "LOAD":
				loadStudent(request, response);
				break;
				
			case "UPDATE":
				updateStudent(request, response);
				break;
			
			case "DELETE":
				deleteStudent(request, response);
				break;
			
			default:
				listStudents(request,response);
			
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int id = Integer.parseInt(request.getParameter("studentID"));
		studentDBUtil.deleteStudent(id);
		listStudents(request, response);
		
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//read student data from form
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create new student
		Student student = new Student(id, firstName, lastName, email);
		//perform update on db
		studentDBUtil.updateStudent(student);
		//send them back to the "list students" page
		listStudents(request, response);
	}



	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//read student id from form data
		String studentID = request.getParameter("studentID");
		//get student from db
		Student theStudent = studentDBUtil.getStudent(studentID);
		//place student in th request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		//send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}



	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from the form
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		//create new student object
		Student student = new Student(firstName, lastName, email);
		
		//add the student to the database
		studentDBUtil.addStudent(student);
		//send back to main page
		listStudents(request,response);
	}



	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get students from DBUtil
		List<Student> students = studentDBUtil.getStudents();
		// add students to the request
		request.setAttribute("STUDENT_LIST", students);
		//send to JSP page(view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp"); //to sent data to JSP
		dispatcher.forward(request, response);
		
	}

}
