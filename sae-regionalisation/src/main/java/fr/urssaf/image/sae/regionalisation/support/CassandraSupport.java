/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.support;

import com.netflix.astyanax.Keyspace;

/**
 * Service contenant les opérations liées à CASSANDRA
 * 
 */
public interface CassandraSupport {

   /**
    * Connexion à cassandra
    */
   void connect();
   
   /**
    * déconnexion de CASSANDRA
    */
   void disconnect();
   
   /**
    * @return le keyspace
    */
   Keyspace getKeySpace();
   
}
