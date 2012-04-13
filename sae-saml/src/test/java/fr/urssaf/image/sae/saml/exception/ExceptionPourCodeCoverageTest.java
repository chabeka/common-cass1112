package fr.urssaf.image.sae.saml.exception;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class ExceptionPourCodeCoverageTest {

   
   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void testConstructeurs() {
      
      // SamlCoreException
      new SamlCoreException(StringUtils.EMPTY);
      new SamlCoreException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlCoreException(new IllegalArgumentException());
      
      // SamlFormatException
      new SamlFormatException(new IllegalArgumentException());
      
      // SamlVerificationException
      new SamlVerificationException(StringUtils.EMPTY);
      new SamlVerificationException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlVerificationException(new IllegalArgumentException());
      
      // SamlExtractionException
      new SamlExtractionException(StringUtils.EMPTY);
      new SamlExtractionException(StringUtils.EMPTY,new IllegalArgumentException());
      new SamlExtractionException(new IllegalArgumentException());
      
   }
   
   
}
