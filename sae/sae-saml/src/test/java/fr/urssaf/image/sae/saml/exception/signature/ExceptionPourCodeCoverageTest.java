package fr.urssaf.image.sae.saml.exception.signature;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class ExceptionPourCodeCoverageTest {

   
   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void testConstructeurs() {
      
      // SamlSignatureException
      new SamlSignatureException(StringUtils.EMPTY);
      new SamlSignatureException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlSignatureException(new IllegalArgumentException());
      
   }
   
   
}
