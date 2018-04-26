package fr.urssaf.image.sae.pile.travaux.ihmweb.utils;

import javax.servlet.http.HttpSession;

import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.CassandraEtZookeeperConfig;

public class ConfigUtils {

   private static final String CLE_LAST_CONFIG = "lastConfig";
   
   public static CassandraEtZookeeperConfig defaultConfig() {
      
      CassandraEtZookeeperConfig config = new CassandraEtZookeeperConfig();
      
      config.setZookeeperHosts("cer69-ds4int.cer69.recouv:2181");
      config.setZookeeperNamespace("SAE");
      
      config.setCassandraHosts("cer69imageint9.cer69.recouv:9160");
      config.setCassandraUserName("root");
      config.setCassandraPassword("regina4932");
      config.setCassandraKeySpace("SAE");
      
      return config;
      
   }
   
   
   public static void putConfigInSession(
         HttpSession session,
         CassandraEtZookeeperConfig config) {
      
      session.setAttribute(CLE_LAST_CONFIG, config);
      
   }
   
   
   public static CassandraEtZookeeperConfig getConfigFromSession(
         HttpSession session) {
      
      Object obj = session.getAttribute(CLE_LAST_CONFIG);
      if (obj==null) {
         return null;
      } else {
         return (CassandraEtZookeeperConfig)obj;
      }

   }
   
}
