package fr.urssaf.image.sae.saml.exception.signature.keyinfo;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;



public class ExceptionPourCodeCoverageTest {

   
   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void testConstructeurs() {
      
      // SamlKeyInfoException
      new SamlKeyInfoException(StringUtils.EMPTY);
      new SamlKeyInfoException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlKeyInfoException(new IllegalArgumentException());
      
      // SamlKeyInfoMissingException
      new SamlKeyInfoMissingException(StringUtils.EMPTY);
      new SamlKeyInfoMissingException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlKeyInfoMissingException(new IllegalArgumentException());
      
      // SamlX509CertificateMissingException
      new SamlX509CertificateMissingException(StringUtils.EMPTY);
      new SamlX509CertificateMissingException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlX509CertificateMissingException(new IllegalArgumentException());
      new SamlX509CertificateMissingException(StringUtils.EMPTY).getMessage();
      
      // SamlX509ConvertException
      new SamlX509ConvertException(StringUtils.EMPTY);
      new SamlX509ConvertException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlX509ConvertException(new IllegalArgumentException());
      new SamlX509ConvertException(StringUtils.EMPTY).getMessage();
      
      // SamlX509DataMissingException
      new SamlX509DataMissingException();
      new SamlX509DataMissingException(StringUtils.EMPTY);
      new SamlX509DataMissingException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlX509DataMissingException(new IllegalArgumentException());
      new SamlX509DataMissingException(StringUtils.EMPTY).getMessage();
      
   }
   
}
