package fr.urssaf.image.sae.storage.dfce.data.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.storage.dfce.data.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.data.model.DesiredMetaData;
import fr.urssaf.image.sae.storage.dfce.data.model.SaeDocument;
import fr.urssaf.image.sae.storage.dfce.services.CommonsServices;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;

/**
 * Cette clase contient les tests des services de gestion du fichier xml contenant les
 * données sur la base SAE.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-storage-dfce-test.xml" })
public class SaeDocumentProviderTest {

   @Autowired
   private CommonsServices commonsServices;

   @Before
   public void init() throws ConnectionServiceEx {
      commonsServices.initServicesParameters();
   }

   @After
   public void end() {
      commonsServices.closeServicesParameters();
   }

   @Test
   @SuppressWarnings( { "PMD.AvoidInstantiatingObjectsInLoops",
         "PMD.DataflowAnomalyAnalysis" })
   public void getSaeDocumentsFromXML() throws FileNotFoundException {
      File file[] = new File[Constants.XML_PATH_DOC_WITHOUT_ERROR.length];
      int numFile = 0;
      for (String pathFile : Constants.XML_PATH_DOC_WITHOUT_ERROR) {
         file[numFile] = new File(pathFile);
         numFile++;
      }
      final List<SaeDocument> saeDocuments = commonsServices
            .getXmlDataService().saeDocumentsReader(file);
      Assert.assertNotNull(saeDocuments);
   }

   @Test
   public void getDesiredMetadatas() throws IOException {
      final DesiredMetaData saeMetaData = commonsServices.getXmlDataService()
            .desiredMetaDataReader(
                  new File(Constants.XML_FILE_DESIRED_MDATA[0]));
      Assert.assertNotNull(saeMetaData);
   }
}
