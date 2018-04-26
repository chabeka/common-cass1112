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
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationAffichableParametrageException;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class SAEConsultationServiceValidationTest {

   private SAEConsultationService service;

   @Before
   public void before() {

      service = new SAEConsultationService() {

         @Override
         public UntypedDocument consultation(UUID idArchive)
               throws SAEConsultationServiceException,
               UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {

            return null;
         }

         @Override
         public UntypedDocument consultation(ConsultParams consultParams)
               throws SAEConsultationServiceException,
               UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {
            return null;
         }

         @Override
         public UntypedDocument consultationAffichable(
               ConsultParams consultParams)
               throws SAEConsultationServiceException,
               UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx,
               SAEConsultationAffichableParametrageException {
            return null;
         }

      };
   }

   @Test
   public void consultation_success() throws SAEConsultationServiceException,
         UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx {

      UUID idArchive = UUID.fromString("3ae2e9ba-6e81-4c0e-a6b4-3ed64adc76a0");

      try {
         service.consultation(idArchive);

      } catch (IllegalArgumentException e) {
         fail("les arguments en entrée doivent être valides");
      }

   }

   @Test
   public void consultation_failure_idArchive_null()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {

      try {
         service.consultation((UUID) null);
         fail("l'argument idArchive doit être renseigné");
      } catch (IllegalArgumentException e) {
         assertEquals("message d'exception non attendu",
               "L'argument 'idArchive' doit être renseigné ou être non null.",
               e.getMessage());
      }

   }

   @Test
   public void consultation_failure_consultParams_null()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {

      try {
         service.consultation((ConsultParams) null);
         fail("l'argument consultParams doit être renseigné");
      } catch (IllegalArgumentException e) {
         assertEquals(
               "message d'exception non attendu",
               "L'argument 'consultParams' doit être renseigné ou être non null.",
               e.getMessage());
      }

   }

   @Test
   public void consultation_failure_consultParams_notNull_idArchiveNull()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {

      try {
         ConsultParams consultParams = new ConsultParams(null);
         service.consultation(consultParams);
         fail("l'argument idArchive doit être renseigné");
      } catch (IllegalArgumentException e) {
         assertEquals("message d'exception non attendu",
               "L'argument 'idArchive' doit être renseigné ou être non null.",
               e.getMessage());
      }

   }

   @Test
   public void consultationAffichable_success()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException {

      ConsultParams consultParams = new ConsultParams(UUID
            .fromString("3ae2e9ba-6e81-4c0e-a6b4-3ed64adc76a0"));

      try {
         service.consultationAffichable(consultParams);

      } catch (IllegalArgumentException e) {
         fail("les arguments en entrée doivent être valides");
      }

   }

   @Test
   public void consultationAffichable_failure_consultParams_null()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException {

      try {
         service.consultationAffichable((ConsultParams) null);
         fail("l'argument consultParams doit être renseigné");
      } catch (IllegalArgumentException e) {
         assertEquals(
               "message d'exception non attendu",
               "L'argument 'consultParams' doit être renseigné ou être non null.",
               e.getMessage());
      }

   }

   @Test
   public void consultationAffichable_failure_consultParams_notNull_idArchiveNull()
         throws SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx,
         SAEConsultationAffichableParametrageException {

      try {
         ConsultParams consultParams = new ConsultParams(null);
         service.consultationAffichable(consultParams);
         fail("l'argument idArchive doit être renseigné");
      } catch (IllegalArgumentException e) {
         assertEquals("message d'exception non attendu",
               "L'argument 'idArchive' doit être renseigné ou être non null.",
               e.getMessage());
      }

   }

}
