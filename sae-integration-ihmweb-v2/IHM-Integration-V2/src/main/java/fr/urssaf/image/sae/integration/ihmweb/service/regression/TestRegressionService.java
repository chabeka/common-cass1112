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
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.modele.EcdeRepertoire;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestMasse;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestProprietes;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.EcdeService;

/**
 * couche service pour la partie des tests de non regression
 * 
 */
@Service
public class TestRegressionService extends org.apache.axis2.client.Stub {

   @Autowired
   private TestConfig testConfig;

   @Autowired
   private EcdeService ecdeService;

   private SaeServiceStub stub;

   private TestProprietes test;

   /**
    * @param checkboxValue
    *           la liste des test sélectionnées via la checkbox
    * @return
    * @throws IOException
    * @throws XMLStreamException
    * @throws InterruptedException
    * @throws SAXException
    * @throws ParserConfigurationException
    */
   public TestProprietes testRegression(String[] checkboxValue)
         throws IOException, XMLStreamException, InterruptedException,
         SAXException, ParserConfigurationException {

      // Initialisation de tout les conteneurs
      Map<String, Map<String, String>> resultat = new LinkedHashMap<String, Map<String, String>>();
      Map<String, Map<String, Map<String, String>>> testMessageMap = new LinkedHashMap<String, Map<String, Map<String, String>>>();
      Map<String, String> resStub = new LinkedHashMap<String, String>();
      Map<String, String> testMessage = new LinkedHashMap<String, String>();
      Map<String, Map<String, String>> messageXml = new LinkedHashMap<String, Map<String, String>>();
      Map<String, String> testOkKo = new LinkedHashMap<String, String>();
      test = new TestProprietes();
      test.setCheckboxValue(checkboxValue);

      // on boucle sur l'ensemble des test de non regression
      for (String reg : checkboxValue) {
         // on stocke le nom de chaque test dans une list
         List<String> records = new ArrayList<String>();
         BufferedReader reader = new BufferedReader(new FileReader(
               testConfig.getTestRegression() + reg));
         String line;
         while ((line = reader.readLine()) != null) {
            records.add(line);
         }
         reader.close();

         // on boucle sur l'ensemble des tes XML de chaque test de non
         // regression
         for (String str : records) {

            // Recupere nom du service a appeller
            String sub = str;
            int debut = str.indexOf("_");
            int fin = str.indexOf("_", debut + 1);
            sub = str.substring(debut + 1, fin);

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

            // on stocke le resultat de l'appelle du stub
            testMessage.put(contenu, res);
            messageXml.put(str, testMessage);

            // on appelle la methode de verification du resultat du test
            boolean isOk = checkRes(res, str);

            // on stocke le resultat (OK si reussi / KO sinon)
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
      }

      // remplir map testOkKo
      for (Map.Entry<String, Map<String, String>> entry : resultat.entrySet()) {
         Map<String, String> mp = entry.getValue();
         int i = 0;
         for (Map.Entry<String, String> entry2 : mp.entrySet()) {
            if (entry2.getValue().equals("KO")) {
               i++;
            }
         }
         if (i == 0)
            testOkKo.put(entry.getKey(), "OK");
         else
            testOkKo.put(entry.getKey(), "KO");

      }

      test.setTestOkKo(testOkKo);
      test.setResStub(resultat);
      test.setMessageInOut(testMessageMap);

      return test;
   }

   /**
    * methode permettant de lancer les tests de non regression pour les
    * traitements de masse si isALancer = true sinon on recupere les
    * informations des tests precedents
    * 
    * @param checkboxValue
    * @param isALancer
    * @return
    * @throws IOException
    * @throws XMLStreamException
    * @throws SAXException
    * @throws ParserConfigurationException
    */
   public List<TestMasse> testRegressionMasse(String[] checkboxValue,
         boolean isALancer) throws IOException, XMLStreamException,
         SAXException, ParserConfigurationException {
      test = new TestProprietes();
      test.setCheckboxValue(checkboxValue);
      List<TestMasse> resMasse = new ArrayList<TestMasse>();

      // on boucle sur l'ensemble des test de non regression
      for (String reg : checkboxValue) {
         // on stocke le nom de chaque test dans une list
         List<String> records = new ArrayList<String>();
         BufferedReader reader = new BufferedReader(new FileReader(
               testConfig.getTestRegression() + reg));
         String line;
         while ((line = reader.readLine()) != null) {
            records.add(line);
         }
         reader.close();

         // on boucle sur l'ensemble des tes XML de chaque test de non
         // regression
         for (String str : records) {

            // Recupere nom du service a appeller
            String sub = str;
            int debut = str.indexOf("_");
            int fin = str.indexOf("_", debut + 1);
            sub = str.substring(debut + 1, fin);

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
            String ecde;

            // Recuperation du chemin vers l'ECDE
            if (contenu.contains("<saes:urlSommaire>")) {
               ecde = StringUtils.substringBetween(contenu,
                     "<saes:urlSommaire>", "</saes:urlSommaire>");
               ecde = StringUtils.substringBeforeLast(ecde, "/") + "/";
            } else {
               ecde = StringUtils.substringBetween(contenu,
                     "<ns1:urlSommaire>", "</ns1:urlSommaire>");
               ecde = StringUtils.substringBeforeLast(ecde, "/") + "/";
            }

            // appelle du stub si les tests doivent etre lancés
            if (isALancer) {
               // fonction suppression ancien test
               suppressionAncienTest(ecde);
               // appelle au web services
               appelleStub(sub, contenu, str);
            }

            // remplie les informations des tests
            TestMasse testMasse = new TestMasse();
            testMasse.setLienEcde(ecde);
            testMasse.setName(reg);
            testMasse.setValider("NON");
            testMasse.setIsResultatPresent(isResPresent(ecde));
            resMasse.add(testMasse);
         }
      }

      return resMasse;
   }

   /**
    * Fonction vérifiant si le resultat.xml est present dans le repertoire ecde
    * passé en paramètre
    * 
    * @param ecde
    * @return
    */
   public boolean isResPresent(String ecde) {
      String cheminFicSommaire = ecdeService.convertUrlEcdeToPath(ecde);

      String repTraitement = FilenameUtils.getFullPath(cheminFicSommaire);

      String cheminFichierFlag = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_FLAG_TDM);

      File file = new File(cheminFichierFlag);
      boolean isResultatPresent = file.exists();

      return isResultatPresent;
   }

   /**
    * Fonction permettant de vérifier l'attendu pour chaque test de traitement
    * de masse
    * 
    * @param testMasse
    * @return
    * @throws IOException
    */
   public String validerTestMasse(TestMasse testMasse) throws IOException {

      String cheminFicSommaire = ecdeService.convertUrlEcdeToPath(testMasse
            .getLienEcde());

      String repTraitement = FilenameUtils.getFullPath(cheminFicSommaire);

      String cheminFichierResultat = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_RESULTATS);

      File fileResultat = new File(cheminFichierResultat);

      BufferedInputStream in;
      int b;
      StringWriter out;

      String resultat;

      // Recuperation du contenu du fichier resultat
      if (fileResultat.exists()) {
         in = new BufferedInputStream(new FileInputStream(fileResultat));
         out = new StringWriter();
         b = 0;
         while ((b = in.read()) != -1)
            out.write(b);
         out.flush();
         out.close();
         in.close();
         resultat = out.toString();
      } else
         return ("Fichier resultat introuvable");

      // fonction de compare avec resultat et fichier attendu
      String nom = StringUtils.remove(testMasse.getName(), ".txt");
      // Recupere contenu du fichier pour message context

      BufferedReader br = null;
      FileReader fr = null;

      try {

         fr = new FileReader(testConfig.getTestAttendu() + nom + "_attendu.xml");
         br = new BufferedReader(fr);

         String sCurrentLine;

         br = new BufferedReader(new FileReader(testConfig.getTestAttendu()
               + nom + "_attendu.xml"));

         // on verifi que chaque ligne contenu dans le fichier "attendu" se
         // trouve bien dans le resultat du test
         while ((sCurrentLine = br.readLine()) != null) {
            if (!resultat.contains(sCurrentLine))
               return "KO";
         }

      } catch (IOException e) {

         e.printStackTrace();
      }

      return "OK";
   }

   /**
    * Fonction permettant de supprimer les anciens fichiers avant de relancer
    * les tests pour les traitements de masse
    * 
    * @param ecde
    */
   public void suppressionAncienTest(String ecde) {

      String cheminFicSommaire = ecdeService.convertUrlEcdeToPath(ecde);

      String repTraitement = FilenameUtils.getFullPath(cheminFicSommaire);

      String cheminFichierFinFlag = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_FLAG_TDM);
      String cheminFichierDebutFlag = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_DEB_FLAG_TDM);
      String cheminFichierResultat = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_RESULTATS);

      File fileFinFlag = new File(cheminFichierFinFlag);
      File fileDebutFlag = new File(cheminFichierDebutFlag);
      File fileResultat = new File(cheminFichierResultat);

      if (fileFinFlag.exists())
         fileFinFlag.delete();
      if (fileDebutFlag.exists())
         fileDebutFlag.delete();
      if (fileResultat.exists())
         fileResultat.delete();
   }

   /**
    * Fonction permettant de recuperer le contenu des fichiers contenu dans le
    * chemin ecde passé en parametre
    * 
    * @param ecde
    * @return
    * @throws IOException
    */
   public EcdeRepertoire contenuEcde(String ecde) throws IOException {

      EcdeRepertoire rep = new EcdeRepertoire();

      String cheminFicSommaire = ecdeService.convertUrlEcdeToPath(ecde);

      String repTraitement = FilenameUtils.getFullPath(cheminFicSommaire);

      String cheminFichierFinFlag = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_FLAG_TDM);
      String cheminFichierDebutFlag = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_DEB_FLAG_TDM);
      String cheminFichierResultat = FilenameUtils.concat(repTraitement,
            SaeIntegrationConstantes.NOM_FIC_RESULTATS);
      String cheminFichierSommaire = FilenameUtils.concat(repTraitement,
            "sommaire.xml");

      // Recuperation des fichiers de l'ecde
      File fileFinFlag = new File(cheminFichierFinFlag);
      File fileDebutFlag = new File(cheminFichierDebutFlag);
      File fileResultat = new File(cheminFichierResultat);
      File fileSommaire = new File(cheminFichierSommaire);

      BufferedInputStream in;
      int b;
      StringWriter out;

      // Verification si le fichier finTraitement.flag existe
      if (fileFinFlag.exists()) {
         // recuperation du contenu du fichier finTraitement.flag
         in = new BufferedInputStream(new FileInputStream(fileFinFlag));
         out = new StringWriter();
         b = 0;
         while ((b = in.read()) != -1)
            out.write(b);
         out.flush();
         out.close();
         in.close();
         rep.setFinTraitement(out.toString());
      } else
         rep.setFinTraitement("Fichier finTraitement.flag introuvable");

      // Verification si le fichier debutTraitement.flag existe
      if (fileDebutFlag.exists()) {
         // recuperation du contenu du fichier debutTraitement.flag
         in = new BufferedInputStream(new FileInputStream(fileDebutFlag));
         out = new StringWriter();
         b = 0;
         while ((b = in.read()) != -1)
            out.write(b);
         out.flush();
         out.close();
         in.close();
         rep.setDebutTraitement(out.toString());
      } else
         rep.setDebutTraitement("Fichier debutTraitement.flag introuvable");

      // Verification si le fichier resultat.xml existe
      if (fileResultat.exists()) {
         // recuperation du contenu du fichier resultat.xml
         in = new BufferedInputStream(new FileInputStream(fileResultat));
         out = new StringWriter();
         b = 0;
         while ((b = in.read()) != -1)
            out.write(b);
         out.flush();
         out.close();
         in.close();
         rep.setResultat(out.toString());
      } else
         rep.setResultat("Fichier resultat introuvable");

      // Verification si le fichier sommaire.xml existe
      if (fileSommaire.exists()) {
         // recuperation du contenu du fichier sommaire.xml
         in = new BufferedInputStream(new FileInputStream(fileSommaire));
         out = new StringWriter();
         b = 0;
         while ((b = in.read()) != -1)
            out.write(b);
         out.flush();
         out.close();
         in.close();
         rep.setSommaire(out.toString());
      } else
         rep.setSommaire("Fichier sommaire introuvable");

      return rep;
   }

   /**
    * Fonction pour vérifier le resultat du test
    * 
    * @param res
    * @param nomFichier
    * @return
    * @throws IOException
    */
   public boolean checkRes(String res, String nomFichier) throws IOException {

      // fonction de compare avec resultat et fichier attendu
      String nom = StringUtils.remove(nomFichier, "_Out.xml");
      // Recupere contenu du fichier pour message context

      BufferedReader br = null;
      FileReader fr = null;

      try {

         fr = new FileReader(testConfig.getTestAttendu() + nom + "_attendu.xml");
         br = new BufferedReader(fr);

         String sCurrentLine;

         br = new BufferedReader(new FileReader(testConfig.getTestAttendu()
               + nom + "_attendu.xml"));

         // on verifi que chaque ligne contenu dans le fichier "attendu" se
         // trouve bien dans le resultat du test
         while ((sCurrentLine = br.readLine()) != null) {
            if (!res.contains(sCurrentLine))
               return false;
         }

      } catch (IOException e) {

         e.printStackTrace();
      }
      // sinon return true
      return true;

   }

   /**
    * Fonction permettant l'appel au web service du SAE via le stub
    * 
    * @param serviceName
    * @param file
    * @param str
    * @return
    * @throws XMLStreamException
    * @throws SAXException
    * @throws IOException
    * @throws ParserConfigurationException
    */
   public String appelleStub(String serviceName, String file, String str)
         throws XMLStreamException, SAXException, IOException,
         ParserConfigurationException {

      QName q = new javax.xml.namespace.QName(
            "http://www.cirtil.fr/saeService", serviceName);

      // initialisation du stub
      stub = new SaeServiceStub(testConfig.getUrlSaeService());
      stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(5 * 60 * 1000);
      org.apache.axis2.client.OperationClient _operationClient = stub
            ._getServiceClient().createClient(q);
      _operationClient.getOptions().setAction(serviceName);
      _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

      File file2 = new File(testConfig.getTestXml() + str);

      addPropertyToOperationClient(
            _operationClient,
            org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
            "&");

      // initialisation du message context
      MessageContext _messageContext = new org.apache.axis2.context.MessageContext();

      // recuperation du contenu du fichier xml contenant le test
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      XMLStreamReader reader = inputFactory
            .createXMLStreamReader(new FileInputStream(file2));
      StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(reader);
      SOAPMessage soapMessage = builder.getSoapMessage();

      // ajout de l'enveloppe SOAP au message context
      _messageContext.setEnvelope(soapMessage.getSOAPEnvelope());
      // ajout du message context a l'operation client
      _operationClient.addMessageContext(_messageContext);

      // appelle au web service du SAE via le Stub
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

      // retourn le resultat
      return _returnMessageContext.getEnvelope().toString();

   }

   /**
    * Fonction permettant de sauvegarder un nouveau test de non regression sur
    * le serveur
    * 
    * @param file
    * @return
    * @throws IOException
    */
   public boolean sauvegarderTest(File file) throws IOException {

      OutputStream out = null;
      InputStream filecontent = new FileInputStream(file);

      // fontion de sauvegarde d'un test de regression sur le serveur
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
