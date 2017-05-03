package fr.urssaf.image.sae.webservice.client.demo.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.webservice.client.demo.util.AssertXML;

public class PingServiceTest {

   private static PingService service;

   private static final Logger LOG = LoggerFactory.getLogger(PingServiceTest.class);

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
