<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<p id="etape-${numeroEtape}" style="font-weight:bold;text-decoration:underline;">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de capture de masse
</p>


<table border=0 cellspacing=3 cellpadding=3 style="width:100%;">
   <tr style="vertical-align:top;">
      <td style="width:50%;">
         <table border=0 cellspacing=3 cellpadding=3 style="width:100%;">
            <tr>
               <td style="width:20%;">URL Sommaire:</td>
               <td style="width:80%;">
                  <form:input path="${pathFormulaire}.urlSommaire" cssStyle="width:100%;" readonly="${readonly}" />
               </td>
            </tr>
         </table>
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
   value="Appel du service web de capture de masse"
   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'"  />

<hr />