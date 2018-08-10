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

<title>IHM Web Exploit - Ajout/Modification d'un format</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
   function initForm() {
      var ajout = document.getElementById("ajout").value;
      if (ajout == 'true') {
         document.getElementById("retour").style.display='none';
         var identifiant = document.getElementById('identifiant');
         identifiant.removeAttribute("readonly");
      } else {
         document.getElementById("retour").style.display='block';
      }
   }

 
   function checkModifiedData() {

      if (document.getElementById('identifiant').value == null
            || document.getElementById('identifiant').value == '') {
         document.getElementById("erreur").innerHTML = "L'identifiant est une valeur obligatoire";
         return false;
      }
      ;
      if (document.getElementById('typeMime').value == null
            || document.getElementById('typeMime').value == '') {
         document.getElementById("erreur").innerHTML = "Le type mime est une valeur obligatoire";
         return false;
      }
      ;
      
      if (document.getElementById('extension').value == null
            || document.getElementById('extension').value == '') {
         document.getElementById("erreur").innerHTML = "L'extension est une valeur obligatoire";
         return false;
      }
      ;
      if (document.getElementById('description').value == null
            || document.getElementById('description').value == '') {
         document.getElementById("erreur").innerHTML = "La description est une valeur obligatoire";
         return false;
      }
      ;

      if (document.getElementById('visualisable').value == null
            || document.getElementById('visualisable').value == '') {
         document.getElementById("erreur").innerHTML = "La propriété Visualisable est obligatoire";
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
         document.forms[0].action = 'gestionFormats.do?action=modifier';
         if (checkModifiedData()) {
            document.forms[0].submit();
         } else {
            return false;
         }
      } else if (action == 'ajouter') {
         document.forms[0].action = 'gestionFormats.do?action=ajouter';
         if (checkModifiedData()) {
            document.forms[0].submit();
         } else {
            return false;
         }
      }

   }
</script>



</head>

<body onload="initForm()">


<form:form method="post" modelAttribute="formatAjoutModifForm">

   <form:hidden id="ajout" path="ajout" />
   <form:hidden id="modification" path="modification" />

   <h6 class="msg_erreur" id="erreur"><form:errors path='*' /></h6>
   	<c:if test="${stacktrace !=null}">
		<p><textarea class="stacktrace" rows="15" cols="180"><c:out value="${stacktrace}"/></textarea></p>
	</c:if>
   <h5><form:label id="message" path="message">
      <c:out value="${formatAjoutModifForm.message}"></c:out>
   </form:label></h5>
  
   <div id="retour"><p><a href="javascript:window.history.go(-1)">[ Retour ]</a></p></div>
  
   <table>
   
      <tr>
         <td>Identifiant</td>
         <td><form:input id="identifiant" path="format.idFormat" size="40" readonly="true" /></td>
      </tr>
      <tr>
         <td>Type Mime</td>
         <td><form:input id="typeMime" path="format.typeMime" size="40"/></td>
      </tr>
      <tr>
         <td>Extension</td>
         <td><form:input id="extension" path="format.extension" size="40"/></td>
      </tr>
      <tr>
         <td>Description</td>
         <td><form:input id="description" path="format.description" size="100"/></td>
      </tr>
      <tr>
         <td>Visualisable</td>
         <td>
         	<form:checkbox id="visualisable" path="format.visualisable"/>
         </td>
      </tr>
      <tr>
         <td>Autorisé en GED</td>
         <td>
         	<form:checkbox id="autoriseGED" path="format.autoriseGED"/>
         </td>
      </tr>
       <tr>
         <td>Validateur</td>
         <td><form:input id="validator" path="format.validator" size="40"/></td>
      </tr>     
       <tr>
         <td>Identificateur</td>
         <td><form:input id="identificateur" path="format.identificateur" size="40"/></td>
      </tr>     
       <tr>
         <td>Convertisseur</td>
         <td><form:input id="convertisseur" path="format.convertisseur" size="40"/></td>
      </tr> 

      <tr>
         <c:if test="${metaAjoutModifForm.ajout=='false'}">
            <td><a href="#" title="Modifier la métadonnées" onclick="activateModifiableMeta()">Modifier la métadonnées</a></td>
         </c:if>
      </tr>
      
   </table>
   
   <c:if test="${formatAjoutModifForm.modification=='true'}">
      <p>
         <input id="bouttonModifier" type="submit" onclick="submitForm('modifier')" value="Modifier" />
       </p>
   </c:if>
   
   <c:if test="${formatAjoutModifForm.ajout=='true'}">
      <p>
         <input type="submit" onclick="return submitForm('ajouter');" value="Ajouter" />
      </p>
   </c:if>

</form:form>

</body>
</html>


