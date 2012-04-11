package fr.urssaf.image.zookeeper.exception;


public class ZookeeperEx extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 8964227951237138104L;

   /**
    * Construit un {@link ZookeeperEx }
    */
   public ZookeeperEx() {
      super();
   }

   /**
    * Construit un {@link ZookeeperEx }
    * 
    * @param message
    *           : Le message d'erreur
    */
   public ZookeeperEx(final String message) {
      super(message);
   }

   /**
    * Construit un {@link ZookeeperEx }
    * 
    * @param message
    *           : Le message d'erreur
    * @param cause
    *           : La cause de l'erreur
    */
   public ZookeeperEx(final String message, final Throwable cause) {
      super(message, cause);
   }

}
