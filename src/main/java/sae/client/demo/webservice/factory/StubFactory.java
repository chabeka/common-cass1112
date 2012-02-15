package sae.client.demo.webservice.factory;

import java.io.IOException;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.modele.SaeServiceStub;


/**
 * Factory de création du Stub pour appeler le service web SaeService
 */
public class StubFactory {

   
   private static final String NOM_FICHIER_PROP = "sae-client-demo.properties";
   
   private static final String CLE_URL_SERVICE = "server.url";
   
   
   private static String litUrlSaeService(){
      Properties properties = new Properties();
      try {
         properties.load(ResourceUtils.loadResource(new StubFactory(), NOM_FICHIER_PROP));
         return properties.getProperty(CLE_URL_SERVICE);
      } catch (IOException e) {
         throw new DemoRuntimeException(e);
      }
   }
   
   
   /**
    * Création d'un Stub paramétré avec l'authentification au service web du SAE 
    * 
    * @param urlServiceWeb l'URL du service web
    * @return le Stub
    */
   public static SaeServiceStub createStubAvecAuthentification() {
      
      // Le fichier de configuration "axis2.xml" contient
      //  la mécanique qui permet de brancher une classe avant l'envoi du message SOAP
      // Cette classe est chargé de générer la partie authentification (VI) et de l'insérer
      //  dans l'en-tête SOAP
      
      ConfigurationContext configContext;
      try {
         configContext = ConfigurationContextFactory.createBasicConfigurationContext("axis2.xml");
      } catch (Exception e) {
         throw new DemoRuntimeException(e);
      }
      
      String urlSaeService = litUrlSaeService();
      
      SaeServiceStub service;
      try {
         service = new SaeServiceStub(
               configContext,
               urlSaeService);
      } catch (AxisFault e) {
         throw new DemoRuntimeException(e);
      }
      
      return service;
      
   }
   
   
   /**
    * Création d'un Stub sans authentification
    * 
    * @return le Stub
    */
   public static SaeServiceStub createStubSansAuthentification() {
      try {
         
         String urlSaeService = litUrlSaeService();
         
         return new SaeServiceStub(urlSaeService);
         
      } catch (AxisFault e) {
         throw new DemoRuntimeException(e);
      }
   }
   
}
