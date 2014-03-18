package fr.urssaf.image.sae.services.mapping;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.io.FileUtils;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.utils.InputStreamSource;
import fr.urssaf.image.sae.model.SAEDocumentMockData;
import fr.urssaf.image.sae.model.SAEMockMetadata;
import fr.urssaf.image.sae.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;

/**
 * Fournit des méthodes statiques pour convertir les objets
 * {@link UntypedDocument} , {@link SAEDocument} et les objets
 * {@link SaeDocument}.
 * 
 */
@SuppressWarnings( { "PMD.LongVariable" })
public final class BeanTestDocumentMapper {
   // CHECKSTYLE:OFF
   /**
    * Permet de convertir les données du document XML vers {@link SAEDocument}.<br/>
    * 
    * @param saeDocumentMockData
    *           : Document à injecter pour les tests.
    * @return Un objet de type {SAEDocument}
    * @throws IOException
    *            lorsque le fichier n'existe pas.
    * @throws ParseException
    *            Exception lorsque le parsing de la chaîne ne se déroule pas
    *            bien.
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public static SAEDocument saeMockDocumentXmlToSAEDocument(
         SAEDocumentMockData saeDocumentMockData) throws IOException,
         ParseException {

      // Initalisation de l'objet StotageDocument
      SAEDocument saeDocument = new SAEDocument();
      // Liste des métadonnées
      List<SAEMetadata> metadatas = new ArrayList<SAEMetadata>();
      // Liste des catégories à partir du XML
      List<SAEMockMetadata> listOfCategoory = saeDocumentMockData
            .getMetadatas();
      for (SAEMockMetadata saeCategory : Utils
            .nullSafeIterable(listOfCategoory)) {
         final String codeMetaData = saeCategory.getCode();
         Object value = saeCategory.getValue();
         // if (Constants.TEC_METADATAS[1].equals(codeMetaData)) {
         // value = Utils.formatStringToDate((String) value);
         // }
         metadatas.add(new SAEMetadata(codeMetaData, value));
      }

      InputStreamSource source = new InputStreamSource(FileUtils
            .openInputStream(new File(saeDocumentMockData.getFilePath())));
      saeDocument.setMetadatas(metadatas);
      DataHandler dataHandler = new DataHandler(source);
      saeDocument.setContent(dataHandler);
      saeDocument.setFilePath(saeDocumentMockData.getFilePath());
      return saeDocument;
   }

   /**
    * Permet de convertir les données du document XML vers
    * {@link UntypedDocument}.<br/>
    * 
    * @param saeDocument
    * @return Un objet de type {@UntypedDocument}
    * @throws IOException
    * @throws ParseException
    */
   @SuppressWarnings( { "PMD.AvoidInstantiatingObjectsInLoops" })
   public static UntypedDocument saeMockDocumentXmlToUntypedDocument(
         SAEDocumentMockData saeDocumentMockData) throws IOException,
         ParseException {

      // Initalisation de l'objet StotageDocument
      UntypedDocument untypedDocument = new UntypedDocument();
      // Liste des métadonnées
      List<UntypedMetadata> metadatas = new ArrayList<UntypedMetadata>();
      // Liste des catégories à partir du XML
      List<SAEMockMetadata> listOfCategoory = saeDocumentMockData
            .getMetadatas();
      for (SAEMockMetadata saeCategory : Utils
            .nullSafeIterable(listOfCategoory)) {
         final String codeMetaData = saeCategory.getCode();
         String value = saeCategory.getValue();
         metadatas.add(new UntypedMetadata(codeMetaData, value));
      }
      untypedDocument.setUMetadatas(metadatas);
      untypedDocument.setFilePath(saeDocumentMockData.getFilePath());
      DataHandler dataHandler = new DataHandler(new InputStreamSource(FileUtils
            .openInputStream(new File(saeDocumentMockData.getFilePath()))));
      untypedDocument.setContent(dataHandler);
      return untypedDocument;
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private BeanTestDocumentMapper() {
      assert false;
   }
   // CHECKSTYLE:ON
}