package fr.urssaf.image.commons.pdfbox.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;

import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.exception.SyntaxValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.commons.pdfbox.service.FormatValidationService;

/**
 * Service de validation d'un format de fichier
 */
@Service
public final class FormatValidationServiceImpl implements
      FormatValidationService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(FormatValidationServiceImpl.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> validate(File file) throws FormatValidationException {

      PreflightParser parser = buildPreflightParser(file);

      return validate(parser);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> validate(DataSource dataSource)
         throws FormatValidationException {

      PreflightParser parser = buildPreflightParser(dataSource);

      return validate(parser);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> validate(InputStream inputStream)
         throws FormatValidationException {

      PreflightParser parser = buildPreflightParser(inputStream);

      return validate(parser);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<String> validate(byte[] data) throws FormatValidationException {

      PreflightParser parser = buildPreflightParser(data);

      return validate(parser);

   }

   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   private List<String> validate(PreflightParser parser)
         throws FormatValidationException {

      List<String> result = new ArrayList<String>();

      ValidationResult pdfboxResult;
      try {

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
         LOGGER.debug("Exception levée lors de la validation PDF/A : {}", e);
         throw new FormatValidationException(e);
      } catch (RuntimeException e) {
         LOGGER.debug("Exception levée lors de la validation PDF/A : {}", e);
         throw new FormatValidationException(e);
      }

      // Construit la liste des erreurs de validation dans l'objet résultat
      if (!pdfboxResult.isValid()) {

         // Ajoute un 1er message générique
         //result.add("Le document est n'est pas un PDF/A conforme");

         // Ajoute les erreurs remontées par PDFBox
         for (ValidationError error : pdfboxResult.getErrorsList()) {
            result.add(String.format("%s : %s", error.getErrorCode(), error
                  .getDetails()));
         }

         // Une trace de debug
         LOGGER.debug("Non conforme PDF/A. Détails : {}", result);

      }

      // Renvoie du résultat
      return result;

   }

   private PreflightParser buildPreflightParser(DataSource dataSource)
         throws FormatValidationException {
      try {
         return new PreflightParser(dataSource);
      } catch (IOException ex) {
         throw new FormatValidationException(ex);
      }
   }

   private PreflightParser buildPreflightParser(File file)
         throws FormatValidationException {
      try {
         return new PreflightParser(file);
      } catch (IOException ex) {
         throw new FormatValidationException(ex);
      }
   }

   private PreflightParser buildPreflightParser(InputStream stream)
         throws FormatValidationException {
      try {
         DataSource dataSource = new ByteArrayDataSource(stream, null);
         return buildPreflightParser(dataSource);
      } catch (IOException ex) {
         throw new FormatValidationException(ex);
      }
   }

   private PreflightParser buildPreflightParser(byte[] data)
         throws FormatValidationException {
      DataSource dataSource;
      try {
         dataSource = new ByteArrayDataSource(data, null);
         return buildPreflightParser(dataSource);
      } catch (IOException ex) {
         throw new FormatValidationException(ex);
      }
   }

}
