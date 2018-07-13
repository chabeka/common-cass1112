package fr.urssaf.image.commons.padaf.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import net.padaf.preflight.PdfA1bValidator;
import net.padaf.preflight.PdfAValidatorFactory;
import net.padaf.preflight.ValidationException;
import net.padaf.preflight.ValidationResult;
import net.padaf.preflight.ValidationResult.ValidationError;

import org.apache.commons.mail.ByteArrayDataSource;
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
   public List<String> validate(File file) throws FormatValidationException {

      DataSource dataSource = buildDataSource(file);

      return validate(dataSource);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> validate(InputStream inputStream)
         throws FormatValidationException {

      DataSource dataSource = buildDataSource(inputStream);

      return validate(dataSource);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> validate(byte[] data) throws FormatValidationException {

      DataSource dataSource = buildDataSource(data);

      return validate(dataSource);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public List<String> validate(DataSource dataSource)
         throws FormatValidationException {

      List<String> result = new ArrayList<String>();

      PdfA1bValidator validator;
      try {
         validator = new PdfA1bValidator(PdfAValidatorFactory
               .getStandardPDFA1BConfiguration());
      } catch (ValidationException e) {
         throw new FormatValidationException(e.getMessage(), e);
      }

      ValidationResult padafResult = null;
      try {

         padafResult = validator.validate(dataSource);

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
            LOGGER.debug("Non conforme PDF/A. Détails : {}", result);

         }

      } catch (ValidationException e) {
         LOGGER.debug("Exception levée lors de la validation PDF/A : {}", e);
         throw new FormatValidationException(e.getMessage(), e);

      } catch (RuntimeException e) {
         LOGGER.debug("Exception levée lors de la validation PDF/A : {}", e);
         throw new FormatValidationException(e.getMessage(), e);
      } finally {
         if (padafResult != null) {
            padafResult.closePdf();
         }
      }

      // Renvoie du résultat
      return result;

   }

   private DataSource buildDataSource(InputStream inputStream)
         throws FormatValidationException {

      ByteArrayDataSource dataSource;
      try {

         dataSource = new ByteArrayDataSource(inputStream, null);

         return dataSource;

      } catch (IOException ex) {
         throw new FormatValidationException(ex);
      }

   }

   private DataSource buildDataSource(File file) {
      return new FileDataSource(file);
   }

   private DataSource buildDataSource(byte[] data)
         throws FormatValidationException {
      try {
         return new ByteArrayDataSource(data, null);
      } catch (IOException ex) {
         throw new FormatValidationException(ex);
      }
   }

}
