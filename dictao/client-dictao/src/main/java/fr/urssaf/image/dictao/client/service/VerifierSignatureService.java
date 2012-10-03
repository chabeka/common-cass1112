package fr.urssaf.image.dictao.client.service;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;
import org.apache.commons.httpclient.contrib.ssl.AuthSSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;

import fr.urssaf.image.dictao.client.handler.SoapHandler;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.DVSDetailedStatusStruct;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.DataString;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.DataType;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.VerifySignatureEx;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.VerifySignatureExResponse;

public class VerifierSignatureService {

   
   private static String TRANSACTION_ID = "SignatureXadesDetach";
   private static int REFRESH_CRL = 1;
   
   public void verifierSignature(
         String urlWs,
         URL keystoreURL,
         String keyStorePassword,
         URL trustStoreURL,
         String trustStorePassword,
         String signatureAverifier,
         String sha256,
         int nbIterationsAppelWs,
         boolean sysoutSoapEnSortie) {
      
      // Mise en place du SSL
      initSsl(keystoreURL,keyStorePassword,trustStoreURL,trustStorePassword);
      
      // Création du stub d'accès au WS
      DVSStub stub = createStub(urlWs, sysoutSoapEnSortie);
      
      // Construction des objets d'appel au WS
      
      DataType signature = new DataType();
      
      DataString sigXml = new DataString();
      
      // sigXml.setDataFormat(DataEncoding.gzEnc); // Ne pas spécifier le dataFormat
      
      sigXml.setString(signatureAverifier);
      
      signature.setValue(sigXml);
      
      String signedDataHash = String.format(
         "<SignedDataHash>" +
         " <Manifest>" +
         "  <Ref>" +
         "   <DigestMethod>SHA256</DigestMethod>" +
         "   <DigestValue>%s</DigestValue>" +
         "  </Ref>" +
         " </Manifest>" +
         "</SignedDataHash>", sha256);
      
      String tag = "Tag vérification de masse";
      
      DataType businessData = null;
      DataType signedData = null;
      // PluginParameterStruct[] pluginParameter = null;
      String properties = null;
      
      // Boucle d'appel au WS
      for (int iter = 0; iter < nbIterationsAppelWs; iter++) {
         
         String requestId = "Vérification de masse " + Integer.toString(iter+1);
         System.out.println(requestId);
         
         VerifySignatureEx paramEntree = new VerifySignatureEx();
         paramEntree.setRequestId(requestId);
         paramEntree.setTransactionId(TRANSACTION_ID);
         paramEntree.setRefreshCRLs(REFRESH_CRL);
         paramEntree.setTag(tag);
         paramEntree.setBusinessData(businessData);
         paramEntree.setSignature(signature);
         paramEntree.setSignedData(signedData);
         paramEntree.setSignedDataHash(signedDataHash);
         paramEntree.setVerifySignatureExChoice_type0(null) ;
         paramEntree.setProperties(properties);
         paramEntree.setPluginParameter(null);
         
         VerifySignatureExResponse response;
         try {
            response = stub.verifySignatureEx(paramEntree);
         } catch (RemoteException e) {
            throw new RuntimeException(e);
         }
         
         int opStatus = response.getVerifySignatureExResult().getOpStatus();
         int globalStatus = response.getVerifySignatureExResult().getDVSGlobalStatus();
         
         
         System.out.println(String.format("OpStatus : %d, GlobalStatus: %d", opStatus, globalStatus));
         
         DVSDetailedStatusStruct[] details = response.getVerifySignatureExResult().getDVSDetailedStatus().getDVSDetailedStatusStruct();
         for(DVSDetailedStatusStruct detail: details) {
            
            System.out.println(String.format(
                  "SubjectName : %s, DVSStatus: %s", 
                  detail.getSubjectName(), 
                  detail.getDVSStatus()));
         }
         
         System.out.println();
         
      }
      
   }
   
   
   private void initSsl(
         URL keystoreURL,
         String keyStorePassword,
         URL trustStoreURL,
         String trustStorePassword) {
      
      // TODO: Revoir la mise en place du SSL pour ne plus utiliser du code déprécié
      
      AuthSSLProtocolSocketFactory sslFactory;
      try {
         
         sslFactory = new AuthSSLProtocolSocketFactory (
               keystoreURL, keyStorePassword, trustStoreURL, trustStorePassword);
         
      } catch (GeneralSecurityException e) {
         throw new RuntimeException(e);
      } catch (IOException e) {
         throw new RuntimeException(e);
      } 
      
      Protocol authhttps = new Protocol (
            "https", 
            sslFactory,
            443);
      Protocol.registerProtocol("https", authhttps);
      
   }
   
   
   private DVSStub createStub(String urlWs, boolean avecSortieSoap) {
      
      if (!avecSortieSoap) {
         
         try {
            return new DVSStub(urlWs);
         } catch (AxisFault e) {
            throw new RuntimeException(e);
         }
         
      } else {
         
         // Création d'une configuration Axis2 par défaut
         ConfigurationContext configContext;
         try {
            configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null , null);
         } catch (AxisFault fault) {
            throw new RuntimeException(fault);
         }
         
         // Ajout d'un Handler lors de la phase "MessageOut" pour insérer le VI
         AxisConfiguration axisConfig = configContext.getAxisConfiguration();
         List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
         Phase messageOut = findAxis2PhaseByName(outFlowPhases,"MessageOut");
         messageOut.addHandler(new SoapHandler());
         
         // Création de l'objet Stub
         DVSStub stub;
         try {
            stub = new DVSStub(
                  configContext,
                  urlWs);
         } catch (AxisFault e) {
            throw new RuntimeException(e);
         }
         
         // Renvoie l'objet Stub
         return stub;
         
      }
      
   }
   
   
   private static Phase findAxis2PhaseByName(List<Phase> phases, String nomPhaseRecherchee) {
      
      Phase result = null;
      
      for(Phase phase: phases) {
         if (phase.getName().equals(nomPhaseRecherchee)) {
            result = phase;
            break;
         }
      }
      
      return result;
      
   }
   
   
}
