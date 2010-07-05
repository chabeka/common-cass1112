package fr.urssaf.image.commons.controller.spring.exemple.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;


import fr.urssaf.image.commons.controller.spring.exemple.formulaire.FormFormulaire;
import fr.urssaf.image.commons.controller.spring.exemple.formulaire.TableFormulaire;
import fr.urssaf.image.commons.controller.spring.exemple.modele.Document;
import fr.urssaf.image.commons.controller.spring.exemple.modele.Etat;
import fr.urssaf.image.commons.util.date.DateUtil;

@Service
public class DocumentService {

	private final Map<Integer, Document> documents = new HashMap<Integer, Document>();

	public DocumentService() {

		Document document0 = new Document();

		document0.setId(0);
		document0.setTitre("titre 0");
		document0.setEtat(Etat.close);
		document0.setFlag(true);
		document0.setLevel(3);
		document0.setOpenDate(DateUtil.today(-2));
		document0.setCloseDate(DateUtil.yesterday());
		document0.setComment("ceci est un commentaire");

		documents.put(document0.getId(), document0);

		Document document1 = new Document();

		document1.setId(1);
		document1.setTitre("titre 1");
		document1.setEtat(Etat.open);
		document1.setFlag(true);
		document1.setLevel(1);
		document1.setOpenDate(DateUtil.today(-2));
		document1.setCloseDate(DateUtil.yesterday());
		document1.setComment("un autre commentaire");

		documents.put(document1.getId(), document1);

		Document document2 = new Document();

		document2.setId(2);
		document2.setTitre("titre 2");
		document2.setEtat(Etat.init);
		document2.setFlag(false);
		document2.setLevel(2);
		document2.setOpenDate(DateUtil.today(-2));
		document2.setCloseDate(DateUtil.yesterday());

		documents.put(document2.getId(), document2);

	}

	public List<Document> allDocuments() {

		List<Document> docs = new ArrayList<Document>();

		for (Document document : documents.values()) {
			docs.add(document);
		}

		return docs;
	}

	public void save(FormFormulaire formulaire) {

		Document document = new Document();

		document.setTitre(formulaire.getTitre());
		document.setCloseDate(formulaire.getCloseDate());
		document.setOpenDate(formulaire.getOpenDate());
		document.setEtat(formulaire.getEtat());
		document.setFlag(formulaire.getFlag());
		document.setLevel(formulaire.getLevel());
		document.setEtats(formulaire.getEtats());
		document.setComment(formulaire.getInterneFormulaire().getComment());
		
		document.setId(Collections.max(documents.keySet()) + 1);

		documents.put(document.getId(), document);
	}

	public void update(TableFormulaire formulaire) {

		for (Document document : formulaire.getDocuments()) {

			document.setTitre(formulaire.getTitres().get(document.getId()));
			document.setOpenDate(formulaire.getOpenDates()
					.get(document.getId()));
			document.setCloseDate(formulaire.getCloseDates().get(
					document.getId()));
			document.setEtat(formulaire.getEtats().get(document.getId()));
			document.setLevel(formulaire.getLevels().get(document.getId()));
			document.setFlag(BooleanUtils.toBoolean(formulaire.getFlags().get(document.getId())));
			document.setEtats(formulaire.getEtatss().get(document.getId()));
			document.setComment(formulaire.getComments().get(document.getId()));
		}
	}
}
