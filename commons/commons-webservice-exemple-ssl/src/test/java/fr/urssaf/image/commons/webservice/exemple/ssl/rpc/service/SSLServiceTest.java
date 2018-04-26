package fr.urssaf.image.commons.webservice.exemple.ssl.rpc.service;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class SSLServiceTest {

   private static final Logger LOG = Logger.getLogger(SSLServiceTest.class);

   private SSLService service;

   @Before
   public void init() {

      service = new SSLServiceImpl();

   }

   @Test
   public void bonjour() {

      String resultat = service.bonjour("nom", "prenom");
      LOG.debug(resultat);
      assertEquals("échec du test", "Hello prenom nom (en rpc/literal)",
            resultat);

   }

   @Test
   public void multiplie() {

      long resultat = service.multiplie(4, 6);
      LOG.debug(resultat);
      assertEquals("échec du test", 24, resultat);

   }

}
