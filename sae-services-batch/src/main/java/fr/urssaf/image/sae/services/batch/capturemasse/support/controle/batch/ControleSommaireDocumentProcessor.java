/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.model.CaptureMasseControlResult;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Item processor pour le contrôle des documents du fichier sommaire.xml
 * 
 */
@Component
public class ControleSommaireDocumentProcessor extends AbstractListener
      implements ItemProcessor<UntypedDocument, UntypedDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleSommaireDocumentProcessor.class);

   @Autowired
   private CaptureMasseControleSupport support;

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public final UntypedDocument process(final UntypedDocument item)
         throws Exception {
      String trcPrefix = "process";
      LOGGER.debug("{} - début", trcPrefix);

      final String path = (String) getStepExecution().getJobExecution()
            .getExecutionContext().get(Constantes.SOMMAIRE_FILE);

      final File sommaire = new File(path);

      final File ecdeDirectory = sommaire.getParentFile();

      String cheminDocOnEcde = ecdeDirectory.getAbsolutePath() + File.separator
            + "documents" + File.separator + item.getFilePath();

      CaptureMasseControlResult resultat = support.controleSAEDocument(item,
            ecdeDirectory);

      if (resultat.isIdentificationActivee() || resultat.isValidationActivee()) {
         // on essaye de récupérer la map contenant tous les résultats de controle
         // des documents
         // si cette map n'existe pas, on la cree et on ajout le résultat du
         // controle pour ce document
         // ensuite, on met la map dans le contexte d'execution spring batch pour
         // pouvoir récupérer le résultat
         // du controle lors de l'archivage
         LOGGER
               .debug(
                     "{} - Récupération de la map des résultat de controle pour la capture",
                     trcPrefix);
         Map<String, CaptureMasseControlResult> map = (Map<String, CaptureMasseControlResult>) getStepExecution()
               .getJobExecution().getExecutionContext().get(
                     "mapCaptureControlResult");
         if (map == null) {
            LOGGER.debug("{} - Pas de map de résultat de controle, on la créé",
                  trcPrefix);
            map = new HashMap<String, CaptureMasseControlResult>();
         }
         LOGGER.debug(
               "{} - Ajout du résultat de controle dans la map avec la key : {}",
               trcPrefix, cheminDocOnEcde);
         map.put(cheminDocOnEcde, resultat);
         getStepExecution().getJobExecution().getExecutionContext().put(
               "mapCaptureControlResult", map);
      }

      LOGGER.debug("{} - fin", trcPrefix);

      return item;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ExitStatus specificAfterStepOperations() {
      return getStepExecution().getExitStatus();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void specificInitOperations() {
      // rien à faire
   }
}
