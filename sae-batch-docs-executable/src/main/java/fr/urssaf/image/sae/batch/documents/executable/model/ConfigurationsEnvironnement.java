package fr.urssaf.image.sae.batch.documents.executable.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant l'ensemble des configurations environnement (CASSANDRA et
 * services Web)
 * 
 * 
 */
public class ConfigurationsEnvironnement {

   /**
    * Tableau de toutes les configurations environnement (CASSANDRA et services
    * web)
    */
   private ConfigurationEnvironnement[] configurations;

   /**
    * @return la liste des configurations environnement
    */
   public final ConfigurationEnvironnement[] getConfigurations() {
      return (ConfigurationEnvironnement[]) this.configurations.clone();
   }

   /**
    * 
    * @param configurations
    *           la liste des configurations environnement
    */
   public final void setConfigurations(
         ConfigurationEnvironnement[] configurations) {
      ConfigurationEnvironnement[] conf = (ConfigurationEnvironnement[]) configurations
            .clone();
      this.configurations = conf;
   }

   /**
    * Vérifie l'existente d'une configuration à partir de son nom
    * 
    * @param nomConfiguration
    *           le nom de la configuration choisie
    * @return true si la configuration existe
    */
   public final boolean existe(String nomConfiguration) {
      for (ConfigurationEnvironnement conf : configurations) {
         if (conf.getNom().equals(nomConfiguration)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Retourne la configuration demandée en fonction de son nom
    * 
    * @param nomConfiguration
    *           le nom de la configuration
    * @return null si la configuration n'exite pas, sinon retourne la
    *         configuration
    */
   public final ConfigurationEnvironnement getConfiguration(
         String nomConfiguration) {
      for (ConfigurationEnvironnement conf : configurations) {
         if (conf.getNom().equals(nomConfiguration)) {
            return conf;
         }
      }
      return null;
   }

   /**
    * @return la liste des noms des configurations
    */
   public final List<String> getListeNoms() {
      List<String> listNomConf = new ArrayList<String>();
      for (ConfigurationEnvironnement configurationEnvironnement : configurations) {
         listNomConf.add(configurationEnvironnement.getNom());
      }
      return listNomConf;
   }

}
