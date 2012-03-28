<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
   <title>Comparateur de resultats.xml</title>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
   <link href="css/style.css" rel="stylesheet" type="text/css" />
</head>


<body>

<h1>Comparateur de resultats.xml</h1>


<form:form method="post" modelAttribute="formulaire">

   
   <table border=0>
      <tr>
         <td>Répertoire de référence : </td>
         <td><form:input path="repRef" cssStyle="width:400pt;"/></td>
      </tr>
      <tr>
         <td>Répertoire de la passe :</td>
         <td><form:input path="repPasse" cssStyle="width:400pt;"/></td>
      </tr>
      <tr>
         <td colspan="2"><input type="submit" value="Rafraîchir" /></td>
      </tr>
   </table>

   <br />

   <table border=1>
      
      <tr style="font-weight:bold;">
         <td>Nom du fichier</td>
         <td>Référence</td>
         <td>Passe comparée</td>
      </tr>
      
      <c:forEach items="${formulaire.listeFichiers}" var="fichier">
      
      <tr>
         <td><c:out value="${fichier.nomFichier}"/></td>
         <td><c:out value="${fichier.etatReference}"/></td>
         <td><c:out value="${fichier.etatPasse}"/></td>
      </tr>
   
      </c:forEach>
      
    </table>

</form:form>


</body>
</html>