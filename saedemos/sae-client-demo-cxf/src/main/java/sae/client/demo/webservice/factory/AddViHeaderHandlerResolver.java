/**
 *
 */
package sae.client.demo.webservice.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import fr.urssaf.image.sae.client.vi.JaxWsVIHandler;
import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import sae.client.demo.webservice.security.MyKeyStore;

/**
 * Permet de définir une chaîne de "handler" composée d'un seul handler qui est le "VI Handler" qui
 * permet d'ajouter le VI dans les entêtes SOAP.
 */
public class AddViHeaderHandlerResolver implements HandlerResolver {

   @Override
   public List<Handler> getHandlerChain(final PortInfo portInfo) {
      final List<Handler> handlerChain = new ArrayList<>();
      // Récupération de l'objet exposant le PKCS#12
      // Dans un cas "normal", on récupère l'instance créée au démarrage
      // de l'application.
      final KeyStoreInterface keystore = new MyKeyStore();

      // Instancie l'objet de génération du VI

      JaxWsVIHandler handler;
      try {
         handler = new JaxWsVIHandler(keystore,
                                 SaeServiceStubFactory.listPagmSaeService(),
                                 SaeServiceStubFactory.getProperties().getProperty(SaeServiceStubFactory.CONTRAT_SERVICE),
                                 SaeServiceStubFactory.VI_LOGIN);
         handlerChain.add(handler);

      }
      catch (final IOException e) {
         throw new ViSignatureException(e);
      }

      return handlerChain;
   }

}