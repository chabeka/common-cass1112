/**
 * 
 */
package fr.urssaf.image.sae.commons.dao;

import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Classe abstraite pour les DAO CASSANDRA
 * 
 * @param <CFT>
 *           Type de la clé de ligne
 * @param <CT>
 *           Type du nom de colonne
 * 
 */
public abstract class AbstractDao<CFT, CT> {

   /** Nombre maximum de colonnes à retourner */
   public static final int MAX_ATTRIBUTS = 100;

   private final Keyspace keyspace;
   private final ColumnFamilyTemplate<CFT, CT> cfTmpl;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           keyspace utilisé
    */
   public AbstractDao(Keyspace keyspace) {
      this.keyspace = keyspace;

      cfTmpl = new ThriftColumnFamilyTemplate<CFT, CT>(keyspace,
            getColumnFamilyName(), getRowKeySerializer(),
            getColumnKeySerializer());

      cfTmpl.setCount(MAX_ATTRIBUTS);
   }

   /**
    * Ajout d'une colonne
    * 
    * @param <VT>
    *           Type de la valeur
    * @param cfUpdater
    *           Updater de la column family
    * @param colName
    *           nom de la colonne à ajouter
    * @param value
    *           valeur à insérer dans la colonne
    * @param valueSerializer
    *           serializer de la valeur
    * @param clock
    *           horloge de la création
    */
   public final <VT> void addColumn(ColumnFamilyUpdater<CFT, CT> cfUpdater,
         CT colName, VT value, Serializer<VT> valueSerializer, long clock) {

      HColumn<CT, VT> column = HFactory.createColumn(colName, value,
            getColumnKeySerializer(), valueSerializer);

      column.setClock(clock);
      cfUpdater.setColumn(column);
   }

   /**
    * Ajout d'une colonne avec utilisation d'un Mutator
    * 
    * @param code
    *           clé de la ligne
    * @param <VT>
    *           Type de la valeur
    * @param colName
    *           nom de la colonne à ajouter
    * @param value
    *           valeur à insérer dans la colonne
    * @param valueSerializer
    *           serializer de la valeur
    * @param clock
    *           horloge de la création
    * @param mutator mutator de la column family           
    */
   public final <VT> void addColumnWithMutator(CFT code, CT colName, VT value,
         Serializer<VT> valueSerializer, long clock, Mutator<CFT> mutator) {

      HColumn<CT, VT> column = HFactory.createColumn(colName, value,
            getColumnKeySerializer(), valueSerializer);

      column.setClock(clock);

      mutator.addInsertion(code, getColumnFamilyName(), column);

   }

   /**
    * Suppression d'une ligne de la column family
    * 
    * @param mutator
    *           mutator de la column family
    * @param code
    *           clé de la ligne à supprimer
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionLigne(Mutator<CFT> mutator, CFT code,
         long clock) {
      mutator.addDeletion(code, getColumnFamilyName(), clock);
   }

   /**
    * Suppression d'une colonne d'une ligne de la column family
    * 
    * @param mutator
    *           mutator de la column family
    * @param code
    *           clé de la ligne à supprimer
    * @param key
    *           nom de la colonne à supprimer
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionColonne(Mutator<CFT> mutator, CFT code,
         CT key, long clock) {
      mutator.addDeletion(code, getColumnFamilyName(), key,
            getColumnKeySerializer(), clock);
   }

   /**
    * 
    * @return Mutator de la column family
    */
   public final Mutator<CFT> createMutator() {

      Mutator<CFT> mutator = HFactory.createMutator(keyspace,
            getRowKeySerializer());

      return mutator;

   }

   /**
    * @return le keyspace utilise
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }

   /**
    * @return le template de la column family
    */
   public final ColumnFamilyTemplate<CFT, CT> getCfTmpl() {
      return cfTmpl;
   }

   /**
    * @return le nom de la column family
    */
   public abstract String getColumnFamilyName();

   /**
    * @return le serialiseur du nom des colonnes de la column family
    */
   public abstract Serializer<CT> getColumnKeySerializer();

   /**
    * @return le serialiseur de la clé des lignes de la column family
    */
   public abstract Serializer<CFT> getRowKeySerializer();

   /**
    * @return the maxAttributs
    */
   public final int getMaxAttributs() {
      return MAX_ATTRIBUTS;
   }

}
