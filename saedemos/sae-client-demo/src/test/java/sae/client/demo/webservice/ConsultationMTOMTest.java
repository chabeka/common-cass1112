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
import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationMTOM;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationMTOMResponse;
import sae.client.demo.webservice.modele.SaeServiceStub.ConsultationMTOMResponseType;
import sae.client.demo.webservice.modele.SaeServiceStub.MetadonneeType;

public class ConsultationMTOMTest {

   /**
    * Exemple de consommation de l'opération consultationMTOM du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    * 
    * @throws RemoteException
    */
   @Test
   public void consultationMTOM_success() throws RemoteException {

      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      final String idArchive = "49d43409-1223-42a8-b8a0-6489f0551f03";

      // Construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération consultationMTOM,
      // avec les objets modèle générés par Axis2.
      final ConsultationMTOM paramsEntree = Axis2ObjectFactory.contruitParamsEntreeConsultationMTOM(idArchive);

      // Appel du service web de consultation
      final ConsultationMTOMResponse reponse = saeService.consultationMTOM(paramsEntree);

      // Affichage du résultat de la consultation
      afficheResultatConsultation(reponse);

   }

   private void afficheResultatConsultation(final ConsultationMTOMResponse reponse) {

      final ConsultationMTOMResponseType consultationResponse = reponse
                                                                       .getConsultationMTOMResponse();

      // Les métadonnées

      System.out.println("Métadonnées : ");

      final MetadonneeType[] tabMetas = consultationResponse.getMetadonnees()
                                                            .getMetadonnee();

      String valeurMetaNomFichier = "";
      String codeMeta;
      String valeurMeta;
      for (final MetadonneeType metadonnee : tabMetas) {

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
      final DataHandler contenu = consultationResponse.getContenu();

      // TODO : est-ce que le fichier est bien renvoyé en MTOM

      // On va créér un fichier dans le répertoire temporaire de l'OS
      final String repTempOs = System.getProperty("java.io.tmpdir");
      final File file = new File(repTempOs, valeurMetaNomFichier);

      // Ecrit le flux
      OutputStream outputStream = null;
      try {
         outputStream = new FileOutputStream(file);
      }
      catch (final FileNotFoundException e) {
         throw new DemoRuntimeException(e);
      }
      try {
         contenu.writeTo(outputStream);
      }
      catch (final IOException e) {
         throw new DemoRuntimeException(e);
      }

      // Ecrit dans la console le chemin complet du fichier créé
      System.out.println("");
      System.out.println("Fichier créé : " + file.getAbsolutePath());

   }

   /**
    * Exemple de consommation de l'opération consultationMTOM du service web SaeService<br>
    * <br>
    * Cas avec erreur : On demande un identifiant unique d'archivage qui n'existe pas dans le SAE<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    * <li>Code : sae:ArchiveNonTrouvee</li>
    * <li>Message : Il n'existe aucun document pour l'identifiant d'archivage '00000000-0000-0000-0000-000000000000'</li>
    * </ul>
    */
   @Test
   public void consultationMTOM_failure() {

      // Identifiant unique d'archivage inexistant
      final String idArchive = "00000000-0000-0000-0000-000000000000";

      // Construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération consultationMTOM,
      // avec les objets modèle générés par Axis2.
      final ConsultationMTOM paramsEntree = Axis2ObjectFactory.contruitParamsEntreeConsultationMTOM(idArchive);

      // Appel de l'opération consultationMTOM
      try {

         // Appel de l'opération consultationMTOM
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         saeService.consultationMTOM(paramsEntree);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      }
      catch (final AxisFault fault) {

         // sysout
         TestUtils.sysoutAxisFault(fault);

         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
                                   fault,
                                   "urn:sae:faultcodes",
                                   "ArchiveNonTrouvee",
                                   "Il n'existe aucun document pour l'identifiant d'archivage '00000000-0000-0000-0000-000000000000'");

      }
      catch (final RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);

      }

   }

   /**
    * Exemple de consommation de l'opération consultation du service web SaeService<br>
    * <br>
    * On spécifie les métadonnées souhaitées en retour de la consultation.<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    * 
    * @throws RemoteException
    */
   @Test
   public void consultationMTOM_avecMeta_success() throws RemoteException {

      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      final String idArchive = "EA6DC1AE-D226-4765-853E-4CACED884C18";

      // Métadonnées souhaitées en retour de la consultation
      final List<String> codesMetasSouhaites = new ArrayList<>();
      codesMetasSouhaites.add("Siren");
      codesMetasSouhaites.add("CodeRND");
      codesMetasSouhaites.add("CodeOrganismeGestionnaire");
      codesMetasSouhaites.add("NomFichier");

      // Construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération consultationMTOM,
      // avec les objets modèle générés par Axis2.
      final ConsultationMTOM paramsEntree = Axis2ObjectFactory.contruitParamsEntreeConsultationMTOM(
                                                                                                    idArchive,
                                                                                                    codesMetasSouhaites);

      // Appel du service web de consultation
      final ConsultationMTOMResponse reponse = saeService.consultationMTOM(paramsEntree);

      // Affichage du résultat de la consultation
      afficheResultatConsultation(reponse);

   }

}
