/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.modification.support.traces.TracesServicesModificationMasseSupport;

/**
 * Listener du job modification en masse
 */
@Component
public class ModificationMasseJobListener {

  /**
   * Support de tracabilité
   */
  @Autowired
  private TracesServicesModificationMasseSupport tracesSupport;

  /**
   * Initialisation des variables nécessaires au bon déroulement du job
   * 
   * @param jobExecution
   *          le jobExecution
   */
  @BeforeJob
  public final void init(final JobExecution jobExecution) {

    final ExecutionContext context = jobExecution.getExecutionContext();

    final ConcurrentLinkedQueue<Integer> listIndex = new ConcurrentLinkedQueue<>();
    context.put(Constantes.INDEX_EXCEPTION, listIndex);

    final ConcurrentLinkedQueue<Integer> listRefIndex = new ConcurrentLinkedQueue<>();
    context.put(Constantes.INDEX_REF_EXCEPTION, listRefIndex);

    final ConcurrentLinkedQueue<String> listCodes = new ConcurrentLinkedQueue<>();
    context.put(Constantes.CODE_EXCEPTION, listCodes);

    final ConcurrentLinkedQueue<String> listExceptions = new ConcurrentLinkedQueue<>();
    context.put(Constantes.DOC_EXCEPTION, listExceptions);

    final ConcurrentLinkedQueue<String> listRollbackExceptions = new ConcurrentLinkedQueue<>();
    context.put(Constantes.ROLLBACK_EXCEPTION, listRollbackExceptions);

    final ConcurrentLinkedQueue<Integer> listIndexDocumentDone = new ConcurrentLinkedQueue<>();
    context.put(Constantes.INDEX_DOCUMENT_DONE, listIndexDocumentDone);

    final ConcurrentLinkedQueue<String> listTagDoublonsCheckList = new ConcurrentLinkedQueue<>();
    listTagDoublonsCheckList.add("UUID");
    context.put(Constantes.TAG_DOUBLON_CHECK_LIST, listTagDoublonsCheckList);
  }

  /**
   * Méthode appelée après l'exécution du job, que ce soit en réussite ou en
   * échec
   * 
   * @param jobExecution
   *          le jobExecution
   */
  @AfterJob
  public final void afterJob(final JobExecution jobExecution) {

    // Ecriture d'une trace d'échec de capture de masse, le cas échéant
    tracesSupport.traceEchecModificationMasse(jobExecution);

  }

}
