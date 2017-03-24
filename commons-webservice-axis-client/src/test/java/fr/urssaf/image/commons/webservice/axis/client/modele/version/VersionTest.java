package fr.urssaf.image.commons.webservice.axis.client.modele.version;

import static fr.urssaf.image.commons.webservice.axis.client.configuration.ConnectionConfiguration.WS_HTTP;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.urssaf.image.commons.webservice.axis.client.modele.version.VersionStub.GetVersion;
import fr.urssaf.image.commons.webservice.axis.client.modele.version.VersionStub.GetVersionResponse;

@SuppressWarnings( { "PMD.MethodNamingConventions",
      "PMD.JUnitAssertionsShouldIncludeMessage" })
public class VersionTest {

   private static final Logger LOG = Logger.getLogger(VersionTest.class);

   private final static String HTTP = WS_HTTP
         + "Version.VersionHttpSoap12Endpoint/";

   private final static String JMS = "jms:/Version?transport.jms.DestinationType=queue&transport.jms.ContentTypeProperty=Content-Type&java.naming.provider.url=tcp://localhost:61616&java.naming.factory.initial=org.apache.activemq.jndi.ActiveMQInitialContextFactory&transport.jms.ConnectionFactoryJNDIName=QueueConnectionFactory";

   private VersionStub service;

   private static ConfigurationContext ctx;

   private static final String SECURITY_PATH = "src/main/resources/META-INF";

   @BeforeClass
   public static void beforeClass() throws AxisFault {

      ctx = ConfigurationContextFactory
            .createConfigurationContextFromFileSystem(SECURITY_PATH,
                  SECURITY_PATH + "/axis2.xml");

   }

   @Test
   public void getVersion_jms() throws RemoteException, VersionExceptionException {

      service = new VersionStub(ctx, JMS);
      assertVersion(service);
   }

   @Test
   public void getVersion_http() throws RemoteException, VersionExceptionException {

      service = new VersionStub(ctx, HTTP);
      assertVersion(service);
   }

   private void assertVersion(VersionStub service) throws RemoteException, VersionExceptionException  {

       GetVersion request = new GetVersion();
       
       GetVersionResponse response = service.getVersion(request);
       
       String reponse = response.get_return();
      
       LOG.debug(reponse);
   }
}
