package fr.urssaf.image.sae.client.vi.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.client.vi.VIGenerator;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.webservices.security.MyKeyStore;

/**
 * Exemple d'utilisation du générateur de VI sans utiliser le Axis2VIHandler via le
 * framework Axis2, mais en appelant "manuellement" la méthode de génération du
 * VI.
 */
public class SansIntegrationAxis2Test {

   private static final Logger LOGGER = LoggerFactory
                                                     .getLogger(SansIntegrationAxis2Test.class);

   /**
    * Ceci n'est pas un vrai TU, cette méthode sert à générer des VI pour les
    * copier/coller dans SoapUI par exemple
    */
   @Test
   public void genereAssertionSansInvokeAxis2() {

      // Le magasin de certificats
      final KeyStoreInterface keystore = new MyKeyStore();

      // Identifiant du contrat de service
      final String issuer = "IdContratService";

      // Liste des PAGMs
      final List<String> pagms = Arrays.asList("PAGM1", "PAGM2");

      // Génération "manuelle" du VI
      final String login = "AC750xxxx";
      final String vi = VIGenerator.genererEnTeteWsse(issuer, login, pagms, keystore.getKeystore(), keystore.getAlias(), keystore.getPassword());

      // On sort le VI dans le log
      LOGGER.debug("VI Généré : \r\n{}", vi);

   }

}
