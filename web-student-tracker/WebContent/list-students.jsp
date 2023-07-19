<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
	<title>Student Tracker APP</title>
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>



<body>

	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>
	
	<div id="container">
		<div id="container">
		<br/>
			<!-- put new button: Add student -->
			<input type="button" value="Add Student" 
				onclick="window.location.href='add-student-form.jsp'; return false;"
				class="add-student-button">
		
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>	
					<th>Action</th>				
				</tr>
				
				<c:forEach var="tempStudent" items="${STUDENT_LIST }">
					<!-- set up a link for each student -->
					<c:url var="tempLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD"/>    <!-- parameters that we send to Controller -->
						<c:param name="studentID" value="${tempStudent.id }" />
					</c:url>
					
					<!-- set up a link to delete student -->
					<c:url var="deleteLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE"/>    <!-- parameters that we send to Controller -->
						<c:param name="studentID" value="${tempStudent.id }" />
					</c:url>
					
					<tr>
						<td> ${tempStudent.firstName} </td>
						<td> ${tempStudent.lastName} </td>
						<td> ${tempStudent.email} </td>
						<td>
							<a href="${tempLink }">Update</a>
							|
							<!-- java script code with confirm -->
							<a href="${deleteLink }"
							onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">
							Delete</a>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>

</body>
</html>