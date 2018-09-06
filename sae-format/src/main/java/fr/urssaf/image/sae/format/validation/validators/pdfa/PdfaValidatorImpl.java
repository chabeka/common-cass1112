package fr.urssaf.image.sae.format.validation.validators.pdfa;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.commons.pdfbox.service.FormatValidationService;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;
import fr.urssaf.image.sae.format.validation.validators.Validator;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/**
 * Implémentation des appels à l'outil validation.
 */
@Service
public class PdfaValidatorImpl implements Validator {

   private static final String LOG_DEBUT = "{} - Début";
   private static final String LOG_FIN = "{} - Fin";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(PdfaValidatorImpl.class);

   @Autowired
   private FormatValidationService formatValidationService;

   /**
    * {@inheritDoc}
    */
   @Override
   public final ValidationResult validateFile(File file)
         throws FormatValidationException, ValidatorUnhandledException {

      // Traces debug - entrée méthode
      String prefixeTrc = "validateFile()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      LOGGER.debug("{} - Demande de validation du fichier \"{}\"", prefixeTrc,
            file.getAbsolutePath());

      ValidationResult validResult;
      List<String> erreursValidation;
      try {

         // Appel à commons-pdfbox pour la validation d'un fichier
         // la liste est vide si le fichier est valide
         // sinon la liste contient les anomalies

         // attention cette méthode ne renvoit jamais null, minimum liste vide
         LOGGER.debug("{} - Appel à PdfBox", prefixeTrc);
         erreursValidation = formatValidationService.validate(file);

      } catch (Throwable throwable) {
         throw new ValidatorUnhandledException(throwable);
      }

      // Construit l'objet de résultat selon s'il y a eu anomalies ou non
      validResult = buildValidationResult(erreursValidation);
      LOGGER.debug("{} - Résultat de la validation: {}", prefixeTrc,
            validResult.isValid());

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // Renvoie le résultat de la validation
      return validResult;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ValidationResult validateStream(InputStream stream)
         throws FormatValidationException, ValidatorUnhandledException {

      // Traces debug - entrée méthode
      String prefixeTrc = "validateStream()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);
      LOGGER.debug("{} - Demande de validation d'un flux", prefixeTrc);

      ValidationResult validResult;
      List<String> erreursValidation;
      try {

         // Appel à commons-pdfbox pour la validation d'un fichier
         // la liste est vide si le fichier est valide
         // sinon la liste contient les anomalies

         // attention cette méthode ne renvoit jamais null, minimum liste vide
         LOGGER.debug("{} - Appel à PdfBox", prefixeTrc);
         erreursValidation = formatValidationService.validate(stream);

      } catch (Throwable throwable) {
         throw new ValidatorUnhandledException(throwable);
      }

      // Construit l'objet de résultat selon s'il y a eu anomalies ou non
      validResult = buildValidationResult(erreursValidation);
      LOGGER.debug("{} - Résultat de la validation: {}", prefixeTrc,
            validResult.isValid());

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // Renvoie le résultat de la validation
      return validResult;

   }

   private ValidationResult buildValidationResult(List<String> anomalies) {

      // Construit l'objet de résultat selon s'il y a eu anomalies ou non

      ValidationResult validResult;

      if (anomalies.isEmpty()) {
         // pas d'anomalie
         validResult = new ValidationResult(true, null);
      } else {
         // anomalies
         validResult = new ValidationResult(false, anomalies);
      }

      return validResult;

   }

}
