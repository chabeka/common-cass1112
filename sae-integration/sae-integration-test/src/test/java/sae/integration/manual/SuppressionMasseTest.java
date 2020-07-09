
package sae.integration.manual;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environments;
import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.webservice.modele.SuppressionMasseRequestType;
import sae.integration.webservice.modele.SuppressionMasseResponseType;

/**
 * Test une reprise d'un traitement de masse
 */
public class SuppressionMasseTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(SuppressionMasseTest.class);

  @Test
  /**
   * Suppression de masse
   */
  public void suppressionMasseTest() throws Exception {
    final SaeServicePortType service = SaeServiceStubFactory.getServiceForInjecteur(Environments.GNS_INT_PAJE.getUrl());
    // final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_CSPP.getUrl());

    final SuppressionMasseRequestType request = new SuppressionMasseRequestType();

    final String requete = "Test:123";
    request.setRequete(requete);

    try {
      LOGGER.info("Lancement de la suppression de masse");
      final SuppressionMasseResponseType response = service.suppressionMasse(request);
      LOGGER.info("UUID suppression masse : {}", response.getUuid().toString());
    }
    catch (final SOAPFaultException e) {
      LOGGER.info("Exception reçue : {}", e.getMessage());
      LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
      Assert.fail("On n'attendait pas d'exception");
    }

  }
}