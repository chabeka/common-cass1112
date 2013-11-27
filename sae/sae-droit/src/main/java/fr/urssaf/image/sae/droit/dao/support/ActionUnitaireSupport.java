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
import fr.urssaf.image.sae.droit.dao.ActionUnitaireDao;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;

/**
 * Classe de support de la classe {@link ActionUnitaireDao}
 * 
 */
@Component
public class ActionUnitaireSupport extends
      AbstractSupport<ActionUnitaire, String, String> {

   private final ActionUnitaireDao dao;

   /**
    * constructeur
    * 
    * @param auDao
    *           DAO associée aux Actions unitaires
    */
   @Autowired
   public ActionUnitaireSupport(ActionUnitaireDao auDao) {
      this.dao = auDao;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void create(ActionUnitaire actionUnitaire, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<String, String> updaterJobRequest = dao.getCfTmpl()
            .createUpdater(actionUnitaire.getCode());

      // Ecriture des colonnes
      dao.ecritDescription(updaterJobRequest, actionUnitaire.getDescription(),
            clock);

      // Ecrit en base
      dao.getCfTmpl().update(updaterJobRequest);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ActionUnitaire getObjectFromResult(
         ColumnFamilyResult<String, String> row) {

      ActionUnitaire actionUnitaire = null;

      if (row != null && row.hasResults()) {
         actionUnitaire = new ActionUnitaire();

         actionUnitaire.setCode(row.getKey());
         actionUnitaire.setDescription(row
               .getString(ActionUnitaireDao.AU_DESCRIPTION));
      }
      return actionUnitaire;
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
