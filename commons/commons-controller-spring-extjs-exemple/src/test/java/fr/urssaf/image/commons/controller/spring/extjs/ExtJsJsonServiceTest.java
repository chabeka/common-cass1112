package fr.urssaf.image.commons.controller.spring.extjs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.validation.FieldError;


/**
 * Tests unitaires de la classe ExtJsJsonService
 *
 */
@SuppressWarnings("PMD.ConsecutiveLiteralAppends")
public class ExtJsJsonServiceTest {
   
   
   /**
    * Test de la méthode returnValueToJson
    * Cas de test : Avec uniquement le booléen success 
    * @throws IOException 
    */
   @Test
   public void returnValueToJsonTest1() throws IOException
   {

      ExtJsJsonReturnValue returnValue = new ExtJsJsonReturnValue();
      returnValue.setSuccess(true);
      
      String actual = ExtJsJsonService.returnValueToJson(returnValue);
      String expected = "{\"success\":true}";
      String message = "Cas de test : Avec uniquement le booléen success";
      
      assertEquals(message,expected,actual);
      
   }
   
   
   /**
    * Test de la méthode returnValueToJson
    * Cas de test : Avec le booléen success et un message 
    * @throws IOException 
    */
   @Test
   public void returnValueToJsonTest2() throws IOException
   {
      
      ExtJsJsonReturnValue returnValue = new ExtJsJsonReturnValue();
      returnValue.setSuccess(true);
      returnValue.setMessage("le message é&à");
      
      String actual = ExtJsJsonService.returnValueToJson(returnValue);
      
      /*
      {
         "success":true,
         "message":"bla"
      }
      */
      StringBuffer buffer = new StringBuffer(70);
      buffer.append('{');
      buffer.append("\"success\":true,");
      buffer.append("\"message\":\"le message &eacute;&amp;&agrave;\"");
      buffer.append('}'); // accolade de fin générale
      String expected = buffer.toString();
     
      String message = "Cas de test : Avec le booléen success et un message";
      
      assertEquals(message,expected,actual);
      
   }
   
   
   /**
    * Test de la méthode returnValueToJson
    * Cas de test : Avec le booléen success, un message et deux erreurs 
    * @throws IOException 
    */
   @Test
   public void returnValueToJsonTest3() throws IOException
   {
      
      ExtJsJsonReturnValue returnValue = new ExtJsJsonReturnValue();
      returnValue.setSuccess(true);
      returnValue.setMessage("le message é&à");
      returnValue.ajouteErreur("champ1", "erreur champ1 éà");
      returnValue.ajouteErreur("champ2", "erreur champ2 éà");
      
      String actual = ExtJsJsonService.returnValueToJson(returnValue);
      
      /*
       {
          "success":true,
          "message":"bla",
          "errors":
          {
             "champ1":"bla",
             "champ2":"bla"
          }
       }
       */
      StringBuffer buffer = new StringBuffer(180);
      buffer.append('{');
      buffer.append("\"success\":true,");
      buffer.append("\"message\":\"le message &eacute;&amp;&agrave;\",");
      buffer.append("\"errors\":{");
      buffer.append("\"champ1\":\"erreur champ1 &eacute;&agrave;\",");
      buffer.append("\"champ2\":\"erreur champ2 &eacute;&agrave;\"");
      buffer.append('}'); // accolad de fin de errors
      buffer.append('}'); // accolade de fin générale
      String expected = buffer.toString() ;
      
      String message = "Cas de test : Avec le booléen success, un message et deux erreurs";
      
      assertEquals(message,expected,actual);
      
   }
   
   
   /**
    * Test de la méthode fieldErrorsToJsonTest
    * Cas de test : aucune erreur dans la liste (liste nulle) 
    * @throws IOException 
    */
   @Test
   public void fieldErrorsToJsonTest1() throws IOException
   {
      
      String actual = ExtJsJsonService.fieldErrorsToJson(null);
      String expected = "{\"success\":false}";
      String message = "Cas de test : aucune erreur dans la liste (liste nulle)";
      
      assertEquals(message,expected,actual);
      
   }
   
   
   /**
    * Test de la méthode fieldErrorsToJsonTest
    * Cas de test : aucune erreur dans la liste (liste vide) 
    * @throws IOException 
    */
   @Test
   public void fieldErrorsToJsonTest2() throws IOException
   {
      
      List<FieldError> errors = new ArrayList<FieldError>();
      String actual = ExtJsJsonService.fieldErrorsToJson(errors);
      
      String expected = "{\"success\":false}";
      
      String message = "Cas de test : aucune erreur dans la liste (liste nulle)";
      
      assertEquals(message,expected,actual);
      
   }
   
   
   /**
    * Test de la méthode fieldErrorsToJsonTest
    * Cas de test : deux erreurs dans la liste 
    * @throws IOException 
    */
   @Test
   public void fieldErrorsToJsonTest3() throws IOException
   {
      
      List<FieldError> errors = new ArrayList<FieldError>();
      errors.add(new FieldError("champ1","champ1","erreur champ1 éà"));
      errors.add(new FieldError("champ2","champ2","erreur champ2 éà"));
      String actual = ExtJsJsonService.fieldErrorsToJson(errors);
      
      StringBuffer buffer = new StringBuffer(130);
      buffer.append('{');
      buffer.append("\"success\":false,");
      buffer.append("\"errors\":{");
      buffer.append("\"champ1\":\"erreur champ1 &eacute;&agrave;\",");
      buffer.append("\"champ2\":\"erreur champ2 &eacute;&agrave;\"");
      buffer.append('}'); // accolad de fin de errors
      buffer.append('}'); // accolade de fin générale
      String expected = buffer.toString();
      
      String message = "Cas de test : deux erreurs dans la liste";
      
      assertEquals(message,expected,actual);
      
   }
   
   
}
