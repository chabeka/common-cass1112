package fr.urssaf.image.commons.webservice.spring.exemple.service.rpc.encoded;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.webservice.spring.exemple.modele.Document;
import fr.urssaf.image.commons.webservice.spring.exemple.service.DocumentServiceImpl;

@Service
@WebService(serviceName = "DocServiceRpcEncoded", endpointInterface = "fr.urssaf.image.commons.webservice.spring.exemple.service.rpc.encoded.DocService", portName = "DocumentServicePort")
public class DocServiceRpcEncoded implements DocService {

	@Autowired
	private DocumentServiceImpl impl;

	@Override
	public Document[] allDocuments() {

		return impl.allDocuments();
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
