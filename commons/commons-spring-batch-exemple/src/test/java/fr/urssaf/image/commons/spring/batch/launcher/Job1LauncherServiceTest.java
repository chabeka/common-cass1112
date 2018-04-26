package fr.urssaf.image.commons.spring.batch.launcher;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-batch.xml",
      "/applicationContext-item.xml", "/applicationContext-jobs.xml",
      "/applicationContext-service-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class Job1LauncherServiceTest {

   @Autowired
   private Job1LauncherService launcherService;

   private static final File TMP_FILE;

   static {

      TMP_FILE = SystemUtils.getJavaIoTmpDir();

   }

   @Before
   public void before() throws IOException {

      File directory = new File(FilenameUtils.concat(
            TMP_FILE.getAbsolutePath(), "batch-exemple"));

      FileUtils.cleanDirectory(directory);
   }

   @Test
   public void launcher_success() {

      File xmlPath = new File("src/test/resources/data/bibliotheque.xml");
      File filePath = new File(FilenameUtils.concat(TMP_FILE.getAbsolutePath(),
            "batch-exemple/livres.txt"));

      String exitCode = launcherService.launch(xmlPath, filePath);

      Assert.assertEquals("la sortie du job est incorrecte", "COMPLETED",
            exitCode);
   }

   @Test
   public void launcher_failure() {

      File xmlPath = new File(
            "src/test/resources/data/bibliotheque_notfounds.xml");
      File filePath = new File(FilenameUtils.concat(TMP_FILE.getAbsolutePath(),
            "batch-exemple/livres.txt"));

      try {
         launcherService.launch(xmlPath, filePath);

         Assert.fail("le test doit échouer car '" + xmlPath
               + "' ne doit pas exister");

      } catch (IllegalArgumentException e) {

         Assert.assertEquals("le message de l'exception est incorrect", xmlPath
               .getAbsoluteFile()
               + " n'existe pas!", e.getMessage());

      }

   }

}
