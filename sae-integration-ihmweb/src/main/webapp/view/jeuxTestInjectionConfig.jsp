<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>SAE - Intégration</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- Twitter boootstrap -->
	<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="css/style.css"  />

	<script type="text/javascript">
		function validateDelete(id) {
			if (confirm("Etes-vous sûr de vouloir supprimer ce cas de test ?")) {
				document.getElementById('idSup').value = id;
				document.getElementById('action').value='delete';
				return true;
			} else {
				return false;
			}
		}
	</script>
</head>
<body>
<table class="table">
	<tr>
		<td style="width: 75%;">
		<h3 class="text-primary">Jeux de test à injecter : Configuration</h3>
		</td>
		<td align="right" style="width: 25%;"><a href="index.do">&lt;&lt;&nbsp;Retour
		à l&apos;accueil </a></td>
	</tr>
	<tr>
		<td colspan="2" align="right"><a href="listeTests.do">Liste
		des cas de tests&nbsp;&gt;&gt;</a></td>
	</tr>
	<tr>
		<td colspan="2">
		
		<p style="color: red; font-style: italic"><b>Attention : il faut
		cliquer sur le bouton Sauvegarder pour que les modifications soient
		effectives</b></p>
		</td>
	</tr>
</table>
<form:form method="post" modelAttribute="formulaire">
	<input type="hidden" name="action" id="action" />
	<input type="hidden" name="idSup" id="idSup" />

	<table class="table table-bordered">
		<thead>
			<tr>
				<th>Sup.</th>
				<th>Nom du cas de test</th>
				<th>URL ECDE du cas de test</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td colspan="3">
				<h3>Supression</h3>
				</td>
			</tr>
			<c:forEach items="${formulaire.ecdeTests.listTests}"
				var="currentCas" varStatus="status">
				<tr>
					<td style="text-align:center;"><input type="submit"
						onclick="return validateDelete('<c:out value="${status.index}" />')"
						value="supprimer" /></td>
					<td><form:input
						path="ecdeTests.listTests[${status.index}].name" cssStyle="width:95%;" /></td>
					<td><form:input
						path="ecdeTests.listTests[${status.index}].url" cssStyle="width:95%;" size="50" /></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3">
				<h3>Ajout</h3>
				</td>
			</tr>
			<tr>
				<td style="background-color: gray"><b>Saisie</b></td>
				<td><form:input path="ecdeTest.name" size="50" /></td>
				<td><form:input path="ecdeTest.url" size="50" /></td>
			</tr>
			<tr>
				<td colspan="3"><input type="submit"
					onclick="document.getElementById('action').value='add'"
					value="Ajouter" /></td>
			</tr>
			<tr>

			</tr>
			<tr>
				<td colspan="2">
				<h3 style="color: red">Sauvegarder le fichier</h3>
				</td>
				<td><input type="submit"
					onclick="document.getElementById('action').value='generate'"
					value="Sauvegarder" /></td>
			</tr>
		</tbody>
	</table>
   
   <br /><br /><br /><br />
   
</form:form>
</body>
</html>