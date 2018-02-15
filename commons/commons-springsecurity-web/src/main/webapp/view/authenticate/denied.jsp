<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Acc&egrave;s refus&eacute;</title>
</head>
<body>

<div style="color: red">

	<c:if test="${not empty SPRING_SECURITY_403_EXCEPTION}">
		<p>
			${SPRING_SECURITY_403_EXCEPTION.message}</p>
	</c:if>

</div>

</body>

</html>