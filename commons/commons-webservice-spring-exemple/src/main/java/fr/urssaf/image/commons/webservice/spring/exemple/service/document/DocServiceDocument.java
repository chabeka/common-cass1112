package fr.urssaf.image.commons.webservice.spring.exemple.service.document;

import java.util.Arrays;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.webservice.spring.exemple.modele.Document;
import fr.urssaf.image.commons.webservice.spring.exemple.service.DocumentServiceImpl;

@Service
@WebService(serviceName = "DocServiceDocument", endpointInterface = "fr.urssaf.image.commons.webservice.spring.exemple.service.document.DocService", portName = "DocumentServicePort")
public class DocServiceDocument implements DocService {

	@Autowired
	private DocumentServiceImpl impl;
	
	@Override
	public List<Document> allDocuments() {
		return Arrays.asList(impl.allDocuments());
	}

	@Override
	@SuppressWarnings("PMD.ShortVariable") 
	public Document getDocument(int id) {
		return impl.getDocument(id);
	}

	@Override
	public void save(Document document) {
		impl.save(document);
	}

}
