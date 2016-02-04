package fr.urssaf.image.sae.services.batch.validation;

import java.text.MessageFormat;
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
import fr.urssaf.image.sae.services.batch.common.Constantes.TYPES_JOB;
import fr.urssaf.image.sae.services.batch.common.model.TraitemetMasseParametres;
import fr.urssaf.image.sae.services.batch.exception.JobNonReserveException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capturemasse-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class TraitementAsynchroneServiceValidationTest {

   private TraitementAsynchroneService service;

   private static final String FAIL_MESSAGE = "Une exception de type illegalArgumentException doit être levée";

   private static final String EXCEPTION_MESSAGE = "Le message de l'exception est inattendu";
   
   private static final String ARG_EMPTY = "L''argument ''{0}'' doit être renseigné.";

   @Before
   public void before() {

      service = new TraitementAsynchroneService() {
         @Override
         public void lancerJob(UUID idJob) {
            // Aucune implémentation
         }
         @Override
         public void ajouterJobCaptureMasse(TraitemetMasseParametres parametres) {
            // Aucune implémentation
         }
         @Override
         public void ajouterJobRestoreMasse(TraitemetMasseParametres parametres) {
            // Aucune implémentation
         }
         @Override
         public void ajouterJobSuppressionMasse(TraitemetMasseParametres parametres) {
            // Aucune implémentation
         }
      };
   }

   private static final UUID UUID_TRAITEMENT = UUID.randomUUID();
   private static final String URL_ECDE = "sommaire.xml";
   private static final String REQ_LUCENE = "cot:123456798";

   @Test
   public void ajouterJobCaptureMasse_success() {
      
      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.ECDE_URL, URL_ECDE);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, UUID_TRAITEMENT, TYPES_JOB.capture_masse.name(), 
            null, null, null, null);
      service.ajouterJobCaptureMasse(parametres);
   }

   @Test
   public void ajouterJobCaptureMasse_failure_empty_urlEcde() {
      assertAjouterJobCaptureMasse_urlEcde(null);
      assertAjouterJobCaptureMasse_urlEcde("");
      assertAjouterJobCaptureMasse_urlEcde(" ");
   }
   

   @Test
   public void ajouterJobCaptureMasse_failure_empty_uuid() {

      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.ECDE_URL, URL_ECDE);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, null, TYPES_JOB.capture_masse.name(), null, null, null, null);
      try {
         service.ajouterJobCaptureMasse(parametres);
         Assert.fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(EXCEPTION_MESSAGE,
               MessageFormat.format(ARG_EMPTY, "uuid"), e.getMessage());
      }
   }
   
   @Test
   public void ajouterJobCaptureMasse_failure_empty_type() {

      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.ECDE_URL, URL_ECDE);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, UUID_TRAITEMENT, null, null, null, null, null);
      try {
         service.ajouterJobCaptureMasse(parametres);
         Assert.fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(EXCEPTION_MESSAGE,
               MessageFormat.format(ARG_EMPTY, "type"), e.getMessage());
      }
   }

   @Test
   public void ajouterJobSuppressionMasse_success() {
      
      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.REQ_LUCENE_SUPPRESSION, UUID_TRAITEMENT.toString());
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, UUID_TRAITEMENT, TYPES_JOB.suppression_masse.name(), 
            null, null, null, null);
      service.ajouterJobSuppressionMasse(parametres);
   }
   
   
   @Test
   public void ajouterJobSuppressionMasse_failure_empty_requete() {
      assertAjouterJobSuppressionMasse_requete(null);
      assertAjouterJobSuppressionMasse_requete("");
      assertAjouterJobSuppressionMasse_requete(" ");
   }
   
   @Test
   public void ajouterJobSuppressionMasse_failure_empty_type() {

      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.ECDE_URL, URL_ECDE);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, UUID_TRAITEMENT, null, null, null, null, null);
      try {
         service.ajouterJobSuppressionMasse(parametres);
         Assert.fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(EXCEPTION_MESSAGE,
               MessageFormat.format(ARG_EMPTY, "type"), e.getMessage());
      }
   }
   
   @Test
   public void ajouterJobSuppressionMasse_failure_empty_uuid() {
      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.REQ_LUCENE_SUPPRESSION, REQ_LUCENE);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, null, TYPES_JOB.capture_masse.name(), null, null, null, null);
      try {
         service.ajouterJobSuppressionMasse(parametres);
         Assert.fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(EXCEPTION_MESSAGE,
               MessageFormat.format(ARG_EMPTY, "uuid"), e.getMessage());
      }
   }
   
   
   @Test
   public void ajouterJobRestoreMasse_success() {
      
      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.UUID_TRAITEMENT_RESTORE, "UUID_TRAITEMENT");
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, UUID_TRAITEMENT, TYPES_JOB.restore_masse.name(), null, null, null, null);
      service.ajouterJobRestoreMasse(parametres);
   }
   
   
   @Test
   public void ajouterJobRestoreMasse_failure_empty_type() {

      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.UUID_TRAITEMENT_RESTORE, "UUID_TRAITEMENT");
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, UUID_TRAITEMENT, null, null, null, null, null);
      try {
         service.ajouterJobRestoreMasse(parametres);
         Assert.fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(EXCEPTION_MESSAGE,
               MessageFormat.format(ARG_EMPTY, "type"), e.getMessage());
      }
   }
   
   @Test
   public void ajouterJobRestoreMasse_failure_empty_idTraitement() {
      assertAjouterJobRestoreMasse_idTraitement(null);
      assertAjouterJobRestoreMasse_idTraitement("");
      assertAjouterJobRestoreMasse_idTraitement(" ");
   }


   @Test
   public void ajouterJobRestoreMasse_failure_empty_uuid() {
      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.UUID_TRAITEMENT_RESTORE, REQ_LUCENE);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, null, TYPES_JOB.restore_masse.name(), null, null, null, null);
      try {
         service.ajouterJobRestoreMasse(parametres);
         Assert.fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(EXCEPTION_MESSAGE,
               MessageFormat.format(ARG_EMPTY, "uuid"), e.getMessage());
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
               MessageFormat.format(ARG_EMPTY, "idJob"), e.getMessage());
      }
   }
  

   private void assertAjouterJobRestoreMasse_idTraitement(String idTraitement) {

      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.UUID_TRAITEMENT_RESTORE, idTraitement);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, UUID_TRAITEMENT, TYPES_JOB.restore_masse.name(), null, null, null, null);
      try {
         service.ajouterJobRestoreMasse(parametres);
         Assert.fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(EXCEPTION_MESSAGE, 
               MessageFormat.format(ARG_EMPTY, "idTraitement"), e.getMessage());
      }
   } 
   
   private void assertAjouterJobCaptureMasse_urlEcde(String urlECDE) {

      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.ECDE_URL, urlECDE);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, UUID_TRAITEMENT, TYPES_JOB.capture_masse.name(), null, null, null, null);
      try {
         service.ajouterJobCaptureMasse(parametres);
         Assert.fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(EXCEPTION_MESSAGE, 
               MessageFormat.format(ARG_EMPTY, "urlEcde"), e.getMessage());
      }
   } 
   
   private void assertAjouterJobSuppressionMasse_requete(String requete) {
      
      Map<String, String> jobParams = new HashMap<String, String>();
      jobParams.put(Constantes.REQ_LUCENE_SUPPRESSION, requete);
      TraitemetMasseParametres parametres = new TraitemetMasseParametres(
            jobParams, UUID_TRAITEMENT, TYPES_JOB.capture_masse.name(), null, null, null, null);
      try {
         service.ajouterJobSuppressionMasse(parametres);
         Assert.fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(EXCEPTION_MESSAGE, 
               MessageFormat.format(ARG_EMPTY, "requete"), e.getMessage());
      }
   } 
   
}
