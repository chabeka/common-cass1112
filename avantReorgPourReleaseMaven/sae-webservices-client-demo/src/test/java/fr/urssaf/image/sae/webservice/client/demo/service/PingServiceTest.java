package fr.urssaf.image.sae.webservice.client.demo.service;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.sae.webservice.client.demo.util.AssertXML;

public class PingServiceTest {

   private static PingService service;

   private static final Logger LOG = Logger.getLogger(PingServiceTest.class);

   @BeforeClass
   public static void beforeClass() {

      service = new PingService();
   }

   @Test
   public void ping_success() {

      String response = service.ping();

      LOG.debug(response);

      AssertXML.assertElementContent("Les services SAE sont en ligne",
            "http://www.cirtil.fr/saeService", "pingString", response);
   }

}
