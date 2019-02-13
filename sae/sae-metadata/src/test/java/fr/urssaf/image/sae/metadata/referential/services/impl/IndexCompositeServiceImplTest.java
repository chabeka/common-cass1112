package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;
import fr.urssaf.image.sae.storage.services.storagedocument.StorageDocumentService;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.model.reference.CompositeIndex;

/**
 * Contient les tests sur les services liés aux index composites
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-metadata-test.xml"})
public class IndexCompositeServiceImplTest {

   @Autowired
   private IndexCompositeServiceImpl indexCompositeService;

   @Autowired // C'est un mock défini dans la configuration spring
   private StorageServiceProvider storageServiceProvider;

   @Before
   public void setUp() {
      configureCompositeIndexProvider();
   }

   @Test
   public void getAllComputedIndexCompositeTest() throws Exception {
      final List<SaeIndexComposite> indexList = indexCompositeService.getAllComputedIndexComposite();
      Assert.assertEquals("Nombre d'index composité computed trouvés", 4, indexList.size());
   }

   @Test
   public void checkIndexCompositeValidKOTest() throws Exception {
      final SaeIndexComposite indexComposite = new SaeIndexComposite(createCompositeIndex(Arrays.asList("cot", "cop", "mch"), true));
      final Collection<String> listShortCodeMetadatas = Arrays.asList("cot", "mch");
      final boolean isValid = indexCompositeService.checkIndexCompositeValid(indexComposite, listShortCodeMetadatas);
      Assert.assertEquals(false, isValid);
   }

   @Test
   public void checkIndexCompositeValidOKTest() throws Exception {
      final SaeIndexComposite indexComposite = new SaeIndexComposite(createCompositeIndex(Arrays.asList("cot", "cop", "mch"), true));
      final Collection<String> listShortCodeMetadatas = Arrays.asList("zob", "cot", "mch", "zib", "cot");
      final boolean isValid = indexCompositeService.checkIndexCompositeValid(indexComposite, listShortCodeMetadatas);
      Assert.assertEquals(false, isValid);
   }

   @Test
   public void getBestIndexCompositeTest() throws Exception {
      final SaeIndexComposite indexComposite1 = new SaeIndexComposite(createCompositeIndex(Arrays.asList("cot", "cop", "mch"), true));
      final SaeIndexComposite indexComposite2 = new SaeIndexComposite(createCompositeIndex(Arrays.asList("cot", "cop"), true));
      final SaeIndexComposite indexComposite3 = new SaeIndexComposite(createCompositeIndex(Arrays.asList("cop"), true));
      final SaeIndexComposite best = indexCompositeService.getBestIndexComposite(Arrays.asList(indexComposite1, indexComposite2, indexComposite3));
      Assert.assertEquals(indexComposite1, best);
   }

   @Test
   public void getBestIndexForQueryTest1() throws Exception {
      final List<String> metaInQuery = Arrays.asList("cot", "cop", "mch", "cpt", "sco", "SM_DOCUMENT_TYPE", "nti");
      final String best = indexCompositeService.getBestIndexForQuery(metaInQuery);
      Assert.assertEquals("cpt&sco&SM_DOCUMENT_TYPE&nti&", best);
   }

   @Test
   public void getBestIndexForQueryTest2() throws Exception {
      final List<String> metaInQuery = Arrays.asList("cot", "cop");
      final String best = indexCompositeService.getBestIndexForQuery(metaInQuery);
      // Il n'y a aucun index composite ou simple qui peut répondre à cette requête
      Assert.assertNull(best);
   }

   @Test
   public void getBestIndexForQueryTest3() throws Exception {
      final List<String> metaInQuery = Arrays.asList("srt", "cop");
      final String best = indexCompositeService.getBestIndexForQuery(metaInQuery);
      // C'est un index simple qui peut répondre à la requête
      Assert.assertEquals("srt", best);
   }

   /**
    * Permet de créer et configurer un mock qui renvoie une liste d'index composites
    */
   private void configureCompositeIndexProvider() {
      final Set<CompositeIndex> compositeIndexList = new HashSet<>();
      compositeIndexList.add(createCompositeIndex(Arrays.asList("cot", "cop", "mch"), true));
      compositeIndexList.add(createCompositeIndex(Arrays.asList("cpt", "sco", "SM_DOCUMENT_TYPE", "nti"), true));
      compositeIndexList.add(createCompositeIndex(Arrays.asList("cpt", "sco", "SM_DOCUMENT_TYPE", "nds"), true));
      compositeIndexList.add(createCompositeIndex(Arrays.asList("drs", "apr", "atr", "ame", "SM_ARCHIVAGE_DATE"), true));
      // Index non "computed"
      compositeIndexList.add(createCompositeIndex(Arrays.asList("cpt", "sco", "SM_DOCUMENT_TYPE", "nco"), false));

      final StorageDocumentService storageDocumentService = EasyMock.createMock(StorageDocumentService.class);
      EasyMock.expect(storageDocumentService.getAllIndexComposite()).andReturn(compositeIndexList).anyTimes();
      EasyMock.replay(storageDocumentService);
      EasyMock.reset(storageServiceProvider);
      EasyMock.expect(storageServiceProvider.getStorageDocumentService()).andReturn(storageDocumentService).anyTimes();
      EasyMock.replay(storageServiceProvider);
   }

   private CompositeIndex createCompositeIndex(final List<String> categories, final boolean computed) {
      final CompositeIndex index = new CompositeIndex();
      index.setComputed(computed);
      final List<Category> catList = new ArrayList<>();
      for (final String categoryName : categories) {
         final Category cat = new Category();
         cat.setName(categoryName);
         catList.add(cat);
      }
      index.setCategories(catList);
      return index;
   }
}
