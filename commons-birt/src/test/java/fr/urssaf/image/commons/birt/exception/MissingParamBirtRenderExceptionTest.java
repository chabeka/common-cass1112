package fr.urssaf.image.commons.birt.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link MissingParamBirtRenderException}<br>
 * Principalement pour le code coverage 
 *
 */
@SuppressWarnings("PMD")
public class MissingParamBirtRenderExceptionTest {

   @Test
   public void constructeurs()
   {
      new MissingParamBirtRenderException();
      new MissingParamBirtRenderException("un_message");
      new MissingParamBirtRenderException(new Exception());
      new MissingParamBirtRenderException("un_message",new Exception());
   }
   
}
