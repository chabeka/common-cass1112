package fr.urssaf.image.commons.webservice.wssecurity.spring.client.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class ClientPasswordCallback implements CallbackHandler {
	
	private Map<String, String> passwords = new HashMap<String, String>();

	public ClientPasswordCallback(){
		 passwords.put("myclientkey", "ckpass");
		 passwords.put("myuser", "mypassword");

	}

	@Override
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		for (int i = 0; i < callbacks.length; i++) {

			if (callbacks[i] instanceof WSPasswordCallback) {
				WSPasswordCallback wsPassword = (WSPasswordCallback) callbacks[0];
				if (passwords.containsKey(wsPassword.getIdentifier())) {
					wsPassword.setPassword(passwords.get(wsPassword.getIdentifier()));
					return;
				}
			}
			
		}

	}
}
