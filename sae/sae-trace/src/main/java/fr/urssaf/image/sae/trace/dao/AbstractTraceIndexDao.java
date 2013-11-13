/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.sql.Date;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.SliceQuery;
import fr.urssaf.image.sae.commons.dao.AbstractDao;

/**
 * Classe mère de traitement des données d'index
 * 
 */
public abstract class AbstractTraceIndexDao<VT> extends
      AbstractDao<String, UUID> {

   /**
    * Constructeur par défaut
    * 
    * @param keyspace
    *           le keyspace utilisé
    */
   public AbstractTraceIndexDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * @param <VT>
    *           le type de la valeur
    * @param valueSerializer
    *           le serializer de la valeur
    * @return le SliceQuery
    */
   public final SliceQuery<String, UUID, VT> createSliceQuery() {

      SliceQuery<String, UUID, VT> sliceQuery = HFactory.createSliceQuery(
            getKeyspace(), getRowKeySerializer(), getColumnKeySerializer(),
            getValueSerializer());

      sliceQuery.setColumnFamily(getColumnFamilyName());

      return sliceQuery;
   }

   /**
    * ajoute une colonne <b>name</b>
    * 
    * @param updater
    *           updater d'index
    * @param name
    *           nom de la colonne
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumn(ColumnFamilyUpdater<String, UUID> updater,
         UUID name, VT value, long clock) {
      addColumn(updater, name, value, getValueSerializer(), clock);
   }

   /**
    * Création du ColumnFamilyUpdater
    * 
    * @param journee
    *           la journée, au format obtenu de {@link #getJournee(Date)}
    * @return le ColumnFamilyUpdater
    */
   public final ColumnFamilyUpdater<String, UUID> createUpdater(String journee) {
      return getCfTmpl().createUpdater(journee);
   }

   /**
    * Flush les mises à jour
    * 
    * @param updater
    *           l'updater de la CF
    */
   public final void update(ColumnFamilyUpdater<String, UUID> updater) {
      getCfTmpl().update(updater);
   }

   /**
    * @return le nom de la column family
    */
   public abstract String getColumnFamilyName();

   /**
    * {@inheritDoc}
    */
   @Override
   public final Serializer<UUID> getColumnKeySerializer() {
      return UUIDSerializer.get();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * @return le serializer de la valeur
    */
   public abstract Serializer<VT> getValueSerializer();

}
