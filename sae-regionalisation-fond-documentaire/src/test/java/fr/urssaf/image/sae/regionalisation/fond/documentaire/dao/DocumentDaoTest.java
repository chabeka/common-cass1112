/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.DfceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
public class DocumentDaoTest {

   @Autowired
   private DocumentDao dao;

   @Test(expected = IllegalArgumentException.class)
   public void testErreurGetDocumentUuidObligatoire() {
      dao.getDocument(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testErreurSaveDocumentObligatoire() throws DfceException {
      dao.updateDocument(null);
   }

}
