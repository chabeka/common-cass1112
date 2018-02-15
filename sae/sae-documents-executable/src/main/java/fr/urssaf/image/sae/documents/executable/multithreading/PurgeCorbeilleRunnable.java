package fr.urssaf.image.sae.documents.executable.multithreading;

import net.docubase.toolkit.model.document.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.FrozenDocumentException;

import fr.urssaf.image.sae.documents.executable.service.DfceService;
import fr.urssaf.image.sae.documents.executable.support.TracesDfceSupport;

/**
 * Thread de purge d'un document dans la corbeille
 */
public class PurgeCorbeilleRunnable implements Runnable {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(PurgeCorbeilleRunnable.class);

   /**
    * Informations du document.
    */
   private Document document;

   /**
    * Service d'accès couche DFCE
    */
   private DfceService dfceService;

   /**
    * Support pour l'écriture des traces
    */
   private TracesDfceSupport tracesSupport;
   
   /**
    * Constructeur de la classe : Initialise le document et la liste des
    * métadonnées à ajouter
    * 
    * @param dfceService
    *           services DFCE
    * @param doc
    *           document
    * @param metas
    *           métadonnées
    */
   public PurgeCorbeilleRunnable(DfceService dfceService, TracesDfceSupport tracesSupport, Document doc) {
      super();
      this.setDocument(doc);
      this.setDfceService(dfceService);
      this.setTracesDfceSupport(tracesSupport);
   }

   private void setDfceService(DfceService dfceService) {
      this.dfceService = dfceService;
   }

   private void setTracesDfceSupport(TracesDfceSupport tracesSupport) {
      this.tracesSupport = tracesSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void run() {
      // Suppression du document
      try {
         dfceService.getServiceProvider().getRecycleBinService().deleteDocument(document.getUuid());
      
         // -- Trace l'événement "Suppression d'un document de DFCE"
         tracesSupport.traceSuppressionDocumentDeDFCE(document.getUuid());
      } catch (FrozenDocumentException e) {
         String error = e.getMessage();
         LOGGER.warn("Erreur lors de la suppression du document {}:", document
               .getUuid()
               + "(" + error + ")");
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
