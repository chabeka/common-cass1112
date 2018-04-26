<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
<title>Exemple d'un accès via JNDI</title>
<c:import url="/view/header.jsp" />
</head>
<body>
<div id="table">
<table>
	<thead>
		<tr>
			<th><fmt:message key="document.id" /></th>
			<th><fmt:message key="document.titre" /></th>
			<th><fmt:message key="document.date" /></th>
		</tr>
	</thead>
	<c:forEach var="document" items="${documents}"
		varStatus="status">

		<tr>
			<td>
				<c:out value="${document.id}"/> 
			</td>

			<td>
				<c:out value="${document.titre}"/> 
			</td>

			<td>
				<fmt:formatDate value="${document.date}" pattern="dd/MM/yyyy"/>
			</td>
		</tr>
	</c:forEach>
</table>
</div>
</body>
</html>



