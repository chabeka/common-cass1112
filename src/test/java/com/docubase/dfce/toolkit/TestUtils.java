package com.docubase.dfce.toolkit;

import java.io.File;
import java.io.InputStream;

public class TestUtils {

   /**
    * Get an InputStream from the given system resource name.
    * 
    * @param resourceName
    *           the file name
    * @return the input stream
    */
   public static InputStream getInputStream(String resourceName) {
      return ClassLoader.getSystemResourceAsStream(resourceName);
   }

   /**
    * Load a file from the given system resource name.
    * 
    * @param resourceName
    *           the file name
    * @return the file
    */
   public static File getFile(String resourceName) {
      return new File(ClassLoader.getSystemResource(resourceName).getPath());
   }

   /**
    * Return the "default.pdf" file located in test resources.
    * <p>
    * This PDF has 10 pages.
    * 
    * @return the default file
    */
   public static File getDefaultFile() {
      return getFile("default.pdf");
   }

}
