package fr.urssaf.image.sae.format.identification.service.impl;

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

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
import fr.urssaf.image.sae.format.identification.identifiers.Identifier;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;
import fr.urssaf.image.sae.format.identification.service.IdentificationService;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * 
 * Implémentation du processus d'identification d'un flux ou d'un fichier.
 * 
 */
@Service
public class IdentificationServiceImpl implements IdentificationService {

   private static final String LOG_DEBUT = "{} - Début";
   private static final String LOG_FIN = "{} - Fin";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(IdentificationServiceImpl.class);

   /**
    * Définition de caches permettant de ne pas recharger à chaque fois les
    * services à partir de l'applicationContext
    */
   private final LoadingCache<String, Identifier> identifiers;

   /**
    * Service permettant d'interroger le référentiel des formats
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
    * 
    */
   @Autowired
   public IdentificationServiceImpl(
         ReferentielFormatService referentielFormatService,
         @Value("${sae.referentiel.format.cache}") int cacheDuration,
         @Value("${sae.referentiel.format.initCacheOnStartup}") boolean initCacheOnStartup,
         ApplicationContext applicationContext) {

      this.referentielFormatService = referentielFormatService;
      this.applicationContext = applicationContext;

      // Gestion d'un cache contenant les beans d'identification
      identifiers = CacheBuilder.newBuilder().refreshAfterWrite(cacheDuration,
            TimeUnit.MINUTES).build(new CacheLoader<String, Identifier>() {

         @Override
         public Identifier load(String identifiant) {
            LOGGER
                  .debug(
                        "Charge dans le cache l'objet d'identification dont le nom du bean est \"{}\"",
                        identifiant);
            return IdentificationServiceImpl.this.applicationContext.getBean(
                  identifiant, Identifier.class);
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
         if (StringUtils.isNotEmpty(format.getIdentificateur())) {
            Identifier identificateur = IdentificationServiceImpl.this.applicationContext.getBean(
                  format.getIdentificateur(), Identifier.class);
            if (identificateur != null) {
               identifiers.put(format.getIdentificateur(), identificateur);
            }
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final IdentificationResult identifyFile(String idFormat, File fichier)
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      // Traces debug - entrée méthode
      String prefixeTrc = "identifyFile()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // On récupère le nom du bean qui doit réaliser l'identification
      String nomBeanIdentifier = lectureNomBeanIdentifier(idFormat);

      // On récupère le bean correspondant
      Identifier identifier = recupereBeanIdentifier(nomBeanIdentifier);

      // On appel la méthode identifyFile en passant en paramètre le
      // fichier et l'idFormat
      LOGGER.debug("{} - Exécution de l'identification", prefixeTrc);
      IdentificationResult identifResult = identifier.identifyFile(idFormat,
            fichier);
      LOGGER.debug("{} - L'identification a été réalisée. Résultat: {}",
            prefixeTrc, identifResult.isIdentified());

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // En retour on récupère et on renvoi un objet IdentificationResult
      // contenant le résultat de l'identification et une liste des traces.
      return identifResult;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final IdentificationResult identifyStream(String idFormat,
         InputStream stream, String nomFichier) throws UnknownFormatException,
         IdentifierInitialisationException {

      // Traces debug - entrée méthode
      String prefixeTrc = "identifyStream()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // On récupère le nom du bean qui doit réaliser l'identification
      String nomBeanIdentifier = lectureNomBeanIdentifier(idFormat);

      // On récupère le bean correspondant
      Identifier identifier = recupereBeanIdentifier(nomBeanIdentifier);

      // On appel la méthode identifyStream en passant en paramètre le
      // stream et l'idFormat
      IdentificationResult identifResult = identifier.identifyStream(idFormat,
            stream, nomFichier);

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // En retour on récupère et on renvoi un objet
      // IdentificationResult
      // contenant le résultat de l'identification et une liste des
      // traces.
      return identifResult;

   }

   private String lectureNomBeanIdentifier(String idFormat)
         throws UnknownFormatException, IdentifierInitialisationException {

      // Traces debug - entrée méthode
      String prefixeTrc = "lectureNomBeanIdentifier()";
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
         throw new IdentificationRuntimeException(except.getMessage(), except);
      }

      // L'objet formatFichier est forcément non null, car sinon la méthode
      // getFormat doit lever
      // une exception UnknownFormatException. On fait tout de même une
      // vérification en cas d'anomalie
      // de fonctionnement dans la méthode getFormat
      if (formatFichier == null) {
         throw new IdentifierInitialisationException(
               String
                     .format(
                           "Erreur technique: Le format %s n'existe pas dans le référentiel des formats, et le référentiel des formats n'a pas remonté l'erreur",
                           idFormat));
      } else {

         // Cas normal si le format existe dans le référentiel

         // On lit le nom du bean de l'objet d'identification
         String nomBean = formatFichier.getIdentificateur();

         // On vérifie que le nom du bean est renseigné
         if (StringUtils.isBlank(nomBean)) {

            // Le nom du bean n'est pas renseigné => on lève une exception
            throw new IdentifierInitialisationException(
                  String
                        .format(
                              "Le nom du bean permettant de réaliser l'identification du format %s n'est pas renseigné dans le référentiel des formats",
                              idFormat));

         } else {

            // Trace applicative
            LOGGER
                  .debug(
                        "{} - Le nom du bean qui va réaliser l'identification est \"{}\"",
                        prefixeTrc, nomBean);

            // Traces debug - sortie méthode
            LOGGER.debug(LOG_FIN, prefixeTrc);

            // On renvoie le nom du bean
            return nomBean;

         }

      }

   }

   private Identifier recupereBeanIdentifier(String nomBeanIdentifier)
         throws IdentifierInitialisationException {

      // Traces debug - entrée méthode
      String prefixeTrc = "recupereBeanIdentifier()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // Récupère l'objet du cache
      LOGGER
            .debug(
                  "{} - Lecture du cache pour récupérer l'objet qui va réaliser l'identification",
                  prefixeTrc);
      Identifier identifier;
      try {
         identifier = identifiers.getUnchecked(nomBeanIdentifier);
      } catch (InvalidCacheLoadException ex) {
         throw new IdentifierInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.recup.identif"), ex);

      } catch (UncheckedExecutionException ex) {
         throw new IdentifierInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.recup.identif"), ex);
      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // Renvoie l'objet d'identification
      return identifier;

   }

}
