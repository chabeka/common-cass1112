package fr.urssaf.image.sae.client.vi;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * 
 */
public class VIHandler extends AbstractHandler {

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
   public VIHandler(KeyStoreInterface iKeyStore, List<String> pagms,
         String issuer, String login) {
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

   /**
    * Création d'une balise WS-Security dans le header du SOAP<br>
    * <br>
    * Insertion du VI dans cette balise WS-Security
    * 
    * @param msgCtx
    *           Axis2 MessageContext
    * @return Axis2 InvocationResponse
    * @throws Axis2
    *            exception
    */
   public final InvocationResponse invoke(MessageContext msgCtx)
         throws AxisFault {

      // Ajout de l'en-tête WS-Security chargé depuis un fichier de ressource
      // XML
      try {

         // Génération de l'en-tête wsse
         String wsse = genererEnTeteWsse(msgCtx);

         SOAPHeader soapHeader = msgCtx.getEnvelope().getHeader();

         soapHeader.addChild(org.apache.axis2.util.XMLUtils
               .toOM(new StringReader(wsse)));

         soapHeader.build();

         StringWriter sWriter = new StringWriter();
         msgCtx.getEnvelope().serialize(sWriter);

      } catch (Exception e) {
         throw new ViSignatureException(e);
      }

      return InvocationResponse.CONTINUE;

   }

   /**
    * Génération de l'en-tête WSSE
    * 
    * @param msgCtx
    *           contexte du message
    * @return l'en-tête à insérer dans le message
    */
   public final String genererEnTeteWsse(MessageContext msgCtx) {

      LOGGER.debug("Début génération en-tête wsse");

      KeyStore keystore;
      String alias, password, mIssuer, mLogin;
      List<String> mPagms;

      if (iKeyStore == null) {

         LOGGER.debug("La construction du VIHandler a été faite par Axis2.");

         if (msgCtx.getParameter(KEY_KEYSTORE) == null) {

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
            String className = (String) msgCtx.getParameter(KEY_KEYSTORE)
                  .getValue();
            KeyStoreInterface keyStoreInterface = getKeystore(className);
            keystore = keyStoreInterface.getKeystore();
            alias = keyStoreInterface.getAlias();
            password = keyStoreInterface.getPassword();
            mIssuer = (String) msgCtx.getParameter(KEY_ISSUER).getValue();
            mLogin = (String) msgCtx.getParameter(KEY_LOGIN).getValue();
            String sPagms = (String) msgCtx.getParameter(KEY_PAGMS).getValue();
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
      DateTime systemDate = new DateTime();
      UUID identifiant = UUID.randomUUID();

      // pour des questions de dérives d'horloges la période de début et de fin
      // de validé du jeton est de 2heures
      DateTime notAfter = systemDate.plusHours(2);
      DateTime notBefore = systemDate.minusHours(2);

      // Trace applicative
      LOGGER
            .debug(
                  "Paramètres pour la génération l'assertion: heure système={}, identifiant={}, notAfter={}, notBefore={}",
                  new Object[] { systemDate, identifiant, notAfter, notBefore });

      // Génération du VI
      String assertion;
      try {

         SAML20Service assertionService = new SAML20Service();

         assertion = assertionService.createAssertion20(mIssuer, mLogin, mPagms,
               notAfter, notBefore, systemDate, identifiant, keystore, alias,
               password);

         LOGGER.debug("Assertion générée: {}", assertion);

      } catch (XmlSignatureException exception) {
         throw new ViSignatureException(exception);
      }

      // Génération de l'en-tête WS-Security
      WSSecurityService wsService = new WSSecurityService();
      String wsseSecurity = wsService.createWSSEHeader(assertion, identifiant);
      LOGGER.debug("En-tête WS-Security généré: {}", wsseSecurity);

      // Renvoie l'en-tête wsse
      LOGGER.debug("Fin génération en-tête wsse");
      return wsseSecurity;

   }

   private KeyStoreInterface getKeystore(String className) {
      try {
         return (KeyStoreInterface) Class.forName(className).getDeclaredMethod(
               KEYSPACE_GET_INSTANCE, new Class[0]).invoke(null, new Object[0]);

      } catch (IllegalArgumentException exception) {
         throw new ViSignatureException(exception);

      } catch (SecurityException exception) {
         throw new ViSignatureException(exception);

      } catch (IllegalAccessException exception) {
         throw new ViSignatureException(exception);

      } catch (InvocationTargetException exception) {
         throw new ViSignatureException(exception);

      } catch (NoSuchMethodException exception) {
         throw new ViSignatureException(exception);

      } catch (ClassNotFoundException exception) {
         throw new ViSignatureException(exception);
      }
   }
}
