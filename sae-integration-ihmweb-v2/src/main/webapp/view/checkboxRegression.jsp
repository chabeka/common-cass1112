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
<h3 class="text-primary">Liste des cas de test pour l' ${checkboxValue}</h3>
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
		</div>
<hr />
		<table class="table table-bordered">
			
				<p>Choisissez les tests que vous desirez lancer :</p>
				<br />
				<form method="post">
			<input type="hidden" name="action" id="action" />
				<c:forEach var="meta" items="${checkboxRegression}">
					<tr>
						<input type="checkbox" name="checkboxName" value="${meta}" checked/>
						<label for="${meta}">${meta}</label>
						<br />

						<br />
				</c:forEach>
			</tr>
			<input type="submit"
				onclick="document.getElementById('action').value='lancerTest'"
				value="Lancer les tests sélectionnés" />
				
			</form>
		</table>
</body>
</html>
