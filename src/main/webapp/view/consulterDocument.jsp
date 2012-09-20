<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>IHM Web Exploit - Consultation de document</title>
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
</script>
</head>

<body>

<form:form method="post" modelAttribute="consultationForm">

	<h6 class="msg_erreur"><form:errors path='*' /></h6>

	
	<p><label for="identifiant">Identifiant du document :</label>
	<form:input cssClass="inputText" id="identifiant" path="identifiant"/></p>
	

	<p><a id="lienMetaAffichage" href="javascript:afficherCacherDiv('metaAffichage')" onclick="javascript:changerLienMetaAff();">Personnaliser les métadonnées à afficher en retour</a></p>

	<div id="metaAffichage" style="overflow: scroll; height: 180px; display: none;">
	
	<display:table
		name="listeMetaAffichage" id="meta2"
		requestURI="/consulterDocument.do" style="width: 90%;">
		<display:column style="width: 5%" title="">
			<form:checkbox path="codeMeta" value="${meta2.code}" />
		</display:column>
		<display:column property="code" title="Code" sortable="true" />
		<display:column property="libelle" title="Libelle" sortable="true" />
		<display:column property="indexee" title="Indexée" sortable="true" />
	</display:table>
	

	</div>
	<p><input type="submit" value="Consulter" /></p>

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


