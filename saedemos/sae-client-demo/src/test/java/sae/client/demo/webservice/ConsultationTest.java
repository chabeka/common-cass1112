package sae.client.demo.webservice;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;

import javax.activation.DataHandler;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.Consultation;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationResponse;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationResponseType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeType;
import sae.client.demo.webservice.modele.SaeServiceStub.ObjetNumeriqueConsultationType;

public class ConsultationTest {

   
   /**
    * Exemple de consommation de l'opération consultation du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    * 
    * @throws RemoteException 
    */
   @Test
   public void consultation_success() throws RemoteException {
      
      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      String idArchive = "1261E8B1-B1AF-4562-82DF-3268C888E9AB";
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération consultation, 
      //  avec les objets modèle générés par Axis2.
      Consultation paramsEntree = Axis2ObjectFactory.contruitParamsEntreeConsultation(idArchive);
      
      // Appel du service web de consultation
      ConsultationResponse reponse = saeService.consultation(paramsEntree);
      
      // Affichage du résultat de la consultation
      afficheResultatConsultation(reponse);
      
   }
   
   
   private void afficheResultatConsultation(ConsultationResponse reponse) {

      ConsultationResponseType consultationResponse = reponse
            .getConsultationResponse();

      // Les métadonnées

      System.out.println("Métadonnées : ");

      MetadonneeType[] tabMetas = consultationResponse.getMetadonnees()
            .getMetadonnee();

      String valeurMetaNomFichier = "";
      String codeMeta;
      String valeurMeta;
      for (MetadonneeType metadonnee : tabMetas) {

         codeMeta = metadonnee.getCode().getMetadonneeCodeType();
         valeurMeta = metadonnee.getValeur().getMetadonneeValeurType();

         System.out.println(codeMeta + "=" + valeurMeta);

         // Mémorise la valeur de la métadonnée NomFichier
         // pour l'écriture ultérieure du fichier renvoyée
         if (codeMeta.equals("NomFichier")) {
            valeurMetaNomFichier = valeurMeta;
         }

      }

      // Le fichier
      ObjetNumeriqueConsultationType objetNumerique = consultationResponse
            .getObjetNumerique();

      // Récupère le flux base64 renvoyé
      DataHandler contenu = objetNumerique
            .getObjetNumeriqueConsultationTypeChoice_type0().getContenu();

      // On va créér un fichier dans le répertoire temporaire de l'OS
      String repTempOs = System.getProperty("java.io.tmpdir");
      File file = new File(repTempOs, valeurMetaNomFichier);

      // Ecrit le flux
      OutputStream outputStream = null;
      try {
         outputStream = new FileOutputStream(file);
      } catch (FileNotFoundException e) {
         throw new DemoRuntimeException(e);
      }
      try {
         contenu.writeTo(outputStream);
      } catch (IOException e) {
         throw new DemoRuntimeException(e);
      }

      // Ecrit dans la console le chemin complet du fichier créé
      System.out.println("");
      System.out.println("Fichier créé : " + file.getAbsolutePath());

   }
   
   
   /**
    * Exemple de consommation de l'opération consultation du service web SaeService<br>
    * <br>
    * Cas avec erreur : On demande un identifiant unique d'archivage qui n'existe pas dans le SAE<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : sae:ArchiveNonTrouvee</li>
    *    <li>Message : Il n'existe aucun document pour l'identifiant d'archivage '00000000-0000-0000-0000-000000000000'</li>
    * </ul>
    */
   @Test
   public void consultation_failure() {
      
      // Identifiant unique d'archivage inexistant
      String idArchive = "00000000-0000-0000-0000-000000000000";
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération consultation, 
      //  avec les objets modèle générés par Axis2.
      Consultation paramsEntree = Axis2ObjectFactory.contruitParamsEntreeConsultation(idArchive);
      
      // Appel de l'opération consultation
      try {
         
         // Appel de l'opération consultation
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         saeService.consultation(paramsEntree);
         
         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");
         
      } catch (AxisFault fault) {
         
         // sysout
         System.out.println("Une SoapFault a été obtenue");
         System.out.println("Code namespace : " + fault.getFaultCode().getNamespaceURI());
         System.out.println("Code préfixe : " + fault.getFaultCode().getPrefix());
         System.out.println("Code partie locale : " + fault.getFaultCode().getLocalPart());
         System.out.println("Message : " + fault.getReason());
         
         // Test le code de la SoapFault
         // Le namespace
         assertEquals(
               "Le namespace du code de la SoapFault est incorrect",
               "urn:sae:faultcodes",
               fault.getFaultCode().getNamespaceURI());
         // Le préfixe
         assertEquals(
               "Le préfixe du code de la SoapFault est incorrect",
               "sae",
               fault.getFaultCode().getPrefix());
         // La partie locale
         assertEquals(
               "La partie locale du code de la SoapFault est incorrecte",
               "ArchiveNonTrouvee",
               fault.getFaultCode().getLocalPart());
         
         // Test le message de la SoapFault
         assertEquals(
               "Le message de la SoapFault est incorrecte",
               "Il n'existe aucun document pour l'identifiant d'archivage '00000000-0000-0000-0000-000000000000'",
               fault.getReason());
       
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault");
         
      }
      
   }
   
}
