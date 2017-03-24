package fr.urssaf.image.commons.webservice.wssecurity.spring.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import fr.urssaf.image.commons.webservice.wssecurity.spring.modele.Document;

@WebService
public interface DocumentService {

	@WebMethod
	List<Document> allDocuments();
	
	@WebMethod
	@SuppressWarnings("PMD.ShortVariable")
	Document getDocument(int id);
	
	@WebMethod
	void save(Document document);
	
}
