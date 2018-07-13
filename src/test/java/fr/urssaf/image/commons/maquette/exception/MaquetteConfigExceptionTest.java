package fr.urssaf.image.commons.maquette.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link MaquetteConfigException}<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class MaquetteConfigExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurEtGetterSetterTest()
   {
      new MaquetteConfigException();
      new MaquetteConfigException("un_message");
      new MaquetteConfigException(new Exception());
      new MaquetteConfigException("un_message",new Exception());
   }
   
   
}
