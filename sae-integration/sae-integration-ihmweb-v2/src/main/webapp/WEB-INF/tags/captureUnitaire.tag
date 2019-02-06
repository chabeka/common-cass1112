<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<h4 id="etape-${numeroEtape}" class="etape text-primary">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de capture unitaire</h4>

<div class="row">
	<div class="col-md-6">

		<div class="radio">
	  		<label>
	    		<form:radiobutton path="${pathFormulaire}.modeCapture" value="archivageUnitaire" title="ArchivageUnitaire (avec URL ECDE)" label="ArchivageUnitaire (avec URL ECDE)" />
	  		</label>
		</div>
		<div class="radio">
	  		<label>
	    		<form:radiobutton path="${pathFormulaire}.modeCapture" value="archivageUnitairePJUrlEcde" title="ArchivageUnitairePJ avec URL ECDE" label="ArchivageUnitairePJ avec URL ECDE" />
	  		</label>
		</div>
		<div class="radio">
	  		<label>
	    		<form:radiobutton path="${pathFormulaire}.modeCapture" value="archivageUnitairePJContenuSansMtom" title="ArchivageUnitairePJ avec contenu sans MTOM" label="ArchivageUnitairePJ avec contenu sans MTOM" />
	  		</label>
		</div>
		<div class="radio">
	  		<label>
	    		<form:radiobutton path="${pathFormulaire}.modeCapture" value="archivageUnitairePJContenuAvecMtom" title="ArchivageUnitairePJ avec contenu avec MTOM" label="ArchivageUnitairePJ avec contenu avec MTOM" />
	  		</label>
		</div>
          
		<div class="form-group">
			<label class="col-sm-3 control-label">URL ECDE</label>
			<div class="col-sm-9">
				<form:input path="${pathFormulaire}.urlEcde" class="form-control" readonly="${readonly}" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">NOM DU FICHIER</label>
			<div class="col-sm-9">
				<form:input path="${pathFormulaire}.nomFichier" class="form-control" readonly="${readonly}" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">METAS</label>
			<div class="col-sm-9">
				<form:textarea path="${pathFormulaire}.metadonnees" class="form-control" cssStyle="width:100%;height:250px;" readonly="${readonly}"  />
			</div>
		</div>
    </div>
    <div class="col-md-6">
       <sae:resultatTest objetResultats="${objetFormulaire.resultats}"
          pathResultats="${pathFormulaire}.resultats"  height="324pt" />
    </div>
	
	<input class="btn btn-primary submit" type="submit"
	   value="Appel du service web de capture unitaire"
	   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'"  />
</div>