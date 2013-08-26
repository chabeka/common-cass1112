package fr.urssaf.image.sae.regionalisation.dao;

import java.io.File;

import fr.urssaf.image.sae.regionalisation.bean.Trace;

/**
 * Service contenant les opérations concernant les traces d'exécution.
 * 
 * 
 */
public interface TraceDao {

   /**
    * Ouvre le flux
    * 
    * @param uuid
    *           identifiant unique du processus
    */
   void open(String uuid);

   /**
    * Ferme le flux
    */
   void close();

   /**
    * Ajout d'une trace de modification de métadonnée.
    * 
    * @param trace
    *           informations de trace
    */
   void addTraceMaj(Trace trace);

   /**
    * @return le fichier où les données sont insérées
    */
   File getFile();
}
