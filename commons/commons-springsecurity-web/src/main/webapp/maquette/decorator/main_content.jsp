
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@page import="com.opensymphony.module.sitemesh.Page"%>
<%@page import="com.opensymphony.module.sitemesh.RequestConstants"%>
<%
   Page thepage = (Page) request.getAttribute(RequestConstants.PAGE);
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<page:applyDecorator name="main" title="<%= thepage.getTitle() %>">
	<body>
	<div id="content">
	
	
	<div id="leftbox">
	<sec:authorize access="isAuthenticated()">
	<div id="identity">
		<sec:authentication property="principal.authorities" var="authorities"/>
		<span>id:<sec:authentication property="principal.username"/></span>
		
		<c:forEach 	var="authority" items="${authorities}">
			<div><span>${authority.authority}</span></div>
		</c:forEach>
		
	</div>
	</sec:authorize>
	
	<div id="menu">

	<div class="menu"><a href="welcome.html">Accueil</a></div>
	<div class="menu"><a href="simpleForm.html">formulaire</a></div>
	
	</div>
	
	<sec:authorize access="isAuthenticated()">
		<button type="button"
				onclick="javascript: location.href = 'logout';">deconnexion</button>
	</sec:authorize>
	
	<sec:authorize access="!isAuthenticated()">
		<button type="button" onclick="javascript: location.href = 'login.do';">
			<span>connexion</span>
		</button>
	</sec:authorize>
	
	</div>

	<decorator:body />
	</div>
	</body>
</page:applyDecorator>



