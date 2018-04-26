package fr.urssaf.image.commons.webservice.exemple.ssl.rpc.service;

import java.math.BigInteger;

import fr.urssaf.image.commons.webservice.exemple.ssl.rpc.modele.HelloService;
import fr.urssaf.image.commons.webservice.exemple.ssl.rpc.modele.HelloServicePortType;

public class SSLServiceImpl implements SSLService {

   private final HelloServicePortType port;

   public SSLServiceImpl() {
      HelloService locator = new HelloService();
      port = locator.getHelloServicePort();

   }

   @Override
   public String bonjour(String nom, String prenom) {
      return port.bonjour(nom, prenom);
   }

   @Override
   public long multiplie(long valeur1, long valeur2) {
      return port.multiplie(BigInteger.valueOf(valeur1),
            BigInteger.valueOf(valeur2)).longValue();
   }
}
