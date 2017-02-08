<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>SAE - Intégration</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<!-- Twitter boootstrap -->
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link
	href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css"
	rel="stylesheet">
</head>
<body>
	<h3 class="text-primary">Gestion des tests de non-regression</h3>
	<div class="pull-right">
		<table width="100%">
			<tr>
				<td colspan="2" align="right"><a href='listeTests.do'>Retour
						&agrave; la liste des tests</a></td>
			</tr>
			<tr>
				<td colspan="2" align="right"><a href='index.do'>Retour
						&agrave; l'accueil</a></td>
			</tr>

		</table>
	</div>
	
	<c:choose>
		<c:when test="${resSauvegarde == true}">
			<h5 class="bg-success pad15">
				<c:out value="${description}" />
			</h5>
		</c:when>
		<c:when test="${resSauvegarde == false}">
			<h5 class="bg-danger pad15">
				<c:out value="${description}" />
			</h5>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	
	<hr />

	<form method="post" enctype="multipart/form-data">
		<input type="hidden" name="action" id="action" /> sauvegarder des
		tests XML sur le serveur :
		<input type="file" name="file" multiple />
		<br />
		<input type="submit"
			onclick="document.getElementById('action').value='sauvegarderTestXml'"
			value="sauvegarder" />
	</form>

	<hr />
	
	<form method="post" enctype="multipart/form-data">
		<input type="hidden" name="action2" id="action2" /> 
		 sauvegarder des tests contenant l'attendu sur le serveur :
		<input type="file" name="file2" multiple/>
		<br />
		<input type="submit"
			onclick="document.getElementById('action2').value='sauvegarderTestAttendu'"
			value="sauvegarder" />
	</form>
	
	<hr />
	
	<form method="post" enctype="multipart/form-data">
		<input type="hidden" name="action3" id="action3" /> 
		sauvegarder des tests de non regression sur le serveur :
		<input type="file" name="file3" multiple/>
		<br />
		<input type="submit"
			onclick="document.getElementById('action3').value='sauvegarderTestRegression'"
			value="sauvegarder" />
	</form>
	
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.2.0/js/bootstrap.min.js'></script>
</body>
</html>