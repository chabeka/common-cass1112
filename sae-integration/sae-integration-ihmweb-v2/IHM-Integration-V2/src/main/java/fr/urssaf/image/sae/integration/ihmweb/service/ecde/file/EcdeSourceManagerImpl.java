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

import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSource;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSources;

/**
 * Classe d'implémentation du service EcdeSourceManager
 * 
 */
@SuppressWarnings("PMD.InstantiationToGetClass")
public class EcdeSourceManagerImpl implements EcdeSourceManager {

   private String filePath;

   /**
    * {@inheritDoc}
    */
   @Override
   public final EcdeSources load() throws Exception {
      try {
         // Désérialisation des objets EcdeSource via Xstream
         StaxDriver staxDriver = new StaxDriver();
         XStream xstream = new XStream(staxDriver);
         xstream.alias("source", EcdeSource.class);
         xstream.alias("sources", new EcdeSource[] {}.getClass());
         EcdeSources ecdeSources = new EcdeSources();
         EcdeSource tabEcde[] = (EcdeSource[]) xstream
               .fromXML(new FileInputStream(filePath));
         List<EcdeSource> list = Arrays.asList(tabEcde);
         ecdeSources.setSources(list);

         return ecdeSources;

      } catch (FileNotFoundException e) {
         throw new Exception("Le fichier : " + filePath + "n'existe pas.", e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void generate(List<EcdeSource> sources) throws Exception {
      StaxDriver staxDriver = new StaxDriver();
      XStream xstream = new XStream(staxDriver);
      xstream.alias("source", EcdeSource.class);
      xstream.alias("sources", List.class);
      xstream.toXML(sources, new FileOutputStream(filePath));

   }

   /**
    * @return the filePath
    */
   public String getFilePath() {
      return filePath;
   }

   /**
    * @param filePath
    *           the filePath to set
    */
   public void setFilePath(String filePath) {
      this.filePath = filePath;
   }
}
