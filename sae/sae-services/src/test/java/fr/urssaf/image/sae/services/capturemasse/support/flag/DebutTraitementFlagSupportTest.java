/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.support.flag.model.DebutTraitementFlag;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-test.xml" })
public class DebutTraitementFlagSupportTest {

   @Autowired
   private EcdeTestTools ecdeTestTools;

   @Autowired
   private DebutTraitementFlagSupport support;

   private EcdeTestSommaire ecdeTestSommaire;

   @Before
   public void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
   }

   @After
   public void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDebutTraitementObligatoire() {

      support.writeDebutTraitementFlag(null, new File(""));

      Assert.fail("sortie aspect attendue");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testHostObligatoire() {

      DebutTraitementFlag flag = new DebutTraitementFlag();
      flag.setHostInfo(null);
      flag.setIdTraitement(UUID.randomUUID());
      flag.setStartDate(new Date());

      support.writeDebutTraitementFlag(flag, new File(""));

      Assert.fail("sortie aspect attendue");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDateObligatoire() throws UnknownHostException {

      DebutTraitementFlag flag = new DebutTraitementFlag();
      flag.setHostInfo(InetAddress.getLocalHost());
      flag.setIdTraitement(UUID.randomUUID());
      flag.setStartDate(null);

      support.writeDebutTraitementFlag(flag, new File(""));

      Assert.fail("sortie aspect attendue");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testIdObligatoire() throws UnknownHostException {

      DebutTraitementFlag flag = new DebutTraitementFlag();
      flag.setHostInfo(InetAddress.getLocalHost());
      flag.setIdTraitement(null);
      flag.setStartDate(new Date());

      support.writeDebutTraitementFlag(flag, new File(""));

      Assert.fail("sortie aspect attendue");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testEcdeObligatoire() throws UnknownHostException {

      DebutTraitementFlag flag = new DebutTraitementFlag();
      flag.setHostInfo(InetAddress.getLocalHost());
      flag.setIdTraitement(UUID.randomUUID());
      flag.setStartDate(new Date());

      support.writeDebutTraitementFlag(flag, null);

      Assert.fail("sortie aspect attendue");
   }

   @Test
   public void testRepertoireInexistant() throws UnknownHostException {

      File ecdeDirectory = ecdeTestSommaire.getRepEcde();

      DebutTraitementFlag flag = new DebutTraitementFlag();
      flag.setHostInfo(InetAddress.getLocalHost());
      flag.setIdTraitement(UUID.randomUUID());
      flag.setStartDate(new Date());

      support.writeDebutTraitementFlag(flag, ecdeDirectory);

   }

}
