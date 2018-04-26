<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/tld/sae.tld" prefix="sae"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>SAE - Pile des travaux - Historique</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>


<body>

<h1>Pile des travaux - Historique</h1>

<form:form method="post" modelAttribute="formulaire">

	
   <!-- Identifiant du job + Bouton rafraichir -->
   
   <table border=0>
      <tr>
         <td>Id du job : </td>
         <td><form:input path="idJob" cssStyle="width:300px;"/></td>
         <td><input type="submit" value="Rafraichir" /></td>
      </tr>
   </table>
   <br /><br />
   
   
   
   <!-- Liste des traces -->
   
   <table class="tabPile">

		<tr style="font-weight: bold;">
			<td style="width:250px;">Date</td>
			<td style="width:250px;">Trace</td>
		</tr>


		<c:forEach items="${requestScope.history}" var="objTrace"
			varStatus="loopStatus">

			<tr class="ligne${loopStatus.count % 2}">
				<td>${sae:formateDateTime(objTrace.date)}</td>
            <td><c:out value="${objTrace.trace}" /></td>
			</tr>

		</c:forEach>

	</table>
   
   
   
   <!-- Configuration Cassandra/Zookeeper -->
   <br /><br /><br />
   <sae:configPile
      pathFormulaire="connexionConfig"
      objetFormulaire="${formulaire.connexionConfig}"/>
   

</form:form>


</body>
</html>