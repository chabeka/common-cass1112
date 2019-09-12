/**
 *
 */
package sae.integration.webservice.factory;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import fr.urssaf.image.sae.client.vi.JaxWsVIHandler;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;

/**
 * Permet de définir une chaîne de "handler" composée d'un seul handler qui est le "VI Handler" qui
 * permet d'ajouter le VI dans les entêtes SOAP.
 */
public class AddViHeaderHandlerResolver implements HandlerResolver {

   private final String p12Name;

   private final String password;

   private final List<String> pagms;

   private final String cs;

   private final String login;

   public AddViHeaderHandlerResolver(final String p12Name, final String password, final List<String> pagms, final String cs, final String login) {
      this.p12Name = p12Name;
      this.password = password;
      this.pagms = pagms;
      this.cs = cs;
      this.login = login;
   }

   @Override
   public List<Handler> getHandlerChain(final PortInfo portInfo) {
      final List<Handler> handlerChain = new ArrayList<>();
      // Récupération de l'objet exposant le PKCS#12
      final KeyStoreInterface keystore = new MyKeyStore(p12Name, password);
      // Instancie l'objet de génération du VI
      JaxWsVIHandler handler;
      handler = new JaxWsVIHandler(keystore, pagms, cs, login);
      handlerChain.add(handler);

      return handlerChain;
   }

}