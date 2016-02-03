package fr.urssaf.image.sae.services.batch.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.services.batch.TraitementAsynchroneService;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capturemasse-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class TraitementAsynchroneServiceValidationTest {

   private TraitementAsynchroneService service;

   private static final String FAIL_MESSAGE = "Une exception de type illegalArgumentException doit être levée";

   private static final String EXCEPTION_MESSAGE = "Le message de l'exception est inattendu";

   @Before
   public void before() {

      service = new TraitementAsynchroneService() {


         @Override
         public void lancerJob(UUID idJob) {

            // aucune implémentation

         }

         @Override
         public void ajouterJobCaptureMasse(TraitemetMasseParametres parametres) {
            // TODO Auto-generated method stub
            
         }

         @Override
         public void ajouterJobRestoreMasse(TraitemetMasseParametres parametres) {
            // TODO Auto-generated method stub
            
         }

         @Override
         public void ajouterJobSuppressionMasse(
               TraitemetMasseParametres parametres) {
            // TODO Auto-generated method stub
            
         }

      };
   }

   private static final UUID UUID_CAPTURE = UUID.randomUUID();

   private static final String URL_ECDE = "sommaire.xml";

   @Test
   public void ajouterJobCaptureMasse_success() {
      
      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.ECDE_URL, URL_ECDE);
     
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(jobParams, UUID_CAPTURE, Constantes.TYPES_JOB.capture_masse.name(), null, null, null, null);

      service.ajouterJobCaptureMasse(parametres);
   }

   @Test
   public void ajouterJobCaptureMasse_failure_empty_urlEcde() {

      assertAjouterJobCaptureMasse_urlEcde(null);
      assertAjouterJobCaptureMasse_urlEcde("");
      assertAjouterJobCaptureMasse_urlEcde(" ");
   }

   private void assertAjouterJobCaptureMasse_urlEcde(String urlECDE) {

      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.ECDE_URL, urlECDE);
     
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(jobParams, UUID_CAPTURE, Constantes.TYPES_JOB.capture_masse.name(), null, null, null, null);

      
      try {
         service.ajouterJobCaptureMasse(parametres);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'urlEcde' doit être renseigné.", e.getMessage());
      }
   }

   @Test
   public void ajouterJobCaptureMasse_failure_empty_uuid() {

      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.ECDE_URL, URL_ECDE);
     
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(jobParams, null, Constantes.TYPES_JOB.capture_masse.name(), null, null, null, null);


      try {
         service.ajouterJobCaptureMasse(parametres);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'uuid' doit être renseigné.", e.getMessage());
      }
   }

   @Test
   public void lancerJob_failure_empty_idJob() throws JobInexistantException,
         JobNonReserveException {

      try {
         service.lancerJob(null);

         Assert.fail(FAIL_MESSAGE);

      } catch (IllegalArgumentException e) {

         Assert.assertEquals(EXCEPTION_MESSAGE,
               "L'argument 'idJob' doit être renseigné.", e.getMessage());
      }
   }

}
