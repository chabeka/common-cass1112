/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.util.UUID;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.DfceException;

import net.docubase.toolkit.model.document.Document;

/**
 * Interface offrant les services sur les documents
 * 
 */
public interface DocumentService {

   /**
    * Recherche et retourne le document avec l'UUID donné
    * 
    * @param uuid
    *           uuid du document à chercher
    * @return le documment
    */
   Document getDocument(UUID uuid);

   /**
    * mise à jour du document dans DFCE
    * 
    * @param document
    *           document
    * @throws DfceException
    *            exception levée si la mise à jour du document rencontre un
    *            problème
    */
   void updateDocument(Document document) throws DfceException;
}
