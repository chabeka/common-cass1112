package fr.urssaf.image.sae.batch.documents.executable.service;

import java.io.InputStream;
import java.util.Iterator;

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
    * Retourne l'object connection DFCE
    *
    * @return {@link DFCEServices}
    */
   DFCEServices getDfceServices();

   /**
    * Retourne le contenu du document.
    *
    * @param document
    *           Le document
    * @return Le contenu du document
    */
   InputStream recupererContenu(Document document);

}
