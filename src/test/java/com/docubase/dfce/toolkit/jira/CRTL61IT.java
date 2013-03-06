package com.docubase.dfce.toolkit.jira;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;

import org.junit.Test;

import com.docubase.dfce.exception.TagControlException;
import com.docubase.dfce.toolkit.TestUtils;

public class CRTL61IT extends AbstractCRTLIT {
   private static final String SRN_VALUE = "SRN_VALUE";
   private static final Date CREATION_DATE;
   private static final Date LIFE_CYCLE_REFERENCE_DATE;

   static {
      Calendar calendar = Calendar.getInstance();

      calendar.set(2009, 5, 20);
      CREATION_DATE = calendar.getTime();

      calendar.set(2011, 9, 13, 22, 10);
      LIFE_CYCLE_REFERENCE_DATE = calendar.getTime();

   }

   @Test
   public void testCheckDigestRightDigest() throws TagControlException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.addCriterion(SRN, SRN_VALUE);

      document.setLifeCycleReferenceDate(LIFE_CYCLE_REFERENCE_DATE);
      document.setCreationDate(CREATION_DATE);

      InputStream inputStream = TestUtils.getInputStream("doc1.pdf");
      document = serviceProvider.getStoreService().storeDocument(document, "doc1", "pdf",
            "b0315d219d99c7be579a639aa55fa104eff28112", inputStream);

   }

   @Test(expected = TagControlException.class)
   public void testCheckDigestWrongDigest() throws TagControlException {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);
      document.addCriterion(SRN, SRN_VALUE);

      document.setLifeCycleReferenceDate(LIFE_CYCLE_REFERENCE_DATE);
      document.setCreationDate(CREATION_DATE);

      InputStream inputStream = TestUtils.getInputStream("doc1.pdf");
      document = serviceProvider.getStoreService().storeDocument(document, "doc1", "pdf",
            "b0315d219d99c7be579a639aa55fa104eff28111", inputStream);
   }

}
