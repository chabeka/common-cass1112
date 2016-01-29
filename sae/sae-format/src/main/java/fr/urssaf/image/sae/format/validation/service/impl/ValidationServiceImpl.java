package fr.urssaf.image.sae.format.validation.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;
import fr.urssaf.image.sae.format.validation.exceptions.ValidationRuntimeException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;
import fr.urssaf.image.sae.format.validation.service.ValidationService;
import fr.urssaf.image.sae.format.validation.validators.Validator;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/***
 * Implémentation du processus de validation d'un flux ou d'un fichier.
 * 
 */
@Service
public final class ValidationServiceImpl implements ValidationService {

   private static final String LOG_DEBUT = "{} - Début";
   private static final String LOG_FIN = "{} - Fin";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ValidationServiceImpl.class);

   /**
    * Définition de caches permettant de ne pas recharger à chaque fois les
    * services à partir de l'applicationContext
    */
   private final LoadingCache<String, Validator> validators;

   /**
    * Service permettant d’interroger le référentiel des formats
    */
   private final ReferentielFormatService referentielFormatService;

   private final ApplicationContext applicationContext;

   /**
    * Contructeur
    * 
    * @param referentielFormatService
    *           le service de référentiel des formats
    * @param cacheDuration
    *           la durée de cache pour les formats
    * @param initCacheOnStartup
    *           flag indiquant si initialise le cache au demarrage du serveur d'application
    * @param applicationContext
    *           Le contexte Spring
    */
   @Autowired
   public ValidationServiceImpl(
         ReferentielFormatService referentielFormatService,
         @Value("${sae.referentiel.format.cache}") int cacheDuration,
         @Value("${sae.referentiel.format.initCacheOnStartup}") boolean initCacheOnStartup,
         ApplicationContext applicationContext) {

      this.referentielFormatService = referentielFormatService;
      this.applicationContext = applicationContext;

      // Gestion d'un cache contenant les beans de validation
      validators = CacheBuilder.newBuilder().refreshAfterWrite(cacheDuration,
            TimeUnit.MINUTES).build(new CacheLoader<String, Validator>() {

         @Override
         public Validator load(String identifiant) {
            LOGGER
                  .debug(
                        "Charge dans le cache l'objet de validation dont le nom du bean est \"{}\"",
                        identifiant);
            return ValidationServiceImpl.this.applicationContext.getBean(
                  identifiant, Validator.class);
         }
      });
      
      if (initCacheOnStartup) {
         populateCache();
      }
   }
   
   private void populateCache() {
      // initialisation du cache des identificateurs
      List<FormatFichier> allFormat = referentielFormatService.getAllFormat();
      for (FormatFichier format : allFormat) {
         if (StringUtils.isNotEmpty(format.getValidator())) {
            Validator validateur = ValidationServiceImpl.this.applicationContext.getBean(
                  format.getValidator(), Validator.class);
            if (validateur != null) {
               validators.put(format.getValidator(), validateur);
            }
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ValidationResult validateFile(String idFormat, File file)
         throws UnknownFormatException, ValidatorInitialisationException,
         ValidatorUnhandledException {

      // Traces debug - entrée méthode
      String prefixeTrc = "validateFile()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // On récupère le nom du bean qui doit réaliser la validation
      String nomBeanValidator = lectureNomBeanValidator(idFormat);

      // On récupère le bean correspondant
      Validator validator = recupereBeanValidator(nomBeanValidator);

      // On appel la méthode identifyFile en passant en paramètre le
      // fichier et l'idFormat
      LOGGER.debug("{} - Exécution de la validation", prefixeTrc);
      ValidationResult validResult;
      try {
         validResult = validator.validateFile(file);
      } catch (FormatValidationException except) {
         throw new ValidationRuntimeException(except.getMessage(), except);
      }
      LOGGER.debug("{} - La validation a été réalisée. Résultat: {}",
            prefixeTrc, validResult.isValid());

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // En retour on récupère et on renvoi un objet ValidationResult
      // contenant le résultat de la validation et une liste des traces.
      return validResult;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ValidationResult validateStream(String idFormat, InputStream stream)
         throws UnknownFormatException, ValidatorInitialisationException,
         IOException, ValidatorUnhandledException {

      // Traces debug - entrée méthode
      String prefixeTrc = "validateStream()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // On récupère le nom du bean qui doit réaliser la validation
      String nomBeanValidator = lectureNomBeanValidator(idFormat);

      // On récupère le bean correspondant
      Validator validator = recupereBeanValidator(nomBeanValidator);

      // On appel la méthode identifyFile en passant en paramètre le
      // fichier et l'idFormat
      LOGGER.debug("{} - Exécution de la validation", prefixeTrc);
      ValidationResult validResult;
      try {
         validResult = validator.validateStream(stream);
      } catch (FormatValidationException except) {
         throw new ValidationRuntimeException(except.getMessage(), except);
      }
      LOGGER.debug("{} - La validation a été réalisée. Résultat: {}",
            prefixeTrc, validResult.isValid());

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // En retour on récupère et on renvoi un objet ValidationResult
      // contenant le résultat de la validation et une liste des traces.
      return validResult;

   }

   private String lectureNomBeanValidator(String idFormat)
         throws UnknownFormatException, ValidatorInitialisationException {

      // Traces debug - entrée méthode
      String prefixeTrc = "lectureNomBeanValidator()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // Récupération de l'objet définissant le format du fichier depuis le
      // référentiel des formats
      LOGGER
            .debug(
                  "{} - Interrogation du référentiel des formats pour récupérer la définition du format {}",
                  prefixeTrc, idFormat);
      FormatFichier formatFichier;
      try {
         formatFichier = referentielFormatService.getFormat(idFormat);
      } catch (ReferentielRuntimeException except) {
         throw new ValidationRuntimeException(except.getMessage(), except);
      }

      // L'objet formatFichier est forcément non null, car sinon la méthode
      // getFormat doit lever
      // une exception UnknownFormatException. On fait tout de même une
      // vérification en cas d'anomalie
      // de fonctionnement dans la méthode getFormat
      if (formatFichier == null) {
         throw new ValidatorInitialisationException(
               String
                     .format(
                           "Erreur technique: Le format %s n'existe pas dans le référentiel des formats, et le référentiel des formats n'a pas remonté l'erreur",
                           idFormat));
      } else {

         // Cas normal si le format existe dans le référentiel

         // On lit le nom du bean de l'objet de validation
         String nomBean = formatFichier.getValidator();

         // On vérifie que le nom du bean est renseigné
         if (StringUtils.isBlank(nomBean)) {

            // Le nom du bean n'est pas renseigné => on lève une exception
            throw new ValidatorInitialisationException(
                  String
                        .format(
                              "Le nom du bean permettant de réaliser la validation du format %s n'est pas renseigné dans le référentiel des formats",
                              idFormat));

         } else {

            // Trace applicative
            LOGGER
                  .debug(
                        "{} - Le nom du bean qui va réaliser la validation est \"{}\"",
                        prefixeTrc, nomBean);

            // Traces debug - sortie méthode
            LOGGER.debug(LOG_FIN, prefixeTrc);

            // On renvoie le nom du bean
            return nomBean;

         }

      }

   }

   private Validator recupereBeanValidator(String nomBeanValidator)
         throws ValidatorInitialisationException {

      // Traces debug - entrée méthode
      String prefixeTrc = "recupereBeanValidator()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // Récupère l'objet du cache
      LOGGER
            .debug(
                  "{} - Lecture du cache pour récupérer l'objet qui va réaliser la validation",
                  prefixeTrc);
      Validator validator;
      try {
         validator = validators.getUnchecked(nomBeanValidator);
      } catch (InvalidCacheLoadException ex) {
         throw new ValidatorInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.init.validator"), ex);

      } catch (UncheckedExecutionException ex) {
         throw new ValidatorInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.init.validator"), ex);
      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // Renvoie l'objet de validation
      return validator;

   }

}
