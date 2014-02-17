package fr.urssaf.image.sae.integration.jeuxtests.generator;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class SommaireGeneratorTest {

   @Test
   @Ignore
   public void bavaria() throws IOException {

      File repEcde = new File(
            "G:/pmareche/sae_dev/ecde/ecde_local/CS/20140214/Bavaria");

      SommaireGenerator service = new SommaireGenerator(repEcde);
      service.genereSommaire();

   }
   
   @Test
   @Ignore
   public void isartor() throws IOException {

      File repEcde = new File(
            "G:/pmareche/sae_dev/ecde/ecde_local/CS/20140214/Isartor");

      SommaireGenerator service = new SommaireGenerator(repEcde);
      service.genereSommaire();

   }
   
   @Test
   @Ignore
   public void prod01() throws IOException {

      File repEcde = new File(
            "G:/pmareche/sae_dev/ecde/ecde_local/CS/20140214/SAE-Prod-01");

      SommaireGenerator service = new SommaireGenerator(repEcde);
      service.genereSommaire();

   }

}
