package fr.urssaf.image.commons.birt.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link MissingConstructorParamBirtException}<br>
 * Principalement pour le code coverage 
 *
 */
@SuppressWarnings("PMD")
public class MissingConstructorParamBirtExceptionTest {

   @Test
   public void constructeurs()
   {
      new MissingConstructorParamBirtException();
      new MissingConstructorParamBirtException("un_message");
      new MissingConstructorParamBirtException(new Exception());
      new MissingConstructorParamBirtException("un_message",new Exception());
   }
   
}
