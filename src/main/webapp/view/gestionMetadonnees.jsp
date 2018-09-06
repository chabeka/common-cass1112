<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/map.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<title>IHM Web Exploit - Ajout/Modification d'une métadonnée</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
   function intiForm() {
      var ajout = document.getElementById("ajout").value;
      if (ajout == 'true') {
         document.getElementById('internal').checked = false;
         document.getElementById("retour").style.display='none';
         var codeCourt = document.getElementById('codeCourt');
         codeCourt.removeAttribute("readonly");
         var codeLong = document.getElementById('codeLong');
         codeLong.removeAttribute("readonly");
         var type = document.getElementById('type');
     	 type.removeAttribute("disabled");
     	 
         activateModifiableMeta();
      	
      } else {
         document.getElementById("retour").style.display='block';
      }
   }

   function activateModifiableMeta() {

      var intern = document.getElementById('internal');
      
      var descr = document.getElementById('description');
      descr.removeAttribute("readonly");

      var label = document.getElementById('label');
      label.removeAttribute("readonly");

      var reqArch = document.getElementById('requiredForArchival');
      reqArch.removeAttribute("disabled");

      if (intern.checked == false) { 
      	var reqStor = document.getElementById('requiredForStorage');
      	reqStor.removeAttribute("disabled");
      }

      var diffClient = document.getElementById('diffClient');
      diffClient.removeAttribute("disabled");
      
      var leftTrim = document.getElementById('leftTrim');
      leftTrim.removeAttribute("disabled");

      var rightTrim = document.getElementById('rightTrim');
      rightTrim.removeAttribute("disabled");    
      
      var length = document.getElementById('length');
      length.removeAttribute("readonly");

      var cons = document.getElementById('consultable');
      cons.removeAttribute("disabled");

      var defCons = document.getElementById('defaultConsultable');
      defCons.removeAttribute("disabled");

      if (intern.checked == false) { 
      	var index = document.getElementById('isIndexed');
      	index.removeAttribute("disabled");
      }

      var dict = document.getElementById('hasDictionary');
      dict.removeAttribute("disabled");

      var dictName = document.getElementById('dictionaryName');
      dictName.removeAttribute("readonly");

      var search = document.getElementById('searchable');
      search.removeAttribute("disabled");

      var mod = document.getElementById('modifiable');
      mod.removeAttribute("disabled");

      var arch = document.getElementById('archivable');
      arch.removeAttribute("disabled");

      var transf = document.getElementById('transferable');
      transf.removeAttribute("disabled");

      if (document.getElementById('bouttonModifier') != null) {
      	document.getElementById('bouttonModifier').style.display = "block";
      }
   }

   function checkModifiedData() {

      if (document.getElementById('codeLong').value == null
            || document.getElementById('codeLong').value == '') {
         document.getElementById("erreur").innerHTML = "Le code long est une valeur obligatoire";
         return false;
      }
      ;
      if (document.getElementById('codeCourt').value == null
            || document.getElementById('codeCourt').value == '') {
         document.getElementById("erreur").innerHTML = "Le code court est une valeur obligatoire";
         return false;
      }
      ;

      if (document.getElementById('label').value == null
            || document.getElementById('label').value == '') {
         document.getElementById("erreur").innerHTML = "Le libellé est une valeur obligatoire";
         return false;
      }
      ;
      if (document.getElementById('description').value == null
            || document.getElementById('description').value == '') {
         document.getElementById("erreur").innerHTML = "La description est une valeur obligatoire";
         return false;
      }
      ;

      if (document.getElementById('type').value == null
            || document.getElementById('type').value == '') {
         document.getElementById("erreur").innerHTML = "Le type de la métadonnées est une valeur obligatoire";
         return false;
      }
      ;
      if (document.getElementById('hasDictionary').value == true
            && (document.getElementById('dictionaryName').value == null || document
                  .getElementById('dictionaryName').value == '')) {
         document.getElementById("erreur").innerHTML = "Si la métadonnée a un dictionnaire le nom de ce dernier est obligatoire";
         return false;
      }
      ;

      document.getElementById('type').removeAttribute("disabled");
      document.getElementById('requiredForStorage').removeAttribute("disabled");
      document.getElementById('isIndexed').removeAttribute("disabled");
      document.getElementById('internal').removeAttribute("disabled");
     
      return true;
   }

   function submitForm(action) {

      if (action == 'modifier') {
         document.forms[0].action = 'gestionMetadonnee.do?action=modifier';
         if (checkModifiedData()) {
            document.forms[0].submit();
         } else {
            return false;
         }
      } else if (action == 'ajouter') {
         document.forms[0].action = 'gestionMetadonnee.do?action=ajouter';
         if (checkModifiedData()) {
            document.forms[0].submit();
         } else {
            return false;
         }
      }

   }
</script>



</head>

<body onload="intiForm()">


<form:form method="post" modelAttribute="metaAjoutModifForm">

   <form:hidden id="ajout" path="ajout" />
   <form:hidden id="modification" path="modification" />

   <h6 class="msg_erreur" id="erreur"><form:errors path='*' /></h6>
   	<c:if test="${stacktrace !=null}">
		<p><textarea class="stacktrace" rows="15" cols="180"><c:out value="${stacktrace}"/></textarea></p>
	</c:if>
   <h5><form:label id="message" path="message">
      <c:out value="${metaAjoutModifForm.message}"></c:out>
   </form:label></h5>
   <c:set var="metaReference" value="${metaAjoutModifForm.detailMeta}" />

   <div id="retour"><p><a href="javascript:window.history.go(-1)">[ Retour ]</a></p></div>
  
   <table>
   
      <tr>
         <td>Code long</td>
         <td><form:input id="codeLong" path="detailMeta.longCode" size="40" readonly="true" /></td>
      </tr>
      
      <tr>
         <td>Code court</td>
         <td><form:input id="codeCourt" path="detailMeta.shortCode" size="40" readonly="true" /></td>
      </tr>
      
      <tr>
         <td>Libellé</td>
         <td><form:input id="label" path="detailMeta.label" size="100" readonly="true" /></td>
      </tr>
      
      <tr>
         <td>Description</td>
         <td><form:input id="description" path="detailMeta.description" size="100" readonly="true" /></td>
      </tr>
      
      <tr>
         <td>Archivable</td>
         <td>
         	<form:checkbox id="archivable" path="detailMeta.archivable" disabled="true" />
         </td>
      </tr>
      
      <tr>
         <td>Requis à l'archivage</td>
         <td><form:checkbox id="requiredForArchival" path="detailMeta.requiredForArchival" disabled="true" /></td>
      </tr>
      
      <tr>
         <td>Requis au stockage</td>
         <td><form:checkbox id="requiredForStorage" path="detailMeta.requiredForStorage" disabled="true" /></td>
      </tr>
      <tr>
         <td>Recherchable</td>
         <td><form:checkbox id="searchable" path="detailMeta.searchable" disabled="true" /></td>
      </tr>
      
      <tr>
         <td>Consultable</td>
         <td><form:checkbox id="consultable" path="detailMeta.consultable" disabled="true" /></td>
      </tr>
      
      <tr>
         <td>Consultable par défaut</td>
         <td><form:checkbox id="defaultConsultable" path="detailMeta.defaultConsultable" disabled="true" /></td>
      </tr>
      
      <tr>
         <td>Modifiable (GNT)</td>
         <td><form:checkbox id="modifiable" path="detailMeta.modifiable" disabled="true" /></td>
      </tr>
      
      <tr>
         <td>Transférable</td>
         <td><form:checkbox id="transferable" path="detailMeta.transferable" disabled="true" /></td>
      </tr>

      <tr>
         <td>Indexée</td>
         <td><form:checkbox id="isIndexed" path="detailMeta.isIndexed" disabled="true" /></td>
      </tr>
      
      <tr>
         <td>Possède un dictionnaire</td>
         <td><form:checkbox id="hasDictionary" path="detailMeta.hasDictionary" disabled="true" /></td>
      </tr>
      
      <tr>
         <td>Nom du dictionnaire</td>
         <td><form:input id="dictionaryName" path="detailMeta.dictionaryName" readonly="true" /></td>
      </tr>
      
      <tr>
         <td>Trim à gauche avant archivage</td>
         <td><form:checkbox id="leftTrim" path="detailMeta.leftTrimable" disabled="true" /></td>
      </tr>
      
      <tr>
         <td>Trim à droite avant archivage</td>
         <td><form:checkbox id="rightTrim" path="detailMeta.rightTrimable" disabled="true" /></td>
      </tr>
      
      <tr>
         <td>Type DFCE</td>
         <td><form:select id="type" path="detailMeta.type" disabled="true">
            <form:option value="String" />
            <form:option value="Boolean" />
            <form:option value="Date" />
            <form:option value="Datetime" />
            <form:option value="Integer" />
            <form:option value="Long" />
            <form:option value="Float" />
            <form:option value="Double" />
            <form:option value="UUID" />
         </form:select></td>
      </tr>

      <tr>
         <td>Taille max</td>
         <td><form:input id="length" path="detailMeta.length" readonly="true" /></td>
      </tr>

      <tr>
         <td>Interne DFCE</td>
         <td>
         	<form:checkbox id="internal" path="detailMeta.internal" disabled="true" />
         </td>
       </tr>

     	<tr>
         	<td>Diffusable client</td>
         	<td>
	         	<form:checkbox id="diffClient" path="detailMeta.clientAvailable" disabled="true" />
         	</td>
       	</tr>


      <tr>
         <c:if test="${metaAjoutModifForm.ajout=='false'}">
            <td><a href="#" title="Modifier la métadonnée" onclick="activateModifiableMeta()">Modifier la métadonnées</a></td>
         </c:if>
      </tr>
      
   </table>
   
   <c:if test="${metaAjoutModifForm.modification=='true'}">
      <p style="text-align: right">
         <input style="display: none" id="bouttonModifier" type="submit" onclick="submitForm('modifier')" value="Enregistrer" />
       </p>
   </c:if>
   
   <c:if test="${metaAjoutModifForm.ajout=='true'}">
      <p>
         <input type="submit" onclick="return submitForm('ajouter');" value="Ajouter" />
      </p>
   </c:if>

</form:form>

</body>
</html>

