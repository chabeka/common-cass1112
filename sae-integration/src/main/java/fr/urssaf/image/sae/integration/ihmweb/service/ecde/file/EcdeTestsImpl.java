/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.service.ecde.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTests;

/**
 * 
 * 
 */
public class EcdeTestsImpl implements EcdeTestInterface {

   /**
    * Chemin vers le fichier de cas de tests
    */
   private String filePath;

   /**
    * {@inheritDoc}
    */
   @Override
   public void generateFile(List<EcdeTest> ecdeTest) throws Exception {
      StaxDriver staxDriver = new StaxDriver();
      XStream xstream = new XStream(staxDriver);
      xstream.alias("test", EcdeTest.class);
      xstream.alias("tests", List.class);
      xstream.toXML(ecdeTest, new FileOutputStream(filePath));
   }

   /**
    * {@inheritDoc}
    * 
    * @throws Exception
    */
   @Override
   public EcdeTests load() throws Exception {
      try {
         // Désérialisation des objets EcdeSource via Xstream
         StaxDriver staxDriver = new StaxDriver();
         XStream xstream = new XStream(staxDriver);
         xstream.alias("test", EcdeTest.class);
         xstream.alias("tests", new EcdeTest[] {}.getClass());
         EcdeTests ecdeTests = new EcdeTests();
         EcdeTest tabEcde[] = (EcdeTest[]) xstream.fromXML(new FileInputStream(
               filePath));
         List<EcdeTest> list = Arrays.asList(tabEcde);
         ecdeTests.setListTests(list);

         return ecdeTests;

      } catch (FileNotFoundException e) {
         throw new Exception("Le fichier : " + filePath + "n'existe pas.", e);
      }
   }

   /**
    * @param filePath
    *           the filePath to set
    */
   public void setFilePath(String filePath) {
      this.filePath = filePath;
   }

}
