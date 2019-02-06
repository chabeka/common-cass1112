package fr.urssaf.image.sae.documents.executable.service;

import java.io.InputStream;
import java.util.Iterator;
import java.util.UUID;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
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
    * @throws SearchQueryParseException
    */
   Iterator<Document> executerRequete(String requeteLucene)
         throws SearchQueryParseException;


   /**
    * Retourne des documents de la corbeille concernés par la requête.
    *
    * @param requeteLucene
    *           La requête à exécuter
    * @return Liste des documents correspondants à la recherche
    * @throws SearchQueryParseException
    */
   Iterator<Document> executerRequeteCorbeille(String requeteLucene)
         throws SearchQueryParseException;

   /**
    * Retourne le contenu du document.
    *
    * @param document
    *           Le document
    * @return Le contenu du document
    */
   InputStream recupererContenu(Document document);

   /**
    * Permet de récupérer la connexion vers les services de DFCE.
    *
    * @return DFCEServices
    */
   DFCEServices getDFCEServices();

   /**
    * Recupère un document par son identifiant.
    * @param idDoc identifiant du doc.
    * @return Document
    */
   Document getDocumentById(UUID idDoc);
}
