package fr.urssaf.image.sae.saml.opensaml.signature;

import static org.junit.Assert.assertEquals;

import java.security.cert.CertificateException;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.lang.NullArgumentException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.X509Certificate;

import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlKeyInfoException;
import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlKeyInfoMissingException;
import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlX509CertificateMissingException;
import fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlX509DataMissingException;
import fr.urssaf.image.sae.saml.opensaml.SamlConfiguration;
import fr.urssaf.image.sae.saml.testutils.TuUtils;

/**
 * Tests unitaires de la classe {@link SamlSignatureUtils}
 */
@SuppressWarnings("PMD.MethodNamingConventions")
public class SamlSignatureUtilsTest {

   
   @Before
   public void prepare() {
      new SamlConfiguration();
   }
   
   
   /**
    * Test unitaire de la méthode {@link SamlSignatureUtils#getPublicKey(org.opensaml.xml.signature.Signature)}<br>
    * <br>
    * Cas de test : Le paramètre <code>signature</code> passé à la méthode est <code>null</code><br>
    * <br>
    * Résultat attendu : Levée d'une exception de type {@link org.apache.commons.lang.NullArgumentException}
    *  
    * @throws SamlKeyInfoException 
    * 
    */
   @Test(expected=NullArgumentException.class)
   public void getPublicKey_test01() throws SamlKeyInfoException {
      
      SamlSignatureUtils.getPublicKey(null);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link SamlSignatureUtils#getPublicKey(org.opensaml.xml.signature.Signature)}<br>
    * <br>
    * Cas de test : La signature ne contient pas la partie &lt;KeyInfo&gt;<br>
    * <br>
    * Résultat attendu : Levée d'une exception de type {@link fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlKeyInfoMissingException}
    *  
    * @throws SamlKeyInfoException 
    * 
    */
   @Test(expected=SamlKeyInfoMissingException.class)
   public void getPublicKey_test02() throws SamlKeyInfoException {
      
      // Chargement de la signature XML depuis un fichier de test
      Signature signature = TuUtils.loadSignature(
            "src/test/resources/getPublicKey/test02.xml");
      
      // Appel de la méthode à tester
      SamlSignatureUtils.getPublicKey(signature);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link SamlSignatureUtils#getPublicKey(org.opensaml.xml.signature.Signature)}<br>
    * <br>
    * Cas de test : La partie &lt;X509Data&gt; est manquante dans la partie &lt;KeyInfo&gt;<br>
    * <br>
    * Résultat attendu : Levée d'une exception de type {@link fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlX509DataMissingException}
    *  
    * @throws SamlKeyInfoException 
    * 
    */
   @Test(expected=SamlX509DataMissingException.class)
   @Ignore("Le test échoue : il semblerait que OpenSaml ajoute automatiquement une zone X509Data")
   public void getPublicKey_test03() throws SamlKeyInfoException {
      
      // Chargement de la signature XML depuis un fichier de test
      Signature signature = TuUtils.loadSignature(
            "src/test/resources/getPublicKey/test03.xml");
      
      // Appel de la méthode à tester
      SamlSignatureUtils.getPublicKey(signature);
      
   }
   
   
   
   /**
    * Test unitaire de la méthode {@link SamlSignatureUtils#getPublicKey(org.opensaml.xml.signature.Signature)}<br>
    * <br>
    * Cas de test : Aucun &lt;X509Certificate&gt; dans la partie &lt;X509Data&gt;<br>
    * <br>
    * Résultat attendu : Levée d'une exception de type {@link fr.urssaf.image.sae.saml.exception.signature.keyinfo.SamlX509CertificateMissingException}
    *  
    * @throws SamlKeyInfoException 
    * 
    */
   @Test(expected=SamlX509CertificateMissingException.class)
   public void getPublicKey_test04() throws SamlKeyInfoException {
      
      // Chargement de la signature XML depuis un fichier de test
      Signature signature = TuUtils.loadSignature(
            "src/test/resources/getPublicKey/test04.xml");
      
      // Appel de la méthode à tester
      SamlSignatureUtils.getPublicKey(signature);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link SamlSignatureUtils#getPublicKey(org.opensaml.xml.signature.Signature)}<br>
    * <br>
    * Cas de test : cas normal<br>
    * <br>
    * Résultat attendu : On récupère le bon certificat
    *  
    * @throws SamlKeyInfoException 
    * @throws CertificateException 
    * 
    */
   @Test
   public void getPublicKey_test05() throws SamlKeyInfoException, CertificateException {
      
      // Chargement de la signature XML depuis un fichier de test
      Signature signature = TuUtils.loadSignature(
            "src/test/resources/getPublicKey/test05.xml");
      
      // Appel de la méthode à tester
      X509Certificate certificat = SamlSignatureUtils.getPublicKey(signature);
      
      // Vérifications du résultat
      java.security.cert.X509Certificate certNatif = KeyInfoHelper.getCertificate(certificat);
      assertEquals(
            "La clé publique récupérée est incorrecte : Le SubjectDN ne correspond pas",
            "CN=Portail Image,OU=Applications,O=ACOSS,L=Paris,ST=France,C=FR",
            certNatif.getSubjectX500Principal().getName(X500Principal.RFC2253));
      assertEquals(
            "La clé publique récupérée est incorrecte : Le IssuerDN ne correspond pas",
            "CN=AC Applications,O=ACOSS,L=Paris,ST=France,C=FR",
            certNatif.getIssuerX500Principal().getName(X500Principal.RFC2253));
      
   }
   
   
}
