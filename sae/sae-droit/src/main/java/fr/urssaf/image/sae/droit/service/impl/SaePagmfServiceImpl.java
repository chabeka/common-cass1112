/**
 * 
 */
package fr.urssaf.image.sae.droit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
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
    * Constructeur
    * 
    * @param pagmfSup
    *           la classe support
    * @param clockSupport
    *           l'horloge {@link JobClockSupport}
    * @param value
    *           durée du cache
    */
   @Autowired
   public SaePagmfServiceImpl(PagmfSupport pagmfSup,
         JobClockSupport clockSupport) {

      this.pagmfSupport = pagmfSup;
      this.clockSupport = clockSupport;
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

      Pagmf pagmf = pagmfSupport.find(code);

      if (pagmf == null) {
         throw new PagmfNotFoundException(ResourceMessagesUtils.loadMessage(
               "erreur.no.pagmf.found", code));
      }

      return pagmf;
   }

   @Override
   public final List<Pagmf> getAllPagmf() {
      return pagmfSupport.findAll();
   }

}
