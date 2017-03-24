package fr.urssaf.image.commons.webservice.exemple.ssl.document.service;

import java.math.BigInteger;

import fr.urssaf.image.commons.webservice.exemple.ssl.document.modele.BonjourRequestType;
import fr.urssaf.image.commons.webservice.exemple.ssl.document.modele.HelloService;
import fr.urssaf.image.commons.webservice.exemple.ssl.document.modele.HelloServicePortType;
import fr.urssaf.image.commons.webservice.exemple.ssl.document.modele.MultiplieRequestType;

public class SSLServiceImpl implements SSLService {

   private final HelloServicePortType port;

   public SSLServiceImpl() {

      HelloService locator = new HelloService();
      port = locator.getHelloServicePort();

   }

   @Override
   public String bonjour(String nom, String prenom) {

      BonjourRequestType parameters = new BonjourRequestType();
      parameters.setNom(nom);
      parameters.setPrenom(prenom);

      return port.bonjour(parameters).getResultat();
   }

   @Override
   public long multiplie(long valeur1, long valeur2) {

      MultiplieRequestType parameters = new MultiplieRequestType();
      parameters.setValeur1(BigInteger.valueOf(valeur1));
      parameters.setValeur2(BigInteger.valueOf(valeur2));

      return port.multiplie(parameters).getProduit().longValue();
   }

}
