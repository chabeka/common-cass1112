package fr.urssaf.image.sae.integration.ihmweb.service.listetests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.soap.SOAPMessage;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;

@Service
public class ListTestsService extends org.apache.axis2.client.Stub {

   @Autowired
   private TestConfig testConfig;

   private SaeServiceStub stub;

   
   public Map<String, String> lancerTest(File file) throws XMLStreamException, SAXException, IOException, ParserConfigurationException{
      
      Map<String, String> resStub = new LinkedHashMap<String, String>();
      
      String sub = file.getName();
      int debut = file.getName().indexOf("_");
      int fin = file.getName().indexOf("_", debut + 1);
      sub = file.getName().substring(debut + 1, fin);
      
      String res = appelleStub(sub, file);
      resStub.put(file.getName(), res);
      
      return resStub;
      
   }

   public String appelleStub(String serviceName, File file2)
         throws XMLStreamException, SAXException, IOException,
         ParserConfigurationException {

      QName q = new javax.xml.namespace.QName(
            "http://www.cirtil.fr/saeService", serviceName);

      stub = new SaeServiceStub(testConfig.getUrlSaeService());
      org.apache.axis2.client.OperationClient _operationClient = stub
            ._getServiceClient().createClient(q);
      _operationClient.getOptions().setAction(serviceName);
      _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

      addPropertyToOperationClient(
            _operationClient,
            org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
            "&");
      MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      XMLStreamReader reader = inputFactory
            .createXMLStreamReader(new FileInputStream(file2));
      StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(reader);
      SOAPMessage soapMessage = builder.getSoapMessage();
      

      _messageContext.setEnvelope(soapMessage.getSOAPEnvelope());
      // add the message context to the operation client
      _operationClient.addMessageContext(_messageContext);

      // execute the operation client
      try {
         _operationClient.execute(true);
      } catch (AxisFault fault) {
         org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
               .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);

         return _returnMessageContext.getEnvelope().toString();
      }
      org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
            .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);

      return _returnMessageContext.getEnvelope().toString();
   }
   
   public boolean sauvegarderTest(File file) throws IOException{
      
      OutputStream out = null;
      InputStream filecontent = new FileInputStream(file);
    
      try {
          out = new FileOutputStream(new File(testConfig.getTestXml() + file.getName()));

          int read = 0;
          final byte[] bytes = new byte[2048];

          while ((read = filecontent.read(bytes)) != -1) {
              out.write(bytes, 0, read);
          }
  
      } catch (FileNotFoundException fne) {
         fne.printStackTrace();
         return false;
      } finally {
          if (out != null) {
              out.close();
          }
          if (filecontent != null) {
              filecontent.close();
          }
      }   
      return true;
   }
}
