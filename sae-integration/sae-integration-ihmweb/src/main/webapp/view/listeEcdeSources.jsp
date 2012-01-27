<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Configuration ECDE</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function validateDelete(id) {
		if (confirm("Etes-vous sûr de vouloir supprimer ce lien ?")) {
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
<table width="100%">
	<tr>
		<td style="width: 75%;">
		<p class="titre1">Configuration ECDE</p>
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

		<p style="color: red; font-style: italic"><b>Attention : il
		faut cliquer sur le bouton Sauvegarder pour que les modifications
		soient effectives</b></p>
		</td>
	</tr>
</table>
<form:form method="post" modelAttribute="formulaire">
	<input type="hidden" name="action" id="action" />
	<input type="hidden" name="idSup" id="idSup" />

	<table border="1px" width="50%">
		<thead>
			<tr>
				<th>Sup.</th>
				<th>DNS de l&apos;URL ECDE</th>
				<th>répertoire de base</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td colspan="3">
				<h3>Modification des liens existants</h3>
				</td>
			</tr>
			<c:forEach items="${formulaire.ecdeSources.sources}"
				var="currentEcde" varStatus="status">
				<tr>
					<td><input type="submit"
						onclick="return validateDelete('<c:out value="${status.index}" />')"
						value="supprimer" /></td>
					<td><form:input
						path="ecdeSources.sources[${status.index}].host" size="50" /></td>
					<td><form:input
						path="ecdeSources.sources[${status.index}].basePath" size="50" /></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3">
				<h3>Ajout de lien</h3>
				</td>
			</tr>
			<tr>
				<td style="background-color: gray"><b>Saisie</b></td>
				<td><form:input path="source.host" size="50" /></td>
				<td><form:input path="source.basePath" size="50" /></td>
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
	<br/><hr/>
	<table width="100%">
		<tbody>
			<tr>
				<td colspan="2">
				<h3>Modification de l'adresse WebService</h3>
				</td>
			</tr>
			<tr>
				<td>Adresse du WebService</td>
				<td><form:input path="urlWS" size="50" /></td>
			</tr>
			<tr>
				<td>
				<h3 style="color: red">Modifier l'adresse</h3>
				</td>
				<td><input type="submit"
					onclick="document.getElementById('action').value='saveURL'"
					value="Changer l'adresse" /></td>
			</tr>
		</tbody>
	</table>
</form:form>
</body>
</html>