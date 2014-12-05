package fr.urssaf.image.sae.extraitdonnees.service;

import java.io.File;

import fr.urssaf.image.sae.extraitdonnees.bean.CassandraConfig;

/**
 * Service d'extraction des données
 */
public interface ExtraitDonneesService {

   /**
    * Extraction dans un fichier plat d'une liste d'UUID de documents virtuels
    * ou non
    * 
    * @param fichierSortie
    *           le fichier de sortie
    * @param nbDocsSouhaites
    *           le nombre de documents souhaités dans le fichier (nombre d'UUID)
    * @param isVirtuel
    *           true s'il faut sortir des documents virtuels, false pour les
    *           documents normaux
    * @param cassandraConfig
    *           la configuration pour se connecter à Cassandra
    */
   void extraitUuid(File fichierSortie, int nbDocsSouhaites, boolean isVirtuel,
         CassandraConfig cassandraConfig);

}
