package fr.urssaf.image.sae.batch.documents.executable.multithreading;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.batch.documents.executable.bootstrap.ExecutableMain;
import fr.urssaf.image.sae.batch.documents.executable.service.DfceService;
import fr.urssaf.image.sae.batch.documents.executable.service.impl.TraitementServiceImpl.SommaireLineMapper;
import net.docubase.toolkit.model.document.Document;

/**
 * Runnable d’import d'un document
 */
public class ImportDocsRunnable implements Runnable {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ImportDocsRunnable.class);


   /**
    * Service d'accès couche DFCE
    */
   private final DfceService dfceService;

   /**
    * Le document à stocker
    */
   private final Document document;

   /**
    * Fichier du document à stocker
    */
   private final File docFile;

   /**
    * Filename du fichier
    */
   private final String docFilename;

   /**
    * Extension du fichier
    */
   private final String docExtension;

   /**
    * Accès à l'objet document à importer par la tâche courante.
    * @return
    */
   public Document getDocument() {
      return document;
   }

   /**
    * Constructeur de la classe
    *
    * @param dfceService
    *           services DFCE
    * @param mapper
    *           document
    * @param metas
    *           métadonnées
    */
   public ImportDocsRunnable(final DfceService dfceService, final SommaireLineMapper mapper, final File docFile){
      this.document = mapper.getDocument();
      this.docExtension = mapper.getDocExtension();
      this.docFilename = mapper.getDocFilename();
      this.docFile = docFile;
      this.dfceService = dfceService;
   }

   /**
    * {@inheritDoc}
    */
   public final void run() {

      InputStream docStream;

      try {
         docStream = new FileInputStream(docFile);
      } catch (final FileNotFoundException e) {
         LOGGER.error("Le document {} est introuvable.", document.getUuid());
         return;
      }

      try {
         //-- On stocke le documents en base
         dfceService.getDfceServices().storeDocument(document, docFilename, docExtension, docStream);

         if(ExecutableMain.DEBUG_MODE) {
            LOGGER.info("IMPORTED : {}", document.getUuid());
         }

      } catch (final TagControlException e) {
         final String mssg = "Une erreur s'est produite lors du controle du document {}: {}";
         LOGGER.error(mssg, document.getUuid(), e.getMessage());

      } catch (final Exception e) {
         final String mssg = "Une erreur s'est produite lors de l'import du document {}: {}";
         LOGGER.error(mssg, document.getUuid());
      }

   }
}
