/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.droit.dao.ActionUnitaireDao;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;

/**
 * Classe de support de la classe {@link ActionUnitaireDao}
 * 
 */
@Component
public class ActionUnitaireSupport {

   @Autowired
   private ActionUnitaireDao dao;

   /**
    * méthode de création d'une nouvelle ligne
    * 
    * @param actionUnitaire
    *           propriétés de l'action unitaire à créer
    * @param clock
    *           horloge de la création
    */
   public final void create(ActionUnitaire actionUnitaire, long clock) {

      // On utilise un ColumnFamilyUpdater, et on renseigne
      // la valeur de la clé dans la construction de l'updater
      ColumnFamilyUpdater<String, String> updaterJobRequest = dao
            .getActionUnitaireTmpl().createUpdater(actionUnitaire.getCode());

      // Ecriture des colonnes
      dao.ecritDescription(updaterJobRequest, actionUnitaire.getDescription(),
            clock);

      // Ecrit en base
      dao.getActionUnitaireTmpl().update(updaterJobRequest);
   }

   /**
    * Méthode de suppression d'une ligne
    * 
    * @param code
    *           identifiant de l'actionUnitaire
    * @param clock
    *           horloge de la suppression
    */
   public final void delete(String code, long clock) {

      // Création du Mutator
      Mutator<String> mutator = dao.createMutator();

      // suppression du JobRequest
      dao.mutatorSuppressionActionUnitaire(mutator, code, clock);

      // Execution de la commande
      mutator.execute();

   }

   /**
    * Lecture d'une ligne
    * 
    * @param code
    *           identifiant de l'action unitaire
    * @return l'objet action unitaire
    */
   public final ActionUnitaire find(String code) {

      ColumnFamilyResult<String, String> result = dao.getActionUnitaireTmpl()
            .queryColumns(code);

      ActionUnitaire actionUnitaire = getActionUnitaireFromResult(result);

      return actionUnitaire;
   }

   /**
    * Lecture de toutes les lignes (attention aux performances)
    * 
    * @param maxKeysToRead
    *           nombre maximum d'enregistrements à récupérer
    * @return la liste de toutes les actions unitaires
    */
   public final List<ActionUnitaire> findAll(int maxKeysToRead) {

      // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
      // en requêtant directement dans la CF JobRequest
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(dao.getKeyspace(), StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery
            .setColumnFamily(ActionUnitaireDao.AU_CFNAME);
      rangeSlicesQuery.setRange("", "", false,
            ActionUnitaireDao.MAX_AU_ATTIBUTS);
      rangeSlicesQuery.setRowCount(maxKeysToRead);
      QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery
            .execute();

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<String, String, byte[]>();
      ColumnFamilyResultWrapper<String, String> result = converter
            .getColumnFamilyResultWrapper(queryResult, StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);

      // On itère sur le résultat
      HectorIterator<String, String> resultIterator = new HectorIterator<String, String>(
            result);
      List<ActionUnitaire> list = new ArrayList<ActionUnitaire>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {
         ActionUnitaire actionUnitaire = getActionUnitaireFromResult(row);
         
         if (actionUnitaire != null)
            list.add(actionUnitaire);
      }
      return list;
   }

   private ActionUnitaire getActionUnitaireFromResult(
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

}
