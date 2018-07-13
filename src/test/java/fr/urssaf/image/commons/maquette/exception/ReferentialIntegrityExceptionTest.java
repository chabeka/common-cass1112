package fr.urssaf.image.commons.maquette.exception;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link ReferentialIntegrityException}<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class ReferentialIntegrityExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurTest()
   {
      new ReferentialIntegrityException();
      new ReferentialIntegrityException("un_message");
      new ReferentialIntegrityException(new Exception());
      new ReferentialIntegrityException("un_message",new Exception());
   }
   
   
}
