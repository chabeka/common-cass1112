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

<title>IHM Web Exploit - Consulter la pile des travaux par serveur</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
   
</script>



</head>

<body>

<div><br /></div>

<form:form method="post" modelAttribute="gererPileTravauxForm" action="consulterPileParServeur.do">

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

<c:if test="${not empty listeJobsEnCours}">

	<display:table
		name="listeJobsEnCours" id="jobRow" requestURI="/consulterPileParServeur.do"
		pagesize="${nbLignesTableau}" style="width: 90%" defaultsort="1" defaultorder="ascending">

			<display:column property="reservedBy" title="reservedBy" sortable="true"/>
			<display:column title="idJob" sortable="true">
				<c:choose>
					<c:when test="${not empty jobRow.idJob && jobRow.type == 'capture_masse'}">
						<a href="afficherDocuments.do?requeteLucene=IdTraitementMasseInterne:${jobRow.idJob}" title="Rechercher les documents">${jobRow.idJob}</a>
					</c:when>
					<c:when test="${not empty jobRow.idJob && jobRow.type != 'capture_masse'}">
						${jobRow.idJob}
					</c:when>
					<c:otherwise>
						Aucun job en cours pour ce serveur.
					</c:otherwise>
				</c:choose>
			</display:column>
			<display:column property="type" title="type" sortable="true" />
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
				<c:choose>
					<c:when  test="${jobRow.state == 'SUCCESS' && jobRow.toCheckFlag == false}">
						<display:column property="state" title="state" sortable="true" style="color:green;"/>
					</c:when>
					<c:when  test="${jobRow.state == 'SUCCESS' && jobRow.toCheckFlag == true}">
						<display:column property="state" title="state" sortable="true" style="color:red;"/>
					</c:when>
					<c:when  test="${jobRow.state == 'FAILURE'}">
						<display:column property="state" title="state" sortable="true" style="color:red;"/>
					</c:when>
					<c:when  test="${jobRow.state == 'CREATED'}">
						<!-- l'etat CREATED alors qu'il est affecte a un serveur ne devrait pas se produire  -->
						<display:column property="state" title="state" sortable="true" style="color:orange;"/>
					</c:when>
					<c:otherwise>
						<display:column property="state" title="state" sortable="true" style="color:blue;"/>
					</c:otherwise>
				</c:choose>
			<display:column property="docCount" title="docCount"
					sortable="true" format="{0,number,#,###}" style='text-align:right' />
			<display:column property="creationDate" title="creationDate"
					sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
			<display:column property="reservationDate" title="reservationDate"
					sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
			<display:column property="startingDate" title="startingDate"
					sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
			<display:column property="endingDate" title="endingDate"
					sortable="true" format="{0,date,dd/MM/yyyy HH:mm}" />
			
	</display:table>
	
</c:if>

</body>
</html>


