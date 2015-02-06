<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>SAE - Intégration</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- Twitter boootstrap -->
	<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="css/style.css"  />
</head>
<body>
	<h3 class="text-primary">Gel d'un document</h3>
	<div class="pull-right" >
		<a href="/sae-integration-ihmweb">Retour &agrave; l'accueil</a>
	</div>
	
	<hr/>
	<form:form method="post" modelAttribute="formulaire">
		<div class="col-md-6">
			<label>IDENTIFIANT DU DOCUMENT</label>
			<form:input cssClass="form-control" path="idDocument" />
		</div>
		<div class="col-md-6" style="margin-top:20px;">
			<form:textarea cssClass="form-control" path="resultats" rows="10" readonly="true" />
		</div>
		<div class="col-md-6">
			<br/>
			<input class="btn btn-primary" type="submit" value="Appel de la fonction de gel" />
		</div>
	</form:form>
</body>
</html>