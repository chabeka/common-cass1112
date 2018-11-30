<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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
	<h3 class="text-primary">Réaliser des tests</h3>
	<div class="pull-right">
		<a href="/sae-integration-ihmweb-v2">Retour &agrave; l'accueil</a>
	</div>

	<c:choose>
		<c:when test="${resSauvegarde == true}">
			<h5 class="bg-success pad15">
				<c:out value="${description}" />
			</h5>
		</c:when>
		<c:when test="${resSauvegarde == false}">
			<h5 class="bg-danger pad15">
				<c:out value="${description}" />
			</h5>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>

	<hr />

	<form method="post" enctype="multipart/form-data">
		<input type="hidden" name="action" id="action" /> Charger un Test à
		lancer depuis votre ordinateur :
		<input type="file" name="file" />
		<br />
		<input type="submit"
			onclick="document.getElementById('action').value='lancerTest'"
			value="Lancer" />
	</form>

	<hr />

	<table class="table table-bordered">
		<tbody>
			<tr>
				<td><a href="testXmlServeur.do"><c:out
							value="Lancer un test XML sauvegardé sur le serveur" /></a></td>
			</tr>
			<tr>
				<td><a href="testsLibres.do"><c:out
							value="Liste des tests libres" /></a></td>
			</tr>
			<tr>
				<td><a href="gestionTestNonRegression.do"><c:out
							value="Gestion des tests de non regression sur le serveur" /></a></td>
			</tr>
		</tbody>
	</table>

	<script>
      var testEl = Ext.get('testExtJS');
      var maFonction = function(e, t) {
         alert('Test réussi');
      };
      testEl.on('mouseover', maFonction);
   </script>
</body>
</html>