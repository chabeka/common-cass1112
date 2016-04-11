/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.serializer.PagmSerializer;

/**
 * Service DAO de la famille de colonnes "DroitPagm"
 * 
 */
@Repository
public class PagmDao extends AbstractDao<String, String> {

   public static final String PAGM_CFNAME = "DroitPagm";

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public PagmDao(Keyspace keyspace) {
      super(keyspace);

   }

   /**
    * ajoute une colonne de PAGM
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritPagm(ColumnFamilyUpdater<String, String> updater,
         Pagm value, long clock) {

      addColumn(updater, value.getCode(), value, PagmSerializer.get(), clock);

   }

   /**
    * ajoute une nouvelle ligne avec utilisation d'un mutator
    * 
    * @param idContratService
    *           Identifiant du contrat de service (clé de la ligne)
    * @param pagm
    *           pagm à créer
    * @param clock
    *           horloge de la colonne
    * @param mutator
    *           Mutator
    */
   public final void mutatorEcritPagm(String idContratService, Pagm pagm,
         long clock, Mutator<String> mutator) {
      addColumnWithMutator(idContratService, pagm.getCode(), pagm,
            PagmSerializer.get(), clock, mutator);
   }

   /**
    * Suppression d'un PAGM
    * 
    * @param mutator
    *           Mutator de <code>Pagm</code>
    * @param idContratService
    *           Identifiant du contrat de service auquel le PAGM est rattaché
    * @param codePagm
    *           identifiant du Pagm à supprimer
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionPagm(Mutator<String> mutator,
         String idContratService, String codePagm, long clock) {
      this
            .mutatorSuppressionColonne(mutator, idContratService, codePagm,
                  clock);
   }

   @Override
   public final String getColumnFamilyName() {
      return PAGM_CFNAME;
   }

   @Override
   public final Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }
}
