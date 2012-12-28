/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.util;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.TraitementService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
public class ParcoursDocumentsTest {

   @Autowired
   private TraitementService service;

   @Test
   @Ignore
   public void listeDocumentsNonMigres() throws CassandraException {

      service
            .writeDocStartingWithCodeOrga(
                  "c:/donnees.csv",
                  "S:/produits/Qualite/Projet_ae/Documentation refonte/Refonte/Régionalisation/"
                        + "Vague 2 (fin 2012)/Programme/correspondances_orga.properties");
   }
}
