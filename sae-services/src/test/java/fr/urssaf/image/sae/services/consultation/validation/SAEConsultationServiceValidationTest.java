package fr.urssaf.image.sae.services.consultation.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SAEConsultationServiceValidationTest {

   private SAEConsultationService service;

   @Before
   public void before() {

      service = new SAEConsultationService() {

         @Override
         public UntypedDocument consultation(UUID idArchive)
               throws SAEConsultationServiceException {

            return null;
         }

      };
   }

   @Test
   public void consultation_success() throws SAEConsultationServiceException {

      UUID idArchive = UUID.fromString("3ae2e9ba-6e81-4c0e-a6b4-3ed64adc76a0");

      try {
         service.consultation(idArchive);

      } catch (IllegalArgumentException e) {
         fail("les arguments en entrée doivent être valides");
      }

   }

   @Test
   public void consultation_failure_idArchive_null()
         throws SAEConsultationServiceException {

      try {
         service.consultation(null);
         fail("l'argument idArchive doit être renseigné");
      } catch (IllegalArgumentException e) {
         assertEquals("message d'exception non attendu",
               "L'argument 'idArchive' doit être renseigné ou être non null.", e
                     .getMessage());
      }

   }

}
