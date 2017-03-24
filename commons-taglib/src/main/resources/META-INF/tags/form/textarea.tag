<%@ tag language="java" pageEncoding="utf8"
	body-content="empty"
	dynamic-attributes="attributes"
	
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="name"
	required="true"
	rtexprvalue="true"
%>
<%@ attribute name="value"
	required="false"
	rtexprvalue="true"
%>


<c:set var="field_exception" value= "${name}" />
<c:set var="lg" value= "${fn:length(exception[field_exception].value)}" />
<textarea id="${name}" name="${name}"
<c:forEach var="attribute" items="${attributes}">
${attribute.key} = "${attribute.value}"
</c:forEach>
><c:choose>
<c:when test="${lg == 0}">${value}</c:when>
<c:otherwise>${exception[field_exception].value}</c:otherwise>
</c:choose></textarea>

