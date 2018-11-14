package fr.urssaf.image.sae.anais.portail.controller;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.anais.framework.service.exception.AucunDroitException;
import fr.urssaf.image.sae.anais.portail.configuration.AppliSaeConfig;
import fr.urssaf.image.sae.anais.portail.service.ConnectionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-servlet.xml",
      "/applicationContext.xml" })
@SuppressWarnings("PMD")
@Ignore("Tests à reprendre")
public class ConnectionControllerTest extends
      ControllerTestSupport<ConnectionController> {

   @Autowired
   private ConnectionController servlet;

   private ControllerAssert<ConnectionController> controllerAssert;

   private static final String PASSWORD_VALUE = "CER6990010";

   private static final String LOGIN_VALUE = "CER6990010";

   private static final String PASSWORD_FIELD = "userPassword";

   private static final String LOGIN_FIELD = "userLogin";

   @Before
   public void init() {

      controllerAssert = new ControllerAssert<ConnectionController>(this,
            servlet);

   }

   @Test
   public void getDefaultView() {

      this.initGet();

      controllerAssert.assertView("connection/connection");
   }

   @Test
   public void connectSuccess() throws IOException {

      this.initPost();

      this.initParameter(LOGIN_FIELD, LOGIN_VALUE);
      this.initParameter(PASSWORD_FIELD, PASSWORD_VALUE);

      controllerAssert.assertView("connection/connection_success");

   }

   @Test
   public void connectFailure_password() {

      this.connectFailure_password(null);
      this.connectFailure_password(" ");
      this.connectFailure_password("");

   }

   private void connectFailure_password(String password) {

      this.initPost();

      this.initParameter(LOGIN_FIELD, LOGIN_VALUE);
      controllerAssert.assertError(PASSWORD_FIELD, password, "connectionForm",
            "NotEmpty", "Le mot de passe doit être renseigné");

      controllerAssert.assertView("connection/connection_failure");

   }

   @Test
   public void connectFailure_login() {

      this.connectFailure_login(null);
      this.connectFailure_login("");
      this.connectFailure_login(" ");

   }

   private void connectFailure_login(String login) {

      this.initPost();

      this.initParameter(PASSWORD_FIELD, PASSWORD_VALUE);
      controllerAssert.assertError(LOGIN_FIELD, login, "connectionForm",
            "NotEmpty", "L''identifiant doit être renseigné");

      controllerAssert.assertView("connection/connection_failure");

   }

   @Test
   public void connectFailure_authentification() {

      this.initPost();

      this.initParameter(LOGIN_FIELD, LOGIN_VALUE);
      this.initParameter(PASSWORD_FIELD, "inconnu");

      controllerAssert.assertView("connection/connection_failure");

   }

   @Test
   public void connectFailure_noright() throws AucunDroitException {

      this.initPost();

      this.initParameter(LOGIN_FIELD, "CER6990012");
      this.initParameter(PASSWORD_FIELD, "CER6990012");

      controllerAssert.assertView("connection/connection_failure");
   }

   @Autowired
   private AppliSaeConfig appliSaeConfig;

   @Autowired
   private ConnectionService connectionService;

   @Test(expected = IllegalStateException.class)
   public void connectException_successConfiguration()
         throws AucunDroitException {

      new ConnectionController(null, connectionService);
   }

   @Test(expected = IllegalStateException.class)
   public void connectException_connectionService() throws AucunDroitException {

      new ConnectionController(appliSaeConfig, null);
   }
}
