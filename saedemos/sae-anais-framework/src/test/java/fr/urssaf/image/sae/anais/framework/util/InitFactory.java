package fr.urssaf.image.sae.anais.framework.util;

//CHECKSTYLE:OFF

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import fr.urssaf.image.sae.anais.framework.component.AnaisConnectionSupport;
import fr.urssaf.image.sae.anais.framework.component.DataSource;
import fr.urssaf.image.sae.anais.framework.modele.ObjectFactory;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAdresseServeur;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisProfilCompteApplicatif;

public final class InitFactory {

   private InitFactory() {

   }

   public static SaeAnaisAdresseServeur initServeur() {

      SaeAnaisAdresseServeur serveur = ObjectFactory
            .createSaeAnaisAdresseServeur();

      try {

         AbstractConfiguration.setDefaultListDelimiter("|".charAt(0));
         Configuration config = new PropertiesConfiguration(
               AnaisConnectionSupport.ANAIS_CONNECTION);

         serveur.setHote(config.getStringArray("anais.host.Developpement")[0]);
         serveur.setPort(config.getInt("anais.port"));
         serveur.setTimeout(config.getInt("anais.timeout"));
         serveur.setTls(config.getBoolean("anais.tls"));

      } catch (ConfigurationException e) {
         throw new IllegalStateException(e);
      }

      return serveur;
   }

   public static CTD initCTD(String key) {

      CTD ctd = new CTD();

      try {
         Configuration config = new PropertiesConfiguration("ctd.properties");
         Configuration cfg = config.subset(key);

         ctd.setUserLogin(cfg.getString("userLogin"));
         ctd.setUserPassword(cfg.getString("userPassword"));

      } catch (ConfigurationException e) {
         throw new IllegalStateException(e);
      }

      return ctd;

   }

   public static SaeAnaisProfilCompteApplicatif initCompteApplicatif() {

      SaeAnaisProfilCompteApplicatif profil = ObjectFactory
            .createSaeAnaisProfilCompteApplicatif();
      try {

         AbstractConfiguration.setDefaultListDelimiter("|".charAt(0));
         Configuration config = new PropertiesConfiguration("profil.properties");

         Configuration cfg = config.subset("profil0");

         profil.setCodeApplication(cfg.getString("code-application"));
         profil.setDn(cfg.getString("cn"));
         profil.setPassword(cfg.getString("compte-applicatif-password"));

      } catch (ConfigurationException e) {
         throw new IllegalStateException(e);
      }
      
      return profil;
   }
   
   public static DataSource initDataSource(){
      
      DataSource dataSource = new DataSource();
      
      try {
         AbstractConfiguration.setDefaultListDelimiter("|".charAt(0));
         Configuration config = new PropertiesConfiguration(
               AnaisConnectionSupport.ANAIS_CONNECTION);

         dataSource.setCodeapp(config.getString("anais.code-application"));
         dataSource.setPasswd(config
               .getString("anais.compte-applicatif-password"));
         dataSource.setCodeenv("PROD");
         dataSource.setAppdn(config.getString("anais.cn"));
         dataSource.setPort(config.getInt("anais.port"));

         dataSource.setHostname(config.getStringArray("anais.host.Developpement")[0]);
         dataSource.setTimeout(config.getString("anais.timeout"));
         dataSource.setUsetls(config.getBoolean("anais.tls"));
         
         dataSource.setComptePortail(config.getString("anais.comptePortail"));
         dataSource.setDroitsDirect(config.getBoolean("anais.activerDroitsDirects"));

      } catch (ConfigurationException configException) {
         throw new IllegalStateException(configException);
      }
      
      return dataSource;

   }
}

//CHECKSTYLE:ON
