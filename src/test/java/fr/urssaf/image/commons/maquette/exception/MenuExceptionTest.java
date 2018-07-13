package fr.urssaf.image.commons.maquette.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link MenuException}<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class MenuExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurEtGetterSetterTest()
   {
      new MenuException();
      new MenuException("un_message");
      new MenuException(new Exception());
      new MenuException("un_message",new Exception());
   }
   
   
}
