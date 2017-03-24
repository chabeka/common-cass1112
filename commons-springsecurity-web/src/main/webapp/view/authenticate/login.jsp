<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>


<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Connexion</title>
</head>
<body>
<div>

<form action="j_spring_security_check" method="post">

	<div id="form_login">

	<div>
	<label for="login">
		<fmt:message key="login" />
	</label>:
	<input type="text" name='j_username' id='login' value="${SPRING_SECURITY_LAST_USERNAME}" />
		
	</div>

	<div>
	<label for="password">
		<fmt:message key='password' />
	</label>:
	
	<input type="password" name='j_password' id='password'/>
	</div>


	<div id="submit">

	<input type='submit' id='boutton_login'
		value='<fmt:message key="connection" />' />
		
	<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
		<p class="error">
			${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
	</c:if>

	
		
	</div>
	
	</div>
	
	
</form>


<div>
Comptes utilisateurs:
<ul>
<li> admin : adminpassword</li>
<li> user  : userpassword</li>
<li> auth  : authpassword</li>
</ul>

</div>

</div>


</body>
</html>
