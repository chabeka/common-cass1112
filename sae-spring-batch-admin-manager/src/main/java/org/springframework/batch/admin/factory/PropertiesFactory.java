package org.springframework.batch.admin.factory;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertiesFactory {

   private static final Logger LOGGER = Logger.getLogger(PropertiesFactory.class);
   
   public PropertyPlaceholderConfigurer load() {
      
      File tempDir = new File(System.getProperty("java.io.tmpdir"), "SpringBatchJobConfigFiles");
      LOGGER.info("batch.job.configuration.file.dir : " + tempDir);
      
      Properties properties = new Properties() ;
      properties.put("batch.job.configuration.file.dir", tempDir.toString());
      
      PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
      ppc.setProperties(properties);
      
      ppc.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");
      ppc.setIgnoreResourceNotFound(true);
      ppc.setIgnoreUnresolvablePlaceholders(true);
      ppc.setOrder(10);
      
      
      return ppc;
      
   }
   
}
