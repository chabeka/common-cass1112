package fr.urssaf.image.sae.igc.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.service.IgcConfigService;

/**
 * Classe d'implémentation de {@link IgcConfigService}
 */
@Service
public final class IgcConfigServiceImpl implements IgcConfigService {

   /**
    * {@inheritDoc}
    */
   @Override
   public IgcConfigs loadConfig(String pathConfigFile)
         throws IgcConfigException {

      InputStream inputStream;

      try {
         inputStream = new FileInputStream(pathConfigFile);
      } catch (FileNotFoundException e) {
         throw new IgcConfigException(e);
      }

      return loadConfig(inputStream);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IgcConfigs loadConfig(Resource configFile) throws IgcConfigException {

      InputStream inputStream;

      try {
         inputStream = configFile.getInputStream();
      } catch (IOException e) {
         throw new IgcConfigException(e);
      }

      return loadConfig(inputStream);

   }

   private IgcConfigs loadConfig(InputStream inputStream)
         throws IgcConfigException {

      final XStream xstream = new XStream();
      xstream.processAnnotations(IgcConfigs.class);
      IgcConfigs configs;

      try {
         configs = IgcConfigs.class.cast(xstream.fromXML(inputStream));
      } catch (Exception e) {
         throw new IgcConfigException(e);
      } finally {
         if (inputStream != null) {
            try {
               inputStream.close();
            } catch (IOException e) {
               // nothing to do
            }
         }
      }

      return configs;

   }

}
