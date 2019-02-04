/**
 *
 */
package sae.client.demo.utils;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.axis2.Constants;

import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitairePJ;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitairePJResponse;

/**
 *
 *
 */
public final class ArchivageUtils {

   public static String archivageUnitairePJ() throws RemoteException {

      // Fichier à archiver
      final String nomFichier = "attestation1234.pdf";
      final InputStream contenu = ResourceUtils.loadResource(new ArchivageUtils(), "archivageUnitairePJ/pj1.pdf");

      // Métadonnées associées au document à archiver
      final HashMap<String, String> metadonnees = new HashMap<String, String>();
      // Métadonnées obligatoires
      metadonnees.put("ApplicationProductrice", "ADELAIDE");
      metadonnees.put("CodeOrganismeGestionnaire", "CER69");
      metadonnees.put("CodeOrganismeProprietaire", "CER69");
      metadonnees.put("CodeRND", "2.3.1.1.12");
      metadonnees.put("DateCreation", "2011-09-01");
      metadonnees.put("FormatFichier", "fmt/354");
      metadonnees.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.put("NbPages", "2");
      metadonnees.put("Titre", "Attestation de vigilance");
      metadonnees.put("TypeHash", "SHA-1");
      // Des métadonnées spécifiables à l'archivage
      metadonnees.put("Siren", "123456780");
      // ...

      // Construction du Stub
      final SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Activation de l'optimisation MTOM si demandée

      saeService._getServiceClient().getOptions().setProperty(
                                                              Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);

      // Construction du paramètre d'entrée de l'opération archivageUnitairePJ,
      // avec les objets modèle générés par Axis2.
      final ArchivageUnitairePJ paramsEntree = Axis2ObjectFactory.contruitParamsEntreeArchivageUnitairePJavecContenu(
                                                                                                                     nomFichier, contenu, metadonnees);

      // Appel de l'opération archivageUnitairePJ
      final ArchivageUnitairePJResponse reponse = saeService.archivageUnitairePJ(paramsEntree);

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idUniqueArchivage = reponse.getArchivageUnitairePJResponse().getIdArchive().toString();
      System.out.println(idUniqueArchivage);
      return idUniqueArchivage;
   }
}
