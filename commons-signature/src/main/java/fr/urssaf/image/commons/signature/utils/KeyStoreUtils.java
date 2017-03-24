package fr.urssaf.image.commons.signature.utils;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.log4j.Logger;

/**
 * Fonctions utilitaires pour la gestion des KeyStore
 * 
 */
public final class KeyStoreUtils {

   private KeyStoreUtils() {

   }

   
   /**
    * Affiche dans Logger de Debug le contenu du KeyStore passé en paramètre
    * 
    * @param logger Le Logger à utiliser
    * @param keystore Le KeyStore à étudier
    * @throws KeyStoreException en cas de problème lors de la lecture du KeyStore
    */
   public static void logContenuKeyStore(
         Logger logger,
         KeyStore keystore) 
   throws KeyStoreException {

      if (logger == null) {
         throw new IllegalArgumentException("logger est null");
      }

      if (keystore == null) {
         logger.debug("Le KeyStore est null");
      } else {

         logger.debug("Le KeyStore est renseigné");
         logger.debug("Type: " + keystore.getType());
         logger.debug("Provider.Name: " + keystore.getProvider().getName());
         logger.debug("Provider.Info: " + keystore.getProvider().getInfo());

         Enumeration<String> aliases = keystore.aliases();
         if (aliases == null) {
            logger.debug("Le KeyStore ne contient aucun Alias");
         } else {
            logger.debug("Les alias : ");
            String alias;
            Boolean bClePrivee;
            Boolean bCertifConfiance;
            while (aliases.hasMoreElements()) {

               // Le nom de l'alias
               alias = aliases.nextElement();
               logger.debug("  Alias '" + alias + "'");

               // Est-ce un certificat ?
               bCertifConfiance = keystore.isCertificateEntry(alias);
               if (bCertifConfiance) {
                  logger.debug("    Est-ce un certificat de confiance : OUI");
               } else {
                  logger.debug("    Est-ce un certificat de confiance : NON");
               }

               // Est-ce une clé privée ?
               bClePrivee = keystore.isKeyEntry(alias);
               if (bClePrivee) {
                  logger.debug("    Est-ce une clé privée : OUI");
               } else {
                  logger.debug("    Est-ce une clé privée : NON");
               }

               // Si l'alias est une clé privée, on tente d'afficher
               // la chaîne de certification
               if (bClePrivee) {
                  logCertificateChain(logger,keystore,alias,"    ");
               }

            }
         }
      }

   }
   
   
   private static void logCertificateChain(
         Logger logger,
         KeyStore keystore,
         String alias,
         String indentation) throws KeyStoreException {

      Certificate[] chaineCertif = keystore.getCertificateChain(alias);
      if (chaineCertif == null) {
         logger
               .debug(indentation + "La chaîne de certification de la clé privée est null");
      } else {
         logger
               .debug(indentation + "La chaîne de certification de la clé privée contient "
                     + chaineCertif.length + " certificats");
         for (int i = 0; i < chaineCertif.length; i++) {
            Certificate certificat = chaineCertif[i];
            logger.debug("");
            logger.debug(indentation + "  Certificat #" + (i + 1) + " Type "
                  + certificat.getType());
            if (certificat.getType().equals("X.509")) {
               X509Certificate certX509 = (X509Certificate) certificat;
               logger.debug(indentation + "  Certificat #" + (i + 1) + " SubjectDN = "
                     + certX509.getSubjectDN());
               logger.debug(indentation + "  Certificat #" + (i + 1) + " IssuerDN  = "
                     + certX509.getIssuerDN());
            }
         }
      }
      
   }

}
