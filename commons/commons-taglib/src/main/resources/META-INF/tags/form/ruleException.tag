<%@ tag language="java" pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="rule"
	required="true"
	rtexprvalue="true"
%>

<div id="error_${rule}">
<c:set var="rule_exception" value= "${rule}" />
<c:if test="${ruleException[rule_exception]!= null}">
<c:choose>
<c:when test="${fn:length(ruleException[rule_exception]) == 1}">
<c:set var="error" value= "${ruleException[rule_exception][0].exception}" />
<fmt:message key="${error.code}" var="message">
	<c:forEach items="${error.parameters}" var="parametre" begin="0">
	<fmt:param value="${parametre}"/>
	</c:forEach>
	</fmt:message>
	<c:out value="${message}"/>
</c:when>
<c:otherwise>
<ul style="top">
	<c:forEach items="${ruleException[rule_exception]}" var="error">
	<li type="disc" >
	<fmt:message key="${error.code}" var="message">
	<c:forEach items="${error.parameters}" var="parametre" begin="0">
	<fmt:param value="${parametre}"/>
	</c:forEach>
	</fmt:message>
	<c:out value="${message}"/>
	</li>
	</c:forEach>
</ul>
</c:otherwise>
</c:choose>

</c:if>
</div>
