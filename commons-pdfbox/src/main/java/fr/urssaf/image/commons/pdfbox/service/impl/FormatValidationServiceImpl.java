package fr.urssaf.image.commons.pdfbox.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.exception.SyntaxValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.commons.pdfbox.service.FormatValidationService;

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

      final List<String> result = new ArrayList<String>();

      ValidationResult pdfboxResult;
      try {

         PreflightParser parser = new PreflightParser(file);
         
         /*
          * Parse the PDF file with PreflightParser that inherits from the
          * NonSequentialParser. Some additional controls are present to check a
          * set of PDF/A requirements. (Stream length consistency, EOL after
          * some Keyword...)
          */
         parser.parse();

         /*
          * Once the syntax validation is done, the parser can provide a
          * PreflightDocument (that inherits from PDDocument) This document
          * process the end of PDF/A validation.
          */
         final PreflightDocument document = parser.getPreflightDocument();
         document.validate();

         // Get validation result
         pdfboxResult = document.getResult();
         document.close();

      } catch (SyntaxValidationException e) {
         /*
          * the parse method can throw a SyntaxValidationExceptionif the PDF
          * file can't be parsed.
          */
         // In this case, the exception contains an instance of ValidationResult
         pdfboxResult = e.getResult();
         
      } catch (IOException e) {
         LOGGER.debug("Exception levée lors de la validation du fichier {} : {}",
               file.getAbsolutePath(), e);
         throw new FormatValidationException(e.getMessage(), e);
      } catch (RuntimeException e) {
         LOGGER.debug("Exception levée lors de la validation du fichier {} : {}",
               file.getAbsolutePath(), e);
         throw new FormatValidationException(e.getMessage(), e);
      }

      // Construit la liste des erreurs de validation dans l'objet résultat
      if (!pdfboxResult.isValid()) {

         // Ajoute un 1er message générique
         result.add("Le document est n'est pas un PDF/A conforme");

         // Ajoute les erreurs remontées par PDFBox
         for (ValidationError error : pdfboxResult.getErrorsList()) {
            result.add(String.format("%s : %s", error.getErrorCode(), error
                  .getDetails()));
         }

         // Une trace de debug
         LOGGER
               .debug(
                     "Le fichier '{}' n'a pas passé la validation PDFBox du PDF/A. Détails : {}",
                     file.getAbsolutePath(), result);

      }

      // Renvoie du résultat
      return result;

   }

}
