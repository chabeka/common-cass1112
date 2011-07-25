package fr.urssaf.image.sae.storage.dfce.data.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.storage.dfce.data.model.DesiredMetaData;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.data.test.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.services.CommonServicesImpl;

/**
 * Cette clase contient les tests des services de gestion du fichier xml contenant les
 * données sur la base SAE.
 * 
 * @author akenore, rhofir.
 * 
 */
public class SaeDocumentProviderTest extends CommonServicesImpl {
   @Test
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public void getSaeDocumentsFromXML() throws FileNotFoundException {
      File file[] = new File[7];
      int numFile = 0;
      for (String pathFile : Constants.XML_FILE_PATH) {
         file[numFile] = new File(pathFile);
         numFile++;
      }
      final List<SaeDocument> saeDocuments = getXmlDataService()
            .saeDocumentsReader(file);
      Assert.assertNotNull(saeDocuments);
   }

   @Test
   public void getDesiredMetadatas() throws IOException {
      final DesiredMetaData saeMetaData = getXmlDataService()
            .desiredMetaDataReader(new File(Constants.XML_FILE_DESIRED_MDATA[0]));
      Assert.assertNotNull(saeMetaData);
   }
}
