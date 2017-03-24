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
<%@ attribute name="readonly"
	required="false"
	rtexprvalue="true"
	type="java.lang.Boolean"
%>

<c:set var="field_exception" value= "${name}" />
<input type="text" id="${name}" name="${name}" class="form" autocomplete="off"
<c:choose>
<c:when test="${empty exception[field_exception].value}">
	value="${value}"
</c:when>
<c:otherwise>
	value="${exception[field_exception].value}"
</c:otherwise>
</c:choose>
<c:forEach var="attribute" items="${attributes}">
${attribute.key} = "${attribute.value}"
</c:forEach>
<c:if test="${readonly}">readonly="readonly" disabled="true"</c:if>
 />

