package fr.urssaf.image.sae.integration.jeuxtests.generator;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class BavariaIsartorSommaireGeneratorTest {

   @Test
   @Ignore
   public void bavaria() throws IOException {

      File repEcde = new File(
            "G:/pmareche/sae_dev/ecde/ecde_local/CS/20140214/Bavaria");

      BavariaIsartorSommaireGenerator service = new BavariaIsartorSommaireGenerator(repEcde);
      service.genereSommaireBaravia();

   }
   
   @Test
   @Ignore
   public void isartor() throws IOException {

      File repEcde = new File(
            "G:/pmareche/sae_dev/ecde/ecde_local/CS/20140214/Isartor");

      BavariaIsartorSommaireGenerator service = new BavariaIsartorSommaireGenerator(repEcde);
      service.genereSommaireBaravia();

   }

}
