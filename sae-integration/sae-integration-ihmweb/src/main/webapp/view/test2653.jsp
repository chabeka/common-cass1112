<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		
			<form:hidden path="etape" />
		
			<sae:casTest />
		
			<sae:urlServiceWeb />
			
			<sae:vi
		      objetFormulaire="${formulaire.viFormulaire}"
		      pathFormulaire="viFormulaire" />
			
			<sae:captureUnitaire numeroEtape="1"
				objetFormulaire="${formulaire.captureUnitaire}"
				pathFormulaire="captureUnitaire" readonly="false" />
		
			<sae:transfert numeroEtape="2"
				objetFormulaire="${formulaire.transfert}"
				pathFormulaire="transfert" />
				
			<sae:recherche numeroEtape="3"
				objetFormulaire="${formulaire.recherche}" pathFormulaire="recherche"
				readonly="false" />
				
			<sae:recherche numeroEtape="4"
				objetFormulaire="${formulaire.rechercheGns}" pathFormulaire="rechercheGns"
				readonly="false" />
				
			<sae:soapMessages objetFormulaire="${formulaire.soapFormulaire}" />
		   
		</form:form>
	</div>
</body>
</html>