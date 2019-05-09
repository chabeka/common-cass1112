package fr.urssaf.image.sae.documents.executable.multithreading;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.documents.executable.service.DfceService;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;

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
   public AddMetadatasRunnable(final DfceService dfceService, final Document doc,
                               final Map<String, String> metas) {
      super();
      this.setDocument(doc);
      this.setMetadonnees(metas);
      this.setDfceService(dfceService);
   }

   private void setDfceService(final DfceService dfceService) {
      this.dfceService = dfceService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void run() {
      for (final Map.Entry<String, String> meta : metadonnees.entrySet()) {
         if (document.getCriterions(meta.getKey()).isEmpty() && meta.getValue() != null) {
            // ajout d'une nouvelle valeur de metadonnee
            document.addCriterion(meta.getKey(), meta.getValue());
         } else if (!document.getCriterions(meta.getKey()).isEmpty() && meta.getValue() == null) {
            // gere la suppression d'un valeur de metadonnee
            for (final Criterion criterion : document.getCriterions(meta.getKey())) {
               document.deleteCriterion(criterion);
            }
         }
      }

      // -- Mise à jour du document en base
      try {
         dfceService.getDFCEServices().updateDocument(
                                                      document);
      } catch (final TagControlException e) {
         final String error = e.getMessage();
         LOGGER.warn("Erreur lors de la mise à jour du document {}:", document
                     .getUuid()
                     + "(" + error + ")");
      } catch (final FrozenDocumentException e) {
         final String error = e.getMessage();
         LOGGER.warn("Erreur lors de la mise à jour du document {}:", document
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

   /**
    * @return the metadonnees
    */
   public final Map<String, String> getMetadonnees() {
      return metadonnees;
   }

   /**
    * @param metadonnees
    *           the metadonnees to set
    */
   public final void setMetadonnees(final Map<String, String> metadonnees) {
      this.metadonnees = metadonnees;
   }

}
