<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationMasseFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<h4 id="etape-${numeroEtape}" class="etape text-primary">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de modification de masse
</h4>

<div class="row">
	<div class="col-md-6">
		<div class="form-group">
			<label>URL Sommaire</label>
			<form:input class="form-control" path="${pathFormulaire}.urlSommaire" cssStyle="width:100%;" readonly="${readonly}" />
		</div>
		<div class="form-group">
			<form:checkbox label="Avec hash du sommaire" path="${pathFormulaire}.avecHash" />
		</div>
		<div class="form-group">
			<label>HASH</label>
			<form:input class="form-control" path="${pathFormulaire}.hash" cssStyle="width:100%;" readonly="${readonly}" />
		</div>
		<div class="form-group">
			<label>TYPE HASH</label>
			<form:input class="form-control" path="${pathFormulaire}.typeHash" cssStyle="width:100%;" readonly="${readonly}" />
		</div>
		<div class="form-group">
			<label>CODE TRAITEMENT</label>
			<form:input class="form-control" path="${pathFormulaire}.codeTraitement" cssStyle="width:100%;" readonly="${readonly}" />
		</div>
	</div>
	<div class="col-md-6">
		<sae:resultatTest objetResultats="${objetFormulaire.resultats}"
			pathResultats="${pathFormulaire}.resultats" height="190pt" />
	</div>

	<input class="btn btn-primary submit" type="submit"
	   value="Appel du service web de modification de masse"
	   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'"  />
</div>