package fr.urssaf.image.commons.webservice.axis.client.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

/**
 * Security pour WS Security
 * 
 * 
 */
public class PasswordCallbackHandler implements CallbackHandler {

   private final Map<String, String> passwords = new HashMap<String, String>();

   /**
    * Implementation des login/password
    */
   public PasswordCallbackHandler() {
      passwords.put("myuser", "mypassword");
   }

   @Override
   public final void handle(Callback[] callbacks) throws IOException,
         UnsupportedCallbackException {

      for (int i = 0; i < callbacks.length; i++) {

         if (callbacks[i] instanceof WSPasswordCallback) {
            WSPasswordCallback wsPassword = (WSPasswordCallback) callbacks[i];
            if (passwords.containsKey(wsPassword.getIdentifier())) {
               wsPassword
                     .setPassword(passwords.get(wsPassword.getIdentifier()));
               return;
            }

         }

      }
   }

}
