/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.util.concurrent.UncheckedExecutionException;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.support.FormatControlProfilSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.service.FormatControlProfilService;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Implémentation de l'interface décrivant les méthodes proposées par
 * FormatControlProfilService
 * 
 */
@Service
public class FormatControlProfilServiceImpl implements
      FormatControlProfilService {

   private final FormatControlProfilSupport formatControlSupport;
   private final JobClockSupport clockSupport;

   /**
    * Gestion du cache
    */
   private final LoadingCache<String, FormatControlProfil> formatsControlProfil;

   /**
    * Constructeur
    * 
    * @param formatSup
    *           la classe support
    * @param clockSupport
    *           l'horloge {@link JobClockSupport}
    * @param value
    *           durée du cache
    * 
    *           Récupération du conteneur de clockSupport
    * 
    *           sae.format.control.profil.cache à définir dans un fichier tel
    *           sae-config.properties dans src/test/resources/config
    */
   @Autowired
   public FormatControlProfilServiceImpl(FormatControlProfilSupport formatSup,
         JobClockSupport clockSupport,
         @Value("${sae.format.control.profil.cache}") int value) {

      this.formatControlSupport = formatSup;
      this.clockSupport = clockSupport;

      // Mise en cache
      formatsControlProfil = CacheBuilder.newBuilder().refreshAfterWrite(value,
            TimeUnit.MINUTES).build(
            new CacheLoader<String, FormatControlProfil>() {

               @Override
               public FormatControlProfil load(String identifiant) {
                  return formatControlSupport.find(identifiant);
               }
            });
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean formatControlProfilExists(String code) {

      try {
         formatsControlProfil.getUnchecked(code);
         return true;
      } catch (InvalidCacheLoadException e) {
         return false;
      } catch (UncheckedExecutionException e) {
         throw new DroitRuntimeException(e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void addFormatControlProfil(
         FormatControlProfil formatControlProfil) {

      formatControlSupport.create(formatControlProfil, clockSupport
            .currentCLock());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void modifyFormatControlProfil(
         FormatControlProfil formatControlProfil)
         throws FormatControlProfilNotFoundException {
      if (formatControlProfilExists(formatControlProfil.getFormatCode())) {
         addFormatControlProfil(formatControlProfil);
      } else {
         throw new FormatControlProfilNotFoundException(ResourceMessagesUtils
               .loadMessage("erreur.format.control.profil.not.found",
                     formatControlProfil.getFormatCode()));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void deleteFormatControlProfil(String codeFormatControlProfil)
         throws FormatControlProfilNotFoundException {

      formatControlSupport.delete(codeFormatControlProfil, clockSupport
            .currentCLock());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final FormatControlProfil getFormatControlProfil(
         String codeFormatControlProfil)
         throws FormatControlProfilNotFoundException {

      FormatControlProfil formatControlProfil;
      try {
         formatControlProfil = formatsControlProfil
               .getUnchecked(codeFormatControlProfil);
         return formatControlProfil;
      } catch (InvalidCacheLoadException e) {
         throw new FormatControlProfilNotFoundException(ResourceMessagesUtils
               .loadMessage("erreur.format.control.profil.not.found",
                     codeFormatControlProfil), e);
      } catch (UncheckedExecutionException e) {
         throw new DroitRuntimeException(e);
      }
   }

   @Override
   public final List<FormatControlProfil> getAllFormatControlProfil() {

      return formatControlSupport.findAll();
   }

}
