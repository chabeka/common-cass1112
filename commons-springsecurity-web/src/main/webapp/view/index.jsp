<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"  %>

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>Bienvenue</title>
</head>
<body>

<p>Bienvenue dans l'application de d&eacute;monstration de spring security</p>
<div style="margin-left: 45%;margin-right: auto;">
	<span>hi&eacute;rarchie des r&ocirc;les&nbsp;:</span>
	<br/>
	<sec:authorize access="hasAuthority('ROLE_ADMIN')">
		<div style="margin-left: 40px"><span>admin</span></div>
	</sec:authorize>

	<sec:authorize access="hasAuthority('ROLE_USER')">
		<div style="margin-left: 40px"><span>user</span></div>
	</sec:authorize>

	<sec:authorize access="hasAuthority('ROLE_AUTH')">
		<div style="margin-left: 40px"><span>auth</span></div>
	</sec:authorize>
</div>
</body>

</html>