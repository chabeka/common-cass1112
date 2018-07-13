package fr.urssaf.image.commons.maquette.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Tests unitaires de la classe {@link MissingInfoBoxPropertyException}<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class MissingInfoBoxPropertyExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurEtGetterSetterTest()
   {
      new MissingInfoBoxPropertyException();
      new MissingInfoBoxPropertyException("un_message");
      new MissingInfoBoxPropertyException(new Exception());
      new MissingInfoBoxPropertyException("un_message",new Exception());
      MissingInfoBoxPropertyException ex = new MissingInfoBoxPropertyException("un_message","une_property");
      assertEquals("","une_property",ex.getMissingProperty());
   }
   
   
}
