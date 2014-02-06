package fr.urssaf.image.sae.format.identification.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
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
 * Implémentation du processus d’identification d’un flux ou d’un fichier.
 * 
 */
@Service
public class IdentificationServiceImpl implements IdentificationService {

   /**
    * Définition de caches permettant de ne pas recharger à chaque fois les
    * services à partir de l'applicationContext
    */
   private final LoadingCache<String, Identifier> identifiers;

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
    */
   @Autowired
   public IdentificationServiceImpl(
         ReferentielFormatService referentielFormatService,
         @Value("${sae.referentiel.format.cache}") int cacheDuration,
         ApplicationContext applicationContext) {
      this.referentielFormatService = referentielFormatService;
      this.applicationContext = applicationContext;
      // Mise en cache
      identifiers = CacheBuilder.newBuilder().refreshAfterWrite(cacheDuration,
            TimeUnit.MINUTES).build(new CacheLoader<String, Identifier>() {

         @Override
         public Identifier load(String identifiant) {
            return IdentificationServiceImpl.this.applicationContext.getBean(
                  identifiant, Identifier.class);
         }
      });
   }

   @Override
   public final IdentificationResult identifyFile(String idFormat, File fichier)
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      FormatFichier format;
      Identifier identifier;
      IdentificationResult identifResult;
      try {
         // On récupère l'identificateur à utiliser pour l'idFormat donné à
         // partir du référentiel des formats
         format = referentielFormatService.getFormat(idFormat);

         if (format != null
               && StringUtils.isNotBlank(format.getIdentificateur())) {

            String identificateur = format.getIdentificateur();

            // On utilise l'application contexte pour récupérer une instance de
            // l'identificateur
            identifier = identifiers.getUnchecked(identificateur);

            // On appel la méthode identifyFile en passant en paramètre le
            // fichier et l'idFormat
            identifResult = identifier.identifyFile(idFormat, fichier);

            // En retour on récupère et on renvoi un objet
            // IdentificationResult
            // contenant le résultat de l'identification et une liste des
            // traces.
            return identifResult;

         } else {
            throw new IdentifierInitialisationException(SaeFormatMessageHandler
                  .getMessage("erreur.recup.identif"));
         }

      } catch (InvalidCacheLoadException except) {
         throw new IdentifierInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.recup.identif"));

      } catch (UncheckedExecutionException except) {
         throw new IdentifierInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.recup.identif"));

      } catch (ReferentielRuntimeException except) {
         throw new IdentificationRuntimeException(except.getMessage(), except);
      }

   }

   @Override
   public final IdentificationResult identifyStream(String idFormat,
         InputStream stream) throws UnknownFormatException,
         IdentifierInitialisationException {

      FormatFichier format;
      Identifier identifier;
      IdentificationResult identifResult;
      try {
         format = referentielFormatService.getFormat(idFormat);
   
         if (format != null
               && StringUtils.isNotBlank(format.getIdentificateur())) {
   
            String identificateur = format.getIdentificateur();
   
            // On utilise l'application contexte pour récupérer une instance de
            // l'identificateur
            identifier = identifiers.getUnchecked(identificateur);
   
            // On appel la méthode identifyStream en passant en paramètre le
            // stream et l'idFormat
            identifResult = identifier.identifyStream(idFormat, stream);
   
            // En retour on récupère et on renvoi un objet
            // IdentificationResult
            // contenant le résultat de l'identification et une liste des
            // traces.
            return identifResult;
   
         } else {
            throw new IdentifierInitialisationException(SaeFormatMessageHandler
                  .getMessage("erreur.recup.identif"));
         }
      } catch (InvalidCacheLoadException except) {
         throw new IdentifierInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.recup.identif"));

      } catch (UncheckedExecutionException except) {
         throw new IdentifierInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.recup.identif"));

      } catch (ReferentielRuntimeException except) {
         throw new IdentificationRuntimeException(except.getMessage(), except);
      }   
   }

}
