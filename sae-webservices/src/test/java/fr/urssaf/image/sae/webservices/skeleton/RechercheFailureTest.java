package fr.urssaf.image.sae.webservices.skeleton;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.cirtil.www.saeservice.Recherche;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;
import fr.urssaf.image.sae.webservices.security.exception.SaeAccessDeniedAxisFault;
import fr.urssaf.image.sae.webservices.util.XMLStreamUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-service-test.xml" })
@SuppressWarnings( { "PMD.MethodNamingConventions", "PMD.PreserveStackTrace",
      "PMD.AvoidPrintStackTrace" })
public class RechercheFailureTest {
   @Autowired
   private SaeServiceSkeleton skeleton;
   @Autowired
   private SAEDocumentService documentService;

   private static final String VIDE = "";
   private static final String TITRE = "Titre";
   private static final String SAE = "sae";

   @After
   public void after() {
      EasyMock.reset(documentService);
   }

   private Recherche createSearchType(String filePath) {
      try {
         XMLStreamReader reader = XMLStreamUtils
               .createXMLStreamReader(filePath);
         return Recherche.Factory.parse(reader);

      } catch (Exception e) {
         throw new NestableRuntimeException(e);
      }

   }

   private static final String AXIS_FAULT = "AxisFault non attendue";

   private static void assertAxisFault(AxisFault axisFault, String expectedMsg,
         String expectedType, String expectedPrefix) {

      Assert.assertEquals(AXIS_FAULT, expectedMsg, axisFault.getMessage());
      Assert.assertEquals(AXIS_FAULT, expectedType, axisFault.getFaultCode()
            .getLocalPart());
      Assert.assertEquals(AXIS_FAULT, expectedPrefix, axisFault.getFaultCode()
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
    */
   @Test
   public void searchFailureSAESearchServiceEx() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx, SaeAccessDeniedAxisFault {

      String requete = TITRE + ":NOTIFICATIONS";
      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add("TailleFichier");

      try {
         // valeur attendu est MetaDataUnauthorizedToSearchEx via andThrow
         EasyMock
               .expect(documentService.search(requete, listMetaDesired))
               .andThrow(
                     new MetaDataUnauthorizedToSearchEx(
                           "La ou les m\u00E9tadonn\u00E9es suivantes, utilis\u00E9es dans la requ\u00EAte de recherche, ne sont pas autoris\u00E9s comme crit\u00E8res de recherche : "
                                 + listMetaDesired + "."));

         EasyMock.replay(documentService);

         Recherche request = createSearchType("src/test/resources/recherche/recherche_failure_01.xml");

         skeleton.rechercheSecure(request).getRechercheResponse();

         Assert
               .fail("le test doit échouer à cause de la levée d'une exception de type "
                     + SAESearchServiceEx.class);

      } catch (RechercheAxis2Fault fault) {
         assertAxisFault(
               fault,
               "La ou les m\u00E9tadonn\u00E9es suivantes, utilis\u00E9es dans la requ\u00EAte de recherche, ne sont pas autoris\u00E9s comme crit\u00E8res de recherche : "
                     + listMetaDesired + ".", "RechercheMetadonneesInterdite",
               SAE);
      }
   }

   /**
    * Test qui echoue car requete vide
    * 
    * @throws MetaDataUnauthorizedToConsultEx
    * @throws MetaDataUnauthorizedToSearchEx
    * @throws SAESearchServiceEx
    * @throws SyntaxLuceneEx
    * @throws UnknownLuceneMetadataEx
    * @throws UnknownDesiredMetadataEx
    * @throws MetaDataUnauthorizedToConsultEx
    * @throws MetaDataUnauthorizedToSearchEx
    * @throws SaeAccessDeniedAxisFault 
    */
   @Test
   @Ignore
   public void searchFailureReqVide() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx, SaeAccessDeniedAxisFault {

      String requete = VIDE;
      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add(TITRE);

      try {
         // valeur attendu est SAESearchServiceEx via andThrow
         EasyMock.expect(documentService.search(requete, listMetaDesired))
               .andThrow(
                     new SAESearchServiceEx(
                           "La requête de recherche n'est pas renseignée."));

         EasyMock.replay(documentService);

         Recherche request = createSearchType("src/test/resources/recherche/recherche_failure_03.xml");
         // recuperer le type de reponse de la recherche
         skeleton.rechercheSecure(request).getRechercheResponse();

         Assert.fail("le test doit échouer car la requete lucene est vide.");

      } catch (RechercheAxis2Fault fault) {
         assertAxisFault(fault, "La requête de recherche n'est pas renseignée",
               "RequeteLuceneVideOuNull", SAE);
      }
   }

   /**
    * Test qui echoue car format requete incorrecte
    * 
    * @throws MetaDataUnauthorizedToConsultEx
    * @throws MetaDataUnauthorizedToSearchEx
    * @throws SAESearchServiceEx
    * @throws SyntaxLuceneEx
    * @throws UnknownLuceneMetadataEx
    * @throws UnknownDesiredMetadataEx
    * @throws MetaDataUnauthorizedToConsultEx
    * @throws MetaDataUnauthorizedToSearchEx
    * @throws SaeAccessDeniedAxisFault 
    */
   @Test
   public void searchFailureReqIncorrecte() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx, SaeAccessDeniedAxisFault {

      String requete = TITRE + ":NOTIFICATION DateCreation:01-012011";
      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add(TITRE);

      try {
         // valeur attendu est SyntaxLuceneEx via andThrow
         EasyMock
               .expect(documentService.search(requete, listMetaDesired))
               .andThrow(
                     new SyntaxLuceneEx(
                           "La syntaxe de la requête de recherche n'est pas valide."));

         EasyMock.replay(documentService);

         Recherche request = createSearchType("src/test/resources/recherche/recherche_failure_05.xml");

         // recuperer le type de reponse de la recherche
         skeleton.rechercheSecure(request).getRechercheResponse();

         Assert.fail("Test qui echoue car format requete incorrecte.");

      } catch (RechercheAxis2Fault fault) {
         assertAxisFault(fault,
               "La syntaxe de la requête de recherche n'est pas valide.",
               "SyntaxeLuceneNonValide", SAE);
      }
   }

   /**
    * Test qui echoue car la liste des metadonnées n'est pas presente dans le
    * réferentiel des métadonnées.
    * 
    * @throws SAESearchServiceEx
    * @throws SyntaxLuceneEx
    * @throws UnknownLuceneMetadataEx
    * @throws UnknownDesiredMetadataEx
    * @throws MetaDataUnauthorizedToConsultEx
    * @throws MetaDataUnauthorizedToSearchEx
    * @throws SaeAccessDeniedAxisFault 
    */
   @Test
   public void searchFailureMetaDataNotExist() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx, SaeAccessDeniedAxisFault {

      String requete = TITRE + ":NOTIFICATIONS";
      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add("Metadata");

      try {
         // valeur attendu est MetaDataUnauthorizedToSearchEx via andThrow
         EasyMock
               .expect(documentService.search(requete, listMetaDesired))
               .andThrow(
                     new UnknownLuceneMetadataEx(
                           "La ou les m\u00E9tadonn\u00E9e(s) suivantes, "
                                 + "utilis\u00E9es dans la requ\u00EAte de recherche, "
                                 + "n'existent pas dans le r\u00E9f\u00E9rentiel des m\u00E9tadonn\u00E9es : "
                                 + listMetaDesired + "."));

         EasyMock.replay(documentService);

         Recherche request = createSearchType("src/test/resources/recherche/recherche_failure_02.xml");

         skeleton.rechercheSecure(request).getRechercheResponse();

         Assert
               .fail("le test doit échouer à cause de la levée d'une exception de type "
                     + SAESearchServiceEx.class);

      } catch (RechercheAxis2Fault fault) {
         assertAxisFault(
               fault,
               "La ou les m\u00E9tadonn\u00E9e(s) suivantes, "
                     + "utilis\u00E9es dans la requ\u00EAte de recherche, "
                     + "n'existent pas dans le r\u00E9f\u00E9rentiel des m\u00E9tadonn\u00E9es : "
                     + listMetaDesired + ".", "RechercheMetadonneesInconnues",
               SAE);
      }
   }

   @Test
   public void search_Failure_RuntimeException() throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx, SaeAccessDeniedAxisFault {

      List<String> listMetaDesired = new ArrayList<String>();
      listMetaDesired.add(EasyMock.anyObject(String.class));

      try {

         EasyMock.expect(
               documentService.search(EasyMock.anyObject(String.class),
                     listMetaDesired)).andThrow(
               new RuntimeException("une runtime exception est levée"));

         EasyMock.replay(documentService);

         Recherche request = createSearchType("src/test/resources/recherche/recherche_failure_02.xml");

         skeleton.rechercheSecure(request).getRechercheResponse();

         Assert
               .fail("le test doit échouer à cause de la levée d'une exception de type "
                     + SAESearchServiceEx.class);

      } catch (RechercheAxis2Fault fault) {
         assertAxisFault(
               fault,
               "Une erreur interne à l'application est survenue lors de la recherche.",
               "ErreurInterneRecherche", SAE);
      }
   }

}
