package fr.urssaf.image.sae.extraitdonnees.support;

import com.netflix.astyanax.Keyspace;

import fr.urssaf.image.sae.extraitdonnees.bean.CassandraConfig;

/**
 * Service contenant les opérations liées à Cassandra
 * 
 */
public interface CassandraSupport {

   /**
    * Connexion à Cassandra
    * 
    * @param cassandraConfig
    *           les paramètres de connexion
    */
   void connect(CassandraConfig cassandraConfig);

   /**
    * Déconnexion de Cassandra
    */
   void disconnect();

   /**
    * Le Keyspace
    * 
    * @return le keyspace
    */
   Keyspace getKeySpace();

}
