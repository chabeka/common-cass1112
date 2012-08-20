<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Calculatrice de date</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="js/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="js/extjs/resources/css/xtheme-blue.css" />

<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="js/extjs/ext-all-debug.js"></script>
<script type="text/javascript" src="js/extjs/src/locale/ext-lang-fr.js"></script>
</head>

<body>

<table width="100%">
	<tr>
		<td style="width: 75%">
		<p class="titre1">Outils de calcul sur les dates/heures</p>
		</td>
		<td style="width: 25%" align="right"><a href="index.do">&lt;&lt;&nbsp;Retour à l&apos;accueil </a></td>
	</tr>
</table>

<p><br /><br /></p>
<p class="titre2">Calcul d'une date de fin à partir d'une date de début et d'un nombre de jours à ajouter</p>
<div id='calcDiv'></div>

<p><br /><br /></p>
<p class="titre2">Conversion d'un timestamp ms en date/heure intelligible</p>
<div id='calcDiv2'></div>

<p><br /><br /></p>
<p class="titre2">Conversion d'une date/heure en timestamp ms</p>
<div id='calcDiv3'></div>

<script type="text/javascript" src="js/calcTemp/calculate.js"></script>
<script type="text/javascript" src="js/calcTemp/conversionTimestampToDate.js"></script>
<script type="text/javascript" src="js/calcTemp/conversionDateToTimestamp.js"></script>

</body>
</html>