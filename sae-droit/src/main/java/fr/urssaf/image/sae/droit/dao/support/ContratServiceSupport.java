/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.Arrays;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.ContratServiceDao;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.serializer.ListSerializer;

/**
 * Classe de support de la classe {@link ContratServiceDao}
 * 
 */
@Component
public class ContratServiceSupport extends
      AbstractSupport<ServiceContract, String, String> {

   private final ContratServiceDao dao;

   /**
    * constructeur
    * 
    * @param csDao
    *           DAO associée au Constrat de service
    */
   @Autowired
   public ContratServiceSupport(ContratServiceDao csDao) {
      this.dao = csDao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void create(ServiceContract contratService, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<String, String> updaterJobRequest = dao.getCfTmpl()
            .createUpdater(contratService.getCodeClient());

      // écriture des colonnes
      dao.ecritLibelle(updaterJobRequest, contratService.getLibelle(), clock);
      dao.ecritDescription(updaterJobRequest, contratService.getDescription(),
            clock);
      dao.ecritViDuree(updaterJobRequest, contratService.getViDuree(), clock);

      if (CollectionUtils.isNotEmpty(contratService.getListPki())) {
         dao.ecritListePki(updaterJobRequest, contratService.getListPki(),
               clock);
      } else if (StringUtils.isNotEmpty(contratService.getIdPki())) {
         dao.ecritListePki(updaterJobRequest, Arrays.asList(contratService
               .getIdPki()), clock);
      }

      if (CollectionUtils.isNotEmpty(contratService.getListCertifsClient())) {
         dao.ecritListeCert(updaterJobRequest, contratService
               .getListCertifsClient(), clock);
      } else if (contratService.getIdCertifClient() != null) {
         dao.ecritListeCert(updaterJobRequest, Arrays.asList(contratService
               .getIdCertifClient()), clock);
      }

      dao.ecritFlagControlNommage(updaterJobRequest, contratService
            .isVerifNommage(), clock);

      // écriture en base
      dao.getCfTmpl().update(updaterJobRequest);

   }

   /**
    * {@inheritDoc}
    */
   protected final ServiceContract getObjectFromResult(
         ColumnFamilyResult<String, String> result) {

      ServiceContract contract = null;

      if (result != null && result.hasResults()) {
         contract = new ServiceContract();
         contract.setCodeClient(result.getKey());
         contract.setLibelle(result.getString(ContratServiceDao.CS_LIBELLE));
         contract.setDescription(result
               .getString(ContratServiceDao.CS_DESCRIPTION));
         contract.setViDuree(result.getLong(ContratServiceDao.CS_VI_DUREE));

         if (result.getString(ContratServiceDao.CS_PKI) != null) {
            contract.setIdPki(result.getString(ContratServiceDao.CS_PKI));
         }

         if (result.getString(ContratServiceDao.CS_CERT) != null) {
            contract.setIdCertifClient(result
                  .getString(ContratServiceDao.CS_CERT));
         }

         if (result.getByteArray(ContratServiceDao.CS_LISTE_CERT) != null) {
            byte[] bytes = result.getByteArray(ContratServiceDao.CS_LISTE_CERT);
            contract
                  .setListCertifsClient(ListSerializer.get().fromBytes(bytes));
         }

         if (result.getByteArray(ContratServiceDao.CS_LISTE_PKI) != null) {
            byte[] bytes = result.getByteArray(ContratServiceDao.CS_LISTE_PKI);
            contract.setListPki(ListSerializer.get().fromBytes(bytes));
         }

         if (result.getBoolean(ContratServiceDao.CS_VERIF_NOMMAGE) != null) {
            contract.setVerifNommage(result
                  .getBoolean(ContratServiceDao.CS_VERIF_NOMMAGE));
         }
      }

      return contract;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final AbstractDao<String, String> getDao() {
      return dao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getMax() {
      return StringUtils.EMPTY;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getMin() {
      return StringUtils.EMPTY;
   }
}
