
package sae.integration.manual;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.RepriseRequestType;
import sae.integration.webservice.modele.RepriseResponseType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Test une reprise d'un traitement de masse
 */
public class RepriseTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(RepriseTest.class);

  @Test
  /**
   * Reprise d'un traitement de masse
   */
  public void repriseTest() throws Exception {
    // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
    // final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_CSPP.getUrl());
    final SaeServicePortType service = SaeServiceStubFactory.getServiceForSaturneGNT("http://hwi69gntweb.cer69.recouv/gnt/services/SaeService");

    final RepriseRequestType request = new RepriseRequestType();
    final String uuidTraitementMasse = "c6189820-8295-11ea-84d7-0050569b170f";
    request.setUuid(uuidTraitementMasse);

    try {
      LOGGER.info("Lancement de la reprise du traitement de masse");
      final RepriseResponseType response = service.reprise(request);
      LOGGER.info("UUID reprise : {}", response.getUuid().toString());
    }
    catch (final SOAPFaultException e) {
      LOGGER.info("Exception reçue : {}", e.getMessage());
      LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
      Assert.fail("On n'attendait pas d'exception");
    }

  }
}