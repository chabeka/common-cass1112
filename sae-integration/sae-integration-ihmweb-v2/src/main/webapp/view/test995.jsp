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
	<script type="text/javascript">
	
		var cocher = false;
		
		function toutCocherDecocher() {
		
		   document.getElementsByName('test001.aLancer')[0].checked = cocher;
		   document.getElementsByName('test002.aLancer')[0].checked = cocher;
		   document.getElementsByName('test003.aLancer')[0].checked = cocher;
		   document.getElementsByName('test004.aLancer')[0].checked = cocher;
		   document.getElementsByName('test005.aLancer')[0].checked = cocher;
		   document.getElementsByName('test006.aLancer')[0].checked = cocher;
		   document.getElementsByName('test007.aLancer')[0].checked = cocher;
		   document.getElementsByName('test008.aLancer')[0].checked = cocher;
		
		   cocher = !cocher;
		}
	</script>
</head>
<body>

<form:form method="post" modelAttribute="formulaire" name="formulaire">

<sae:casTest/>

<sae:urlServiceWeb/>
<p>
<input
   style="width:100%;"
   type="submit"
   value="Lancer les tests cochés" />
</p>

<p>
<input type="button" value="Tout cocher/décocher" onclick="javascript:toutCocherDecocher();" />
</p>
<table border="1" style="width:100%; font-size:14px;">

   <tr style="width:70%;font-weight: bold;text-align:center;">
      <td style="width:30%">Cas de test</td>
      <td style="width:10%">Résultat</td>
      <td style="width:60%">Détails</td>
   </tr>
   
   <tr>
      <td><form:checkbox path="test001.aLancer" label="${formulaire.test001.code}" /></td>
      <sae:statusTest objetStatus="${formulaire.test001.status}"/>
      <td><form:textarea path="test001.details" cssStyle="width:100%;height:20pt;" readonly="true" /></td>
   </tr>
   
   <tr>
      <td><form:checkbox path="test002.aLancer" label="${formulaire.test002.code}" /></td>
      <sae:statusTest objetStatus="${formulaire.test002.status}"/>
      <td><form:textarea path="test002.details" cssStyle="width:100%;height:20pt;" readonly="true" /></td>
   </tr>
   
   <tr>
      <td><form:checkbox path="test003.aLancer" label="${formulaire.test003.code}" /></td>
      <sae:statusTest objetStatus="${formulaire.test003.status}"/>
      <td><form:textarea path="test003.details" cssStyle="width:100%;height:20pt;" readonly="true" /></td>
   </tr>
   
   <tr>
      <td><form:checkbox path="test004.aLancer" label="${formulaire.test004.code}" /></td>
      <sae:statusTest objetStatus="${formulaire.test004.status}"/>
      <td><form:textarea path="test004.details" cssStyle="width:100%;height:20pt;" readonly="true" /></td>
   </tr>
   
   <tr>
      <td><form:checkbox path="test005.aLancer" label="${formulaire.test005.code}" /></td>
      <sae:statusTest objetStatus="${formulaire.test005.status}"/>
      <td><form:textarea path="test005.details" cssStyle="width:100%;height:20pt;" readonly="true" /></td>
   </tr>
   
   <tr>
      <td><form:checkbox path="test006.aLancer" label="${formulaire.test006.code}" /></td>
      <sae:statusTest objetStatus="${formulaire.test006.status}"/>
      <td><form:textarea path="test006.details" cssStyle="width:100%;height:20pt;" readonly="true" /></td>
   </tr>
   
   <tr>
      <td><form:checkbox path="test007.aLancer" label="${formulaire.test007.code}" /></td>
      <sae:statusTest objetStatus="${formulaire.test007.status}"/>
      <td><form:textarea path="test007.details" cssStyle="width:100%;height:20pt;" readonly="true" /></td>
   </tr>
   
   <tr>
      <td><form:checkbox path="test008.aLancer" label="${formulaire.test008.code}" /></td>
      <sae:statusTest objetStatus="${formulaire.test008.status}"/>
      <td><form:textarea path="test008.details" cssStyle="width:100%;height:20pt;" readonly="true" /></td>
   </tr>
   
</table>


</form:form>

<p><br /><br /><br /><br /></p>
</body>
</html>