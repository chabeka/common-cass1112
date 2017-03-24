package fr.urssaf.image.commons.webservice.exemple.document.service;

import java.rmi.RemoteException;
import java.util.List;

import fr.urssaf.image.commons.webservice.exemple.document.modele.Document;

public interface DocumentService {

	List<Document> allDocuments() throws RemoteException;
	
	void save(Document document) throws RemoteException;
	
	@SuppressWarnings("PMD.ShortVariable") 
	Document get(int id) throws RemoteException;
}
