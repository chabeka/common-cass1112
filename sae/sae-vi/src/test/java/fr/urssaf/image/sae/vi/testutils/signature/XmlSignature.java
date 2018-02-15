package fr.urssaf.image.sae.vi.testutils.signature;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.vi.testutils.signature.exception.XmlSignatureException;

/**
 * Signature de XML en utilisant l'API standard livrée avec Java : <b>Java XML
 * Digital Signature API</b><br>
 * <br>
 * 
 * @see http://java.sun.com/developer/technicalArticles/xml/dig_signature_api/
 * 
 */
@SuppressWarnings("PMD.ExcessiveImports")
public final class XmlSignature {

   private XmlSignature() {

   }

   /**
    * Signature de XML avec les options suivantes :<br>
    * <br>
    * <table border=1 bordercolor=black cellspacing=0>
    * <tr>
    * <td><b>Option</b></td>
    * <td><b>Valeur</b></td>
    * </tr>
    * <tr>
    * <td>Algorithme de hachage</td>
    * <td>SHA1</td>
    * </tr>
    * <tr>
    * <td>Type de transformation</td>
    * <td>Signature enveloppée</td>
    * </tr>
    * <tr>
    * <td>Canonicalisation</td>
    * <td>Canonicalisation XML exclusive</td>
    * </tr>
    * <tr>
    * <td>Méthode de signature</td>
    * <td>RSAwithSHA1</td>
    * </tr>
    * </table>
    * 
    * @param xmlAsigner
    *           Le XML à signer
    * @param keyStore
    *           Le KeyStore contenant la clé privée, sa clé publique et sa
    *           chaîne de certification
    * @param aliasClePrivee
    *           L'alias de la clé privée dans le KeyStore
    * @param passwordClePrivee
    *           Le mot de passe pour accéder à la clé privée dans le KeyStore
    * @return Le XML avec la signature, sous forme d'une chaîne de caractères
    * @throws XmlSignatureException
    *            une erreur lors du processus de signature
    */
   public static String signeXml(InputStream xmlAsigner, KeyStore keyStore,
         String aliasClePrivee, String passwordClePrivee)
         throws XmlSignatureException {

      // NB : Le code est tiré de l'article suivant :
      // http://java.sun.com/developer/technicalArticles/xml/dig_signature_api/

      // Create a DOM XMLSignatureFactory that will be used to
      // generate the enveloped signature.
      XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

      // Create a Reference to the enveloped document (in this case,
      // you are signing the whole document, so a URI of "" signifies
      // that, and also specify the SHA1 digest algorithm and
      // the ENVELOPED Transform.
      Reference ref = signeXmlCreateReference(fac);

      // Create the SignedInfo.
      SignedInfo signedInfo = signeXmlCreateSignedInfo(fac, ref);

      // Récupération de la clé privée
      PrivateKeyEntry privateKeyEntry = signeXmlGetPrivateKey(keyStore,
            aliasClePrivee, passwordClePrivee);

      // Create the KeyInfo containing the X509Data.
      KeyInfo keyInfo;
      try {
         keyInfo = signeXmlCreateKeyInfo(fac, keyStore, aliasClePrivee);
      } catch (KeyStoreException e) {
         throw new XmlSignatureException(e);
      }

      // Instantiate the document to be signed.
      Document doc = signeXmlInstantiateDoc(xmlAsigner);

      // Signe
      signeXmlsigne(fac, signedInfo, privateKeyEntry, keyInfo, doc);

      // Output the resulting document.
      String result = signeXmlOutputResult(doc);

      // Renvoie du résultat
      return result;

   }

   private static Reference signeXmlCreateReference(XMLSignatureFactory fac)
         throws XmlSignatureException {

      // Create a Reference to the enveloped document (in this case,
      // you are signing the whole document, so a URI of "" signifies
      // that, and also specify the SHA1 digest algorithm and
      // the ENVELOPED Transform.
      Reference ref;
      try {
         ref = fac.newReference("", fac
               .newDigestMethod(DigestMethod.SHA1, null), Collections
               .singletonList(fac.newTransform(Transform.ENVELOPED,
                     (TransformParameterSpec) null)), null, null);
      } catch (GeneralSecurityException ex) {
         throw new XmlSignatureException(ex);
      } 

      return ref;

   }

   private static SignedInfo signeXmlCreateSignedInfo(XMLSignatureFactory fac,
         Reference ref) throws XmlSignatureException {

      // Create the SignedInfo.
      SignedInfo signedInfo;
      try {
         signedInfo = fac.newSignedInfo(fac
               .newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE,
                     (C14NMethodParameterSpec) null), fac.newSignatureMethod(
               SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));
      }  catch (GeneralSecurityException ex) {
         throw new XmlSignatureException(ex);
      }

      return signedInfo;

   }

   private static PrivateKeyEntry signeXmlGetPrivateKey(KeyStore keyStore,
         String aliasClePrivee, String passwordClePrivee)
         throws XmlSignatureException {

      try {

         PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore
               .getEntry(aliasClePrivee, new KeyStore.PasswordProtection(
                     passwordClePrivee.toCharArray()));
         return privateKeyEntry;

      }  catch (GeneralSecurityException ex) {
         throw new XmlSignatureException(ex);
      }

   }

   private static KeyInfo signeXmlCreateKeyInfo(XMLSignatureFactory fac,
         KeyStore keyStore, String aliasClePrivee) throws KeyStoreException {

      // Récupération de la chaîne de certification
      Certificate[] chaineCertif = keyStore.getCertificateChain(aliasClePrivee);

      // Create the KeyInfo containing the X509Data.
      KeyInfoFactory keyInfoFac = fac.getKeyInfoFactory();
      List<X509Certificate> x509Content = new ArrayList<X509Certificate>();
      for (Certificate certif : chaineCertif) {
         x509Content.add((X509Certificate) certif);
      }
      X509Data x509data = keyInfoFac.newX509Data(x509Content);

      return keyInfoFac.newKeyInfo(Collections.singletonList(x509data));

   }

   private static Document signeXmlInstantiateDoc(InputStream xmlAsigner)
         throws XmlSignatureException {

      // Instantiate the document to be signed.
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      Document doc;
      try {
         doc = dbf.newDocumentBuilder().parse(xmlAsigner);
      } catch (SAXException ex) {
         throw new XmlSignatureException(ex);
      } catch (IOException ex) {
         throw new XmlSignatureException(ex);
      } catch (ParserConfigurationException ex) {
         throw new XmlSignatureException(ex);
      }
      return doc;

   }

   private static void signeXmlsigne(XMLSignatureFactory fac,
         SignedInfo signedInfo, PrivateKeyEntry privateKeyEntry,
         KeyInfo keyInfo, Document doc) throws XmlSignatureException {

      // Create a DOMSignContext and specify the RSA PrivateKey and
      // location of the resulting XMLSignature's parent element.
      DOMSignContext dsc = new DOMSignContext(privateKeyEntry.getPrivateKey(),
            doc.getDocumentElement());

      // Create the XMLSignature, but don't sign it yet.
      XMLSignature signature = fac.newXMLSignature(signedInfo, keyInfo);

      // Marshal, generate, and sign the enveloped signature.
      try {
         signature.sign(dsc);
      } catch (MarshalException ex) {
         throw new XmlSignatureException(ex);
      } catch (XMLSignatureException ex) {
         throw new XmlSignatureException(ex);
      }

   }

   private static String signeXmlOutputResult(Document doc)
         throws XmlSignatureException {

      try {

         // Output the resulting document.
         StringWriter stringWriter = new StringWriter();
         TransformerFactory transFactory = TransformerFactory.newInstance();
         Transformer trans = transFactory.newTransformer();
         trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

         trans.transform(new DOMSource(doc), new StreamResult(stringWriter));

         // Renvoie du résultat
         return stringWriter.toString();

      } catch (TransformerException ex) {
         throw new XmlSignatureException(ex);
      }

   }

   /**
    * Vérification d'une signature XML, pour la partie cryptographique
    * uniquement (c'est à dire la comparaison des hash).<br>
    * <br>
    * La clé publique utilisé pour la vérification est le premier certificat
    * X509 trouvé dans la partie KeyInfo de la signature XML
    * 
    * @param xmlsigneAverifier
    *           le XML signé dont il faut vérifier la signature
    * @return true si la signature est cryptographiquement correcte, false dans
    *         le cas contraire
    * @throws XmlSignatureException
    *            si un problème survient lors de la vérification de la signature
    */
   public static Boolean verifieSignatureXmlCrypto(InputStream xmlsigneAverifier)
         throws XmlSignatureException {

      try {

         // Instantiate the document
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setNamespaceAware(true);
         Document doc = dbf.newDocumentBuilder().parse(xmlsigneAverifier);

         // Create a DOM XMLSignatureFactory that will be used to
         // generate the enveloped signature.
         XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

         // Find Signature element.
         NodeList nodeList = doc.getElementsByTagNameNS(XMLSignature.XMLNS,
               "Signature");
         if (nodeList.getLength() == 0) {
            throw new Exception("Cannot find Signature element");
         }

         // Create a DOMValidateContext and specify a KeySelector
         // and document context.
         DOMValidateContext valContext = new DOMValidateContext(
               new X509KeySelector(), nodeList.item(0));

         // Unmarshal the XMLSignature.
         XMLSignature signature = fac.unmarshalXMLSignature(valContext);

         // Validate the XMLSignature.
         boolean coreValidity = signature.validate(valContext);

         // Renvoie du résultat
         return coreValidity;

      } catch (Exception ex) {
         throw new XmlSignatureException(ex);
      }

   }

}
