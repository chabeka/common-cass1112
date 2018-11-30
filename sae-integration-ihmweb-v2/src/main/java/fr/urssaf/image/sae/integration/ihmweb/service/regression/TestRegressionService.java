package fr.urssaf.image.sae.integration.ihmweb.service.regression;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import fr.urssaf.image.sae.integration.ihmweb.modele.TestProprietes;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;

;

@Service
public class TestRegressionService extends org.apache.axis2.client.Stub {

   @Autowired
   private TestConfig testConfig;

   private SaeServiceStub stub;

   private TestProprietes test;

   // public Map<String, Map<String, String>> testRegression() throws
   // IOException,
   // XMLStreamException, InterruptedException, SAXException,
   // ParserConfigurationException {
   //
   // System.out.println("Service OK !");
   //
   // Map<String, Map<String, String>> resultat = new LinkedHashMap<String,
   // Map<String, String>>();
   // Map<String, String> resStub = new LinkedHashMap<String, String>();
   //
   // File repertoireRegression = new File(testConfig.getTestRegression());
   // File[] filesRegression = repertoireRegression.listFiles();
   //
   // // Boucler ici
   // for (File f : filesRegression) {
   // List<String> records = new ArrayList<String>();
   // BufferedReader reader = new BufferedReader(new FileReader(
   // f));
   // String line;
   // while ((line = reader.readLine()) != null) {
   // records.add(line);
   // }
   // reader.close();
   //
   // for (String str : records) {
   //
   // // Recupere nom du service a appeller
   // String sub = str;
   // int debut = str.indexOf("_");
   // int fin = str.indexOf("_", debut + 1);
   // sub = str.substring(debut + 1, fin);
   // //sub = str.substring(0, str.indexOf("_"));
   //
   // // Recupere contenu du fichier pour message context
   // File messageOut = new File(testConfig.getTestXml() + str);
   // BufferedInputStream in = new BufferedInputStream(
   // new FileInputStream(messageOut));
   // StringWriter out = new StringWriter();
   // int b;
   // while ((b = in.read()) != -1)
   // out.write(b);
   // out.flush();
   // out.close();
   // in.close();
   // String contenu = out.toString();
   // test.setMessageOut(contenu);
   //
   // // appelle du stub
   // String res = appelleStub(sub, contenu, str);
   // System.out.println("RES : " + res);
   //
   // // fonction compare retourne boolean
   //
   // // essaye une fois seulement
   // boolean isOk = checkRes(res);
   // System.out.println("RES COMPARE : " + isOk);
   //
   // //add map resStub
   //
   // if (isOk)
   // resStub.put(str, "OK");
   // else
   // resStub.put(str, "KO");
   //
   // }
   // //add map resultat
   // resultat.put(f.getName(), resStub);
   // resStub = new LinkedHashMap<String, String>();
   // System.out.println("NAME " + f.getName());
   // }
   //
   // System.out.println("Service 2 OK !");
   //
   // return resultat;
   // }

   public TestProprietes testRegression(String[] checkboxValue)
         throws IOException, XMLStreamException, InterruptedException,
         SAXException, ParserConfigurationException {

      System.out.println("Service OK !");

      Map<String, Map<String, String>> resultat = new LinkedHashMap<String, Map<String, String>>();
      Map<String, Map<String, String>> testMessageMap = new LinkedHashMap<String, Map<String, String>>();
      Map<String, String> resStub = new LinkedHashMap<String, String>();
      Map<String, String> testMessage = new LinkedHashMap<String, String>();
      test = new TestProprietes();
      test.setCheckboxValue(checkboxValue);

      // File repertoireRegression = new File(testConfig.getTestRegression());
      // File[] filesRegression = repertoireRegression.listFiles();

      // Boucler ici
      for (String reg : checkboxValue) {
         List<String> records = new ArrayList<String>();
         BufferedReader reader = new BufferedReader(new FileReader(
               testConfig.getTestRegression() + reg));
         String line;
         while ((line = reader.readLine()) != null) {
            records.add(line);
         }
         reader.close();

         for (String str : records) {

            // Recupere nom du service a appeller
            String sub = str;
            int debut = str.indexOf("_");
            int fin = str.indexOf("_", debut + 1);
            sub = str.substring(debut + 1, fin);
            // sub = str.substring(0, str.indexOf("_"));

            // Recupere contenu du fichier pour message context
            File messageOut = new File(testConfig.getTestXml() + str);
            BufferedInputStream in = new BufferedInputStream(
                  new FileInputStream(messageOut));
            StringWriter out = new StringWriter();
            int b;
            while ((b = in.read()) != -1)
               out.write(b);
            out.flush();
            out.close();
            in.close();
            String contenu = out.toString();

            // appelle du stub
            String res = appelleStub(sub, contenu, str);
            System.out.println("RES : " + res);

            testMessage.put(contenu, res);

            // fonction compare retourne boolean

            // essaye une fois seulement
            boolean isOk = checkRes(res);
            System.out.println("RES COMPARE : " + isOk);

            // add map resStub

            if (isOk)
               resStub.put(str, "OK");
            else
               resStub.put(str, "KO");

         }
         // add map resultat
         resultat.put(reg, resStub);
         testMessageMap.put(reg, testMessage);
         resStub = new LinkedHashMap<String, String>();
         testMessage = new LinkedHashMap<String, String>();
         System.out.println("NAME " + reg);
      }

      System.out.println("Service 2 OK !");
      test.setResStub(resultat);
      test.setMessageInOut(testMessageMap);

      return test;
   }

   public boolean checkRes(String res) {

      return !res.contains("soapenv:Fault");
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

   // public Map<String, String> testRegression() throws IOException,
   // XMLStreamException, InterruptedException, SAXException,
   // ParserConfigurationException {
   //
   // System.out.println("Service OK !");
   //
   // Map<String, String> resStub = new LinkedHashMap<String, String>();
   //
   // File repertoireRegression = new File(testConfig.getTestRegression());
   // File[] filesRegression = repertoireRegression.listFiles();
   // List<String> records = new ArrayList<String>();
   // // Boucler ici
   // for (File f : filesRegression) {
   // records.add(f.getName());
   // }
   //
   // for (String str : records) {
   //
   // // Recupere nom du service a appeller
   // String sub = str;
   // int debut = str.indexOf("_");
   // int fin = str.indexOf("_", debut + 1);
   // sub = str.substring(debut + 1, fin);
   // //sub = str.substring(0, str.indexOf("_"));
   //
   // // Recupere contenu du fichier pour message context
   // File messageOut = new File(testConfig.getTestRegression() + str);
   // BufferedInputStream in = new BufferedInputStream(
   // new FileInputStream(messageOut));
   // StringWriter out = new StringWriter();
   // int b;
   // while ((b = in.read()) != -1)
   // out.write(b);
   // out.flush();
   // out.close();
   // in.close();
   // String contenu = out.toString();
   //
   // // appelle du stub
   // String res = appelleStub(sub, contenu, str);
   //
   // // fonction compare retourne boolean
   //
   // // essaye une fois seulement
   // boolean isOk = checkRes(res);
   //
   // if (isOk)
   // resStub.put(str, "OK");
   // else
   // resStub.put(str, "KO");
   //
   // }
   //
   // return resStub;
   // }
   //
   // public boolean checkRes(String res) {
   //
   // return !res.contains("soapenv:Fault");
   // }
   //
   // public String appelleStub(String serviceName, String file, String str)
   // throws XMLStreamException, SAXException, IOException,
   // ParserConfigurationException {
   //
   // QName q = new javax.xml.namespace.QName(
   // "http://www.cirtil.fr/saeService", serviceName);
   //
   // stub = new SaeServiceStub(testConfig.getUrlSaeService());
   // org.apache.axis2.client.OperationClient _operationClient = stub
   // ._getServiceClient().createClient(q);
   // _operationClient.getOptions().setAction(serviceName);
   // _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
   //
   // File file2 = new File(testConfig.getTestRegression() + str);
   //
   // addPropertyToOperationClient(
   // _operationClient,
   // org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
   // "&");
   // MessageContext _messageContext = new
   // org.apache.axis2.context.MessageContext();
   //
   // XMLInputFactory inputFactory = XMLInputFactory.newInstance();
   // XMLStreamReader reader = inputFactory
   // .createXMLStreamReader(new FileInputStream(file2));
   // StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(reader);
   // SOAPMessage soapMessage = builder.getSoapMessage();
   //
   // _messageContext.setEnvelope(soapMessage.getSOAPEnvelope());
   // // add the message context to the operation client
   // _operationClient.addMessageContext(_messageContext);
   //
   // // execute the operation client
   // try {
   //
   // _operationClient.execute(true);
   //
   // } catch (AxisFault fault) {
   // org.apache.axis2.context.MessageContext _returnMessageContext =
   // _operationClient
   // .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
   //
   // if (_returnMessageContext.getEnvelope() != null)
   // return _returnMessageContext.getEnvelope().toString();
   // else
   // return "soapenv:Fault : Catch Error";
   // }
   // org.apache.axis2.context.MessageContext _returnMessageContext =
   // _operationClient
   // .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
   //
   // return _returnMessageContext.getEnvelope().toString();
   //
   // }

   public boolean sauvegarderTest(File file) throws IOException {

      OutputStream out = null;
      InputStream filecontent = new FileInputStream(file);

      try {
         out = new FileOutputStream(new File(testConfig.getTestRegression()
               + file.getName()));

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
