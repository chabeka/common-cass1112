/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.listener;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.traces.TracesServicesSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Listener du job capture de masse
 */
@Component
public class CaptureMasseJobListener {

  @Autowired
  private TracesServicesSupport tracesSupport;

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

    final ConcurrentLinkedQueue<String> listMessageExceptions = new ConcurrentLinkedQueue<>();
    context.put(Constantes.DOC_EXCEPTION, listMessageExceptions);

    final ConcurrentLinkedQueue<String> listRollbackExceptions = new ConcurrentLinkedQueue<>();
    context.put(Constantes.ROLLBACK_EXCEPTION, listRollbackExceptions);

    final ConcurrentLinkedQueue<Integer> listIndexDocumentDone = new ConcurrentLinkedQueue<>();
    context.put(Constantes.INDEX_DOCUMENT_DONE, listIndexDocumentDone);

    final ConcurrentLinkedQueue<String> listMetaDoublonsCheckList = new ConcurrentLinkedQueue<>();
    listMetaDoublonsCheckList.add("IdGed");
    context.put(Constantes.META_DOUBLON_CHECK_LIST, listMetaDoublonsCheckList);

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
    tracesSupport.traceEchecCaptureMasse(jobExecution);

  }

}
