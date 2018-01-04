package fr.urssaf.image.commons.webservice.exemple.rpc.encoded.service;

import java.rmi.RemoteException;
import java.util.List;

import fr.urssaf.image.commons.webservice.exemple.rpc.encoded.modele.Document;

public interface DocumentService {

	List<Document> allDocuments() throws RemoteException;

	void save(Document document) throws RemoteException;

	@SuppressWarnings("PMD.ShortVariable") 
	Document get(int id) throws RemoteException;
}