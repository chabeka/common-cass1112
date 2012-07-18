package fr.urssaf.image.sae.regionalisation.dao;

import java.util.List;

import net.docubase.toolkit.model.document.Document;

/**
 * Service contenant les opérations concernant les documents SAE
 * 
 * 
 */
public interface SaeDocumentDao {

   /**
    * Récupération de la liste des documents correspondants aux critères passés
    * en paramètres.
    * 
    * @param lucene
    *           requête lucène
    * @return liste des documents correspondants aux critères de recherche
    *         passés en paramètre
    */
   List<Document> getDocuments(String lucene);

   /**
    * Mise à jour via DFCE du document passé en paramètre.
    * 
    * @param document
    *           Document à mettre à jour
    */
   void update(Document document);

}
