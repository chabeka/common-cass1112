package com.docubase.dfce.toolkit.document;

import static junit.framework.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class DocumentVersioningTest extends AbstractTestCaseCreateAndPrepareBase {

   /** The logger. */
   private static Logger logger = Logger.getLogger(DocumentVersioningTest.class);

   /** The file. */
   private static File file;

   @BeforeClass
   public static void beforeClass() {
      file = getFile("48pages.pdf", DocumentVersioningTest.class);
   }

   /**
    * Check the given digest against the computed digest of the given file.
    * 
    * @param digest
    *           the digest
    * @param algorithm
    *           the algorithm
    * @param totottt
    *           the file
    */
   private void checkDigest(String digest, String algorithm, InputStream input) {
      try {
         MessageDigest instance = MessageDigest.getInstance(algorithm);
         byte[] buffer = new byte[4 * 1024];
         int read = input.read(buffer, 0, 4 * 1024);

         while (read > -1) {
            instance.update(buffer, 0, read);
            read = input.read(buffer, 0, 4 * 1024);
         }

         assertEquals(Hex.encodeHexString(instance.digest()), digest);
      } catch (NoSuchAlgorithmException e) {
         fail(e.getMessage());
      } catch (FileNotFoundException e) {
         fail(e.getMessage());
      } catch (IOException e) {
         fail(e.getMessage());
      }
   }

   /**
    * Store document.
    * 
    * @return the document the rhea toolkit exception
    * @throws TagControlException
    */
   private Document storeDocument() throws TagControlException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.setCreationDate(generateCreationDate()).setType("PDF");
      document.addCriterion(base.getBaseCategory(catNames[0]),
            "FileRef_" + System.currentTimeMillis());

      Document stored = storeDocument(document, file);

      Assert.assertNotNull(stored);

      return stored;
   }

   /**
    * Test store document.
    * 
    * @throws IOException
    * @throws TagControlException
    */
   @Test
   public void testStoreDocument() throws TagControlException, IOException {
      Document document = storeDocument();
      UUID uuid = document.getUuid();
      assertNotNull(uuid);

      assertEquals(1, document.getCurrentVersionNumber());
      String digest = document.getDigest();
      String algorithm = document.getDigestAlgorithm();

      assertNotNull("Digest should not be null.", digest);
      assertNotNull("digest algorithm should not be null.", algorithm);

      InputStream inputStream = new FileInputStream(file);
      checkDigest(digest, algorithm, inputStream);

      InputStream documentFile = ServiceProvider.getStoreService().getDocumentFile(document);

      checkDigest(digest, algorithm, documentFile);

      logger.info("Digest = " + digest);
      logger.info("Digest algorithm = " + algorithm);
      logger.info("UUID du document du r�f�rence :" + uuid);
      inputStream.close();
      documentFile.close();
   }

   /**
    * Test update document version.
    * 
    * @throws TagControlException
    * 
    */
   // Versioning is not active by default.
   @Test(expected = RuntimeException.class)
   public void testUpdateDocumentVersion() throws TagControlException, FileNotFoundException {
      Document document = storeDocument();

      Document updated = ServiceProvider.getStoreService().updateFileVersion(document,
            new FileInputStream(file));
      assertNotNull(updated);

      document = ServiceProvider.getSearchService().getDocumentByUUID(base, document.getUuid());
      assertNotNull(document);
      assertEquals(2, document.getCurrentVersionNumber());

   }
}
