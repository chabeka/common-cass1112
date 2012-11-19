/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao;

import java.util.UUID;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.DfceException;

import net.docubase.toolkit.model.document.Document;

/**
 * Interface founissant les opérations concernant les documents
 * 
 */
public interface DocumentDao {

   /**
    * Recherche et retourne le document défini par luuid passé en paramètre
    * 
    * @param uuid
    *           identifiant unique du document
    * @return un document DFCE
    */
   Document getDocument(UUID uuid);

   /**
    * Met à jour le document
    * 
    * @param document
    *           document à mettre à jour
    * @throws DfceException
    *            erreur soulevée lors de la mise à jour du document sous DFCE
    */
   void updateDocument(Document document) throws DfceException;
}
