package fr.urssaf.image.commons.springsecurity.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import fr.urssaf.image.commons.springsecurity.service.modele.Modele;

@WebService
public interface SimpleWebService {

   @WebMethod
   void save(@WebParam(name = "title") String title,
         @WebParam(name = "text") String text);

   @WebMethod
   Modele load();
}
