package sae.client.demo.webservice.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.ws.soap.SOAPBinding;

import fr.urssaf.image.sae.client.vi.util.ResourceUtils;
import sae.client.demo.webservice.modele.SaeService;

/**
 * Factory de création du Stub pour appeler le service web SaeService
 */
public final class SaeServiceStubFactory {

   /**
    * Nom du fichier properties contenant l'URL du service web SAE
    */
   private static final String NOM_FICHIER_PROP = "sae-client-demo.properties";

   /**
    * Clé de la propriété dans le fichier {@link #NOM_FICHIER_PROP} qui contient
    * l'URL du service web SAE
    */
   private static final String CLE_URL_SERVICE = "server.url";

   /**
    * Login de l'utilisateur qui lance l'action.
    */
   public static final String VI_LOGIN = "AC750XXXXX";

   /**
    * Identifiant du Contrat de Service à utiliser dans le Vecteur
    * d'Identification. A Renseigné dans le fichier de properties
    */
   public static final String CONTRAT_SERVICE = "contrat.de.service";

   /**
    * PAGM à utiliser dans le Vecteur d'Identification. A Renseigné dans le fichier de properties
    */
   private static final String PAGM = "pagm";

   private SaeServiceStubFactory() {
      // Constructeur privé
   }

   public static Properties getProperties() throws IOException {
      final Properties properties = new Properties();

      properties.load(ResourceUtils.loadResource(new SaeServiceStubFactory(),
                                                 NOM_FICHIER_PROP));
      return properties;

   }

   /**
    * Lecture dans le fichier properties de l'URL pointant sur le service web
    * SAE
    *
    * @return URL du service web SAE
    * @throws IOException
    */
   public static String litUrlSaeService() throws IOException {
      return getProperties().getProperty(CLE_URL_SERVICE);
   }

   public static List<String> listPagmSaeService() throws IOException {
      final String pagm = getProperties().getProperty(PAGM);
      return Arrays.asList(pagm.split("\\s*,\\s*"));
   }

   /**
    * Création d'un Stub paramétré avec l'authentification au service web du SAE
    *
    * @return le Stub
    * @throws IOException
    */
   public static SaeService createStubAvecAuthentification() throws IOException {

      final SaeService saeService = new SaeService();

      // Ajout du port avec l'url du web service au service
      saeService.addPort(SaeService.SaeServicePort, SOAPBinding.SOAP12HTTP_MTOM_BINDING, litUrlSaeService());

      // Ajout d'un Handler pour insérer le VI dans l'entete
      saeService.setHandlerResolver(new AddViHeaderHandlerResolver());

      return saeService;

   }

   /**
    * Création d'un Stub sans authentification
    *
    * @return le Stub
    * @throws IOException
    */
   public static SaeService createStubSansAuthentification() throws IOException {

      final SaeService saeService = new SaeService();

      // Ajout du port avec l'url du web service au service
      saeService.addPort(SaeService.SaeServicePort, SOAPBinding.SOAP12HTTP_MTOM_BINDING, litUrlSaeService());

      return saeService;

   }

}
