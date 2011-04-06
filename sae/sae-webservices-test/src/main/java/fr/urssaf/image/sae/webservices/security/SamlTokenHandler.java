package fr.urssaf.image.sae.webservices.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ws.security.WSConstants;
import org.w3c.dom.Element;

import fr.urssaf.image.sae.vi.service.WebServiceVIService;
import fr.urssaf.image.sae.webservices.util.AuthenticateUtils;

/**
 * Handler pour ajouter le jeton SAML 2.0 dans la la balise WS-security du web
 * service<br>
 * Le handler est configuré dans le fichier <code>META-INF/axis2.xml</code>
 * 
 * <pre>
 * 
 * &lt;phaseOrder type="OutFlow">
 *    ...
 *    &lt;phase name="Security" />
 *    &lt;phase name="SamlToken">
 *          &lt;handler name="SamlTokenHandler"
 *             class="fr.urssaf.image.sae.webservices.security.SamlTokenHandler" /> 
 *    &lt;/phase>
 * &lt;/phaseOrder>
 * 
 * </pre>
 * 
 * 
 */
public class SamlTokenHandler extends AbstractHandler {

   private final WebServiceVIService viService;

   private static KeyStore keystore;

   private static String alias;

   private static String password;

   private static final String ISSUER = "urn:ISSUER_NON_RENSEIGNE";

   static {
      password = "hiUnk6O3QnRN";
      try {
         keystore = KeyStore.getInstance("PKCS12");
         FileInputStream inputStream = new FileInputStream(
               "src/main/resources/Portail_Image.p12");
         try {
            keystore.load(inputStream, password.toCharArray());

         } finally {
            inputStream.close();
         }
         alias = keystore.aliases().nextElement();
      } catch (KeyStoreException e) {
         throw new IllegalStateException(e);
      } catch (FileNotFoundException e) {
         throw new IllegalStateException(e);
      } catch (NoSuchAlgorithmException e) {
         throw new IllegalStateException(e);
      } catch (CertificateException e) {
         throw new IllegalStateException(e);
      } catch (IOException e) {
         throw new IllegalStateException(e);
      }
   }

   /**
    * instanciation de {@link WebServiceVIService}
    */
   public SamlTokenHandler() {
      super();
      this.viService = new WebServiceVIService();
   }

   /**
    * crée un jeton SAML 2.0 en appelant la méthode
    * {@link WebServiceVIService#creerVIpourServiceWeb}<br>
    * Le jeton SAML est ajouté dans la partie WS-Security situé dans l'entête du
    * SOAP
    * 
    * {@inheritDoc}
    * 
    */
   @Override
   public final InvocationResponse invoke(MessageContext msgCtx) throws AxisFault {

      List<String> roles = AuthenticateUtils.getRoles();

      if (CollectionUtils.isNotEmpty(roles)) {

         Element token = this.viService.creerVIpourServiceWeb(roles, ISSUER,
               null, keystore, alias, password);

         SOAPHeader header = msgCtx.getEnvelope().getHeader();

         OMElement security = header.getFirstChildWithName(new QName(
               WSConstants.WSSE_NS, "Security"));

         try {
            //Element element = XMLUtils.parse(token);

            security.addChild(org.apache.axis2.util.XMLUtils.toOM(token));

         } catch (Exception e) {
            throw new IllegalStateException(e);
         }
      }

      return InvocationResponse.CONTINUE;
   }
}
