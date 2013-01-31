package fr.urssaf.image.sae.regionalisation.fond.documentaire.util;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.RegioParDocumentService;

/**
 * Cette classe n'est pas une "vraie" classe de TU.
 * 
 * Elle contient les méthodes de traitement pour réaliser les opérations
 * de régionalisation de la plateforme de validation nationale (GIVN)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
@Ignore
public class RegionalisationGivnTest {

   @Autowired
   private RegioParDocumentService regioParDocumentService;

   
   @Test
   public void etape1_PrepareFichiersV2() throws IOException {

      File pathFichiersV2 = new File("c:/datas/");
      File pathFichiersSortie = new File("c:/divers/sortie/");
      
      regioParDocumentService.prepareFichiersV2(pathFichiersV2, pathFichiersSortie);
      
   }
   
   
   @Test
   public void etape2_ExtraitDonnees() throws IOException {

      File fichierSortieCsv = new File("c:/divers/fonds_doc_givn.csv");
      
      regioParDocumentService.extractionFondsDocumentaire(fichierSortieCsv);
      
   }
   
   
   @Test
   public void etape3_TraitementsPosgreSQL() throws IOException {

      // Traitements à effectuer dans PostgreSQL
      // Ce TU n'est là que rappeler que cette étape doit être réalisée
      
   }
   
   
   
   @Test
   public void etape4_majDocuments() {
      
      File fichierCsv = new File("c:/divers/givn_docs_a_regio.csv");
      // int numeroPremiereLigne = 2; // Ligne 1 = ligne d'en-tête
      int numeroPremiereLigne = 3;
      
      File fichierTraces = new File("c:/divers/traces_renum_givn.txt");
      
      regioParDocumentService.miseAjourDocuments(
            fichierCsv, numeroPremiereLigne, fichierTraces);
      
   }
   
   
   
}
