package fr.urssaf.image.sae.client.vi;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.exception.XmlSignatureException;
import fr.urssaf.image.sae.client.vi.signature.DefaultKeystore;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.client.vi.ws.SAML20Service;
import fr.urssaf.image.sae.client.vi.ws.WSSecurityService;

/**
 * Handler Axis2 pour ajouter le Vecteur d'Identification dans l'en-tête SOAP.<br>
 * <br>
 * Ce handler peut être branché dans la phase "MessageOut".
 */
public class VIHandler implements SOAPHandler<SOAPMessageContext> {

   private static final Logger LOGGER = LoggerFactory
                                                     .getLogger(VIHandler.class);

   public static final String KEYSPACE_GET_INSTANCE = "getInstance";

   public static final String KEY_KEYSTORE = "keystore";

   public static final String KEY_ISSUER = "issuer";

   public static final String KEY_LOGIN = "login";

   public static final String KEY_PAGMS = "pagms";

   private static final String DEFAULT_ISSUER = "PNR";

   private static final String DEFAULT_LOGIN = "NON_RENSEIGNE";

   private static final String DEFAUL_PAGM = "ROLE_TOUS;FULL";

   private KeyStoreInterface iKeyStore = null;

   private List<String> pagms = null;

   private String issuer = null;

   private String login = null;

   /**
    * Constructeur utilisé lors de la déclaration dans les fichiers AXIS
    */
   public VIHandler() {
      super();
      LOGGER.debug("Instanciation d'un VIHandler sans paramètre");
   }

   /**
    * Constructeur utilisé dans le code
    *
    * @param iKeyStore
    *           keystore à utiliser
    * @param pagms
    *           liste des pagms
    * @param issuer
    *           issuer
    */
   public VIHandler(final KeyStoreInterface iKeyStore, final List<String> pagms,
                    final String issuer, final String login) {
      super();
      this.iKeyStore = iKeyStore;
      this.issuer = issuer;
      this.pagms = pagms;
      this.login = login;

      LOGGER
            .debug(
                   "Instanciation d'un VIHandler avec les paramètres suivants : issuer={}, login={}, pagms={}, iKeyStore={}",
                   new Object[] { issuer, login, pagms, iKeyStore });
   }

   /*
    * (non-Javadoc)
    * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
    */
   @Override
   public boolean handleMessage(final SOAPMessageContext msgCtx) {

      // Ajout de l'en-tête WS-Security chargé depuis un fichier de ressource
      // XML
      try {

         // Génération de l'en-tête wsse
         final String wsse = genererEnTeteWsse(msgCtx);

         final SOAPMessage soapMsg = msgCtx.getMessage();
         final SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
         SOAPHeader soapHeader = soapEnv.getHeader();

         if (soapHeader == null) {
            soapHeader = soapEnv.addHeader();
         }

         final Document doc = createXMLDocumentFromString(wsse);

         // Ajout du message dans le header
         final SOAPBodyElement soapBody = addXMLDocumentToSoapHeader(doc, soapMsg);

      }
      catch (final Exception e) {
         throw new ViSignatureException(e);
      }
      return true;

   }

   /*
    * (non-Javadoc)
    * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
    */
   @Override
   public void close(final MessageContext arg0) {
      // do nothing

   }

   /*
    * (non-Javadoc)
    * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
    */
   @Override
   public boolean handleFault(final SOAPMessageContext arg0) {
      return true;
   }

   /*
    * (non-Javadoc)
    * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
    */
   @Override
   public Set<QName> getHeaders() {
      // Pas de traitement specifique sur les headers
      return null;
   }

   /*
    * ####################################################################
    * ####################################################################
    */

   public static SOAPBodyElement addXMLDocumentToSoapHeader(final Document document, final SOAPMessage soapMessage) throws SOAPException {
      final SOAPBodyElement newBodyElement = null;
      final DocumentFragment docFrag = document.createDocumentFragment();
      final Element rootElement = document.getDocumentElement();
      if (rootElement != null) {
         docFrag.appendChild(rootElement);
         final Document ownerDoc = soapMessage.getSOAPHeader().getOwnerDocument();
         final org.w3c.dom.Node replacingNode = ownerDoc.importNode(docFrag, true);
         soapMessage.getSOAPHeader().appendChild(replacingNode);

      }

      return newBodyElement;
   }

   private static Document createXMLDocumentFromString(final String xmlString) {

      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      // true si l'analyseur généré prend en charge les espaces de noms XML
      factory.setNamespaceAware(true);
      // API to obtain DOM Document instance
      DocumentBuilder builder = null;
      try {
         // Create DocumentBuilder with default configuration
         builder = factory.newDocumentBuilder();

         // Parse the content to Document object
         final Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
         return doc;
      }
      catch (final Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   /**
    * Génération de l'en-tête WSSE
    *
    * @param msgCtx
    *           contexte du message
    * @return l'en-tête à insérer dans le message
    * @throws SOAPException
    */
   public final String genererEnTeteWsse(final SOAPMessageContext msgCtx) throws SOAPException {

      LOGGER.debug("Début génération en-tête wsse");

      KeyStore keystore;
      String alias, password, mIssuer, mLogin;
      List<String> mPagms;

      if (iKeyStore == null) {

         LOGGER.debug("La construction du VIHandler a été faite par Axis2.");

         if (msgCtx.getMessage().getProperty(KEY_KEYSTORE) == null) {

            // récupération du keystore par défaut
            keystore = DefaultKeystore.getInstance().getKeystore();
            alias = DefaultKeystore.getInstance().getAlias();
            password = DefaultKeystore.getInstance().getPassword();
            mIssuer = DEFAULT_ISSUER;
            mLogin = DEFAULT_LOGIN;
            mPagms = Arrays.asList(DEFAUL_PAGM);

            LOGGER
                  .debug(
                         "Aucun KeyStore n'a été spécifié dans le MessageContext. On utilise le paramétrage par défaut: issuer={}, login={}, pagms={}, keystore={}",
                         new Object[] { mIssuer, mLogin, mPagms, keystore });

         } else {
            final String className = msgCtx.getMessage().getProperty(KEY_KEYSTORE).toString();
            final KeyStoreInterface keyStoreInterface = getKeystore(className);
            keystore = keyStoreInterface.getKeystore();
            alias = keyStoreInterface.getAlias();
            password = keyStoreInterface.getPassword();
            mIssuer = msgCtx.getMessage().getProperty(KEY_ISSUER).toString();
            mLogin = msgCtx.getMessage().getProperty(KEY_LOGIN).toString();
            final String sPagms = msgCtx.getMessage().getProperty(KEY_PAGMS).toString();
            mPagms = Arrays.asList(sPagms.split(","));

            LOGGER
                  .debug(
                         "Un KeyStore a été spécifié dans le MessageContext. On l'utilise, ainsi que les autres informations du MessageContext: issuer={}, login={}, pagms={}, keystore={}",
                         new Object[] { mIssuer, mLogin, mPagms, keystore });

         }

      } else {

         LOGGER
               .debug("La construction du VIHandler a été faite manuellement via le constructeur.");

         keystore = iKeyStore.getKeystore();
         alias = iKeyStore.getAlias();
         password = iKeyStore.getPassword();
         mPagms = this.pagms;
         mLogin = this.login;
         mIssuer = this.issuer;
      }

      // instanciation des paramètres du jeton SAML
      final DateTime systemDate = new DateTime();
      final UUID identifiant = UUID.randomUUID();

      // pour des questions de dérives d'horloges la période de début et de fin
      // de validé du jeton est de 2heures
      final DateTime notAfter = systemDate.plusHours(2);
      final DateTime notBefore = systemDate.minusHours(2);

      // Trace applicative
      LOGGER
            .debug(
                   "Paramètres pour la génération l'assertion: heure système={}, identifiant={}, notAfter={}, notBefore={}",
                   new Object[] { systemDate, identifiant, notAfter, notBefore });

      // Génération du VI
      String assertion;
      try {

         final SAML20Service assertionService = new SAML20Service();

         assertion = assertionService.createAssertion20(mIssuer,
                                                        mLogin,
                                                        mPagms,
                                                        notAfter,
                                                        notBefore,
                                                        systemDate,
                                                        identifiant,
                                                        keystore,
                                                        alias,
                                                        password);

         LOGGER.debug("Assertion générée: {}", assertion);

      }
      catch (final XmlSignatureException exception) {
         throw new ViSignatureException(exception);
      }

      // Génération de l'en-tête WS-Security
      final WSSecurityService wsService = new WSSecurityService();
      final String wsseSecurity = wsService.createWSSEHeader(assertion, identifiant);
      LOGGER.debug("En-tête WS-Security généré: {}", wsseSecurity);

      // Renvoie l'en-tête wsse
      LOGGER.debug("Fin génération en-tête wsse");
      return wsseSecurity;

   }

   private KeyStoreInterface getKeystore(final String className) {
      try {
         return (KeyStoreInterface) Class.forName(className)
                                         .getDeclaredMethod(
                                                            KEYSPACE_GET_INSTANCE, new Class[0])
                                         .invoke(null, new Object[0]);

      }
      catch (final IllegalArgumentException exception) {
         throw new ViSignatureException(exception);

      }
      catch (final SecurityException exception) {
         throw new ViSignatureException(exception);

      }
      catch (final IllegalAccessException exception) {
         throw new ViSignatureException(exception);

      }
      catch (final InvocationTargetException exception) {
         throw new ViSignatureException(exception);

      }
      catch (final NoSuchMethodException exception) {
         throw new ViSignatureException(exception);

      }
      catch (final ClassNotFoundException exception) {
         throw new ViSignatureException(exception);
      }
   }

}
