package fr.urssaf.image.sae.documents.executable.multithreading;

import java.util.Map;

import net.docubase.toolkit.model.document.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.documents.executable.service.DfceService;

/**
 * Thread d’ajout de métadonnées à un document
 */
public class AddMetadatasRunnable implements Runnable {
   
   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(AddMetadatasRunnable.class);
   
   /**
    * Informations du document.
    */
   private Document document;

   /**
    * Liste des métadonnées à ajouter au document
    */
   private Map<String, String> metadonnees;

   /**
    * Service d'accès couche DFCE
    */
   private DfceService dfceService;

   public Map<String, String> getMetadonnees() {
      return metadonnees;
   }

   public void setMetadonnees(Map<String, String> metadonnees) {
      this.metadonnees = metadonnees;
   }

   /**
    * Constructeur de la classe : Initialise le document et la liste des
    * métadonnées à ajouter
    * 
    * @param doc
    * @param metas
    */
   public AddMetadatasRunnable(DfceService dfceService, Document doc,
         Map<String, String> metas) {
      super();
      this.setDocument(doc);
      this.setMetadonnees(metas);
      this.setDfceService(dfceService);
   }

   private void setDfceService(DfceService dfceService) {
      this.dfceService = dfceService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void run() {
      for (Map.Entry<String, String> meta : metadonnees.entrySet()) {
         document.addCriterion(meta.getKey(), meta.getValue());
      }
      
      //-- Mise à jour du document en base
      try {
         dfceService.getServiceProvider().getStoreService()
            .updateDocument(document);
      } 
      catch (TagControlException e) {
         String error = e.getMessage();
         LOGGER.warn("Erreur lors de la mise à jour du document {}:", 
               document.getUuid() + "(" + error + ")" );
      } 
      catch (FrozenDocumentException e) {
         String error = e.getMessage();
         LOGGER.warn("Erreur lors de la mise à jour du document {}:", 
               document.getUuid() + "(" + error + ")" );
      }
   }

   /**
    * Permet de récupérer l'objet Document.
    * 
    * @return Document
    */
   public final Document getDocument() {
      return document;
   }

   /**
    * Permet de modifier l'objet Document.
    * 
    * @param document
    *           document DFCE
    */
   public final void setDocument(final Document document) {
      this.document = document;
   }
}
