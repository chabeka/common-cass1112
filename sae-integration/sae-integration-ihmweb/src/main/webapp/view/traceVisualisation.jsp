<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Visulaisation des traces</title>
<link rel="stylesheet" type="text/css"
	href="js/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="js/extjs/resources/css/xtheme-blue.css" />
	
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="js/extjs/ext-all-debug.js"></script>
</head>
<body>
<table width="100%">
	<tr>
		<td style="width: 75%">
		<p class="titre1">Visualisation des Traces</p>
		</td>
		<td style="width: 25%" align="right"><a href="index.do">&lt;&lt;&nbsp;Retour
		à l&apos;accueil </a></td>
	</tr>
</table>

<div id="parametres"></div>
<div id='lecture'> <input type="button" value="Visualiser les traces" onclick="javascript:getTrace();" ></input></div>
<div id='traceTable' style="width:99%;"></div>


<script type="text/javascript"
	src="js/traces/traceVisualisation.js"></script>
</body>
</html>