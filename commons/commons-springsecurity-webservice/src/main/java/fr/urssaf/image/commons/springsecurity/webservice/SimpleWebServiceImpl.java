package fr.urssaf.image.commons.springsecurity.webservice;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.springsecurity.service.SimpleService;
import fr.urssaf.image.commons.springsecurity.service.modele.Modele;

@Service
@WebService(serviceName = "SimpleService", endpointInterface = "fr.urssaf.image.commons.springsecurity.webservice.SimpleWebService", portName = "SimpleServicePort")
public class SimpleWebServiceImpl implements SimpleWebService {

   @Autowired
   private SimpleService service;

   @Override
   public final Modele load() {

      return service.load();
   }

   @Override
   public final void save(String title, String text) {

      Modele modele = new Modele();
      modele.setText(text);
      modele.setTitle(title);

      service.save(modele);

   }

}
