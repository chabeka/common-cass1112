package fr.urssaf.image.sae.anais.framework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import recouv.cirti.anais.api.source.AnaisExceptionAuthFailure;
import fr.urssaf.image.sae.anais.framework.config.SaeAnaisConfig;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAuth;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAuthHabilitation;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;
import fr.urssaf.image.sae.anais.framework.util.InitFactory;

/**
 * Tests unitaires de la classe SaeAnaisService<br>
 * Rassemble les TU pour les cas où un succès de l'opération est attendue<br>
 * S'appuie sur des CTD associés à l'application "Recherche documentaire"
 */
@SuppressWarnings("PMD")
public class SaeAnaisServiceRgTest {

   private static SaeAnaisConfig anaisConfig;

   @BeforeClass
   public static void initClass() {

      anaisConfig = InitFactory.initConfigRechercheDocumentaire();

   }

   @Ignore
   @Test
   public void success_quatre_droits() throws AucunDroitException {

      SaeAnaisService service = new SaeAnaisService(anaisConfig);

      SaeAnaisAuth saeAnaisAuth = service.habilitationsAnais("CER6990010",
            "CER6990010", null, null);

      // Nom=AGENT-CTD
      assertEquals("Le nom de famille est incorrect", "AGENT-CTD", saeAnaisAuth
            .getNom());

      // Prenom=Prenom-90010
      assertEquals("Le prénom est incorrect", "Prenom-90010", saeAnaisAuth
            .getPrenom());

      // Nombre d'habilitations : 4
      assertEquals("Le nombre d'habilitations est incorrect", 4, saeAnaisAuth
            .getHabilitations().size());

      // Droit GESTIONNAIREACCESCOMPLET sur RECHERCHE-DOCUMENTAIRE_PROD déployé
      // en CER69
      assertHabilitation(saeAnaisAuth.getHabilitations().get(0),
            "GESTIONNAIREACCESCOMPLET", "CER69", "IR69");

      // Droit GESTIONNAIREACCESCOMPLET sur RECHERCHE-DOCUMENTAIRE_PROD déployé
      // en UR030
      assertHabilitation(saeAnaisAuth.getHabilitations().get(1),
            "GESTIONNAIREACCESCOMPLET", "UR030", "IR69");

      // Droit GESTIONNAIRESRVRH sur RECHERCHE-DOCUMENTAIRE_PROD déployé en
      // UR710
      assertHabilitation(saeAnaisAuth.getHabilitations().get(2),
            "GESTIONNAIRESRVRH", "UR710", "IR69");

      // Droit GESTIONNAIRESRVRH sur RECHERCHE-DOCUMENTAIRE_PROD déployé en
      // UR730
      assertHabilitation(saeAnaisAuth.getHabilitations().get(3),
            "GESTIONNAIRESRVRH", "UR730", "IR69");

   }

   private void assertHabilitation(SaeAnaisAuthHabilitation hab,
         String codeDroitAttendu, String codeOrgaAttendu, String codeIrAttendu) {

      assertEquals("L'habilitation n'est pas celle attendue", codeDroitAttendu,
            hab.getCode());

      assertEquals("L'habilitation n'est pas celle attendue", codeOrgaAttendu,
            hab.getCodeOrga());

      assertEquals("L'habilitation n'est pas celle attendue", "IR69",
            codeIrAttendu);

   }

   @Test
   public void failure_mdp_incorrect() throws AucunDroitException {

      SaeAnaisService service = new SaeAnaisService(anaisConfig);

      try {

         service.habilitationsAnais("CER6990010", "mauvais mot de passe", null,
               null);

         fail("Une exception aurait dû être levée");

      } catch (SaeAnaisApiException e) {

         assertEquals("le login est incorrect",
               AnaisExceptionAuthFailure.class, e.getCause().getClass());
      }

   }

   @Test
   public void failure_aucun_droit() {

      SaeAnaisService service = new SaeAnaisService(anaisConfig);

      try {

         service.habilitationsAnais("CER6990011", "CER6990011", null, null);

         fail("Une exception aurait dû être levée");

      } catch (AucunDroitException ex) {

         // Test OK

      }

   }

}
