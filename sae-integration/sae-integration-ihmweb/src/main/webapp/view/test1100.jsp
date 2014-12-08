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
		
			<form:hidden id="etape" path="etape" />
		   
		   <sae:casTest />
		
			<sae:urlServiceWeb />
		   
		   <sae:vi
		      objetFormulaire="${formulaire.viFormulaire}"
		      pathFormulaire="viFormulaire" />
		
		
			<sae:captureUnitaire
		      numeroEtape="Capture unitaire"
				objetFormulaire="${formulaire.captureUnitaire}"
				pathFormulaire="captureUnitaire" />
		
				
			<sae:captureMasse
		      numeroEtape="Capture Masse #1"
		      objetFormulaire="${formulaire.captureMasseDeclenchement}"
		      pathFormulaire="captureMasseDeclenchement" />
		
		   <sae:captureMasseResultat
		      numeroEtape="Capture Masse #2"
		      objetFormulaire="${formulaire.captureMasseResultat}"
		      pathFormulaire="captureMasseResultat" />
		   
		   <sae:comptagesTdm
		      numeroEtape="Capture Masse #3"
		      pathFormulaire="comptagesFormulaire"
		      objetFormulaire="${formulaire.comptagesFormulaire}" />
		   
		   
		   <sae:recherche
		      numeroEtape="Recherche"
		      objetFormulaire="${formulaire.recherche}"
		      pathFormulaire="recherche" />
		   
		   
		   <sae:consultation
		      numeroEtape="Consultation"
		      objetFormulaire="${formulaire.consultation}"
		      pathFormulaire="consultation" />
		   
		   
		   <sae:soapMessages
		      objetFormulaire="${formulaire.soapFormulaire}" />
		      
		</form:form>
	</div>
</body>
</html>