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

   
   <table border=0 style="width:100%;">
      <tr>
         <td style="width:15%;">Répertoire de référence : </td>
         <td style="width:85%;"><form:input path="repRef" cssStyle="width:100%;"/></td>
      </tr>
      <tr>
         <td>Répertoire de la passe :</td>
         <td><form:input path="repPasse" cssStyle="width:100%;"/></td>
      </tr>
      <tr>
         <td colspan="2"><input type="submit" value="Rafraîchir" /></td>
      </tr>
   </table>

   <br />

   <table border=1>
      
      <tr style="font-weight:bold;">
         <td style="width:120pt;">Nom du fichier</td>
         <td style="width:150pt;">Référence</td>
         <td style="width:150pt;">Passe comparée</td>
      </tr>
      
      <c:forEach items="${formulaire.listeFichiers}" var="fichier">
      
      
         <c:choose>
               
            <c:when test="${fichier.etatReference == 'REFERENCE'}">
               <c:set var="texteEtatRef" value="" />
               <c:set var="couleurFondEtatRef" value="white" />
            </c:when>
            
            <c:when test="${fichier.etatReference == 'MANQUANT'}">
               <c:set var="texteEtatRef" value="Non présent" />
               <c:set var="couleurFondEtatRef" value="orange" />
            </c:when>
               
            <c:otherwise>
               <c:set var="texteEtatRef" value="${fichier.etatReference}" />
               <c:set var="couleurFondEtatRef" value="orange" />
            </c:otherwise>
               
         </c:choose>
         
         
         <c:choose>
               
            <c:when test="${fichier.etatPasse == 'IDENTIQUE_REFERENCE'}">
               <c:set var="texteEtatPasse" value="OK" />
               <c:set var="couleurFondEtatPasse" value="green" />
               <c:set var="couleurPoliceEtatPasse" value="white" />
            </c:when>
            
            <c:when test="${fichier.etatPasse == 'DIFFERENT_REFERENCE'}">
               <c:set var="texteEtatPasse" value="différent" />
               <c:set var="couleurFondEtatPasse" value="red" />
               <c:set var="couleurPoliceEtatPasse" value="black" />
            </c:when>
            
            <c:when test="${fichier.etatPasse == 'EN_PLUS'}">
               <c:set var="texteEtatPasse" value="En +" />
               <c:set var="couleurFondEtatPasse" value="orange" />
               <c:set var="couleurPoliceEtatPasse" value="black" />
            </c:when>
            
            <c:when test="${fichier.etatPasse == 'EN_MOINS'}">
               <c:set var="texteEtatPasse" value="En -" />
               <c:set var="couleurFondEtatPasse" value="orange" />
               <c:set var="couleurPoliceEtatPasse" value="black" />
            </c:when>
               
            <c:otherwise>
               <c:set var="texteEtatPasse" value="${fichier.etatPasse}" />
               <c:set var="couleurFondEtatPasse" value="blue" />
               <c:set var="couleurPoliceEtatPasse" value="white" />
            </c:otherwise>
               
         </c:choose>
      
      
      
      <tr>
         
         <td style="background-color:${couleurFondEtatPasse};color:${couleurPoliceEtatPasse};">
            <c:out value="${fichier.nomFichier}"/>
         </td>
         
         <td style="background-color:${couleurFondEtatRef}">
            <c:out value="${texteEtatRef}"/>
         </td>
         
         <td style="background-color:${couleurFondEtatPasse};color:${couleurPoliceEtatPasse};">
            <c:out value="${texteEtatPasse}"/>
         </td>
      </tr>
   
      </c:forEach>
      
    </table>

</form:form>


</body>
</html>