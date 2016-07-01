<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.RestoreMasseFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<h4 id="etape-${numeroEtape}" class="etape text-primary">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de resoreMasse
</h4>

<div class="row">
	<div class="col-md-6">		
        <div class="form-group">
           <label>ID ARCHIVAGE</label>
           <form:input class="form-control" path="${pathFormulaire}.idArchivage" cssStyle="width:100%;" readonly="${readonly}" />
        </div>
	</div>	
	<div class="col-md-6">
		<sae:resultatTest objetResultats="${objetFormulaire.resultats}" pathResultats="${pathFormulaire}.resultats" height="190pt" />
	</div>

	<input class="btn btn-primary submit" type="submit"
	   value="Appel du service web de restoreMasse"
	   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'"  />
</div>

