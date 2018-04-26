package fr.urssaf.image.commons.birt.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link EnvVarNotSettedBirtEngineException} <br>
 * Principalement pour le code coverage 
 *
 */
@SuppressWarnings("PMD")
public class EnvVarNotSettedBirtEngineExceptionTest {

   @Test
   public void constructeurs()
   {
      new EnvVarNotSettedBirtEngineException();
      new EnvVarNotSettedBirtEngineException("un_message");
      new EnvVarNotSettedBirtEngineException(new Exception());
      new EnvVarNotSettedBirtEngineException("un_message",new Exception());
   }
   
}
