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
<title>IHM Web Exploit - Gestion des Contrat de service</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />
<link href="css/calendrier.css" rel="stylesheet" media="screen"
	type="text/css" title="Design" />

<script type="text/javascript" src="js/calendrier.js"></script>
<script type="text/javascript">

   function submitForm(action) {

      if (action == 'modifier') {
         document.getElementById('modeLoad').value = 'modeLoad';
         document.forms[1].action = 'gestionCs.do?action=modifier';
         document.forms[1].submit();
         return true;
      } else if (action == 'ajouter') {
         document.getElementById('modeLoad').value = 'modeLoad';
         document.forms[1].action = 'gestionCs.do?action=ajouter';
         document.forms[1].submit();
         return true;
      }else if (action == 'charger') {
         document.getElementById('modeLoad').value = 'modeLoad';
         document.forms[0].action = 'gestionCs.do?action=charger';
         document.forms[0].submit();
         return true;
      }
   }
   
</script>


</head>

<body>

<form:form method="post" modelAttribute="csForm"
	enctype="multipart/form-data">


	<h5 class="msg_erreur" id="erreur"><form:errors path='*' /></h5>
	<c:if test="${stacktrace !=null}">
		<p><textarea class="stacktrace" rows="15" cols="180"><c:out
			value="${stacktrace}" /></textarea></p>
	</c:if>

	<p><c:if test="${csForm.message !=null}">
		<c:forEach items="${csForm.message}" var="ligne">
			<c:out value="${ligne}"></c:out>
			<br />
		</c:forEach>
	</c:if></p>

	<c:if test="${empty detailCs}">
		<table>
			<tr class="tableForm">
				<td colspan="3"><form:hidden path="modeCreation" id="modeLoad" />
				<label for="modeLoad">Création / Modification d'un CS à
				partir d'un fichier : </label></td>
			</tr>
			<tr class="tableForm">
				<td class="tableFormGauche"><label>Sélectionner votre
				fichier : </label></td>
				<td><form:input id="file" type="file" path="file"
					cssClass="inputText" /></td>
				<td><input type="submit" value="charger"
					onclick="return submitForm('charger')" /></td>
			</tr>
		</table>
	</c:if>
</form:form>

<form:form id="cs" method="post" modelAttribute="csForm">
	<form:hidden id="droit" path="droit" />
	<form:hidden id="ajout" path="ajout" />
	<form:hidden id="store" path="store" />

	<c:if test="${not empty droitCs}">
		<p>Contrat de service :</p>
		<display:table name="droitCs" id="cs" style="width: 95%"
			defaultorder="ascending" pagesize="${nbLignesTableau}"
			requestURI="/gestionCs.do">
			<display:column title="Issuer" sortable="true">
				<c:out value="${cs.issuer}" />
			</display:column>
			<display:column title="Description">
				<c:out value="${cs.description}" />
			</display:column>

			<display:column title="PKI">
				<c:forEach items="${cs.lstCnPki.cnPki}" var="pki">
					<c:out value="${pki}" />
					<br />
				</c:forEach>
			</display:column>
			<display:column title="Certificats">
				<c:forEach items="${cs.lstCnCert.cnCert}" var="cert">
					<c:out value="${cert}" />
					<br />
				</c:forEach>
			</display:column>
			<display:column title="Durée VI">
				<c:out value="${cs.viduree}" />
			</display:column>
			<display:column title="Vérif certificat">
				<c:if test="${cs.verifCnCert==true}">
					<c:out value="Oui" />
				</c:if>
				<c:if test="${cs.verifCnCert==false}">
					<c:out value="Non" />
				</c:if>
			</display:column>
		</display:table>
	</c:if>

	<c:if test="${not empty cs.pagms.pagm}">

		<p>PAGM :</p>
		<c:set var="listePagms" value="${cs.pagms.pagm}"></c:set>
		<display:table name="${listePagms}" id="pagm" style="width:95%"
			requestURI="/detailCs.do">
			<display:column title="Code PAGM" sortable="true">
				<c:out value="${pagm.code}" />
			</display:column>
			<c:set var="listeParametres" value="${pagm.parametres.parametre}"></c:set>
			<display:column title="Paramètres">
				<c:forEach items="${listeParametres}" var="parametrePagm">
					<c:out value="${parametrePagm.code}" /> : 
							<c:out value="${parametrePagm.valeur}" />
				</c:forEach>
			</display:column>
			<c:set var="listeActions" value="${pagm.pagma.actions}"></c:set>
			<display:column title="Actions">

				<c:forEach items="${listeActions.action}" var="action">
					<c:out value="${action}" />
					<br />
				</c:forEach>
			</display:column>


			<display:column title="PRMD" sortable="true">
				<c:out value="${pagm.pagmp.prmd}" />
			</display:column>

			<display:column title="Profil de contrôle du format" sortable="true">
				<c:out value="${pagm.pagmf.codeFormatControlProfil}" />
			</display:column>

		</display:table>
	</c:if>

	<c:if test="${not empty droitPrmd}">

		<p>PRMD :</p>
		<display:table name="droitPrmd" id="prmd" style="width: 95%"
			defaultorder="ascending" pagesize="${nbLignesTableau}"
			requestURI="/gestionCs.do">
			<display:column title="Code" sortable="true">
				<c:out value="${prmd.code}" />
			</display:column>
			<display:column title="Description">
				<c:out value="${prmd.description}" />
			</display:column>
			<c:set var="listeMeta" value="${prmd.metadonnees.metadonnee}"></c:set>
			<display:column title="Metadonnées">

				<display:table name="${listeMeta}" id="meta" style="width: 100%">
					<display:column title="Code">
						<c:out value="${meta.code}" />
					</display:column>
					<display:column title="Valeurs">
						<c:forEach items="${meta.valeurs.valeur}" var="valeur">
							<c:out value="${valeur}" />
						</c:forEach>
					</display:column>
				</display:table>
			</display:column>
			<display:column title="Lucene" sortable="true">
				<c:out value="${prmd.lucene}" />
			</display:column>
		</display:table>
	</c:if>


	<c:if test="${not empty formatControlProfil}">

		<p>Profil de contrôle du format :</p>
		<display:table name="formatControlProfil" id="fcp" style="width: 95%"
			defaultorder="ascending" pagesize="${nbLignesTableau}"
			requestURI="/gestionCs.do">

			<display:column title="Code" sortable="true">
				<c:out value="${fcp.code}" />
			</display:column>
			<display:column title="Description">
				<c:out value="${fcp.description}" />
			</display:column>


			<display:column title="Profil de contrôle">
				<c:set var="cp" value="${fcp.controlProfil}"></c:set>

				<display:table style="width: 100%" name="${cp}" id="detailCp"
					class="simple sublist">
					<display:column title="Identification du format">
						<c:if test="${detailCp.formatIdentification == true}">
							Oui
						</c:if>
						<c:if test="${detailCp.formatIdentification == false}">
							Non
						</c:if>
					</display:column>
					<display:column title="Validation du format">
						<c:if test="${detailCp.formatValidation == true}">
							Oui
						</c:if>
						<c:if test="${detailCp.formatValidation == false}">
							Non
						</c:if>
					</display:column>
					<display:column title="Mode de validation">
						<c:out value="${detailCp.formatValidationMode}" />
					</display:column>
					<display:column title="Format du fichier">
						<c:out value="${detailCp.fileFormat}" />
					</display:column>
				</display:table>

			</display:column>
		</display:table>
	</c:if>




	<c:if test="${not empty droitAjoutPagm}">
		<p>Ajout de PAGM :</p>
		<display:table name="droitAjoutPagm" id="ajoutPagm" style="width: 95%"
			defaultorder="ascending" pagesize="${nbLignesTableau}"
			requestURI="/gestionCs.do">
			<display:column title="csIssuer" sortable="true">
				<c:out value="${ajoutPagm.csIssuer}" />
			</display:column>



			<display:column title="Pagms">
				<c:set var="listePagms" value="${ajoutPagm.pagms.pagm}"></c:set>
				<display:table style="width:100%" name="${listePagms}" id="pagm">
					<display:column title="Code PAGM" sortable="true">
						<c:out value="${pagm.code}" />
					</display:column>
					<display:column title="Description">
						<c:out value="${pagm.description}" />
					</display:column>
					<c:set var="listeParametres" value="${pagm.parametres.parametre}"></c:set>
					<display:column title="Paramètres">
						<c:forEach items="${listeParametres}" var="parametrePagm">
							<c:out value="${parametrePagm.code}" /> : 
							<c:out value="${parametrePagm.valeur}" />
						</c:forEach>
					</display:column>
					<c:set var="listeActions" value="${pagm.pagma.actions}"></c:set>
					<display:column title="Actions">
						<c:forEach items="${listeActions.action}" var="action">
							<c:out value="${action}" />
							<br />
						</c:forEach>
					</display:column>
					<c:set var="codePrmd" value="${pagm.pagmp.prmd}"></c:set>
					<display:column title="PRMD" sortable="true">
						<c:out value="${codePrmd}" />
					</display:column>
					<display:column title="Profil de contrôle du format"
						sortable="true">
						<c:out value="${pagm.pagmf.codeFormatControlProfil}" />
					</display:column>
				</display:table>
			</display:column>





		</display:table>
	</c:if>

	<c:if test="${not empty droitModifPagm}">
		<p>Modification de PAGM :</p>
		<display:table name="droitModifPagm" id="modifPagm" style="width: 95%"
			defaultorder="ascending" pagesize="${nbLignesTableau}"
			requestURI="/gestionCs.do">
			<display:column title="csIssuer" sortable="true">
				<c:out value="${modifPagm.csIssuer}" />
			</display:column>

			<display:column title="Pagms">
				<c:set var="listePagms" value="${modifPagm.pagms.pagm}"></c:set>
				<display:table style="width:100%" name="${listePagms}" id="pagm">
					<display:column title="Code PAGM" sortable="true">
						<c:out value="${pagm.code}" />
					</display:column>
					<display:column title="Description">
						<c:out value="${pagm.description}" />
					</display:column>
					<c:set var="listeParametres" value="${pagm.parametres.parametre}"></c:set>
					<display:column title="Paramètres">
						<c:forEach items="${listeParametres}" var="parametrePagm">
							<c:out value="${parametrePagm.code}" /> : 
							<c:out value="${parametrePagm.valeur}" />
						</c:forEach>
					</display:column>
					<c:set var="listeActions" value="${pagm.pagma.actions}"></c:set>
					<display:column title="Actions">
						<c:forEach items="${listeActions.action}" var="action">
							<c:out value="${action}" />
							<br />
						</c:forEach>
					</display:column>
					<c:set var="codePrmd" value="${pagm.pagmp.prmd}"></c:set>
					<display:column title="PRMD" sortable="true">
						<c:out value="${codePrmd}" />
					</display:column>
					<display:column title="Profil de contrôle du format"
						sortable="true">
						<c:out value="${pagm.pagmf.codeFormatControlProfil}" />
					</display:column>
				</display:table>
			</display:column>
		</display:table>
	</c:if>

	<c:if test="${not empty droitSuppPagm}">
		<p>Suppression de PAGM :</p>
		<display:table name="droitSuppPagm" id="pagm" style="width: 95%"
			defaultorder="ascending" pagesize="${nbLignesTableau}"
			requestURI="/gestionCs.do">
			<display:column title="csIssuer" sortable="true">
				<c:out value="${pagm.csIssuer}" />
			</display:column>

			<display:column title="Pagms">
				<c:set var="codePagm" value="${pagm.codesPagm.codePagm}"></c:set>

				<c:forEach items="${codePagm}" var="code">
					<c:out value="${code}" />
					<br />
				</c:forEach>

			</display:column>
		</display:table>
	</c:if>


	<c:if test="${not empty droitModifPrmd}">

		<p>Modification de PRMD :</p>
		<display:table name="droitModifPrmd" id="prmd" style="width: 95%"
			defaultorder="ascending" pagesize="${nbLignesTableau}"
			requestURI="/gestionCs.do">
			<display:column title="Code" sortable="true">
				<c:out value="${prmd.code}" />
			</display:column>
			<display:column title="Description">
				<c:out value="${prmd.description}" />
			</display:column>
			<c:set var="listeMeta" value="${prmd.metadonnees.metadonnee}"></c:set>
			<display:column title="Metadonnées">

				<display:table name="${listeMeta}" id="meta" style="width: 100%">
					<display:column title="Code">
						<c:out value="${meta.code}" />
					</display:column>
					<display:column title="Valeurs">
						<c:forEach items="${meta.valeurs.valeur}" var="valeur">
							<c:out value="${valeur}" />
						</c:forEach>
					</display:column>
				</display:table>
			</display:column>
			<display:column title="Lucene" sortable="true">
				<c:out value="${prmd.lucene}" />
			</display:column>
		</display:table>
	</c:if>

	<c:if test="${not empty droitModifFormatControlProfil}">

		<p>Modification de profil de contrôle du format :</p>
		<display:table name="droitModifFormatControlProfil" id="mfcp"
			style="width: 95%" defaultorder="ascending"
			pagesize="${nbLignesTableau}" requestURI="/gestionCs.do">

			<display:column title="Code" sortable="true">
				<c:out value="${mfcp.code}" />
			</display:column>
			<display:column title="Description">
				<c:out value="${mfcp.description}" />
			</display:column>


			<display:column title="Profil de contrôle">
				<c:set var="cp" value="${mfcp.controlProfil}"></c:set>

				<display:table style="width: 100%" name="${cp}" id="detailCp"
					class="simple sublist">
					<display:column title="Identification du format">
						<c:if test="${detailCp.formatIdentification == true}">
							Oui
						</c:if>
						<c:if test="${detailCp.formatIdentification == false}">
							Non
						</c:if>
					</display:column>
					<display:column title="Validation du format">
						<c:if test="${detailCp.formatValidation == true}">
							Oui
						</c:if>
						<c:if test="${detailCp.formatValidation == false}">
							Non
						</c:if>
					</display:column>
					<display:column title="Mode de validation">
						<c:out value="${detailCp.formatValidationMode}" />
					</display:column>
					<display:column title="Format du fichier">
						<c:out value="${detailCp.fileFormat}" />
					</display:column>
				</display:table>

			</display:column>
		</display:table>
	</c:if>

	<c:if test="${not empty detailCs}">
		<p><a href="javascript:window.history.go(-1)">[ Retour ]</a></p>
		<p>Contrat de service :</p>
		<display:table name="detailCs" id="cs" style="width: 95%"
			defaultorder="ascending" pagesize="${nbLignesTableau}"
			requestURI="/detailCs.do">
			<display:column title="Issuer">
				<c:out value="${cs.codeClient}" />
			</display:column>
			<display:column title="Description">
				<c:out value="${cs.description}" />
			</display:column>
			<display:column title="PKI">
				<c:forEach items="${cs.listPki}" var="pki">
					<c:out value="${pki}" />
					<br />
				</c:forEach>
				<c:out value="${cs.idPki}" />
			</display:column>
			<display:column title="Certificats">
				<c:forEach items="${cs.listCertifsClient}" var="cert">
					<c:out value="${cert}" />
					<br />
				</c:forEach>
				<c:out value="${cs.idCertifClient}" />
			</display:column>
			<display:column title="Durée VI">
				<c:out value="${cs.viDuree}" />
			</display:column>
			<display:column title="Vérif certificat">
				<c:if test="${cs.verifNommage==true}">
					<c:out value="Oui" />
				</c:if>
				<c:if test="${cs.verifNommage==false}">
					<c:out value="Non" />
				</c:if>
			</display:column>
		</display:table>

		<p>PAGM :</p>
		<c:set var="listePagms" value="${cs.saePagms}"></c:set>
		<display:table name="${listePagms}" id="pagm" style="width:95%"
			requestURI="/detailCs.do">
			<display:column title="Code PAGM" sortable="true">
				<c:out value="${pagm.code}" />
			</display:column>
			<display:column title="Paramètres">
				<c:out value="${pagm.parametres}" />
			</display:column>
			<display:column title="Actions">
				<c:set var="listeActions" value="${pagm.pagma.actionUnitaires}"></c:set>
				<c:forEach items="${listeActions}" var="action">
					<c:out value="${action}" />
					<br />
				</c:forEach>
			</display:column>


			<display:column title="PRMD" sortable="true">
				<c:out value="${pagm.pagmp.prmd}" />
			</display:column>

			<display:column title="Profil de contrôle du format" sortable="true">
				<c:out value="${pagm.pagmf.formatProfile}" />
			</display:column>

		</display:table>

		<p>PRMD :</p>

		<c:set var="listePrmd" value="${detailCs.saePrmds}"></c:set>
		<display:table name="${listePrmd}" id="saePrmd" style="width:95%"
			requestURI="/detailCs.do">

			<display:column title="Code PRMD" sortable="true">
				<c:out value="${saePrmd.prmd.code}" />
			</display:column>
			<display:column title="Description">
				<c:out value="${saePrmd.prmd.description}" />
			</display:column>
			<display:column title="Métadonnées">
				<table width="100%">

					<c:set var="listeMeta" value="${saePrmd.prmd.metadata}"></c:set>
					<c:forEach items="${listeMeta}" var="meta">
						<tr>
							<td><c:out value="${meta.key}" /></td>
							<td><c:forEach items="${meta.value}" var="valeur">
								<c:out value="${valeur}" />
								<br />
							</c:forEach></td>
						</tr>
					</c:forEach>

				</table>

			</display:column>
			<display:column title="Requête Lucene">
				<c:out value="${saePrmd.prmd.lucene}" />
			</display:column>

		</display:table>

		<p>Profil de contrôle du format :</p>

		<c:set var="listeFcp" value="${detailCs.formatControlProfils}"></c:set>
		<display:table name="${listeFcp}" id="fcp" style="width:95%"
			requestURI="/detailCs.do">

			<display:column title="Code" sortable="true">
				<c:out value="${fcp.formatCode}" />
			</display:column>

			<display:column title="Description">
				<c:out value="${fcp.description}" />
			</display:column>


			<display:column title="Profil de contrôle">
				<c:set var="cp" value="${fcp.controlProfil}"></c:set>

				<display:table style="width: 100%" name="${cp}" id="detailCp"
					class="simple sublist" requestURI="/detailCs.do">
					<display:column title="Identification du format">
						<c:if test="${detailCp.formatIdentification == true}">
							Oui
						</c:if>
						<c:if test="${detailCp.formatIdentification == false}">
							Non
						</c:if>
					</display:column>
					<display:column title="Validation du format">
						<c:if test="${detailCp.formatValidation == true}">
							Oui
						</c:if>
						<c:if test="${detailCp.formatValidation == false}">
							Non
						</c:if>
					</display:column>
					<display:column title="Mode de validation">
						<c:out value="${detailCp.formatValidationMode}" />
					</display:column>
					<display:column title="Format du fichier">
						<c:out value="${detailCp.fileFormat}" />
					</display:column>
				</display:table>

			</display:column>
		</display:table>


	</c:if>


	<c:if test="${csForm.modification=='true'}">
		<p style="text-align: right"><input style="display: none"
			id="bouttonModifier" type="submit" onclick="submitForm('modifier')"
			value="Modifier" /></p>
	</c:if>
	<c:if test="${csForm.ajout =='true'}">
		<p style="text-align: right"><input type="submit"
			onclick="return submitForm('ajouter')" value="Lancer" /></p>
	</c:if>
</form:form>


</body>
</html>


