package fr.urssaf.image.sae.commons.bo;

/**
 * Valeurs de paramètre de configuration
 * 
 */
public enum ParameterType {

   /**
    * Nom du paramètre de durée de rétention de la purge des registres
    * techniques
    */
   PURGE_TECH_DUREE,

   /**
    * Nom du paramètre de durée de rétention de la purge des registres de
    * sécurité
    */
   PURGE_SECU_DUREE,

   /**
    * Nom du paramètre de durée de rétention de la purge des registres
    * d'exploitation
    */
   PURGE_EXPLOIT_DUREE,

   /**
    * Nom du paramètre de la dernière date minimale utilisée par la purge des
    * registres technique
    */
   PURGE_TECH_DATE,

   /**
    * Nom du paramètre de la dernière date minimale utilisée par la purge des
    * registres de sécurité
    */
   PURGE_SECU_DATE,

   /**
    * Nom du paramètre de la dernière date minimale utilisée par la purge des
    * registres d'exploitation
    */
   PURGE_EXPLOIT_DATE,

   /**
    * Nom du paramètre indiquant si une purge des registres techniques est déjà
    * en cours
    */
   PURGE_TECH_IS_RUNNING,

   /**
    * Nom du paramètre indiquant si une purge des registres de sécurité est déjà
    * en cours
    */
   PURGE_SECU_IS_RUNNING,

   /**
    * Nom du paramètre indiquant si une purge des registres d'exploitation est
    * déjà en cours
    */
   PURGE_EXPLOIT_IS_RUNNING,

   /**
    * Nom du paramètre de la dernière date minimale utilisée par la
    * journalisation des événements du SAE
    */
   JOURNALISATION_EVT_DATE,

   /**
    * Nom du paramètre indiquant si une purge des traces des événements du SAE
    * est déjà en cours
    */
   JOURNALISATION_EVT_IS_RUNNING,

   /**
    * Nom du paramètre de rétention de la purge des événements du SAE
    */
   PURGE_EVT_DUREE,

   /**
    * Nom du paramètre de la dernière date minimale utilisée par la purge des
    * événements du SAE
    */
   PURGE_EVT_DATE,

   /**
    * Nom du paramètre indiquant si une purge des traces des événements SAE est
    * déjà en cours
    */
   PURGE_EVT_IS_RUNNING,

   /**
    * Identifiant du journal précédent dans le cadre de la journalisation des
    * événements SAE
    */
   JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT,

   /**
    * Hash du journal précédent dans le cadre de la journalisation des
    * événements SAE
    */
   JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT,

   /**
    * Métadonnée TITRE
    */
   JOURNALISATION_EVT_META_TITRE,

   /**
    * Métadonnée application productrice
    */
   JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,

   /**
    * Métadonnée application de traitement
    */
   JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT,

   /**
    * Métadonnée code organisme (propriétaire et gestionnaire)
    */
   JOURNALISATION_EVT_META_CODE_ORGA, 
   
   /**
    * Métadonnée Code RND
    */
   JOURNALISATION_EVT_META_CODE_RND

}
