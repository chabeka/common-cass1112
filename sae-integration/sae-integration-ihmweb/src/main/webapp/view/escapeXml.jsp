<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Echape de chaîne XML</title>
<script type="text/javascript">
function selectionner(id){
   var element = document.getElementById(id);
   element.focus();
   element.select();
}
</script>
</head>
<body>

<table width="100%">
	<tr>
		<td style="width: 75%">
		<p class="titre1">Convertion du XML en chaînes de caractères </p>
		</td>
		<td style="width: 25%" align="right"><a href="index.do">&lt;&lt;&nbsp;Retour
		à l&apos;accueil </a></td>
	</tr>
</table>
<form:form method="post" modelAttribute="escapeFormulaire">
<div>
	<form:textarea cols="100" rows="10" path="xmlString"></form:textarea>

<div>
	<input type="submit" value="Convertir"></input>
</div>
<div>
	<form:textarea cols="100" rows="10" path="escapedString" id="escapedString"></form:textarea>
</div>
<div>
	<input type="button" onclick="javascript:selectionner('escapedString');" value="Sélectionner"></input>
</div>
</form:form>
</body>
</html>