package fr.urssaf.image.sae.services.batch.reprise.tasklet;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.building.services.BuildService;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.services.batch.capturemasse.support.ecde.EcdeSommaireFileSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.rollback.RollbackSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlReadUtils;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.Constantes.BATCH_MODE;
import fr.urssaf.image.sae.services.batch.reprise.exception.RepriseException;
import fr.urssaf.image.sae.services.batch.suppression.exception.SuppressionMasseSearchException;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.util.SAESearchUtil;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.dfce.services.impl.StorageServiceProviderImpl;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.AbstractFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

@Component
public class RepriseCaptureMasseInitTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RepriseCaptureMasseInitTasklet.class);

   /**
    * Nombre de documents par page d'itération par défaut.
    */
   private final static int MAX_PAR_PAGE = 500;

   /**
    * Nombre de documents par page d'itération.
    */
   @Value("${sae.reprise.archivagemasse.rollback.pool.size}")
   private int limit;

   /**
    * Support pour le rollback des documents
    */
   @Autowired
   private RollbackSupport support;

   /**
    * Service de build permettant de générer les critères de la recherche
    * paginée.
    */
   @Autowired
   @Qualifier("buildService")
   private BuildService buildService;

   /**
    * Service permettant d'exécuter la recherche paginée
    */
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider storageServiceProvider;

   /**
    * Support pour le traitement de l'interruption de service
    */
   @Autowired
   private InterruptionTraitementMasseSupport interruptionSupport;

   /**
    * Configuration de l'interuption de service
    */
   @Autowired
   private InterruptionTraitementConfig config;

   /**
    * Service de lecture des jobs
    */
   @Autowired
   private JobLectureService jobLectureService;

   /**
    * Support pour l'ECDE
    */
   @Autowired
   private EcdeSommaireFileSupport fileSupport;

   /**
    * Requete lucene récupéré dans le contexte du job d'exécution
    */
   private String requeteLucene;

   /**
    * Iterateur de documents.
    */
   private Iterator<StorageDocument> iterateurDoc = null;

   /**
    * Flag indiquant s'il s'agit de la dernière itération.
    */
   private boolean lastIteration = false;

   /**
    * Dernier identifiant de document remonté à l'itération précédente.
    */
   private UUID lastIdDoc = null;

   /**
    * Liste des métadonnées que l'on souhaite récupérés
    */
   private final static List<SAEMetadata> DESIRED_METADATAS = new ArrayList<SAEMetadata>();

   /**
    * {@inheritDoc}
    */
   @Override
   public RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {
      LOGGER.info("Début Rollback reprise archivage de masse");
      int incr = 0;

      final StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();

      final String ident = stepExecution.getJobParameters().getString(
            Constantes.ID_TRAITEMENT_A_REPRENDRE_BATCH);

      Assert.notNull(ident, "L'identifiant du job à reprendre est requis");

      final UUID idTraitement = UUID.fromString(ident);

      JobRequest jobAReprendre = jobLectureService
            .getJobRequest(idTraitement);

      Assert.notNull(jobAReprendre, "Le job à reprendre est requis");

      // le paramètre stocké dans la pile des travaux correspond pour les
      // traitements de capture en masse à l'URL ECDE
      String urlECDE = StringUtils.EMPTY;
      if (StringUtils.isNotBlank(jobAReprendre.getParameters())) {
         urlECDE = jobAReprendre.getParameters();
      } else {
         urlECDE = jobAReprendre.getJobParameters().get(Constantes.ECDE_URL);
      }

      Assert.notNull(urlECDE, "L'url de l'ECDE est obligatoire");

      URI sommaireURL;
      try {
         sommaireURL = URI.create(urlECDE);
      } catch (IllegalArgumentException e) {
         throw new RepriseException("Erreur de parsing de l'url ECDE : "
               + e.getMessage(), idTraitement.toString());
      }

      File sommaireFile = fileSupport.convertURLtoFile(sommaireURL);

      if (sommaireFile == null) {
         throw new RepriseException("Le fichier " + sommaireURL.toString()
               + " ne peut être lu", idTraitement.toString());
      }

      stepExecution.getJobExecution().getExecutionContext()
      .put(Constantes.SOMMAIRE_FILE, sommaireFile.getAbsolutePath());

      String batchmode = XmlReadUtils.getElementValue(sommaireFile,
            Constantes.BATCH_MODE_ELEMENT_NAME);

      if (batchmode == null || batchmode.isEmpty()
            || !BATCH_MODE.batchModeExist(batchmode)) {
         throw new RepriseException("Le mode du batch n'est pas reconnu",
               idTraitement.toString());
      }

      if (BatchModeType.TOUT_OU_RIEN.name().equals(batchmode)) {
         requeteLucene = StorageTechnicalMetadatas.ID_TRAITEMENT_MASSE_INTERNE
               .getShortCode() + ":" + idTraitement;

         try {
            // Vérification de la requête lucène
            SAESearchUtil.verifieSyntaxeLucene(requeteLucene);
         } catch (SyntaxLuceneEx e) {
            throw new RepriseException(
                  "La requete lucene de recherche des documents à supprimer est erroné : "
                        + requeteLucene,
                        idTraitement.toString());
         }

         StorageDocument doc = null;
         boolean isFirstRound = true;
         while (hasNext()) {
            doc = next();

            if (doc != null) {
               try {
                  LOGGER.debug("Suppression du document {} en cours", doc
                        .getUuid().toString());
                  support.rollback(doc.getUuid());
                  incr++;
                  LOGGER.debug("Fin suppression du document {} en cours", doc
                        .getUuid().toString());
               } catch (Exception e) {
                  throw new RepriseException(e);
               }
               isFirstRound = false;
            }
         }

         if (isFirstRound) {
            LOGGER.info("Pas de documents identifiés pour le rollback du traitement "
                  + idTraitement.toString());
         }

      } else {
         LOGGER.info("Mode partiel - Pas de rollback à réaliser");
      }

      LOGGER.info(
            "Fin Rollback reprise archivage de masse - Nombre documents supprimés = {}",
            incr);

      return RepeatStatus.FINISHED;
   }

   /**
    * Permet de verifier s'il y a un élément suivant.
    * 
    * @return boolean
    * @throws SuppressionMasseSearchException
    */
   private boolean hasNext() throws RepriseException {
      boolean hasNext;
      if (iterateurDoc == null || !iterateurDoc.hasNext()) {
         // dans ce cas, on est soit à la première itération, soit on n'a plus
         // d'éléments dans notre itérateurs 'local'
         // il faut donc aller récupérer des nouveaux éléments si on n'est pas à
         // la dernière itération
         if (lastIteration) {
            hasNext = false;
         } else {
            // avant de faire appel à la base pour récupérer les éléments, on va
            // tester qu'on est pas à l'heure
            // de l'interruption des serveurs
            gererInterruption();
            // on appelle la récupération des prochains éléments
            hasNext = fetchMore();
         }
      } else {
         // on a encore des éléments dans l'itérateur 'local'
         hasNext = true;
      }
      return hasNext;
   }

   /**
    * Recupere le prochain element.
    * 
    * @return StorageDocument
    */
   private StorageDocument next() {
      return iterateurDoc.next();
   }

   /**
    * Permet de récupérer les prochains éléments.
    * 
    * @return boolean
    * @throws SuppressionMasseSearchException
    */
   private boolean fetchMore() throws RepriseException {
      PaginatedStorageDocuments paginatedStorageDocuments = null;
      boolean hasMore = false;
      try {
         String strIdDoc;
         if (lastIdDoc == null) {
            strIdDoc = "null";
         } else {
            strIdDoc = lastIdDoc.toString();
         }

         LOGGER.debug("fetchMore : {} - {}", requeteLucene, strIdDoc);
         PaginatedLuceneCriteria paginatedLuceneCriteria = buildService
               .buildStoragePaginatedLuceneCriteria(requeteLucene,
                     limit > 0 ? limit : MAX_PAR_PAGE, DESIRED_METADATAS,
                           new ArrayList<AbstractFilter>(), lastIdDoc, "");

         ((StorageServiceProviderImpl) storageServiceProvider)
               .getDfceServicesManager().openConnection();

         paginatedStorageDocuments = storageServiceProvider
               .getStorageDocumentService().searchPaginatedStorageDocuments(
                     paginatedLuceneCriteria);

         // recupere les infos de la requete
         iterateurDoc = paginatedStorageDocuments.getAllStorageDocuments()
               .iterator();
         lastIteration = paginatedStorageDocuments.getLastPage();
         if (!paginatedStorageDocuments.getAllStorageDocuments().isEmpty()) {
            int nbElements = paginatedStorageDocuments.getAllStorageDocuments()
                  .size();
            lastIdDoc = paginatedStorageDocuments.getAllStorageDocuments()
                  .get(nbElements - 1).getUuid();
            hasMore = true;
         } else {
            hasMore = false;
         }

      } catch (ConnectionServiceEx except) {
         throw new RepriseException(except);
      } catch (SearchingServiceEx except) {
         throw new RepriseException(except);
      } catch (QueryParseServiceEx except) {
         throw new RepriseException(except);
      }
      return hasMore;
   }

   /**
    * Methode permettant de gerer la plage d'interruption.
    * 
    * @throws SuppressionMasseSearchException
    */
   private void gererInterruption() throws RepriseException {
      // on vérifie que le traitement ne doit pas s'interrompre
      final DateTime currentDate = new DateTime();

      if (config != null
            && interruptionSupport.hasInterrupted(currentDate, config)) {

         try {
            interruptionSupport.interruption(currentDate, config);
         } catch (InterruptionTraitementException e) {
            throw new RepriseException(e);
         }
      }
   }


}
