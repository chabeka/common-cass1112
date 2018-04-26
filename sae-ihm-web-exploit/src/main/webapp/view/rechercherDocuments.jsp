<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>IHM Web Exploit - Recherche de documents</title>
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

	function changerLien() {
		
		if (document.getElementById("lienCodeRecherche").innerHTML == "Voir la liste des métadonnées disponibles") {
			document.getElementById("lienCodeRecherche").innerHTML = "Cacher la liste des métadonnées";
		} else {
			document.getElementById("lienCodeRecherche").innerHTML = "Voir la liste des métadonnées disponibles";
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

<form:form method="post" modelAttribute="rechercheForm" action="afficherDocuments.do">

	<h6 class="msg_erreur"><form:errors path='*' /></h6>
	<c:if test="${stacktrace !=null}">
		<p><textarea class="stacktrace" rows="15" cols="180"><c:out value="${stacktrace}"/></textarea></p>
	</c:if>
	<p>Veuillez saisir la requête lucène (<a id="lienCodeRecherche" href="javascript:afficherCacherDiv('codeRecherche')" onclick="javascript:changerLien();">Voir la liste des métadonnées disponibles</a>) :</p>
	<p><form:textarea path="requeteLucene" cols="105" rows="3"
		title="Requête Lucène" /></p>

	
	<div id="codeRecherche" style="overflow: scroll; height: 180px; display: none; padding-top: 10px">
		<display:table
		name="listeMetaRecherche" id="meta1"
		requestURI="/rechercherDocuments.do" style="width: 90%;">
			<display:column property="code" title="Code" sortable="true" />
			<display:column property="libelle" title="Libelle" sortable="true" />
			<display:column property="indexee" title="Indexée" sortable="true" />
		</display:table></div>
	
	
	<p><a id="lienMetaAffichage" href="javascript:afficherCacherDiv('metaAffichage')" onclick="javascript:changerLienMetaAff();">Personnaliser les métadonnées à afficher en retour</a></p>

	<div id="metaAffichage" style="overflow: scroll; height: 180px; display: none;">
		<display:table name="listeMetaAffichage" id="meta2"
		requestURI="/rechercherDocuments.do" style="width: 90%">
		<display:column style="text-align:center" title="<input type='checkbox' onClick='javascript:GereChkbox();'/>">	
			<form:checkbox path="codeMeta" value="${meta2.code}" />
		</display:column>
		<display:column property="code" title="Code" sortable="true" />
		<display:column property="libelle" title="Libelle" sortable="true" />
		<display:column property="indexee" title="Indexée" sortable="true" />
	</display:table></div>

	<p> Nombre de lignes dans le tableau résultat :
		<form:input cssClass="inputNum" size="3" id="nbLignesTableau" path="nbLignesTableau"/>
	</p>
	<p><input type="submit" value="Rechercher" /></p>

	<c:if test="${resRecherche != null}">
		<p>
		<c:out value="${resRecherche.nbResultat}" /> document(s) trouvé(s)
		<c:if test="${resRecherche.resultatTronque == true}"> - résultat tronqué</c:if>
		<c:if test="${resRecherche.resultatTronque == false}"> - résultat complet</c:if>
		</p>
		<div style="overflow: scroll">
		<display:table name="resRecherche.donnees" id="docRow" requestURI="/afficherDocuments.do" pagesize="${nbLignesTableau}" style="width: 90%">
			<display:column title="Identifiant" sortable="true">
				<a href="lancerConsultDoc.do?identifiant=${docRow.uuid}" title="Consulter le document">${docRow.uuid}</a>
			</display:column>
			<c:forEach items="${docRow.metadonnees}" var="meta">
				<display:column title="${meta.code}" sortable="true">${meta.valeur}</display:column>				
			</c:forEach>		
			</display:table>
		</div>
	</c:if>
</form:form>

</body>
</html>


