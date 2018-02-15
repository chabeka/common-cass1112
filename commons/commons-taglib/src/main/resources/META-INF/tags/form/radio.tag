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
	required="true"
	rtexprvalue="true"
%>
<%@ attribute name="checked"
	required="true"
	rtexprvalue="true"
%>


<c:set var="field_exception" value= "${name}" />
<input 	type="radio" id="${name}" name="${name}"
		value="${checked}"
		<c:if test="${value == checked}">checked</c:if>
<c:forEach var="attribute" items="${attributes}">
${attribute.key} = "${attribute.value}"
</c:forEach>
 />

