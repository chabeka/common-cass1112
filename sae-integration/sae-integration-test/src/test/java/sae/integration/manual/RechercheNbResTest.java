
package sae.integration.manual;

import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environments;
import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ListeMetadonneeCodeType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.RechercheNbResRequestType;
import sae.integration.webservice.modele.RechercheNbResResponseType;
import sae.integration.webservice.modele.ResultatRechercheType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Test une recherche simple
 */
public class RechercheNbResTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(RechercheNbResTest.class);

   @Test
   /**
    * Recherche par Siret
    */
   public void rechercheSiretTest() throws Exception {
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_CSPP.getUrl());

      final RechercheNbResRequestType request = new RechercheNbResRequestType();
      final ListeMetadonneeCodeType metasToGet = new ListeMetadonneeCodeType();
      metasToGet.getMetadonneeCode().add("Siret");
      metasToGet.getMetadonneeCode().add("DateArchivage");
      request.setMetadonnees(metasToGet);
      // request.setRequete("Siret:33005008900029");
      request.setRequete("Siret:33005008* AND DateCreation:[20131001 TO 20181201]");
      // request.setRequete("NumeroCompteExterne:3170000010108095*");
      try {
         LOGGER.info("Lancement de la recherche");
         final RechercheNbResResponseType response = service.rechercheNbRes(request);
         final List<ResultatRechercheType> resultats = response.getResultats().getResultat();
         LOGGER.info("Nombre de documents trouvés : {}", resultats.size());
         for (final ResultatRechercheType resultat : resultats) {
            final ListeMetadonneeType metas = resultat.getMetadonnees();
            LOGGER.info("Doc id {} : {}", resultat.getIdArchive(), SoapHelper.getMetasAsString(metas));
         }

      }
      catch (final SOAPFaultException e) {
         LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }
   }
}