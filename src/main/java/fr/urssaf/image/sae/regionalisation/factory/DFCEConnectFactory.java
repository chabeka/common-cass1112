package fr.urssaf.image.sae.regionalisation.factory;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.Resource;

import fr.urssaf.image.commons.dfce.manager.DFCEConnectionFactory;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.util.PropertiesUtils;
import fr.urssaf.image.sae.regionalisation.exception.ErreurTechniqueException;

/**
 * Classe d'implémentation du paramétrage de DFCE
 * 
 * 
 */
public final class DFCEConnectFactory {
   
   private DFCEConnectFactory(){
      
   }

   /**
    * 
    * @param dfceConfiguration
    *           chemin complet du fichier de configuration de DFCE
    * @return configuration DFCE
    */
   public static DFCEConnection createDFCEConnectionByDFCEConfiguration(
         Resource dfceConfiguration) {

      Properties dfceProperties;
      try {
         dfceProperties = PropertiesUtils.load(dfceConfiguration.getFile());
      } catch (IOException e) {
         throw new ErreurTechniqueException(e);
      }

      DFCEConnection dfceConnection = DFCEConnectionFactory
            .createDFCEConnectionByDFCEConfiguration(dfceProperties);

      return dfceConnection;

   }
}
