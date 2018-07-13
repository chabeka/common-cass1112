package fr.urssaf.image.commons.cassandra.model;

import fr.urssaf.image.commons.cassandra.exception.CassandraConfigurationException;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.Keyspace;

/**
 * Classe utilisé pour simuler une connexion vers aucun keyspace Cassandra.
 * Cette classe est utilisé lors que la connexion cassandra de transfert n'est pas configurée. 
 * C'est le cas sur le SAE.
 */
public class NoConnectionKeyspace implements Keyspace {

   /**
    * {@inheritDoc}
    */
   @Override
   public long createClock() {
      throw new CassandraConfigurationException("Connexion cassandra non configurée");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getKeyspaceName() {
      throw new CassandraConfigurationException("Connexion cassandra non configurée");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setConsistencyLevelPolicy(ConsistencyLevelPolicy cp) {
      throw new CassandraConfigurationException("Connexion cassandra non configurée");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setCqlVersion(String version) {
      throw new CassandraConfigurationException("Connexion cassandra non configurée");
   }

}
