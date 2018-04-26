package fr.urssaf.image.commons.webservice.wssecurity.spring.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.util.date.DateUtil;
import fr.urssaf.image.commons.webservice.wssecurity.spring.modele.Document;
import fr.urssaf.image.commons.webservice.wssecurity.spring.modele.Etat;

@Service
@WebService(serviceName = "DocumentService", endpointInterface = "fr.urssaf.image.commons.webservice.wssecurity.spring.service.DocumentService", portName = "DocumentServicePort")
public class DocumentServiceImpl implements DocumentService {

	private final Map<Integer, Document> documents = new HashMap<Integer, Document>();

	public DocumentServiceImpl() {

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
	
	@Override
	public List<Document> allDocuments() {

		Document[] docs = new Document[documents.size()];

		int index = 0;
		for (Document document : documents.values()) {
			docs[index] = document;
			index++;
		}

		return Arrays.asList(docs);
	}

	@Override
	@SuppressWarnings("PMD.ShortVariable") 
	public Document getDocument(int id) {

		return documents.get(id);
	}

	@Override
	public void save(Document document) {

		document.setId(documents.size());

		documents.put(document.getId(), document);
	}

}
