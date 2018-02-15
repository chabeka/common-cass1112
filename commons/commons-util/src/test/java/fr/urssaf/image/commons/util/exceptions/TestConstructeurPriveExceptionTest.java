package fr.urssaf.image.commons.util.exceptions;

import org.junit.Test;

/**
 * Tests unitaires de la classe {@link TestConstructeurPriveException} <br>
 * Principalement pour le code coverage 
 *
 */
@SuppressWarnings("PMD")
public class TestConstructeurPriveExceptionTest {

   @Test
   public void constructeurs()
   {
      new TestConstructeurPriveException();
      new TestConstructeurPriveException("un_message");
      new TestConstructeurPriveException(new Exception());
      new TestConstructeurPriveException("un_message",new Exception());
   }
   
}
