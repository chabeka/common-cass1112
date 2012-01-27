<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Calculatrice de date</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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

<table width="100%">
	<tr>
		<td style="width: 75%">
		<p class="titre1">Calculatrice temporelle</p>
		</td>
		<td style="width: 25%" align="right"><a href="index.do">&lt;&lt;&nbsp;Retour
		à l&apos;accueil </a></td>
	</tr>
</table>

<div id='calcDiv'></div>

<script type="text/javascript" src="js/calcTemp/calculate.js"></script>
</body>
</html>