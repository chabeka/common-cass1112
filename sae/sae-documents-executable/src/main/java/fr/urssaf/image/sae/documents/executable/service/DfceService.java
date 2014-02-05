package fr.urssaf.image.sae.documents.executable.service;

import java.io.InputStream;
import java.util.Iterator;

import net.docubase.toolkit.model.document.Document;

/**
 * Service permettant de réaliser des opérations sur DFCE.
 */
public interface DfceService {

   /**
    * Réalise l'ouverture de la connexion à DFCE.
    */
   void ouvrirConnexion();

   /**
    * Réalise la fermeture de la connexion à DFCE.
    */
   void fermerConnexion();

   /**
    * Retourne des documents concernés par la requête.
    * 
    * @param requeteLucene
    *           La requête à exécuter
    * @return Liste des documents correspondants à la recherche
    */
   Iterator<Document> executerRequete(String requeteLucene);

   /**
    * Retourne le contenu du document.
    * 
    * @param document
    *           Le document
    * @return Le contenu du document
    */
   InputStream recupererContenu(Document document);
}
