package fr.urssaf.image.sae.batch.documents.executable.service;

import java.util.UUID;

import fr.urssaf.image.sae.batch.documents.executable.model.DeleteDocsParametres;
import fr.urssaf.image.sae.batch.documents.executable.model.ExportDocsParametres;
import fr.urssaf.image.sae.batch.documents.executable.model.ImportDocsParametres;

/**
 * Service centralisant les traitements sur les documents.
 */
public interface TraitementService {

   /**
    * Réalise la suppression des documents
    * 
    * @param parametres
    *           Les paramètres concernant le traitement
    */
   public void deleteDocuments(DeleteDocsParametres parameters);

   /**
    * Réalise la copie des documents
    * 
    * @param parametres
    *           Les paramètres concernant le traitement
    */
   public void exportDocuments(ExportDocsParametres parameters);

   /**
    * Réalise la copie des documents
    * 
    * @param parametres
    *           Les paramètres concernant le traitement
    */
   public void importDocuments(ImportDocsParametres parameters);

   /**
    * Gel un document
    * 
    * @param docUUID
    *           L'uuid du document à geler
    */
   public void gelDocument(UUID docUUID);

   /**
    * Dé-gel un document
    * 
    * @param docUUID
    *           L'uuid du document à dé-geler
    */
   public void degelDocument(UUID docUUID);

}
