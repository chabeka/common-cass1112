<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%

Context initialContext = new InitialContext();
String title = (String) initialContext.lookup("java:comp/env/title/default");


%>
<h1 class="title">
	<c:out value="<%= title  %>"/> Locale = ${pageContext.response.locale}
</h1>
