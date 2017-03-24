package fr.urssaf.image.commons.birt;

import fr.urssaf.image.commons.birt.exception.EnvVarNotSettedBirtEngineException;

/**
 * Fonctions utilitaires pour BIRT<br>
 * <br>
 * <b><u>Pour l'utilisation des classes de BIRT, se référer à la fiche de développement F025</u></b>
 *
 */
public final class BirtTools {
   
   private BirtTools()
   {}

   /**
    * 
    * Renvoie le contenu de la variable d'environnement <code>BIRT_HOME</code><br>
    * Elle doit contenir le chemin complet du BIRT Report Engine
    * 
    * @return le contenu de la variable d'environnement BIRT_HOME
    * @throws EnvVarNotSettedBirtEngineException si la variable d'environnement BIRT_HOME
    *         n'est pas définie ou si elle est vide
    */
   public static String getBirtHomeFromEnvVar() throws EnvVarNotSettedBirtEngineException
   {
      
      // Lecture de la variable d'environnement
      String reportEngineHome = System.getenv(getBirtHomeVarName()) ;
      
      // Vérifie que la variable d'environnement est définie
      if ((reportEngineHome == null) || (reportEngineHome.isEmpty()))
      {
         throw new EnvVarNotSettedBirtEngineException() ;
      }
      
      // Renvoie la valeur de la variable d'environnement
      return reportEngineHome;
   }
   
   
   /**
    * 
    * Renvoie le nom de la variable d'environnement contenant le chemin
    * complet du BIRT Report Engine
    * 
    * @return le nom de la variable d'environnement contenant le chemin
    * complet du BIRT Report Engine
    */
   public static String getBirtHomeVarName()
   {
      return "BIRT_HOME" ;
   }
}
