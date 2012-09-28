package fr.urssaf.image.sae.regionalisation.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.spi.LoginModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import fr.urssaf.image.sae.regionalisation.exception.ErreurTechniqueException;

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

   private final Properties passwords;

   /**
    * constructeur par défaut
    */
   public AuthenticateSupport() {

      this(new RegionalisationCallbackHandler(),
            RegionalisationLoginModule.class);

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

      ClassPathResource resource = new ClassPathResource("password.txt");

      try {

         InputStreamReader inputStream = new InputStreamReader(resource
               .getInputStream());

         BufferedReader input = new BufferedReader(inputStream);

         try {

            passwords = new Properties();
            passwords.load(input);

         } finally {

            input.close();
         }

      } catch (IOException e) {

         throw new ErreurTechniqueException(e);

      }
   }

   // l'implémentation s'appuie en grande partie sur
   // http://docs.oracle.com/javase/6/docs/technotes/guides/security/jaas/tutorials/GeneralAcnOnly.html
   /**
    * méthode d'authentification.
    * 
    * @param mode
    *           mode de lancement : <b>MISE_A_JOUR</b> ou <b>TIR_A_BLANC</b>
    * @throws LoginException
    *            l'authentification a échoué
    */
   public void authenticate(final String mode) throws LoginException {

      Configuration configuration = new Configuration() {

         @Override
         public AppConfigurationEntry[] getAppConfigurationEntry(String name) {

            AppConfigurationEntry[] config = new AppConfigurationEntry[1];

            String authentificationPassword = passwords.getProperty(mode);

            Map<String, Object> options = new HashMap<String, Object>();
            options.put("password", authentificationPassword);

            config[0] = new AppConfigurationEntry(loginModule
                  .getCanonicalName(), LoginModuleControlFlag.SUFFICIENT,
                  options);

            return config;
         }

      };

      LoginContext loginContext = new LoginContext("regionalisation", null,
            callbackHandler, configuration);

      // on se connecte pour s'authentifier
      loginContext.login();

      LOGGER.debug("L'authentification a réussi");

      // on se déconnecte car aucun contexte d'authentification n'est utilisé
      // ici dans la régionalisation
      loginContext.logout();

   }

}
