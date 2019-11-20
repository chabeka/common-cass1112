package fr.urssaf.image.commons.cassandra.model;

import java.util.List;

import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.model.ColumnFamilyModel;
import org.cassandraunit.model.KeyspaceModel;

/**
 * DataSet permettant de fusionner plusieurs DataSet fichiers
 */
public class MemoryDataSet implements DataSet {

   private KeyspaceModel keyspace;

   private List<ColumnFamilyModel> columnFamilies;

   /**
    * {@inheritDoc}
    */
   @Override
   public List<ColumnFamilyModel> getColumnFamilies() {
      return this.columnFamilies;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public KeyspaceModel getKeyspace() {
      return this.keyspace;
   }

   /**
    * Set le Keyspace
    * 
    * @param keyspace
    *           le Keyspace
    */
   public void setKeyspace(KeyspaceModel keyspace) {
      this.keyspace = keyspace;
   }

   /**
    * Set les Column Families
    * 
    * @param columnFamilies
    *           les column families
    */
   public void setColumnFamilies(List<ColumnFamilyModel> columnFamilies) {
      this.columnFamilies = columnFamilies;
   }

}
