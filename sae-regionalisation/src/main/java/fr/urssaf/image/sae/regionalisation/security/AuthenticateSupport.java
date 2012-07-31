package fr.urssaf.image.sae.regionalisation.security;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.spi.LoginModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Support pour l'authentification sur le service de régionalisation.
 * 
 * 
 */
public final class AuthenticateSupport {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(AuthenticateSupport.class);

   private final CallbackHandler callbackHandler;

   private final Class<? extends LoginModule> loginModule;

   /**
    * constructeur par défaut
    */
   public AuthenticateSupport() {

      this.callbackHandler = new RegionalisationCallbackHandler();
      this.loginModule = RegionalisationLoginModule.class;
   }

   /**
    * 
    * @param callbackHandler
    *           mécanisme de récupération du mot de passe
    * @param loginModule
    *           module de login
    */
   public AuthenticateSupport(CallbackHandler callbackHandler,
         Class<? extends LoginModule> loginModule) {

      this.callbackHandler = callbackHandler;
      this.loginModule = loginModule;
   }

   // l'implémentation s'appuie en grande partie sur
   // http://docs.oracle.com/javase/6/docs/technotes/guides/security/jaas/tutorials/GeneralAcnOnly.html
   /**
    * méthode d'authentification.
    * 
    * @throws LoginException
    *            l'authentification a échoué
    */
   public void authenticate() throws LoginException {

      Configuration configuration = new Configuration() {

         @Override
         public AppConfigurationEntry[] getAppConfigurationEntry(String name) {

            AppConfigurationEntry[] config = new AppConfigurationEntry[1];

            Map<String, Object> options = new HashMap<String, Object>();

            config[0] = new AppConfigurationEntry(loginModule
                  .getCanonicalName(), LoginModuleControlFlag.SUFFICIENT,
                  options);

            return config;
         }

      };

      LoginContext loginContext = new LoginContext("regionalisation", null,
            callbackHandler, configuration);

      loginContext.login();

      LOGGER.debug("L'authentification a réussi");

   }

}
