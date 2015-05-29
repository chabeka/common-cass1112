package fr.urssaf.image.sae.webservices;

/**
 * Interface des services du SAE
 * 
 * 
 */
public interface SaeService {

   String PING_MSG = "Les services SAE sont en ligne";

   String PING_SECURE_MSG = "Les services du SAE sécurisés par authentification sont en ligne";

   /**
    * fonction de test d'accès au service web
    * 
    * @return "Les services SAE sont en ligne"
    */
   String ping();

   /**
    * 
    * @return "Les services du SAE sécurisés par authentification sont en ligne"
    */
   String pingSecure();
}
