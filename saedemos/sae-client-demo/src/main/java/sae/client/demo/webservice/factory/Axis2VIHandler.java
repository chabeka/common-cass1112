package sae.client.demo.webservice.factory;

import java.io.StringReader;
import java.util.List;

import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.client.vi.VIGenerator;
import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;

/**
 * Handler Axis 2 permettant d'ajouter un VI dans les entêtes SOAP des requêtes SOAP
 */
public class Axis2VIHandler extends AbstractHandler {

   private static final Logger LOGGER = LoggerFactory.getLogger(Axis2VIHandler.class);

   private KeyStoreInterface iKeyStore = null;

   private List<String> pagms = null;

   private String issuer = null;

   private String login = null;

   /**
    * Constructeur
    *
    * @param iKeyStore
    *           keystore à utiliser
    * @param pagms
    *           liste des pagms
    * @param issuer
    *           issuer
    * @param login
    *           login de l'utilisateur demandeur du service
    */
   public Axis2VIHandler(final KeyStoreInterface iKeyStore, final List<String> pagms,
                         final String issuer, final String login) {
      super();
      this.iKeyStore = iKeyStore;
      this.issuer = issuer;
      this.pagms = pagms;
      this.login = login;

      LOGGER.debug("Instanciation d'un VIHandler avec les paramètres suivants : issuer={}, login={}, pagms={}, iKeyStore={}",
                   issuer,
                   login,
                   pagms,
                   iKeyStore);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public InvocationResponse invoke(final MessageContext msgContext) throws AxisFault {
      // Ajout de l'en-tête WS-Security
      try {

         // Génération de l'en-tête wsse
         final String wsse = VIGenerator.genererEnTeteWsse(issuer, login, pagms, iKeyStore.getKeystore(), iKeyStore.getAlias(), iKeyStore.getPassword());

         final SOAPHeader soapHeader = msgContext.getEnvelope().getHeader();
         soapHeader.addChild(org.apache.axis2.util.XMLUtils.toOM(new StringReader(wsse)));
         soapHeader.build();

      }
      catch (final Exception e) {
         throw new ViSignatureException(e);
      }
      return InvocationResponse.CONTINUE;
   }

}
