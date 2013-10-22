package fr.urssaf.image.sae.client.vi.test;

import java.util.Arrays;
import java.util.List;

import org.apache.axis2.context.MessageContext;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.client.vi.VIHandler;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.webservices.security.MyKeyStore;

/**
 * Exemple d'utilisation du générateur de VI sans utiliser le VIHandler via le
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
   @Ignore
   public void genereAssertionSansInvokeAxis2() {

      // Le magasin de certificats
      KeyStoreInterface keystore = new MyKeyStore();

      // Identifiant du contrat de service
      String issuer = "IdContratService";

      // Liste des PAGMs
      List<String> pagms = Arrays.asList("PAGM1", "PAGM2");

      // Génération "manuelle" du VI
      VIHandler handler = new VIHandler(keystore, pagms, issuer);
      MessageContext context = new MessageContext();
      String vi = handler.genererEnTeteWsse(context);

      // On sort le VI dans le log
      LOGGER.debug("VI Généré : \r\n{}", vi);

   }

}
