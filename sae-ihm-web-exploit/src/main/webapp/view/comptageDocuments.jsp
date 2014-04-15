<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>IHM Web Exploit - Comptage des documents archivés par un traitement de masse</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />

<script>
	// Afficher ou non la liste des critères de recherche
	function afficherCacherDiv(idDiv) {
	   if (document.getElementById(idDiv).style.display == "none") {
	      document.getElementById(idDiv).style.display = "inline";
	   } else {
	      document.getElementById(idDiv).style.display = "none";
	   }
	}
	
	function changerLienMetaAff() {
		
		if (document.getElementById("lienMetaAffichage").innerHTML == "Personnaliser les métadonnées à afficher en retour") {
			document.getElementById("lienMetaAffichage").innerHTML = "Cacher la liste des métadonnées de retour";
		} else {
			document.getElementById("lienMetaAffichage").innerHTML = "Personnaliser les métadonnées à afficher en retour";
		}		
	}

	
	// Cocher / Tous décocher (pour la liste des métadonnées à afficher)
	var isCoche = false;
	function GereChkbox() {
	   	blnEtat = isCoche ? false : true;
		this.cocher(blnEtat);
	   	isCoche = blnEtat;
	}
	function cocher(aFaire) {
		var Chckbox = document.getElementsByName("codeMeta");
		for ( var i = 0; i < Chckbox.length; i++) {
			if (Chckbox[i].nodeName=="INPUT") {
				if (Chckbox[i].getAttribute("type")=="checkbox") {
					document.getElementById(Chckbox[i].getAttribute("id")).checked = aFaire;
				}
			}	 
      	}		
	}
	
</script>
</head>

<body>

<form:form method="post" modelAttribute="comptageForm" action="lancerComptageDoc.do">

	<h6 class="msg_erreur"><form:errors path='*' /></h6>
	<c:if test="${stacktrace !=null}">
		<p><textarea class="stacktrace" rows="15" cols="180"><c:out value="${stacktrace}"/></textarea></p>
	</c:if>
	<p>Comptage du nombre de documents archivés par un traitement de masse : </p>
	<p><label for="identifiant">Identifiant du traitement de masse :</label>
	<form:input cssClass="inputText" id="identifiant" path="identifiant" /></p>
	<p><c:if test="${nbDocArchives !=null}">Nombre de documents archivés : ${nbDocArchives}</c:if></p>
	
	<p><input type="submit" value="Compter" /></p>

	<c:if test="${resRecherche != null}">
		<p><a href="telecharger.do?idConsult=<c:out value="${consultationForm.identifiant}"/>">Ouvrir/Télécharger le document</a></p>
		
		<table>
		<c:forEach items="${resRecherche.metadonnees}" var="meta">
			<tr><td><c:out value="${meta.code}"/></td><td><c:out value="${meta.valeur}" /></td></tr>
		</c:forEach>
		</table>
	</c:if>
	

</form:form>

</body>
</html>


