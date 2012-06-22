package fr.urssaf.image.sae.saml.opensaml.signature;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorManager;
import org.opensaml.xml.security.keyinfo.NamedKeyInfoGeneratorManager;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.security.x509.X509KeyInfoGeneratorFactory;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;

import fr.urssaf.image.sae.saml.opensaml.SamlXML;


/**
 * Classe de service pour la signature des assertions SAML 2.0<br>
 * <br>
 * Le recours à cette classe nécessite une instanciation au préalable de
 * {@link fr.urssaf.image.sae.saml.opensaml.SamlConfiguration}
 * 
 * 
 */
public class SamlSignatureSignService {

   
   /**
    * Signature du jeton SAML<br>
    * <br>
    * La méthode instancie un objet {@link Signature} attaché ensuite à l'objet
    * {@link Assertion}<br>
    * <br>
    * algo de signature : RSAwithSHA1<br>
    * algo de hachage : SHA1<br>
    * algo de canonicalisation XML : Canonicalisation XML exclusive<br>
    * <br>
    * tutorial pour l'implémentation d'une signature dans un jeton SAML <a href="https://spaces.internet2.edu/display/OpenSAML/OSTwoUserManJavaDSIG#OSTwoUserManJavaDSIG-SigningExamples"
    * />exemple de code</a><br>
    * <br>
    * 
    * @param assertion
    *           jeton SAML à signer
    * @param credential
    *           contient les différents certificats nécessaire à la signature
    */
   public final void sign(Assertion assertion, X509Credential credential) {

      Signature signature = SamlXML.create(Signature.DEFAULT_ELEMENT_NAME);

      signature.setSigningCredential(credential);
      // la signature est "RSAwithSHA1"
      signature
            .setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);

      // la canonicalisation XML est "Canonicalisation XML exclusive"
      signature
            .setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

      try {
         signature.setKeyInfo(getKeyInfo(credential));
      } catch (SecurityException e) {
         throw new IllegalStateException(e);
      }

      assertion.setSignature(signature);

      try {
         Configuration.getMarshallerFactory().getMarshaller(assertion)
               .marshall(assertion);
      } catch (MarshallingException e) {
         throw new IllegalStateException(e);
      }

      try {
         Signer.signObject(signature);
      } catch (SignatureException e) {
         throw new IllegalStateException(e);
      }

   }

   private KeyInfo getKeyInfo(X509Credential credential) throws SecurityException {

      SecurityConfiguration secConfig = 
         Configuration.getGlobalSecurityConfiguration();
      
      NamedKeyInfoGeneratorManager namedKiGenMan = 
         secConfig.getKeyInfoGeneratorManager();
      
      KeyInfoGeneratorManager keyInfoGenMan =
         namedKiGenMan.getDefaultManager();
      
      X509KeyInfoGeneratorFactory keyInfoGenFac =
         (X509KeyInfoGeneratorFactory) keyInfoGenMan.getFactory(credential);
      keyInfoGenFac.setEmitEntityCertificateChain(true);
      
      KeyInfoGenerator keyInfoGenerator = keyInfoGenFac.newInstance();
      
      KeyInfo keyInfo = keyInfoGenerator.generate(credential);
      
      return keyInfo;
   }

}
