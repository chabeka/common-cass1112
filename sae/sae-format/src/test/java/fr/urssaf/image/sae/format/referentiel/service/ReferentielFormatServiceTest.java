package fr.urssaf.image.sae.format.referentiel.service;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.Utils;

/**
 * 
 * Classe testant les services de la classe {@link ReferentielFormatService}
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ReferentielFormatServiceTest {

   @Autowired
   private ReferentielFormatService refFormatService;

   private static final String ERREUR_FIND_MESSAGE = "FIND - Erreur : Le message de l'exception est incorrect";

   @Test
   public void getFormatSuccess() throws ReferentielRuntimeException,
         UnknownFormatException {

      String idFormat = "fmt/354";

      FormatFichier refFormatTrouve = refFormatService.getFormat(idFormat);
      Assert.assertNotNull(refFormatTrouve);

      Assert.assertEquals("FIND - Erreur dans l'idFormat.", "fmt/354",
            refFormatTrouve.getIdFormat());
      Assert.assertEquals("FIND - Erreur dans l'extension.", "Pdf",
            refFormatTrouve.getExtension());
      Assert.assertEquals("FIND - Erreur dans le typeMime.", "application/pdf",
            refFormatTrouve.getTypeMime());
      Assert.assertEquals("FIND - Erreur dans le boolean visualisable.", true,
            refFormatTrouve.isVisualisable());
      Assert.assertEquals("FIND - Erreur dans le validateur.",
            "pdfaValidatorImpl", refFormatTrouve.getValidator());
      Assert.assertEquals("FIND - Erreur dans l'identifieur.",
            "pdfaIdentifierImpl", refFormatTrouve.getIdentificateur());
      Assert.assertEquals("FIND - Erreur dans le convertisseur.", 
            "pdfSplitterImpl", refFormatTrouve.getConvertisseur());

      idFormat = "fmt/353";

      refFormatTrouve = refFormatService.getFormat(idFormat);
      Assert.assertNotNull(refFormatTrouve);

      Assert.assertEquals("FIND - Erreur dans l'idFormat.", "fmt/353",
            refFormatTrouve.getIdFormat());
      Assert.assertEquals("FIND - Erreur dans l'extension.", "TIF",
            refFormatTrouve.getExtension());
      Assert.assertEquals("FIND - Erreur dans le typeMime.", "image/tiff",
            refFormatTrouve.getTypeMime());
      Assert.assertEquals("FIND - Erreur dans le boolean visualisable.", false,
            refFormatTrouve.isVisualisable());
      Assert.assertNull("FIND - Erreur dans le validateur.", refFormatTrouve
            .getValidator());
      Assert.assertNull("FIND - Erreur dans l'identifieur.", refFormatTrouve
            .getIdentificateur());
      Assert.assertEquals("FIND - Erreur dans le convertisseur.",
            "tiffToPdfConvertisseurImpl", refFormatTrouve.getConvertisseur());
   }

   @Test
   public void findRefFormatNonTrouve() {

      try {
         String idFormat = "fmt/534";
         FormatFichier refFormatNonTrouve = refFormatService
               .getFormat(idFormat);
         Assert.assertNull(refFormatNonTrouve);
         Assert
               .fail("Une exception UnknownFormatException aurait dû être levée");
      } catch (UnknownFormatException ex) {
         Assert.assertEquals(ERREUR_FIND_MESSAGE,
               "Aucun format n'a été trouvé avec l'identifiant : fmt/534.", ex
                     .getMessage());
      }
   }

   @Test
   public void findAllSuccess() throws ReferentielRuntimeException {

      List<FormatFichier> listRefFormatTrouve = refFormatService.getAllFormat();
      Assert.assertNotNull(listRefFormatTrouve);

      Assert.assertEquals("Le nombre d'éléments est incorrect.", 2,
            listRefFormatTrouve.size());
   }

   @Test
   public void createFailureParamObligManquant()
         throws ReferentielRuntimeException {
      try {
         FormatFichier refFormat = Utils.getRefFormParamObligManquant(); // idFormat
         // et
         // description
         refFormatService.addFormat(refFormat);

         Assert
               .fail("Une exception ReferentielRuntimeException aurait dû être levée");
      } catch (IllegalArgumentException ex) {
         Assert
               .assertEquals(
                     ERREUR_FIND_MESSAGE,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, description].",
                     ex.getMessage());
      }
   }

   @Test
   public void createSuccess() throws ReferentielRuntimeException,
         UnknownFormatException {

      FormatFichier refFormat = Utils.genererRefFormatLambda();
      // idFormat : lambda
      String idFormat = refFormat.getIdFormat();
      Assert.assertEquals("lambda", idFormat);

      refFormatService.addFormat(refFormat);

      // le referentielFormat lambda a bien été créé.
      // pour le vérifier -> recherche dessus
      FormatFichier refFormatTrouve = refFormatService.getFormat(idFormat);
      Assert.assertNotNull(refFormatTrouve);

      Assert.assertEquals("FIND - Erreur dans l'idFormat.", "lambda",
            refFormatTrouve.getIdFormat());
      Assert.assertEquals("FIND - Erreur dans l'extension.", "Lambda",
            refFormatTrouve.getExtension());
      Assert.assertEquals("FIND - Erreur dans le typeMime.",
            "application/lambda", refFormatTrouve.getTypeMime());
      Assert.assertEquals("FIND - Erreur dans le boolean visualisable.", true,
            refFormatTrouve.isVisualisable());
      Assert.assertEquals("FIND - Erreur dans le validateur.",
            "LambdaValidatorImpl", refFormatTrouve.getValidator());
      Assert.assertEquals("FIND - Erreur dans l'identifieur.",
            "LambdaIdentifierImpl", refFormatTrouve.getIdentificateur());
      Assert.assertEquals("FIND - Erreur dans le convertisseur.",
            "LambdaConvertisseurImpl", refFormatTrouve.getConvertisseur());

   }

   @Test
   public void deleteSucess() throws ReferentielRuntimeException {
      try {
         FormatFichier refFormat = Utils.genererRefFormatLambda();
         // idFormat : lambda
         String idFormat = refFormat.getIdFormat();
         Assert.assertEquals("lambda", idFormat);

         refFormatService.addFormat(refFormat);
         // le referentielFormat lambda a bien été créé.
         // pour le vérifier -> recherche dessus
         FormatFichier refFormatTrouve = refFormatService.getFormat(idFormat);
         Assert.assertNotNull(refFormatTrouve);

         // suppression de ce format
         refFormatService.deleteFormat(idFormat);

         // exception levée car le format n'existe plus.
         refFormatService.getFormat(idFormat);

      } catch (UnknownFormatException ex) {
         Assert.assertEquals(ERREUR_FIND_MESSAGE,
               "Aucun format n'a été trouvé avec l'identifiant : lambda.", ex
                     .getMessage());
      }
   }

}
