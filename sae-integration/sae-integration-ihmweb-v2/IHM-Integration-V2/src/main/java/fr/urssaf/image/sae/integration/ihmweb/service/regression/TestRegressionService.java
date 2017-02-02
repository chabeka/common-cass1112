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
import java.nio.charset.Charset;
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
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestProprietes;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.LogInMessageHandler;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.VIHandler;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViService;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;

;

@Service
public class TestRegressionService extends org.apache.axis2.client.Stub {

   @Autowired
   private TestConfig testConfig;
   
//   @Autowired
//   private ViService viService;

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

      Map<String, Map<String, String>> resultat = new LinkedHashMap<String, Map<String, String>>();
      Map<String, Map<String, Map<String, String>>> testMessageMap = new LinkedHashMap<String, Map<String, Map<String, String>>>();
      Map<String, String> resStub = new LinkedHashMap<String, String>();
      Map<String, String> testMessage = new LinkedHashMap<String, String>();
      Map<String, Map<String, String>> messageXml = new LinkedHashMap<String, Map<String, String>>();
      Map<String, String> testOkKo = new LinkedHashMap<String, String>();
      test = new TestProprietes();
      test.setCheckboxValue(checkboxValue);

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
            messageXml.put(str,  testMessage);
            // fonction compare retourne boolean

            // essaye une fois seulement
            boolean isOk = checkRes(res, str);
            System.out.println("RES COMPARE : " + isOk);

            // add map resStub

            if (isOk)
               resStub.put(str, "OK");
            else
               resStub.put(str, "KO");
            testMessage = new LinkedHashMap<String, String>();
         }
         // add map resultat
         resultat.put(reg, resStub);
         testMessageMap.put(reg, messageXml);
         messageXml = new LinkedHashMap<String, Map<String, String>>();
         resStub = new LinkedHashMap<String, String>();
         
         System.out.println("NAME " + reg);
      }
      
      //remplir map testOkKo
      for (Map.Entry<String, Map<String, String>> entry : resultat.entrySet()){
         Map<String, String> mp = entry.getValue();
         int i = 0;
         for(Map.Entry<String, String> entry2 : mp.entrySet()){
            if (entry2.getValue().equals("KO")){
             i++;
            }
         }
         if (i == 0)
         testOkKo.put(entry.getKey(), "OK"); 
         else
            testOkKo.put(entry.getKey(), "KO"); 
         
      }
      
      for (Map.Entry<String, String> en : testOkKo.entrySet()){
         System.out.println("KEY : " + en.getKey() + " ||| value : " + en.getValue());
      }
      
      test.setTestOkKo(testOkKo);
      test.setResStub(resultat);
      test.setMessageInOut(testMessageMap);

      return test;
   }

   public boolean checkRes(String res, String nomFichier) throws IOException {

      //fonction de compare avec resultat et fichier attendu
      String nom = StringUtils.remove(nomFichier, "_Out.xml");
   // Recupere contenu du fichier pour message context
   
      BufferedReader br = null;
      FileReader fr = null;
      
      try {
         
      fr = new FileReader(testConfig.getTestAttendu() + nom + "_attendu.xml");
      br = new BufferedReader(fr);

      String sCurrentLine;

      br = new BufferedReader(new FileReader(testConfig.getTestAttendu() + nom + "_attendu.xml"));

      while ((sCurrentLine = br.readLine()) != null) {
         System.out.println(sCurrentLine);
         if (!res.contains(sCurrentLine))
          return false;
      }
      
      }catch (IOException e) {

         e.printStackTrace();
      }
      //sinon return true
      return true;
      
   }
   
//   public SaeServiceStub createStub(String urlServiceWeb, ViStyle viStyle, ViFormulaire viParams){
//      
//      try {
//
//         // Création d'une configuration Axis2 par défaut
//         ConfigurationContext configContext = 
//            ConfigurationContextFactory.createConfigurationContextFromFileSystem(null , null) ;
//         
//         // ----------------------------------------------
//         // Gestion du VI + Log du message SOAP de request
//         // ----------------------------------------------
//         
//         // 1) Ajout de 2 propriétés dans lesquelles on met le style du fichier de VI
//         //    à inclure dans le message SOAP ainsi que les propriétés éventuelles.
//         //    L'inclusion sera faite dans un handler
//         configContext.setProperty(VIHandler.PROP_STYLE_VI, viStyle);
//         configContext.setProperty(VIHandler.PROP_PARAMS_VI, viParams);
//         
//         // 2) Ajout d'un Handler lors de la phase "MessageOut" pour insérer le VI
//         AxisConfiguration axisConfig = configContext.getAxisConfiguration();
//         List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
//         Phase messageOut = findPhaseByName(outFlowPhases,"MessageOut");
//         messageOut.addHandler(new VIHandler(viService));
//         
//         
//         // ----------------------------------------------
//         // Log du message SOAP de response
//         // ----------------------------------------------
//         
//         List<Phase> inFlowPhases = axisConfig.getInFlowPhases();
//         Phase dispatch = findPhaseByName(inFlowPhases,"Dispatch");
//         dispatch.addHandler(new LogInMessageHandler());
//         
//         List<Phase> inFaultPhases = axisConfig.getInFaultFlowPhases();
//         dispatch = findPhaseByName(inFaultPhases,"Dispatch");
//         dispatch.addHandler(new LogInMessageHandler());
//         
//         
//         // Création du Stub
//         SaeServiceStub service = new SaeServiceStub(configContext,
//               urlServiceWeb);
//         
//         // Renvoie du Stub
//         return service;
//
//      } catch (Exception e) {
//         throw new IntegrationRuntimeException(e);
//      }
//      
//   }
//   
// private static Phase findPhaseByName(List<Phase> phases, String nomPhaseRecherchee) {
//      
//      Phase result = null;
//      
//      for(Phase phase: phases) {
//         if (phase.getName().equals(nomPhaseRecherchee)) {
//            result = phase;
//            break;
//         }
//      }
//      
//      return result;
//      
//   }

   public String appelleStub(String serviceName, String file, String str)
         throws XMLStreamException, SAXException, IOException,
         ParserConfigurationException {

      QName q = new javax.xml.namespace.QName(
            "http://www.cirtil.fr/saeService", serviceName);

     stub = new SaeServiceStub(testConfig.getUrlSaeService());
      //stub = createStub(testConfig.getUrlSaeService(), ViStyle.VI_SF_sae_DroitsInsuffisants, null);
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
