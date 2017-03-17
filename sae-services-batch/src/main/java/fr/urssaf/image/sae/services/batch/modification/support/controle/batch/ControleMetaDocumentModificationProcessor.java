/**
 * 
 */
package fr.urssaf.image.sae.services.batch.modification.support.controle.batch;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.modification.support.controle.ModificationMasseControleSupport;
import fr.urssaf.image.sae.services.batch.modification.support.controle.model.ModificationMasseControlResult;

/**
 * Item processor pour le contrôle des documents du fichier sommaire.xml pour le
 * service de modification en masse
 * 
 */
@Component
public class ControleMetaDocumentModificationProcessor extends AbstractListener
      implements ItemProcessor<UntypedDocument, UntypedDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ControleMetaDocumentModificationProcessor.class);

   @Autowired
   private ModificationMasseControleSupport support;

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

      String uuidString = item.getUuid().toString();

      ModificationMasseControlResult resultat = null;

      try {
         resultat = support.controleSAEDocumentMetadatas(item, ecdeDirectory);
      } catch (Exception e) {
         if (isModePartielBatch()) {
            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            final String message = e.getMessage();
            getExceptionErreurListe().add(new Exception(message));
         } else {
            throw e;
         }
      }
      

      if (resultat != null && resultat.getStorageMetadatasList() != null) {
         LOGGER
               .debug(
                     "{} - Récupération de la map des résultat de controle pour la modification",
                     trcPrefix);
         Map<String, ModificationMasseControlResult> map = (Map<String, ModificationMasseControlResult>) getStepExecution()
               .getJobExecution().getExecutionContext().get(
                     "mapModificationControlResult");
         if (map == null) {
            LOGGER.debug("{} - Pas de map de résultat de controle, on la créé",
                  trcPrefix);
            map = new HashMap<String, ModificationMasseControlResult>();
         }
         LOGGER.debug(
               "{} - Ajout du résultat de controle dans la map avec la key : {}",
               trcPrefix, uuidString);
         map.put(uuidString, resultat);
         getStepExecution().getJobExecution().getExecutionContext().put(
               "mapModificationControlResult", map);
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
      getStepExecution().getExecutionContext().put(Constantes.CTRL_INDEX, -1);
   }

   /**
    * Action exécutée avant chaque process
    * 
    * @param untypedType
    *           le document
    */
   @BeforeProcess
   public final void beforeProcess(final JAXBElement<Object> untypedType) {

      ExecutionContext context = getStepExecution().getExecutionContext();

      int valeur = context.getInt(Constantes.CTRL_INDEX);
      valeur++;

      context.put(Constantes.CTRL_INDEX, valeur);

   }
}
