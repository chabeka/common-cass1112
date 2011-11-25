package fr.urssaf.image.sae.anais.framework.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAdresseServeur;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCodesEnvironnement;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisProfilCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.framework.util.CTD;
import fr.urssaf.image.sae.anais.framework.util.InitFactory;
import fr.urssaf.image.sae.anais.framework.util.TokenAssert;

@SuppressWarnings("PMD")
public class SaeAnaisServiceTest {

   private static final Logger LOG = Logger
         .getLogger(SaeAnaisServiceTest.class);

   private SaeAnaisService service;

   @Before
   public void initService() {
      service = new SaeAnaisService();
   }

   private static SaeAnaisAdresseServeur serveur;

   private static CTD ctd_0_right;

   private static CTD ctd_1_right;

   private static CTD ctd_rights;

   @BeforeClass
   public static void initClass() {

      serveur = InitFactory.initServeur();

      ctd_0_right = InitFactory.initCTD("ctd_0_right");
      ctd_1_right = InitFactory.initCTD("ctd_1_right");
      ctd_rights = InitFactory.initCTD("ctd_rights");

   }

   @Test
   public void envDEV() throws IOException, AucunDroitException {

      this.assertEnv(SaeAnaisEnumCodesEnvironnement.Developpement);
   }

   @Test
   @Ignore
   public void envPROD() throws IOException, AucunDroitException {

      this.assertEnv(SaeAnaisEnumCodesEnvironnement.Production);
   }

   @Test
   @Ignore
   public void envVAL() throws IOException, AucunDroitException {

      this.assertEnv(SaeAnaisEnumCodesEnvironnement.Validation);
   }

   @Test(expected = AucunDroitException.class)
   public void serveurNotNull_CTD_0_right() throws IOException, AucunDroitException {

      this.AssertServeurNotNull(ctd_0_right, "ctd_0_right.xml");

   }

   @Test
   @Ignore
   public void serveurNotNull_CTD_1_right() throws IOException, AucunDroitException {

      this.AssertServeurNotNull(ctd_1_right, "ctd_1_right.xml");
   }

   @Test
   public void serveurNotNull_CTD_rights() throws IOException, AucunDroitException {

      this.AssertServeurNotNull(ctd_rights, "ctd_rights.xml");
   }

   @Test
   public void cptAppliNotNull() throws IOException, AucunDroitException {

      SaeAnaisProfilCompteApplicatif profil = InitFactory
            .initCompteApplicatif();

      String xml = this.createToken(
            SaeAnaisEnumCodesEnvironnement.Developpement, serveur,
            SaeAnaisEnumCompteApplicatif.Autre, profil, ctd_rights);

      LOG.debug(xml);
      TokenAssert.assertCTD_rights(xml);

   }

   private void assertEnv(SaeAnaisEnumCodesEnvironnement environnement)
         throws IOException, AucunDroitException {

      String xml = this.createToken(environnement, null,
            SaeAnaisEnumCompteApplicatif.Sae, null, ctd_rights);

      LOG.debug(xml);
      TokenAssert.assertCTD_rights(xml);
   }

   private void AssertServeurNotNull(CTD ctd, String key) throws IOException, AucunDroitException {

      String xml = this.createToken(
            SaeAnaisEnumCodesEnvironnement.Developpement, serveur,
            SaeAnaisEnumCompteApplicatif.Sae, null, ctd);

      LOG.debug(xml);
      TokenAssert.assertCTD(key, xml);
   }

   private String createToken(SaeAnaisEnumCodesEnvironnement environnement,
         SaeAnaisAdresseServeur serveur, SaeAnaisEnumCompteApplicatif cptAppli,
         SaeAnaisProfilCompteApplicatif profil, CTD ctd) throws IOException, AucunDroitException {

      String xml = service.authentifierPourSaeParLoginPassword(environnement,
            serveur, cptAppli, profil, ctd.getUserLogin(), ctd
                  .getUserPassword(), ctd.getCodeir(), ctd.getCodeorg());

      return xml;
   }

}
