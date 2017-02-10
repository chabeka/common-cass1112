<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.EtatTraitementMasseFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ attribute name="notesSpecifiques" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<h4 id="etape-${numeroEtape}" class="etape text-primary">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de Etat Traitement Masse
</h4>

<div class="row">
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

<div class="row">
	<div class="col-md-6">
		<div class="form-group">
    		<label class="col-sm-3 control-label">LISTE UUID</label>
    		<div class="col-sm-9">
    			<form:textarea path="${pathFormulaire}.requeteListeUUID" class="form-control" cssStyle="height:132px;" readonly="${readonly}" />
  			</div>
  		</div>
	</div>
	<div class="col-md-6">
		<div class="form-group">
			<sae:resultatTest objetResultats="${objetFormulaire.resultats}"
            pathResultats="${pathFormulaire}.resultats" />
		</div> 
	</div>
</div>

<input
   class="btn btn-primary submit" 
   type="submit"
   value="Appel du service web de etatTraitementMasse"
   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'"  />

</div>