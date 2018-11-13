package fr.urssaf.image.sae.webservices.skeleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.IdentifiantPageType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.RechercheParIterateur;
import fr.cirtil.www.saeservice.RechercheParIterateurResponseType;
import fr.cirtil.www.saeservice.ResultatRechercheType;
import fr.urssaf.image.sae.bo.model.AbstractMetadata;
import fr.urssaf.image.sae.bo.model.untyped.PaginatedUntypedDocuments;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.DoublonFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.ExcessiveImports" })
public class RechercheIterateurTest {

   @Autowired
   private SaeServiceSkeleton skeleton;
   @Autowired
   private SAEDocumentService documentService;

   private static final String NB_MD_INATTENDU = "nombre de metadatas inattendu";
   private static final String MD_ATTENDU = "Des métadonnées sont attendues";
   private static final String DATECREATION = "dateCreation";
   private static final String DATE = "01012011";

   private RechercheParIterateur createSearchIterateurType(String filePath) {
      try {
         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return RechercheParIterateur.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }
   }

   // insertion de metadonnée
   private static void assertMetadata(MetadonneeType metadata,
         Map<String, Object> expectedMetadatas) {
      Assert.assertTrue("la metadonnée '"
            + metadata.getCode().getMetadonneeCodeType() + "' "
            + "est inattendue", expectedMetadatas.containsKey(metadata
            .getCode().getMetadonneeCodeType()));

      expectedMetadatas.remove(metadata.getCode().getMetadonneeCodeType());
   }

   // Toujours present lorsqu'on travaille avec des Mocks.
   @After
   public void after() {
      EasyMock.reset(documentService);
   }

   @Test
   public void searchSuccess() throws IOException, SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {

      PaginatedUntypedDocuments documents = new PaginatedUntypedDocuments();
      UntypedDocument document1 = new UntypedDocument();
      UntypedDocument document2 = new UntypedDocument();

      List<UntypedMetadata> untypedMetadatas1 = new ArrayList<UntypedMetadata>();
      List<UntypedMetadata> untypedMetadatas2 = new ArrayList<UntypedMetadata>();

      UntypedMetadata metadata1 = new UntypedMetadata();
      metadata1.setLongCode("CodeActivite");
      metadata1.setValue("2");
      untypedMetadatas1.add(metadata1);

      UntypedMetadata metadata2 = new UntypedMetadata();
      metadata2.setLongCode("DateCreation");
      metadata2.setValue("20141231");
      untypedMetadatas2.add(metadata2);

      document1.setUMetadatas(untypedMetadatas1);
      UUID uuidDoc = UUID.fromString("cc4a5ec1-788d-4b41-baa8-d349947865bf");
      document1.setUuid(uuidDoc);

      document2.setUuid(uuidDoc);
      document2.setUMetadatas(untypedMetadatas2);

      List<UntypedDocument> listeDoc = new ArrayList<UntypedDocument>();
      listeDoc.add(document1);
      listeDoc.add(document2);
      documents.setDocuments(listeDoc);
      documents.setLastPage(true);
      documents.setValeurMetaLastPage("20141231");

      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add("CodeActivite");
      listMetaDesired.add("ContratDeService");
      listMetaDesired.add("DateCreation");

      // int nbDocumentsParPage = 10;
      // UUID lastIdDoc = null;
      //      
      // UntypedMetadata uMeta = new UntypedMetadata("Siret", "123");
      // List<UntypedMetadata> fixedMetadatas = new
      // ArrayList<UntypedMetadata>();
      // fixedMetadatas.add(uMeta);
      //      
      // UntypedRangeMetadata varyingMetadata = new
      // UntypedRangeMetadata("DateCreation", "20141201", "20141231");
      //      
      // List<AbstractMetadata> filters = null;

      // valeur attendu est documents via andReturn
      /*
       * EasyMock.expect(documentService.searchPaginated(fixedMetadatas,
       * varyingMetadata, filters, nbDocumentsParPage, lastIdDoc,
       * listMetaDesired)) .andReturn(documents);
       */
      EasyMock.expect(
            documentService.searchPaginated((List<UntypedMetadata>) EasyMock
                  .anyObject(), (UntypedRangeMetadata) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(),
                  (List<AbstractMetadata>) EasyMock.anyObject(), EasyMock
                        .anyInt(), (UUID) EasyMock.anyObject(),
                  (List<String>) EasyMock.anyObject())).andReturn(documents);
      // permet de sauvegarder l'enregistrement
      EasyMock.replay(documentService);

      RechercheParIterateur request = createSearchIterateurType("src/test/resources/recherche/rechercheIterateur_success_01.xml");

      // recuper le type de reponse de la recherche
      RechercheParIterateurResponseType response = skeleton
            .rechercheParIterateurSecure(request)
            .getRechercheParIterateurResponse();

      ResultatRechercheType[] resultats = response.getResultats().getResultat();

      boolean dernierePage = response.getDernierePage();
      Assert.assertEquals("le boolean dernière page doit être à true", true,
            dernierePage);

      IdentifiantPageType idPage = response.getIdentifiantPageSuivante();
      Assert.assertEquals("L'UUID du document est incorrect",
            "cc4a5ec1-788d-4b41-baa8-d349947865bf", idPage.getIdArchive()
                  .toString());
      Assert.assertEquals(
            "La valeur de l'identifiant de la page est incorrecte", "20141231",
            idPage.getValeur().getMetadonneeValeurType());

      Assert.assertEquals(NB_MD_INATTENDU, 2, resultats.length);

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("CodeActivite", "2");
      expectedMetadatas.put("DateCreation", "20141231");

      assertMetadata(resultats[0].getMetadonnees().getMetadonnee()[0],
            expectedMetadatas);
      assertMetadata(resultats[1].getMetadonnees().getMetadonnee()[0],
            expectedMetadatas);

      Assert.assertTrue(MD_ATTENDU, expectedMetadatas.isEmpty());

   }

   // @Test
   // public void searchSuccessAndOr() throws IOException, SAESearchServiceEx,
   // MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
   // UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
   //
   // UntypedDocument document1 = new UntypedDocument();
   //
   // List<UntypedDocument> listUntyp = new ArrayList<UntypedDocument>();
   // List<UntypedMetadata> untypedMetadatas1 = new
   // ArrayList<UntypedMetadata>();
   //
   // UntypedMetadata metadata1 = new UntypedMetadata();
   // metadata1.setLongCode(DATECREATION);
   // metadata1.setValue(DATE);
   // untypedMetadatas1.add(metadata1);
   //
   // document1.setUMetadatas(untypedMetadatas1);
   // UUID uuidDoc = UUID.fromString("21-3-1-131-121");
   // document1.setUuid(uuidDoc);
   //
   // listUntyp.add(document1);
   //
   // String requete =
   // "_uuid:21-3-1-131-121 and dateCreation:01012011 or itm :99999";
   // List<String> listMetaDesired = new ArrayList<String>();
   // listMetaDesired.add(DATECREATION);
   //
   // // valeur attendu est listUntyp via andReturn
   // EasyMock.expect(documentService.search(requete, listMetaDesired))
   // .andReturn(listUntyp);
   // // permet de sauvegarder l'enregistrement
   // EasyMock.replay(documentService);
   //
   // Recherche request =
   // createSearchType("src/test/resources/recherche/recherche_success_02.xml");
   //
   // // recuperer le type de reponse de la recherche
   // RechercheResponseType response = skeleton.rechercheSecure(request)
   // .getRechercheResponse();
   //
   // ResultatRechercheType[] resultats = response.getResultats().getResultat();
   //
   // Assert.assertEquals(NB_MD_INATTENDU, 1, resultats.length);
   //
   // Map<String, Object> expectedMetadatas = new HashMap<String, Object>();
   //
   // expectedMetadatas.put(DATECREATION, DATE);
   //
   // assertMetadata(resultats[0].getMetadonnees().getMetadonnee()[0],
   // expectedMetadatas);
   //
   // Assert.assertTrue(MD_ATTENDU, expectedMetadatas.isEmpty());
   //
   // }
   //
   // @Test
   // public void searchSuccessDate() throws IOException, SAESearchServiceEx,
   // MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
   // UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
   //
   // UntypedDocument document1 = new UntypedDocument();
   //
   // List<UntypedDocument> listUntyp = new ArrayList<UntypedDocument>();
   // List<UntypedMetadata> untypedMetadatas1 = new
   // ArrayList<UntypedMetadata>();
   //
   // UntypedMetadata metadata1 = new UntypedMetadata();
   // metadata1.setLongCode(DATECREATION);
   // metadata1.setValue(DATE);
   // untypedMetadatas1.add(metadata1);
   //
   // document1.setUMetadatas(untypedMetadatas1);
   // UUID uuidDoc = UUID.fromString("21-3-1-131-121");
   // document1.setUuid(uuidDoc);
   //
   // listUntyp.add(document1);
   //
   // String requete = "_uuid:2131131121 dateCreation:[01012011 TO 01122011]";
   // List<String> listMetaDesired = new ArrayList<String>();
   // listMetaDesired.add(DATECREATION);
   //
   // // valeur attendu est listUntyp via andReturn
   // EasyMock.expect(documentService.search(requete, listMetaDesired))
   // .andReturn(listUntyp);
   // // permet de sauvegarder l'enregistrement
   // EasyMock.replay(documentService);
   //
   // Recherche request =
   // createSearchType("src/test/resources/recherche/recherche_success_03.xml");
   //
   // // recuperer le type de reponse de la recherche
   // RechercheResponseType response = skeleton.rechercheSecure(request)
   // .getRechercheResponse();
   //
   // ResultatRechercheType[] resultats = response.getResultats().getResultat();
   //
   // Assert.assertEquals(NB_MD_INATTENDU, 1, resultats.length);
   //
   // Map<String, Object> expectedMetadatas = new HashMap<String, Object>();
   //
   // expectedMetadatas.put(DATECREATION, DATE);
   //
   // assertMetadata(resultats[0].getMetadonnees().getMetadonnee()[0],
   // expectedMetadatas);
   //
   // Assert.assertTrue(MD_ATTENDU, expectedMetadatas.isEmpty());
   //
   // }
   //
   // /**
   // * Test avec liste meta donnees desiree vide. Logiquement cette liste est
   // * remplie par defaut par les metadonnees consultables.
   // */
   // @Test
   // public void searchSuccesMetadataVide() throws IOException,
   // SAESearchServiceEx, MetaDataUnauthorizedToSearchEx,
   // MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
   // UnknownLuceneMetadataEx, SyntaxLuceneEx {
   //
   // UntypedDocument document1 = new UntypedDocument();
   //
   // List<UntypedDocument> listUntyp = new ArrayList<UntypedDocument>();
   // List<UntypedMetadata> untypedMetadatas1 = new
   // ArrayList<UntypedMetadata>();
   //
   // UntypedMetadata metadata1 = new UntypedMetadata();
   // metadata1.setLongCode(DATECREATION);
   // metadata1.setValue(DATE);
   // untypedMetadatas1.add(metadata1);
   //
   // document1.setUMetadatas(untypedMetadatas1);
   // UUID uuidDoc = UUID.fromString("21-3-1-131-121");
   // document1.setUuid(uuidDoc);
   //
   // listUntyp.add(document1);
   //
   // String requete = "_uuid:2131131121 dateCreation:[01012011 TO 01122011]";
   // List<String> listMetaDesired = new ArrayList<String>();
   //
   // // valeur attendu est listUntyp via andReturn
   // EasyMock.expect(documentService.search(requete, listMetaDesired))
   // .andReturn(listUntyp);
   // // permet de sauvegarder l'enregistrement
   // EasyMock.replay(documentService);
   //
   // Recherche request =
   // createSearchType("src/test/resources/recherche/recherche_success_04.xml");
   //
   // // recuperer le type de reponse de la recherche
   // RechercheResponseType response = skeleton.rechercheSecure(request)
   // .getRechercheResponse();
   //
   // ResultatRechercheType[] resultats = response.getResultats().getResultat();
   //
   // Assert.assertEquals(NB_MD_INATTENDU, 1, resultats.length);
   //
   // Map<String, Object> expectedMetadatas = new HashMap<String, Object>();
   //
   // expectedMetadatas.put(DATECREATION, DATE);
   //
   // assertMetadata(resultats[0].getMetadonnees().getMetadonnee()[0],
   // expectedMetadatas);
   //
   // Assert.assertTrue(MD_ATTENDU, expectedMetadatas.isEmpty());
   //
   // }

   @Test
   public void testMDDesiredListNull_success() {

   }

   /**
    * @return : saeServiceSkeleton
    */
   public final SaeServiceSkeleton getSaeServiceSkeleton() {
      return skeleton;
   }

   /**
    * @param saeServiceSkeleton
    *           : saeServiceSkeleton
    */
   public final void setSaeServiceSkeleton(SaeServiceSkeleton saeSkeleton) {
      this.skeleton = saeSkeleton;
   }

}
