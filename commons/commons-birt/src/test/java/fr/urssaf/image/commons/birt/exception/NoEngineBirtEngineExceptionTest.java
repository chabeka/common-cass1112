package fr.urssaf.image.commons.birt.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link NoEngineBirtEngineException}<br>
 * Principalement pour le code coverage 
 *
 */
@SuppressWarnings("PMD")
public class NoEngineBirtEngineExceptionTest {

   @Test
   public void constructeurs()
   {
      new NoEngineBirtEngineException();
      new NoEngineBirtEngineException("un_message");
      new NoEngineBirtEngineException(new Exception());
      new NoEngineBirtEngineException("un_message",new Exception());
   }
   
}
