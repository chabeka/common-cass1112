package fr.urssaf.image.commons.webservice.axis2.client.modele;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.commons.webservice.axis2.client.modele.Axis2UserGuideServiceStub.DoInOnlyRequest;
import fr.urssaf.image.commons.webservice.axis2.client.modele.Axis2UserGuideServiceStub.MultipleParametersAddItemRequest;
import fr.urssaf.image.commons.webservice.axis2.client.modele.Axis2UserGuideServiceStub.MultipleParametersAddItemResponse;
import fr.urssaf.image.commons.webservice.axis2.client.modele.Axis2UserGuideServiceStub.NoParametersRequest;
import fr.urssaf.image.commons.webservice.axis2.client.modele.Axis2UserGuideServiceStub.TwoWayOneParameterEchoRequest;
import fr.urssaf.image.commons.webservice.axis2.client.modele.Axis2UserGuideServiceStub.TwoWayOneParameterEchoResponse;

public class Axis2UserGuideServiceTest {

   public final static String HTTP = "http://localhost:8080/commons-webservice-axis2/services/Axis2UserGuideService/";

   private Axis2UserGuideServiceStub service;

   private static final String META_INF_PATH = "src/main/resources/META-INF";

   private ConfigurationContext ctx;

   @Before
   public void before() throws AxisFault {

      ctx = ConfigurationContextFactory
            .createConfigurationContextFromFileSystem(META_INF_PATH,
                  META_INF_PATH + "/axis2.xml");

   }

   @Test
   public void doInOnly() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, HTTP);

      DoInOnlyRequest request = new DoInOnlyRequest();
      request.setMessageString("message");

      service.doInOnly(request);
   }

   @Test
   public void twoWayOneParameterEcho() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, HTTP);

      String echo = "echo";

      TwoWayOneParameterEchoRequest request = new TwoWayOneParameterEchoRequest();
      request.setEchoString(echo);

      TwoWayOneParameterEchoResponse response = service
            .twoWayOneParameterEcho(request);

      assertEquals(echo, response.getEchoString());
   }

   @Test
   public void noParameters() throws RemoteException {
      service = new Axis2UserGuideServiceStub(ctx, HTTP);

      NoParametersRequest request = new NoParametersRequest();
      assertNotNull(service.noParameters(request));
   }

   @Test
   public void multipleParametersAddItem() throws RemoteException {

      service = new Axis2UserGuideServiceStub(ctx, HTTP);

      int itemId = 1;

      MultipleParametersAddItemRequest request = new MultipleParametersAddItemRequest();
      request.setDescription("description");
      request.setItemId(1);
      request.setItemName("name");
      request.setPrice(new Float(24.2));

      MultipleParametersAddItemResponse response = service
            .multipleParametersAddItem(request);

      assertEquals(itemId, response.getItemId());
      assertEquals(true, response.getSuccessfulAdd());

   }

}
