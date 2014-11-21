<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>SAE - Intégration - Liste des tests</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- Twitter boootstrap -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
</head>
<body>
	<div class="container">
		<div class="row">
			<table width="100%">
				<tr>
					<td><h1>SAE - Intégration - Liste des tests</h1></td>
					<td align="right"><a href="listeTests.do">&lt;&lt;&nbsp;Retour à la liste des tests </a></td>
				</tr>
			</table>
			
			<c:forEach var="categorie" items="${listeTests.categorie}">
		
				<c:set var="lon" value="${fn:length(categorie.casTests.casTest)}" />
				<h2><c:out value="${categorie.nom}" /></h2>
		
				<table class="table table-bordered">
					<tbody>
						<c:forEach var="casTest" items="${categorie.casTests.casTest}" varStatus="status">
							
							<c:if test="${not status.first and status.index % 2 == 0}">
								<tr></tr>
							</c:if>
							
							<tr><td>
								<c:choose>
									<c:when test="${casTest.implemente==true}">
										<a class="lienCas"
											href="test<c:out value="${casTest.id}" />.do?id=<c:out value='${categorie.id}' />">
										<c:out value="${casTest.code}" /> </a>
									</c:when>
				
									<c:otherwise>
										<c:out value="${casTest.code}" />
									</c:otherwise>
								</c:choose>
							</td></tr>
						</c:forEach>
					</tbody>
				</table>
			</c:forEach>
		</div>
	</div>
</body>
</html>
