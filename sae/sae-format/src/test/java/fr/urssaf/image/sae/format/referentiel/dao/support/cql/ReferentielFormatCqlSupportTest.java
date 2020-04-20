package fr.urssaf.image.sae.format.referentiel.dao.support.cql;

import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.commons.utils.Row;
import fr.urssaf.image.sae.commons.utils.cql.DataCqlUtils;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.FormatFichierUtils;
import fr.urssaf.image.sae.format.utils.Utils;

/**
 * 
 * Classe test pour {@link ReferentielFormatSupport}
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-format-test.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReferentielFormatCqlSupportTest {

  @Qualifier("referentielFormatCqlSupport")
  @Autowired
  private ReferentielFormatCqlSupport refFormatSupport;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private ModeApiCqlSupport modeApiSupport;

  private static final String FIND_MESSAGE_INCORRECT = "FIND - Erreur : Le message de l'exception est incorrect";



  private static final Logger LOGGER = LoggerFactory
      .getLogger(ReferentielFormatCqlSupportTest.class);

  @Before
  public void before() throws Exception {
    if (server.getStartLocal()) {
      // Création à partir du xml
      final URL url = this.getClass().getResource("/cassandra-local-datasets/cassandra-local-dataset-sae-format.xml");
      final List<Row> list = DataCqlUtils.deserializeColumnFamilyToRows(url.getPath(), "ReferentielFormat");
      final List<FormatFichier> listFormatFichier = FormatFichierUtils.convertRowsToFormatFichier(list);
      for (final FormatFichier formatFichier : listFormatFichier) {
        refFormatSupport.create(formatFichier);
      }
      // Recherche
      final List<FormatFichier> listFormatFichierBase = refFormatSupport.findAll();
      // On supprime lambda si besoin
      try {
        refFormatSupport.delete("lambda");
      }
      catch (final UnknownFormatException e) {
        LOGGER.error("Erreur sur la suppression du format lambda {}", e.getMessage());
      }
      final int taille = listFormatFichierBase.size();
      modeApiSupport.updateModeApi(ModeGestionAPI.MODE_API.DATASTAX, Constantes.CF_REFERENTIEL_FORMAT);

    }
  }

  @After
  public void end() throws Exception {

    server.resetDataOnly();

  }

  @Test
  public void init() {
    try {
      if (server.isCassandraStarted()) {
        server.resetData();
      }
      Assert.assertTrue(true);

    }
    catch (final Exception e) {
      LOGGER.error("Une erreur s'est produite lors du resetData de cassandra: {}", e.getMessage());
    }
  }

  @Test
  public void findSuccess() throws UnknownFormatException {

    String idFormat = "fmt/354";

    FormatFichier refFormatTrouve = refFormatSupport.find(idFormat);          
    Assert.assertNotNull(refFormatTrouve);  

    Assert.assertEquals("FIND - Erreur dans l'idFormat.", "fmt/354", refFormatTrouve.getIdFormat());
    Assert.assertEquals("FIND - Erreur dans l'extension.", "Pdf", refFormatTrouve.getExtension());
    Assert.assertEquals("FIND - Erreur dans le typeMime.", "application/pdf", refFormatTrouve.getTypeMime());
    Assert.assertEquals("FIND - Erreur dans le boolean visualisable.", true, refFormatTrouve.isVisualisable());
    Assert.assertEquals("FIND - Erreur dans le validateur.", "pdfaValidatorImpl", refFormatTrouve.getValidator());
    Assert.assertEquals("FIND - Erreur dans l'identifieur.", "pdfaIdentifierImpl", refFormatTrouve.getIdentificateur());
    Assert.assertEquals("FIND - Erreur dans le convertisseur.", "pdfSplitterImpl", refFormatTrouve.getConvertisseur());

    idFormat = "fmt/353";

    refFormatTrouve = refFormatSupport.find(idFormat);          
    Assert.assertNotNull(refFormatTrouve);  

    Assert.assertEquals("FIND - Erreur dans l'idFormat.", "fmt/353", refFormatTrouve.getIdFormat());
    Assert.assertEquals("FIND - Erreur dans l'extension.", "TIF,tiff", refFormatTrouve.getExtension());
    Assert.assertEquals("FIND - Erreur dans le typeMime.", "image/tiff", refFormatTrouve.getTypeMime());
    Assert.assertEquals("FIND - Erreur dans le boolean visualisable.", false, refFormatTrouve.isVisualisable());
    Assert.assertNull("FIND - Erreur dans le validateur.", refFormatTrouve.getValidator());
    Assert.assertNull("FIND - Erreur dans l'identifieur.", refFormatTrouve.getIdentificateur());
    Assert.assertEquals("FIND - Erreur dans le convertisseur.", "tiffToPdfConvertisseurImpl", refFormatTrouve.getConvertisseur());
  }

  @Test
  public void findRefFormatNonTrouve() {

    //try {
    final String idFormat = "fmt/534";
    final FormatFichier refFormatNonTrouve = refFormatSupport.find(idFormat);   
    Assert.assertNull(refFormatNonTrouve);     
    //         Assert.fail("Une exception UnknownParameterException aurait dû être levée");
    //      } catch (UnknownFormatException ex) {
    //         Assert.assertEquals(FIND_MESSAGE_INCORRECT, 
    //                             "Aucun format n'a été trouvé avec l'identifiant : fmt/534.", 
    //                              ex.getMessage());
    //      }
  }

  @Test
  public void findAllSuccess() {

    final List<FormatFichier> listRefFormatTrouve = refFormatSupport.findAll();          
    Assert.assertNotNull(listRefFormatTrouve);  

    Assert.assertEquals("Le nombre d'éléments est incorrect.", 5, listRefFormatTrouve.size());
  }




  @Test
  public void createFailureParamObligManquant() {
    try {   
      final FormatFichier refFormat = Utils.getRefFormParamObligManquant();   // idFormat et description
      refFormatSupport.create(refFormat);

      Assert.fail("Une exception IllegalArgumentException aurait dû être levée");
    } catch (final IllegalArgumentException ex) {
      // Assert.assertEquals(FIND_MESSAGE_INCORRECT,
      // "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, description].",
      // ex.getMessage());
      Assert.assertEquals(FIND_MESSAGE_INCORRECT,
                          "L'identifiant ne peut être null",
                          ex.getMessage());
    }
  }

  @Test
  public void createSuccess() throws UnknownFormatException {

    final FormatFichier refFormat = Utils.genererRefFormatLambda();
    // idFormat : lambda
    final String idFormat = refFormat.getIdFormat();
    Assert.assertEquals("lambda", idFormat);

    refFormatSupport.create(refFormat);

    // le referentielFormat lambda a bien été créé.
    // pour le vérifier -> recherche dessus 
    final FormatFichier refFormatTrouve = refFormatSupport.find(idFormat);   
    Assert.assertNotNull(refFormatTrouve);

    Assert.assertEquals("FIND - Erreur dans l'idFormat.", "lambda", refFormatTrouve.getIdFormat());
    Assert.assertEquals("FIND - Erreur dans l'extension.", "Lambda", refFormatTrouve.getExtension());
    Assert.assertEquals("FIND - Erreur dans le typeMime.", "application/lambda", refFormatTrouve.getTypeMime());
    Assert.assertEquals("FIND - Erreur dans le boolean visualisable.", true, refFormatTrouve.isVisualisable());
    Assert.assertEquals("FIND - Erreur dans le validateur.", "LambdaValidatorImpl", refFormatTrouve.getValidator());
    Assert.assertEquals("FIND - Erreur dans l'identifieur.", "LambdaIdentifierImpl", refFormatTrouve.getIdentificateur());
    Assert.assertEquals("FIND - Erreur dans le convertisseur.", "LambdaConvertisseurImpl", refFormatTrouve.getConvertisseur());
  }

  @Test
  public void deleteSuccessAvecFindVerif() throws ReferentielRuntimeException {
    try {   
      final FormatFichier refFormat = Utils.genererRefFormatLambda();
      // idFormat : lambda
      final String idFormat = refFormat.getIdFormat();
      Assert.assertEquals("lambda", idFormat);

      refFormatSupport.create(refFormat);
      // le referentielFormat lambda a bien été créé.
      // pour le vérifier -> recherche dessus 
      final FormatFichier refFormatTrouve = refFormatSupport.find(idFormat);   
      Assert.assertNotNull(refFormatTrouve);

      // suppression de ce format
      refFormatSupport.delete(idFormat);

      // exception levée car le format n'existe plus.
      refFormatSupport.find(idFormat);

    } catch (final UnknownFormatException ex) {
      Assert.assertEquals(FIND_MESSAGE_INCORRECT, 
                          "Aucun format n'a été trouvé avec l'identifiant : lambda.", 
                          ex.getMessage());
    }
  }

  @Test
  public void deleteFailureRefFormatInexistant() throws ReferentielRuntimeException {
    try {   

      final String idFormat = "refFormatInexistant";

      final FormatFichier refFormatNonTrouve = refFormatSupport.find(idFormat);   
      Assert.assertNull(refFormatNonTrouve);

      // suppression de ce format
      refFormatSupport.delete(idFormat);
      // exception levée car le format n'existe pas.

    } catch (final UnknownFormatException ex) {
      Assert.assertEquals(FIND_MESSAGE_INCORRECT, 
                          "Le format à supprimer : [refFormatInexistant] n'existe pas en base.", 
                          ex.getMessage());
    }
  }

}
