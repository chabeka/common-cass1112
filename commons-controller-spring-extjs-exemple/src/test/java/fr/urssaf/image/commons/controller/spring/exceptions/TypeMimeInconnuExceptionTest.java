package fr.urssaf.image.commons.controller.spring.exceptions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Tests unitaires de la classe TypeMimeInconnuException<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class TypeMimeInconnuExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurEtGetterSetterTest()
   {
      new TypeMimeInconnuException();
      new TypeMimeInconnuException("un_message");
      new TypeMimeInconnuException(new Exception());
      new TypeMimeInconnuException("un_message",new Exception());
      
      TypeMimeInconnuException exception = new TypeMimeInconnuException("un_message","nom_ressource");
      assertEquals("Problème dans le getter de nomRessource","nom_ressource",exception.getNomRessource());
      
      exception.setNomRessource("nom_ressource_2");
      assertEquals("Problème dans le setter de nomRessource","nom_ressource_2",exception.getNomRessource());
      
   }
   
   
}
