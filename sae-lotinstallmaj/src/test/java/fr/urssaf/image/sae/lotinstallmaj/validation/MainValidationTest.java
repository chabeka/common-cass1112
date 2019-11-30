package fr.urssaf.image.sae.lotinstallmaj.validation;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.sae.lotinstallmaj.Main;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;


/**
 * Classe test pour la validation des paramétres de la méthodes main.
 */
public class MainValidationTest {
   
   @Test(expected=MajLotGeneralException.class)
   public void mainTestNull() {
      
      String[] args = null;
      Main.main(args);
   }
   
   @Test(expected=MajLotGeneralException.class)
   public void mainCheminFileInexistant() {
      String[] args = new String[1];
      args[0] = "cheminInexistant.pdf";
      Main.main(args);
   }
   
   @Test(expected=MajLotGeneralException.class)
   public void mainCheminFileExistantOperationInexistante() {
      String[] args = new String[2];
      args[0] = "src/test/resources/test.txt";
      Main.main(args);
      fail("Erreur : Il faut indiquer, en deuxieme argument de la ligne de commande, " +
      	  "le nom de l'opération à réaliser.");
   }
   
   @Test(expected=MajLotGeneralException.class)
   public void mainCheminNumeroLotInconnu() {
      String[] args = new String[2];
      args[0] = "src/test/resources/test.txt";
      args[1] = "operationInconnue";
      Main.main(args);
      fail("Erreur : Opération inconnue.");
   }
   
   @Test
   @Ignore("Faire le test sur son poste en lancant le jar executable" +
   		  "avec en paramètre le fichier properties et le nom de l'opération souhaitée.")
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   public void mainCheminNumeroSuccess() {
      String[] args = new String[2];
      args[0] = "src/test/resources/test.txt";
      args[1] = "nomOperationSouhaitee";
      Main.main(args);
   }

}
