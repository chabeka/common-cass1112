package fr.urssaf.image.sae.webservices.factory;

import java.util.Arrays;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;

import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.security.MyKeyStore;

/**
 * Factory du stub pour consommer le service web SAE
 */
public final class StubFactory {

   private static final String URL_WS = "http://frontalint.gidn.recouv/frontalged_be/services/saeService/";

   private static final String ID_CONTRAT_SERVICE = "CS_FRONTAL_GNT";

   private static final List<String> PAGMS = Arrays.asList("PAGM_FRONTAL_GNT_ALL");

   private static final String LOGIN = "AC750xxx";

   private StubFactory() {
      // Constructeur privé
   }

   /**
    * Renvoie le stub pour consommer le service web SAE
    * 
    * @return le stub pour consommer le service web SAE
    * @throws AxisFault
    *            en cas d'erreur générée par Axis2
    */
   public static SaeServiceStub getStub() throws AxisFault {

      // Création d'une configuration Axis2 par défaut
      final ConfigurationContext configContext = ConfigurationContextFactory
                                                                            .createConfigurationContextFromFileSystem(null, null);

      // Création du Stub
      final SaeServiceStub service = new SaeServiceStub(configContext, URL_WS);

      // Récupération de l'objet exposant le PKCS#12
      // Dans un cas "normal", on récupère l'instance créée au démarrage
      // de l'application.
      final KeyStoreInterface keystore = new MyKeyStore();

      // Instancie l'objet de génération du VI
      final Axis2VIHandler handler = new Axis2VIHandler(keystore, PAGMS, ID_CONTRAT_SERVICE, LOGIN);

      // Ajout d'un Handler lors de la phase "MessageOut" pour insérer le VI
      final AxisConfiguration axisConfig = configContext.getAxisConfiguration();
      final List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
      final Phase messageOut = findPhaseByName(outFlowPhases, "MessageOut");
      messageOut.addHandler(handler);

      return service;
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
