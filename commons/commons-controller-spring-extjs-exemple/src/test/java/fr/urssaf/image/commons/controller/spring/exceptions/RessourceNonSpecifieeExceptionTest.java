package fr.urssaf.image.commons.controller.spring.exceptions;

import org.junit.Test;


/**
 * Tests unitaires de la classe RessourceNonSpecifieeException<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class RessourceNonSpecifieeExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurTest()
   {
      new RessourceNonSpecifieeException();
      new RessourceNonSpecifieeException("un_message");
      new RessourceNonSpecifieeException(new Exception());
      new RessourceNonSpecifieeException("un_message",new Exception());
   }
   
   
}
