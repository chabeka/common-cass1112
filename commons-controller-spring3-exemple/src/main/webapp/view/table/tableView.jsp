<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page import="java.util.*"%>
<%@page import="fr.urssaf.image.commons.controller.spring3.exemple.modele.*"%>

<html>
<head>
<title>Exemple d'un tableau</title>
<c:import url="/view/header.jsp" />
</head>
<body>
<form:form method="post" modelAttribute="formulaire">
<div id="table">
<table>
	<thead>
		<tr>
			<th><fmt:message key="document.titre" /></th>
			<th><fmt:message key="document.etat" /></th>
			<th><fmt:message key="document.openDate" /></th>
			<th><fmt:message key="document.closeDate" /></th>
			<th><fmt:message key="document.level" /></th>
			<th><fmt:message key="document.flag" /></th>
			<th><fmt:message key="document.etats" /></th>
			<th><fmt:message key="document.comment" /></th>
		</tr>
		<%
			List<Etat> etats = new ArrayList<Etat>();
			etats.addAll(Arrays.asList(Etat.values()));
		%>
		<c:set var="etatOptions" value= "<%= etats %>" />
	</thead>
	<c:forEach var="document" items="${formulaire.documents}"
		varStatus="status">

		<tr>
			<td>
				<form:input path='interneFormulaire[${document.id}].titre'/> 
				<h6 class="erreur"><form:errors path='interneFormulaire[${document.id}].titre' /></h6>
			</td>

			<td>
				<form:select path='interneFormulaire[${document.id}].etat'>
					<form:options items='${etatOptions}'/>
				</form:select>
				<h6 class="erreur"><form:errors path='interneFormulaire[${document.id}].etat'/></h6>
			</td>

			<td>
				<form:input path='interneFormulaire[${document.id}].openDate' />
				<h6 class="erreur"> <form:errors path='interneFormulaire[${document.id}].openDate'/></h6>
			</td>

			<td>
				<form:input path='interneFormulaire[${document.id}].closeDate' /> 
				<h6 class="erreur"><form:errors path='interneFormulaire[${document.id}].closeDate'/></h6>
			</td>

			<td>
				<c:forEach begin="1" end="3" varStatus="status">
					<form:radiobutton path='interneFormulaire[${document.id}].level' value='${status.index}'/>
				</c:forEach>
				<h6 class="erreur"><form:errors path='interneFormulaire[${document.id}].level'/></h6>
			</td>

			<td align="center">
				<form:checkbox path='interneFormulaire[${document.id}].flag' value='${flag}'/>
				<h6 class="erreur"><form:errors path='interneFormulaire[${document.id}].flag'/></h6>
			</td>
			
			<td>
				<form:select path='interneFormulaire[${document.id}].etats' multiple='true' size='4' >
					<form:options items='${etatOptions}'/>
				</form:select>
				<h6 class="erreur"><form:errors path='interneFormulaire[${document.id}].etats'/></h6>
			</td>
			
			<td>
				<form:textarea path='interneFormulaire[${document.id}].interneFormulaire.comment'/>
				<h6 class="erreur"> <form:errors path='interneFormulaire[${document.id}].interneFormulaire.comment'/></h6>
			</td>
		</tr>
	</c:forEach>
</table>
</div>
<input type="submit" />
<button type="button" onclick="javascript:location.href='table.do?action=add';"><fmt:message key="button.document.add"/></button>
</form:form>

</body>
</html>