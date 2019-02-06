/**
 * 
 */
package fr.urssaf.image.commons.maquette.util;

import java.io.File;
import java.io.IOException;

import javax.naming.NamingException;

import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.mock.web.MockFilterConfig;

/**
 * 
 * 
 */
public class JndiSupport {

   static private File file;
   static private SimpleNamingContextBuilder builder;
   static private MockFilterConfig filterConfig;

   static {
      try {
         builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();

         file = File.createTempFile("confFile_success", ".properties");
         builder.bind("java:comp/env/confFile", file.getAbsolutePath());
         builder.activate();

         filterConfig = new MockFilterConfig();
         filterConfig.addInitParameter("fichierProprietes", "confFile");

      } catch (IllegalStateException e) {
         throw new RuntimeException(e);

      } catch (NamingException e) {
         throw new RuntimeException(e);

      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static File getFile() {
      return file;
   }

   public static void setFile(File fileToSet) {
      file = fileToSet;
   }

   public static final MockFilterConfig getFilterConfig() {
      return filterConfig;
   }

   public static final void setFilterConfig(MockFilterConfig filterConfig) {
      JndiSupport.filterConfig = filterConfig;
   }

   
}
