<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>SAE - Intégration - Liste des tests</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- Twitter boootstrap -->
	<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="css/style.css"  />
</head>
<body>
	<div class="container">
		<div class="row">
			<table width="100%">
				<tr>
					<td><h1 class="titre">SAE - Intégration - Liste des tests</h1></td>
					<td align="right"><a href="index.do">&lt;&lt;&nbsp;Retour à l&apos;accueil </a></td>
				</tr>
			</table>
		
			<table class="table table-bordered">
				<tbody>
					<c:forEach var="categorie" items="${listeTests.categorie}">
						<tr><td><a href="listeTests.do?action=detail&id=${categorie.id}"><c:out value="${categorie.nom}" /></a></td></tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
