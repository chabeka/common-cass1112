package fr.urssaf.image.sae.anais.framework.component.aspect;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.sae.anais.framework.modele.ObjectFactory;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisAdresseServeur;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCodesEnvironnement;
import fr.urssaf.image.sae.anais.framework.modele.SaeAnaisEnumCompteApplicatif;
import fr.urssaf.image.sae.anais.framework.service.SaeAnaisService;
import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.framework.service.exception.EnvironnementNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.HoteNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.ParametresApplicatifsNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.PortNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.ProfilCompteApplicatifNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.UserLoginNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.UserPasswordNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.util.InitFactory;

@SuppressWarnings("PMD")
public class SaeAnaisServiceTest {

   private SaeAnaisService service;

   private static final String NOTEMPTY = "not empty";

   @Before
   public void initService() {
      service = new SaeAnaisService();
   }

   private static SaeAnaisAdresseServeur serveur;

   @BeforeClass
   public static void initClass() {

      serveur = InitFactory.initServeur();

   }

   @Test(expected = EnvironnementNonRenseigneException.class)
   public void emptyEnvironnement() throws AucunDroitException {

      service.authentifierPourSaeParLoginPassword(null, serveur,
            SaeAnaisEnumCompteApplicatif.Sae, null, NOTEMPTY, NOTEMPTY,
            NOTEMPTY, NOTEMPTY);

   }

   @Test(expected = ProfilCompteApplicatifNonRenseigneException.class)
   public void emptyCompteApplicatif() throws AucunDroitException {

      service.authentifierPourSaeParLoginPassword(
            SaeAnaisEnumCodesEnvironnement.Developpement, serveur, null, null,
            NOTEMPTY, NOTEMPTY, NOTEMPTY, NOTEMPTY);

   }

   @Test(expected = UserLoginNonRenseigneException.class)
   public void emptyLogin() throws AucunDroitException {

      this.emptyLogin(null);
      this.emptyLogin(" ");
      this.emptyLogin("");

   }

   private void emptyLogin(String login) throws AucunDroitException {

      service.authentifierPourSaeParLoginPassword(
            SaeAnaisEnumCodesEnvironnement.Developpement, serveur,
            SaeAnaisEnumCompteApplicatif.Sae, null, login, NOTEMPTY, NOTEMPTY,
            NOTEMPTY);

   }

   @Test(expected = UserPasswordNonRenseigneException.class)
   public void emptyPassword() throws AucunDroitException {

      this.emptyPassword(null);
      this.emptyPassword(" ");
      this.emptyPassword("");

   }

   private void emptyPassword(String password) throws AucunDroitException {

      service.authentifierPourSaeParLoginPassword(
            SaeAnaisEnumCodesEnvironnement.Developpement, serveur,
            SaeAnaisEnumCompteApplicatif.Sae, null, NOTEMPTY, password,
            NOTEMPTY, NOTEMPTY);

   }

   @Test(expected = HoteNonRenseigneException.class)
   public void emptyHost() throws AucunDroitException {

      this.emptyHost(null);
      this.emptyHost(" ");
      this.emptyHost("");

   }

   private void emptyHost(String host) throws AucunDroitException {

      SaeAnaisAdresseServeur serveur = ObjectFactory
            .createSaeAnaisAdresseServeur();
      serveur.setHote(host);
      serveur.setPort(1352);

      service.authentifierPourSaeParLoginPassword(
            SaeAnaisEnumCodesEnvironnement.Developpement, serveur,
            SaeAnaisEnumCompteApplicatif.Sae, null, NOTEMPTY, NOTEMPTY,
            NOTEMPTY, NOTEMPTY);

   }

   @Test(expected = PortNonRenseigneException.class)
   public void emptyPort() throws AucunDroitException {

      SaeAnaisAdresseServeur serveur = ObjectFactory
            .createSaeAnaisAdresseServeur();
      serveur.setHote("hostname");
      serveur.setPort(null);

      service.authentifierPourSaeParLoginPassword(
            SaeAnaisEnumCodesEnvironnement.Developpement, serveur,
            SaeAnaisEnumCompteApplicatif.Sae, null, NOTEMPTY, NOTEMPTY,
            NOTEMPTY, NOTEMPTY);

   }

   @Test(expected = ParametresApplicatifsNonRenseigneException.class)
   public void emptyCompteAppli() throws AucunDroitException {

      service.authentifierPourSaeParLoginPassword(
            SaeAnaisEnumCodesEnvironnement.Developpement, serveur,
            SaeAnaisEnumCompteApplicatif.Autre, null, NOTEMPTY, NOTEMPTY,
            NOTEMPTY, NOTEMPTY);

   }

}
