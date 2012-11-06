package fr.urssaf.image.sae.services.dispatchers;

/**
 * Dispatcher d'exceptions.
 * 
 * Le dispatcher dispose d'une chaine de responsabilité qui traitera chaque
 * exception selon l'implémentation et le rang de chaque handler.
 * 
 * <p>
 * Exemple de handlers concrets participant à la chaine de responsabilité :
 * {@link fr.urssaf.image.sae.services.dispatchers.handlers.LoggerExceptionHandler}
 * {@link fr.urssaf.image.sae.services.dispatchers.handlers.ThrowerExceptionHandler}
 * </p>
 */
public class ExceptionDispatcher {

   /**
    * @return Premier handler de la chaine
    */
   private AbstractExceptionHandler handler;

   /**
    * Dispatche l'exception au travers d'une chaine de responsabilité
    * 
    * @param exception
    *           Exception à dispatcher, elle sera traitée par les handlers
    *           concrets.
    * @throws Exception
    */
   public <T extends Exception> void dispatch(T exception) throws T {
      if (handler != null) {
         handler.handle(exception);
      }
   }

   /**
    * @return Premier handler de la chaine
    */
   public AbstractExceptionHandler getHandler() {
      return this.handler;
   }

   /**
    * Permet d'injecter le premier handler de la chaine de responsabilité.
    * 
    * @param handler
    *           Premier handler de la chaine
    */
   public void setHandler(AbstractExceptionHandler handler) {
      this.handler = handler;
   }

}