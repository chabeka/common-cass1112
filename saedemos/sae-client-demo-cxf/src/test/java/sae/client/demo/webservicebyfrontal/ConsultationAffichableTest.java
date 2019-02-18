package sae.client.demo.webservicebyfrontal;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import sae.client.demo.util.ArchivageUtils;
import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.ConsultationAffichableRequestType;
import sae.client.demo.webservice.modele.ConsultationAffichableResponseType;
import sae.client.demo.webservice.modele.ListeMetadonneeCodeType;
import sae.client.demo.webservice.modele.ListeMetadonneeType;
import sae.client.demo.webservice.modele.MetadonneeType;
import sae.client.demo.webservice.modele.SaeService;
import sae.client.demo.webservice.modele.SaeServicePortType;

public class ConsultationAffichableTest {

   /**
    * Exemple de consommation de l'opération consultationAffichable du service web SaeService<br>
    * <br>
    * Cas sans erreur (sous réserve que l'identifiant unique d'archivage utilisé
    * dans le test corresponde à une archive en base)
    *
    * @throws IOException
    */
   @Test
   public void consultationAffichable_success() throws IOException {

      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      final String idArchive = ArchivageUtils.archivageUnitairePJ();

      // Construction du Stub
      final SaeService saeService = SaeServiceStubFactory.createStubAvecAuthentification();
      final SaeServicePortType port = saeService.getSaeServicePort();

      // Construction du paramètre d'entrée de l'opération consultationAffichable,
      // avec les objets modèle générés par Axis2.
      final ConsultationAffichableRequestType paramsEntree = new ConsultationAffichableRequestType();
      paramsEntree.setIdArchive(idArchive);

      // Appel du service web de consultation
      final ConsultationAffichableResponseType reponse = port.consultationAffichable(paramsEntree);

      // Affichage du résultat de la consultation
      afficheResultatConsultation(reponse);

   }

   private void afficheResultatConsultation(final ConsultationAffichableResponseType consultationResponse) throws IOException {

      // Les métadonnées

      System.out.println("Métadonnées : ");

      final ListeMetadonneeType tabMetas = consultationResponse.getMetadonnees();

      String valeurMetaNomFichier = "";
      String codeMeta;
      String valeurMeta;
      for (final MetadonneeType metadonnee : tabMetas.getMetadonnee()) {

         codeMeta = metadonnee.getCode();
         valeurMeta = metadonnee.getValeur();

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
      final byte[] contenu = consultationResponse.getContenu();

      // TODO : est-ce que le fichier est bien renvoyé en MTOM

      // On va créér un fichier dans le répertoire temporaire de l'OS
      final String repTempOs = System.getProperty("java.io.tmpdir");
      final File file = new File(repTempOs, valeurMetaNomFichier);

      // Ecrit le flux
      FileUtils.writeByteArrayToFile(file, contenu);

      // Ecrit dans la console le chemin complet du fichier créé
      System.out.println("");
      System.out.println("Fichier créé : " + file.getAbsolutePath());

   }

   /**
    * Exemple de consommation de l'opération consultationAffichable du service web SaeService<br>
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
   public void consultationAffichable_failure() {

      // Identifiant unique d'archivage inexistant
      final String idArchive = "00000000-0000-0000-0000-000000000000";

      // Construction du paramètre d'entrée de l'opération consultationAffichable,
      // avec les objets modèle générés par Axis2.
      final ConsultationAffichableRequestType paramsEntree = new ConsultationAffichableRequestType();
      paramsEntree.setIdArchive(idArchive);

      // Appel de l'opération consultationAffichable
      try {
         // Construction du Stub
         final SaeService saeService = SaeServiceStubFactory.createStubAvecAuthentification();
         final SaeServicePortType port = saeService.getSaeServicePort();
         // Appel de l'opération consultationAffichable
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         port.consultationAffichable(paramsEntree);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      }
      catch (final SOAPFaultException e) {
         // TODO Auto-generated catch block
         final SOAPFaultException fault = e;

         // sysout
         TestUtils.sysoutSoapFault(fault);

         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
                                   fault,
                                   "urn:frontal:faultcodes",
                                   "ns1",
                                   "ArchiveNonTrouvee",
                                   "L'archive 00000000-0000-0000-0000-000000000000 n'a été trouvée dans aucune des instances de la GED.");

      }
      catch (final IOException exception) {

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
    * @throws IOException
    */
   @Test
   public void consultationAffichable_avecMeta_success() throws IOException {

      // Identifiant unique d'archivage de l'archive que l'on veut consulter
      final String idArchive = ArchivageUtils.archivageUnitairePJ();

      // Métadonnées souhaitées en retour de la consultation
      final List<String> codesMetasSouhaites = new ArrayList<String>();
      codesMetasSouhaites.add("Siren");
      codesMetasSouhaites.add("CodeRND");
      codesMetasSouhaites.add("CodeOrganismeGestionnaire");
      codesMetasSouhaites.add("NomFichier");

      // Construction du paramètre d'entrée de l'opération consultationAffichable,
      // avec les objets modèle générés par Axis2.
      final ConsultationAffichableRequestType paramsEntree = new ConsultationAffichableRequestType();
      paramsEntree.setIdArchive(idArchive);
      final ListeMetadonneeCodeType codeTypes = new ListeMetadonneeCodeType();
      codeTypes.getMetadonneeCode().add("Siren");
      codeTypes.getMetadonneeCode().add("CodeRND");
      codeTypes.getMetadonneeCode().add("CodeOrganismeGestionnaire");
      codeTypes.getMetadonneeCode().add("NomFichier");
      paramsEntree.setMetadonnees(codeTypes);

      // Construction du Stub
      final SaeService saeService = SaeServiceStubFactory.createStubAvecAuthentification();
      final SaeServicePortType port = saeService.getSaeServicePort();
      // Appel de l'opération consultationAffichable
      // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
      final ConsultationAffichableResponseType reponse = port.consultationAffichable(paramsEntree);

      // Affichage du résultat de la consultation
      afficheResultatConsultation(reponse);

   }
}
