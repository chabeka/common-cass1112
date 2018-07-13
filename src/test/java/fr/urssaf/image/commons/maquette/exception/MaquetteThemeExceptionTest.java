package fr.urssaf.image.commons.maquette.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link MaquetteThemeException}<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class MaquetteThemeExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurEtGetterSetterTest()
   {
      new MaquetteThemeException();
      new MaquetteThemeException("un_message");
      new MaquetteThemeException(new Exception());
      new MaquetteThemeException("un_message",new Exception());
   }
   
   
}
