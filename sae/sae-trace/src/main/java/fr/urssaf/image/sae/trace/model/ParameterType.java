/**
 * 
 */
package fr.urssaf.image.sae.trace.model;

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
   PURGE_EXPLOIT_IS_RUNNING

}
