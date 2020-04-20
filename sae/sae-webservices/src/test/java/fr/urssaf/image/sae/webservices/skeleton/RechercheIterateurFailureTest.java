package fr.urssaf.image.sae.webservices.skeleton;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.RechercheParIterateur;
import fr.urssaf.image.sae.metadata.exceptions.IndexCompositeException;
import fr.urssaf.image.sae.metadata.referential.services.IndexCompositeService;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.DoublonFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.webservices.security.exception.SaeAccessDeniedAxisFault;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-service-test.xml"})
@SuppressWarnings({"PMD.MethodNamingConventions", "PMD.PreserveStackTrace",
                   "PMD.AvoidPrintStackTrace"})
public class RechercheIterateurFailureTest {
   @Autowired
   private SaeServiceSkeleton skeleton;

   @Autowired
   private SAEDocumentService documentService;

   @Autowired
   private IndexCompositeService indexCompositeService;

   private static final String VIDE = "";

   private static final String TITRE = "Titre";

   private static final String SAE = "sae";

   @After
   public void after() {
      EasyMock.reset(documentService, indexCompositeService);
   }

   private RechercheParIterateur createSearchIterateurType(final String filePath) {
      try {
         final XMLStreamReader reader = XMLStreamUtils
                                                      .createXMLStreamReader(filePath);
         return RechercheParIterateur.Factory.parse(reader);

      }
      catch (final Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   private static final String AXIS_FAULT = "AxisFault non attendue";

   private static void assertAxisFault(final AxisFault axisFault, final String expectedMsg,
                                       final String expectedType, final String expectedPrefix) {

      Assert.assertEquals(AXIS_FAULT, expectedMsg, axisFault.getMessage());
      Assert.assertEquals(AXIS_FAULT,
                          expectedType,
                          axisFault.getFaultCode()
                                   .getLocalPart());
      Assert.assertEquals(AXIS_FAULT,
                          expectedPrefix,
                          axisFault.getFaultCode()
                                   .getPrefix());
   }

   /**
    * Test qui echoue car la liste des metadonnées est non recherchable
    * 
    * @throws SAESearchServiceEx
    * @throws SyntaxLuceneEx
    * @throws UnknownLuceneMetadataEx
    * @throws UnknownDesiredMetadataEx
    * @throws MetaDataUnauthorizedToConsultEx
    * @throws MetaDataUnauthorizedToSearchEx
    * @throws SaeAccessDeniedAxisFault
    * @throws UnknownFiltresMetadataEx
    * @throws DoublonFiltresMetadataEx
    * @throws IndexCompositeException
    */
   @Test
   public void searchFailureSAESearchServiceEx() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx,
         SaeAccessDeniedAxisFault, UnknownFiltresMetadataEx, DoublonFiltresMetadataEx, IndexCompositeException {

      final List<String> listMetaDesired = new ArrayList<>();
      listMetaDesired.add("TailleFichier");

      try {
         // valeur attendu est MetaDataUnauthorizedToSearchEx via andThrow
         EasyMock
                 .expect(
                         documentService.searchPaginated(
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyInt(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyObject()))
                 .andThrow(
                           new MetaDataUnauthorizedToSearchEx(
                                                              "La ou les m\u00E9tadonn\u00E9es suivantes, utilis\u00E9es dans la requ\u00EAte de recherche, ne sont pas autoris\u00E9s comme crit\u00E8res de recherche : "
                                                                    + listMetaDesired + "."));

         EasyMock.expect(indexCompositeService.untypedMetadatasToShortCodeMetadatas(EasyMock.anyObject())).andReturn(new ArrayList<>()).anyTimes();

         EasyMock.expect(indexCompositeService.getBestIndexForQuery(EasyMock.anyObject())).andReturn(null).anyTimes();

         EasyMock.replay(documentService, indexCompositeService);

         final RechercheParIterateur request = createSearchIterateurType("src/test/resources/recherche/rechercheIterateur_failure_01.xml");

         skeleton.rechercheParIterateurSecure(request)
                 .getRechercheParIterateurResponse();

         Assert
               .fail("le test doit échouer à cause de la levée d'une exception de type "
                     + SAESearchServiceEx.class);

      }
      catch (final AxisFault fault) {
         assertAxisFault(
                         fault,
                         "La ou les m\u00E9tadonn\u00E9es suivantes, utilis\u00E9es dans la requ\u00EAte de recherche, ne sont pas autoris\u00E9s comme crit\u00E8res de recherche : "
                               + listMetaDesired + ".",
                         "RechercheMetadonneesInterdite",
                         SAE);
      }
   }

   @Test
   public void search_Failure_RuntimeException() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx,
         SaeAccessDeniedAxisFault, UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {

      try {

         EasyMock.expect(
                         documentService.searchPaginated(EasyMock.anyObject(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyInt(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyObject(),
                                                         EasyMock.anyObject()))
                 .andThrow(new RuntimeException("une runtime exception est levée"));

         EasyMock.replay(documentService);

         final RechercheParIterateur request = createSearchIterateurType("src/test/resources/recherche/rechercheIterateur_failure_01.xml");

         skeleton.rechercheParIterateurSecure(request)
                 .getRechercheParIterateurResponse();

         Assert
               .fail("le test doit échouer à cause de la levée d'une exception de type "
                     + SAESearchServiceEx.class);

      }
      catch (final AxisFault fault) {
         assertAxisFault(
                         fault,
                         "Une erreur interne à l'application est survenue lors de la recherche.",
                         "ErreurInterneRecherche",
                         SAE);
      }
   }

}
