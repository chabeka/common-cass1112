<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/map.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="ihmexploit" uri="/WEB-INF/tld/ihm-exploit-function.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<title>IHM Web Exploit - Etat des jobs DFCe</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
   
</script>



</head>

<body>

<div><br /></div>

<c:if test="${not empty etatJobsSae}">

   <display:table
      name="etatJobsSae"
      id="jobRow"
      style="width: 90%"
      defaultorder="ascending"
      pagesize="10"
      requestURI="#">
      
      <display:column title="Nom du job" sortable="false">
      	 <c:choose>
      	 	<c:when test="${jobRow.codeJob == 'PURGE_EVT'}">
      	 		Purge du journal des événéments du SAE
      	 	</c:when>
      	 	<c:when test="${jobRow.codeJob == 'PURGE_TECH'}">
      	 		Purge du registre de surveillance technique
      	 	</c:when>
      	 	<c:when test="${jobRow.codeJob == 'PURGE_SECU'}">
      	 		Purge du registre de sécurité
      	 	</c:when>
      	 	<c:when test="${jobRow.codeJob == 'PURGE_EXPLOIT'}">
      	 		Purge du registre d'exploitation
      	 	</c:when>
      	 	<c:when test="${jobRow.codeJob == 'JOURNALISATION_EVT'}">
      	 		Journalisation des événements du SAE
      	 	</c:when>
      	 	<c:when test="${jobRow.codeJob == 'PURGE_PILE_TRAVAUX'}">
      	 		Purge de la pile des travaux
      	 	</c:when>
      	 	<c:when test="${jobRow.codeJob == 'PURGE_CORBEILLE'}">
      	 		Purge des documents dans la corbeille DFCE
      	 	</c:when>
      	 	<c:otherwise>
      	 		<c:out value="${jobRow.codeJob}" />
      	 	</c:otherwise>
      	 </c:choose>
      </display:column>
      
      <fmt:formatDate value="${jobRow.date}" pattern="dd/MM/yyyy HH:mm" var="date" />
      <display:column property="date" title="Date"
				sortable="false" format="{0,date,dd/MM/yyyy HH:mm}" />
				
      <display:column title="En cours">
			<c:out value="${ihmexploit:booleanToOuiNon(jobRow.running)}" />
      </display:column>
      
      <c:choose>
			<c:when  test="${jobRow.etat == 'SUCCESS'}">
				<display:column property="etat" title="Statut du job" sortable="false" style="color:green;"/>
			</c:when>
			<c:when  test="${jobRow.etat == 'FAILURE'}">
				<display:column property="etat" title="Statut du job" sortable="false" style="color:red;"/>
			</c:when>
			<c:when  test="${jobRow.etat == 'WARNING'}">
				<display:column property="etat" title="Statut du job" sortable="false" style="color:orange;"/>
			</c:when>
	  </c:choose>

   </display:table>
</c:if>

</body>
</html>


