<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<p style="font-weight:bold;text-decoration:underline;">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de capture unitaire
</p>


<table border=0 cellspacing=3 cellpadding=3 style="width:100%;">
   <tr style="vertical-align:top;">
      <td style="width:50%;">
         <table border=0 cellspacing=3 cellpadding=3 style="width:100%;">
            <tr>
               <td style="width:20%;vertical-align:top;">Service à appeler:</td>
               <td style="width:80%;" >
                  <form:radiobutton path="${pathFormulaire}.modeCapture" value="archivageUnitaire" title="archivageUnitaire (avec URL ECDE)" label="archivageUnitaire (avec URL ECDE)" />
                  <br />
                  <form:radiobutton path="${pathFormulaire}.modeCapture" value="archivageUnitairePJUrlEcde" title="archivageUnitairePJ avec URL ECDE" label="archivageUnitairePJ avec URL ECDE" />
                  <br />
                  <form:radiobutton path="${pathFormulaire}.modeCapture" value="archivageUnitairePJContenuSansMtom" title="archivageUnitairePJ avec contenu sans MTOM" label="archivageUnitairePJ avec contenu sans MTOM" />
                  <br />
                  <form:radiobutton path="${pathFormulaire}.modeCapture" value="archivageUnitairePJContenuAvecMtom" title="archivageUnitairePJ avec contenu avec MTOM" label="archivageUnitairePJ avec contenu avec MTOM" />
               </td>
            </tr>
            <tr>
               <td>URL ECDE:</td>
               <td>
                  <form:input path="${pathFormulaire}.urlEcde" cssStyle="width:100%;" readonly="${readonly}" />
               </td>
            </tr>
            <tr>
               <td>Nom du fichier:</td>
               <td>
                  <form:input path="${pathFormulaire}.nomFichier" cssStyle="width:100%;" readonly="${readonly}" />
               </td>
            </tr>
            <tr style="vertical-align:top;">
               <td>Métadonnées :</td>
               <td>
                  <form:textarea path="${pathFormulaire}.metadonnees" cssStyle="width:100%;height:250px;" readonly="${readonly}" />
               </td>
            </tr>
         </table>
      </td>
      <td style="width:50%;border-left-width:2px;border-left-color:black;border-left-style:solid;">
         <sae:resultatTest
            objetResultats="${objetFormulaire.resultats}"
            pathResultats="${pathFormulaire}.resultats"
            height="280pt" />
      </td>
   </tr>
</table>

<input
   style="width:100%;"
   type="submit"
   value="Appel du service web de capture unitaire"
   onclick="javascript:document.getElementById('etape').value=<c:out value="${numeroEtape}"/>"  />

<hr />