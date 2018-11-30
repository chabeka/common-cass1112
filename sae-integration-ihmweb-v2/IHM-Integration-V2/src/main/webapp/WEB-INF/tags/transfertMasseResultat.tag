<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.TransfertMasseResultatFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<h4 id="etape-${numeroEtape}" class="etape text-primary">
Etape <c:out value="${numeroEtape}"/> : Lecture du r&eacute;sultat d'un traitement de masse sur l'ECDE
</h4>

<div class="row">
	<div class="col-md-6">
		<div class="form-group">
			<label>Emplacement du fichier sommaire.xml (URL ECDE) :</label>
         	<form:input class="form-control" path="${pathFormulaire}.urlSommaire" cssStyle="width:100%;" readonly="${readonly}" />
      	</div>
	</div>
	<div class="col-md-6">
		<sae:resultatTest objetResultats="${objetFormulaire.resultats}"
			pathResultats="${pathFormulaire}.resultats" height="190pt" />
	</div>
	<input class="btn btn-primary submit" type="submit"
	   value="Lecture du r&eacute;sultat du traitement de masse"
	   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'" />
</div>