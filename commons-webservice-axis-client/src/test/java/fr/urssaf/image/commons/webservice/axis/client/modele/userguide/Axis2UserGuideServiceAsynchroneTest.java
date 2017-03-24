package fr.urssaf.image.commons.webservice.axis.client.modele.userguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.MultipleParametersAddItemRequest;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.MultipleParametersAddItemResponse;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.NoParametersRequest;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.NoParametersResponse;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.TwoWayOneParameterEchoRequest;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.TwoWayOneParameterEchoResponse;

@SuppressWarnings( { "PMD.MethodNamingConventions",
      "PMD.JUnitAssertionsShouldIncludeMessage" })
public class Axis2UserGuideServiceAsynchroneTest {

   private final static String HTTP = Axis2UserGuideServiceTest.HTTP;

   private final static String JMS = Axis2UserGuideServiceTest.JMS;

   private Axis2UserGuideServiceStub service;

   private static final Logger LOG = Logger
         .getLogger(Axis2UserGuideServiceAsynchroneTest.class);

   private static final String SECURITY_PATH = "src/main/resources/META-INF";

   private static final long SLEEP_TIME = 2000;

   private ConfigurationContext ctx;

   @Before
   public void before() throws AxisFault {

      ctx = ConfigurationContextFactory
            .createConfigurationContextFromFileSystem(SECURITY_PATH,
                  SECURITY_PATH + "/axis2-security.xml");

   }

   private static void sleep() {

      try {
         Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException e) {
         throw new IllegalStateException(e);
      }
   }

   private static void assertReceive(
         Axis2UserGuideServiceCallbackHandler callback) {

      assertTrue("message non reçu", BooleanUtils.isTrue((Boolean) callback
            .getClientData()));
   }

   @Test
   public void twoWayOneParameterEcho_http() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, HTTP);
      assertTwoWayOneParameterEcho(service);
   }

   @Test
   public void twoWayOneParameterEcho_jms() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, JMS);
      assertTwoWayOneParameterEcho(service);
   }

   private void assertTwoWayOneParameterEcho(Axis2UserGuideServiceStub service)
         throws RemoteException {

      TwoWayOneParameterEchoRequest request = new TwoWayOneParameterEchoRequest();
      request.setEchoString("echo");

      Axis2UserGuideServiceCallbackHandler callback = new Axis2UserGuideServiceCallbackHandler() {

         String echo = "echo";

         @Override
         public void receiveResulttwoWayOneParameterEcho(
               TwoWayOneParameterEchoResponse result) {

            clientData = true;

            LOG.debug(result.getEchoString());
            assertEquals(echo, result.getEchoString());

         }
      };
      service.starttwoWayOneParameterEcho(request, callback);

      sleep();

      assertReceive(callback);

   }

   @Test
   public void noParameters_http() throws RemoteException {
      service = new Axis2UserGuideServiceStub(ctx, HTTP);
      assertNoParameters(service);
   }

   @Test
   public void noParameters_jms() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, JMS);
      assertNoParameters(service);

   }

   private static void assertNoParameters(Axis2UserGuideServiceStub service)
         throws RemoteException {

      NoParametersRequest request = new NoParametersRequest();

      Axis2UserGuideServiceCallbackHandler callback = new Axis2UserGuideServiceCallbackHandler() {

         @Override
         public void receiveResultnoParameters(NoParametersResponse result) {

            clientData = true;

            assertNotNull(result);

         }
      };
      service.startnoParameters(request, callback);

      sleep();

      assertReceive(callback);

   }

   @Test
   public void multipleParametersAddItem_http() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, HTTP);
      assertMultipleParametersAddItem(service);

   }

   @Test
   public void multipleParametersAddItem_jms() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, JMS);
      assertMultipleParametersAddItem(service);

   }

   private void assertMultipleParametersAddItem(
         Axis2UserGuideServiceStub service) throws RemoteException {

      MultipleParametersAddItemRequest request = new MultipleParametersAddItemRequest();
      request.setDescription("description");
      request.setItemId(1);
      request.setItemName("name");
      request.setPrice(new Float(24.2));

      Axis2UserGuideServiceCallbackHandler callback = new Axis2UserGuideServiceCallbackHandler() {

         int itemId = 1;

         @Override
         public void receiveResultmultipleParametersAddItem(
               MultipleParametersAddItemResponse result) {

            clientData = true;

            LOG.debug(result.getItemId());
            LOG.debug(result.getSuccessfulAdd());

            assertEquals(itemId, result.getItemId());
            assertEquals(true, result.getSuccessfulAdd());

         }
      };
      service.startmultipleParametersAddItem(request, callback);

      sleep();

      assertReceive(callback);

   }

}
