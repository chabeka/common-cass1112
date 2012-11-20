package fr.urssaf.image.sae.webservices.security;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMException;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.Handler.InvocationResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.vi.service.WebServiceVICreateService;
import fr.urssaf.image.sae.webservices.util.Constantes;

@SuppressWarnings("PMD.MethodNamingConventions")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices-test.xml" })
public class SamlTokenHandlerTest {

   @Autowired
   private WebServiceVICreateService createService;


   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void invoke_success() throws AxisFault {

      SamlTokenHandler handler = new SamlTokenHandler(createService,
            Constantes.DEFAULT_ISSUER, "ROLE_TEST");

      MessageContext msgCtx = new MessageContext();
      this.init(msgCtx, "src/test/resources/soap/soap_success.xml");

      assertEquals(InvocationResponse.CONTINUE, handler.invoke(msgCtx));
   }

   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   public void invoke_success_emptyRole() throws AxisFault {

      SamlTokenHandler handler = new SamlTokenHandler(createService);
      
      MessageContext msgCtx = new MessageContext();
      this.init(msgCtx, "src/test/resources/soap/soap_success.xml");

      assertEquals(InvocationResponse.CONTINUE, handler.invoke(msgCtx));
   }

   private void init(MessageContext msgCtx, String xml) {

      try {
         InputStream input = new FileInputStream(xml);

         StAXSOAPModelBuilder stax = new StAXSOAPModelBuilder(StAXUtils
               .createXMLStreamReader(input));

         msgCtx.setEnvelope(stax.getSOAPEnvelope());
      } catch (FileNotFoundException e) {
         throw new IllegalStateException(e);
      } catch (XMLStreamException e) {
         throw new IllegalStateException(e);
      } catch (AxisFault e) {
         throw new IllegalStateException(e);
      } catch (OMException e) {
         throw new IllegalStateException(e);
      }

   }

   @Test(expected = IllegalStateException.class)
   public void invoke_failure_emptyEnveloppe() throws AxisFault {

      SamlTokenHandler handler = new SamlTokenHandler(createService);
      
      MessageContext msgCtx = new MessageContext();
      handler.invoke(msgCtx);
   }
}
