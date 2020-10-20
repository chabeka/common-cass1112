/**
 *
 */
package sae.client.demo.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.ArchivageUnitairePJRequestType;
import sae.client.demo.webservice.modele.ArchivageUnitairePJResponseType;
import sae.client.demo.webservice.modele.DataFileType;
import sae.client.demo.webservice.modele.ListeMetadonneeType;
import sae.client.demo.webservice.modele.MetadonneeType;
import sae.client.demo.webservice.modele.SaeService;
import sae.client.demo.webservice.modele.SaeServicePortType;

/**
 *
 *
 */
public final class ArchivageUtils {

   public static String archivageUnitairePJ() throws IOException {

      // Métadonnées associées au document à archiver
      // Métadonnées obligatoires
      final ArchivageUnitairePJRequestType paramsEntree = getParamsEntree();
      paramsEntree.setDataFile(getDataFileType());

      // Construction du Stub
      SaeService saeService;

      saeService = SaeServiceStubFactory.createStubAvecAuthentification();
      final SaeServicePortType port = saeService.getSaeServicePort();

      // Appel de l'opération archivageUnitairePJ
      final ArchivageUnitairePJResponseType reponse = port.archivageUnitairePJ(paramsEntree);

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idUniqueArchivage = reponse.getIdArchive();
      System.out.println(idUniqueArchivage);
      return idUniqueArchivage;

   }

   /**
    * @return
    */
   public static ArchivageUnitairePJRequestType getParamsEntree() {
      final ListeMetadonneeType listMetasType = new ListeMetadonneeType();
      MetadonneeType metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("ApplicationProductrice");
      metaType.setValeur("ADELAIDE");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("CodeOrganismeGestionnaire");
      metaType.setValeur("UR827");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("CodeOrganismeProprietaire");
      metaType.setValeur("UR827");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("CodeRND");
      metaType.setValeur("2.3.1.1.12");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("DateCreation");
      metaType.setValeur("2011-09-01");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("FormatFichier");
      metaType.setValeur("fmt/354");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("Hash");
      metaType.setValeur("a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("NbPages");
      metaType.setValeur("2");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("Titre");
      metaType.setValeur("Attestation de vigilance");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("TypeHash");
      metaType.setValeur("SHA-1");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("Siren");
      metaType.setValeur("123456777");
      listMetasType.getMetadonnee().add(metaType);
      // ...

      final ArchivageUnitairePJRequestType paramsEntree = new ArchivageUnitairePJRequestType();
      paramsEntree.setMetadonnees(listMetasType);

      return paramsEntree;
   }

   public static DataFileType getDataFileType() {
      // Nom et contenu du fichier
      final String nomFichier = "attestation1234.pdf";
      final InputStream contenu = ResourceUtils.loadResource(new ArchivageUtils(), "archivageUnitairePJ/pj1.pdf");

      final DataFileType dataFile = new DataFileType();
      dataFile.setFileName(nomFichier);
      byte[] contenuBytes;
      try {
         contenuBytes = IOUtils.toByteArray(contenu);
         dataFile.setFile(contenuBytes);
         dataFile.setFileName(nomFichier);
      }
      catch (final IOException e) {
         throw new DemoRuntimeException(e);
      }

      // Renvoie du paramètre d'entrée de l'opération archivageUnitairePJ
      return dataFile;

   }
}
