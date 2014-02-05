package fr.urssaf.image.sae.format.validation.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.util.concurrent.UncheckedExecutionException;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.sae.format.context.SaeFormatApplicationContext;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;
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
    * Définition de caches permettant de ne pas recharger à chaque fois les
    * services à partir de l'applicationContext
    */
   private final LoadingCache<String, Validator> validators;

   /**
    * Service permettant d’interroger le référentiel des formats
    */
   private ReferentielFormatService referentielFormatService;

   private ApplicationContext applicationContext;

   /**
    * Contructeur
    * 
    * @param referentielFormatService
    *           le service de référentiel des formats
    * @param cacheDuration
    *           la durée de cache pour les formats
    */
   @Autowired
   public ValidationServiceImpl(
         ReferentielFormatService referentielFormatService,
         @Value("${sae.referentiel.format.cache}") int cacheDuration,
         ApplicationContext applicationContext) {
      this.referentielFormatService = referentielFormatService;
      this.applicationContext = applicationContext;
      // Mise en cache
      validators = CacheBuilder.newBuilder().refreshAfterWrite(cacheDuration,
            TimeUnit.MINUTES).build(new CacheLoader<String, Validator>() {

         @Override
         public Validator load(String identifiant) {
            return ValidationServiceImpl.this.applicationContext.getBean(
                  identifiant, Validator.class);
         }
      });
   }

   @Override
   public final ValidationResult validateFile(String idFormat, File file)
         throws UnknownFormatException, ValidatorInitialisationException {

      ValidationResult validResult;

      try {
         // On récupère le validateur à utiliser pour l’IdFormat donnée
         // à partir du référentiel des formats.
         FormatFichier refFormat = referentielFormatService.getFormat(idFormat);

         // On utilise l’application contexte pour récupérer une instance du
         // validateur
         Validator validator = validators
               .getUnchecked(refFormat.getValidator());

         // On appel la méthode validateFile(file)
         validResult = validator.validateFile(file);

      } catch (InvalidCacheLoadException except) {
         throw new ValidatorInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.init.validator"), except);

      } catch (UncheckedExecutionException except) {
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
