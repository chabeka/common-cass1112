package fr.urssaf.image.commons.webservice.rpc.aed.context;

import javax.net.ssl.SSLContext;

import fr.urssaf.image.commons.util.net.MySSLContextFactory;

/**
 * Classe de création de SSLContext
 * 
 * 
 */
public final class AEDSSLContextFactory {

   private AEDSSLContextFactory() {

   }

   /**
    * Méthode pour récupérer un SSLContext initialiser pour AED
    * 
    * @return SSLContext
    */
   public static SSLContext getSSLContext() {

      try {
         MySSLContextFactory ctx = new MySSLContextFactory(AEDConfig.P12,
               AEDConfig.PASSWORD);

         return ctx.getSSLContext();
      } catch (Exception e) {
         throw new IllegalArgumentException(e);
      }

   }
}
