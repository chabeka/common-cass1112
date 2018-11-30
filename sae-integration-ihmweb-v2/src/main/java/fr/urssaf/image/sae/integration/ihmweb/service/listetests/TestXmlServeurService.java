package fr.urssaf.image.sae.integration.ihmweb.service.listetests;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
public class TestXmlServeurService extends org.apache.axis2.client.Stub {

   @Autowired
   TestConfig testConfig;

   private SaeServiceStub stub;

   public Map<String, String> lancerTest(String testXml)
         throws XMLStreamException, SAXException, IOException,
         ParserConfigurationException {

      Map<String, String> resStub = new LinkedHashMap<String, String>();

      // Recupere nom du service a appeller
      String sub = testXml;
      int debut = testXml.indexOf("_");
      int fin = testXml.indexOf("_", debut + 1);
      sub = testXml.substring(debut + 1, fin);
      // sub = str.substring(0, str.indexOf("_"));

      // Recupere contenu du fichier pour message context
      File messageOut = new File(testConfig.getTestXml() + testXml);
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(
            messageOut));
      StringWriter out = new StringWriter();
      int b;
      while ((b = in.read()) != -1)
         out.write(b);
      out.flush();
      out.close();
      in.close();
      String contenu = out.toString();

      // appelle du stub
      String res = appelleStub(sub, contenu, testXml);
      System.out.println("RES : " + res);

      resStub.put(testXml, res);

      return resStub;

   }

   public String appelleStub(String serviceName, String file, String str)
         throws XMLStreamException, SAXException, IOException,
         ParserConfigurationException {

      QName q = new javax.xml.namespace.QName(
            "http://www.cirtil.fr/saeService", serviceName);

      stub = new SaeServiceStub(testConfig.getUrlSaeService());
      org.apache.axis2.client.OperationClient _operationClient = stub
            ._getServiceClient().createClient(q);
      _operationClient.getOptions().setAction(serviceName);
      _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

      File file2 = new File(testConfig.getTestXml() + str);

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

         if (_returnMessageContext.getEnvelope() != null)
            return _returnMessageContext.getEnvelope().toString();
         else
            return "soapenv:Fault : Catch Error";
      }
      org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
            .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);

      return _returnMessageContext.getEnvelope().toString();

   }

}
