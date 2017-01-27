<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>SAE - Intégration - Resultat du test XML contenu sur le serveur</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- Twitter boootstrap -->
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
	<div class="container">
		<table width="100%">
			<tr>
		<td colspan="2" align="right"><a href='listeTests.do'>Retour &agrave; la liste des tests</a></td>
	</tr>
			<tr>
				<td colspan="2" align="right"><a href='index.do'>Retour
						&agrave; l'accueil</a></td>
			</tr>
		</table>
		

		<table class="table table-bordered">
		<p class="titre2">${testName}</p><form method="post" >
					<c:forEach var="meta" items="${resTest}">
					<tr>
						<td style="text-align: center;"><c:out value="${meta.key}" /></td>
								<td><c:out
										value="${meta.value}" /></td>
								<br />
								</tr>
					</c:forEach>
		</table>
	</div>
</body>
</html>
