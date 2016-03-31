/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.PagmpDao;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;

/**
 * Classe de support de la classe {@link PagmpDao}
 * 
 */
@Component
public class PagmpSupport extends AbstractSupport<Pagmp, String, String> {

   private final PagmpDao dao;

   /**
    * constructeur
    * 
    * @param pagmpDao
    *           DAO associée au pagmp
    */
   @Autowired
   public PagmpSupport(PagmpDao pagmpDao) {
      this.dao = pagmpDao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void create(Pagmp pagmp, long clock) {

      ColumnFamilyUpdater<String, String> updater = dao.getCfTmpl()
            .createUpdater(pagmp.getCode());

      dao.ecritDescription(updater, pagmp.getDescription(), clock);
      dao.ecritPrmd(updater, pagmp.getPrmd(), clock);

      dao.getCfTmpl().update(updater);

   }

   /**
    * Méthode de création d'un ligne avec Mutator
    * 
    * @param pagmp
    *           propriétés du PAGMp à créer
    * @param clock
    *           horloge de la création
    * @param mutator
    *           Mutator
    */
   public final void create(Pagmp pagmp, long clock, Mutator<String> mutator) {
      dao.ecritDescription(pagmp.getCode(), pagmp.getDescription(), clock,
            mutator);
      dao.ecritPrmd(pagmp.getCode(), pagmp.getPrmd(), clock, mutator);
   }

   /**
    * Méthode de suppression d'une ligne avec Murator en paramètre
    * 
    * @param code
    *           identifiant du PAGMp
    * @param clock
    *           horloge de suppression
    * @param mutator
    *           Mutator
    */
   public final void delete(String code, long clock, Mutator<String> mutator) {
      dao.mutatorSuppressionLigne(mutator, code, clock);
   }

   /**
    * {@inheritDoc}
    */
   protected final Pagmp getObjectFromResult(
         ColumnFamilyResult<String, String> result) {

      Pagmp pagmp = null;

      if (result != null && result.hasResults()) {
         pagmp = new Pagmp();
         pagmp.setCode(result.getKey());
         pagmp.setDescription(result.getString(PagmpDao.PAGMP_DESCRIPTION));
         pagmp.setPrmd(result.getString(PagmpDao.PAGMP_PRMD));
      }

      return pagmp;
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
