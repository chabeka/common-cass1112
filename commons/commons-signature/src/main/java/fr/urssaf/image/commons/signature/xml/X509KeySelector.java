package fr.urssaf.image.commons.signature.xml;

import java.security.Key;
import java.security.PublicKey;
import java.util.Iterator;

import java.security.cert.X509Certificate;
import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.X509Data;


/**
 * Implémentation de l'interface {@link javax.xml.crypto.KeySelector} pour les besoins de
 * la classe {@link fr.urssaf.image.commons.signature.xml.XmlSignature}
 *
 */
class X509KeySelector extends KeySelector {
   
   
   /**
    * Attempts to find a key that satisfies the specified constraints.
    *
    * @param keyInfo a <code>KeyInfo</code> (may be <code>null</code>)
    * @param purpose the key's purpose ({@link Purpose#SIGN}, 
    *    {@link Purpose#VERIFY}, {@link Purpose#ENCRYPT}, or 
    *    {@link Purpose#DECRYPT})
    * @param method the algorithm method that this key is to be used for.
    *    Only keys that are compatible with the algorithm and meet the 
    *    constraints of the specified algorithm should be returned.
    * @param context an <code>XMLCryptoContext</code> that may contain
    *    useful information for finding an appropriate key. If this key 
    *    selector supports resolving {@link RetrievalMethod} types, the 
    *    context's <code>baseURI</code> and <code>dereferencer</code> 
    *    parameters (if specified) should be used by the selector to 
    *    resolve and dereference the URI.
    * @return the result of the key selector
    * @throws KeySelectorException if an exceptional condition occurs while 
    *    attempting to find a key. Note that an inability to find a key is not 
    *    considered an exception (<code>null</code> should be
    *    returned in that case). However, an error condition (ex: network 
    *    communications failure) that prevented the <code>KeySelector</code>
    *    from finding a potential key should be considered an exception.
    * @throws ClassCastException if the data type of <code>method</code> 
    *    is not supported by this key selector
    */
   @SuppressWarnings("unchecked")
   public KeySelectorResult select(
         KeyInfo keyInfo,
         KeySelector.Purpose purpose, 
         AlgorithmMethod method,
         XMLCryptoContext context) throws KeySelectorException {
      Iterator keyInfoIterator = keyInfo.getContent().iterator();
      while (keyInfoIterator.hasNext()) {
         XMLStructure info = (XMLStructure) keyInfoIterator.next();
         if (!(info instanceof X509Data)) {
            continue;
         }
         X509Data x509Data = (X509Data) info;
         Iterator x509iterator = x509Data.getContent().iterator();
         while (x509iterator.hasNext()) {
            Object obj = x509iterator.next();
            if (!(obj instanceof X509Certificate)) {
               continue;
            }
            final PublicKey key = ((X509Certificate) obj).getPublicKey();
            // Make sure the algorithm is compatible
            // with the method.
            if (algEquals(method.getAlgorithm(), key.getAlgorithm())) {
               return new KeySelectorResult() {
                  
                  /**
                   * Returns the selected key.
                   *
                   * @return the selected key, or <code>null</code> if none can be found
                   */
                  public Key getKey() {
                     return key;
                  }
               };
            }
         }
      }
      throw new KeySelectorException("No key found!");
   }

   private static boolean algEquals(String algURI, String algName) {
      return ((algName.equalsIgnoreCase("DSA") && algURI
            .equalsIgnoreCase(SignatureMethod.DSA_SHA1))
            || (algName.equalsIgnoreCase("RSA") && algURI
                  .equalsIgnoreCase(SignatureMethod.RSA_SHA1))) ;
   }

}
