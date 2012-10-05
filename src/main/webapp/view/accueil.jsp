<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@page import="java.util.ArrayList"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>IHM Web Exploit - Accueil</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/displaytag.css" rel="stylesheet" type="text/css" />
<link href="css/formulaire.css" rel="stylesheet" type="text/css" />
</head>
<body>
 
<div style="margin-top: 30px">
<form:form method="post" modelAttribute="sessionForm">
	<label for="choixConfig">Environnement sélectionné :</label>
	<form:select id="choixConfig" path="configuration" onchange="submit();">
		<form:options items="${listeNomConf}" />
	</form:select>

</form:form>

<table style=" text-align:center; margin-top: 20px;">
	<thead><tr><td colspan="2">Détail de la configuration</td></tr></thead>
	<tr><td>URL des services Web SaeService : </td><td>${detailConfig.urlWs}</td></tr>
	<tr><td>DNS ou IP des serveurs zookeper : </td><td>${detailConfig.zookeeperHost}</td></tr>
	<tr><td>NameSpace(s) zookeeper : </td><td>${detailConfig.zookeeperNameSpace}</td></tr>
	<tr><td>DNS ou IP des serveurs Cassandra : </td><td>${detailConfig.cassandraHost}</td></tr>
</table>

</div>
</body>
</html>


