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
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>

<form:form method="post" modelAttribute="formulaire">

   <sae:casTest />

   <sae:urlServiceWeb />
   
   <form:hidden path="etape" />

   <sae:captureUnitaire numeroEtape="1"
      objetFormulaire="${formulaire.captureUnitaire}" pathFormulaire="captureUnitaire"
      readonly="false" />
      
   <sae:lienRegistreSurvTec />

   <sae:recherche numeroEtape="2"
      objetFormulaire="${formulaire.recherche1}" pathFormulaire="recherche1"
      readonly="false" />
      
   <sae:recherche numeroEtape="3"
      objetFormulaire="${formulaire.recherche2}" pathFormulaire="recherche2"
      readonly="false" />

   <sae:soapMessages objetFormulaire="${formulaire.soapFormulaire}" />

</form:form>

<p><br />
<br />
<br />
<br />
</p>
</body>
</html>