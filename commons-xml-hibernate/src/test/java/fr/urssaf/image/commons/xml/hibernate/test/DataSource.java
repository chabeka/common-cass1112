package fr.urssaf.image.commons.xml.hibernate.test;

import java.sql.Driver;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

@Component
public class DataSource extends SimpleDriverDataSource {

   private static Driver driver;
   private static String url;
   private static String username;
   private static String password;

   private static final Logger LOG = Logger.getLogger(DataSource.class);

   static {
      try {
         PropertiesConfiguration jdbc = new PropertiesConfiguration(
               "jdbc.properties");

         String driverClassName = jdbc.getString("jdbc.driverClassName");
         driver = (Driver) BeanUtils
               .instantiate(Class.forName(driverClassName));
         url = jdbc.getString("jdbc.url");
         username = jdbc.getString("jdbc.username");
         password = jdbc.getString("jdbc.password");

      } catch (ConfigurationException e) {
         LOG.error(e.getMessage(), e);
      } catch (BeanInstantiationException e) {
         LOG.error(e.getMessage(), e);
      } catch (ClassNotFoundException e) {
         LOG.error(e.getMessage(), e);
      }

   }

   public DataSource() {
      super(driver, url, username, password);
   }

}
