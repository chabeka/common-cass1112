package fr.urssaf.image.sae.saml.exception.signature.validate;

import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class ExceptionPourCodeCoverageTest {

   
   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void testConstructeurs() {
      
      // SamlAutoSignedCertificateException
      new SamlAutoSignedCertificateException();
      new SamlAutoSignedCertificateException(StringUtils.EMPTY);
      new SamlAutoSignedCertificateException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlAutoSignedCertificateException(new IllegalArgumentException());
      new SamlAutoSignedCertificateException().getMessage();
      
      // SamlIssuerPatternException
      new SamlIssuerPatternException("issuer", Collections.singletonList("pattern"));
      
      // SamlNotAutoSignedCertificateException
      new SamlNotAutoSignedCertificateException("subject","issuer");
      
      // SamlSignatureCryptoException
      new SamlSignatureCryptoException();
      new SamlSignatureCryptoException(StringUtils.EMPTY);
      new SamlSignatureCryptoException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlSignatureCryptoException(new IllegalArgumentException());
      
      // SamlSignatureKeyInfoException
      new SamlSignatureKeyInfoException();
      new SamlSignatureKeyInfoException(StringUtils.EMPTY);
      new SamlSignatureKeyInfoException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlSignatureKeyInfoException(new IllegalArgumentException());
      new SamlSignatureKeyInfoException().getMessage();
      
      // SamlSignatureNonConformeAuProfilException
      new SamlSignatureNonConformeAuProfilException();
      new SamlSignatureNonConformeAuProfilException(StringUtils.EMPTY);
      new SamlSignatureNonConformeAuProfilException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlSignatureNonConformeAuProfilException(new IllegalArgumentException());
      new SamlSignatureNonConformeAuProfilException().getMessage();
      
      // SamlSignatureNotFoundException
      new SamlSignatureNotFoundException();
      new SamlSignatureNotFoundException(StringUtils.EMPTY);
      new SamlSignatureNotFoundException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlSignatureNotFoundException(new IllegalArgumentException());
      new SamlSignatureNotFoundException().getMessage();
      
      // SamlSignatureValidateException
      new SamlSignatureValidateException(StringUtils.EMPTY);
      new SamlSignatureValidateException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlSignatureValidateException(new IllegalArgumentException());
      
   }
   
   
}
