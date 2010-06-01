package fr.urssaf.image.commons.webservice.spring.exemple.service.rpc.encoded;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import fr.urssaf.image.commons.webservice.spring.exemple.modele.Document;

@WebService
@SOAPBinding(style=Style.RPC, use=Use.ENCODED)
public interface DocService {

	@WebMethod
	public Document[] allDocuments();
	
	@WebMethod
	public Document getDocument(@WebParam(name = "id")int id);
	
	@WebMethod
	public void save(@WebParam(name = "document")Document document);
}
