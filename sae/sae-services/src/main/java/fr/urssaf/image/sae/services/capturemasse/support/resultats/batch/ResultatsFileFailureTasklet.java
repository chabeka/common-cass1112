/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
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
import fr.urssaf.image.sae.services.capturemasse.model.CaptureMasseIntegratedDocument;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.capturemasse.support.stockage.multithreading.InsertionPoolThreadExecutor;
import fr.urssaf.image.sae.services.util.XmlValidationUtils;

/**
 * Tasklet pour l'écriture du fichier resultats.xml lors d'un échec de
 * traitement
 * 
 */
@Component
public class ResultatsFileFailureTasklet implements Tasklet {

   private static final String LIBELLE_BUL003 = "La capture de masse en mode "
         + "\"Tout ou rien\" a été interrompue. Une procédure d'exploitation a été "
         + "initialisée pour supprimer les données qui auraient pu être stockées.";

   @Autowired
   private ResultatsFileEchecSupport support;

   private static final String RESULTATS_XSD = "xsd_som_res/resultats.xsd";

   @Autowired
   private SommaireFormatValidationSupport validationSupport;

   @Autowired
   private ApplicationContext applContext;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ResultatsFileFailureTasklet.class);

   // Pool d'execution des insertions de documents
   @Autowired
   private InsertionPoolThreadExecutor executor;
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      final Map<String, Object> map = chunkContext.getStepContext()
            .getJobExecutionContext();
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) map
            .get(Constantes.CODE_EXCEPTION);
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) map
            .get(Constantes.INDEX_EXCEPTION);
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) map
            .get(Constantes.DOC_EXCEPTION);

      CaptureMasseErreur erreur = new CaptureMasseErreur();
      erreur.setListCodes(Arrays.asList(codes.toArray(new String[0])));
      erreur.setListException(Arrays.asList(exceptions
            .toArray(new Exception[0])));
      erreur.setListIndex(Arrays.asList(index.toArray(new Integer[0])));

      final String pathSommaire = (String) map.get(Constantes.SOMMAIRE_FILE);
      File sommaireFile = new File(pathSommaire);

      /*
       * On vérifie qu'on est mode tout ou rien et qu'il reste des rollback à
       * effectuer. Si c'est le cas on change le code possible fonctionnel en
       * code technique et le message correspondant
       */
      boolean isModeToutOuRien = true;
      try {
         validationSupport.validerModeBatch(sommaireFile, "TOUT_OU_RIEN");
      } catch (Exception e) {
         isModeToutOuRien = false;
      }

      ConcurrentLinkedQueue<CaptureMasseIntegratedDocument> listIntDocs = executor.getIntegratedDocuments();

      if (isModeToutOuRien && CollectionUtils.isNotEmpty(listIntDocs)) {
         erreur.getListCodes().set(0, Constantes.ERR_BUL003);
         erreur.getListException().set(0, new Exception(LIBELLE_BUL003));
      }

      final File ecdeDirectory = sommaireFile.getParentFile();

      int nbreDocs = (Integer) map.get(Constantes.DOC_COUNT);

      support.writeResultatsFile(ecdeDirectory, sommaireFile, erreur, nbreDocs);

      File resultats = new File(ecdeDirectory, "resultats.xml");

      try {
         Resource sommaireXSD = applContext.getResource(RESULTATS_XSD);
         URL xsdSchema = sommaireXSD.getURL();

         XmlValidationUtils.parse(resultats, xsdSchema);
      } catch (IOException ioExcept) {
         LOGGER.warn("Erreur lors de la validation XSD", ioExcept);
      } catch (ParserConfigurationException parseExcept) {
         LOGGER.warn("Erreur lors de la validation XSD", parseExcept);
      } catch (SAXException saxExcept) {
         LOGGER.warn("Erreur lors de la validation XSD", saxExcept);
      }

      return RepeatStatus.FINISHED;
   }
}
