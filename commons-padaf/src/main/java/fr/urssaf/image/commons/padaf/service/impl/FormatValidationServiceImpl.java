package fr.urssaf.image.commons.padaf.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.FileDataSource;

import net.padaf.preflight.PdfA1bValidator;
import net.padaf.preflight.PdfAValidatorFactory;
import net.padaf.preflight.ValidationException;
import net.padaf.preflight.ValidationResult;
import net.padaf.preflight.ValidationResult.ValidationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.padaf.exception.FormatValidationException;
import fr.urssaf.image.commons.padaf.service.FormatValidationService;


/**
 * Service de validation d'un format de fichier
 */
public final class FormatValidationServiceImpl implements
      FormatValidationService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(FormatValidationServiceImpl.class);

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public List<String> validate(final File file) throws FormatValidationException {

      List<String> result = new ArrayList<String>();
      
      PdfA1bValidator validator;
      try {
         validator = new PdfA1bValidator(PdfAValidatorFactory
               .getStandardPDFA1BConfiguration());
      } catch (ValidationException e) {
         throw new FormatValidationException(e.getMessage(), e);
      }
      
      FileDataSource fileDataSource = new FileDataSource(file);
      
      ValidationResult padafResult = null;
      try {
         
         padafResult = validator.validate(fileDataSource);
         
         // Construit la liste des erreurs de validation dans l'objet résultat
         if (!padafResult.isValid()) {

            // Ajoute un 1er message générique
            result.add("Le document est n'est pas un PDF/A conforme");

            // Ajoute les erreurs remontées par PDFBox
            for (ValidationError error : padafResult.getErrorsList()) {
               result.add(String.format("%s : %s", error.getErrorCode(), error
                     .getDetails()));
            }

            // Une trace de debug
            LOGGER
                  .debug(
                        "Le fichier '{}' n'a pas passé la validation Padaf du PDF/A. Détails : {}",
                        file.getAbsolutePath(), result);

         }
         
         
      } catch (ValidationException e) {
         LOGGER.debug("Exception levée lors de la validation du fichier {} : {}",
               file.getAbsolutePath(), e);
         throw new FormatValidationException(e.getMessage(), e);
         
      } catch (RuntimeException e) {
         LOGGER.debug("Exception levée lors de la validation du fichier {} : {}",
               file.getAbsolutePath(), e);
         throw new FormatValidationException(e.getMessage(), e);
      }
      finally {
         if (padafResult!=null) {
            padafResult.closePdf();
         }
      }
      
      // Renvoie du résultat
      return result;

   }

}
