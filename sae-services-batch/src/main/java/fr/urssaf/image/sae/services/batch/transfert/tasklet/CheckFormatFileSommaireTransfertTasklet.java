package fr.urssaf.image.sae.services.batch.transfert.tasklet;

import java.io.File;

import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.batch.CheckFormatFileSommaireTasklet;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Tasklet de vérification du format de fichier sommaire.xml
 * 
 */
@Component
public class CheckFormatFileSommaireTransfertTasklet extends CheckFormatFileSommaireTasklet{
   
   @Override
   protected void validationSpecifiqueSommaire(final File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException {
      LOGGER.debug("{} - Début de validation du BATCH_MODE du sommaire.xml",
            TRC_EXEC);

      validationSupport.validerModeBatch(sommaireFile,
            Constantes.BATCH_MODE.PARTIEL.getModeNom());

      LOGGER.debug("{} - Fin de validation du BATCH_MODE du sommaire.xml",
            TRC_EXEC);
      
      LOGGER.debug("{} - Debut de validation du DocumentTypeMultiAction du sommaire.xml",
            TRC_EXEC);
      
      validationSupport.validationDocumentTypeMultiActionSommaire(sommaireFile);
      
      LOGGER.debug("{} - Debut de validation du DocumentTypeMultiAction du sommaire.xml",
            TRC_EXEC);
      
      LOGGER.debug("{} - Début de validation spécifique de la présence de l'uuid du fichier",
            TRC_EXEC);
      validationSupport.validationDocumentBaliseRequisSommaire(sommaireFile, "UUID");    
      LOGGER.debug("{} - Fin de validation spécifique de la présence de l'uuid du fichier",
            TRC_EXEC);
      
      LOGGER.debug("{} - Début de validation spécifique de la présence du type action dans document",
            TRC_EXEC);
      validationSupport.validationDocumentBaliseRequisSommaire(sommaireFile, "typeAction");    
      LOGGER.debug("{} - Fin de validation spécifique de la présence du type action dans document",
            TRC_EXEC);
      
      LOGGER.debug("{} - Début de validation unicité uuid des documents",
            TRC_EXEC);
      
      validationSupport.validerUniciteUuid(sommaireFile);
      
      LOGGER.debug("{} - Fin de validation unicité uuid des documents",
            TRC_EXEC);
   }
  

}
