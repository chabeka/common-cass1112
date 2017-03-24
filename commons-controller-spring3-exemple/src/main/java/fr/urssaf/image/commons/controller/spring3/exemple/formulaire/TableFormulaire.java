package fr.urssaf.image.commons.controller.spring3.exemple.formulaire;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.validation.Errors;

import fr.urssaf.image.commons.controller.spring3.exemple.modele.Document;

public class TableFormulaire {

	private List<Document> documents;

	public List<Document> getDocuments() {
		return documents;
	}

	public void initDocuments(List<Document> documents) {
		this.documents = documents;
		interneFormulaire.clear();

		for (Document document : documents) {
			@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
			FormFormulaire formulaire = new FormFormulaire();
			interneFormulaire.put(document.getId(), formulaire);
			formulaire.init(document);
			
		}
	}

	public Map<Integer, FormFormulaire> interneFormulaire = new HashMap<Integer, FormFormulaire>();

	@Valid
	public Map<Integer, FormFormulaire> getInterneFormulaire() {
		return this.interneFormulaire;
	}
	
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public void validate(Errors errors) {
		
		for(Integer id:interneFormulaire.keySet()){
			
			FormFormulaire formulaire = interneFormulaire.get(id);
			
			RuleFormUtil.DateRule dateRule = new RuleFormUtil.DateRule(formulaire.getCloseDate(),
					formulaire.getOpenDate());
			
			if (!dateRule.isValid()) {
				errors.rejectValue("interneFormulaire["+id+"].closeDate","validDate");
			}
		}
		
	}

}
