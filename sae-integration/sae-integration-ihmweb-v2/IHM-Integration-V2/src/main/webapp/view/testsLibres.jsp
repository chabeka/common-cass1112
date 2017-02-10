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
	

	<div class="container">
		<div class="row">
			<table width="100%">
				<tr>
					<td><h1 class="titre">SAE - Intégration - Liste des tests</h1></td>
					<td align="right"><a href="index.do">&lt;&lt;&nbsp;Retour à l&apos;accueil </a></td>
				</tr>
			</table>
		
			<table class="table table-bordered">
				<tbody>
					<c:forEach var="categorie" items="${listeTests.categorie}">
						<tr><td><a href="test<c:out value="${categorie.id}" />.do?id=<c:out value='${categorie.id}' />"><c:out value="${categorie.nom}" /></a></td></tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	

<!-- 	<table class="table table-bordered"> -->
<!-- 		<tbody> -->
<!-- 		<tr> -->
<%-- 				<td><a href="test995.do"><c:out --%>
<%-- 							value="Tests techniques" /></a></td> --%>
<!-- 			</tr> -->
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

</body>
</html>