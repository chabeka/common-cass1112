<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.EnvoiSoapFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<p id="etape-${numeroEtape}" style="font-weight:bold;text-decoration:underline;">
Etape <c:out value="${numeroEtape}"/> : Envoi de message SOAP
</p>


<table border=0 cellspacing=3 cellpadding=3 style="width:100%;">
   <tr style="vertical-align:top;">
      <td style="width:50%;">
         <table border=0 cellspacing=3 cellpadding=3 style="width:100%;">
            <tr style="vertical-align:top;">
               <td style="width:20%;">SOAP Request :</td>
               <td style="width:80%;">
                  <form:textarea path="${pathFormulaire}.soapRequest" cssStyle="width:100%;height:250px;" readonly="true" />
               </td>
            </tr>
         </table>
      </td>
      <td style="width:50%;border-left-width:2px;border-left-color:black;border-left-style:solid;">
         <!-- Mettre le retour SOAP ici -->
         &nbsp;
      </td>
   </tr>
</table>

<!-- Mettre ici le submit -->

<br />

<hr />