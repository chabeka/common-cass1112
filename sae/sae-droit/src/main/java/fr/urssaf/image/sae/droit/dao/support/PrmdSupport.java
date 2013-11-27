/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.PrmdDao;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.MapSerializer;

/**
 * Classe de support de la classe {@link PrmdDao}
 * 
 */
@Component
public class PrmdSupport extends AbstractSupport<Prmd, String, String> {

   private final PrmdDao dao;

   /**
    * constructeur
    * 
    * @param prmdDao
    *           DAO associée au PRMD
    */
   @Autowired
   public PrmdSupport(PrmdDao prmdDao) {
      this.dao = prmdDao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void create(Prmd prmd, long clock) {
      ColumnFamilyUpdater<String, String> updater = dao.getCfTmpl()
            .createUpdater(prmd.getCode());

      dao.ecritDescription(updater, prmd.getDescription(), clock);

      if (prmd.getLucene() != null) {
         dao.ecritLucene(updater, prmd.getLucene(), clock);
      }
      if (prmd.getBean() != null) {
         dao.ecritBean(updater, prmd.getBean(), clock);
      }
      if (prmd.getMetadata() != null) {
         dao.ecritMetaData(updater, prmd.getMetadata(), clock);
      }

      dao.getCfTmpl().update(updater);
   }

   /**
    * {@inheritDoc}
    */
   protected final Prmd getObjectFromResult(ColumnFamilyResult<String, String> result) {
      Prmd prmd = null;

      if (result != null && result.hasResults()) {
         prmd = new Prmd();
         prmd.setCode(result.getKey());
         prmd.setDescription(result.getString(PrmdDao.PRMD_DESCRIPTION));
         prmd.setLucene(result.getString(PrmdDao.PRMD_LUCENE));

         if (result.getByteArray(PrmdDao.PRMD_METADATA) != null) {
            byte[] bMetadata = result.getByteArray(PrmdDao.PRMD_METADATA);
            prmd.setMetadata(MapSerializer.get().fromBytes(bMetadata));
         }

         prmd.setBean(result.getString(PrmdDao.PRMD_BEAN));
      }

      return prmd;
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
