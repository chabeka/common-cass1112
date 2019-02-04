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

import fr.urssaf.image.sae.client.vi.VIHandler;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import sae.client.demo.webservice.security.MyKeyStore;

/**
 *
 *
 */
public class HeaderHandlerResolver implements HandlerResolver {

   @Override
   @SuppressWarnings("unchecked")
   public List<Handler> getHandlerChain(final PortInfo portInfo) {
      final List<Handler> handlerChain = new ArrayList<Handler>();
      // Récupération de l'objet exposant le PKCS#12
      // Dans un cas "normal", on récupère l'instance créée au démarrage
      // de l'application.
      final KeyStoreInterface keystore = new MyKeyStore();

      // Instancie l'objet de génération du VI

      VIHandler handler;
      try {
         handler = new VIHandler(keystore,
                                 StubFactory.listPagmSaeService(),
                                 StubFactory.getProperties().getProperty(StubFactory.CONTRAT_SERVICE),
                                 StubFactory.VI_LOGIN);
         handlerChain.add(handler);

      }
      catch (final IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return handlerChain;
   }

}