package fr.urssaf.image.sae.webservices.security;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.ClassPathResource;


public class MyKeystoreProvider {

   public static final MyKeyStore portailImageKeyStore;
   
   
   static {
      
      // Chargement du keystore Portail_Image.p12
      portailImageKeyStore = chargeKeyStorePortailImage();
      
   }
   
   
   private static MyKeyStore chargeKeyStorePortailImage() {
      
      String idCertif = "PortailImage";
      
      String cheminPkcs12 = "Portail_Image.p12";
      String password = "hiUnk6O3QnRN";
      KeyStore keyStore = chargeKeyStore(cheminPkcs12, password);
      
      String aliasClePrivee = trouveAliasClePrivee(keyStore);
      
      MyKeyStore myKeyStore = new MyKeyStore(idCertif, keyStore, password, aliasClePrivee);
      
      return myKeyStore;
      
   }
   
   
   private static KeyStore chargeKeyStore(
         String cheminPkcs12dansRessource,
         String password)  {
      
      try {
         
         ClassPathResource resource = new ClassPathResource(cheminPkcs12dansRessource);
         
         KeyStore keystore = KeyStore.getInstance("PKCS12");
         
         keystore.load(resource.getInputStream(), password.toCharArray());
         
         return keystore;
      
      } catch (Exception ex) {
         throw new RuntimeException(ex);
      }

   }
   
   private static String trouveAliasClePrivee(KeyStore keystore) {
      
      List<String> aliases;
      try {
         
         aliases = Collections.list(keystore.aliases());
         
         for (String alias: aliases) {
            if (keystore.isKeyEntry(alias)) {
               return alias;
            }
         }
         
         // Si pas trouvé, on lève une exception
         throw new RuntimeException("erreur improbable");
         
      } catch (KeyStoreException e) {
         throw new RuntimeException(e);
      }
      
   }
   
}
