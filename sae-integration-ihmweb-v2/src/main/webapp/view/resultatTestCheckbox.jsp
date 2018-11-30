<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>SAE - Intégration - Cas de test unitaire</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- Twitter boootstrap -->
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
	<h3 class="text-primary">Lancer un cas de test contenu sur le
		serveur</h3>
	<div class="pull-right">
		<table width="100%">
			<tr>
				<td colspan="2" align="right"><a href='testRegression.do'>Retour
						&agrave; la liste des tests</a></td>
			</tr>
			<tr>
				<td colspan="2" align="right"><a href='index.do'>Retour
						&agrave; l'accueil</a></td>
			</tr>

		</table>
		<hr />
	</div>
	<table class="table table-bordered">

		<c:forEach var="metadonnee" items="${resTest}">
			<tr>
				<td style="font-weight: bold;"><c:out value="${metadonnee.key}" /></td>
				<c:forEach var="meta" items="${metadonnee.value}">
					<tr>
						<td style="text-align: center;"><c:out value="${meta.key}" /></td>
						<td><c:out value="${meta.value}" /></td>
					</tr>
					<br />
				</c:forEach>
			</tr>
		</c:forEach>

	</table>
</body>
</html>
