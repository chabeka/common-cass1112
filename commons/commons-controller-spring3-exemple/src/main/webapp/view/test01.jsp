<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Spring MVC exemple</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

<h1>Aperçu rapide d'une page web avec formulaire</h1>

<form:form method="post" modelAttribute="leFormulaire">
	<form:input path='question' />
	<form:input path='reponse' />
	<input type="submit" />
</form:form>

</body>
</html>
