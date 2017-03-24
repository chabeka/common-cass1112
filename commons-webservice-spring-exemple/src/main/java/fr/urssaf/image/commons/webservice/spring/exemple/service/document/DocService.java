package fr.urssaf.image.commons.webservice.spring.exemple.service.document;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import fr.urssaf.image.commons.webservice.spring.exemple.modele.Document;

@WebService
public interface DocService {

	@WebMethod
	List<Document> allDocuments();
	
	@WebMethod
	@SuppressWarnings("PMD.ShortVariable") 
	Document getDocument(@WebParam(name = "id")int id);
	
	@WebMethod
	void save(@WebParam(name = "document")Document document);
}
