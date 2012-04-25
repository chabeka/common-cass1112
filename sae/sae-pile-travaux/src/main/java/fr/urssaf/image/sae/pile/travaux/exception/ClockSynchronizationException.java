package fr.urssaf.image.sae.pile.travaux.exception;

import java.text.MessageFormat;

/**
 * Exception levée lors d'un problème de synchronisation
 * 
 * 
 */
public class ClockSynchronizationException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private static final String EXCEPTION_MESSAGE = "Les horloges des serveurs sont désynchronisées. Le timestamp actuel est de {0} alors que celui de la colonne est : {1}";

   private final long columnClock;

   private final long actualClock;

   /**
    * 
    * @param columnClock
    *           horloge de la colonne
    * @param actualClock
    *           horloge du serveur
    */
   public ClockSynchronizationException(long columnClock, long actualClock) {

      super();

      this.columnClock = columnClock;
      this.actualClock = actualClock;

   }

   /**
    * 
    * @return horloge du serveur
    */
   public final long getActualClock() {
      return this.actualClock;
   }

   /**
    * 
    * @return horloge de la colonne
    */
   public final long getColumnClock() {
      return this.columnClock;
   }

   /**
    * 
    * {@inheritDoc} <br>
    * <br>
    * Le message est formaté sur le modèle {@value #EXCEPTION_MESSAGE}
    * <ul>
    * <li>{0} : <code>timestamp actuel de l'horloge du serveur</code></li>
    * <li>{1} : <code>timestamp de l'horloge de la colonne</code></li>
    * </ul>
    * 
    * 
    */
   @Override
   public final String getMessage() {

      String message = MessageFormat.format(EXCEPTION_MESSAGE,
            this.actualClock, this.columnClock);

      return message;
   }

}
