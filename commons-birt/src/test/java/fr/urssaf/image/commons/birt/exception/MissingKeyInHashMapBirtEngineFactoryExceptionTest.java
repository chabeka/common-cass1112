package fr.urssaf.image.commons.birt.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link MissingKeyInHashMapBirtEngineFactoryException}<br>
 * Principalement pour le code coverage 
 *
 */
@SuppressWarnings("PMD")
public class MissingKeyInHashMapBirtEngineFactoryExceptionTest {

   @Test
   public void constructeurs()
   {
      new MissingKeyInHashMapBirtEngineFactoryException();
      new MissingKeyInHashMapBirtEngineFactoryException("un_message");
      new MissingKeyInHashMapBirtEngineFactoryException(new Exception());
      new MissingKeyInHashMapBirtEngineFactoryException("un_message",new Exception());
   }
   
}
