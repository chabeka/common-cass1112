package fr.urssaf.image.commons.webservice.wssecurity.spring.client.service;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;

import fr.urssaf.image.commons.webservice.wssecurity.spring.client.modele.Document;
import fr.urssaf.image.commons.webservice.wssecurity.spring.client.modele.DocumentService_Service;

public class DocumentServiceImpl implements DocumentService {

	private final fr.urssaf.image.commons.webservice.wssecurity.spring.client.modele.DocumentService port;

	private static final String BLANC = " ";
	
	public DocumentServiceImpl() {

		DocumentService_Service service = new DocumentService_Service();

		this.port = service.getDocumentServicePort();

		Client client = ClientProxy.getClient(this.port);
		Endpoint cxfEndpoint = client.getEndpoint();
		initWSS4JOutInterceptor(cxfEndpoint);
		initWSS4JInInterceptor(cxfEndpoint);

	}

	@SuppressWarnings("PMD.ConsecutiveLiteralAppends")
	private void initWSS4JOutInterceptor(Endpoint cxfEndpoint) {

		Map<String, Object> outProps = new HashMap<String, Object>();
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);

		cxfEndpoint.getOutInterceptors().add(wssOut);
		cxfEndpoint.getOutInterceptors().add(new SAAJOutInterceptor());

		StringBuffer action = new StringBuffer();
		action.append(WSHandlerConstants.USERNAME_TOKEN);
		action.append(BLANC);
		action.append(WSHandlerConstants.SIGNATURE);
		action.append(BLANC);
		action.append(WSHandlerConstants.ENCRYPT);
		action.append(BLANC);
		action.append(WSHandlerConstants.TIMESTAMP);
		
		outProps.put(WSHandlerConstants.ACTION, action.toString());
		outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
				ClientPasswordCallback.class.getName());

		// USERNAME_TOKEN
		outProps.put(WSHandlerConstants.USER, "myuser");
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);

		// SIGNATURE
		outProps.put(WSHandlerConstants.SIGNATURE_USER, "myclientkey");
		outProps.put(WSHandlerConstants.SIG_PROP_FILE,
				"clientKeyStore.properties");

		// ENCRYPT
		outProps.put(WSHandlerConstants.ENCRYPTION_USER, "myservicekey");
		outProps.put(WSHandlerConstants.ENC_PROP_FILE,
				"clientKeyStore.properties");

		StringBuffer parts = new StringBuffer(177);

		parts.append("{Content}{http://www.w3.org/2000/09/xmldsig#}Signature;");
		parts
				.append("{Content}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd}UsernameToken;");
		parts.append("{Content}{}Body;");

		outProps.put(WSHandlerConstants.ENCRYPTION_PARTS, parts.toString());
	}

	private void initWSS4JInInterceptor(Endpoint cxfEndpoint) {

		Map<String, Object> inProps = new HashMap<String, Object>();
		WSS4JInInterceptor wssIn = new WSS4JInInterceptor(inProps);
		wssIn.setIgnoreActions(true);
		cxfEndpoint.getInInterceptors().add(wssIn);

		cxfEndpoint.getInInterceptors().add(new SAAJInInterceptor());

		StringBuffer action = new StringBuffer();
		action.append(WSHandlerConstants.ENCRYPT);

		inProps.put(WSHandlerConstants.ACTION, action.toString());
		inProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
				ClientPasswordCallback.class.getName());

		// ENCRYPT
		inProps.put(WSHandlerConstants.DEC_PROP_FILE,
				"clientKeyStore.properties");

	}

	@Override
	public List<Document> allDocuments() throws RemoteException {
		return port.allDocuments();
	}

	@Override
	@SuppressWarnings("PMD.ShortVariable")
	public Document get(int id) throws RemoteException {
		return port.getDocument(id);
	}

	@Override
	public void save(Document document) throws RemoteException {
		port.save(document);

	}

}
