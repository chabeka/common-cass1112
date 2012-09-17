/**
 * 
 */
package fr.urssaf.image.sae.vi.util;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

/**
 * 
 * 
 */
public class CertificatUtils {

   public static X509Certificate getCertificat() {
      X509Certificate certificate = new X509Certificate() {

         @Override
         public boolean hasUnsupportedCriticalExtension() {
            return false;
         }

         @Override
         public Set<String> getNonCriticalExtensionOIDs() {
            return null;
         }

         @Override
         public byte[] getExtensionValue(String oid) {
            return null;
         }

         @Override
         public Set<String> getCriticalExtensionOIDs() {
            return null;
         }

         @Override
         public void verify(PublicKey key, String sigProvider)
               throws CertificateException, NoSuchAlgorithmException,
               InvalidKeyException, NoSuchProviderException, SignatureException {

         }

         @Override
         public void verify(PublicKey key) throws CertificateException,
               NoSuchAlgorithmException, InvalidKeyException,
               NoSuchProviderException, SignatureException {

         }

         @Override
         public String toString() {
            return null;
         }

         @Override
         public PublicKey getPublicKey() {
            return null;
         }

         @Override
         public byte[] getEncoded() throws CertificateEncodingException {
            return null;
         }

         @Override
         public int getVersion() {
            return 0;
         }

         @Override
         public byte[] getTBSCertificate() throws CertificateEncodingException {
            return null;
         }

         @Override
         public boolean[] getSubjectUniqueID() {
            return null;
         }

         @Override
         public Principal getSubjectDN() {
            return null;
         }

         @Override
         public byte[] getSignature() {
            return null;
         }

         @Override
         public byte[] getSigAlgParams() {
            return null;
         }

         @Override
         public String getSigAlgOID() {
            return null;
         }

         @Override
         public String getSigAlgName() {
            return null;
         }

         @Override
         public BigInteger getSerialNumber() {
            return null;
         }

         @Override
         public Date getNotBefore() {
            return null;
         }

         @Override
         public Date getNotAfter() {
            return null;
         }

         @Override
         public boolean[] getKeyUsage() {
            return null;
         }

         @Override
         public boolean[] getIssuerUniqueID() {
            return null;
         }

         @Override
         public Principal getIssuerDN() {
            return null;
         }

         @Override
         public int getBasicConstraints() {
            return 0;
         }

         @Override
         public void checkValidity(Date date)
               throws CertificateExpiredException,
               CertificateNotYetValidException {

         }

         @Override
         public void checkValidity() throws CertificateExpiredException,
               CertificateNotYetValidException {

         }
      };

      return certificate;

   }
}
