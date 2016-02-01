package fr.urssaf.image.sae.format.conversion.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
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

import de.schlichtherle.io.FileInputStream;
import fr.urssaf.image.sae.format.conversion.convertisseurs.Convertisseur;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionRuntimeException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConvertisseurInitialisationException;
import fr.urssaf.image.sae.format.conversion.service.ConversionService;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * Implémentation de la conversion d’un fichier.
 * 
 */
@Service
public class ConversionServiceImpl implements ConversionService {

   private static final String LOG_DEBUT = "{} - Début";
   private static final String LOG_FIN = "{} - Fin";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ConversionServiceImpl.class);

   /**
    * Cache.
    */
   private final LoadingCache<String, Convertisseur> convertisseurs;

   /**
    * Service de manipulation du referential des formats.
    */
   private final ReferentielFormatService referentielFormatService;

   /**
    * Le contexte de l’application
    */
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
   public ConversionServiceImpl(
         ReferentielFormatService referentielFormatService,
         @Value("${sae.referentiel.format.cache}") int cacheDuration,
         @Value("${sae.referentiel.format.initCacheOnStartup}") boolean initCacheOnStartup,
         ApplicationContext applicationContext) {

      this.referentielFormatService = referentielFormatService;
      this.applicationContext = applicationContext;

      // Gestion d'un cache contenant les beans d'identification
      convertisseurs = CacheBuilder.newBuilder().refreshAfterWrite(
            cacheDuration, TimeUnit.MINUTES).build(
            new CacheLoader<String, Convertisseur>() {

               @Override
               public Convertisseur load(String identifiant) {
                  LOGGER
                        .debug(
                              "Charge dans le cache l'objet d'identification dont le nom du bean est \"{}\"",
                              identifiant);
                  return ConversionServiceImpl.this.applicationContext.getBean(
                        identifiant, Convertisseur.class);
               }
            });

      if (initCacheOnStartup) {
         populateCache();
      }
   }
   
   private void populateCache() {
      // initialisation du cache des convertisseurs
      List<FormatFichier> allFormat = referentielFormatService.getAllFormat();
      for (FormatFichier format : allFormat) {
         if (StringUtils.isNotEmpty(format.getConvertisseur())) {
            Convertisseur convertisseur = ConversionServiceImpl.this.applicationContext.getBean(
                  format.getConvertisseur(), Convertisseur.class);
            if (convertisseur != null) {
               convertisseurs.put(format.getConvertisseur(), convertisseur);
            }
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final byte[] convertirFichier(String idFormat, File fichier,
         Integer numeroPage, Integer nombrePages)
         throws ConvertisseurInitialisationException, UnknownFormatException,
         ConversionParametrageException {

      // Traces debug - entrée méthode
      String prefixeTrc = "convertirFichier()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // On récupère le nom du bean qui doit réaliser la conversion
      String nomBeanConvertisseur = lectureNomBeanConvertisseur(idFormat);
      
      byte[] fichierConverti = null;
      // On vérifie que le nom du bean est renseigné
      if (StringUtils.isBlank(nomBeanConvertisseur)) {

         // Trace applicative
         LOGGER
               .debug(
                     "{} - Le format \"{}\" ne comporte aucun convertisseur. Le document va être renvoyé dans son format original",
                     prefixeTrc, idFormat);
         
         // recupere le tableau de byte a partir du fichier
         try {
            fichierConverti = IOUtils.toByteArray(new FileInputStream(fichier));
         } catch (FileNotFoundException e) {
            LOGGER.error(LOG_FIN, "Erreur de lecture du fichier : " + e.getMessage());
         } catch (IOException e) {
            LOGGER.error(LOG_FIN, "Erreur de lecture du fichier : " + e.getMessage());
         }

      } else {
      
         // On récupère le bean correspondant
         Convertisseur convertisseur = recupereBeanConvertisseur(nomBeanConvertisseur);
         
         LOGGER.debug("{} - Exécution de la conversion", prefixeTrc);
         fichierConverti = convertisseur.convertirFichier(fichier,
               numeroPage, nombrePages);
      }
      
      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // En retour on récupère et on renvoi un objet byte[]
      // contenant le flux converti
      return fichierConverti;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final byte[] convertirFichier(String idFormat, byte[] fichier,
         Integer numeroPage, Integer nombrePages)
         throws ConvertisseurInitialisationException, UnknownFormatException,
         ConversionParametrageException {

      // Traces debug - entrée méthode
      String prefixeTrc = "convertirFichier()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // On récupère le nom du bean qui doit réaliser la conversion
      String nomBeanConvertisseur = lectureNomBeanConvertisseur(idFormat);
      
      byte[] fichierConverti;
      // On vérifie que le nom du bean est renseigné
      if (StringUtils.isBlank(nomBeanConvertisseur)) {

         // Trace applicative
         LOGGER
               .debug(
                     "{} - Le format \"{}\" ne comporte aucun convertisseur. Le document va être renvoyé dans son format original",
                     prefixeTrc, idFormat);
         
         // clone le tableau de byte
         fichierConverti = fichier.clone();

      } else {

         // Trace applicative
         LOGGER
               .debug(
                     "{} - Le nom du bean qui va réaliser la conversion est \"{}\"",
                     prefixeTrc, nomBeanConvertisseur);

         // On récupère le bean correspondant
         Convertisseur convertisseur = recupereBeanConvertisseur(nomBeanConvertisseur);

         LOGGER.debug("{} - Exécution de la conversion", prefixeTrc);
         fichierConverti = convertisseur.convertirFichier(fichier,
               numeroPage, nombrePages);
      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // En retour on récupère et on renvoi un objet byte[]
      // contenant le flux converti
      return fichierConverti;
   }

   private String lectureNomBeanConvertisseur(String idFormat)
         throws UnknownFormatException, ConvertisseurInitialisationException {

      // Traces debug - entrée méthode
      String prefixeTrc = "lectureNomBeanConvertisseur()";
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
         throw new ConversionRuntimeException(except.getMessage(), except);
      }

      // L'objet formatFichier est forcément non null, car sinon la méthode
      // getFormat doit lever
      // une exception UnknownFormatException. On fait tout de même une
      // vérification en cas d'anomalie
      // de fonctionnement dans la méthode getFormat
      if (formatFichier == null) {
         throw new ConvertisseurInitialisationException(
               String
                     .format(
                           "Erreur technique: Le format %s n'existe pas dans le référentiel des formats, et le référentiel des formats n'a pas remonté l'erreur",
                           idFormat));
      } else {

         // Cas normal si le format existe dans le référentiel

         // On lit le nom du bean de l'objet de conversion
         String nomBean = formatFichier.getConvertisseur();
         
         // Traces debug - sortie méthode
         LOGGER.debug(LOG_FIN, prefixeTrc);
         
         // On renvoie le nom du bean
         return nomBean;
      }
   }

   private Convertisseur recupereBeanConvertisseur(String nomBeanConvertisseur)
         throws ConvertisseurInitialisationException {

      // Traces debug - entrée méthode
      String prefixeTrc = "recupereBeanConvertisseur()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      // Récupère l'objet du cache
      LOGGER
            .debug(
                  "{} - Lecture du cache pour récupérer l'objet qui va réaliser la conversion",
                  prefixeTrc);
      Convertisseur convertisseur;
      try {
         convertisseur = convertisseurs.getUnchecked(nomBeanConvertisseur);
      } catch (InvalidCacheLoadException ex) {
         throw new ConvertisseurInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.recup.convers"), ex);

      } catch (UncheckedExecutionException ex) {
         throw new ConvertisseurInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.recup.convers"), ex);
      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      // Renvoie l'objet de conversion
      return convertisseur;

   }
}
