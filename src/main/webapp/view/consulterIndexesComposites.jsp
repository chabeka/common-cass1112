<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/map.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title>IHM Web Exploit - Consultation des Indeces composites</title>
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
	
	<form:form method="post" modelAttribute="indexesListeForm" action="consulterIndexesComposites.do">
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
	
	<c:if test="${not empty listeIndexes}">
	
	   <display:table
	      name="listeIndexes"
	      id="indexRow"
	      style="width: 90%"
	      defaultorder="ascending"
	      pagesize="${nbLignesTableau}"
	      requestURI="/consulterIndexesComposites.do">
	      <display:column title="Codes longs" sortable="true">
	      	<c:out value="${indexRow.label}"></c:out>
	      </display:column>
	      <display:column property="name" title="Codes courts" sortable="true" />
	      <display:column  title="Indexé" sortable="true">
	      	<c:choose>
				<c:when test="${indexRow.isComputed}">OUI</c:when>
				<c:otherwise>NON</c:otherwise>
			</c:choose>
	      </display:column>
	   </display:table>
	   
	   <div><br /><br /></div>
	   
	</c:if>

</body>
</html>
