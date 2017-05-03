package sae.client.demo.webservice;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationGNTGNSResponse;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationGNTGNSResponseType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeType;

public class ConsultationGNTGNSTest {

   /**
    * Exemple de consommation de l'opération consultationGNTGNS du service web
    * SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    * 
    * @throws RemoteException
    */
   @Test
   public void consultationGNTGNS_success() throws RemoteException {

      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      String idArchive = "27a26994-cf69-4375-878f-7c0475c9627e";

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération consultationMTOM,
      // avec les objets modèle générés par Axis2.
      SaeServiceStub.ConsultationGNTGNS paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeConsultationGNTGNS(idArchive);

      // Appel du service web de consultation
      ConsultationGNTGNSResponse reponse = saeService
            .consultationGNTGNS(paramsEntree);

      // Affichage du résultat de la consultation
      afficheResultatConsultationGNTGNS(reponse);

   }

   /**
    * Exemple de consommation de l'opération consultationGNTGNS du service web
    * SaeService<br>
    * <br>
    * Cas sans erreur avec métadonnées(sous réserve que l'identifiant unique
    * d'archivage utilisé dans le test corresponde à une archive en base)
    * 
    * @throws RemoteException
    */
   @Test
   public void consultationGNTGNS_avecMeta_success() throws RemoteException {

      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      String idArchive = "27a26994-cf69-4375-878f-7c0475c9627e";

      // Métadonnées souhaitées en retour de la consultation
      List<String> codesMetasSouhaites = new ArrayList<String>();
      codesMetasSouhaites.add("Siren");
      codesMetasSouhaites.add("CodeRND");
      codesMetasSouhaites.add("CodeOrganismeGestionnaire");
      codesMetasSouhaites.add("NomFichier");

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération consultationMTOM,
      // avec les objets modèle générés par Axis2.
      sae.client.demo.webservice.modele.SaeServiceStub.ConsultationGNTGNS paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeConsultationGNTGNS(idArchive,
                  codesMetasSouhaites);

      // Appel du service web de consultation
      ConsultationGNTGNSResponse reponse = saeService
            .consultationGNTGNS(paramsEntree);

      // Affichage du résultat de la consultation
      afficheResultatConsultationGNTGNS(reponse);

   }

   /**
    * Exemple de consommation de l'opération consultationGNTGNS du service web
    * SaeService<br>
    * <br>
    * Cas avec erreur : On demande un identifiant unique d'archivage qui
    * n'existe pas dans le SAE<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    * <li>Code : sae:ArchiveNonTrouvee</li>
    * <li>Message : Il n'existe aucun document pour l'identifiant d'archivage
    * '00000000-0000-0000-0000-000000000000'</li>
    * </ul>
    */
   @Test
   public void consultationGNTGNS_failure() {

      // Identifiant unique d'archivage inexistant
      String idArchive = "00000000-0000-0000-0000-000000000000";

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération consultationMTOM,
      // avec les objets modèle générés par Axis2.
      sae.client.demo.webservice.modele.SaeServiceStub.ConsultationGNTGNS paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeConsultationGNTGNS(idArchive);

      // Appel de l'opération consultationMTOM
      try {

         // Appel de l'opération consultationMTOM
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé
         // obtenir une SoapFault
         saeService.consultationGNTGNS(paramsEntree);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      } catch (AxisFault fault) {

         // sysout
         TestUtils.sysoutAxisFault(fault);

         // Vérification de la SoapFault
         TestUtils
               .assertSoapFault(
                     fault,
                     "urn:sae:faultcodes",
                     "sae",
                     "ArchiveNonTrouvee",
                     "Il n'existe aucun document pour l'identifiant d'archivage '00000000-0000-0000-0000-000000000000'");

      } catch (RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n"
               + exception);

      }

   }

   private void afficheResultatConsultationGNTGNS(
         ConsultationGNTGNSResponse reponse) {

      ConsultationGNTGNSResponseType consultationResponse = reponse
            .getConsultationGNTGNSResponse();

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
      if (StringUtils.isBlank(valeurMetaNomFichier)) {
         valeurMetaNomFichier = UUID.randomUUID().toString() + ".tmp";
      }

      // Le fichier
      DataHandler contenu = consultationResponse.getContenu();

      // TODO : est-ce que le fichier est bien renvoyé en MTOM

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
}
