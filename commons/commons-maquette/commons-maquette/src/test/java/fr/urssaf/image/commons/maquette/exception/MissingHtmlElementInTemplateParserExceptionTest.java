package fr.urssaf.image.commons.maquette.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link MissingHtmlElementInTemplateParserException}<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class MissingHtmlElementInTemplateParserExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurEtGetterSetterTest()
   {
      new MissingHtmlElementInTemplateParserException();
      new MissingHtmlElementInTemplateParserException("un_message");
      new MissingHtmlElementInTemplateParserException(new Exception());
      new MissingHtmlElementInTemplateParserException("un_message",new Exception());
   }
   
   
}
