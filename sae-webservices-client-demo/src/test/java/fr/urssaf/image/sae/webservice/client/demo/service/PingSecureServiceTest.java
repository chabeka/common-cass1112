package fr.urssaf.image.sae.webservice.client.demo.service;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.sae.webservice.client.demo.util.AssertXML;

public class PingSecureServiceTest {

   private static PingSecureService service;

   private static final Logger LOG = Logger
         .getLogger(PingSecureServiceTest.class);

   @BeforeClass
   public static void beforeClass() {

      service = new PingSecureService();

   }

   @Test
   public void pingSecure_success() {

      String response = service.pingSecure("ROLE_TOUS;FULL");

      LOG.debug(response);

      AssertXML.assertElementContent(
            "Les services du SAE sécurisés par authentification sont en ligne",
            "http://www.cirtil.fr/saeService", "pingString", response);
   }

   @Test
   public void pingSecure_failure() {

      String response = service.pingSecure("ROLE_OTHER;FULL");

      LOG.debug(response);

      AssertXML
            .assertElementContent(
                  "Les droits présents dans le vecteur d'identification sont insuffisants pour effectuer l'action demandée",
                  "", "faultstring", response);
   }

}
