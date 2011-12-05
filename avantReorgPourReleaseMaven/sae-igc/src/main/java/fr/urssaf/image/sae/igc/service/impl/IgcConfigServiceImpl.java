package fr.urssaf.image.sae.igc.service.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertyConverter;
import org.apache.commons.configuration.XMLConfiguration;

import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.modele.IgcConfig;
import fr.urssaf.image.sae.igc.service.IgcConfigService;

/**
 * classe d'implémentation de {@link IgcConfigService}
 * 
 * 
 */
public class IgcConfigServiceImpl implements IgcConfigService {

   @Override
   public final IgcConfig loadConfig(String pathConfigFile)
         throws IgcConfigException {

      try {

         XMLConfiguration config = new XMLConfiguration(pathConfigFile);
         IgcConfig igcConfig = this.loadConfig(config);

         return igcConfig;
      } catch (ConversionException e) {
         throw new IgcConfigException(e);
      } catch (ConfigurationException e) {
         throw new IgcConfigException(e);
      }

   }

   /**
    * Renvoie la configuration des éléments de l'IGC
    * 
    * @param config
    *           classe de configuration de l'IGC
    * @return Configuration des éléments de l'IGC
    */
   public final IgcConfig loadConfig(XMLConfiguration config) {

      IgcConfig igcConfig = new IgcConfig();
     
      String acRacines = config.getString("repertoireACRacines");
      igcConfig.setRepertoireACRacines(acRacines);
      String crlsRepertory = config.getString("repertoireCRL");
      igcConfig.setRepertoireCRLs(crlsRepertory);

      List<URL> urls = new ArrayList<URL>();

      for (Object value : config.getList("URLTelechargementCRL.url")) {

         URL url = PropertyConverter.toURL(value);
         urls.add(url);

      }

      igcConfig.setUrlsTelechargementCRLs(urls);

      return igcConfig;

   }
}
