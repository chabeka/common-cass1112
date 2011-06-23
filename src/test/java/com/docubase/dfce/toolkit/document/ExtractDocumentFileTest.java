package com.docubase.dfce.toolkit.document;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.docubase.toolkit.exception.ged.TagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import com.docubase.dfce.toolkit.base.AbstractTestCaseCreateAndPrepareBase;

public class ExtractDocumentFileTest extends AbstractTestCaseCreateAndPrepareBase {

   @Test
   public void testExtract() throws TagControlException, FileNotFoundException, IOException {

      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.setType("PDF");
      document.addCriterion(base.getBaseCategory(catNames[0]), "testExtract");

      File file = getFile("48Pages.pdf", ExtractDocumentFileTest.class);
      String shaHex = DigestUtils.shaHex(new FileInputStream(file));

      Document documentStored = ServiceProvider.getStoreService().storeDocument(document,
            new FileInputStream(file));

      assertNotNull(documentStored);
      assertNotNull(documentStored.getUuid());

      InputStream in = ServiceProvider.getStoreService().getDocumentFile(documentStored);

      assertEquals(shaHex, DigestUtils.shaHex(in));
      in.close();
   }
}
