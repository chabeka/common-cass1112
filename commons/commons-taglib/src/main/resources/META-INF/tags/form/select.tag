<%@ tag language="java" pageEncoding="utf8"
	body-content="empty"
	dynamic-attributes="attributes"
	
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://fr.urssaf.image.commons/taglib/ctl" prefix="ctl"%>

<%@ attribute name="name"
	required="true"
	rtexprvalue="true"
%>
<%@ attribute name="options"
	required="true"
	rtexprvalue="true"
	type="java.util.Collection"
%>

<%@ attribute name="value"
	required="true"
	rtexprvalue="true"
	type="java.lang.Object"
%>


<c:if test="${empty size}">
<c:set var="size" value= "1" />
</c:if>

<select id="${name}" name="${name}"
	     
	     <c:forEach var="attribute" items="${attributes}">
		${attribute.key} = "${attribute.value}"
		</c:forEach>
	     
/>

<c:forEach var="option" items="${options}">

	<c:choose>
		<c:when test='${ctl:contains(option,value)}'><option selected>${option}</option></c:when>
		<c:otherwise><option value="${option}">${option}</option></c:otherwise>
	</c:choose>
	
</c:forEach>

</select>

