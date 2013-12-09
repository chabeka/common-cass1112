<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>IHM Web Exploit - Consultation des journaux</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />
<link href="css/calendrier.css" rel="stylesheet" media="screen" type="text/css" title="Design"  />

<script type="text/javascript" src="js/calendrier.js"></script>
<script type="text/javascript">

   function selectionnerParId() {
      document.getElementById("modeIdentifiant").checked = true;
      majListeDeroulante();
   }

   function selectionnerParDates() {
      document.getElementById("modeDate").checked = true;
      majListeDeroulante();
   }

   function selectionnerParIdentifiantJournal() {
      document.getElementById("modeIdentifiantJournal").checked = true;
      majListeDeroulante();
   }

   function majListeDeroulante() {
      if (document.getElementById("modeIdentifiant").checked == true) {
		document.getElementById("journalType").value = "JOURNAL_CYCLE_VIE";
		document.getElementById("journalType").disabled = true;
		document.getElementById("journalTypeHidden").value = "JOURNAL_CYCLE_VIE";
		
      } else {
         document.getElementById("journalType").disabled = false;
         document.getElementById("journalTypeHidden").value = "";
      }
      if (document.getElementById("journalType").value == "JOURNAL_EVENEMENT_SAE") {
         document.getElementById("verifieChainage").disabled = true;
         document.getElementById("verifieChainage").checked = false;
      } else {
         document.getElementById("verifieChainage").disabled = false;
      }
   }

   function verificationForm() {
      
      if (document.getElementById("modeIdentifiant").checked == true) {
         document.getElementById("journalTypeHidden").value = document.getElementById("journalType").value;
		if (document.getElementById("documentUuid").value == "") {
			document.getElementById("erreur").innerHTML = "L'identifiant du document doit être renseigné";	
			return false;
		}
      } else {
         document.getElementById("journalTypeHidden").disabled = true;
      }
      if (document.getElementById("modeDate").checked == true) {
   		if (document.getElementById("dateDebut").value == "") {
   			document.getElementById("erreur").innerHTML = "La date de début doit être renseignée";	
   			return false;
   		}
   		if (document.getElementById("dateFin").value == "") {
   			document.getElementById("erreur").innerHTML = "La date de fin doit être renseignée";	
   			return false;
   		}

      }
      if (document.getElementById("modeIdentifiantJournal").checked == true) {
         document.getElementById("formulaire").action="contenuJournal.do";
      } else {
         document.getElementById("formulaire").action = "lancerConsultJournaux.do";
      }
         		
      return true;
   }

   function initForm() {
      if (document.getElementById("modeIdentifiant").checked == true) {
   		document.getElementById("journalType").value = "JOURNAL_CYCLE_VIE";
   		document.getElementById("journalType").disabled = true;
   		document.getElementById("journalTypeHidden").value = "JOURNAL_CYCLE_VIE";
      }

      if (document.getElementById("journalType").value == "JOURNAL_EVENEMENT_SAE") {
         document.getElementById("verifieChainage").disabled = true;
         document.getElementById("verifieChainage").checked = false;
      } else {
         document.getElementById("verifieChainage").disabled = false;
      }

   }

</script>
		
</head>

<body onload="initForm()">


<table class="ds_box" cellpadding="0" cellspacing="0" id="ds_conclass" style="display: none;">
	<tr>
		<td id="ds_calclass"></td>
	</tr>
</table>


<form:form id="formulaire" method="post" modelAttribute="journauxForm"
	action="lancerConsultJournaux.do" onsubmit="return verificationForm()">

	<h6 class="msg_erreur" id="erreur"><form:errors path='*' /></h6>
	<c:if test="${stacktrace !=null}">
		<p><textarea class="stacktrace" rows="15" cols="180"><c:out value="${stacktrace}"/></textarea></p>
	</c:if>
	<div class="cadreForm">

	<p>Sélectionner le mode de recherche : </p>
	
	<table class="tableForm">
	<tr class="tableForm">
	<td colspan="2">
		<form:radiobutton id="modeDate" path="modeRecherche" value="parDates" onchange="majListeDeroulante()"/>
		<label for="modeDate" class="inline">Par dates des évenements :</label>

	<table class="tableForm" cellpadding="0" cellspacing="0" border="0">
	<tr class="tableForm">
		<td class="tableForm">
			<label for="dateDebut">Date de début</label>
		</td>
		<td>
			<form:input cssClass="inputDate" id="dateDebut" path="dateDebut" size="5" onclick="selectionnerParDates();"/>
		</td>
		<td><img src="./img/calendar.png" title="Voir le calendrier" alt="Voir le calendrier" onclick="selectionnerParDates();ds_sh(dateDebut);" onmouseover="this.style.cursor='pointer';"/></td>
		<td class="tableForm">
			<label for="dateFin">Date fin </label>
		</td>
		<td> 
			<form:input	cssClass="inputDate" id="dateFin" path="dateFin" size="5" onclick="selectionnerParDates();"/>
		</td>
		<td><img src="./img/calendar.png" title="Voir le calendrier" alt="Voir le calendrier" onclick="selectionnerParDates();ds_sh(dateFin);" onmouseover="this.style.cursor='pointer';"/></td>
		</tr>
	</table>
	</td>
	</tr>
	<tr class="tableForm">
	<td>
	<form:radiobutton id="modeIdentifiant" path="modeRecherche" value="parIdentifiant" onchange="majListeDeroulante()" />
	<label for="modeIdentifiant" class="inline">Par identifiant du document : </label>
	</td>
	<td> 
	<form:input id="documentUuid" cssClass="inputText" path="documentUuid" onclick="selectionnerParId()"/>
	</td>
	</tr>
	<tr class="tableForm">
	<td>	
	<form:radiobutton id="modeIdentifiantJournal" path="modeRecherche" value="parIdentifiantJournal" onchange="majListeDeroulante()" />
	<label for="modeIdentifiantJournal" class="inline">Par identifiant du journal : </label>
	</td>
	<td> 
	<form:input id="journalUuid" cssClass="inputText" path="journalUuid" onclick="selectionnerParIdentifiantJournal()"/>
	</td>
	</tr>	
	</table>	
	
	<br/>		
	<table class="tableForm" cellpadding="0" cellspacing="0" border="0">
		<tr class="tableForm"> 
			<td class="tableForm"><label for="journalType"><b>Type de journal</b></label></td>
			<td>
				<form:hidden path="listeJournalType" id="journalTypeHidden"/>
				<form:select cssClass="inputSelectPetit" id="journalType" path="listeJournalType" onchange="majListeDeroulante()">
					<form:options itemLabel="description" />		
				</form:select>
			</td>
			<td class="tableForm"><form:checkbox path="verifieChainage" id="verifieChainage"/></td>
			<td><label for="verifieChainage">Vérifier le chainage</label></td>
		</tr>
	</table>
	
	
	<p style="text-align: right"><input type="submit" value="Consulter"/></p>
	</div>



<c:if test="${listeJournaux != null}">
	<p>
	<display:table name="listeJournaux" id="journalRow" requestURI="/lancerConsultJournaux.do"
		 style="width: 70%"
		 defaultorder="descending" defaultsort="2">

		<display:column title="Identifiant journal" sortable="true">
			<a href="contenuJournal.do?journalUuid=${journalRow.identifiant}&listeJournalType=${journauxForm.listeJournalType}&nomFichier=${journalRow.nomFichier}" title="Télécharger le journal">${journalRow.identifiant}</a>
		</display:column>
		<c:if test="${journauxForm.listeJournalType == 'JOURNAL_EVENEMENT_SAE'}">
			<display:column property="dateDebutEvt" title="Date des événements" sortable="true" format="{0,date,dd/MM/yyyy}" />
		</c:if>
		<c:if test="${journauxForm.listeJournalType != 'JOURNAL_EVENEMENT_SAE'}">
			<display:column property="dateDebutEvt" title="Date de début des événements" sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
			<display:column property="dateFinEvt" title="Date de fin des événements" sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
		</c:if>
		<display:column property="date" title="Date de création du journal" sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
		
		
	</display:table>
	</p>
</c:if>


<c:if test="${listeChainages != null}">
	<display:table name="listeChainages" id="chainageRow" requestURI="/lancerConsultJournaux.do"
		 style="width: 100%"
		 defaultorder="descending" defaultsort="2">
		<display:column title="Identifiant unique du journal précédent" sortable="true">
			<a href="contenuJournal.do?journalUuid=${chainageRow.uuidPrecedentJournal}&listeJournalType=${journauxForm.listeJournalType}" title="Télécharger le journal">${chainageRow.uuidPrecedentJournal}</a>
		</display:column>
		<display:column property="algoHash" title="Algorithme hash" sortable="true" />
		<display:column property="hash" title="Hash" sortable="true" />
		<display:column property="hashRecalcule" title="Hash recalculé" sortable="true" />
		<display:column property="dateFin" title="Date de fin" sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
	</display:table>
</c:if>


</form:form>
</body>
</html>


