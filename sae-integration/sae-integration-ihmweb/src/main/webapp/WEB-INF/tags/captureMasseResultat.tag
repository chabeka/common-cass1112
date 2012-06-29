<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<p style="font-weight:bold;text-decoration:underline;">
Etape <c:out value="${numeroEtape}"/> : Lecture du résultat d'un traitement de masse sur l'ECDE
</p>


<table border=0 cellspacing=3 cellpadding=3 style="width:100%;">
   <tr style="vertical-align:top;">
      <td style="width:50%;">
         <p>Emplacement du fichier sommaire.xml (URL ECDE) :</p>
         <p><form:input path="${pathFormulaire}.urlSommaire" cssStyle="width:100%;" readonly="${readonly}" /></p>
      </td>
      <td style="width:50%;border-left-width:2px;border-left-color:black;border-left-style:solid;">
         <sae:resultatTest
            objetResultats="${objetFormulaire.resultats}"
            pathResultats="${pathFormulaire}.resultats" />
      </td>
   </tr>
</table>

<input
   style="width:100%;"
   type="submit"
   value="Lecture du résultats du traitement de masse"
   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'" />

<hr />