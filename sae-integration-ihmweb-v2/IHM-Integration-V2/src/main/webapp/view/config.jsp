<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Configuration de l'application</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- Twitter boootstrap -->
	<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="css/style.css"  />

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
		<p class="titre1">Configuration de l'application</p>
		</td>
		<td align="right" style="width: 25%;"><a href="index.do">&lt;&lt;&nbsp;Retour
		à l&apos;accueil </a></td>
	</tr>
</table>


<form:form method="post" modelAttribute="formulaire">
	<input type="hidden" name="action" id="action" />
	<input type="hidden" name="idSup" id="idSup" />

	<!-- ====================================================== -->
   <!-- La configuration des ECDE -->
   <!-- ====================================================== -->
   
   <hr/>
   
   <p class="titre2">1) Configuration des ECDE</p>
   
   <p style="color:red; font-style:italic; font-weight:bold;">Attention : il faut cliquer sur le bouton Sauvegarder pour que les modifications soient effectives</p>
   
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
   
   
   <!-- ====================================================== -->
   <!-- La configuration de l'URL du service web SAE -->
   <!-- ====================================================== -->
   
   
   <p class="titre2">2) Configuration de l'URL du service web SAE</p>
   
   <p style="color:red; font-style:italic; font-weight:bold;">Attention : cette modification n'est valable que pour la session en cours. Pour une modification définitive, il faut modifier le fichier de configuration de l'application.</p>
   
	<table width="100%">
		<tbody>
			<tr>
				<td>Adresse du WebService</td>
				<td><form:input path="urlWS" size="100" /></td>
			</tr>
			<tr>
				<td><h3 style="color: red">Modifier l'adresse</h3></td>
				<td>
               <input
                  type="submit"
					   onclick="document.getElementById('action').value='saveURL'"
					   value="Changer l'adresse" />
            </td>
			</tr>
		</tbody>
	</table>
   
   <br /><br /><br /><br />
   
   <!-- ====================================================== -->
   <!-- La configuration du chemin vers les tests de regression -->
   <!-- ====================================================== -->
   
   
   <p class="titre2">3) Configuration du chemin vers les tests de non-regression</p>
   
   <p style="color:red; font-style:italic; font-weight:bold;">Attention : cette modification n'est valable que pour la session en cours. Pour une modification définitive, il faut modifier le fichier de configuration de l'application.</p>
   
	<table width="100%">
		<tbody>
			<tr>
				<td>Chemin vers le dossier contenant les tests de regression</td>
				<td><form:input path="cheminTest" size="100" /></td>
			</tr>
			<tr>
				<td><h3 style="color: red">modifier le chemin</h3></td>
				<td>
               <input
                  type="submit"
					   onclick="document.getElementById('action').value='saveTestRegression'"
					   value="Changer le chemin" />
            </td>
			</tr>
		</tbody>
	</table>
   
   <br /><br /><br /><br />
   
   <!-- ====================================================== -->
   <!-- La configuration du chemin vers les tests XML -->
   <!-- ====================================================== -->
   
   
   <p class="titre2">4) Configuration du chemin vers les tests d'integration au format XML</p>
   
   <p style="color:red; font-style:italic; font-weight:bold;">Attention : cette modification n'est valable que pour la session en cours. Pour une modification définitive, il faut modifier le fichier de configuration de l'application.</p>
   
	<table width="100%">
		<tbody>
			<tr>
				<td>Chemin vers le dossier contenant les tests d'integration</td>
				<td><form:input path="cheminTestXml" size="100" /></td>
			</tr>
			<tr>
				<td><h3 style="color: red">modifier le chemin</h3></td>
				<td>
               <input
                  type="submit"
					   onclick="document.getElementById('action').value='saveTestXml'"
					   value="Changer le chemin" />
            </td>
			</tr>
		</tbody>
	</table>
   
   <br /><br /><br /><br />
   
    <!-- ====================================================== -->
   <!-- La configuration du chemin vers les tests de regression -->
   <!-- ====================================================== -->
   
   
   <p class="titre2">3) Configuration du chemin vers les fichiers des résultats attendus pour les tests de non-regression</p>
   
   <p style="color:red; font-style:italic; font-weight:bold;">Attention : cette modification n'est valable que pour la session en cours. Pour une modification définitive, il faut modifier le fichier de configuration de l'application.</p>
   
	<table width="100%">
		<tbody>
			<tr>
				<td>Chemin vers le dossier contenant les résultats attendus des tests de non-regression</td>
				<td><form:input path="cheminTestAttendu" size="100" /></td>
			</tr>
			<tr>
				<td><h3 style="color: red">modifier le chemin</h3></td>
				<td>
               <input
                  type="submit"
					   onclick="document.getElementById('action').value='saveTestAttendu'"
					   value="Changer le chemin" />
            </td>
			</tr>
		</tbody>
	</table>
   
   <br /><br /><br /><br />
   
</form:form>

</body>
</html>