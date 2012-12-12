package fr.urssaf.image.sae.integration.droits;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.integration.droits.service.DroitService;

/**
 * Ceci n'est pas une classe de TU<br>
 * Elle sert uniquement pour lancer des créations à la volée
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-integration-droits-test.xml" })
@SuppressWarnings("PMD")
public class CreationTest {

   @Autowired
   private DroitService droitService;
   
   
   
   @Test
   @Ignore
   public void creationDesDroits() {
      
      // Utile uniquement quand on est en cassandra local
      // Pour un "vrai" Cassandra, commenter la ligne (dès que sae-lotinstallmaj crééra les AU)
      droitService.creationDesAu();
      
      File fichierDroitsXml = new File("src/test/resources/jeuxTest/saedroits.xml");
//      File fichierDroitsXml = new File(
//         "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Cellule_Intégration/Intégrations/0007 - Lot 12xx10/droits/saedroits.xml");
      
      droitService.creationDesDroits(fichierDroitsXml);
      
      // Une 2ème fois, pour vérifier les tests d'existence des PRMD et des CS
      droitService.creationDesDroits(fichierDroitsXml);
      
   }
   
   
   @Test
   @Ignore
   public void creationDesAu() {
      
      droitService.creationDesAu();
      
   }

   
}
