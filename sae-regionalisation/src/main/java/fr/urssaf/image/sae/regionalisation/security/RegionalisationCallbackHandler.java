package fr.urssaf.image.sae.regionalisation.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * Implémenation de {@link CallbackHandler} pour l'authentification sur la
 * régionalisation
 * 
 * 
 */
public class RegionalisationCallbackHandler implements CallbackHandler {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void handle(Callback[] callbacks) throws IOException,
         UnsupportedCallbackException {

      for (int i = 0; i < callbacks.length; i++) {

         PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];

         String output = loadPassword(passwordCallback);

         passwordCallback.setPassword(output.toCharArray());

      }
   }

   @SuppressWarnings("PMD.SystemPrintln")
   protected String loadPassword(PasswordCallback passwordCallback)
         throws IOException {

      System.out.print(passwordCallback.getPrompt());
      System.out.flush();

      BufferedReader input = new BufferedReader(
            new InputStreamReader(System.in));

      return input.readLine();

   }
}
