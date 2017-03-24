package fr.urssaf.image.commons.birt.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link NullFactoryBirtEngineException}<br>
 * Principalement pour le code coverage 
 *
 */
@SuppressWarnings("PMD")
public class NullFactoryBirtEngineExceptionTest {

   @Test
   public void constructeurs()
   {
      new NullFactoryBirtEngineException();
      new NullFactoryBirtEngineException("un_message");
      new NullFactoryBirtEngineException(new Exception());
      new NullFactoryBirtEngineException("un_message",new Exception());
   }
   
}
