package fr.urssaf.image.commons.webservice.exemple.rpc.literal.service;

import java.rmi.RemoteException;
import java.util.List;

import fr.urssaf.image.commons.webservice.exemple.rpc.literal.modele.DocService;
import fr.urssaf.image.commons.webservice.exemple.rpc.literal.modele.DocServiceRpcLiteral;
import fr.urssaf.image.commons.webservice.exemple.rpc.literal.modele.Document;

public class DocumentServiceImpl implements DocumentService {

	private final DocService port;

	public DocumentServiceImpl() {

		DocServiceRpcLiteral service = new DocServiceRpcLiteral();
		port = service.getDocumentServicePort();

	}

	@Override
	public List<Document> allDocuments() throws RemoteException {
		return port.allDocuments().getItem();
	}
	
	@Override
	@SuppressWarnings("PMD.ShortVariable") 
	public Document get(
			int id) throws RemoteException {
		return port.getDocument(id);
	}

	@Override
	public void save(
			Document document)
			throws RemoteException {
		port.save(document);
		
	}

}
