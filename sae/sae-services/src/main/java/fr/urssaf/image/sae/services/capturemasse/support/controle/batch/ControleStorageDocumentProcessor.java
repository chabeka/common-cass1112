/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle.batch;

import java.io.File;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.controle.CaptureMasseControleSupport;

/**
 * ItemProcessor pour le contrôle des documents au stockage
 * 
 */
@Component
public class ControleStorageDocumentProcessor implements
      ItemProcessor<SAEDocument, SAEDocument> {

   @Autowired
   private CaptureMasseControleSupport support;

   private StepExecution stepExecution;

   /**
    * Méthode réalisée avant le step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(StepExecution stepExecution) {
      this.stepExecution = stepExecution;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final SAEDocument process(final SAEDocument item) throws Exception {

      support.controleSAEDocumentStockage(item);

      String pathSommaire = stepExecution.getJobExecution()
            .getExecutionContext().getString(Constantes.SOMMAIRE_FILE);

      File sommaireFile = new File(pathSommaire);
      File ecdeDirectory = sommaireFile.getParentFile();

      String path = ecdeDirectory.getAbsolutePath() + File.separator
            + "documents" + File.separator + item.getFilePath();
      item.setFilePath(path);

      return item;
   }

}
