package fr.urssaf.image.sae.regionalisation.security;

import java.io.IOException;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("PMD.MethodNamingConventions")
public class AuthenticateSupportTest {

   private Class<? extends LoginModule> loginModule;

   @Before
   public void before() {

      loginModule = RegionalisationLoginModule.class;
   }

   @Test
   public void authenticate_success() throws LoginException {

      CallbackHandler callbackHandler = new RegionalisationCallbackHandler() {

         protected String loadPassword(PasswordCallback passwordCallback)
               throws IOException {

            return "tirablanc";

         }

      };

      AuthenticateSupport authenticateSupport = new AuthenticateSupport(
            callbackHandler, loginModule);

      authenticateSupport.authenticate("TIR_A_BLANC");

   }

   @Test
   public void authenticate_failure() throws LoginException {

      CallbackHandler callbackHandler = new RegionalisationCallbackHandler() {

         protected String loadPassword(PasswordCallback passwordCallback)
               throws IOException {

            return "badpassword";

         }

      };

      AuthenticateSupport authenticateSupport = new AuthenticateSupport(
            callbackHandler, loginModule);

      try {
         authenticateSupport.authenticate("MISE_A_JOUR");

         Assert.fail("l'authentification doit lever une LoginException");

      } catch (LoginException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "le mot de passe est incorrect", e.getMessage());

      }

   }

}
