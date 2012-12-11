/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.batch;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeSommaireFileSupport;
import fr.urssaf.image.sae.services.controles.SAEControleSupportService;

/**
 * Tasklet pour la vérification du fichier sommaire.xml
 * 
 */
@Component
public class CheckFileSommaireTasklet implements Tasklet {

   @Autowired
   private EcdeSommaireFileSupport fileSupport;

   @Autowired
   private SAEControleSupportService controleSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("unchecked")
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final Map<String, Object> parameters = chunkContext.getStepContext()
            .getJobParameters();

      final String urlEcde = (String) parameters.get(Constantes.SOMMAIRE);
      
      final StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();
      final ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      context.put(Constantes.SOMMAIRE, urlEcde);

      try {
         final URI uriEcde = new URI(urlEcde);

         final File sommaire = fileSupport.convertURLtoFile(uriEcde);

         controleSupport.checkEcdeWrite(sommaire);
         
         context.put(Constantes.SOMMAIRE_FILE, sommaire.getAbsolutePath());
         
      } catch (CaptureMasseSommaireEcdeURLException e) {
         final Exception exception = new Exception(e.getMessage());

         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) chunkContext
               .getStepContext().getStepExecution().getJobExecution()
               .getExecutionContext().get(Constantes.DOC_EXCEPTION);
         exceptions.add(exception);

      } catch (CaptureMasseSommaireFileNotFoundException e) {
         final Exception exception = new Exception(e.getMessage());
         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) chunkContext
               .getStepContext().getStepExecution().getJobExecution()
               .getExecutionContext().get(Constantes.DOC_EXCEPTION);
         exceptions.add(exception);

      } catch (CaptureMasseEcdeWriteFileException e) {
         final Exception exception = new Exception(e.getMessage());
         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) chunkContext
               .getStepContext().getStepExecution().getJobExecution()
               .getExecutionContext().get(Constantes.DOC_EXCEPTION);
         exceptions.add(exception);

      } catch (CaptureMasseRuntimeException e) {
         final Exception exception = new Exception(e.getMessage());
         ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) chunkContext
               .getStepContext().getStepExecution().getJobExecution()
               .getExecutionContext().get(Constantes.DOC_EXCEPTION);
         exceptions.add(exception);
      } 

      return RepeatStatus.FINISHED;
   }
}
