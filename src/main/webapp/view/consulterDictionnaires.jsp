<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib prefix="custom" uri="/WEB-INF/tld/map.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<title>IHM Web Exploit - Consultation des Dictionnaires</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />
<link href="css/calendrier.css" rel="stylesheet" media="screen" type="text/css" title="Design" />

<script type="text/javascript" src="js/calendrier.js"></script>

<script type="text/javascript">

   function activateModifiable() {
      document.getElementById('entries').disabled = false;
   }

   function checkModifiedData() {

      if (document.getElementById('identifiant').value == null) {
      }
      ;

      if (document.getElementById('valeurs').value == null) {
         document.getElementById("erreur").innerHTML = "il est necessaire de spécifier au moins une valeur";
         return false;
      }
      ;
   }

   function selectionnerParId() {
      document.getElementById("modeIdentifiant").checked = true;
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

<form:form method="post" modelAttribute="dictForm" action="lancerConsultDico.do">

   <h6 class="msg_erreur" id="erreur"><form:errors path='*' /></h6>
	<c:if test="${stacktrace !=null}">
		<p><textarea class="stacktrace" rows="15" cols="180"><c:out value="${stacktrace}"/></textarea></p>
	</c:if>
	
   <div class="cadreForm">

      <div>Sélectionner le mode de recherche :</div>
      
      <table class="tableForm">
         <tr class="tableForm">
            <td colspan="2" class="tableFormGauche">
               <form:radiobutton id="modeTous" path="modeRecherche" value="modeTous" />
               <label for="modeTous">Afficher tous les dictionnaires : </label>
            </td>
         </tr>
         <tr class="tableForm">
            <td class="tableFormGauche">
               <form:radiobutton id="modeIdentifiant" path="modeRecherche" value="modeIdentifiant"/>
               <label for="modeIdentifiant">Par identifiant du dictionnaire : </label>
            </td>
            <td class="tableFormGauche">
               <form:input id="identifiant" cssClass="inputText" path="identifiant" onclick="selectionnerParId()"/>
            </td>
         </tr>
      </table>
   
      <div style="padding-top: 5px">
         Nombre de lignes dans le tableau résultat :
         <form:input cssClass="inputNum" size="3" id="nbLignesTableau" path="nbLignesTableau" />
      </div>
   
      <div><input type="submit" value="Consulter" /></div>
   
   </div>

</form:form>

<div><br /></div>

<c:if test="${not empty listeDict}">

   <display:table
      name="listeDict"
      id="dictRow"
      style="width: 90%"
      defaultorder="ascending"
      pagesize="${nbLignesTableau}"
      requestURI="/lancerConsultDict.do">
      
      <display:column property="id" title="Nom du dictionnaire" sortable="true"></display:column>
      <display:column property="entries" title="Liste des valeurs autorisées" />
      <display:column title="Détails">
         <a href="detailDico.do?identifiant=${dictRow.id}" title="Voir le détail">Détail</a>
      </display:column>
      
   </display:table>
   
</c:if>

</body>
</html>
