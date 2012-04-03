/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.services.capturemasse.common.CaptureMasseErreur;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.util.XmlValidationUtils;

/**
 * Tasklet pour l'écriture du fichier resultats.xml lors d'un échec de
 * traitement
 * 
 */
@Component
public class ResultatsFileFailureTasklet implements Tasklet {

   @Autowired
   private ResultatsFileEchecSupport support;

   private static final String RESULTATS_XSD = "xsd_som_res/resultats.xsd";

   @Autowired
   private ApplicationContext applContext;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileFailureTasklet.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final Map<String, Object> map = chunkContext.getStepContext()
            .getJobExecutionContext();
      @SuppressWarnings("unchecked")
      List<String> codes = (List<String>) map.get(Constantes.CODE_EXCEPTION);
      @SuppressWarnings("unchecked")
      List<Integer> index = (List<Integer>) map.get(Constantes.INDEX_EXCEPTION);
      @SuppressWarnings("unchecked")
      List<Exception> exceptions = (List<Exception>) map
            .get(Constantes.DOC_EXCEPTION);

      CaptureMasseErreur erreur = new CaptureMasseErreur();
      erreur.setListCodes(codes);
      erreur.setListException(exceptions);
      erreur.setListIndex(index);

      final String pathSommaire = (String) map.get(Constantes.SOMMAIRE_FILE);
      File sommaireFile = new File(pathSommaire);

      final File ecdeDirectory = sommaireFile.getParentFile();

      int nbreDocs = (Integer) map.get(Constantes.DOC_COUNT);

      support.writeResultatsFile(ecdeDirectory, sommaireFile, erreur, nbreDocs);

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

      return RepeatStatus.FINISHED;
   }

}
