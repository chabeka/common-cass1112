package fr.urssaf.image.sae.documents.executable.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;
import net.docubase.toolkit.model.document.Document;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres;
import fr.urssaf.image.sae.documents.executable.model.FormatValidationParametres.MODE_VERIFICATION;
import fr.urssaf.image.sae.documents.executable.service.impl.TraitementServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-documents-executable-test.xml" })
public class TraitementServiceTest {

   @Autowired
   private TraitementService traitementService;

   @Autowired
   private DfceService dfceService;

   private final File file = new File(
         "src/test/resources/identification/PdfaValide.pdf");

   private final File doc1 = new File(
         "src/test/resources/identification/doc1.pdf");

   private final File doc = new File(
         "src/test/resources/identification/word.doc");

   private FormatValidationParametres createParametres(String requeteLucene,
         MODE_VERIFICATION mode) {
      FormatValidationParametres parametres = new FormatValidationParametres();
      parametres.setModeVerification(mode);
      parametres.setRequeteLucene(requeteLucene);
      parametres.setNombreMaxDocs(10);
      parametres.setTaillePasExecution(5);
      parametres.setTaillePool(1);
      parametres.setTailleQueue(5);
      parametres.setTempsMaxTraitement(0);
      parametres.setMetadonnees(new ArrayList<String>());
      return parametres;
   }

   private Document createDocument(String idFormat) {
      Document document = new Document();
      document.setUuid(UUID.fromString("00000000-0000-0000-0000-000000000000"));
      document.setArchivageDate(new Date());
      document.setType("2.3.1.1.12");
      document.addCriterion("cse", "CS1");
      document.addCriterion("apr", "GED");
      document.addCriterion("atr", "GED");
      document.addCriterion("ffi", idFormat);
      return document;
   }

   private void createMock(TraitementServiceImpl traitementService,
         String idFormat, String requeteLucene, final File fichier)
         throws FileNotFoundException, SearchQueryParseException {

      Document document = createDocument(idFormat);
      // creation de la liste des documents
      List<Document> listeDoc = new ArrayList<Document>();
      for (int index = 0; index < 10; index++) {
         listeDoc.add(document);
      }

      // creation du mock
      DfceService dfceService = EasyMock.createNiceMock(DfceService.class);
      EasyMock.expect(dfceService.executerRequete(requeteLucene)).andReturn(
            listeDoc.iterator());
      EasyMock.expect(dfceService.recupererContenu(document)).andAnswer(
            new IAnswer<InputStream>() {
               @Override
               public InputStream answer() throws Throwable {
                  return new FileInputStream(fichier);
               }

            }).anyTimes();
      EasyMock.replay(dfceService);
      traitementService.setDfceService(dfceService);
   }

   @Test
   public void identifierValiderFichiersIdentificationValide()
         throws FileNotFoundException, SearchQueryParseException {

      FormatValidationParametres parametres = createParametres(
            "iti:73132b50-d404-11e2-9df1-005056c00008",
            MODE_VERIFICATION.IDENTIFICATION);

      createMock((TraitementServiceImpl) traitementService, "fmt/354",
            parametres.getRequeteLucene(), file);

      // cas d'identification valide
      int nbTraites = traitementService.identifierValiderFichiers(parametres);
      Assert.assertEquals("Le nombre de documents traités n'est pas correct",
            10, nbTraites);

      // remet a jour le context
      ((TraitementServiceImpl) traitementService).setDfceService(dfceService);
   }

   @Test
   public void identifierValiderFichiersIdentificationNonValide()
         throws FileNotFoundException, SearchQueryParseException {

      FormatValidationParametres parametres = createParametres(
            "srt:41882050200023", MODE_VERIFICATION.IDENTIFICATION);

      createMock((TraitementServiceImpl) traitementService, "fmt/18",
            parametres.getRequeteLucene(), doc);

      // cas d'identification non valide
      int nbTraites = traitementService.identifierValiderFichiers(parametres);

      Assert.assertEquals("Le nombre de documents traités n'est pas correct",
            10, nbTraites);

      // remet a jour le context
      ((TraitementServiceImpl) traitementService).setDfceService(dfceService);
   }

   @Test
   public void identifierValiderFichiersValidationValide()
         throws FileNotFoundException, SearchQueryParseException {

      FormatValidationParametres parametres = createParametres(
            "iti:73132b50-d404-11e2-9df1-005056c00008",
            MODE_VERIFICATION.VALIDATION);

      createMock((TraitementServiceImpl) traitementService, "fmt/354",
            parametres.getRequeteLucene(), file);

      // cas de validation (document valide)
      int nbTraites = traitementService.identifierValiderFichiers(parametres);

      Assert.assertEquals("Le nombre de documents traités n'est pas correct",
            10, nbTraites);

      // remet a jour le context
      ((TraitementServiceImpl) traitementService).setDfceService(dfceService);
   }

   @Test
   public void identifierValiderFichiersValidationNonValide()
         throws FileNotFoundException, SearchQueryParseException {

      FormatValidationParametres parametres = createParametres(
            "iti:73132b50-d404-11e2-9df1-005056c00008",
            MODE_VERIFICATION.VALIDATION);

      createMock((TraitementServiceImpl) traitementService, "fmt/354",
            parametres.getRequeteLucene(), doc1);

      // cas de validation (document non valide)
      int nbTraites = traitementService.identifierValiderFichiers(parametres);

      Assert.assertEquals("Le nombre de documents traités n'est pas correct",
            10, nbTraites);

      // remet a jour le context
      ((TraitementServiceImpl) traitementService).setDfceService(dfceService);
   }

   @Test
   public void identifierValiderFichiersValidationUnknownFormat()
         throws FileNotFoundException, SearchQueryParseException {

      FormatValidationParametres parametres = createParametres(
            "srt:41882050200023", MODE_VERIFICATION.VALIDATION);

      createMock((TraitementServiceImpl) traitementService, "fmt/18",
            parametres.getRequeteLucene(), doc);

      // cas de validation (unknown format)
      int nbTraites = traitementService.identifierValiderFichiers(parametres);

      Assert.assertEquals("Le nombre de documents traités n'est pas correct",
            10, nbTraites);

      // remet a jour le context
      ((TraitementServiceImpl) traitementService).setDfceService(dfceService);
   }

   @Test
   public void identifierValiderFichiersIdentValidationValide()
         throws FileNotFoundException, SearchQueryParseException {

      FormatValidationParametres parametres = createParametres(
            "iti:73132b50-d404-11e2-9df1-005056c00008",
            MODE_VERIFICATION.IDENT_VALID);

      createMock((TraitementServiceImpl) traitementService, "fmt/354",
            parametres.getRequeteLucene(), file);

      // cas d'identification et validation (document non valide)
      int nbTraites = traitementService.identifierValiderFichiers(parametres);

      Assert.assertEquals("Le nombre de documents traités n'est pas correct",
            10, nbTraites);

      // remet a jour le context
      ((TraitementServiceImpl) traitementService).setDfceService(dfceService);
   }

   @Test
   public void identifierValiderFichiersIdentValidationNonValide()
         throws FileNotFoundException, SearchQueryParseException {

      FormatValidationParametres parametres = createParametres(
            "iti:73132b50-d404-11e2-9df1-005056c00008",
            MODE_VERIFICATION.IDENT_VALID);

      createMock((TraitementServiceImpl) traitementService, "fmt/354",
            parametres.getRequeteLucene(), doc1);

      // cas d'identification et validation (document non valide)
      int nbTraites = traitementService.identifierValiderFichiers(parametres);

      Assert.assertEquals("Le nombre de documents traités n'est pas correct",
            10, nbTraites);

      // remet a jour le context
      ((TraitementServiceImpl) traitementService).setDfceService(dfceService);
   }

   @Test
   public void identifierValiderFichiersIdentValidationUnknownFormat()
         throws FileNotFoundException, SearchQueryParseException {

      FormatValidationParametres parametres = createParametres(
            "srt:41882050200023", MODE_VERIFICATION.IDENT_VALID);

      createMock((TraitementServiceImpl) traitementService, "fmt/18",
            parametres.getRequeteLucene(), doc);

      // cas d'identification et validation (unknown format)
      int nbTraites = traitementService.identifierValiderFichiers(parametres);

      Assert.assertEquals("Le nombre de documents traités n'est pas correct",
            10, nbTraites);

      // remet a jour le context
      ((TraitementServiceImpl) traitementService).setDfceService(dfceService);
   }
}
