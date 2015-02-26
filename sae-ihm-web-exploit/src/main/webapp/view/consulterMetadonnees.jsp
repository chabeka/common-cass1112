<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/map.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title>IHM Web Exploit - Consultation des Métadonnées</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
	<link href="css/formulaire.css" rel="stylesheet" type="text/css" />
</head>

<body>


<table class="ds_box" cellpadding="0" cellspacing="0" id="ds_conclass" style="display: none;">
   <tr>
      <td id="ds_calclass"></td>
   </tr>
</table>

<form:form method="post" modelAttribute="metaListeForm" action="consulterMetadonnees.do">

   <h6 class="msg_erreur" id="erreur"><form:errors path='*' /></h6>
	<c:if test="${stacktrace !=null}">
		<p><textarea class="stacktrace" rows="15" cols="180"><c:out value="${stacktrace}"/></textarea></p>
	</c:if>
   <div class="cadreForm">

      <div style="padding-top: 5px">
         Nombre de lignes dans le tableau résultat :
         <form:input cssClass="inputNum" size="3" id="nbLignesTableau" path="nbLignesTableau" />
         <br /><br />
      </div>

      <div><input type="submit" value="Rafraîchir" /></div>
   
   </div>
   
</form:form>

<div><br /></div>

<c:if test="${not empty listeMeta}">

   
   
   <display:table
      name="listeMeta"
      id="metaRow"
      style="width: 90%"
      defaultorder="ascending"
      pagesize="${nbLignesTableau}"
      requestURI="/consulterMetadonnees.do">
      
      <display:column property="longCode" title="Code long" sortable="true" />
      <display:column property="shortCode" title="Code court" sortable="true" />
      <display:column property="label" title="Libellé" sortable="true" />
      <display:column property="description" title="Description" />
      <display:column property="type" title="Type"></display:column>
      <display:column title="Détails">
         <a href="detailMeta.do?codeLong=${metaRow.longCode}" title="Voir le détail">Détail</a>
      </display:column>
      
   </display:table>
   
   <div><br /><br /></div>
   
</c:if>


</body>
</html>
