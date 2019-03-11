

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.client.vi.VIGenerator;
import fr.urssaf.image.sae.client.vi.signature.DefaultKeystore;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;

/**
 * Exemple d'utilisation du générateur de VI sans utiliser le VIHandler via le
 * framework Axis2, mais en appelant "manuellement" la méthode de génération du
 * VI.
 */
public class SansIntegrationAxis2Test {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SansIntegrationAxis2Test.class);

  /**
   * Login de l'utilisateur qui lance l'action.
   */
  public static final String VI_LOGIN = "[LOGIN]";

  /**
   * Ceci n'est pas un vrai TU, cette méthode sert à générer des VI pour les
   * copier/coller dans SoapUI par exemple
   */
  @Test
  public void genereAssertionSansInvokeAxis2() {

    // Le magasin de certificats
    final KeyStoreInterface keystore = DefaultKeystore.getInstance();

    // Identifiant du contrat de service
    final String issuer = "CS_DEV_TOUTES_ACTIONS";
    // final String issuer = "INT_CS_ATT_VIGI";
    //String issuer = "INT_CS_2_CERTIFS";
    //String issuer = "INT_CS_ATT_AEPL";
    //String issuer = "INT_CS_UNE_META";
    //String issuer = "INT_CS_ANCIEN_SYSTEM";
    //String issuer = "INT_CS_PRMD_DYNA_CODERND";
    //String issuer = "INT_CS_UNE_META";
    //String issuer = "INT_CS_ATT_VIGI_CODERND_231112";

    // Liste des PAGMs
    //List<String> pagms = Arrays.asList("PAGM1", "PAGM2");
    final List<String> pagms = Arrays.asList("PAGM_TOUTES_ACTIONS");
    //List<String> pagms = Arrays.asList("INT_PAGM_ATT_VIGI_ARCH_MASSE");
    //List<String> pagms = Arrays.asList("INT_PAGM_ATT_VIGI_RECH");
    //List<String> pagms = Arrays.asList("INT_PAGM_ATT_AEPL_ARCH_UNIT");
    //List<String> pagms = Arrays.asList("INT_PAGM_ATT_AEPL_RECH");
    //List<String> pagms = Arrays.asList("INT_PAGM_UNE_META_ARCH_UNIT");
    //List<String> pagms = Arrays.asList("INT_PAGM_UNE_META_RECH");
    //List<String> pagms = Arrays.asList("INT_PAGM_PLUSIEURS_META_ARCH_UNIT");
    //List<String> pagms = Arrays.asList("INT_PAGM_ATT_VIGI_CODERND_231112_CONSULT");
    //List<String> pagms = Arrays.asList("INT_PAGM_PRMD_DYNA_CODERND_ARCH_UNIT");
    //List<String> pagms = Arrays.asList("INT_PAGM_PRMD_DYNA_CODERND_ALL");
    //List<String> pagms = Arrays.asList("PAGM_WATT_ARCHIVAGE_GNT","PAGM_WATT_AUTRES_GNT");
    // final List<String> pagms = Arrays.asList("INT_PAGM_ATT_VIGI_ARCH_UNIT","INT_PAGM_ATT_VIGI_ARCH_MASSE");
    //List<String> pagms = Arrays.asList("INT_PAGM_UNE_META_ALL");
    // Génération "manuelle" du VI

    // On sort le VI dans le log
    LOGGER.debug("DEBUT");
    VIGenerator.getWsseHeader(issuer, VI_LOGIN, pagms, keystore.getKeystore(), keystore.getAlias(), keystore.getPassword());

    LOGGER.debug("FIN");

  }

}
