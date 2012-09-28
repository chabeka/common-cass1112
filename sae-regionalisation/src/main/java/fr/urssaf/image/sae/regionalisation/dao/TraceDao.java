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
    * Ajout d'une trace permettant de connaître le nombre de documents rattachés
    * à un critère de recherche
    * 
    * @param requeteLucene
    *           requete lucène qui entre en jeu dans la recherche
    * @param lineNumber
    *           numéro de ligne de l'enregistrement en cours de traitement
    * @param documentCount
    *           nombre de documents associés au critère de recherche
    * @param maj
    *           <code>true</code> si le mode est MISE_A_JOUR, <code>false</code>
    *           si le mode est TIR_A_BLANC
    */
   void addTraceRec(String requeteLucene, int lineNumber, int documentCount,
         boolean maj);

   /**
    * @return le fichier où les données sont insérées
    */
   File getFile();
}
