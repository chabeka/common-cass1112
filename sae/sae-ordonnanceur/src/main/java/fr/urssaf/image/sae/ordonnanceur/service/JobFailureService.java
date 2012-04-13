package fr.urssaf.image.sae.ordonnanceur.service;

import java.util.Set;
import java.util.UUID;

/**
 * Service des traitements de masse contenus dans la pile des travaux causant un
 * problème.<br>
 * <br>
 * Les problèmes peuvent venir par exemple lors de la réservation ou du
 * lancement d'un traitement.<br>
 * <br>
 * Les raisons peuvent être multiples, arrêt d'un serveur, intégrité des données
 * non respectée...
 * 
 * 
 */
public interface JobFailureService {
   
   /**
    * Paramètre indiquant le nombre maximum d'anomalies toléré pour un
    * traitement de la pile des travaux<br>
    * <br>
    * La valeur est {@value #MAX_ANOMALIE}
    */
   int MAX_ANOMALIE = 3;

   /**
    * Les traitements posant à un moment donné un problème sont persistés.<br>
    * A chaque nouveau un index indique le nombre de fois que le traitement a
    * soulevé une anomalie.
    * 
    * @param idJob
    *           identifiant du traitement ayant posé problème dans
    *           l'ordonnanceur
    * @param echec
    *           cause du problème
    */
   void ajouterEchec(UUID idJob, Throwable echec);

   /**
    * Retourne une liste des traitements ayant levée une anomalie.<br>
    * Les traitements doivent avoir au moins un certain nombre d'anomalies.<br>
    * 
    * @return liste des identifiants de traitements ayant levée une anomalie
    */
   Set<UUID> findJobEchec();
}
