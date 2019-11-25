package sae.integration.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import sae.integration.util.SoapBuilder;
import sae.integration.webservice.modele.DataFileType;
import sae.integration.webservice.modele.ListeMetadonneeType;

/**
 * Permet de générer des données de test
 */
public class TestData {

   private TestData() {
      // Classe statique
   }

   /**
    * Permet d'alimenter les méta nécessaires (hash, nom du fichier, ...) relatives au fichier pdf de test
    * 
    * @param listMetasType
    */
   public static void updateMetaForPdfFile(final ListeMetadonneeType listMetasType) {
      getPdfFile(listMetasType);
   }

   /**
    * Permet d'alimenter les méta nécessaires (hash, nom du fichier, ...) relatives au fichier tiff de test
    * 
    * @param listMetasType
    */
   public static void updateMetaForTiffFile(final ListeMetadonneeType listMetasType) {
      getTiffFile(listMetasType);
   }

   /**
    * Permet d'alimenter les méta nécessaires (hash, nom du fichier, ...) relatives au fichier txt de test
    * 
    * @param listMetasType
    */
   public static void updateMetaForTxtFile(final ListeMetadonneeType listMetasType) {
      getTxtFile(listMetasType);
   }

   /**
    * Récupère un fichier PDF de test, pour archivage
    * Alimente les métadonnées relatives à ce fichier
    * 
    * @param listMetasType
    *           métadonnées à alimenter
    * @return
    *         Le fichier PDF prêt à être archivé
    */
   public static DataFileType getPdfFile(final ListeMetadonneeType listMetasType) {
      return getFile("documents/testDoc.pdf", "fmt/354", "1", listMetasType);
   }

   /**
    * Récupère un fichier TIFF de test, pour archivage
    * Alimente les métadonnées relatives à ce fichier
    * 
    * @param listMetasType
    *           métadonnées à alimenter
    * @return
    *         Le fichier TIFF prêt à être archivé
    */
   public static DataFileType getTiffFile(final ListeMetadonneeType listMetasType) {
      return getFile("documents/testDoc.tif", "fmt/353", "1", listMetasType);
   }

   /**
    * Récupère un fichier TXT de test, pour archivage
    * Alimente les métadonnées relatives à ce fichier
    * 
    * @param listMetasType
    *           métadonnées à alimenter
    * @return
    *         Le fichier TXT prêt à être archivé
    */
   public static DataFileType getTxtFile(final ListeMetadonneeType listMetasType) {
      return getFile("documents/testDoc.txt", "x-fmt/111", "1", listMetasType);
   }

   public static DataFileType getFile(final String resourcePath, final String formatPronom, final String nbPages, final ListeMetadonneeType metaList) {
      final String nomFichier = Paths.get(resourcePath).getFileName().toString();
      final InputStream contenu = TestData.class.getClassLoader().getResourceAsStream(resourcePath);

      final DataFileType dataFile = new DataFileType();
      dataFile.setFileName(nomFichier);
      byte[] contenuBytes;
      try {
         contenuBytes = IOUtils.toByteArray(contenu);
         dataFile.setFile(contenuBytes);
         dataFile.setFileName(nomFichier);
      }
      catch (final IOException e) {
         throw new RuntimeException(e);
      }

      // Alimentation des métadonnées associées au fichier
      SoapBuilder.setMetaValue(metaList, "FormatFichier", formatPronom);
      SoapBuilder.setMetaValue(metaList, "Hash", DigestUtils.sha1Hex(contenuBytes));
      SoapBuilder.setMetaValue(metaList, "TypeHash", "SHA-1");
      SoapBuilder.setMetaValue(metaList, "NbPages", nbPages);
      return dataFile;
   }

   /**
    * Alimente, dans la liste des métadonnées, les métadonnées relatives du document PDF de test, à savoir,
    * FormatFichier, Hash, TypeHash, et NbPages
    * 
    * @param listMetasType
    *           la liste de méta à enrichir
    * @return le chemin du fichier
    */
   public static String addPdfFileMeta(final ListeMetadonneeType listMetasType) {
      final DataFileType file = getPdfFile(listMetasType);
      return "documents/" + file.getFileName();
   }

   /**
    * Alimente, dans la liste des métadonnées, les métadonnées relatives du document Tiff de test, à savoir,
    * FormatFichier, Hash, TypeHash, et NbPages
    * 
    * @param listMetasType
    *           la liste de méta à enrichir
    * @return le chemin du fichier
    */
   public static String addTiffFileMeta(final ListeMetadonneeType listMetasType) {
      final DataFileType file = getTiffFile(listMetasType);
      return "documents/" + file.getFileName();
   }

   /**
    * Alimente, dans la liste des métadonnées, les métadonnées relatives du document TXT de test, à savoir,
    * FormatFichier, Hash, TypeHash, et NbPages
    * 
    * @param listMetasType
    *           la liste de méta à enrichir
    * @return le chemin du fichier
    */
   public static String addTxtFileMeta(final ListeMetadonneeType listMetasType) {
      final DataFileType file = getTxtFile(listMetasType);
      return "documents/" + file.getFileName();
   }

}
