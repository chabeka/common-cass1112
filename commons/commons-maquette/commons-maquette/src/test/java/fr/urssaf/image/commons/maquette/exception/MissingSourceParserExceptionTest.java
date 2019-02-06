package fr.urssaf.image.commons.maquette.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link MissingSourceParserException}<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class MissingSourceParserExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurEtGetterSetterTest()
   {
      new MissingSourceParserException();
      new MissingSourceParserException("un_message");
      new MissingSourceParserException(new Exception());
      new MissingSourceParserException("un_message",new Exception());
   }
   
   
}
