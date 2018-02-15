<%@ tag language="java" pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="field"
	required="true"
	rtexprvalue="true"
%>

<div id="error_${field}">
<c:set var="field_exception" value= "${field}" />
<c:if test="${exception[field_exception]!= null}">
<c:choose>
<c:when test="${fn:length(exception[field_exception].formulaireExceptions) == 1}">
<c:set var="error" value= "${exception[field_exception].formulaireExceptions[0]}" />
<fmt:message key="${error.code}" var="message">
	<c:forEach items="${error.parameters}" var="parametre" begin="0">
	<fmt:param value="${parametre}"/>
	</c:forEach>
	</fmt:message>
	<c:out value="${message}"/>
</c:when>
<c:otherwise>
<ul style="top">
	<c:forEach items="${exception[field_exception].formulaireExceptions}" var="error">
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
