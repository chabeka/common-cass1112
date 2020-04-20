package fr.urssaf.image.sae.services.batch.capturemasse.utils;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;

/**
 * Factory de mocks pour les test
 */
public final class MockFactory {

   private MockFactory() {
   }

   /**
    * @return un mock de type {@link SAEConvertMetadataService}
    */
   public static SAEConvertMetadataService returnMock() {
      return EasyMock.createMock(SAEConvertMetadataService.class);
   }

   /**
    * @return un mock de type {@link StaxUtils}
    */
   public static StaxUtils returnStaxUtils() {
      return EasyMock.createMock(StaxUtils.class);
   }
}
