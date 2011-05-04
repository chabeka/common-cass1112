package fr.urssaf.image.commons.webservice.axis.client.modele.userguide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.DoInOnlyRequest;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.MultipleParametersAddItemRequest;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.MultipleParametersAddItemResponse;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.NoParametersRequest;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.TwoWayOneParameterEchoRequest;
import fr.urssaf.image.commons.webservice.axis.client.modele.userguide.Axis2UserGuideServiceStub.TwoWayOneParameterEchoResponse;

@SuppressWarnings( { "PMD.MethodNamingConventions",
      "PMD.JUnitAssertionsShouldIncludeMessage" })
public class Axis2UserGuideServiceTest {

   private final static String HTTP = "http://localhost:8082/axis2/services/Axis2UserGuideService/";

   private final static String JMS = "jms:/Axis2UserGuideService?transport.jms.DestinationType=queue&transport.jms.ContentTypeProperty=Content-Type&java.naming.provider.url=tcp://localhost:61616&java.naming.factory.initial=org.apache.activemq.jndi.ActiveMQInitialContextFactory&transport.jms.ConnectionFactoryJNDIName=QueueConnectionFactory";

   private Axis2UserGuideServiceStub service;

   private static final Logger LOG = Logger
         .getLogger(Axis2UserGuideServiceTest.class);

   private static final String SECURITY_PATH = "src/main/resources/META-INF";

   private ConfigurationContext ctx;

   @Before
   public void before() throws AxisFault  {

      ctx = ConfigurationContextFactory
            .createConfigurationContextFromFileSystem(SECURITY_PATH,
                  SECURITY_PATH + "/axis2-security.xml");

   }

   @Test
   public void doInOnly_http() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, HTTP);
      assertDoInOnly(service);
   }

   @Test
   public void doInOnly_jms() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, JMS);
      assertDoInOnly(service);
   }

   private static void assertDoInOnly(Axis2UserGuideServiceStub service)
         throws RemoteException {

      DoInOnlyRequest request = new DoInOnlyRequest();
      request.setMessageString("message");

      service.doInOnly(request);
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

   private static void assertTwoWayOneParameterEcho(Axis2UserGuideServiceStub service)
         throws RemoteException {

      String echo = "echo";

      TwoWayOneParameterEchoRequest request = new TwoWayOneParameterEchoRequest();
      request.setEchoString(echo);

      TwoWayOneParameterEchoResponse response = service
            .twoWayOneParameterEcho(request);

      LOG.debug(response.getEchoString());
      assertEquals(echo, response.getEchoString());
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
      assertNotNull(service.noParameters(request));

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

   private static void assertMultipleParametersAddItem(Axis2UserGuideServiceStub service)
         throws RemoteException {

      int itemId = 1;

      MultipleParametersAddItemRequest request = new MultipleParametersAddItemRequest();
      request.setDescription("description");
      request.setItemId(1);
      request.setItemName("name");
      request.setPrice(new Float(24.2));

      MultipleParametersAddItemResponse response = service
            .multipleParametersAddItem(request);

      LOG.debug(response.getItemId());
      LOG.debug(response.getSuccessfulAdd());

      assertEquals(itemId, response.getItemId());
      assertEquals(true, response.getSuccessfulAdd());

   }

}
