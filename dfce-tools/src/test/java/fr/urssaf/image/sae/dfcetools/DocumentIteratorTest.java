/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.image.sae.dfcetools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.dfcetools.helper.DFCEServicesHelper;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.IndexPaginationSearchQuery;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.model.search.SearchResult;

/**
 * TODO (ac75007394) Description du type
 */
public class DocumentIteratorTest {

   @Test
   public void documentIterator_Test() throws Exception {
      // final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("hwi69progednatgntcot1boappli3.cer69.recouv", "GNT-PROD", 8080);
      final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("hwi31intgntv6boappli1.gidn.recouv", "GNT-INT", 8080);

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      // final String query = "(ctr:\"TP17\" OR ctr:\"PCA1\" OR ctr:\"TD50\" OR ctr:\"TC07\" OR ctr:\"PC01\" OR ctr:\"PC23\" OR ctr:\"RC06\" OR ctr:\"TC17\" OR ctr:\"TC06\" OR ctr:\"PC25\" OR ctr:\"PC20\" OR ctr:\"PC30\" OR ctr:\"PC21\" OR ctr:\"PC16\") AND (swa:\"PRET\") AND (cop:\"UR727\") AND (cpr:\"PD28A\" OR cpr:\"PC77A\" OR cpr:\"NC80\" OR cpr:\"PC37C\" OR cpr:\"NC81\" OR cpr:\"PC25B\" OR cpr:\"PC37D\" OR cpr:\"NC60\" OR cpr:\"NC65\" OR cpr:\"NC21\" OR cpr:\"NC43\" OR cpr:\"PC24\" OR cpr:\"NC44\" OR cpr:\"NC45\" OR cpr:\"NC46\" OR cpr:\"NC29\" OR cpr:\"NC09\" OR cpr:\"TD50O\" OR cpr:\"NC03\" OR cpr:\"NC47\" OR cpr:\"NC04\" OR cpr:\"NC48\" OR cpr:\"NC27\" OR cpr:\"NC05\" OR cpr:\"TD50K\" OR cpr:\"NC21A\" OR cpr:\"PC66A\" OR cpr:\"NC54\" OR cpr:\"PP38A\" OR cpr:\"NC10\" OR cpr:\"NC11\" OR cpr:\"NC12\" OR cpr:\"PC30\" OR cpr:\"PC64A\" OR cpr:\"NC31\" OR cpr:\"NC18\" OR cpr:\"NC19\" OR cpr:\"QC66A\" OR cpr:\"NC36\" OR cpr:\"NC58\" OR cpr:\"NC14\" OR cpr:\"NC59\" OR cpr:\"PC16\" OR
      // cpr:\"NC16\" OR cpr:\"NC17\") AND (cot:\"true\") AND SM_ARCHIVAGE_DATE:[20190607 TO 20190617]";
      // final String query = "(ctr:\"TP17\" OR ctr:\"PCA1\" OR ctr:\"TD50\" OR ctr:\"TC07\" OR ctr:\"PC01\" OR ctr:\"PC23\" OR ctr:\"RC06\" OR ctr:\"TC17\" OR ctr:\"TC06\" OR ctr:\"PC25\" OR ctr:\"PC20\" OR ctr:\"PC30\" OR ctr:\"PC21\" OR ctr:\"PC16\") AND (swa:\"PRET\") AND (cop:\"UR727\") AND (cpr:\"PD28A\" OR cpr:\"PC77A\" OR cpr:\"NC80\" OR cpr:\"PC37C\" OR cpr:\"NC81\" OR cpr:\"PC25B\" OR cpr:\"PC37D\" OR cpr:\"NC60\" OR cpr:\"NC65\" OR cpr:\"NC21\" OR cpr:\"NC43\" OR cpr:\"PC24\" OR cpr:\"NC44\" OR cpr:\"NC45\" OR cpr:\"NC46\" OR cpr:\"NC29\" OR cpr:\"NC09\" OR cpr:\"TD50O\" OR cpr:\"NC03\" OR cpr:\"NC47\" OR cpr:\"NC04\" OR cpr:\"NC48\" OR cpr:\"NC27\" OR cpr:\"NC05\" OR cpr:\"TD50K\" OR cpr:\"NC21A\" OR cpr:\"PC66A\" OR cpr:\"NC54\" OR cpr:\"PP38A\" OR cpr:\"NC10\" OR cpr:\"NC11\" OR cpr:\"NC12\" OR cpr:\"PC30\" OR cpr:\"PC64A\" OR cpr:\"NC31\" OR cpr:\"NC18\" OR cpr:\"NC19\" OR cpr:\"QC66A\" OR cpr:\"NC36\" OR cpr:\"NC58\" OR cpr:\"NC14\" OR cpr:\"NC59\" OR cpr:\"PC16\" OR
      // cpr:\"NC16\" OR cpr:\"NC17\") AND (cot:\"true\") AND SM_ARCHIVAGE_DATE:[20190617001019373 TO 20190618]";
      // final String query = "(ctr:\"RP17\" OR ctr:\"TP17\" OR ctr:\"TD50\" OR ctr:\"PC01\" OR ctr:\"TC07\" OR ctr:\"PC23\" OR ctr:\"RC06\" OR ctr:\"TC06\" OR ctr:\"TC17\" OR ctr:\"PC25\" OR ctr:\"PC20\" OR ctr:\"PC21\" OR ctr:\"PC16\" OR ctr:\"RC07\") AND (swa:\"PRET\") AND (cop:\"UR827\") AND (cpr:\"PC77A\" OR cpr:\"NC80\" OR cpr:\"NC81\" OR cpr:\"PC25B\" OR cpr:\"NC60\" OR cpr:\"PC37A\" OR cpr:\"L01\" OR cpr:\"L00\" OR cpr:\"NC43\" OR cpr:\"NC21\" OR cpr:\"NC65\" OR cpr:\"PC24\" OR cpr:\"NC44\" OR cpr:\"NC45\" OR cpr:\"NC46\" OR cpr:\"NC63\" OR cpr:\"NC64\" OR cpr:\"NC29\" OR cpr:\"NC09\" OR cpr:\"NC03\" OR cpr:\"NC47\" OR cpr:\"NC04\" OR cpr:\"NC48\" OR cpr:\"NC05\" OR cpr:\"QC37A\" OR cpr:\"NC21A\" OR cpr:\"PC66A\" OR cpr:\"PP38A\" OR cpr:\"NC10\" OR cpr:\"NC11\" OR cpr:\"NC12\" OR cpr:\"PC64A\" OR cpr:\"NC31\" OR cpr:\"NC18\" OR cpr:\"NC19\" OR cpr:\"QC66A\" OR cpr:\"NC58\" OR cpr:\"NC36\" OR cpr:\"NC14\" OR cpr:\"NC59\" OR cpr:\"PC16\" OR cpr:\"NC16\" OR cpr:\"NC17\") AND
      // (cot:\"true\") AND SM_ARCHIVAGE_DATE:[20190618 TO 201906181224]";
      final String query = "(cot:\"true\") AND (cop:\"UR827\") AND (swa:\"PRET\") AND (cpr:\"PC77A\" OR cpr:\"PC66A\") AND SM_ARCHIVAGE_DATE:[20190618 TO 201906181224]";
      final SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(query, dfceServices.getBase());
      final List<String> indexOrderPreferenceList = new ArrayList<>();
      indexOrderPreferenceList.add("cot&cop&swa&SM_ARCHIVAGE_DATE&");
      // indexOrderPreferenceList.add("cot&cop&swa&cpr&ctr&SM_ARCHIVAGE_DATE&");
      // final SearchQuery searchQuery = new IndexPaginationSearchQuery(query, dfceServices.getBase());
      searchQuery.setIndexOrderPreference(indexOrderPreferenceList);
      searchQuery.setSearchLimit(200); // Pagination

      final Iterator<Document> iterateur = dfceServices.createDocumentIterator(searchQuery);
      int counter = 0;
      while (iterateur.hasNext()) {
         final Document doc = iterateur.next();
         final Date archivageDate = doc.getArchivageDate();
         final UUID uuid = doc.getUuid();
         counter++;
         if (counter % 200 == 0) {
            System.out.println(counter + " " + uuid + " " + archivageDate);
         }
         sysout.println(counter + " " + uuid + " " + archivageDate);
      }
      System.out.println("Compteur : " + counter);
   }

   @Test
   public void documentIterator_TestHubert() throws Exception {
      final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("hwi31dev2gntboappli1.gidn.recouv", "GNT-DEV2", 8080);

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      final String query = "(ctr:\"PC25-FS\" OR ctr:\"PCA1-FS\" OR ctr:\"PCA1\" OR ctr:\"TC17-FS\" OR ctr:\"TD50\" OR ctr:\"PC16-FS\" OR ctr:\"PC23\" OR ctr:\"TC07\" OR ctr:\"PC01\" OR ctr:\"RC06\" OR ctr:\"TC06\" OR ctr:\"TC17\" OR ctr:\"PC25\" OR ctr:\"PC20\" OR ctr:\"PC20-FS\" OR ctr:\"PC21\" OR ctr:\"TC06-FS\" OR ctr:\"TC07-FS\") "
            + "AND (swa:\"PRET\") AND (cop:\"UR827\") "
            + "AND (cpr:\"PC77A\" OR cpr:\"PC25B\" OR cpr:\"PC37C\" OR cpr:\"NC60\" OR cpr:\"PC37D\" OR cpr:\"NC21\" OR cpr:\"NC65\" OR cpr:\"NC20\" OR cpr:\"NC29\" OR cpr:\"NC27\" OR cpr:\"PC66A\" OR cpr:\"T50L0\" OR cpr:\"T50L1\" OR cpr:\"NC10\" OR cpr:\"NC54\" OR cpr:\"NC11\" OR cpr:\"NC12\" OR cpr:\"NC13\" OR cpr:\"NC18\" OR cpr:\"NC19\" OR cpr:\"QC66A\" OR cpr:\"NC14\" OR cpr:\"NC58\" OR cpr:\"NC59\" OR cpr:\"NC16\" OR cpr:\"NC17\" OR cpr:\"NC80\" OR cpr:\"NC81\" OR cpr:\"NC43\" OR cpr:\"PC24\" OR cpr:\"NC87\" OR cpr:\"NC44\" OR cpr:\"NC88\" OR cpr:\"NC45\" OR cpr:\"NC46\" OR cpr:\"PC25B-FS\" OR cpr:\"NC86\" OR cpr:\"NC09\" OR cpr:\"NC03\" OR cpr:\"NC47\" OR cpr:\"NC48\" OR cpr:\"NC04\" OR cpr:\"NC05\" OR cpr:\"PCA1J\" OR cpr:\"NC21A\" OR cpr:\"PC64A\" OR cpr:\"NC31\" OR cpr:\"NC36\" OR cpr:\"PC16\") "
            + "AND (cot:\"true\") AND SM_ARCHIVAGE_DATE:[20190618 TO 20190628]";
      final SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(query, dfceServices.getBase());
      final List<String> indexOrderPreferenceList = new ArrayList<>();
      // indexOrderPreferenceList.add("cot&cop&swa&SM_ARCHIVAGE_DATE&");
      indexOrderPreferenceList.add("cot&cop&swa&cpr&ctr&SM_ARCHIVAGE_DATE&");
      // final SearchQuery searchQuery = new IndexPaginationSearchQuery(query, dfceServices.getBase());
      searchQuery.setIndexOrderPreference(indexOrderPreferenceList);
      searchQuery.setSearchLimit(200); // Pagination

      final Iterator<Document> iterateur = dfceServices.createDocumentIterator(searchQuery);
      int counter = 0;
      while (iterateur.hasNext()) {
         final Document doc = iterateur.next();
         final Date archivageDate = doc.getArchivageDate();
         final UUID uuid = doc.getUuid();
         counter++;
         if (counter % 200 == 0) {
            System.out.println(counter + " " + uuid + " " + archivageDate);
         }
         sysout.println(counter + " " + uuid + " " + archivageDate);
      }
      System.out.println("Compteur : " + counter);
   }

   @Test
   public void documentIterator_Test3() throws Exception {
      final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("hwi31intgntv6boappli1.gidn.recouv", "GNT-INT", 8080);

      // final String query = "(cot:\"true\") AND (cop:\"UR827\") AND (swa:\"PRET\") AND (cpr:\"PC77A\" OR cpr:\"PC66A\") AND SM_ARCHIVAGE_DATE:[20190618 TO 201906181224]";
      final String query = "(cot:\"true\") AND (cop:\"UR827\") AND (swa:\"PRET\") AND (cpr:\"PC66A\" ) AND SM_ARCHIVAGE_DATE:[20190618 TO 201906181224]";
      final SearchQuery searchQuery = ToolkitFactory.getInstance().createMonobaseQuery(query, dfceServices.getBase());
      final List<String> indexOrderPreferenceList = new ArrayList<>();
      indexOrderPreferenceList.add("cot&cop&swa&SM_ARCHIVAGE_DATE&");
      searchQuery.setIndexOrderPreference(indexOrderPreferenceList);
      searchQuery.setSearchLimit(200);
      searchQuery.setSortCategoryName("SM_ARCHIVAGE_DATE");
      // final PrintStream sysout = new PrintStream("d:/temp/out.txt");

      Date lastDate = null;
      final Iterator<Document> iterateur = dfceServices.createDocumentIterator(searchQuery);
      int counter = 0;
      while (iterateur.hasNext()) {
         final Document doc = iterateur.next();
         final Date archivageDate = doc.getArchivageDate();
         final UUID uuid = doc.getUuid();
         counter++;
         /*
         if (counter % 200 == 0) {
            System.out.println(counter + " " + uuid + " " + archivageDate);
         }
         sysout.println(counter + " " + uuid + " " + archivageDate);
          */
         if (lastDate != null && lastDate.after(archivageDate)) {
            System.out.println("Erreur " + counter + " " + uuid + " " + lastDate + " - " + archivageDate);
         }
         lastDate = archivageDate;
      }
      System.out.println("Compteur : " + counter);
   }

   @Test
   public void documentIterator_Test4() throws Exception {
      final DFCEServices dfceServices = DFCEServicesHelper.getDfceServices("hwi31intgntv6boappli1.gidn.recouv", "GNT-INT", 8080);
      final PrintStream sysout = new PrintStream("d:/temp/out.txt");

      final String query = "(cot:\"true\") AND (cop:\"UR827\") AND (swa:\"PRET\") AND (cpr:\"PC77A\" OR cpr:\"PC66A\") AND SM_ARCHIVAGE_DATE:[20190618 TO 201906181224]";
      final IndexPaginationSearchQuery searchQuery = new IndexPaginationSearchQuery(query, dfceServices.getBase());
      final List<String> indexOrderPreferenceList = new ArrayList<>();
      indexOrderPreferenceList.add("cot&cop&swa&SM_ARCHIVAGE_DATE&");
      searchQuery.setIndexOrderPreference(indexOrderPreferenceList);
      searchQuery.setSearchLimit(200);

      final SearchResult searchResult = dfceServices.search(searchQuery);

      final List<String> queries = searchResult.getPerformedQueries();
      for (final String q : queries) {
         System.out.println(q);
         sysout.println(q);
      }
   }


}
