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

import fr.cirtil.www.saeservice.MetadonneeType;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheResponseType;
import fr.cirtil.www.saeservice.ResultatRechercheType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.ExcessiveImports" })
public class RechercheTest {

   @Autowired
   private SaeServiceSkeleton skeleton;
   @Autowired
   private SAEDocumentService documentService;

   private static final String NB_MD_INATTENDU = "nombre de metadatas inattendu";
   private static final String MD_ATTENDU = "Des métadonnées sont attendues";
   private static final String DATECREATION = "dateCreation";
   private static final String DATE = "01012011";

   private Recherche createSearchType(String filePath) {
      try {
         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return Recherche.Factory.parse(reader);
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
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {

      UntypedDocument document1 = new UntypedDocument();
      UntypedDocument document2 = new UntypedDocument();

      List<UntypedDocument> listUntyp = new ArrayList<UntypedDocument>();
      List<UntypedMetadata> untypedMetadatas1 = new ArrayList<UntypedMetadata>();
      List<UntypedMetadata> untypedMetadatas2 = new ArrayList<UntypedMetadata>();

      UntypedMetadata metadata1 = new UntypedMetadata();
      metadata1.setLongCode("CodeActivite");
      metadata1.setValue("2");
      untypedMetadatas1.add(metadata1);

      UntypedMetadata metadata2 = new UntypedMetadata();
      metadata2.setLongCode("ContratDeService");
      metadata2.setValue("CS1");
      untypedMetadatas2.add(metadata2);

      document1.setUMetadatas(untypedMetadatas1);
      UUID uuidDoc = UUID.fromString("cc4a5ec1-788d-4b41-baa8-d349947865bf");
      document1.setUuid(uuidDoc);

      document2.setUuid(uuidDoc);
      document2.setUMetadatas(untypedMetadatas2);

      listUntyp.add(document1);
      listUntyp.add(document2);

      String requete = "Titre:NOTIFICATIONS";
      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add("CodeActivite");
      listMetaDesired.add("ContratDeService");
      listMetaDesired.add("Titre");

      // valeur attendu est listUntyp via andReturn
      EasyMock.expect(documentService.search(requete, listMetaDesired))
            .andReturn(listUntyp);
      // permet de sauvegarder l'enregistrement
      EasyMock.replay(documentService);

      Recherche request = createSearchType("src/test/resources/recherche/recherche_success_01.xml");

      // recuper le type de reponse de la recherche
      RechercheResponseType response = skeleton.rechercheSecure(request)
            .getRechercheResponse();

      ResultatRechercheType[] resultats = response.getResultats().getResultat();

      Assert.assertEquals(NB_MD_INATTENDU, 2, resultats.length);

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("CodeActivite", "2");
      expectedMetadatas.put("ContratDeService", "CS1");

      assertMetadata(resultats[0].getMetadonnees().getMetadonnee()[0],
            expectedMetadatas);
      assertMetadata(resultats[1].getMetadonnees().getMetadonnee()[0],
            expectedMetadatas);

      Assert.assertTrue(MD_ATTENDU, expectedMetadatas.isEmpty());

   }

   @Test
   public void searchSuccessAndOr() throws IOException, SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {

      UntypedDocument document1 = new UntypedDocument();

      List<UntypedDocument> listUntyp = new ArrayList<UntypedDocument>();
      List<UntypedMetadata> untypedMetadatas1 = new ArrayList<UntypedMetadata>();

      UntypedMetadata metadata1 = new UntypedMetadata();
      metadata1.setLongCode(DATECREATION);
      metadata1.setValue(DATE);
      untypedMetadatas1.add(metadata1);

      document1.setUMetadatas(untypedMetadatas1);
      UUID uuidDoc = UUID.fromString("21-3-1-131-121");
      document1.setUuid(uuidDoc);

      listUntyp.add(document1);

      String requete = "_uuid:21-3-1-131-121 and dateCreation:01012011 or itm :99999";
      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add(DATECREATION);

      // valeur attendu est listUntyp via andReturn
      EasyMock.expect(documentService.search(requete, listMetaDesired))
            .andReturn(listUntyp);
      // permet de sauvegarder l'enregistrement
      EasyMock.replay(documentService);

      Recherche request = createSearchType("src/test/resources/recherche/recherche_success_02.xml");

      // recuperer le type de reponse de la recherche
      RechercheResponseType response = skeleton.rechercheSecure(request)
            .getRechercheResponse();

      ResultatRechercheType[] resultats = response.getResultats().getResultat();

      Assert.assertEquals(NB_MD_INATTENDU, 1, resultats.length);

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put(DATECREATION, DATE);

      assertMetadata(resultats[0].getMetadonnees().getMetadonnee()[0],
            expectedMetadatas);

      Assert.assertTrue(MD_ATTENDU, expectedMetadatas.isEmpty());

   }

   @Test
   public void searchSuccessDate() throws IOException, SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {

      UntypedDocument document1 = new UntypedDocument();

      List<UntypedDocument> listUntyp = new ArrayList<UntypedDocument>();
      List<UntypedMetadata> untypedMetadatas1 = new ArrayList<UntypedMetadata>();

      UntypedMetadata metadata1 = new UntypedMetadata();
      metadata1.setLongCode(DATECREATION);
      metadata1.setValue(DATE);
      untypedMetadatas1.add(metadata1);

      document1.setUMetadatas(untypedMetadatas1);
      UUID uuidDoc = UUID.fromString("21-3-1-131-121");
      document1.setUuid(uuidDoc);

      listUntyp.add(document1);

      String requete = "_uuid:2131131121 dateCreation:[01012011 TO 01122011]";
      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add(DATECREATION);

      // valeur attendu est listUntyp via andReturn
      EasyMock.expect(documentService.search(requete, listMetaDesired))
            .andReturn(listUntyp);
      // permet de sauvegarder l'enregistrement
      EasyMock.replay(documentService);

      Recherche request = createSearchType("src/test/resources/recherche/recherche_success_03.xml");

      // recuperer le type de reponse de la recherche
      RechercheResponseType response = skeleton.rechercheSecure(request)
            .getRechercheResponse();

      ResultatRechercheType[] resultats = response.getResultats().getResultat();

      Assert.assertEquals(NB_MD_INATTENDU, 1, resultats.length);

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put(DATECREATION, DATE);

      assertMetadata(resultats[0].getMetadonnees().getMetadonnee()[0],
            expectedMetadatas);

      Assert.assertTrue(MD_ATTENDU, expectedMetadatas.isEmpty());

   }

   /**
    * Test avec liste meta donnees desiree vide. Logiquement cette liste est
    * remplie par defaut par les metadonnees consultables.
    */
   @Test
   public void searchSuccesMetadataVide() throws IOException,
         SAESearchServiceEx, MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx {

      UntypedDocument document1 = new UntypedDocument();

      List<UntypedDocument> listUntyp = new ArrayList<UntypedDocument>();
      List<UntypedMetadata> untypedMetadatas1 = new ArrayList<UntypedMetadata>();

      UntypedMetadata metadata1 = new UntypedMetadata();
      metadata1.setLongCode(DATECREATION);
      metadata1.setValue(DATE);
      untypedMetadatas1.add(metadata1);

      document1.setUMetadatas(untypedMetadatas1);
      UUID uuidDoc = UUID.fromString("21-3-1-131-121");
      document1.setUuid(uuidDoc);

      listUntyp.add(document1);

      String requete = "_uuid:2131131121 dateCreation:[01012011 TO 01122011]";
      List<String> listMetaDesired = new ArrayList<String>();

      // valeur attendu est listUntyp via andReturn
      EasyMock.expect(documentService.search(requete, listMetaDesired))
            .andReturn(listUntyp);
      // permet de sauvegarder l'enregistrement
      EasyMock.replay(documentService);

      Recherche request = createSearchType("src/test/resources/recherche/recherche_success_04.xml");

      // recuperer le type de reponse de la recherche
      RechercheResponseType response = skeleton.rechercheSecure(request)
            .getRechercheResponse();

      ResultatRechercheType[] resultats = response.getResultats().getResultat();

      Assert.assertEquals(NB_MD_INATTENDU, 1, resultats.length);

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put(DATECREATION, DATE);

      assertMetadata(resultats[0].getMetadonnees().getMetadonnee()[0],
            expectedMetadatas);

      Assert.assertTrue(MD_ATTENDU, expectedMetadatas.isEmpty());

   }
   
   
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
