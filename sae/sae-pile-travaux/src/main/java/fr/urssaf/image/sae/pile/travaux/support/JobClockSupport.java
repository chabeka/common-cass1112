package fr.urssaf.image.sae.pile.travaux.support;

import me.prettyprint.hector.api.beans.HColumn;

public interface JobClockSupport {

   /**
    * Temps maximum de décalage d'horloge qu'il nous parait acceptable, en
    * micro-secondes
    */
   int MAX_TIME_SYNCHRO_ERROR = 10 * 1000 * 1000;
   /**
    * Temps maximum de décalage d'horloge, en micro-secondes. Au delà, on logue
    * une warning.
    */
   int MAX_TIME_SYNCHRO_WARN = 2 * 1000 * 1000;

   /**
    * 
    * @return timestamp courant en microsecondes
    */
   long currentCLock();

   /**
    * 
    * Vérifie que l'horloge d'une colonne n'est pas postérieure à l'horloge du
    * actuel du serveur.<br>
    * <br>
    * L'horloge actuel est calculé à partir du Keyspace.<br>
    * Si la différence n'est pas trop importante alors c'est à dire inférieure à
    * {@value #MAX_TIME_SYNCHRO_WARN} alors on renvoie le nouveau timestamp de
    * la mise à jour de la colonne.<br>
    * Si par contre la différence est supérieure à
    * {@value #MAX_TIME_SYNCHRO_ERROR} alors une exception est levée
    * 
    * @param column
    *           colonne dont on va extraire l'horloge de la dernière mise à jour
    * @return timestamp courant éventuellement décalé en microsecondes
    */
   long currentCLock(HColumn<?, ?> column);

}
