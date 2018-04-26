/**
 * 
 */
package fr.urssaf.image.sae.ecde;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;

/**
 * Classe d'implémentation du service EcdeSourceManager
 * 
 */
@SuppressWarnings("PMD.InstantiationToGetClass")
public class EcdeSourceManagerImpl implements EcdeSourceManager {

   /**
    * liste ecdeSources
    */
   private EcdeSources sources;
 
   /**
    * {@inheritDoc}
    */
   @Override
   public final EcdeSources load(File sourcesPath) throws EcdeBadFileException {
      try {
         // Désérialisation des objets EcdeSource via Xstream
         StaxDriver staxDriver = new StaxDriver();
         XStream xstream = new XStream(staxDriver);
         xstream.alias("source", EcdeSource.class);
         xstream.alias("sources", new EcdeSource[] {}.getClass());
         EcdeSources ecdeSources = new EcdeSources();
         ecdeSources.setSources((EcdeSource[]) xstream.fromXML(new FileInputStream(sourcesPath))); 

         return ecdeSources;
         
      } catch (FileNotFoundException e) {
         throw new EcdeBadFileException("Le fichier : " + sourcesPath.getAbsolutePath() + "n'existe pas.", e);
      }
   }
   
   /**
    * @return the sources
    */
   public final EcdeSources getSources() {
      return sources;
   }

   /**
    * @param sources the sources to set
    */
   public final void setSources(EcdeSources sources) {
      this.sources = sources;
   }
}
