<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>SAE - Intégration</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- Twitter boootstrap -->
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>
	<h3 class="text-primary">Lancer un test XML contenu sur le serveur</h3>
	<div class="pull-right">
		<table width="100%">
			<tr>
				<td colspan="2" align="right"><a href='listeTests.do'>Retour
						&agrave; la liste des tests</a></td>
			</tr>
			<tr>
				<td colspan="2" align="right"><a href='index.do'>Retour
						&agrave; l'accueil</a></td>
			</tr>

		</table>
	</div>

	<c:choose>
		<c:when test="${isDelete == true}">
			<h5 class="bg-success pad15">
				<c:out value="le fichier a été supprimé" />
			</h5>
		</c:when>
		<c:when test="${isDelete == false}">
			<h5 class="bg-danger pad15">
				<c:out value="Le fichier n'a pas pu être supprimer" />
			</h5>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>

	<hr />


	<table>
		<tr>
			<td><form method="post">
					<input type="hidden" name="action" id="action" />
					<select multiple="multiple" name="testXml" style="width: 380px">
						<c:forEach var="meta" items="${listeTestXml}">
							<option value="${meta}"
								${listeTestXml[0] == meta ? 'selected' : ''}>${meta}</option>
						</c:forEach>
					</select>
					<br />
					<input type="submit"
						onclick="document.getElementById('action').value='lancerTest'"
						value="Lancer" />
					<input type="submit"
						onclick="document.getElementById('action').value='downloadTest'"
						value="telecharger le test" />
					<input type="submit"
						onclick="document.getElementById('action').value='supprimerTest'"
						value="Supprimer le test du serveur" />
				</form></td>
			<td>
				<form method="post">
					<input type="hidden" name="action" id="action2" />
					<input type="text" name="recherche"> <input type="submit"
						onclick="document.getElementById('action2').value='rechercherTest'"
						value="Recherche un test" />
				</form>
			</td>
		</tr>
	</table>
	<hr />

	<table>
		<tr>
			<c:forEach var="meta" items="${resTest}">
				<tr>
					<td style="font-weight: bold;"><c:out value="${meta.value}" /></td>
					<br />
				</tr>
			</c:forEach>
		</tr>
	</table>
</body>
</html>
