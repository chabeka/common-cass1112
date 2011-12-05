package fr.urssaf.image.sae.services.dispatchers.handlers;

import fr.urssaf.image.sae.services.dispatchers.AbstractExceptionHandler;

/**
 * Handler espion pour les tests unitaires. 
 * <ul>
 * <li><c>called</c> indique si le handler à été appelé</li>
 * <li><c>callNext</c> spécifie si le handler suivant doit être appelé</li>
 * </ul>
 */
public class SpyHandler extends AbstractExceptionHandler {
   /**
    * Indique si le handler à été appelé
    */
   public boolean called = false;
   
   /**
    * True si le handler suivant doit être appelé, False sinon
    */
   public boolean callNext = true;
   
   @Override
   public <T extends Exception> void handleException(T exception) throws T {
      called = true;
      
      if (!callNext) {
         stop();
      }
   }
}
