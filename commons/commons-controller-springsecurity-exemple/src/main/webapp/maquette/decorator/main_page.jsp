
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
	
	<sec:authorize access="isAuthenticated()">
		<div class="menu"><a href="page1.do">page 1</a></div>
	</sec:authorize>
		
	<sec:authorize url="/page2.do">
		<div class="menu"><a href="page2.do">page 2</a></div>
	</sec:authorize>
		
	<sec:authorize url="/page3.do">
		<div class="menu"><a href="page3.do">page 3</a></div>
	</sec:authorize>
		
	<sec:authorize url="/pageForm.do">
		<div class="menu"><a href="pageForm.do">formulaire</a></div>
	</sec:authorize>
	
	</div>
	
	<sec:authorize access="isAuthenticated()">
		<button type="button"
				onclick="javascript: location.href = 'logout';">deconnexion</button>
	</sec:authorize>
	
	<sec:authorize access="!isAuthenticated()">
		<button type="button" onclick="javascript: location.href = 'login_basic.do';">
			<span>connexion</span>
		</button>
	</sec:authorize>

	

	</div>

	<decorator:body />
	</div>
	</body>
</page:applyDecorator>



