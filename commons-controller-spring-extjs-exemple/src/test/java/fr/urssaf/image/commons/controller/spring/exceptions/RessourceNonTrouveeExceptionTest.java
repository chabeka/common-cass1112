package fr.urssaf.image.commons.controller.spring.exceptions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Tests unitaires de la classe RessourceNonTrouveeException<br>
 * Principalement pour le code coverage
 *
 */
@SuppressWarnings("PMD")
public class RessourceNonTrouveeExceptionTest {

   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurEtGetterSetterTest()
   {
      new RessourceNonTrouveeException();
      new RessourceNonTrouveeException("un_message");
      new RessourceNonTrouveeException(new Exception());
      new RessourceNonTrouveeException("un_message",new Exception());
      
      RessourceNonTrouveeException exception = new RessourceNonTrouveeException("un_message","nom_ressource");
      assertEquals("Problème dans le getter de nomRessource","nom_ressource",exception.getNomRessource());
      
      exception.setNomRessource("nom_ressource_2");
      assertEquals("Problème dans le setter de nomRessource","nom_ressource_2",exception.getNomRessource());
      
   }
   
   
}
