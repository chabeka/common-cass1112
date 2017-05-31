package fr.urssaf.image.sae.commons.utils;

/**
 * Classe contenant les constantes utilisées dans les différents projets
 * 
 *
 */
public final class Constantes {

   /**
    * Le code du traitement
    */
   public static final String CODE_TRAITEMENT = "codeTraitement";
   
   /**
    * Prefixe pour la clef cassandra.
    */
   public static final String PREFIXE_SEMAPHORE_JOB = "semaphore_";

   /**
    * Prefixe pour la clef zookeeper.
    */
   public static final String PREFIXE_SEMAPHORE = "/Semaphore/";

   /**
    * Types de traitements
    */
   public static enum TYPES_JOB {
      capture_masse, suppression_masse, restore_masse, modification_masse, transfert_masse, reprise_masse;
   }

   /**
    * Nom du job d'un traitement de reprise en masse
    */
   public static final String REPRISE_MASSE_JN = "reprise_masse";
   
}
