/**
 * 
 */
package fr.urssaf.image.administration.modele;

import me.prettyprint.hector.api.Cluster;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("dataModel")
public class DataBaseModel {
	private String keyspace;
	private int replicationFactor;
	private String strategy;
	@XStreamAlias("columnFamilies")
	private ColumnFamilies columnFamilies;
   private Cluster cluster;
   /**
    * @return the keyspace
    */
   public String getKeyspace() {
      return keyspace;
   }
   /**
    * @param keyspace the keyspace to set
    */
   public void setKeyspace(String keyspace) {
      this.keyspace = keyspace;
   }
   /**
    * @return the columnFamilies
    */
   public ColumnFamilies getColumnFamilies() {
      return columnFamilies;
   }
   /**
    * @param columnFamilies the columnFamilies to set
    */
   public void setColumnFamilies(ColumnFamilies columnFamilies) {
      this.columnFamilies = columnFamilies;
   }
   /**
    * @return the replicationFactor
    */
   public int getReplicationFactor() {
      return replicationFactor;
   }
   /**
    * @param replicationFactor the replicationFactor to set
    */
   public void setReplicationFactor(int replicationFactor) {
      this.replicationFactor = replicationFactor;
   }
   /**
    * @return the strategy
    */
   public String getStrategy() {
      return strategy;
   }
   /**
    * @param strategy the strategy to set
    */
   public void setStrategy(String strategy) {
      this.strategy = strategy;
   }
	
   public void setCluster(Cluster cluster) {
      this.cluster = cluster;
   }

   public Cluster getCluster() {
      return cluster;
   }
	

}
