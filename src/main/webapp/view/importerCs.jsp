<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/map.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<title>IHM Web Exploit - Consultation des Dictionnaires</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />
<link href="css/calendrier.css" rel="stylesheet" media="screen"
	type="text/css" title="Design" />

<script type="text/javascript" src="js/calendrier.js"></script>

<script type="text/javascript">
	// Cocher / Tous décocher (pour la liste des métadonnées à afficher)
	var isCoche = false;

	function GereChkbox() {
		blnEtat = isCoche ? false : true;
		this.cocher(blnEtat);
		isCoche = blnEtat;
	}

	function cocher(aFaire) {
		var Chckbox = document.getElementsByName("codeCsToImport");
		for (var i = 0; i < Chckbox.length; i++) {
			if (Chckbox[i].nodeName == "INPUT") {
				if (Chckbox[i].getAttribute("type") == "checkbox") {
					document.getElementById(Chckbox[i].getAttribute("id")).checked = aFaire;
				}
			}
		}
	}

	// Confirmation de l'import des contrats de service sélectionnés
	function importCS() {
		var listCodesCsSession = document.getElementById("codeCsSession").value
				.toString();
		var listCsToImport = document.getElementsByName("codeCsToImport");
		var codeCsToImport = [];
		var listCodeCsExistant = [];
		var checkConfirmImport = false;

		// Récuperer les cs sélectionnés
		if (listCsToImport != undefined) {
			for (var i = 0; i < listCsToImport.length; i++) {
				if (listCsToImport[i].checked) {
					codeCsToImport.push(listCsToImport[i].value);
				}
			}
		}
		if (codeCsToImport.length == 0) {
			alert('Veuillez sélectionner au moins un contrat de service à importer !');
		} else {
			// Chargement des CS déjà exisants
			var codesCsSession = listCodesCsSession.substr(1,
					listCodesCsSession.length - 1);
			var listCodeCsSession = codesCsSession.split(",");
			for (var j = 0; j < codeCsToImport.length; j++) {
				if (listCodeCsSession.toString().indexOf(
						codeCsToImport[j].toString()) >= 0) {
					checkConfirmImport = true;
					listCodeCsExistant.push(codeCsToImport[j]);
				}
			}
			// Si des CS à importer existent déjà 
			if (checkConfirmImport) {
				if (confirm('Le(s) contrat(s) de service: ' + listCodeCsExistant
						+ ' existe(nt) déjà ! '
						+ ' Voulez-vous vraiment le(s) écraser ? ')) {
					document.getElementById("action").value = "importCs";
					document.forms['csForm'].submit();

				} else {
					// Ne rien faire
				}
			} else {
				document.getElementById("action").value = "importCs";
				document.forms['csForm'].submit();
			}			
		}

	}

	// Confirmation de l'export des CS sélectionnés
	function exportCS() {
		var listCsToImport = document.getElementsByName("codeCsToImport");
		// TODO
		document.forms['csForm'].elements['listCsToLoad'].value = document
				.getElementsByName("listeCs").value;
		alert('En cours de Développement...');
		document.getElementById("action").value = "exportCs";
		// document.forms['csForm'].submit();
	}

	// Déclenche le chargement des contrats de services
	function loadCs() {
		document.forms['csForm'].elements['action'].value = 'loadListCs';
		document.forms['csForm'].submit();
	}

	// Confirmation de l'import de CS avec succès
	function confirmImportCs() {
		var action = document.getElementById("action").value;
		var listCsToImport = document.getElementsByName("codeCsToImport");
		var codeCsToImport = [];

		// Récuperer les cs sélectionnés
		if (listCsToImport != undefined) {
			for (var i = 0; i < listCsToImport.length; i++) {
				if (listCsToImport[i].checked) {
					codeCsToImport.push(listCsToImport[i].value);
				}
			}
		}
		
		if (action === 'importCs' && listCsToImport != undefined
				&& codeCsToImport !='') {
			if(codeCsToImport.length > 1 ){
				alert('Les contrats de service ' + codeCsToImport
						+ ' ont bien été importés');
			}else if(codeCsToImport.length === 1){
				alert('Le contrat de service ' + codeCsToImport
						+ ' a bien été importé');
			}
		}
	}
	
</script>
</head>

<body>
	<table class="ds_box" cellpadding="0" cellspacing="0" id="ds_conclass"
		style="display: none;">
		<tr>
			<td id="ds_calclass"></td>
		</tr>
	</table>

	<form:form method="post" modelAttribute="csForm" name="csForm"
		id="csForm" action="lancerImportCs.do">

		<h6 class="msg_erreur" id="erreur">
			<form:errors path='*' />
		</h6>
		<c:if test="${stacktrace !=null}">
			<p>
				<textarea class="stacktrace" rows="15" cols="180"><c:out
						value="${stacktrace}" /></textarea>
			</p>
		</c:if>

		<form:input type="hidden" name="action" id="action" path="action" />
		<form:input type="hidden" name="codeCsSession" id="codeCsSession"
			path="codeCsSession" />

		<div style="display: none;">
			<form:select cssClass="inputSelect" id="listCsSession"
				name="listCsSession" path="listCsSession">
				<form:options style="display: none;" items="${CodeCsSession}" />
			</form:select>
		</div>

		<div style="display: none;">
			<form:select cssClass="inputSelect" id="listCsToLoad"
				name="listCsToLoad" path="listCsToLoad">
				<form:options style="display: none;" items="${listCs}" />
			</form:select>
		</div>

		<div class="cadreForm">
			<table class="tableForm">
				<tr class="tableForm">
					<td colspan="2" class="tableFormGauche"><label for="modeTous">Sélectionnez
							l'environnement</label> <form:select cssClass="inputSelect"
							id="choixConfig" path="envCsToImport" onchange="loadCs();">
							<form:options items="${listeNomConf}" />
						</form:select></td>
				</tr>
			</table>

			<p style="text-align: right">
				<input type="button" id="exportButton" value="Exporter"
					onclick="exportCS()" /> <input type="button" id="importButton"
					value="Importer" onclick="importCS()" />
			</p>
		</div>
		<div>
			<br />
		</div>

		<c:if test="${not empty listeCs}">
			<display:table name="listeCs" id="csRow" style="width: 90%"
				defaultorder="ascending" pagesize="${nbLignesTableau}"
				requestURI="/lancerImportCs.do">

				<c:set var="pagms" value="${csRow.saePagms}"></c:set>

				<display:column style="text-align:center"
					title="<input type='checkbox' onClick='javascript:GereChkbox();'/>">
					<form:checkbox path="codeCsToImport" name="codeCsToImport"
						value="${csRow.codeClient}" />
				</display:column>
				<display:column title="Code client" sortable="true">
					<c:out value="${csRow.codeClient}" />
				</display:column>
				<display:column title="liste des PAGM">
					<c:forEach items="${pagms}" var="pagm" end="5">
						<c:out value="[${pagm.code}] " />
					</c:forEach>
					<c:if test="${fn:length(pagms) > 5}">
						<c:out value="..." />
					</c:if>
				</display:column>

			</display:table>

		</c:if>
	</form:form>

	<script type="text/javascript">
		confirmImportCs();
	</script>

</body>
</html>
