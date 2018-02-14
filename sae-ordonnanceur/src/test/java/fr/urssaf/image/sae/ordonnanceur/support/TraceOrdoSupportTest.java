package fr.urssaf.image.sae.ordonnanceur.support;

import java.net.URI;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Tests unitaires de la classe TraceOrdoSupport
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-mock-test.xml" })
public class TraceOrdoSupportTest {

   @Autowired
   private TraceOrdoSupport traceOrdoSupport;

   @Test
   public void ecritTraceUrlEcdeNonDispo_success() {

      UUID idJob = UUID.randomUUID();

      URI urlEcde = URI.create("ecde://capture_masse/");

      TraceToCreate traceToCreate = traceOrdoSupport.ecritTraceUrlEcdeNonDispo(
            idJob, urlEcde);

      // On vérifie qu'il n'y a pas eu de gros plantage non prévu
      Assert.assertNotNull("La trace n'a pas été produite", traceToCreate);

      // Vérifie quelques informations, histoire de ...
      Assert.assertNotNull(
            "L'information 'Action' devrait être renseignée dans la trace",
            traceToCreate.getAction());
      Assert.assertEquals(
            "L'information 'code événement' de la trace est incorrect",
            TraceOrdoSupport.TRC_CODE_EVT_ECDE_INDISPO, traceToCreate
                  .getCodeEvt());
      Assert.assertEquals("L'information 'contexte' de la trace est incorrect",
            TraceOrdoSupport.TRC_CONTEXTE, traceToCreate.getContexte());
      Assert
            .assertNull(
                  "L'information 'Contrat de service' ne devrait pas être renseignée dans la trace",
                  traceToCreate.getContrat());
      Assert.assertNotNull(
            "L'information 'infos supp' devrait être renseignée dans la trace",
            traceToCreate.getInfos());
      Assert
            .assertEquals(
                  "Le nombre d'informations supplémentaires de la trace est incorrect",
                  4, traceToCreate.getInfos().size());
      Assert
            .assertNull(
                  "L'information 'Login' ne devrait pas être renseignée dans la trace",
                  traceToCreate.getLogin());
      Assert
            .assertNull(
                  "L'information 'Liste des PAGM' ne devrait pas être renseignée dans la trace",
                  traceToCreate.getLogin());
      Assert
            .assertNull(
                  "L'information 'StackTrace' ne devrait pas être renseignée dans la trace",
                  traceToCreate.getStracktrace());

   }

}
