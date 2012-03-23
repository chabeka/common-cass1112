<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title>Pile des travaux</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>


<body>

<h1>Pile des travaux</h1>


<form:form method="post" modelAttribute="formulaire">

   <table border=1>
      <tr>
         <td>Serveur(s) Zookeeper :</td>
         <td><form:label path="serveursZookeeper" /></td>
      </tr>
      <tr>
         <td>Serveur(s) Cassandra :</td>
         <td><form:label path="serveursCassandra" /></td>
      </tr>
   </table>
   
   <br />
   <br />
   
   
   <table border=1>
      
	   <tr style="font-weight:bold;">
	      <td>idJob</td>
	      <td>type</td>
	      <td>parameters</td>
	      <td>state</td>
	      <td>reservedBy</td>
	      <td>creationDate</td>
	      <td>reservationDate</td>
	      <td>startingDate</td>
	      <td>endingDate</td>
	      <td>message</td>
	   </tr>
      
      <c:forEach items="${formulaire.travaux}" var="travail">
      
      <tr>
         <td><c:out value="${travail.idJob}"/></td>
         <td><c:out value="${travail.type}"/></td>
         <td><c:out value="${travail.parameters}"/></td>
         <td><c:out value="${travail.state}"/></td>
         <td><c:out value="${travail.reservedBy}"/></td>
         <td><c:out value="${travail.creationDate}"/></td>
         <td><c:out value="${travail.reservationDate}"/></td>
         <td><c:out value="${travail.startingDate}"/></td>
         <td><c:out value="${travail.endingDate}"/></td>
         <td><c:out value="${travail.message}"/></td>
      </tr>
      
      </c:forEach>
      
      
   </table>
   
   

</form:form>


</body>
</html>