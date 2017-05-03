/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.controle.batch;

import java.io.File;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.listener.AbstractListener;
import fr.urssaf.image.sae.services.batch.capturemasse.support.controle.CaptureMasseControleSupport;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * ItemProcessor pour le contrôle des documents au stockage
 * 
 */
@Component
public class ControleStorageDocumentProcessor extends AbstractListener
      implements ItemProcessor<SAEDocument, SAEDocument> {

   @Autowired
   private CaptureMasseControleSupport support;

   /**
    * {@inheritDoc}
    */
   @Override
   public final SAEDocument process(final SAEDocument item) throws Exception {

      try {
         // Si il y a déjà eu une erreur sur un document en mode partiel, on ne
         // cherche pas à continuer sur ce document
         if (!(isModePartielBatch()
               && getIndexErreurListe().contains(getStepExecution().getExecutionContext().getInt(
                     Constantes.CTRL_INDEX)))) {
            support.controleSAEDocumentStockage(item);

            String pathSommaire = getStepExecution().getJobExecution()
                  .getExecutionContext().getString(Constantes.SOMMAIRE_FILE);

            File sommaireFile = new File(pathSommaire);
            File ecdeDirectory = sommaireFile.getParentFile();

            String path = ecdeDirectory.getAbsolutePath() + File.separator
                  + "documents" + File.separator + item.getFilePath();
            item.setFilePath(path);
         }
      } catch (Exception e) {
         if (isModePartielBatch()) {

            getCodesErreurListe().add(Constantes.ERR_BUL002);
            getIndexErreurListe().add(
                  getStepExecution().getExecutionContext().getInt(
                        Constantes.CTRL_INDEX));
            getExceptionErreurListe().add(new Exception(e.getMessage()));

         } else {
            throw e;
         }

      }
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
