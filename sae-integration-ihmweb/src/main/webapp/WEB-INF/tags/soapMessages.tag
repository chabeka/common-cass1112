<%@ tag body-content="empty"%>
<%@ attribute name="objetFormulaire" required="true"
	type="fr.urssaf.image.sae.integration.ihmweb.formulaire.SoapFormulaire"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h4 class="etape text-primary">Echanges Web-Services</h4>

<div class="row marginBottom20">
	<div class="col-md-6">
		<label>MESSAGE ENVOYE</label>
		<textarea class="form-control" rows="15" cols="50" readonly="readonly"><c:out value="${objetFormulaire.messageOut}" /></textarea>
	</div>
	<div class="col-md-6">
		<label>MESSAGE RECU</label>
		<textarea class="form-control" rows="15" cols="50" readonly="readonly"><c:out value="${objetFormulaire.messageIn}" /></textarea>
	</div>
</div>
