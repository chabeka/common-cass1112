package fr.urssaf.image.sae.saml.opensaml.signature;

import java.security.cert.CertificateException;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.X509Certificate;
import org.opensaml.xml.signature.X509Data;

import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlKeyInfoException;
import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlKeyInfoMissingException;
import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlX509ConvertException;
import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlX509DataMissingException;
import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlX509CertificateMissingException;


/**
 * Méthodes utilitaires pour la manipulation de la signature Opensaml
 */
public final class SamlSignatureUtils {

   
   private SamlSignatureUtils() {
      
   }
   
   
   /**
    * Lecture du certificat X509 contenant la clé publique associée à la clé privée
    * qui a servi à signer l'assertion SAML.
    * <br>
    * Par convention, il s'agit du premier certificat X509 de la partie KeyInfo de
    * la signature.<br>
    * 
    * 
    * @param signature la signature présente dans l'assertion
    * 
    * @return Le certificat contenant la clé publique
    *  
    * @throws SamlKeyInfoException
    *    en cas de problème lors de la lecture du certificat 
    *    contenant la clé publique
    */
   public static X509Certificate getPublicKey(Signature signature)
      throws SamlKeyInfoException {

      /* Rappel : Format d'une signature XML :
      
      <ds:Signature xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
         <ds:SignedInfo>
            [...]
         </ds:SignedInfo>
         <ds:SignatureValue>
            [...]
         </ds:SignatureValue>
         <ds:KeyInfo>
            <ds:X509Data>
               <ds:X509Certificate>MIICIjANBgkqhkiG9w0BAQEF ...</ds:X509Certificate>
               [...]
            </ds:X509Data>
         </ds:KeyInfo> 
      </ds:Signature>       
       
       */
      
      // Vérifications des paramètres d'entrée
      if (signature==null) {
         throw new NullArgumentException("signature");
      }
      
      // Récupération de la partie <KeyInfo> de la signature
      KeyInfo keyInfo = signature.getKeyInfo();
      if (keyInfo==null) {
         throw new SamlKeyInfoMissingException();
      }
      
      // Récupération du premier <X509Data> de la partie <KeyInfo>
      List<X509Data> x509dataList = keyInfo.getX509Datas();
      if (x509dataList==null) {
         throw new SamlX509DataMissingException();
      }
      if (x509dataList.isEmpty()) {
         throw new SamlX509DataMissingException();
      }
      X509Data x509data = x509dataList.get(0);
      
      // Récupération du 1er certificat X509 de la partie <X509Data>
      List<X509Certificate> x509CertList = x509data.getX509Certificates();
      if (x509CertList==null) {
         throw new SamlX509CertificateMissingException();
      }
      if (x509CertList.isEmpty()) {
         throw new SamlX509CertificateMissingException();
      }
      X509Certificate publicKey = x509CertList.get(0);
      
      // Traces
      // LOG.debug("Clé publique trouvée dans l'assertion SAML : " + publicKey.getValue());
      
      // Renvoie du résultat
      return publicKey;
      
   }
   
   
   /**
    * Lecture du certificat X509 contenant la clé publique associée à la clé privée
    * qui a servi à signer l'assertion SAML.
    * <br>
    * Par convention, il s'agit du premier certificat X509 de la partie KeyInfo de
    * la signature.<br>
    * 
    * 
    * @param signature la signature présente dans l'assertion
    * 
    * @return Le certificat contenant la clé publique
    *  
    * @throws SamlKeyInfoException
    *    en cas de problème lors de la lecture du certificat 
    *    contenant la clé publique
    */
   public static java.security.cert.X509Certificate getPublicKeyNatif(Signature signature)
      throws SamlKeyInfoException {
      
      X509Certificate publicKey = getPublicKey(signature);
      
      try {
         java.security.cert.X509Certificate certNatif =
            KeyInfoHelper.getCertificate(publicKey);
         return certNatif;
      } catch (CertificateException e) {
         throw new SamlX509ConvertException(e);
      }
      
   }
   
   
   /**
    * Conversion d'un certificat X509 au format OpenSaml ({@link org.opensaml.xml.signature.X509Certificate})
    * vers un certificat X509 au format natif de l'API Java ({@link java.security.cert.X509Certificate})
    * 
    * @param certificat le certificat X509 au format OpenSaml
    * @return le certificat X509 au format natif de l'API Java
    * @throws SamlX509ConvertException si une erreur de conversion se produit
    */
   public static java.security.cert.X509Certificate convertX509(
         X509Certificate certificat)
      throws 
         SamlX509ConvertException {
      try {
         java.security.cert.X509Certificate certNatif =
            KeyInfoHelper.getCertificate(certificat);
         return certNatif;
      } catch (CertificateException e) {
         throw new SamlX509ConvertException(e);
      }
   }
   
}
