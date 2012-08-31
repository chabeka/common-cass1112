package fr.urssaf.image.sae.igc.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.service.IgcConfigService;

/**
 * classe d'implémentation de {@link IgcConfigService}
 * 
 * 
 */
public class IgcConfigServiceImpl implements IgcConfigService {

   @Override
   public final IgcConfigs loadConfig(String pathConfigFile)
         throws IgcConfigException {

      final XStream xstream = new XStream();
      xstream.processAnnotations(IgcConfigs.class);
      FileInputStream stream = null;
      IgcConfigs configs;

      try {
         stream = new FileInputStream(pathConfigFile);
         configs = IgcConfigs.class.cast(xstream.fromXML(stream));
      } catch (FileNotFoundException e) {
         throw new IgcConfigException(e);
      } catch (Exception e) {
         throw new IgcConfigException(e);
      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               // nothing to do 
            }
         }
      }
      

      return configs;

   }
}
