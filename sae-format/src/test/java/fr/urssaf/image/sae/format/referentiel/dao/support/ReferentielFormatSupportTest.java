package fr.urssaf.image.sae.format.referentiel.dao.support;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.Utils;

/**
 * 
 * Classe test pour {@link ReferentielFormatSupport}
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ReferentielFormatSupportTest {

   @Autowired
   @Qualifier("referentielFormatSupport")
   private ReferentielFormatSupport refFormatSupport;
         
   @Autowired
   private JobClockSupport jobClock;
   
   private static final String FIND_MESSAGE_INCORRECT = "FIND - Erreur : Le message de l'exception est incorrect";
      
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
         String idFormat = "fmt/534";
         FormatFichier refFormatNonTrouve = refFormatSupport.find(idFormat);   
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
      
      List<FormatFichier> listRefFormatTrouve = refFormatSupport.findAll();          
      Assert.assertNotNull(listRefFormatTrouve);  
     
      Assert.assertEquals("Le nombre d'éléments est incorrect.", 4, listRefFormatTrouve.size());
   }
   
   
   
   
   @Test
   public void createFailureParamObligManquant() {
      try {   
         FormatFichier refFormat = Utils.getRefFormParamObligManquant();   // idFormat et description
         refFormatSupport.create(refFormat, jobClock.currentCLock());
         
         Assert.fail("Une exception IllegalArgumentException aurait dû être levée");
      } catch (IllegalArgumentException ex) {
         Assert.assertEquals(FIND_MESSAGE_INCORRECT, 
                             "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, description].", 
                              ex.getMessage());
      }
   }
   
   @Test
   public void createSuccess() throws UnknownFormatException {
      
      FormatFichier refFormat = Utils.genererRefFormatLambda();
      // idFormat : lambda
      String idFormat = refFormat.getIdFormat();
      Assert.assertEquals("lambda", idFormat);
      
      refFormatSupport.create(refFormat, jobClock.currentCLock());
      
      // le referentielFormat lambda a bien été créé.
      // pour le vérifier -> recherche dessus 
      FormatFichier refFormatTrouve = refFormatSupport.find(idFormat);   
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
            FormatFichier refFormat = Utils.genererRefFormatLambda();
            // idFormat : lambda
            String idFormat = refFormat.getIdFormat();
            Assert.assertEquals("lambda", idFormat);
            
            refFormatSupport.create(refFormat, jobClock.currentCLock());
            // le referentielFormat lambda a bien été créé.
            // pour le vérifier -> recherche dessus 
            FormatFichier refFormatTrouve = refFormatSupport.find(idFormat);   
            Assert.assertNotNull(refFormatTrouve);
            
            // suppression de ce format
            refFormatSupport.delete(idFormat, jobClock.currentCLock());
            
            // exception levée car le format n'existe plus.
            refFormatSupport.find(idFormat);
            
      } catch (UnknownFormatException ex) {
            Assert.assertEquals(FIND_MESSAGE_INCORRECT, 
                                "Aucun format n'a été trouvé avec l'identifiant : lambda.", 
                                 ex.getMessage());
      }
   }
   
   @Test
   public void deleteFailureRefFormatInexistant() throws ReferentielRuntimeException {
      try {   
            
            String idFormat = "refFormatInexistant";
             
            FormatFichier refFormatNonTrouve = refFormatSupport.find(idFormat);   
            Assert.assertNull(refFormatNonTrouve);
            
            // suppression de ce format
            refFormatSupport.delete(idFormat, jobClock.currentCLock());
            // exception levée car le format n'existe pas.
            
      } catch (UnknownFormatException ex) {
            Assert.assertEquals(FIND_MESSAGE_INCORRECT, 
                                "Le format à supprimer : [refFormatInexistant] n'existe pas en base.", 
                                 ex.getMessage());
      }
   }
      
}
