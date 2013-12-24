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
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.PagmfNotFoundException;
import fr.urssaf.image.sae.droit.service.SaePagmfService;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

/**
 * Implémentation de l'interface {@link SaePagmfService} décrivant les <br>
 * proposées par le service Pagmf.
 * 
 */
@Service
public class SaePagmfServiceImpl implements SaePagmfService {

   private final PagmfSupport pagmfSupport;

   private final JobClockSupport clockSupport;

   /**
    * Gestion du cache
    */
   private final LoadingCache<String, Pagmf> pagmfs;

   /**
    * Constructeur
    * 
    * @param pagmfSup
    *           la classe support
    * @param clockSupport
    *           l'horloge {@link JobClockSupport}
    * @param value
    *           durée du cache
    * 
    *           Récupération du conteneur de clockSupport
    * 
    *           sae.pagmf.cache à définir dans un fichier tel
    *           sae-config.properties dans src/test/resources/config
    */
   @Autowired
   public SaePagmfServiceImpl(PagmfSupport pagmfSup,
         JobClockSupport clockSupport, @Value("${sae.pagmf.cache}") int value) {

      this.pagmfSupport = pagmfSup;
      this.clockSupport = clockSupport;

      // Mise en cache
      pagmfs = CacheBuilder.newBuilder().refreshAfterWrite(value,
            TimeUnit.MINUTES).build(new CacheLoader<String, Pagmf>() {

         @Override
         public Pagmf load(String identifiant) {
            return pagmfSupport.find(identifiant);
         }
      });
   }

   @Override
   public final void addPagmf(Pagmf pagmf) {
      // la vérification des paramètres obligatoires est faite en aspect.
      pagmfSupport.create(pagmf, clockSupport.currentCLock());
   }

   @Override
   public final void deletePagmf(String codePagmf) {
      pagmfSupport.delete(codePagmf, clockSupport.currentCLock());
   }

   @Override
   public final Pagmf getPagmf(String code) {

      Pagmf pagmf;
      try {
         pagmf = pagmfs.getUnchecked(code);
         return pagmf;
      } catch (InvalidCacheLoadException e) {
         throw new PagmfNotFoundException(ResourceMessagesUtils.loadMessage(
               "erreur.no.pagmf.found", code), e);
      } catch (UncheckedExecutionException e) {
         throw new DroitRuntimeException(e);
      }
   }

   @Override
   public final List<Pagmf> getAllPagmf() {
      return pagmfSupport.findAll();
   }

}
