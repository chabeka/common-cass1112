package fr.urssaf.image.sae.regionalisation.util;

import java.io.IOException;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;

import fr.urssaf.image.sae.regionalisation.security.AuthenticateSupport;
import fr.urssaf.image.sae.regionalisation.security.RegionalisationCallbackHandler;
import fr.urssaf.image.sae.regionalisation.security.RegionalisationLoginModule;

/**
 * Classe utilitaire pour l'authentification
 * 
 * 
 */
public final class AuthentificationUtils {
   
   private AuthentificationUtils(){
      
   }

   /**
    * Instancie un {@link AuthenticateSupport}
    * 
    * @param password
    *           mot de passe de l'application régionalisation
    * @return instance de {@link AuthenticateSupport}
    */
   public static AuthenticateSupport createAuthenticateSupport(
         final String password) {

      CallbackHandler callbackHandler = new RegionalisationCallbackHandler() {

         protected String loadPassword(PasswordCallback passwordCallback)
               throws IOException {

            return password;

         }

      };

      AuthenticateSupport authenticateSupport = new AuthenticateSupport(
            callbackHandler, RegionalisationLoginModule.class);

      return authenticateSupport;

   }
}
