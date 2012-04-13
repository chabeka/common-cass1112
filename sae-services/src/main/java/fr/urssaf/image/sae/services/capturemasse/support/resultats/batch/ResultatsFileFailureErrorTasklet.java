/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecBloquantSupport;
import fr.urssaf.image.sae.services.util.XmlValidationUtils;

/**
 * Tasklet décriture du fichier resultats.xml en cas d'erreur bloquante
 * 
 */
@Component
public class ResultatsFileFailureErrorTasklet implements Tasklet {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileFailureErrorTasklet.class);

   @Autowired
   private ResultatsFileEchecBloquantSupport support;

   private static final String RESULTATS_XSD = "xsd_som_res/resultats.xsd";

   @Autowired
   private ApplicationContext applContext;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final StepContext stepContext = chunkContext.getStepContext();
      final ExecutionContext context = stepContext.getStepExecution()
            .getJobExecution().getExecutionContext();

      @SuppressWarnings("unchecked")
      final ConcurrentLinkedQueue<Exception> erreurs = (ConcurrentLinkedQueue<Exception>) context
            .get(Constantes.DOC_EXCEPTION);

      Exception erreur = erreurs.toArray(new Exception[0])[0];

      LOGGER.error(erreur.getMessage());

      final String sommairePath = context.getString(Constantes.SOMMAIRE_FILE);

      if (StringUtils.isNotBlank(sommairePath)) {

         final File sommaireFile = new File(sommairePath);
         File ecdeDirectory = sommaireFile.getParentFile();
         support.writeResultatsFile(ecdeDirectory, erreur);

         File resultats = new File(ecdeDirectory, "resultats.xml");

         try {
            Resource sommaireXSD = applContext.getResource(RESULTATS_XSD);
            URL xsdSchema = sommaireXSD.getURL();

            XmlValidationUtils.parse(resultats, xsdSchema);
         } catch (IOException ioExcept) {
            LOGGER.error("Erreur lors de la validation XSD", ioExcept);
         } catch (ParserConfigurationException parseExcept) {
            LOGGER.error("Erreur lors de la validation XSD", parseExcept);
         } catch (SAXException saxExcept) {
            LOGGER.error("Erreur lors de la validation XSD", saxExcept);
         }
      }

      return RepeatStatus.FINISHED;
   }
}
