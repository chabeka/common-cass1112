package sae.integration.manual;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.webservice.modele.TransfertMasseRequestType;
import sae.integration.webservice.modele.TransfertMasseResponseType;

/**
 * Pour tester le debug local ou distant d'un traitement de masse
 */
public class TransfertMasse {

   private static final Logger LOGGER = LoggerFactory.getLogger(TransfertMasse.class);

   @Test
   /**
    * transfert de masse
    */
   public void TransfertMasseTest() throws Exception {
      final Environment environnementGNT = Environments.GNT_INT_INTERNE;
      final SaeServicePortType gntService = SaeServiceStubFactory.getServiceForDevToutesActions(environnementGNT.getUrl());
      /*
       * final String urlSommaire = "ecde://cnp69devecde.cer69.recouv/SAE_INTEGRATION/20110822/TransfertMasse-3708-TransfertMasse-OK-Suppression-droit-modif-meta-1/sommaire.xml";
       * final String hash = "d9f2cfd44b0b1659d558b2e872ae116dbb81fb38";
       */

      final String urlSommaire = "ecde://cnp69devecde.cer69.recouv/SAE_INTEGRATION/20110822/TransfertMasse-3708-TransfertMasse-OK-Suppression-droit-modif-meta-2/sommaire.xml";
      final String hash = "a487ba11c0c51c4b96467e40b20091cec082fe0c";

      final TransfertMasseRequestType request = new TransfertMasseRequestType();
      request.setHash(hash);
      request.setTypeHash("SHA-1");
      request.setUrlSommaire(urlSommaire);
      final TransfertMasseResponseType response = gntService.transfertMasse(request);

   }


}
