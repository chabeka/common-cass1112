/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.factory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import fr.urssaf.image.sae.trace.executable.service.TraitementService;

public class TraceContextFactoryTest {

   private File saeResourceFile, cassandraFile, dfceFile;

   @Before
   public void before() throws IOException {
      ClassPathResource cassandraResource = new ClassPathResource(
            "config/commons-cassandra.properties");
      File file = new File(cassandraResource.getURI());
      List<String> lines = FileUtils.readLines(file);
      cassandraFile = File.createTempFile("cassandra-config", ".properties");
      FileUtils.writeLines(cassandraFile, lines);

      cassandraResource = new ClassPathResource(
            "config/commons-dfce.properties");
      file = new File(cassandraResource.getURI());
      lines = FileUtils.readLines(file);
      dfceFile = File.createTempFile("dfce-config", ".properties");
      FileUtils.writeLines(dfceFile, lines);

      saeResourceFile = File.createTempFile("sae-config", ".properties");
      lines = Arrays
            .asList(
                  "sae.dfce.cheminFichierConfig="
                        + FilenameUtils.separatorsToUnix(dfceFile
                              .getAbsolutePath()),
                  "sae.cassandra.cheminFichierConfig="
                        + FilenameUtils.separatorsToUnix(cassandraFile
                              .getAbsolutePath()),
                  "sae.ecde.cheminFichierConfig=src/test/resources/config/ecdesources.xml",
                  "sae.metadata.cache=6000",
                  "sae.rnd.url=http://cer69imageint4.cer69.recouv:9007/services/duplication.php?WSDL",
                  "sae.rnd.cache=6000",
                  "sae.referentiel.format.cache=1000",
                  "sae.format.control.profil.cache=1000",
                  "sae.pagmf.cache=1000");

      FileUtils.writeLines(saeResourceFile, lines);

   }

   @After
   public void after() {
      FileUtils.deleteQuietly(dfceFile);
      FileUtils.deleteQuietly(cassandraFile);
      FileUtils.deleteQuietly(saeResourceFile);
   }

   @Test
   public void testPurgeObligatoire() {
      ApplicationContext context = TraceContextFactory.loadContext(
            "/applicationContext-sae-trace-executable.xml", saeResourceFile
                  .getAbsolutePath());
      Assert.assertNotNull("le contexte doit etre créé", context);

      TraitementService service = context.getBean(TraitementService.class);
      Assert.assertNotNull("le service TraitementService doit etre créé",
            service);
   }

}
