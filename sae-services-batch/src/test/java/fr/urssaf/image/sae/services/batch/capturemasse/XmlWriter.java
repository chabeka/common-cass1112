/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Classe permettant l'écriture des storagedocument
 * 
 */
public class XmlWriter implements ItemWriter<StorageDocument> {

   private static final SimpleDateFormat FORMAT = new SimpleDateFormat(
         "yyyy-MM-dd");

   private File propertyFile;

   @Autowired
   private MetadataReferenceDAO dao;

   /**
    * executé avant le step
    * 
    * @param stepExecution
    *           le stepExecution
    */
   @BeforeStep
   public final void init(StepExecution stepExecution) {

      ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();

      String pathSommaire = context.getString(Constantes.SOMMAIRE_FILE);
      File sommaire = new File(pathSommaire);
      File parent = sommaire.getParentFile();
      propertyFile = new File(parent, "datas.properties");

   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   public final void write(List<? extends StorageDocument> items)
         throws Exception {

      if (items.size() > 1) {
         throw new RuntimeException(
               "Trop de données à traiter. Un seul élément attendu");
      }

      StorageDocument document = items.get(0);
      Properties properties = new Properties();

      List<StorageMetadata> listMetas = document.getMetadatas();
      MetadataReference reference;
      Object object;
      String value;

      for (StorageMetadata metadata : listMetas) {

         value = null;
         reference = dao.getByShortCode(metadata.getShortCode());

         object = metadata.getValue();

         if (object instanceof Boolean) {
            value = ((Boolean) object).toString();
         } else if (object instanceof Date) {
            value = FORMAT.format((Date) object);
         } else if (object instanceof Long) {
            value = ((Long) object).toString();
         } else if (object instanceof Integer) {
            value = ((Integer) object).toString();
         } else if (object instanceof String) {
            value = (String) object;
         }

         properties.put(reference.getLongCode(), value);
      }

      try {
         FileOutputStream stream = new FileOutputStream(propertyFile);

         try {
            properties.store(stream, "== valeurs à vérifier ==");
         } catch (Exception e) {
            throw new RuntimeException("Impossible de sauvegarder le flux");
         } finally {
            stream.close();
         }

      } catch (Exception e) {
         throw new RuntimeException("Impossible d'ouvrir le flux");
      }
   }
}
