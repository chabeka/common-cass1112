package fr.urssaf.image.sae.format.validation.validators.pdfa;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.commons.pdfbox.service.FormatValidationService;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;
import fr.urssaf.image.sae.format.validation.validators.Validator;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/**
 * Implémentation des appels à l’outil validation.
 */
@Service
public class PdfaValidatorImpl implements Validator {

   @Autowired
   private FormatValidationService formatValidationService;

   @Override
   public final ValidationResult validateFile(File file)
         throws FormatValidationException, ValidatorUnhandledException {

      ValidationResult validResult;

      try {
         // Appel à COMMONS-PDFBOX pour la validation d'un fichier
         // la liste est vide si le fichier est valide
         // sinon la liste contient les anomalies
   
         // attention cette méthode ne renvoit jamais null, minimum liste vide
         List<String> listFormatValid = formatValidationService.validate(file);
   
         // pas d'anomalies
         if (listFormatValid.isEmpty()) {
            validResult = new ValidationResult(true, null);
         }
         // anomalies
         else {
            validResult = new ValidationResult(false, listFormatValid);
         }
      } catch (Throwable throwable) {
         throw new ValidatorUnhandledException(throwable);
      }
      return validResult;
   }

   @Override
   public final ValidationResult validateStream(InputStream stream)
         throws FormatValidationException, ValidatorUnhandledException {

      ValidationResult validResult;

      try {
         // Appel à COMMONS-PDFBOX pour la validation d'un fichier
         // la liste est vide si le fichier est valide
         // sinon la liste contient les anomalies
   
         // attention cette méthode ne renvoit jamais null, minimum liste vide
         List<String> listFormatValid = formatValidationService.validate(stream);
   
         // pas d'anomalie
         if (listFormatValid.isEmpty()) {
            validResult = new ValidationResult(true, null);
         }
         // anomalie
         else {
            validResult = new ValidationResult(false, listFormatValid);
         }
      } catch (Throwable throwable) {
         throw new ValidatorUnhandledException(throwable);
      }

      return validResult;

   }

}
