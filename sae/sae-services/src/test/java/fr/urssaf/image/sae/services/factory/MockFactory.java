package fr.urssaf.image.sae.services.factory;

import org.easymock.EasyMock;

import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;

public class MockFactory {

   public static SAEConvertMetadataService returnMock(){
      return EasyMock.createMock(SAEConvertMetadataService.class);
   }
}
