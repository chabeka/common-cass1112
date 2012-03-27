<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ attribute name="notesSpecifiques" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<p style="font-weight:bold;text-decoration:underline;">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de recherche
</p>



<c:if test="${fn:length(notesSpecifiques) > 0}" >

   <table border=0 cellpadding=0 cellspacing=0>
      <tr>
         <td style="width:10pt;">&nbsp;</td>
         <td>
            <p><b><u>Notes</u></b> : <c:out value="${notesSpecifiques}" /></p>
         </td>
      </tr>
   </table>
   
</c:if>

<table border=0 cellspacing=3 cellpadding=3 style="width:100%;">
   <tr style="vertical-align:top;">
      <td style="width:50%;">
         <table border=0 cellspacing=3 cellpadding=3 style="width:100%;">
            <tr style="vertical-align:top;">
               <td style="width:20%;">Requête LUCENE:</td>
               <td style="width:80%;">
                  <form:textarea path="${pathFormulaire}.requeteLucene" cssStyle="width:100%;height:130px;" readonly="${readonly}" />
               </td>
            </tr>
            <tr style="vertical-align:top;">
               <td>Code des métadonnées souhaitées :</td>
               <td>
                  <form:textarea path="${pathFormulaire}.codeMetadonnees" cssStyle="width:100%;height:130px;" readonly="${readonly}" />
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
   value="Appel du service web de recherche"
   onclick="javascript:document.getElementById('etape').value=<c:out value="${numeroEtape}"/>"  />

<hr />