package com.docubase.dfce.toolkit.document;

import static junit.framework.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;
import com.itextpdf.text.pdf.PdfReader;

public class VirtualDocumentTest extends AbstractTestCaseCreateAndPrepareBase {

   /** The ref document. */
   private static Document refDocument;

   @BeforeClass
   public static void beforeClass() throws Exception {
      if (refDocument == null) {
         refDocument = insertReferenceDocument();
      }
   }

   /**
    * Cette m�thode insert le docuemnt de r�f�rence utiliser pour les tests.
    * 
    * @return the uUID
    * @throws CustomTagControlException
    * @throws IOException
    * @throws FileNotFoundException
    */
   private static Document insertReferenceDocument() throws TagControlException {

      File fileRef = getFile("48pages.pdf", VirtualDocumentTest.class);
      assertNotNull(fileRef);

      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.setType("PDF");

      document.addCriterion(base.getBaseCategory(catNames[0]), "FileRef");

      document = storeDocument(document, fileRef);

      assertNotNull(document);

      logger.info("Ref document stored.");

      return document;
   }

   /**
    * Insert un document virtuel.
    * 
    * @param refDocument
    *           uuid du document de r�f�rence
    * @param startPage
    *           premi�re page
    * @param endPage
    *           derni�re page
    * @param name
    *           nom du document virtuel.
    * 
    * @return le doucument virtuel.
    */
   private static Document insertVirtualDocument(Document refDocument, int startPage, int endPage,
         String name) throws TagControlException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.setCreationDate(generateCreationDate()).setType("pdf");
      document.addCriterion(base.getBaseCategory(catNames[0]),
            "DocumentVirtual" + UUID.randomUUID());

      Document stored = ServiceProvider.getStoreService().storeVirtualDocument(document,
            refDocument, startPage, endPage);
      assertNotNull(stored);
      assertEquals(refDocument.getUuid(), stored.getVirtualReferenceUUID());
      assertEquals(startPage, stored.getVirtualStartPage());
      assertEquals(endPage, stored.getVirtualEndPage());

      return stored;
   }

   /**
    * This method try to valid the given PDF.
    * 
    * @param documentFile
    *           the pdf
    * @param numberOfpage
    *           the number ofpage
    * @throws IOException
    */
   private void checkPDF(InputStream documentFile, int numberOfpage) throws IOException {
      assertNotNull("The PDF file does not exist.", documentFile);

      PdfReader pdfReader;

      try {
         pdfReader = new PdfReader(documentFile);
         assertTrue("PDF Version is not valid.", pdfReader.getPdfVersion() > 0);
         assertEquals("Numbers of pages is not valid.", numberOfpage, pdfReader.getNumberOfPages());
      } finally {
         if (documentFile != null) {
            documentFile.close();
         }
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void test_EndPage_Smaller_Than_StartPage() throws TagControlException {
      Document documentTag = ToolkitFactory.getInstance().createDocumentTag(base);

      ServiceProvider.getStoreService().storeVirtualDocument(documentTag, refDocument, 2, 1);
   }

   @Test(expected = IllegalArgumentException.class)
   public void test_StartPage_Smaller_Than_1() throws TagControlException {
      Document documentTag = ToolkitFactory.getInstance().createDocumentTag(base);

      ServiceProvider.getStoreService().storeVirtualDocument(documentTag, refDocument, 0, 5);
   }

   /**
    * Dans se test, on stocke dans un premier temps un document de r�f�rence �
    * partir duquel seront ind�x� des documents virtuels.
    * 
    * @throws CustomTagControlException
    * @throws IOException
    */
   @Test
   public void testInsertVirtualDocument() throws IOException, TagControlException {
      logger.info("Start testVirtualDocument()...");
      int startPage = 3;
      int endPage = 5;

      Document document = insertVirtualDocument(refDocument, startPage, endPage, "VirtualDoc.pdf");
      document = ServiceProvider.getSearchService().getDocumentByUUID(base, document.getUuid());
      assertNotNull(document.getUuid());
      assertEquals(refDocument.getUuid(), document.getVirtualReferenceUUID());
      assertEquals(startPage, document.getVirtualStartPage());
      assertEquals(endPage, document.getVirtualEndPage());

      InputStream documentFile = ServiceProvider.getStoreService().getDocumentFile(document);
      checkPDF(documentFile, 3);

      logger.info("... end testVirtualDocument().");
      documentFile.close();
   }

   @Test
   public void test_Modify_StartPage() throws IOException, TagControlException {

      int startPage = 3;
      int endPage = 5;

      Document document = insertVirtualDocument(refDocument, startPage, endPage, "StartPage3.pdf");

      document = ServiceProvider.getStoreService().updateVirtualDocument(document, 1, 7);

      assertEquals(1, document.getVirtualStartPage());

      InputStream pdf = ServiceProvider.getStoreService().getDocumentFile(document);

      checkPDF(pdf, 7);

      pdf.close();
   }
}
