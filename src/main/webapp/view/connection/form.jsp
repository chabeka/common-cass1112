<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="center"><form:form method="post"
	modelAttribute="connectionForm" id="form_cirti" name='form_cirti'>

	<div><form:label path="userLogin" cssClass="field">
		<fmt:message key="user.login" />
	</form:label>:<form:input path='userLogin' id='userLogin' cssClass="value" /><form:errors
		path='userLogin' cssClass="error" /></div>

	<div><form:label path="userPassword" cssClass="field">
		<fmt:message key='user.password' />
	</form:label>:<form:password path='userPassword' id='userPassword' cssClass="value" /><form:errors
		path='userPassword' cssClass="error" /></div>


	<div style="padding-top: 1em;" >
	
		<label class="field">&nbsp;</label>
		<input type='submit' id='boutton_login' class="button"
		value='<fmt:message key="button.connection" />' />
	
	</div>


</form:form> <p class="error"><c:out value="${failure}" /></p></div>

<script type="text/javascript">

	var form_cirti = document.getElementById('form_cirti');
	var login = form_cirti.userLogin;
	var password = form_cirti.userPassword;

	if(login.value == ''){
	   login.focus();
	}

	else if(password.value == ''){
	  password.focus();
	}

</script>

