package fr.urssaf.image.sae.anais.framework.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import anaisJavaApi.AnaisExceptionAuthFailure;
import anaisJavaApi.AnaisExceptionServerAuthentication;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAdresseServeur;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCodesEnvironnement;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisProfilCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;
import fr.urssaf.image.sae.anais.framework.util.CTD;
import fr.urssaf.image.sae.anais.framework.util.InitFactory;

@SuppressWarnings("PMD")
public class SaeAnaisServiceFailureTest {

   private SaeAnaisService service;

   @Before
   public void initService() {
      service = new SaeAnaisService();
   }

   private static SaeAnaisAdresseServeur serveur;

   private static CTD ctd;

   @BeforeClass
   public static void initClass() {

      serveur = InitFactory.initServeur();

      ctd = InitFactory.initCTD("ctd_1_right");

   }

   @Test
   public void failureAuth() throws AucunDroitException {
      ctd.setUserPassword("inconnu");
      try {
         this.assertFailure(SaeAnaisEnumCompteApplicatif.Sae, null);
      } catch (SaeAnaisApiException e) {
         assertEquals("le login est incorrect",
               AnaisExceptionAuthFailure.class, e.getCause().getClass());
      }

   }

   @Test
   public void failureDN() throws AucunDroitException {

      SaeAnaisProfilCompteApplicatif profil = InitFactory
            .initCompteApplicatif();
      profil.setDn("cn=USR_READ_NAT_APP_RECHERCHE-DOCUMENTAIRE");

      try {
         this.assertFailure(SaeAnaisEnumCompteApplicatif.Autre, profil);
      } catch (SaeAnaisApiException e) {
         assertEquals(AnaisExceptionServerAuthentication.class, e.getCause()
               .getClass());
      }
   }

   private void assertFailure(SaeAnaisEnumCompteApplicatif compteAppli,
         SaeAnaisProfilCompteApplicatif profil) throws AucunDroitException {

      service.authentifierPourSaeParLoginPassword(
            SaeAnaisEnumCodesEnvironnement.Developpement, serveur, compteAppli,
            profil, ctd.getUserLogin(), ctd.getUserPassword(), ctd.getCodeir(),
            ctd.getCodeorg());

      fail("le test ne doit pas passer");
   }

}
