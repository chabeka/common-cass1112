package sae.client.demo.webservice.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.MessageContextListener;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;

import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.security.MyKeyStore;

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
  private static final String VI_LOGIN = "CER69XXXXX";

  /**
   * Identifiant du Contrat de Service à utiliser dans le Vecteur
   * d'Identification. A Renseigné dans le fichier de properties
   */
  private static final String CONTRAT_SERVICE = "contrat.de.service";

  /**
   * PAGM à utiliser dans le Vecteur d'Identification. A Renseigné dans le fichier de properties
   */
  private static final String PAGM = "pagm";

  private SaeServiceStubFactory() {
    // Constructeur privé
  }

  private static Properties getProperties() {
    final Properties properties = new Properties();
    try {
      properties.load(ResourceUtils.loadResource(new SaeServiceStubFactory(),
                                                 NOM_FICHIER_PROP));
      return properties;
    }
    catch (final IOException e) {
      throw new DemoRuntimeException(e);
    }
  }

  /**
   * Lecture dans le fichier properties de l'URL pointant sur le service web
   * SAE
   *
   * @return URL du service web SAE
   */
  private static String litUrlSaeService() {
    return getProperties().getProperty(CLE_URL_SERVICE);
  }

  private static List<String> listPagmSaeService() {
    final String pagm = getProperties().getProperty(PAGM);
    return Arrays.asList(pagm.split("\\s*,\\s*"));
  }

  /**
   * Création d'un Stub paramétré avec l'authentification au service web du SAE
   *
   * @return le Stub
   */
  public static SaeServiceStub createStubAvecAuthentification() {

    // Lecture du l'URL du service web du SAE depuis le fichier properties
    final String urlSaeService = litUrlSaeService();

    // Création d'une configuration Axis2 par défaut
    ConfigurationContext configContext;
    try {
      configContext = ConfigurationContextFactory
                                                 .createConfigurationContextFromFileSystem(null, null);
    }
    catch (final AxisFault e) {
      throw new DemoRuntimeException(e);
    }

    // Création de l'objet Stub
    SaeServiceStub service;
    try {
      service = new SaeServiceStub(configContext, urlSaeService);

      // Pour pallier au problème MTOM Les explication sur l'url ci-dessous
      // URL : https://issues.apache.org/jira/browse/AXIS2-5202?focusedCommentId=13293399&page=
      // com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#comment-13293399

      service._getServiceClient().getAxisService().addMessageContextListener(new MessageContextListener() {

        @Override
        public void attachServiceContextEvent(final ServiceContext sc, final MessageContext mc) {

        }

        @Override
        public void attachEnvelopeEvent(final MessageContext mc) {
          try {
            mc.getEnvelope().cloneOMElement().serialize(System.out);
          }

          catch (final XMLStreamException e) {
            throw new DemoRuntimeException(e);
          }
        }
      });
    }
    catch (final AxisFault e) {
      throw new DemoRuntimeException(e);
    }

    // Récupération de l'objet exposant le PKCS#12
    // Dans un cas "normal", on récupère l'instance créée au démarrage
    // de l'application.
    final KeyStoreInterface keystore = new MyKeyStore();

    // Instancie l'objet de génération du VI
    final Axis2VIHandler axis2VIHandler = new Axis2VIHandler(keystore,
                                                             listPagmSaeService(),
                                                             getProperties().getProperty(CONTRAT_SERVICE),
                                                             VI_LOGIN);
    // Ajout d'un Handler lors de la phase "MessageOut" pour insérer le VI
    final AxisConfiguration axisConfig = configContext.getAxisConfiguration();
    final List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
    final Phase messageOut = findPhaseByName(outFlowPhases, "MessageOut");
    messageOut.addHandler(axis2VIHandler);

    // Renvoie l'objet Stub
    return service;

  }

  /**
   * Création d'un Stub sans authentification
   *
   * @return le Stub
   */
  public static SaeServiceStub createStubSansAuthentification() {
    try {

      final String urlSaeService = litUrlSaeService();

      return new SaeServiceStub(urlSaeService);

    }
    catch (final AxisFault e) {
      throw new DemoRuntimeException(e);
    }
  }

  private static Phase findPhaseByName(final List<Phase> phases,
                                       final String nomPhaseRecherchee) {

    Phase result = null;

    for (final Phase phase : phases) {
      if (phase.getName().equals(nomPhaseRecherchee)) {
        result = phase;
        break;
      }
    }

    return result;

  }

}
