package fr.urssaf.image.parser_opencsv.application.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "fr.urssaf.image.parser_opencsv.application.repository")
@PropertySource("file:${bnd.config.path}/database.properties")
public class PersistenceJPAConfig {

   @Value("${bnd.jpa.properties.hibernate.dialect}")
   private String driver;

   @Value("${bnd.datasource.username}")
   private String username;

   @Value("${bnd.datasource.password}")
   private String credential;

   @Value("${bnd.datasource.url}")
   private String url;

   @Bean
   public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
      final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
      emf.setDataSource(dataSource());
      emf.setPackagesToScan("fr.urssaf.image.parser_opencsv.application.model.entity");
      final JpaVendorAdapter vendorAdaptor = new HibernateJpaVendorAdapter();
      emf.setJpaVendorAdapter(vendorAdaptor);
      emf.setJpaProperties(additionalProperties());
      return emf;
   }

   @Bean
   public DataSource dataSource() {
      final DriverManagerDataSource datasource = new DriverManagerDataSource();
    // datasource.setDriverClassName(driver);
      datasource.setUsername(username);
      datasource.setPassword(credential);
      datasource.setUrl(url);
      return datasource;
   }

   @Bean
   @Autowired
   public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
      final JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(emf);
      return transactionManager;
   }

   Properties additionalProperties() {
      final Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", "update");
      properties.setProperty("hibernate.dialect", driver);
      return properties;
   }

}
