<%@ tag body-content="empty" %>
<%@ attribute name="numeroEtape" required="true" type="java.lang.String" %>
<%@ attribute name="objetFormulaire" required="true" type="fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire" %>
<%@ attribute name="pathFormulaire" required="true" type="java.lang.String" %>
<%@ attribute name="readonly" required="false" type="java.lang.String" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/tld/sae_integration.tld" prefix="sae" %>

<h4 id="etape-${numeroEtape}" class="etape text-primary">
Etape <c:out value="${numeroEtape}"/> : Appel du service web de copie</h4>

<div class="row">
	<div class="col-md-6">
		<div class="form-group">
    		<label class="col-sm-3 control-label">ID GED</label>
    		<div class="col-sm-9">
    			<form:textarea path="${pathFormulaire}.idGed" class="form-control" cssStyle="width:100%;" readonly="${readonly}" />
  			</div>
  		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label">LISTE METAS SOUHAITES</label>
			<div class="col-sm-9">
				<form:textarea path="${pathFormulaire}.listeMetadonnees" class="form-control" cssStyle="height:132px;margin-top:10px;" readonly="${readonly}" />
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
   value="Appel du service web de copie"
   onclick="javascript:document.getElementById('etape').value='<c:out value="${numeroEtape}"/>'"  />

<hr />