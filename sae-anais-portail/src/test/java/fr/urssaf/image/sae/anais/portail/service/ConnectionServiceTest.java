package fr.urssaf.image.sae.anais.portail.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.framework.service.exception.SaeAnaisApiException;
import fr.urssaf.image.sae.anais.framework.service.exception.UserLoginNonRenseigneException;
import fr.urssaf.image.sae.anais.framework.service.exception.UserPasswordNonRenseigneException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-servlet.xml",
      "/applicationContext.xml" })
@SuppressWarnings("PMD")
public class ConnectionServiceTest {
   
   private static final String PASSWORD_VALUE = "CER6990010";

   private static final String LOGIN_VALUE = "CER6990010";

   @Autowired
   private ConnectionService service;

   @Test
   public void connectSuccess() throws AucunDroitException, IOException {

      String vi = FileUtils.readFileToString(new File(
            "src/test/resources/SAMLResponse.xml"), "UTF-8");

      assertEquals(vi, service.connect(LOGIN_VALUE, PASSWORD_VALUE));
   }

   @Test(expected=UserPasswordNonRenseigneException.class)
   public void connectFailure_password() throws AucunDroitException {

      service.connect(LOGIN_VALUE, null);
   }
   
   @Test(expected=UserLoginNonRenseigneException.class)
   public void connectFailure_login() throws AucunDroitException {

      service.connect(null, PASSWORD_VALUE);
   }
   
   @Test(expected=AucunDroitException.class)
   public void connectFailure_noright() throws AucunDroitException {

      service.connect("CER6990012", "CER6990012");
   }
   
   @Test(expected=SaeAnaisApiException.class)
   public void connectFailure_authentification() throws AucunDroitException {

      service.connect(LOGIN_VALUE, "incorrecte");
   }

   @Test(expected = IllegalStateException.class)
   public void connectException() {

      new ConnectionService(null);
   }
}
