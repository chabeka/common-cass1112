package fr.urssaf.image.sae.batch.documents.executable.multithreading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import net.docubase.toolkit.model.document.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.batch.documents.executable.bootstrap.ExecutableMain;
import fr.urssaf.image.sae.batch.documents.executable.exception.ExportDocsRuntimeException;
import fr.urssaf.image.sae.batch.documents.executable.service.DfceService;

/**
 * Thread d’immport d'un document
 * Cette opération consite à :
 *    
 *    1. Extraire le binaire du document de la base
 *    2. Créer le fichier sommaire du document 
 *    3. Ecrire le fichier document dans le répertoire des documenst 
 */
public class ExportDocsRunnable implements Runnable {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(ExportDocsRunnable.class);
         
   /**
    * Informations du document.
    */
   private Document document;

   /**
    * Service d'accès couche DFCE
    */
   private DfceService dfceService;
   
   private String outputDir;

   /**
    * Constructeur de la classe
    * 
    * @param dfceService
    *           services DFCE
    * @param doc
    *           document
    * @param metas
    *           métadonnées
    */
   public ExportDocsRunnable(DfceService dfceService, Document doc, String dir){
      setDocument(doc);
      setOutputDir(dir);
      setDfceService(dfceService);
   }

   private void setDfceService(DfceService dfceService) {
      this.dfceService = dfceService;
   }
   
   private static synchronized void fileWriteContents(String sFileName, String sContent) 
      throws ExportDocsRuntimeException {
      try {
         File oFile = new File(sFileName); 
         if (!oFile.exists()) {
            LOGGER.warn("Création du fichier {}", sFileName);
            if(!oFile.createNewFile()){
               final String message = "Impossible de creer le fichier "+ sFileName;
               throw new ExportDocsRuntimeException(message); 
            }
         }
         
         FileWriter fWriter = new FileWriter(sFileName, true);
         BufferedWriter bWriter = new BufferedWriter(fWriter);
         bWriter.write(sContent);
         bWriter.newLine();
         try {
            bWriter.close();
         } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
         }  
      } catch (IOException e) {
         final String message = "Impossible d'écrire le fichier "+ sFileName;
         throw new ExportDocsRuntimeException(message); 
      }
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final void run() {
      
      final String docUUID = document.getUuid().toString();

      //-----------------------------------------
      // 1. Récupération du document
      //-----------------------------------------
      InputStream inputStream;
      inputStream = dfceService.getServiceProvider()
         .getStoreService().getDocumentFile(document);

      String fichierErreurs = outputDir + File.separator + "__ERREURS.TXT";
      //------------------------------------------
      // 2. Enregistrement du document
      //------------------------------------------
      FileOutputStream outputStream = null;
      try {
          File file = new File(outputDir + File.separator + docUUID);
          if(file.createNewFile()){
             //-- write file ...
             outputStream = new FileOutputStream(file);
             int read = 0;
             byte[] bytes = new byte[1024];
             while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
             }
             
             if(ExecutableMain.DEBUG_MODE)
                LOGGER.info("EXPORTED : {}", document.getUuid());
             
             outputStream.close();
          } else{
             LOGGER.error("ERREUR: Echec création fichier {}", docUUID); 
             fileWriteContents(fichierErreurs, docUUID);
          }
      } catch (IOException e) {
         LOGGER.error(e.getMessage());
         fileWriteContents(fichierErreurs, docUUID);
      } finally {
         if (inputStream != null) {
            try {
               inputStream.close();
            } catch (IOException e) {
               LOGGER.error(e.getMessage());;
            }
         }
         if (outputStream != null) {
            try {
               outputStream.close();
            } catch (IOException e) {
               LOGGER.error(e.getMessage());;
            }
         }
      }
   }

   /**
    * Permet de récupérer l'objet Document.
    * @return Document
    */
   public final Document getDocument() {
      return document;
   }

   /**
    * Permet de modifier l'objet Document.
    * @param document
    *           document DFCE
    */
   public final void setDocument(final Document document) {
      this.document = document;
   }
   
   /**
    * Set le chemin du répertoir des imports
    */
   public final void setOutputDir(String dir) {
      this.outputDir = dir;
   }
}
