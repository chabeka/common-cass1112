package fr.urssaf.image.sae.services.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.bo.SAELuceneCriteria;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.model.SAEMockMetadata;
import fr.urssaf.image.sae.services.QueriesReferenceDAO;
import fr.urssaf.image.sae.services.document.SAESearchService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
@SuppressWarnings( { "PMD", "unused" })
public class SAESearchServiceImplTest {

   @Autowired
   @Qualifier("saeSearchService")
   private SAESearchService saeSearchService;

   @Autowired
   @Qualifier("queriesReferenceDAO")
   private QueriesReferenceDAO queriesReferenceDAO;

   
   @After
   public void end() {
      AuthenticationContext.setAuthenticationToken(null);
   }
   
   private void initDroits() {
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "recherche" };
      saePrmds.add(saePrmd);

      saeDroits.put("recherche", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }
   
   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * 
    * Tests réalisés : <br>
    * <lu> <br>
    * <li>Test de récupération UUID.</li> <br>
    * <br>
    * <li>Test de récupération du contenu du document</li> <br>
    * <br>
    * <li>Test de récupération des métadonnées</li> <br>
    * <li>Vérification des métadonnées passées dans la requête Lucene ont les
    * mêmes valeur que les métadonnées récupérer du SAE</li> <br>
    * </lu>
    */
   @Test
   @Ignore
   public final void searchSuccess() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String typeTest = "success";
      String requete = null;
      requete = queriesReferenceDAO.getQueryByType(typeTest).getLuceneQuery();
      List<String> listMetaDesiree = new ArrayList<String>();
      listMetaDesiree = buildMockListMetadata(mockMetadata, typeTest);
      List<UntypedDocument> untypedDocuments = saeSearchService.search(requete,
            listMetaDesiree);
      Assert.assertTrue("La liste récupérer doit être non nulle requête :  "
            + requete, !untypedDocuments.isEmpty());
      checkDocumentsAndMetadatas(mockMetadata, untypedDocuments);
      Assert.assertFalse("Des UntypedDocuments sont attendues",
            untypedDocuments.isEmpty());
   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * 
    * test avec une requête non vide avec des métadonnées recherchable et des
    * opérateurs : AND OR Combinaisons des AND et OR [date1 to date2]
    */
   @Test
   @Ignore
   public final void searchSuccessOR() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String typeTest = "success_OR";
      String requete = queriesReferenceDAO.getQueryByType(typeTest)
            .getLuceneQuery();
      List<String> listMetaDesiree = buildMockListMetadata(mockMetadata,
            typeTest);
      List<UntypedDocument> untypedDocuments = saeSearchService.search(requete,
            listMetaDesiree);
      checkDocumentsAndMetadatas(mockMetadata, untypedDocuments);
      Assert.assertFalse("Des UntypedDocuments sont attendues",
            untypedDocuments.isEmpty());

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    */
   @Test
   @Ignore
   public final void searchSuccessDate() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      String typeTest = "success_date";
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String requete = queriesReferenceDAO.getQueryByType(typeTest)
            .getLuceneQuery();
      List<String> listMetaDesiree = buildMockListMetadata(mockMetadata,
            typeTest);
      List<UntypedDocument> untypedDocuments = saeSearchService.search(requete,
            listMetaDesiree);
      Assert.assertFalse("Des UntypedDocuments sont attendues : requête :"
            + requete, untypedDocuments.isEmpty());
   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * Test qui echoue car la liste des metadonnées code court voulu n'est pas
    * consultable
    */
   @Test
   @Ignore("test à reprendre : exception sur la requête lucene + incohérence entre les commentaire et l'implémentation")
   public final void searchFailureListNonConsult() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String typeTest = "listNonConsult";
      String requete = queriesReferenceDAO.getQueryByType(typeTest)
            .getLuceneQuery();
      List<String> listMetaDesiree = buildMockListMetadata(mockMetadata,
            typeTest);
      List<UntypedDocument> untypedDocuments = saeSearchService.search(requete,
            listMetaDesiree);
      checkDocumentsAndMetadatas(mockMetadata, untypedDocuments);
   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * Test qui échoue car la liste des métadonnées est nulle?
    */
   @Test(expected = IllegalArgumentException.class)
   @Ignore
   public final void searchFailureListEmpty() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      String requete = queriesReferenceDAO.getQueryByType("listEmpty")
            .getLuceneQuery();
      List<String> listMetaDesiree = null;
      List<UntypedDocument> untypedDocuments = saeSearchService.search(requete,
            listMetaDesiree);
   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * Test avec une requete vide
    */
   @Test(expected = IllegalArgumentException.class)
   public final void searchFailureReqEmpty() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      
      initDroits();
      
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String typeTest = "failure_reqEmpty";
      String requete = queriesReferenceDAO.getQueryByType(typeTest)
            .getLuceneQuery();
      List<String> listMetaDesiree = buildMockListMetadata(mockMetadata,
            typeTest);
      List<UntypedDocument> untypedDocuments = saeSearchService.search(requete,
            listMetaDesiree);
      checkDocumentsAndMetadatas(mockMetadata, untypedDocuments);

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * Test avec une requete non vide avec une metadonnée non autorisée à la
    * recherche
    */
   @Test(expected = MetaDataUnauthorizedToSearchEx.class)
   @Ignore
   public final void searchFailureReqNonConsult() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String typeTest = "failure_reqNonConsult";
      String requete = queriesReferenceDAO.getQueryByType(typeTest)
            .getLuceneQuery();
      List<String> listMetaDesiree = buildMockListMetadata(mockMetadata,
            typeTest);
      List<UntypedDocument> untypedDocument = saeSearchService.search(requete,
            listMetaDesiree);

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * Test avec une requête non vide avec des metadonnées recherchable et des
    * séparateurs différents de : ": >"
    */
   @Test(expected = SyntaxLuceneEx.class)
   public final void searchFailureSeparateur() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      
      initDroits();
      
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String typeTest = "failure_separateur";
      String requete = queriesReferenceDAO.getQueryByType(typeTest)
            .getLuceneQuery();
      List<String> listMetaDesiree = buildMockListMetadata(mockMetadata,
            typeTest);
      List<UntypedDocument> untypedDocument = saeSearchService.search(requete,
            listMetaDesiree);

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * Test avec une requête non vide avec des metadonnées relation code long
    * code court non existante
    */
   @Test(expected = UnknownLuceneMetadataEx.class)
   @Ignore
   public final void searchFailureNoExists() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String typeTest = "failure_noExists";
      String requete = queriesReferenceDAO.getQueryByType(typeTest)
            .getLuceneQuery();
      List<String> listMetaDesiree = buildMockListMetadata(mockMetadata,
            typeTest);
      List<UntypedDocument> untypedDocument = saeSearchService.search(requete,
            listMetaDesiree);

   }

   /**
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * Test avec une requête non vide avec des metadonnées recherchable et avec
    * le contenu du Term contenant des ":"
    */
   @Test(expected = SyntaxLuceneEx.class)
   @Ignore
   public final void searchFailureSeparateur2Pts() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String typeTest = "failure_separateur2Pts";
      String requete = queriesReferenceDAO.getQueryByType(typeTest)
            .getLuceneQuery();
      List<String> listMetaDesiree = buildMockListMetadata(mockMetadata,
            typeTest);
      List<UntypedDocument> untypedDocument = saeSearchService.search(requete,
            listMetaDesiree);

   }

   /***
    * Test du service :
    * {@link fr.urssaf.image.sae.services.document.SAESearchService#search(SAELuceneCriteria)
    * Test avec une requête non vide avec des metadonnées recherchable et avec
    * le contenu du Term contenant des ":"
    */
   @Test(expected = UnknownDesiredMetadataEx.class)
   public final void searchFailureItem() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      
      initDroits();
      
      Map<String, String> mockMetadata = new HashMap<String, String>();
      String typeTest = "failure_item";
      String requete = queriesReferenceDAO.getQueryByType(typeTest)
            .getLuceneQuery();
      List<String> listMetaDesiree = buildMockListMetadata(mockMetadata,
            typeTest);
      List<UntypedDocument> untypedDocument = saeSearchService.search(requete,
            listMetaDesiree);

   }

   /**
    * Construit une liste de mockTest.
    * 
    * @param mockMetadata
    *           : Tableau de code long/valeur
    * @param typeTest
    *           : type du test
    * @return List métadonnée liste des métadonnées souhaitées
    * @throws SAESearchServiceEx
    *            Exception est levée lors d'un problème de chargement des
    *            requêtes de tests.
    */
   private List<String> buildMockListMetadata(Map<String, String> mockMetadata,
         String typeTest) throws SAESearchServiceEx {
      List<SAEMockMetadata> listMockMetaDesiree = queriesReferenceDAO
            .getQueryByType(typeTest).getDesiredMetadatas();
      List<String> listMetaDesiree = new ArrayList<String>();
      for (SAEMockMetadata saeMockMetadata : Utils
            .nullSafeIterable(listMockMetaDesiree)) {
         listMetaDesiree.add(saeMockMetadata.getCode());
         mockMetadata
               .put(saeMockMetadata.getCode(), saeMockMetadata.getValue());
      }
      return listMetaDesiree;
   }

   /**
    * Vérifie le jeu de données avec les données récupérer de DFCE
    * 
    * @param mockMetadata
    *           {@link mockMetadata}
    * @param untypedDocuments
    *           {@link untypedDocuments}
    */
   private void checkDocumentsAndMetadatas(Map<String, String> mockMetadata,
         List<UntypedDocument> untypedDocuments) {
      for (UntypedDocument untypedDocument : Utils
            .nullSafeIterable(untypedDocuments)) {
         Assert.assertTrue("UUID doit être non null ", StringUtils
               .isNotEmpty(untypedDocument.getUuid().toString()));
         Assert.assertTrue("UUID le contenu doit être non null ", StringUtils
               .isNotEmpty(untypedDocument.getContent().toString()));
         Assert.assertTrue("Liste des métadonnées doit être non null ",
               !untypedDocument.getUMetadatas().isEmpty());
         for (UntypedMetadata untypedMetadata : untypedDocument.getUMetadatas()) {
            Assert.assertTrue("Code long doit être non null ", StringUtils
                  .isNotEmpty(untypedMetadata.getLongCode()));
            Assert.assertTrue("La valeur du code long doit être non null ",
                  StringUtils.isNotEmpty(untypedMetadata.getValue()));
            Assert
                  .assertTrue(
                        "La valeur de la métadonnée passée dans la requête Lucene doit être le même récupérer du SAE ",
                        untypedMetadata.getValue().equals(
                              mockMetadata.get(untypedMetadata.getLongCode())));
         }
      }
   }
}
