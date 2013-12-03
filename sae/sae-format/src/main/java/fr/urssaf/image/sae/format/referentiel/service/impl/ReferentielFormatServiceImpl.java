/**
 * 
 */
package fr.urssaf.image.sae.format.referentiel.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.util.concurrent.UncheckedExecutionException;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * Implémentation de l’interface décrivant les méthodes proposées par le service
 * référentiel des formats.
 * 
 */
@Service
public class ReferentielFormatServiceImpl implements ReferentielFormatService {

   private final ReferentielFormatSupport refFormatSupport;

   private final JobClockSupport clockSupport;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ReferentielFormatServiceImpl.class);

   /**
    * Gestion du cache
    */
   private final LoadingCache<String, FormatFichier> formats;

   /**
    * Constructeur
    * 
    * @param refFormSupport
    *           la classe support
    * @param clockSupport
    *           l'horloge {@link JobClockSupport}
    * @param value
    *           durée du cache
    * 
    *           Récupération du conteneur de clockSupport
    * 
    *           sae.referentiel.format.cache à définir dans un fichier tel
    *           sae-config.properties dans src/test/resources/config
    */
   @Autowired
   public ReferentielFormatServiceImpl(ReferentielFormatSupport refFormSupport,
         JobClockSupport clockSupport,
         @Value("${sae.referentiel.format.cache}") int value) {

      this.refFormatSupport = refFormSupport;
      this.clockSupport = clockSupport;

      // Mise en cache
      formats = CacheBuilder.newBuilder().refreshAfterWrite(value,
            TimeUnit.MINUTES).build(new CacheLoader<String, FormatFichier>() {

         @Override
         public FormatFichier load(String identifiant) {
            return refFormatSupport.find(identifiant);
         }
      });
   }

   @Override
   public final void addFormat(FormatFichier refFormat) {

      refFormatSupport.create(refFormat, clockSupport.currentCLock());

   }

   @Override
   public final void deleteFormat(String idFormat)
         throws UnknownFormatException {

      refFormatSupport.delete(idFormat, clockSupport.currentCLock());
   }

   @Override
   public final FormatFichier getFormat(String idFormat)
         throws UnknownFormatException {

      FormatFichier format;
      try {
         format = formats.getUnchecked(idFormat);
         return format;
      } catch (InvalidCacheLoadException e) {
         LOGGER.debug(SaeFormatMessageHandler.getMessage(
               "erreur.no.format.found", idFormat));
         throw new UnknownFormatException(SaeFormatMessageHandler.getMessage(
               "erreur.no.format.found", idFormat), e);
      } catch (UncheckedExecutionException e) {
         throw new ReferentielRuntimeException(e);
      }
   }

   @Override
   public final List<FormatFichier> getAllFormat() {

      return refFormatSupport.findAll();

   }

}
