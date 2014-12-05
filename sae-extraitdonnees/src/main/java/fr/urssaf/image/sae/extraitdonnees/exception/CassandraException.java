package fr.urssaf.image.sae.extraitdonnees.exception;

/**
 * Erreur soulevée lors d'un problème d'accès à CASSANDRA
 * 
 */
public class CassandraException extends Exception {

   private static final long serialVersionUID = -2578429372058673337L;

   /**
    * Constructeur
    * 
    * @param message
    *           message de l'erreur
    */
   public CassandraException(String message) {
      super(message);
   }

}
