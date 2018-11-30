<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.StockageUnitaireFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<h4 id="etape-${numeroEtape}" class="etape text-primary">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de stockage unitaire</h4>

<div class="row">
	<div class="col-md-6">

		<div class="radio">
	  		<label>
	    		<form:radiobutton path="${pathFormulaire}.modeStockage" value="stockageUnitaireAvecUrlEcde" title="StockageUnitaire (avec URL ECDE)" label="StockageUnitaire avec UrlEcde" />
	  		</label>
		</div>
		<div class="radio">
	  		<label>
	    		<form:radiobutton path="${pathFormulaire}.modeStockage" value="stockageUnitaireAvecContenuAvecMTOM" title="StockageUnitairePJ avec URL ECDE" label="StockageUnitaire avec contenu avec MTOM" />
	  		</label>
		</div>
		<div class="radio">
	  		<label>
	    		<form:radiobutton path="${pathFormulaire}.modeStockage" value="stockageUnitaireAvecContenuSansMTOM" title="StockageUnitairePJ avec contenu sans MTOM" label="StockageUnitaire avec contenu sans MTOM" />
	  		</label>
		</div>
          
		<div class="form-group">
			<label class="col-sm-6 control-label">URL ECDE</label>
			<div class="col-sm-9">
				<form:input path="${pathFormulaire}.urlEcde" class="form-control" readonly="${readonly}" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-6 control-label">URL ECDE ORIGINE</label>
			<div class="col-sm-9">
				<form:input path="${pathFormulaire}.urlEcdeOrig" class="form-control" readonly="${readonly}" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-6 control-label">NOM DU FICHIER A ARCHIVER</label>
			<div class="col-sm-9">
				<form:input path="${pathFormulaire}.nomFichier" class="form-control" readonly="${readonly}" />
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-6 control-label">NOM DU FICHIER ORIGINE</label>
			<div class="col-sm-9">
				<form:input path="${pathFormulaire}.nomFichierOrig" class="form-control" readonly="${readonly}" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-6 control-label">METAS</label>
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
	   value="Appel du service web de stockage unitaire"
	   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'"  />
</div>