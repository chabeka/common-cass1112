<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.opensymphony.module.sitemesh.Page"%>
<%@page import="com.opensymphony.module.sitemesh.RequestConstants"%>
<%
	Page thePage = (Page) request.getAttribute(RequestConstants.PAGE);
	HttpServletRequest decoratedRequest = request;
	boolean isAjax = "XMLHttpRequest".equals(request
			.getHeader("X-Requested-With"));
%>
<html>
<%
	if (!isAjax) {
%>

<head>
<title><decorator:title default="TITRE" /></title>
<decorator:head />
</head>
<body>
<table id="conteneur">
	<tr>
		<td colspan="2" id="header"><%@ include
			file='/maquette/include/header.jsp'%></td>
	</tr>


	<tr>
		<td id="menu_left">
		<div id="navigation"></div>
		</td>
		<td id="contain"><decorator:body /></td>
	</tr>
	<tr>
		<td colspan="2" id="footer"><%@ include
			file='/maquette/include/footer.jsp'%></td>
	</tr>

</table>
</body>

<%
	} else {
%>

<decorator:body />

<%
	}
%>
</html>