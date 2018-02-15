package fr.urssaf.image.sae.cassandra.dao.exception;

/**
 *Exception levée lorsque de la création des archives log ou lors de la purge
 * des logs System et Documents dans DFCE. <BR />
 * 
 */
public class CassandraEx extends Exception {

   /**
    * L'identifiant unique de l'erreur
    */
   private static final long serialVersionUID = -7786562625725866505L;

   /**
    * Construit un {@link CassandraEx }
    */
   public CassandraEx() {
      super();
   }

   /**
    * Construit un {@link CassandraEx }
    * 
    * @param message
    *           : Le message d'erreur
    */
   public CassandraEx(final String message) {
      super(message);
   }

   /**
    * Construit un {@link CassandraEx }
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public CassandraEx(final String message, final Throwable cause) {
      super(message, cause);
   }

}
