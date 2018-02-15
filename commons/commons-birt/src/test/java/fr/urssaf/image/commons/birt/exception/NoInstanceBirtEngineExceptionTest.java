package fr.urssaf.image.commons.birt.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link NoInstanceBirtEngineException}<br>
 * Principalement pour le code coverage 
 *
 */
@SuppressWarnings("PMD")
public class NoInstanceBirtEngineExceptionTest {

   @Test
   public void constructeurs()
   {
      new NoInstanceBirtEngineException();
      new NoInstanceBirtEngineException("un_message");
      new NoInstanceBirtEngineException(new Exception());
      new NoInstanceBirtEngineException("un_message",new Exception());
   }
   
}
