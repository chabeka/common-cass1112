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
   private boolean called = false;
   
   /**
    * True si le handler suivant doit être appelé, False sinon
    */
   private boolean callNext = true;
   
   @Override
   public final <T extends Exception> void handleException(T exception) throws T {
      called = true;
      
      if (!callNext) {
         stop();
      }
   }

   /**
    * @return the callNext
    */
   public final boolean isCallNext() {
      return callNext;
   }

   /**
    * @param callNext the callNext to set
    */
   public final void setCallNext(boolean callNext) {
      this.callNext = callNext;
   }

   /**
    * @return the called
    */
   public final boolean isCalled() {
      return called;
   }

   /**
    * @param called the called to set
    */
   public final void setCalled(boolean called) {
      this.called = called;
   }
}
