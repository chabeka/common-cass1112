<%@ tag language="java" pageEncoding="utf8"
	body-content="empty"
	dynamic-attributes="attributes"
	
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ attribute name="title"
	required="true"
	rtexprvalue="true"
%>
<%@ attribute name="action"
	required="true"
	rtexprvalue="true"
%>

<button type="submit" value="${action}" name="action:${action}"

<c:forEach var="attribute" items="${attributes}">
${attribute.key} = "${attribute.value}"
</c:forEach>

>
<fmt:message key="${title}" var="title"/>
<c:out value="${title}" />


</button>
