/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.stockage.batch;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.rollback.RollbackSupport;
import fr.urssaf.image.sae.services.document.SAEDocumentService;

/**
 * Tasklet pour le rollback
 * 
 */
@Component
public class RollbackTasklet extends AbstractRollbackTasklet implements Tasklet {

   /**
    * 
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(RollbackTasklet.class);

   private static final String TRC_ROLLBACK = "rollbacktasklet()";

   @Autowired
   private RollbackSupport support;

   /**
    * Pool d'exécution des insertions de documents
    */
   @Autowired
   private SAEDocumentService documentService;

   @Autowired
   private InsertionPoolThreadExecutor executor;

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings( { "PMD.AvoidThrowingRawExceptionTypes", "unchecked" })
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) {

      /*
       * on va incrémenter le nombre d'enregistrements lus et écrits. ces
       * nombres sont décorrélés du nombre lus et écrits dans le step précédent.
       */

      int countRead = chunkContext.getStepContext().getStepExecution()
            .getReadCount();

      int countWrite = chunkContext.getStepContext().getStepExecution()
            .getWriteCount();

      ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> listIntegDocs = executor
            .getIntegratedDocuments();

      RepeatStatus status;

      try {

         if (CollectionUtils.isNotEmpty(listIntegDocs)) {

            /*
             * Afin de savoir si il y a eu un problème de rollback dans l'étape
             * de fin, nous supprimons les enregistrements de la liste au fur et
             * à mesure. Il faut donc toujours récupérer le 1er élément de la
             * liste.
             */
            CaptureMasseIntegratedDocument intDoc = listIntegDocs.peek();
            // UUID strDocumentID = listIntegDocs.toArray(new UUID[0])[0];
            UUID strDocumentID = intDoc.getIdentifiant();

            support.rollback(strDocumentID);

            chunkContext.getStepContext().getStepExecution().setReadCount(
                  ++countRead);

            LOGGER.debug("{} - Rollback du document #{} ({})", new Object[] {
                  TRC_ROLLBACK, countRead, strDocumentID });

            chunkContext.getStepContext().getStepExecution().setWriteCount(
                  ++countWrite);

            listIntegDocs.remove(intDoc);

            if (CollectionUtils.isEmpty(listIntegDocs)) {
               status = realiserRecherche(chunkContext);
            } else {
               status = RepeatStatus.CONTINUABLE;
            }

         } else {

            LOGGER.debug("{} - Aucun document à supprimer",
                  new Object[] { TRC_ROLLBACK });

            status = realiserRecherche(chunkContext);
         }

      } catch (Exception e) {

         String idTraitement = (String) chunkContext.getStepContext()
               .getJobParameters().get(Constantes.ID_TRAITEMENT);

         String errorMessage = MessageFormat.format(
               "{0} - Une exception a été levée lors du rollback : {1}",
               TRC_ROLLBACK, idTraitement);

         LOGGER.warn(errorMessage, e);

         LOGGER
               .error(

                     "Le traitement de masse n°{} doit être rollbacké par une procédure d'exploitation",
                     idTraitement);
         chunkContext.getStepContext().getStepExecution().getJobExecution()
               .getExecutionContext().put(Constantes.FLAG_BUL003, Boolean.TRUE);

         // Ajoute l'exception survenue dans la liste des exceptions du Rollback
         ExecutionContext executionContext = chunkContext.getStepContext()
               .getStepExecution().getJobExecution().getExecutionContext();
         if (executionContext.get(Constantes.ROLLBACK_EXCEPTION) != null) {
            ConcurrentLinkedQueue<Exception> listExceptions = (ConcurrentLinkedQueue<Exception>) executionContext
                  .get(Constantes.ROLLBACK_EXCEPTION);
            listExceptions.add(e);
         }

         status = RepeatStatus.FINISHED;

      }

      return status;
   }

   /**
    * @param chunkContext
    */
   private RepeatStatus realiserRecherche(ChunkContext chunkContext) {

      RepeatStatus repeatStatus;

      // ConcurrentLinkedQueue<UUID> listUUID = new
      // ConcurrentLinkedQueue<UUID>();
      ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> listeIntegratedDoc = new ConcurrentLinkedQueue<CaptureMasseIntegratedDocument>();

      int nbreDocsTotal = (Integer) chunkContext.getStepContext()
            .getJobExecutionContext().get(Constantes.DOC_COUNT);

      int rollbackCount = chunkContext.getStepContext().getStepExecution()
            .getExecutionContext().getInt(Constantes.COUNT_ROLLBACK);
      int countRecherche = 0;

      if (!chunkContext.getStepContext().getStepExecution()
            .getExecutionContext().containsKey(Constantes.SEARCH_ROLLBACK)) {

         LOGGER
               .debug(
                     "{} - On recherche les potentiels documents restants à supprimer",
                     new Object[] { TRC_ROLLBACK });

         String idTraitement = (String) chunkContext.getStepContext()
               .getJobParameters().get(Constantes.ID_TRAITEMENT);

         List<UntypedDocument> listDocs = trouverDocumentsRestants(idTraitement);

         if (CollectionUtils.isNotEmpty(listDocs)) {

            // listUUID = transformerListeDocEnUuid(listDocs);
            listeIntegratedDoc = transformerEnListeIntegratedDoc(listDocs);
            executor.getIntegratedDocuments().addAll(listeIntegratedDoc);

            countRecherche = listDocs.size();

         }
      }

      rollbackCount = rollbackCount + countRecherche;
      chunkContext.getStepContext().getStepExecution().getExecutionContext()
            .putInt(Constantes.COUNT_ROLLBACK, rollbackCount);

      if (nbreDocsTotal < rollbackCount) {
         repeatStatus = RepeatStatus.FINISHED;
         LOGGER
               .warn(
                     "{} - Une erreur est survenue lors du rollback. "
                           + "Le nombre maximal de documents à supprimer est de {}, et {} ont été comptabilisés.",
                     new Object[] { TRC_ROLLBACK,
                           String.valueOf(nbreDocsTotal),
                           String.valueOf(rollbackCount) });

      } else if (CollectionUtils.isEmpty(listeIntegratedDoc)) {
         repeatStatus = RepeatStatus.FINISHED;

      } else if (MAX_RESULT == listeIntegratedDoc.size()) {

         repeatStatus = RepeatStatus.CONTINUABLE;

         // chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put(Constantes.INTEG_DOCS,
         // listUUID);
      } else {
         repeatStatus = RepeatStatus.CONTINUABLE;

         // chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put(Constantes.INTEG_DOCS,
         // listUUID);

         chunkContext.getStepContext().getStepExecution().getExecutionContext()
               .put(Constantes.SEARCH_ROLLBACK, Boolean.TRUE.toString());
      }

      return repeatStatus;

   }

   /**
    * @param listDocs
    * @return
    */
   /*
    * private ConcurrentLinkedQueue<UUID> transformerListeDocEnUuid(
    * List<UntypedDocument> listDocs) {
    * 
    * ConcurrentLinkedQueue<UUID> list = new ConcurrentLinkedQueue<UUID>();
    * 
    * if (CollectionUtils.isNotEmpty(listDocs)) {
    * 
    * for (UntypedDocument document : listDocs) { list.add(document.getUuid());
    * } }
    * 
    * return list; }
    */

   /**
    * Transforme une liste de UntypedDoc en liste d'IntegratedDocumentType
    * (uniquement pour l'uuid)
    */
   private ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> transformerEnListeIntegratedDoc(
         List<UntypedDocument> listDocs) {
      ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> list = new ConcurrentLinkedQueue<CaptureMasseIntegratedDocument>();
      if (CollectionUtils.isNotEmpty(listDocs)) {
         for (UntypedDocument document : listDocs) {
            CaptureMasseIntegratedDocument doc = new CaptureMasseIntegratedDocument();
            doc.setIdentifiant(document.getUuid());
            list.add(doc);
         }
      }
      return list;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final SAEDocumentService getDocumentService() {
      return documentService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }

}
