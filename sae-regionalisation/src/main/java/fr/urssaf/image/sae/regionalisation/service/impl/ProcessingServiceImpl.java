package fr.urssaf.image.sae.regionalisation.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.regionalisation.bean.RepriseConfiguration;
import fr.urssaf.image.sae.regionalisation.bean.Trace;
import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.dao.TraceDao;
import fr.urssaf.image.sae.regionalisation.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.service.ProcessingService;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

/**
 * Implémentation du service {@link ProcessingService}
 * 
 * 
 */
@Service
public class ProcessingServiceImpl implements ProcessingService {

   private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
         "dd/MM/yyyy HH:mm:ss");

   private static final int MILLISEC_CONVERSION = 1000;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ProcessingServiceImpl.class);

   private final SaeDocumentDao saeDocumentDao;

   private final TraceDao traceDao;

   private final ServiceProviderSupport serviceProviderSupport;

   private final RepriseConfiguration repriseConfiguration;

   /**
    * Ligne du fichier de données en cours de traitement
    */
   private int currentRecord;

   private int nbDocTraites;

   /**
    * Constructeur
    * 
    * @param saeDocumentDao
    *           dao des documents SAE
    * @param traceDao
    *           dao des traces
    * @param serviceProviderSupport
    *           services DFCE
    * @param repriseConfiguration
    *           configuration de reprise de traitement automatique
    */
   @Autowired
   public ProcessingServiceImpl(SaeDocumentDao saeDocumentDao,
         TraceDao traceDao, ServiceProviderSupport serviceProviderSupport,
         RepriseConfiguration repriseConfiguration) {

      this.saeDocumentDao = saeDocumentDao;
      this.traceDao = traceDao;
      this.serviceProviderSupport = serviceProviderSupport;
      this.repriseConfiguration = repriseConfiguration;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int launchWithFile(boolean updateDatas, File source,
         String idTraitement, int firstRecord, int lastRecord, String dirPath) {

      boolean success = false;
      int countTentatives = 1;
      currentRecord = firstRecord;
      nbDocTraites = 0;

      File dirParent = new File(dirPath);

      // Suppression du fichier fin_traitement_[id].flag
      File endFile = new File(dirParent, "fin_traitement_" + idTraitement
            + ".flag");
      FileUtils.deleteQuietly(endFile);

      // Boucle sur un nombre max de tentatives définies dans la configuration
      while (!success
            && (countTentatives <= repriseConfiguration.getMaxTestCount())) {
         try {

            // A partir de la 2ème tentative, met le programme en pause pendant
            // un laps de temps défini dans la configuration fournie
            if (countTentatives > 1) {
               Thread.sleep(repriseConfiguration.getMaxTestCount()
                     * MILLISEC_CONVERSION);
            }

            // Ouverture des fichiers de traces
            traceDao.open(idTraitement);

            // Création ou maj du fichier debut_traitement_[id].flag
            createOrUpdateDebutTraitement(dirParent, idTraitement,
                  countTentatives);

            // Exécute le traitement de régionalisation
            processFile(updateDatas, source, firstRecord, lastRecord);

            // Si aucune exception, alors cette tentative a réussi
            success = true;

         } catch (Throwable throwable) {
            LOGGER.error(
                  "Echec de la tentative de traitement {} à la ligne {}\n",
                  countTentatives, currentRecord);
            LOGGER.error("erreur source :", throwable);

         } finally {
            traceDao.close();
         }

         // Incrémente le compteur de tentative
         countTentatives++;

      }

      // Création du fichier fin_traitement_[id].flab
      createFinTraitement(dirParent, success, idTraitement);

      // Renvoie le nombre de documents mis à jour
      return nbDocTraites;

   }

   private void processFile(boolean updateDatas, File source, int firstRecord,
         int lastRecord) {

      // Pour les traces applicatives
      String trcPrefixe = "processFile()";

      // Connexion à DFCE
      serviceProviderSupport.connect();

      // Récupération de l'objet Base sur laquelle on travaille
      Base base = saeDocumentDao.getBase();

      // Try/catch pour l'ouverture/fermeture du fichier CSV contenant
      // les documents à modifier
      FileReader fileReader = null;
      BufferedReader reader = null;
      try {

         // Ouverture du fichier contenant les documents à modifier
         fileReader = new FileReader(source);
         reader = new BufferedReader(fileReader);

         // Se déplace jusqu'à la première ligne à traiter
         int currentIndex = 0;
         String line = StringUtils.EMPTY;
         while (currentIndex < firstRecord
               && (line = reader.readLine()) != null) {
            currentIndex++;
         }

         // Traite chaque ligne jusqu'à atteindre la dernière ligne
         // à traiter ou la fin du fichier
         do {

            processDocument(updateDatas, base, line, currentIndex);

            currentIndex++;

         } while ((currentIndex <= lastRecord)
               && ((line = reader.readLine()) != null));

      } catch (FileNotFoundException ex) {
         throw new ErreurTechniqueException(ex);

      } catch (IOException ex) {
         throw new ErreurTechniqueException(ex);

      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               LOGGER.info("{} - Impossible de fermer le flux de données "
                     + source.getName(), trcPrefixe);
            }
         }

         if (fileReader != null) {
            try {
               fileReader.close();
            } catch (IOException e) {
               LOGGER.info("{} - Impossible de fermer le flux de données "
                     + source.getName(), trcPrefixe);
            }
         }
      }

      // Déconnexion de DFCE
      serviceProviderSupport.disconnect();

   }

   private void createOrUpdateDebutTraitement(File dirParent, String uuid,
         int tentative) {

      File startFile = new File(dirParent, "debut_traitement_" + uuid + ".flag");
      FileWriter fileWriter = null;

      try {
         fileWriter = new FileWriter(startFile, true);
         fileWriter.write("tentative " + tentative + " - ");
         fileWriter.write("Date : " + SIMPLE_DATE_FORMAT.format(new Date())
               + "\n");

      } catch (IOException e) {
         throw new ErreurTechniqueException(e);

      } finally {
         if (fileWriter != null) {
            try {
               fileWriter.close();
            } catch (IOException e) {
               LOGGER.info("impossible de fermer le flux de "
                     + startFile.getName());
            }
         }
      }

   }

   private void createFinTraitement(File dirParent, boolean succes, String uuid) {
      File endFile = new File(dirParent, "fin_traitement_" + uuid + ".flag");
      FileWriter fileWriter = null;

      try {
         fileWriter = new FileWriter(endFile);
         fileWriter.write(succes ? "OK" : "KO");
         fileWriter.write("\n");

      } catch (IOException e) {
         throw new ErreurTechniqueException(e);

      } finally {
         if (fileWriter != null) {
            try {
               fileWriter.close();
            } catch (IOException e) {
               LOGGER.info("impossible de fermer le flux de "
                     + endFile.getName());
            }
         }
      }

   }

   private void processDocument(boolean updateDatas, Base base, String ligne,
         int numeroLigne) {

      // Eclate la ligne
      String[] tabLigne = ligne.split(";");
      UUID idDoc = UUID.fromString(tabLigne[0]);
      String newNce = tabLigne[1];
      String newNci = tabLigne[2];
      String newNpe = tabLigne[3];
      String newCog = tabLigne[4];
      String newCop = tabLigne[5];

      // Récupération du document depuis DFCE
      Document document = saeDocumentDao.find(base, idDoc);
      if (document == null) {

         // TODO: gérer le cas du document non trouvé
         LOGGER.warn("Document non trouvé: {}", idDoc);

      } else {

         updateDocument(updateDatas, numeroLigne, base, document, newNce,
               newNci, newNpe, newCog, newCop);

      }

      // Met à jour le nombre de documents traités
      nbDocTraites++;

   }

   private void updateDocument(boolean updateDatas, int numeroLigne, Base base,
         Document document, String newNce, String newNci, String newNpe,
         String newCog, String newCop) {

      // Initialise une liste d'objets traces
      // qui sera persistée en fin de méthode
      List<Trace> traces = new ArrayList<Trace>();

      // Maj des métadonnées dans l'objet mémoire Document
      updateMetadonnees(numeroLigne, document, "nce", newNce, traces);
      updateMetadonnees(numeroLigne, document, "nci", newNci, traces);
      updateMetadonnees(numeroLigne, document, "npe", newNpe, traces);
      updateMetadonnees(numeroLigne, document, "cog", newCog, traces);
      updateMetadonnees(numeroLigne, document, "cop", newCop, traces);

      // Effectue la mise à jour du document si demandé (pas tir à blanc)
      if (updateDatas) {
         saeDocumentDao.update(document);
      }

      // Ecrit les traces de mises à jour
      for (Trace trace : traces) {
         traceDao.addTraceMaj(trace);
      }

   }

   private void updateMetadonnees(int numeroLigne, Document document,
         String codeMeta, String nouvelleValeur, List<Trace> traces) {

      if (StringUtils.isNotBlank(nouvelleValeur)) {

         Trace trace = new Trace();
         trace.setIdDocument(document.getUuid());
         trace.setLineNumber(numeroLigne);
         trace.setMetaName(codeMeta);

         String oldValeur = serviceProviderSupport.getValeurCriterion(document,
               codeMeta);
         trace.setOldValue(oldValeur);

         serviceProviderSupport.updateCriterion(document, codeMeta,
               nouvelleValeur);

         String newValeur = serviceProviderSupport.getValeurCriterion(document,
               codeMeta);
         trace.setNewValue(newValeur);

         traces.add(trace);

      }

   }

}
