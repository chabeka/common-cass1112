package fr.urssaf.image.sae.format.validation.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.sae.format.context.SaeFormatApplicationContext;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.impl.ReferentielFormatServiceImpl;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;
import fr.urssaf.image.sae.format.validation.exceptions.ValidationRuntimeException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.service.ValidationService;
import fr.urssaf.image.sae.format.validation.validators.Validator;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/***
 * Implémentation du processus de validation d’un flux ou d’un fichier.
 * 
 */
@Service
public class ValidationServiceImpl implements ValidationService {

   /**
    * Service permettant d’interroger le référentiel des formats
    */
   @Autowired
   private ReferentielFormatServiceImpl referentielFormatService;

   @Override
   public final ValidationResult validateFile(String idFormat, File file)
         throws UnknownFormatException, ValidatorInitialisationException,
         FileNotFoundException {

      ValidationResult validResult;

      try {
         // On récupère le validateur à utiliser pour l’IdFormat donnée
         // à partir du référentiel des formats.
         FormatFichier refFormat = referentielFormatService.getFormat(idFormat);

         // On utilise l’application contexte pour récupérer une instance du
         // validateur
         Validator validator = SaeFormatApplicationContext
               .getApplicationContext().getBean(refFormat.getValidator(),
                     Validator.class);

         // On appel la méthode validateFile(file)
         validResult = validator.validateFile(file);

      } catch (NoSuchBeanDefinitionException except) {
         throw new ValidatorInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.init.validator"), except);
      } catch (ReferentielRuntimeException except) {
         throw new ValidationRuntimeException(except.getMessage(), except);
      } catch (FormatValidationException except) {
         throw new ValidationRuntimeException(except.getMessage(), except);
      }
      return validResult; // savoir si valide-> isValid
   }

   @Override
   public final ValidationResult validateStream(String idFormat,
         InputStream stream) throws UnknownFormatException,
         ValidatorInitialisationException, IOException {

      try {
         ValidationResult identificationResult;
         File createdFile;
         createdFile = File.createTempFile(SaeFormatMessageHandler
               .getMessage("file.generated"), SaeFormatMessageHandler
               .getMessage("extension.file"));

         FileUtils.copyInputStreamToFile(stream, createdFile);

         identificationResult = validateFile(idFormat, createdFile);
         boolean suppression = createdFile.delete();
         if (!suppression) {
            throw new ValidationRuntimeException(SaeFormatMessageHandler
                  .getMessage("erreur.validation.convert.stream.to.file"));
         } else {
            return identificationResult;
         }
      } catch (IOException except) {
         throw new ValidationRuntimeException(SaeFormatMessageHandler
               .getMessage("erreur.validation.convert.stream.to.file"), except);
      } finally {
         if (stream != null) {
            stream.close();
         }
      }

   }

}
