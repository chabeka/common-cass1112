<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
<title>Formulaire Spring Security</title>
</head>
<body>

<p>&nbsp;formulaire&nbsp;</p>
<div id="form">
<form:form method="post" modelAttribute="formFormulaire">

	<div>
	<form:label path="title">Titre&nbsp;:</form:label>
	<form:input path="title"/>
	<span class="error"><form:errors path="title" /></span>
	</div>
	
	
	<div>
	<form:label path="text">Texte&nbsp;:</form:label>
	<form:textarea path="text" cols="50" rows="3"/>
	<span class="error" style="vertical-align:top;"><form:errors path="text" /></span>
	</div>
	
	<div>
	<label>&nbsp;</label>
	<input type="submit"/>
	
	<c:if test="${not empty message}">
	<span class="msg"><fmt:message key="${message}" /></span>
	</c:if>
	</div>

</form:form>
</div>

</body>
</html>



