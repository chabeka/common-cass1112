<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>SAE - Intégration - Référentiel des cas de tests</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- Twitter boootstrap -->
	<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="css/style.css"  />

</head>
<body>
	<div class="container">
		<table width="100%">
			<tr>
				<td style="width: 75%">
				<h1 class="titre">SAE - Intégration - Référentiel des cas de tests</h1>
				</td>
				<td style="width: 25%" align="right"><a href="index.do">&lt;&lt;&nbsp;Retour à l&apos;accueil </a></td>
			</tr>
			<tr>
				<td colspan="2" align="right">
					<a href="listeTests.do">Liste des cas de test&nbsp;&gt;&gt; </a>
				</td>
			</tr>
		</table>
		
		<c:forEach var="categorie" items="${listeTests.categorie}">
		
			<table class="table table-bordered">
				<tr  class="blod bgcolor1">
					<td style="width: 15%">Catégorie</td>
					<td style="width: 15%">Code</td>
					<td style="width: 60%">Description</td>
				</tr>
		
				<c:forEach var="casTest" items="${categorie.casTests.casTest}">
					<tr>
						<td>${categorie.nom}</td>
						<td>${casTest.code}</td>
						<td>${casTest.description}</td>
					</tr>
				</c:forEach>
			</table>
		</c:forEach>
	</div>
</body>
</html>
