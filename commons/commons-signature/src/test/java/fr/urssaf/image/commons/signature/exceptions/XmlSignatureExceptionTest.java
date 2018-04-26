package fr.urssaf.image.commons.signature.exceptions;

import org.junit.Test;

public class XmlSignatureExceptionTest  {

   @Test
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void samlFormatException() {
      new XmlSignatureException(new IllegalArgumentException("test"));
   }
   
   
}
