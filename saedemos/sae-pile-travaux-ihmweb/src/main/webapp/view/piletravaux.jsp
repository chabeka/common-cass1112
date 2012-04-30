<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/tld/sae.tld" prefix="sae"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>SAE - Pile des travaux</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>


<body>

<h1>Pile des travaux</h1>

<form:form method="post" modelAttribute="formulaire">

	<!-- Configuration Cassandra/Zookeeper -->
   <sae:configPile
      pathFormulaire="connexionConfig"
      objetFormulaire="${formulaire.connexionConfig}"/>
	<br />
	<input type="submit" value="Appliquer la configuration" />
   <br />
	<br />

   
   <!-- Contenu de la pile des travaux -->
	<table class="tabPile">

		<tr style="font-weight: bold;">
			<td>idJob</td>
			<td>type</td>
			<td>parameters</td>
			<td>state</td>
			<td>creationDate</td>
			<td>saeHost</td>
         <td>clientHost</td>
         <td>docCount</td>
         <td>reservationDate</td>
         <td>reservedBy</td>
         <td>startingDate</td>
         <td>pid</td>
			<td>endingDate</td>
			<td>message</td>
         <td>toCheckFlag</td>
         <td>toCheckFlagRaison</td>
		</tr>


		<c:forEach items="${requestScope.jobs}" var="travail"
			varStatus="loopStatus">

			<tr class="ligne${loopStatus.count % 2}">
				<td style="font-size:8pt;">
               <a href="history.do?idJobReq=${travail.idJob}" target="_blank">
               <c:out value="${travail.idJob}" />
               </a>
            </td>
				<td><c:out value="${travail.type}" /></td>
				<td style="font-size:8pt;"><c:out value="${travail.parameters}" /></td>
				<td><c:out value="${travail.state}" /></td>
				<td>${sae:formateDateTime(travail.creationDate)}</td>
				<td><c:out value="${travail.saeHost}" /></td>
            <td><c:out value="${travail.clientHost}" /></td>
            <td><c:out value="${travail.docCount}" /></td>
				<td>${sae:formateDateTime(travail.reservationDate)}</td>
				<td>${sae:ajouteProprietaireDuPC(travail.reservedBy)}</td>
				<td>${sae:formateDateTime(travail.startingDate)}</td>
				<td><c:out value="${travail.pid}" /></td>
				<td>${sae:formateDateTime(travail.endingDate)}</td>
				<td>${sae:nl2br(travail.message)}</td>
            <td><c:out value="${travail.toCheckFlag}" /></td>
            <td style="font-size:8pt;"><c:out value="${travail.toCheckFlagRaison}" /></td>
			</tr>

		</c:forEach>


	</table>

</form:form>


</body>
</html>