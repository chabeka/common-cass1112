package fr.urssaf.image.sae.batch.documents.executable.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Criterion;
import net.docubase.toolkit.model.document.Document;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.sae.batch.documents.executable.exception.ExportDocsRuntimeException;
import fr.urssaf.image.sae.batch.documents.executable.exception.ImportDocsRuntimeException;
import fr.urssaf.image.sae.batch.documents.executable.model.DeleteDocsParametres;
import fr.urssaf.image.sae.batch.documents.executable.model.ExportDocsParametres;
import fr.urssaf.image.sae.batch.documents.executable.model.ImportDocsParametres;
import fr.urssaf.image.sae.batch.documents.executable.multithreading.DeleteDocsRunnable;
import fr.urssaf.image.sae.batch.documents.executable.multithreading.DocumentsThreadExecutor;
import fr.urssaf.image.sae.batch.documents.executable.multithreading.ExportDocsRunnable;
import fr.urssaf.image.sae.batch.documents.executable.multithreading.ImportDocsRunnable;
import fr.urssaf.image.sae.batch.documents.executable.service.DfceService;
import fr.urssaf.image.sae.batch.documents.executable.service.TraitementService;

/**
 * Classe d'implémentation du service <b>TraitementService</b>. Cette classe est
 * un singleton, et est accessible via l'annotation <b>@AutoWired</b>.
 */
public class TraitementServiceImpl implements TraitementService {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraitementServiceImpl.class);

   /**
    * Service permettant de réaliser des opérations sur DFCE.
    */
   private DfceService dfceService;
   
   /**
    * Constructeur
    * @param dfceService
    */
   public TraitementServiceImpl(DfceService dfceService) {
      this.dfceService = dfceService;
   }
   
   private final String SOMMAIRE = "__SOMMAIRE.TXT";

   
   
   private final String[] DATE_CRITERIONS = { "dpa", "dre", "dsf", "dfa", "dfc", 
         "dli", "dsi", "dcr", "dag", "dad", "dcd", "dcc", "ddf", "dbp"};
   
   private final String[] BOOL_CRITERIONS = {"gel", "cot", "cpt", "drh", "dar", "dte", "bap", "cco", "sfa"};
   private final String[] INTG_CRITERIONS = {"aex", "dco", "nbp"};
   private final String[] DOUB_CRITERIONS = {"mre", "mde"};
   private final SimpleDateFormat dtFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   
   /**
    * Permet de récupérer le service permettant de réaliser des opérations sur
    * DFCE.
    * 
    * @return DfceService
    */
   public final DfceService getDfceService() {
      return dfceService;
   }

   /**
    * Permet de modifier le service permettant de réaliser des opérations sur
    * DFCE.
    * 
    * @param dfceService
    *           service permettant de réaliser des opérations sur DFCE.
    */
   public final void setDfceService(final DfceService dfceService) {
      this.dfceService = dfceService;
   }

   /**
    * Methode permettant d'attendre la fin du traitement de suppression
    * 
    * @param poolThead
    *           pool de thread
    */
   private void waitFinTraitement(DocumentsThreadExecutor poolThead) {
      poolThead.shutdown();
      
      // -- On attend la fin de l'execution du poolThead
      poolThead.waitFinishProcess();

      LOGGER.info("{} documents traités au total", poolThead.getNombreTraites());
   }
   
   public class SommaireLineMapper {
      
      private Document document;
      private String docFilename;
      private String docExtension;
      
      public SommaireLineMapper(final String line) throws ParseException {
         String baseName = dfceService.getDfceConnection().getBaseName();
         Base base = dfceService.getServiceProvider().getBaseAdministrationService().getBase(baseName);
         document = ToolkitFactory.getInstance().createDocumentTag(base);
         initDocument(line);
      }
      
      public Document getDocument() {
         return document;
      }

      public String getDocFilename() {
         return docFilename;
      }

      public String getDocExtension() {
         return docExtension;
      }

      /**
       * Deserialise une ligne du fichier sommaire
       * Structure d'une ligne : 
       * uuid||type||title||lifecyclerefdate||creationdate||filename||extension||CRITERIONS
       * avec CRITERIONS sous la forme : key1:|:val1:|:key2:|:val2 ...
       * 
       * @param line
       * @return
       * @throws ParseException
       */
      private void initDocument(String line) throws ParseException {
         
         String[] data = line.split("\\|\\|");

         //-- Champs objet document
         document.setUuid(UUID.fromString(data[0]));
         document.setType(data[1]);
         document.setTitle(data[2]);
         document.setLifeCycleReferenceDate(dtFormatter.parse(data[3]));
         document.setCreationDate(dtFormatter.parse(data[4]));
         
         //-- Infos utiles à la méthode dfce d'enregistrement 
         // du documents en base
         docFilename = data[5];
         docExtension = data[6];
         
         //-- Critérions
         for (int i = 7; i < data.length; i++) {
            
            String[] item = data[i].split(":\\|:");
            String critKey = item[0];
            String critVal = item[1];
            
            if(ArrayUtils.contains(DATE_CRITERIONS, critKey)){
               document.addCriterion(critKey, dtFormatter.parse(critVal));
            } else if(ArrayUtils.contains(BOOL_CRITERIONS, critKey)){
               document.addCriterion(critKey, Boolean.valueOf(critVal));
            } else if(ArrayUtils.contains(DOUB_CRITERIONS, critKey)){
               document.addCriterion(critKey, Double.valueOf(critVal));
            } else if(ArrayUtils.contains(INTG_CRITERIONS, critKey)){
               document.addCriterion(critKey, Integer.valueOf(critVal));
            } else {
               //-- default crtirions type = string
               document.addCriterion(critKey, item[1]);
            }
         }
      }
   }
     
   /**
    * Ecrit une ligne dans le fichier sommaire à partir d'un {@link Document} au format : 
    * "uuid||type||title||lifecyclerefdate||creationdate||filename||extension||CRITERIONS"
    * 
    * avec CRITERIONS = key1:|:val1:|:key2:|:val2 ...
    * 
    * @param doc : le document
    * @param bWriter : le buffer writer du fichier sommaire
    */
   private void writeSommaireMetas(Document doc, BufferedWriter bWriter){
      List<Criterion> criterions = doc.getAllCriterions();
      
      //-- Infos objet document
      StringBuilder sb = new StringBuilder();
      sb.append(doc.getUuid());        // index=0
      sb.append("||");
      sb.append(doc.getType());        // index=1
      sb.append("||");
      sb.append(doc.getTitle());       // index=2
      sb.append("||");
      sb.append(dtFormatter.format(doc.getLifeCycleReferenceDate())); // index=3
      sb.append("||");
      sb.append(dtFormatter.format(doc.getCreationDate()));           // index=4
      sb.append("||");
      sb.append(doc.getFilename());    // index=5
      sb.append("||");
      sb.append(doc.getExtension());   // index=6
      
      //-- Critérions : index=[5,...]
      for (Criterion criterion : criterions) {
         String name = criterion.getCategoryName();
         sb.append("||");
         sb.append(name);
         sb.append(":|:");
         if(!ArrayUtils.contains(DATE_CRITERIONS, name)){
            sb.append(criterion.getWord());
         } else {
            String date = dtFormatter.format((Date)criterion.getWord());
            sb.append(date);
         }
      }
      try { 
         bWriter.write(sb.toString());
         bWriter.newLine();
         
      } catch (Exception e) {
         LOGGER.error(e.getMessage()); 
         String mssg = "Une erreur s'est produite sur le fichier sommaire";
         throw new ExportDocsRuntimeException(mssg, e);
      }
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteDocuments(DeleteDocsParametres parametres) {
      // -- Overture connexion dfce
      getDfceService().ouvrirConnexion();
      
      DocumentsThreadExecutor poolThread = null;
      String requeteLucene = parametres.getRequeteLucene();
      
      try {
         Iterator<Document> it;
         it = getDfceService().executerRequete(requeteLucene);       
         poolThread = new DocumentsThreadExecutor(parametres);
           
         //-- On bouble sur la liste des résultats
         while (it.hasNext()) {
            Document document = (Document) it.next();
            DeleteDocsRunnable deleteDocRun;
            deleteDocRun = new DeleteDocsRunnable(getDfceService(), document);
            poolThread.execute(deleteDocRun);
         }

         // -- On attend la fin de l'execution du poolThead
         waitFinTraitement(poolThread);
         
         LOGGER.info("{} documents traités au total", poolThread.getNombreTraites());
      } catch (SearchQueryParseException ex) {
         LOGGER.error("La syntaxe de la requête n'est pas valide : {}", ex
               .getMessage());
      }  catch (Error ex) {
         // gestion des erreurs grave de la jvm
         // on essaie d'arrêter le pool et d'attendre la fin 
         if (poolThread != null && !poolThread.isShutdown()) {
            // attend la fin du traitement
            waitFinTraitement(poolThread);
         }
         // et on envoie l'erreur à l'appelant
         throw ex;
      } catch (RuntimeException ex) {
         // gestion des erreurs de types runtime
         // on essaie d'arrêter le pool et d'attendre la fin 
         if (poolThread != null && !poolThread.isShutdown()) {
            // attend la fin du traitement
            waitFinTraitement(poolThread);
         }
         // et on envoie l'erreur à l'appelant
         throw ex;
      } 
      
      // -- Fermeture connexion dfce
      getDfceService().fermerConnexion();
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   public void exportDocuments(ExportDocsParametres parameters) {
      // -- Overture connexion dfce
      getDfceService().ouvrirConnexion();
      
      final String exportDir = parameters.getExportDir();
 
      //------------------------------------------
      // 1. Création du dossier de destination des documents
      //------------------------------------------
      File dir = new File(exportDir);
      if(!dir.exists()){
          if(!dir.mkdir()){
             String mssg = "Impossible de creer le dossier de destinantion des documents";
             LOGGER.error(mssg); 
             throw new ExportDocsRuntimeException(mssg);
          }
      } 
      LOGGER.info("Chemin du dossier d'export : {}", exportDir);
      
      //------------------------------------------
      // 2. Création du fichier sommaire
      //------------------------------------------
      String sommaire = dir + File.separator + SOMMAIRE;
      File fSommaire = new File(sommaire); 
      FileWriter fWriter = null;
      BufferedWriter bWriter = null;

      if (!fSommaire.exists()) {
         LOGGER.warn("Création du fichier {}", sommaire);
         try {
            if(!fSommaire.createNewFile()){
               String message = "Impossible de creer le fichier "+ sommaire;
               throw new ExportDocsRuntimeException(message); 
            }
            fWriter = new FileWriter(fSommaire, true);
            bWriter = new BufferedWriter(fWriter);
         } catch (IOException e) {
            String message = "Impossible de creer le fichier sommaire";
            throw new ExportDocsRuntimeException(message, e); 
         }
      }

      //------------------------------------------
      // 3. Exécution requete et parcours des résulatsts
      //------------------------------------------
      
      DocumentsThreadExecutor poolThread = null;
      String requeteLucene = parameters.getRequeteLucene();
      
      try {
         Iterator<Document> it;
         it = getDfceService().executerRequete(requeteLucene);       
         poolThread = new DocumentsThreadExecutor(parameters);
         
         LOGGER.warn("Parcour de la liste des résultats");
         
         //-- On boucle sur la liste des résultats
         while (it.hasNext()) {        
            Document document = (Document) it.next();
            
            //-- Ecriture du fichier sommaire
            writeSommaireMetas(document, bWriter);
            
            //-- Enregistrement du binaire (pile multi-thread)
            ExportDocsRunnable exportDocRun;
            exportDocRun = new ExportDocsRunnable(getDfceService(), document, exportDir);
            poolThread.execute(exportDocRun);
         }
         
         //-- Fermertur du buffer d'écriture dans le fichier sommaire
         try {
            bWriter.close();
         } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
         } 

         // -- On attend la fin de l'execution du poolThead
         waitFinTraitement(poolThread);
         
         LOGGER.info("Total documents traités: {}", poolThread.getNombreTraites());
      } catch (SearchQueryParseException e) {
         LOGGER.error("La syntaxe de la requête n'est pas valide : {}", e.getMessage());
      }  catch (Error ex) {
         // gestion des erreurs grave de la jvm
         // on essaie d'arrêter le pool et d'attendre la fin 
         if (poolThread != null && !poolThread.isShutdown()) {
            // attend la fin du traitement
            waitFinTraitement(poolThread);
         }
         // et on envoie l'erreur à l'appelant
         throw ex;
      } catch (RuntimeException ex) {
         // gestion des erreurs de types runtime
         // on essaie d'arrêter le pool et d'attendre la fin 
         if (poolThread != null && !poolThread.isShutdown()) {
            // attend la fin du traitement
            waitFinTraitement(poolThread);
         }
         // et on envoie l'erreur à l'appelant
         throw new ExportDocsRuntimeException(ex);
      } 
      
      // -- Fermeture connexion dfce
      getDfceService().fermerConnexion();
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   public void importDocuments(ImportDocsParametres parameters) {
      
      // -- Overture connexion dfce
      getDfceService().ouvrirConnexion();
      
      final String basePath = parameters.getImportDir()+ File.separator;
      String sommaire = basePath + SOMMAIRE;
      File fichierSommaire = new File(sommaire);
      
      FileReader fileReader = null;
      DocumentsThreadExecutor poolThread = null;
      poolThread = new DocumentsThreadExecutor(parameters);
      
      try {
         fileReader = new FileReader(fichierSommaire);
         BufferedReader bufReader = new BufferedReader(fileReader);
         String line;
         int index = 0;
         while ((line = bufReader.readLine()) != null) {
            index++;
            if(!StringUtils.isEmpty(line)){
               
               SommaireLineMapper mapper;
               Document document = null;
               try{
                  mapper = new SommaireLineMapper(line);
                  document = mapper.getDocument();
               } catch (ParseException e) {
                  LOGGER.error("Erreur lecture fichier sommaire ligne {} :"+ e.getMessage() , index);
                  continue;
               }
               
               //-- Fichier à insérer en base
               File docFile = new File(basePath + document.getUuid());
               if(!docFile.exists()){
                  LOGGER.error("Erreur: fichier introuvable {}", basePath + document.getUuid());
                  return;
               }
               
               //-- Enregistrement en base (pile multi-thread)
               ImportDocsRunnable importDocRun;
               importDocRun = new ImportDocsRunnable(getDfceService(), mapper, docFile);
               poolThread.execute(importDocRun);
            }
         }
         
         //-- Fermerture buffer lecture fichier sommaire
         try { 
            bufReader.close(); 
         } catch (IOException e){ 
            LOGGER.warn(e.getMessage(), e); 
         } 
         
         //-- attend la fin du traitement
         waitFinTraitement(poolThread);
         
      } catch (FileNotFoundException e) {
         throw new ImportDocsRuntimeException(e);
      } catch (IOException e) {
         throw new ImportDocsRuntimeException(e);
      } catch (Error ex) {
         // gestion des erreurs grave de la jvm
         // on essaie d'arrêter le pool et d'attendre la fin 
         if (poolThread != null && !poolThread.isShutdown()) {
            // attend la fin du traitement
            waitFinTraitement(poolThread);
         }
         // et on envoie l'erreur à l'appelant
         throw ex;
      } catch (RuntimeException ex) {
         // gestion des erreurs de types runtime
         // on essaie d'arrêter le pool et d'attendre la fin 
         if (poolThread != null && !poolThread.isShutdown()) {
            // attend la fin du traitement
            waitFinTraitement(poolThread);
         }
         // et on envoie l'erreur à l'appelant
         throw new ImportDocsRuntimeException(ex);
      }
      
      // -- Fermeture connexion dfce
      getDfceService().fermerConnexion();
   }
}
