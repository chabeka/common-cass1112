package fr.urssaf.image.commons.webservice.wssecurity.spring.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;
import org.springframework.stereotype.Component;

@Component
public class ServerPasswordCallback implements CallbackHandler {

	private Map<String, String> passwords = new HashMap<String, String>();

	public ServerPasswordCallback() {
		passwords.put("myservicekey", "skpass");
		passwords.put("myuser", "mypassword");

	}

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {

		for (int i = 0; i < callbacks.length; i++) {

			if (callbacks[i] instanceof WSPasswordCallback) {
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
				if (passwords.containsKey(pc.getIdentifier())) {
					pc.setPassword(passwords.get(pc.getIdentifier()));
					return;
				}

			}

		}
	}

}
