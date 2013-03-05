<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Visualisation des traces</title>
<link rel="stylesheet" type="text/css"
	href="js/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="js/extjs/resources/css/xtheme-blue.css" />
	
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="js/extjs/ext-all-debug.js"></script>
<script type="text/javascript" src="js/extjs/src/locale/ext-lang-fr.js"></script>
</head>
<body>
<form:form method="post" modelAttribute="formulaire">

	<form:hidden path="action" />
	<form:hidden path="url" />
	<form:hidden path="infoPopUpUrl" />
	<form:hidden path="popUpAction" />

<table width="100%">
	<tr>
		<td style="width: 75%"><p class="titre1"><c:out value="${formulaire.titre}"/></p></td>
		<td style="width: 25%" align="right"><a href="index.do">&lt;&lt;&nbsp;Retour à l'accueil </a></td>
	</tr>
</table>

<br />

<div id="parametres"></div>
<br />
<div id='lecture'><input type="button" onclick="javascript:getTrace();" value="Afficher les traces" ></div>
<br />

<div id='traceTable' style="width:99%;"></div>

<br /><br />

<script type="text/javascript"
	src="js/traces/traceVisualisation.js"></script>
	</form:form>
</body>
</html>