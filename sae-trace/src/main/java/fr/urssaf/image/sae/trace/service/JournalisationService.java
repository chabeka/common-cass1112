/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.List;

import fr.urssaf.image.sae.trace.model.JournalisationType;

/**
 * Service permettant de réaliser la journalisation des traces
 * 
 */
public interface JournalisationService {

   /**
    * Réalise l'exportation des traces dans un fichier XML
    * 
    * @param typeJournalisation
    *           type de journal à exporter
    * @param repertoire
    *           Chemin vers le répertoire qui contient les fichiers d'export
    * @param date
    *           date pour laquelle l'export doit être réalisé
    * @return chemin du fichier créé
    */
   String exporterTraces(JournalisationType typeJournalisation,
         String repertoire, Date date);

   /**
    * Retourne l'ensemble des dates pour lesquelles la journalisation doit être
    * réalisée
    * 
    * @param typeJournalisation
    *           type du journal
    * @return l'ensemble des dates à journaliser
    */
   List<Date> recupererDates(JournalisationType typeJournalisation);

}
