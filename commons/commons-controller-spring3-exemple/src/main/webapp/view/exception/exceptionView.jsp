<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Exception View</title>
<c:import url="/view/header.jsp" />
</head>
<body>
<h6 class="erreur">une erreur a eu lieu</h6>
<h6 class="erreur">${exceptionCode.message}</h6>
<p class="erreur">
<c:forEach var="trace" items="${exceptionCode.stackTrace}">
	${trace}<br>
</c:forEach>
</p>

</body>
</html>



