<%@ tag language="java" pageEncoding="utf8"
	body-content="empty"
	dynamic-attributes="attributes"
	
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="name"
	required="true"
	rtexprvalue="true"
%>
<%@ attribute name="value"
	required="false"
	rtexprvalue="true"
%>
<%@ attribute name="checked"
	required="true"
	rtexprvalue="true"
%>
<%@ attribute name="nochecked"
	required="true"
	rtexprvalue="true"
%>

<div id="div_script_${name}">

</div>

<script>
	
	var input_checkbox = "<input type='checkbox' id='_${name}' name='_${name}'/>";
	var input_hidden = "<input type='hidden' id='${name}' name='${name}' value='${value}'/>"
		
    document.getElementById('div_script_${name}').innerHTML=input_checkbox+input_hidden;
	
	var input_onclick = "checkUnique(this,document.getElementById('${name}'),'${checked}','${nochecked}');"
	
	document.getElementById('_${name}').setAttribute('onclick',input_onclick);
	if('${value}' == '${checked}'){
		document.getElementById('_${name}').setAttribute('checked','true');
	}
	
	
	function checkUnique(checkBox, hidden, checked, nochecked) {
		if (checkBox.checked) {
			hidden.value = checked;
		} else {
			hidden.value = nochecked;
		}

	}

</script>

<c:forEach var='attribute' items='${attributes}'>

	<script>
	document.getElementById('_${name}').setAttribute("${attribute.key}","${attribute.value}");
	</script>


</c:forEach>

<noscript>
<input 	type="checkbox" id="${name}" name="${name}" value="${checked}"
		<c:if test="${value == checked}">checked</c:if>
<c:forEach var="attribute" items="${attributes}">
${attribute.key} = "${attribute.value}"
</c:forEach>
/>
<input type="hidden" id="reset" name="reset" value="${name}"/>
</noscript>
