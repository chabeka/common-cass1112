<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib prefix="custom" uri="/WEB-INF/tld/map.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>IHM Web Exploit - Gérer la pile des travaux</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />

<script>
	//Afficher ou non la liste des colonnes pour personaliser l'affichage
	//à l'initialisation de l'écrané)
	function gererAffichageListeColonnes() {
		if (document.getElementById('modePerso').checked) {
			document.getElementById('colonnes').style.display = "inline";
		} else {
		   document.getElementById('colonnes').style.display = "none";
		}
		if (document.getElementById('parametres').value != "SAISIE") {
			document.getElementById('saisieParametre').style.display = "none";
		}
	}
	
	// Afficher la liste des colonnes pour personaliser l'affichage
	// lorqu'on clique sur le mode personalisé
	// Décoche toutes les cases à cocher
	function afficherListeColonnes() {
	   document.getElementById('colonnes').style.display = "inline";
	   this.cocher('colonnes', false);   
	}
	
	// Cache la liste des colonnes pour personaliser l'affichage
	// lorqu'on clique sur minimal ou complet
	function cacherListeColonnes(){
		document.getElementById('colonnes').style.display = "none";
	} 
	
	// Afficher ou non la liste des critères de recherche
	function afficherListeCriteres() {
	   if (document.getElementById('criteresRecherche').style.display == "none") {
	      document.getElementById('criteresRecherche').style.display = "inline";
	      document.getElementById('boutonEffacer').style.display = "inline";	      
	   } else {
	      document.getElementById('criteresRecherche').style.display = "none";
	      document.getElementById('boutonEffacer').style.display = "none";
	   }
	}
	
	// Cocher / Tous décocher (pour la liste des colonnes du mode personalisé)
	var isCoche = false;
	function GereChkbox(conteneur) {
	
	   	blnEtat = isCoche ? false : true;
		this.cocher(conteneur, blnEtat);
	   	isCoche = blnEtat;
	}
	function cocher(conteneur, aFaire) {
		var Chckbox = document.getElementById(conteneur).firstChild;
		while (Chckbox!=null) {
			if (Chckbox.nodeName=="INPUT")
				if (Chckbox.getAttribute("type")=="checkbox") {
					document.getElementById(Chckbox.getAttribute("id")).checked = aFaire;
				}
			Chckbox = Chckbox.nextSibling;
		}   
	}

	function confirmerSuppression(idJob) {
	   	document.getElementById("action").value = "supprimer"; 
	   	document.forms['formulaireGestion'].elements['identifiantATraiter'].value = idJob;
		return confirm("Confirmer la suppression du job : " + idJob + " ?");
	}

	function voirHistorique(idJob) {
		document.getElementById("action").value = "historique"; 
		document.forms['formulaireGestion'].elements['identifiantATraiter'].value = idJob;
		return true;
	}

	function changerLien() {
		
		if (document.getElementById("lienCriteres").innerHTML == "Afficher les critères de recherche") {
			document.getElementById("lienCriteres").innerHTML = "Cacher les critères de recherche";
		} else {
			document.getElementById("lienCriteres").innerHTML = "Afficher les critères de recherche";
		}		
	}

	function saiseParametre() {
		if(document.getElementById("parametres").value == "SAISIE") {
		   document.getElementById('saisieParametre').style.display = "inline";
		   document.getElementById('parametre').focus();
		   
		} else {
		   document.getElementById('saisieParametre').style.display = "none";
		}
	}

	
	function viderFiltre(maxKeysToReadConf) {

	   document.getElementById("idJob").value = "";
	   document.getElementById("dateCreationMin").value = "";
	   document.getElementById("dateCreationMax").value = "";
	   document.getElementById("type").value = "TOUS";
	   document.getElementById("contratService").value = "TOUS";
	   document.getElementById("parametres").value = "TOUS";
	   document.getElementById("parametre").value = "";
	   document.getElementById('saisieParametre').style.display = "none";
	   document.getElementById("etat").value = "TOUS";
	   document.getElementById("hostSae").value = "";
	   document.getElementById("hostClient").value = "";
	   document.getElementById("maxKeysToRead").value = maxKeysToReadConf;
	   
	}                       

   function exporter(idJob, creationDate, parameters, state, saeHost, clientHost, docCount, reservationDate, reservedBy, startingDate, pid, endingDate, message)
   {
      affiche = "<p style=\"text-align:left;font-family:Arial, Helvetica, sans-serif;font-size:8pt;\">";
      affiche += "<a href=\"javascript:document.getElementById('export').select();\">Tout sélectionner</a>";
      affiche += "</p>";
      affiche += "<div align=\"center\">";
      affiche += "<textarea id=\"export\" rows=\"14\" cols=\"90\">";
      affiche += "<pre>\n";
      affiche += "jobId            : " + idJob + "\n";
      affiche += "creationDate     : " + creationDate + "\n";
      affiche += "parameters       : " + parameters + "\n";
      affiche += "state            : " + state + "\n";
      affiche += "saeHost          : " + saeHost + "\n";
      affiche += "clientHost       : " + clientHost + "\n";
      affiche += "docCount         : " + docCount + "\n";
      affiche += "reservationDate  : " + reservationDate + "\n";
      affiche += "reservedBy       : " + reservedBy + "\n";
      affiche += "startingDate     : " + startingDate + "\n";
      affiche += "pid              : " + pid + "\n";
      affiche += "endingDate       : " + endingDate + "\n";
      affiche += "message          : " + message + "\n";
      affiche += "</pre></textarea></div>";
        
      popup = window.open('', 'popup', 'height=300, width=800, top=50, left=50, toolbar=no, directories=no, menubar=no, location=no, resizable=yes, scrollbars=yes, status=no');

      popup.document.write(affiche);
      popup.document.close();
      popup.document.focus(); 

       
   }

   function afficherTraitement(idJobTraitement) {
      document.forms['formulaireGestion'].elements['identifiantATraiter'].value = idJobTraitement;
      document.forms['formulaireGestion'].submit();
   }

</script>

</head>

<body onload="gererAffichageListeColonnes();">

<form:form name="formulaireGestion" method="post"
	modelAttribute="gererPileTravauxForm" action="gererPile.do">

	<h6 class="msg_erreur"><form:errors path='*' /></h6>
	<c:if test="${stacktrace !=null}">
		<p><textarea class="stacktrace" rows="15" cols="180"><c:out
			value="${stacktrace}" /></textarea></p>
	</c:if>
	<input type="hidden" name="action" id="action" />

	<div class="cadreForm">Sélectionner le mode d'affichage : <form:radiobutton
		id="modeMin" path="modeAffichage" value="minimal"
		onclick="cacherListeColonnes();" /> <label for="modeMin"
		class="inline">minimal</label> <form:radiobutton id="modeComplet"
		path="modeAffichage" value="complet" onclick="cacherListeColonnes();" />
	<label for="modeComplet" class="inline">complet</label> <form:radiobutton
		id="modePerso" path="modeAffichage" value="personnalise"
		onclick="afficherListeColonnes();" /> <label for="modePerso"
		class="inline">personnalisé</label>

	<div id="colonnes"><br />
	<a href="javascript:GereChkbox('colonnes');">Cocher/Décocher</a><br />
	<c:forEach items="${colonnes}" var="colonne">
		<form:checkbox path="colonnesAffichees" value="${colonne}"
			label="${colonne}" />
		<br />
	</c:forEach></div>

	<div style="padding-top: 5px">Nombre de lignes dans le tableau
	résultat : <form:input cssClass="inputNum" size="3"
		id="nbLignesTableau" path="nbLignesTableau" /></div>

	<div style="padding-top: 5px">Activer la visualisation des traitements relancés : 
		<form:radiobutton id="visuRelanceActive" path="visuRelance" value="true"/>
		<label for="visuRelanceActive" class="inline">Oui</label>
		<form:radiobutton id="visuRelanceInactive" path="visuRelance" value="false"/>
		<label for="visuRelanceInactive" class="inline">Non</label>
	</div>

	<!-- Critères de recherche -->
	<div style="padding-top: 5px"><a id="lienCriteres"
		href="javascript:afficherListeCriteres()"
		onclick="javascript:changerLien();">Afficher les critères de
	recherche</a> <c:if test="${filtreActif == true}"> (Filtre actif)</c:if>
	<div id="criteresRecherche" style="display: none">

	<table class="tableForm">
		<tr class="tableForm">
			<td class="tableForm"><label for="idJob"><b>Identifiant du
			job :</b></label></td>
			<td colspan="2"><form:input id="idJob"
				cssClass="inputText" path="identifiantATraiter" /></td>
		</tr>

		<tr class="tableForm">
			<td class="tableForm"><label for="dateCreationMin"><b>Date
			de création minimale :</b><span class="info">(JJ/MM/AAAA)</span></label></td>
			<td><form:input id="dateCreationMin"
				cssClass="inputText" path="dateDebut" /></td>
			<td class="tableForm"><label for="heureDebut">Heure
			début : </label><form:select cssClass="inputHeure" path="heureDebut"
				id="heureDebut" onchange="testHeure()">
				<form:option value="00" />
				<form:option value="01" />
				<form:option value="02" />
				<form:option value="03" />
				<form:option value="04" />
				<form:option value="05" />
				<form:option value="06" />
				<form:option value="07" />
				<form:option value="08" />
				<form:option value="09" />
				<form:option value="10" />
				<form:option value="11" />
				<form:option value="12" />
				<form:option value="13" />
				<form:option value="14" />
				<form:option value="15" />
				<form:option value="16" />
				<form:option value="17" />
				<form:option value="18" />
				<form:option value="19" />
				<form:option value="20" />
				<form:option value="21" />
				<form:option value="22" />
				<form:option value="23" />
				<form:option value="24" />
			</form:select> <form:select cssClass="inputHeure" path="minuteDebut"
				id="minuteDebut" onchange="testHeure()">
				<form:option value="00" />
				<form:option value="15" />
				<form:option value="30" />
				<form:option value="45" />
			</form:select></td>
		</tr>

		<tr class="tableForm">
			<td class="tableForm"><label for="dateCreationMax"><b>Date
			de création maximale :</b><span class="info">(JJ/MM/AAAA)</span></label></td>
			<td><form:input cssClass="inputText" id="dateCreationMax"
				path="dateFin" /></td>
			<td class="tableForm"><label for="heureFin">Heure fin : </label> <form:select
				cssClass="inputHeure" path="heureFin" id="heureFin"
				onchange="testHeure()">
				<form:option value="00" />
				<form:option value="01" />
				<form:option value="02" />
				<form:option value="03" />
				<form:option value="04" />
				<form:option value="05" />
				<form:option value="06" />
				<form:option value="07" />
				<form:option value="08" />
				<form:option value="09" />
				<form:option value="10" />
				<form:option value="11" />
				<form:option value="12" />
				<form:option value="13" />
				<form:option value="14" />
				<form:option value="15" />
				<form:option value="16" />
				<form:option value="17" />
				<form:option value="18" />
				<form:option value="19" />
				<form:option value="20" />
				<form:option value="21" />
				<form:option value="22" />
				<form:option value="23" />
				<form:option value="24" />
			</form:select> <form:select cssClass="inputHeure" path="minuteFin" id="minuteFin"
				onchange="testHeure()">
				<form:option value="00" />
				<form:option value="15" />
				<form:option value="30" />
				<form:option value="45" />
			</form:select></td>
		</tr>
	</table>
	
	<p class="double"><label for="type">Type :</label> <form:select
		id="type" path="type">
		<form:option value="TOUS">Sélectionner...</form:option>
		<form:option value="capture_masse" />
		<form:option value="capture_unitaire" />
	</form:select></p>


	<p class="double"><label for="contratService">Contrat de
	service :</label> <form:select id="contratService" path="contratService">
		<form:option value="TOUS">Sélectionner...</form:option>
		<c:forEach items="${listeCodesClient}" var="cs">
			<form:option value="${cs}">${cs}</form:option>
		</c:forEach>
	</form:select></p>

	<p class="double"><label for="parametres">Paramètre :</label> <form:select
		id="parametres" path="parameters" onchange="saiseParametre()">
		<form:option value="TOUS">Sélectionner...</form:option>
		<c:forEach items="${listeParametres}" var="p">
			<form:option value="${p}">${p}</form:option>
		</c:forEach>
		<form:option value="SAISIE">Autre</form:option>
	</form:select><br />
	</p>

	<p class="double" id="saisieParametre"><label for="parametre">Précisez</label>
	<form:input id="parametre" path="parametersSaisi" /></p>


	<p class="double"><label for="etat">Etat :</label> <form:select
		id="etat" path="state">
		<form:option value="TOUS">Sélectionner...</form:option>
		<form:option value="CREATED" />
		<form:option value="RESERVED" />
		<form:option value="STARTING" />
		<form:option value="SUCCESS" />
		<form:option value="FAILURE" />

	</form:select></p>

	<p class="double"><label for="hostSae">Host SAE :</label> <form:input
		id="hostSae" path="saeHost" /></p>

	<p class="double"><label for="hostClient">Host Client :</label> <form:input
		id="hostClient" path="clientHost" /></p>

	<p class="double"><label for="maxKeysToRead">Nombre de clés
	max pour la récupération des jobs :</label><form:input id="maxKeysToRead"
		path="maxKeysToRead"></form:input></p>

	</div>

	</div>

	<p style="text-align: right"><input id="boutonEffacer"
		style="display: none" type="button" value="Effacer filtre"
		name="bouton_vider" onclick="viderFiltre(${maxKeysToReadConf})" /> 
		<input
		type="submit" value="Rechercher" name="bouton_validation"
		onclick="javascript:formulaireGestion.action.value = 'rechercher'" />
	</p>

	</div>

	<c:if test="${historique != null}">
		<div style="padding-top: 15px"><display:table name="historique">
			<display:column property="date" title="Date"
				format="{0,date,dd/MM/yyyy HH:mm}" />
			<display:column property="trace"
				title="Trace du job ${identifiantATraiter}" />
		</display:table></div>
	</c:if>

	<!-- <form:hidden path="identifiantATraiter" /> -->


	<c:if test="${listeJobAffiches != null}">
		<br/>
		<table width="50%">
			<thead><tr><td></td><td>Total</td><td>CNP31</td><td>CNP69</td><td>CNP75</td><td>Autre</td></tr></thead>
			<tr>
				<td>Jobs en erreur</td>
				<td style='text-align:right'><c:out value="${nbJobEnErreur}"/></td> 
				<td style='text-align:right'><c:out value="${nbJobErreurCNP31}" /></td>
				<td style='text-align:right'><c:out value="${nbJobErreurCNP69}" /></td>
				<td style='text-align:right'><c:out value="${nbJobErreurCNP75}" /></td>
				<td style='text-align:right'><c:out value="${nbJobErreurAutre}" /></td>
			</tr>
			<c:if test="${gererPileTravauxForm.visuRelance}">
				<tr>
					<td>Jobs en erreur relancés</td>
					<td style='text-align:right'><c:out value="${nbTraitementRelance}"/></td> 
					<td style='text-align:right'><c:out value="${nbTraitementRelanceCNP31}" /></td>
					<td style='text-align:right'><c:out value="${nbTraitementRelanceCNP69}" /></td>
					<td style='text-align:right'><c:out value="${nbTraitementRelanceCNP75}" /></td>
					<td style='text-align:right'><c:out value="${nbTraitementRelanceAutre}" /></td>
				</tr>
				<tr>
					<td>Jobs en erreur relancés avec succès</td>
					<td style='text-align:right'><c:out value="${nbTraitementRelanceSucces}"/></td> 
					<td style='text-align:right'><c:out value="${nbTraitementRelanceSuccesCNP31}" /></td>
					<td style='text-align:right'><c:out value="${nbTraitementRelanceSuccesCNP69}" /></td>
					<td style='text-align:right'><c:out value="${nbTraitementRelanceSuccesCNP75}" /></td>
					<td style='text-align:right'><c:out value="${nbTraitementRelanceSuccesAutre}" /></td>
				</tr>
			</c:if>
			<tr>
				<td>Documents non archivés</td>
				<td style='text-align:right'><c:out value="${nbDocsNonArchive}" /></td>
				<td style='text-align:right'><c:out value="${nbDocsNonArchiveCNP31}" /></td>
				<td style='text-align:right'><c:out value="${nbDocsNonArchiveCNP69}" /></td>
				<td style='text-align:right'><c:out value="${nbDocsNonArchiveCNP75}" /></td>
				<td style='text-align:right'><c:out value="${nbDocsNonArchiveAutre}" /></td>
			</tr>
		</table>
		<div style="overflow: scroll; padding-top: 15px"><display:table
			name="listeJobAffiches" id="jobRow" requestURI="/gererPile.do"
			pagesize="${nbLignesTableau}" style="width: 90%"
			defaultsort="${numColonneDateCreation}" defaultorder="descending">

			<display:column style="width: 10; align:center" title="">
				<c:choose>
					<c:when test="${jobRow.removable == 'true'}">
						<input title="Supprimer le job" src="img/recycle_bin.png"
							type="image" value="Supprimer"
							onclick="return confirmerSuppression('${jobRow.idJob}')" />
					</c:when>
					<c:otherwise>
						<input
							style="cursor: default; filter: alpha(opacity =           40); opacity: 0.4;"
							title="Supprimer le job" src="img/recycle_bin.png" type="image"
							value="Supprimer"
							onclick="return confirmerSuppression('${jobRow.idJob}')"
							disabled="disabled" />
					</c:otherwise>
				</c:choose>

			</display:column>


			<display:column style="width: 10; align:center" title="">
				<input title="Voir l'historique" src="img/calendar.png" type="image"
					value="Historique"
					onclick="return voirHistorique('${jobRow.idJob}')" />
			</display:column>

			<display:column style="width: 10; align:center" title="">
				<a href="lancerComptageDoc.do?identifiant=${jobRow.idJob}"
					title="Comptage du nombre de documents archivés"> <img
					src="img/calculator.png"></img></a>
			</display:column>

			<display:column style="width: 10; align:center" title="">
				<fmt:formatDate value="${jobRow.creationDate}"
					pattern="dd/MM/yyyy HH:mm" var="dateCreation" />
				<fmt:formatDate value="${jobRow.reservationDate}"
					pattern="dd/MM/yyyy HH:mm" var="dateReservation" />
				<fmt:formatDate value="${jobRow.startingDate}"
					pattern="dd/MM/yyyy HH:mm" var="dateDebut" />
				<fmt:formatDate value="${jobRow.endingDate}"
					pattern="dd/MM/yyyy HH:mm" var="dateFin" />
				<input title="Exporter" src="img/page_copy.png" type="image"
					value="Export"
					onclick="return exporter('${jobRow.idJob}', '${dateCreation}', '${jobRow.parameters}', '${jobRow.state}', '${jobRow.saeHost}', '${jobRow.clientHost}', '${jobRow.docCount}', '${dateReservation}', '${jobRow.reservedBy}', '${dateDebut}', '${jobRow.pid}', '${dateFin}', '${jobRow.message}')" />
			</display:column>

			<display:column title="idJob">
				<c:choose>
					<c:when test="${jobRow.type == 'capture_masse'}">
						<a
							href="afficherDocuments.do?requeteLucene=IdTraitementMasseInterne:${jobRow.idJob}"
							title="Rechercher les documents">${jobRow.idJob}</a>
					</c:when>
					<c:otherwise>
						${jobRow.idJob}
					</c:otherwise>
				</c:choose>
			</display:column>


			<display:column property="creationDate" title="creationDate"
				sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
			<c:forEach items="${gererPileTravauxForm.colonnesAffichees}"
				var="colonne">
				<c:if test="${colonne == 'type'}">
					<display:column property="type" title="type" sortable="true" />
				</c:if>
				<c:if test="${colonne == 'parameters'}">
					<display:column title="parameters" sortable="true">
						<c:choose>
							<c:when test="${jobRow.jobParameters == null}">
								<i><c:out value="${jobRow.parameters}" /></i>
							</c:when>
							<c:otherwise>
								<custom:map value="${jobRow.jobParameters}"></custom:map>
							</c:otherwise>
						</c:choose>
					</display:column>
				</c:if>
				<c:if test="${colonne == 'state'}">
					<c:if test="${gererPileTravauxForm.visuRelance == true}">
						<c:choose>
							<c:when  test="${jobRow.state == 'SUCCESS'}">
								<display:column property="state" title="state" sortable="true" style="color:green;"/>
							</c:when>
							<c:when  test="${jobRow.state == 'FAILURE' && jobRow.listeJobRelance == null}">
								<display:column property="state" title="state" sortable="true" style="color:red;"/>
							</c:when>
							<c:when  test="${jobRow.state == 'FAILURE' && jobRow.listeJobRelance != null && jobRow.succesRelance == true}">
								<display:column property="state" title="state" sortable="true" style="color:blue;"/>
							</c:when>
							<c:when  test="${jobRow.state == 'FAILURE' && jobRow.listeJobRelance != null && jobRow.succesRelance == false}">
								<display:column property="state" title="state" sortable="true" style="color:orange;"/>
							</c:when>
							<c:otherwise>
								<display:column property="state" title="state" sortable="true"/>
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${gererPileTravauxForm.visuRelance == false}">			
						<c:choose>
							<c:when  test="${jobRow.state == 'SUCCESS'}">
								<display:column property="state" title="state" sortable="true" style="color:green;"/>
							</c:when>
							<c:when  test="${jobRow.state == 'FAILURE'}">
								<display:column property="state" title="state" sortable="true" style="color:red;"/>
							</c:when>
							<c:otherwise>
								<display:column property="state" title="state" sortable="true"/>
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:if>
				<c:if test="${gererPileTravauxForm.visuRelance == true}">
					<c:if test="${colonne == 'relance'}">
						<display:column title="relance" sortable="true">
							<c:choose>
								<c:when test="${jobRow.listeJobRelance != null}">
									<c:forEach items="${jobRow.listeJobRelance}" var="jobRelance">
										<a href="javascript:afficherTraitement('${jobRelance}');"  title="Voir le traitement">${jobRelance}</a>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<c:if test="${jobRow.state == 'FAILURE'}">
									Non
									</c:if>
								</c:otherwise>
							</c:choose>
						</display:column>
					</c:if>
				</c:if>
				
				<c:if test="${colonne == 'saeHost'}">
					<display:column property="saeHost" title="saeHost" sortable="true" />
				</c:if>
				<c:if test="${colonne == 'clientHost'}">
					<display:column property="clientHost" title="clientHost"
						sortable="true" />
				</c:if>
				<c:if test="${colonne == 'docCount'}">
					<display:column property="docCount" title="docCount"
						sortable="true" format="{0,number,#,###}" style='text-align:right' />
				</c:if>
				<c:if test="${colonne == 'reservationDate'}">
					<display:column property="reservationDate" title="reservationDate"
						sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
				</c:if>
				<c:if test="${colonne == 'reservedBy'}">
					<display:column property="reservedBy" title="reservedBy"
						sortable="true" />
				</c:if>
				<c:if test="${colonne == 'startingDate'}">
					<display:column property="startingDate" title="startingDate"
						sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
				</c:if>
				<c:if test="${colonne == 'pid'}">
					<display:column property="pid" title="pid" sortable="true" />
				</c:if>
				<c:if test="${colonne == 'endingDate'}">
					<display:column property="endingDate" title="endingDate"
						sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
				</c:if>
				<c:if test="${colonne == 'message'}">
					<display:column property="message" title="message" sortable="true" />
				</c:if>
				<c:if test="${colonne == 'toCheckFlag'}">
					<display:column property="toCheckFlag" title="toCheckFlag"
						sortable="true" />
				</c:if>
				<c:if test="${colonne == 'toCheckFlagRaison'}">
					<display:column property="toCheckFlagRaison"
						title="toCheckFlagRaison" sortable="true" />
				</c:if>
				<c:if test="${colonne == 'VI'}">
					<display:column property="vi.codeAppli" title="VI - Code Appli"
						sortable="true"></display:column>
					<display:column property="vi.idUtilisateur"
						title="VI - Utilisateur"></display:column>
					<c:forEach items="${jobRow.vi.saeDroits}" var="droit">
						<display:column title="VI - Droits ${droit.key}">
							<c:forEach var="saeprmd" items="${droit.value}">code:&nbsp;${saeprmd.prmd.code}<br />paramètres:&nbsp;${saeprmd.values}<br />lucene:&nbsp;${saeprmd.prmd.lucene}<br />metadonnées:&nbsp;${saeprmd.prmd.metadata}<br />
							</c:forEach>
						</display:column>
					</c:forEach>
				</c:if>

				</c:forEach>
				<display:footer>
    				<tr>
      					<td colspan=30>Total documents : <c:out value="${docCountTotal}"/></td>
    				</tr>
  				</display:footer> 

		</display:table>

		</div>
	</c:if>

</form:form>

</body>
</html>


