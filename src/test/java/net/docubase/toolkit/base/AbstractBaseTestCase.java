package net.docubase.toolkit.base;

import java.io.File;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import net.docubase.toolkit.exception.ged.CustomTagControlException;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.document.ICustomTagControl;
import net.docubase.toolkit.model.search.ChainedFilter;
import net.docubase.toolkit.service.ServiceProvider;

import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class AbstractBaseTestCase {
   /* Instance de la base GED. Utilisée pour définir / modifier la base GED */
   protected static Base base;

   protected static Document storeDoc(Document document, File newDoc, ICustomTagControl control,
         boolean expectStore) {
      boolean stored;
      try {
         stored = ServiceProvider.getStoreService().storeDocument(document, newDoc, control);
         Assert.assertEquals(expectStore, stored);
      } catch (CustomTagControlException e) {
         Assert.assertFalse(expectStore);
      }
      return document;
   }

   protected static void deleteBase(Base base) {
      ServiceProvider.getBaseAdministrationService().stopBase(base);
      ServiceProvider.getBaseAdministrationService().deleteBase(base);
   }

   protected static File getFile(String fileName, Class<?> clazz) {
      if (fileName.startsWith("/") || fileName.startsWith("\\")) {
         fileName = fileName.substring(1, fileName.length());
      }
      File file = new File(clazz.getResource(fileName).getPath());
      if (!file.exists()) {
         file = new File(clazz.getResource("/" + fileName).getPath());
      }

      return file;
   }

   /**
    * Génére une date de crétation. Date du jour moins 2 heures.
    * 
    * @return the date
    */
   protected static Date generateCreationDate() {
      return new DateTime(new Date()).minusHours(2).toDate();
   }

   protected int searchLucene(String query, int searchLimit) {
      return searchLucene(query, searchLimit, null);

   }

   protected int searchLucene(String query, int searchLimit, ChainedFilter chainedFilter) {
      List<Document> docs = ServiceProvider.getSearchService()
            .search(query, searchLimit, base, chainedFilter).getDocuments();
      return docs == null ? 0 : docs.size();
   }
}
