package fr.urssaf.image.sae.webservices.util;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;

/**
 * Petite régression de la version Axis2 1.6 donc ne plus passer par le fichier
 * axis2.xml mais par une configuration manuelle.
 * 
 * 
 */
public final class SaeServiceStubUtils {

   private SaeServiceStubUtils() {

   }

   /**
    * Renvoie le stub du service web, sans mécanique d'ajout de VI
    * 
    * @return le stub
    */
   public static SaeServiceStub getServiceStub() {

      try {

         // Création d'une configuration Axis2 par défaut
         ConfigurationContext configContext = ConfigurationContextFactory
               .createConfigurationContextFromFileSystem(null, null);

         // Récupération de l'URL des services web SAE depuis le fichier
         // properties
         String urlServiceWeb = ConfigurationUtils
               .litUrlServiceWebDuFichierProperties();

         // Création du Stub
         SaeServiceStub service = new SaeServiceStub(configContext,
               urlServiceWeb);

         // Renvoie du Stub
         return service;

      } catch (Exception e) {
         throw new RuntimeException(e);
      }

   }
}
