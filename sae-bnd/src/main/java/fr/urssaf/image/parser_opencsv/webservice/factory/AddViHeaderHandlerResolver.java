/**
 *
 */
package fr.urssaf.image.parser_opencsv.webservice.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import fr.urssaf.image.parser_opencsv.webservice.security.MyKeyStore;
import fr.urssaf.image.sae.client.vi.JaxWsVIHandler;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;

/**
 * Permet de définir une chaîne de "handler" composée d'un seul handler qui est le "VI Handler" qui
 * permet d'ajouter le VI dans les entêtes SOAP.
 */
public class AddViHeaderHandlerResolver implements HandlerResolver {

   private final String pagms;

   private final String contratServices;

   private final String passphrase;

   private final String privateKeyFile;

   private final static String VI_LOGIN = "BND_SCRIPT_CAPTURE";

   /**
    * @param privateKeyFile2
    * @param passphrase2
    * @param pagms2
    * @param contratService
    */
   public AddViHeaderHandlerResolver(final String privateKeyFile, final String passphrase, final String pagms, final String contratService) {
      this.privateKeyFile = privateKeyFile;
      this.pagms = pagms;
      contratServices = contratService;
      this.passphrase = passphrase;
   }

   @Override
   public List<Handler> getHandlerChain(final PortInfo portInfo) {
      final List<Handler> handlerChain = new ArrayList<>();
      // Récupération de l'objet exposant le PKCS#12
      // Dans un cas "normal", on récupère l'instance créée au démarrage
      // de l'application.
      final KeyStoreInterface keystore = new MyKeyStore(passphrase, privateKeyFile);

      // Instancie l'objet de génération du VI

      JaxWsVIHandler handler;
      handler = new JaxWsVIHandler(keystore,
                                   transformStringtoListPagms(pagms),
                                   contratServices,
                                   VI_LOGIN);
      handlerChain.add(handler);

      return handlerChain;
   }

   /**
    * Convertit une liste de pagms sous forme de String en une liste de pagms
    * 
    * @param pagms
    * @return
    */
   private List<String> transformStringtoListPagms(final String pagms) {
      return Arrays.asList(pagms.split("\\s*,\\s*"));
   }
}