package fr.urssaf.image.sae.services.util;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.services.util.SAESearchUtil;

/**
 * Tests unitaires de la classe SAESearchUtil
 */
public class SAESearchUtilTest {

   @Test
   public void trimRequeteClient_casClassique() {

      String requeteInitiale = "meta1:valeur1 OR meta2:valeur2";

      String requeteObtenue = SAESearchUtil.trimRequeteClient(requeteInitiale);

      String requeteAttendue = "meta1:valeur1 OR meta2:valeur2";

      Assert.assertEquals("La requête obtenue n'est pas celle attendue",
            requeteAttendue, requeteObtenue);

   }

   @Test
   public void trimRequeteClient_unEspaceEchappeAlaFin_sansAutresEspaces() {

      String requeteInitiale = "meta1:valeur1 OR meta2:valeur2\\ ";

      String requeteObtenue = SAESearchUtil.trimRequeteClient(requeteInitiale);

      String requeteAttendue = "meta1:valeur1 OR meta2:valeur2\\ ";

      Assert.assertEquals("La requête obtenue n'est pas celle attendue",
            requeteAttendue, requeteObtenue);

   }

   @Test
   public void trimRequeteClient_unEspaceEchappeAlaFin_avecAutresEspaces() {

      String requeteInitiale = "meta1:valeur1 OR meta2:valeur2\\       ";

      String requeteObtenue = SAESearchUtil.trimRequeteClient(requeteInitiale);

      String requeteAttendue = "meta1:valeur1 OR meta2:valeur2\\ ";

      Assert.assertEquals("La requête obtenue n'est pas celle attendue",
            requeteAttendue, requeteObtenue);

   }

   @Test
   public void trimRequeteClient_desEspacesNonEchappesAlaFin() {

      String requeteInitiale = "meta1:valeur1 OR meta2:valeur2       ";

      String requeteObtenue = SAESearchUtil.trimRequeteClient(requeteInitiale);

      String requeteAttendue = "meta1:valeur1 OR meta2:valeur2";

      Assert.assertEquals("La requête obtenue n'est pas celle attendue",
            requeteAttendue, requeteObtenue);

   }

}
