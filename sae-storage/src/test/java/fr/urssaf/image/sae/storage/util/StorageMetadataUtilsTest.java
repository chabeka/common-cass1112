package fr.urssaf.image.sae.storage.util;

import java.util.ArrayList;
import java.util.List;

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

}
