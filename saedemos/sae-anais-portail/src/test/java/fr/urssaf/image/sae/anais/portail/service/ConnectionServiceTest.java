package fr.urssaf.image.sae.anais.portail.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-servlet.xml",
      "/applicationContext.xml" })
@SuppressWarnings("PMD")
@Ignore("Tests à reprendre")
public class ConnectionServiceTest {

   private static final String PASSWORD_VALUE = "CER6990010";

   private static final String LOGIN_VALUE = "CER6990010";

   @Autowired
   private ConnectionService service;

   // @Test
   // public void connectSuccess() throws AucunDroitException, IOException {
   //
   // String vi = FileUtils.readFileToString(new File(
   // "src/test/resources/SAMLResponse.xml"), "UTF-8");
   //
   // assertEquals(vi, service.connect(LOGIN_VALUE, PASSWORD_VALUE));
   // }

   // TODO récupérer ces exceptions

   // @Test(expected=UserPasswordNonRenseigneException.class)
   // public void connectFailure_password() throws AucunDroitException {
   //
   // service.connect(LOGIN_VALUE, null);
   // }
   //   
   // @Test(expected=UserLoginNonRenseigneException.class)
   // public void connectFailure_login() throws AucunDroitException {
   //
   // service.connect(null, PASSWORD_VALUE);
   // }

   // @Test(expected=AucunDroitException.class)
   // public void connectFailure_noright() throws AucunDroitException {
   //
   // service.connect("CER6990012", "CER6990012");
   // }
   //   
   // @Test(expected=SaeAnaisApiException.class)
   // public void connectFailure_authentification() throws AucunDroitException {
   //
   // service.connect(LOGIN_VALUE, "incorrecte");
   // }

   @Test(expected = IllegalStateException.class)
   public void connectException() {

      new ConnectionService(null);
   }
}
