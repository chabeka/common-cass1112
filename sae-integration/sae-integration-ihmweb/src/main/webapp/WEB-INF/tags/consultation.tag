<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<h4 id="etape-${numeroEtape}" class="etape text-primary">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de consultation
</h4>

<div class="row">
	<div class="col-md-6">
		<div class="radio">
			<p><b>SERVICE A APPELER</b></p>
	  		<label>
				<form:radiobutton path="${pathFormulaire}.modeConsult" value="AncienServiceSansMtom" title="Ancien service - sans MTOM" label="Ancien service - sans MTOM" />
      		</label>
		</div>
		<div class="radio">
	  		<label>
				<form:radiobutton path="${pathFormulaire}.modeConsult" value="NouveauServiceAvecMtom" title="Nouveau service - avec MTOM" label="Nouveau service - avec MTOM" />
      		</label>
		</div>
        <div class="form-group">
           <label>ID ARCHIVAGE</label>
           <form:input class="form-control" path="${pathFormulaire}.idArchivage" cssStyle="width:100%;" readonly="${readonly}" />
        </div>
        <div class="form-group">
           <label>CODES METAS SOUHAITEES</label>
           <form:textarea class="form-control" path="${pathFormulaire}.codeMetadonnees" cssStyle="width:100%;height:150pt;" readonly="${readonly}" />
        </div>
	</div>	
	<div class="col-md-6">
		<sae:resultatTest objetResultats="${objetFormulaire.resultats}" pathResultats="${pathFormulaire}.resultats" height="190pt" />
	</div>

	<input class="btn btn-primary submit" type="submit"
	   value="Appel du service web de consultation"
	   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'"  />
</div>

