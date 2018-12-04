<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
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
	<div class="container">
	<form:form method="post" modelAttribute="formulaire">
	
	   <sae:casTest />
	
	   <sae:urlServiceWeb />
	   
	   <sae:vi objetFormulaire="${formulaire.viFormulaire}" pathFormulaire="viFormulaire" />
	   
	   <form:hidden path="etape" />
	
	   <sae:stockageUnitaire numeroEtape="1"
	      objetFormulaire="${formulaire.stockageUnitaire}" pathFormulaire="stockageUnitaire"
	      readonly="false" />
	
	   <sae:recherche numeroEtape="2"
	      objetFormulaire="${formulaire.recherche}" pathFormulaire="recherche"
	      readonly="false" />
	      
	<sae:consultation numeroEtape="3"
		objetFormulaire="${formulaire.consultation}"
		pathFormulaire="consultation" />

	<sae:soapMessages objetFormulaire="${formulaire.soapFormulaire}" />
	      	
	</form:form>
</div>
</body>
</html>