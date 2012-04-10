package fr.urssaf.image.sae.services.dispatchers;

/**
 * Représente un maillon de la chaine de responsabilité utilisée par le
 * dispatcher d'exceptions.
 * 
 * <p>
 * Les classes filles - i.e. les handlers concrets - doivent implémenter
 * {@link AbstractExceptionHandler#handleException handleException()} pour traiter
 * l'exception. Si un handler considère qu'il doit être le dernier dans la
 * chaine, il doit invoquer {@link AbstractExceptionHandler#stop stop()} dans cette
 * méthode.
 * </p>
 * 
 * <p>
 * Si un handler veut modifier le comportement globale de la chaine, il doit
 * surchager la méthode {@link AbstractExceptionHandler#handle handle()}.
 * </p>
 * 
 * <blockquote> 
 * NOTE : Le cas d'utilisation classique est d'implémenter
 * {@link AbstractExceptionHandler#handleException handleException()} sans surcharger
 * {@link AbstractExceptionHandler#handle handle()}. 
 * </blockquote>
 */
public abstract class AbstractExceptionHandler {

   /**
    * Maillon suivant dans la chaine.
    */
   private AbstractExceptionHandler successor;

   /**
    * Flag indiquant s'il faut arrêter la chaine.
    * <ul>
    * <li>False (défaut) : le successeur est appelé.</li>
    * <li>True : le handler courant sera le dernier appelé dans le chaine. Pour
    * cela, les implémentations concrètes doivent invoquer la méthode
    * {@link AbstractExceptionHandler#stop stop()}.</li>
    * </ul>
    */
   private boolean breakChain;

   /**
    * Réalise un traitement à partir de l'exception et la fournit au successeur.
    * Si le handler courant décide de stopper la chaine via la méthode
    * {@link AbstractExceptionHandler#stop stop()}, le successeur ne sera pas invoqué.
    * <p>
    * Si un handler veut modifier le comportement globale de la chaine, il doit
    * surchager cette méthode. 
    * </p>
    * 
    * <blockquote> 
    * NOTE : Le cas d'utilisation
    * classique est d'implémenter {@link AbstractExceptionHandler#handleException
    * handleException()} sans surcharger {@link AbstractExceptionHandler#handle
    * handle()}. 
    * </blockquote>
    * 
    * @param exception
    */
   public <T extends Exception> void handle(T exception) throws T {
      handleException(exception);

      if (successor != null && !breakChain) {
         successor.handle(exception);
      }
   }

   /**
    * Stoppe la chaine de responsabilité.
    * 
    * Si un handler considère qu'il doit être le dernier dans la chaine, il doit
    * appeler cette méthode.
    */
   public void stop() {
      breakChain = true;
   }

   /**
    * Traitement à réaliser par les classes filles. Une fois le traitement
    * terminé, le successeur est automatiquement appelé sauf si la méthode
    * {@link AbstractExceptionHandler#stop stop()} est invoquée.
    * 
    * @param exception
    */
   public abstract <T extends Exception> void handleException(T exception) throws T;

   /**
    * @return AbstractExceptionHandler Handler suivant dans la chaine de responsabilité
    */
   public AbstractExceptionHandler getSuccessor() {
      return this.successor;
   }

   /**
    * @param successor Handler suivant dans la chaine de responsabilité
    * @return AbstractExceptionHandler abstractExceptionHandler
    */
   public AbstractExceptionHandler setSuccessor(AbstractExceptionHandler successor) {
      this.successor = successor;
      return successor;
   }
}