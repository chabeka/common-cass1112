package fr.urssaf.image.sae.storage.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

public class StorageMetadataUtilsTest {

   @Test
   public void valueMetadataFinder() {

      List<StorageMetadata> storageMetadatas = new ArrayList<StorageMetadata>();

      storageMetadatas.add(new StorageMetadata("code1", "value1"));
      storageMetadatas.add(new StorageMetadata("code2", "value2"));
      storageMetadatas.add(new StorageMetadata("code3", "value3"));

      Assert.assertEquals("La valeur de la métadonnée est inattendue",
            "value1", StorageMetadataUtils.valueMetadataFinder(
                  storageMetadatas, "code1"));
      Assert.assertEquals("La valeur de la métadonnée est inattendue",
            "value2", StorageMetadataUtils.valueMetadataFinder(
                  storageMetadatas, "code2"));
      Assert.assertEquals("La valeur de la métadonnée est inattendue",
            "value3", StorageMetadataUtils.valueMetadataFinder(
                  storageMetadatas, "code3"));

      Assert
            .assertNull("Aucune valeur n'est attendu pour code4",
                  StorageMetadataUtils.valueMetadataFinder(storageMetadatas,
                        "code4"));

   }

   @Test
   public void valueObjectMetadataFinder() {
      
      List<StorageMetadata> storageMetadatas = new ArrayList<StorageMetadata>();
      
      Date debut2014 = new DateTime().withDate(2014, 1, 1).withTime(0, 0, 0, 0).toDate(); 

      storageMetadatas.add(new StorageMetadata("code1", "value1"));
      storageMetadatas.add(new StorageMetadata("code2", debut2014));
      storageMetadatas.add(new StorageMetadata("code3", Long.valueOf(99)));
      storageMetadatas.add(new StorageMetadata("code4", Float.valueOf(99.99f)));

      Assert.assertEquals("La valeur de la métadonnée est inattendue",
            "value1", StorageMetadataUtils.valueObjectMetadataFinder(
                  storageMetadatas, "code1"));
      Assert.assertEquals("La valeur de la métadonnée est inattendue",
            debut2014, StorageMetadataUtils.valueObjectMetadataFinder(
                  storageMetadatas, "code2"));
      Assert.assertEquals("La valeur de la métadonnée est inattendue",
            Long.valueOf(99), StorageMetadataUtils.valueObjectMetadataFinder(
                  storageMetadatas, "code3"));
      Assert.assertEquals("La valeur de la métadonnée est inattendue",
            Float.valueOf(99.99f), StorageMetadataUtils.valueObjectMetadataFinder(
                  storageMetadatas, "code4"));

      Assert
            .assertNull("Aucune valeur n'est attendu pour code4",
                  StorageMetadataUtils.valueMetadataFinder(storageMetadatas,
                        "code5"));

   }
}
