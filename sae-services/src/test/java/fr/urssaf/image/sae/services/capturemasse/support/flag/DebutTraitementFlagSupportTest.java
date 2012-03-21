/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.services.capturemasse.support.flag.model.DebutTraitementFlag;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-capture-masse-test.xml" })
public class DebutTraitementFlagSupportTest {

   @Autowired
   private DebutTraitementFlagSupport support;

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

}
