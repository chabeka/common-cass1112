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

import fr.urssaf.image.sae.client.vi.exception.ViSignatureException;
import fr.urssaf.image.sae.client.vi.exception.XmlSignatureException;
import fr.urssaf.image.sae.client.vi.signature.DefaultKeystore;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.client.vi.ws.SAML20Service;
import fr.urssaf.image.sae.client.vi.ws.WSSecurityService;

/**
 * Handler pour ajouter le Vecteur d'Identification dans l'en-tête SOAP<br>
 * . <br>
 * Ce handler est branché dans le fichier <code>axis2-pnr-sae-appelws.xml</code>
 * 
 */
public class VIHandler extends AbstractHandler {

   public static final String KEYSPACE_GET_INSTANCE = "getInstance";
   public static final String KEY_KEYSTORE = "keystore";
   public static final String KEY_ISSUER = "issuer";
   public static final String KEY_PAGMS = "pagms";
   private static final String DEFAULT_ISSUER = "PNR";
   private static final String DEFAUL_PAGM = "ROLE_TOUS;FULL";

   private KeyStoreInterface iKeyStore = null;
   private List<String> pagms = null;
   private String issuer = null;

   /**
    * Constructeur utilisé lors de la déclaration dans les fichiers AXIS
    */
   public VIHandler() {
      super();
   }

   /**
    * Constructeur utilisé dans le code
    * 
    * @param iKeyStore
    *           keystore à utilisé
    * @param pagms
    *           liste des pagms
    * @param issuer
    *           issuer
    */
   public VIHandler(KeyStoreInterface iKeyStore, List<String> pagms,
         String issuer) {
      this.iKeyStore = iKeyStore;
      this.issuer = issuer;
      this.pagms = pagms;
   }

   /**
    * Création d'une balise WS-Security dans le header du SOAP<br>
    * <br>
    * Insertion du VI dans cet balise WS-Security
    * 
    * {@inheritDoc}
    * 
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
    * Génération de l'entete WSSE
    * 
    * @param msgCtx
    *           contexte du message
    * @return l'entete à insérer dans le message
    */
   public final String genererEnTeteWsse(MessageContext msgCtx) {

      KeyStore keystore;
      String alias, password, mIssuer;
      List<String> mPagms;

      if (iKeyStore == null) {

         if (msgCtx.getParameter(KEY_KEYSTORE) == null) {
            // récupération du keystore par défaut
            keystore = DefaultKeystore.getInstance().getKeystore();
            alias = DefaultKeystore.getInstance().getAlias();
            password = DefaultKeystore.getInstance().getPassword();
            mIssuer = DEFAULT_ISSUER;
            mPagms = Arrays.asList(DEFAUL_PAGM);

         } else {
            String className = (String) msgCtx.getParameter(KEY_KEYSTORE)
                  .getValue();
            KeyStoreInterface keyStoreInterface = getKeystore(className);
            keystore = keyStoreInterface.getKeystore();
            alias = keyStoreInterface.getAlias();
            password = keyStoreInterface.getPassword();
            mIssuer = (String) msgCtx.getParameter(KEY_ISSUER).getValue();
            String sPagms = (String) msgCtx.getParameter(KEY_PAGMS).getValue();
            mPagms = Arrays.asList(sPagms.split(","));
         }

      } else {
         keystore = iKeyStore.getKeystore();
         alias = iKeyStore.getAlias();
         password = iKeyStore.getPassword();
         mPagms = this.pagms;
         mIssuer = this.issuer;
      }

      // instanciation des paramètres du jeton SAML
      DateTime systemDate = new DateTime();
      UUID identifiant = UUID.randomUUID();

      // pour des questions de dérives d'horloges la période de début et de fin
      // de validé du jeton est de 2heures
      DateTime notAfter = systemDate.plusHours(2);
      DateTime notBefore = systemDate.minusHours(2);

      // Génération du VI
      String assertion;
      try {

         SAML20Service assertionService = new SAML20Service();

         assertion = assertionService.createAssertion20(mIssuer, mPagms,
               notAfter, notBefore, systemDate, identifiant, keystore, alias,
               password);

      } catch (XmlSignatureException exception) {
         throw new ViSignatureException(exception);
      }

      // Génération de l'en-tête WS-Security
      WSSecurityService wsService = new WSSecurityService();
      String wsseSecurity = wsService.createWSSEHeader(assertion, identifiant);

      // Renvoie l'en-tête wsse
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
