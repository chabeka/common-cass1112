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
				<td><a href="testUnitaire.do"><c:out
							value="Créer un test libre" /></a></td>
			</tr>
			<tr>
				<td><a href="gestionTestNonRegression.do"><c:out
							value="Gestion des tests de non regression sur le serveur" /></a></td>
			</tr>
		</tbody>
	</table>

	<!-- 	<table class="table table-bordered"> -->
	<!-- 		<tbody> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="captureUnitaire.do"><c:out --%>
	<%-- 							value="Capture unitaire" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="captureMasse.do"><c:out --%>
	<%-- 							value="Capture de masse" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="consultation.do"><c:out value="Consultation" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="consultationAffichable.do"><c:out --%>
	<%-- 							value="Consultation affichable" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="consultationGNTGNS.do"><c:out --%>
	<%-- 							value="Consultation GNT/GNS" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="copie.do"><c:out value="Copie" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="recuperationDocOrigine.do"><c:out --%>
	<%-- 							value="Récuperation de document d'origine" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="notes.do"><c:out value="Note" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="idGed.do"><c:out value="Id GED" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="recherche.do"><c:out value="Recherche" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="rechercheNbRes.do"><c:out --%>
	<%-- 							value="Recherche avec nombre de résultats" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="rechercheIterateut.do"><c:out --%>
	<%-- 							value="Recherche par itérateur" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="isolationDonnees.do"><c:out --%>
	<%-- 							value="Isolation des données" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="droits.do"><c:out value="Droits" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="modification.do"><c:out --%>
	<%-- 							value="Modification Métadonnées" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="suppression.do"><c:out --%>
	<%-- 							value="Suppression de document" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="suppressionMasse.do"><c:out --%>
	<%-- 							value="Suppression de masse" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="transfert.do"><c:out --%>
	<%-- 							value="Transfert de document" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="restoreSuppressionMasse.do"><c:out --%>
	<%-- 							value="Restore suppression de masse" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="formats.do"><c:out value="Formats" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="activationDocArchivable.do"><c:out --%>
	<%-- 							value="Activation document archivable" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="pki.do"><c:out value="PKI" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="etatTraitementMasse.do"><c:out --%>
	<%-- 							value="Etat traitement de masse" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<%-- 				<td><a href="recuperationMetadonnee.do"><c:out --%>
	<%-- 							value="Recuperation metadonnee" /></a></td> --%>
	<!-- 			</tr> -->
	<!-- 		</tbody> -->
	<!-- 	</table> -->
	<script>
      var testEl = Ext.get('testExtJS');
      var maFonction = function(e, t) {
         alert('Test réussi');
      };
      testEl.on('mouseover', maFonction);
   </script>
</body>
</html>