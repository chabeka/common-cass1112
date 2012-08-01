package fr.urssaf.image.sae.regionalisation.security;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.regionalisation.exception.ErreurTechniqueException;

/**
 * Implémentation de {@link LoginModule}
 * 
 * 
 */
public class RegionalisationLoginModule implements LoginModule {

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean abort() throws LoginException {

      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean commit() throws LoginException {

      return true;
   }

   private CallbackHandler callbackHandler;

   private String regionalisationPassword;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void initialize(Subject subject,
         CallbackHandler callbackHandler, Map<String, ?> sharedState,
         Map<String, ?> options) {

      Assert.notNull(callbackHandler, "'callbackHandler' is required");

      this.callbackHandler = callbackHandler;

      Assert.isTrue(options.containsKey("password"),
            "les options doivent contenir l'option 'password'");

      this.regionalisationPassword = (String) options.get("password");

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean login() throws LoginException {

      Callback[] callbacks = new Callback[1];
      callbacks[0] = new PasswordCallback("mot de passe: ", false);

      try {

         callbackHandler.handle(callbacks);

      } catch (IOException e) {
         throw new ErreurTechniqueException(e);
      } catch (UnsupportedCallbackException e) {
         throw new ErreurTechniqueException(e);
      }

      char[] tmpPassword = ((PasswordCallback) callbacks[0]).getPassword();

      String password = new String(tmpPassword);

      boolean succeeded = false;

      if (this.regionalisationPassword.equals(DigestUtils.shaHex(password))) {

         succeeded = true;

      } else {

         throw new FailedLoginException("le mot de passe est incorrect");

      }

      return succeeded;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean logout() throws LoginException {

      return true;
   }

}
