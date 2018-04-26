package fr.urssaf.image.sae.webservices.security;

import java.security.KeyStore;

public class MyKeyStore {

   private String idCertif;
   private KeyStore keystore;
   private String password;
   private String aliasClePrivee;
   
   public MyKeyStore() {
      
   }
   
   public MyKeyStore(String idCertif, KeyStore keystore, String password, String aliasClePrivee) {
      this.idCertif = idCertif;
      this.keystore = keystore;
      this.password = password;
      this.aliasClePrivee = aliasClePrivee;
   }
   
   public final KeyStore getKeystore() {
      return keystore;
   }
   public final void setKeystore(KeyStore keystore) {
      this.keystore = keystore;
   }
   public final String getIdCertif() {
      return idCertif;
   }
   public final void setIdCertif(String idCertif) {
      this.idCertif = idCertif;
   }

   public final String getPassword() {
      return password;
   }

   public final void setPassword(String password) {
      this.password = password;
   }

   public final String getAliasClePrivee() {
      return aliasClePrivee;
   }

   public final void setAliasClePrivee(String aliasClePrivee) {
      this.aliasClePrivee = aliasClePrivee;
   }
   
}
