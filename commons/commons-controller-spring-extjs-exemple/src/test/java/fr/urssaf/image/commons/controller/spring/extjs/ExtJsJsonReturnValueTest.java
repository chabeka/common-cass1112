package fr.urssaf.image.commons.controller.spring.extjs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;


/**
 * Tests unitaires de la classe ExtJsJsonReturnValue
 *
 */
public class ExtJsJsonReturnValueTest {

   
   /**
    * Test du constructeur
    */
   @Test
   public void constructorTest()
   {
      ExtJsJsonReturnValue returnValue = new ExtJsJsonReturnValue();
      
      String message = "constructorTest";
      
      assertFalse(message,returnValue.getSuccess());
      assertNull(message,returnValue.getMessage());
      assertNotNull(message,returnValue.getErrors());
      
   }
   
   
   /**
    * Test de la méthode ajouteErreur
    */
   @Test
   public void ajouteErreurTest()
   {
      
      ExtJsJsonReturnValue returnValue = new ExtJsJsonReturnValue();
      returnValue.ajouteErreur("champ1", "message d'erreur champ 1");
      
      String message = "ajouteErreurTest";
      
      assertNotNull(message,returnValue.getErrors());
      assertFalse(message,returnValue.getErrors().isEmpty());
      assertEquals(message,(int)1,returnValue.getErrors().size());
      
   }
   
   
   
   
}
