<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
    <%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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

	<sae:captureMasse numeroEtape="1"
		objetFormulaire="${formulaire.captureMasseDeclenchement}"
		pathFormulaire="captureMasseDeclenchement" />

	<sae:captureMasseResultat numeroEtape="2"
		objetFormulaire="${formulaire.captureMasseResultat}"
		pathFormulaire="captureMasseResultat" />

	<sae:recherche numeroEtape="3" pathFormulaire="rechFormulaire"
		objetFormulaire="${formulaire.rechFormulaire}" />
	
	<sae:soapMessages objetFormulaire="${formulaire.soapFormulaire}" />
		
</form:form>

<p><br />
<br />
<br />
<br />
</p>
</body>
</html>