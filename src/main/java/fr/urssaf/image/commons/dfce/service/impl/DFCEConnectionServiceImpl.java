package fr.urssaf.image.commons.dfce.service.impl;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;

import net.docubase.toolkit.service.ServiceProvider;
import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;

/**
 * Implémentation du service {@link DFCEConnectionService}
 * 
 * 
 */
public class DFCEConnectionServiceImpl implements DFCEConnectionService {

   private final DFCEConnection dfceConnection;

   /**
    * 
    * @param dfceConnection
    *           paramètres de la connexion à DFCE
    */
   public DFCEConnectionServiceImpl(DFCEConnection dfceConnection) {

      Validate.notNull(dfceConnection, "'dfceConnection' is required");

      this.dfceConnection = dfceConnection;

   }

   @Override
   public final ServiceProvider openConnection() {

      ServiceProvider dfceService = ServiceProvider.newServiceProvider();

      dfceService.connect(this.dfceConnection.getLogin(),
            this.dfceConnection.getPassword(),
            ObjectUtils.toString(this.dfceConnection.getServerUrl()),
            this.dfceConnection.getTimeout());

      return dfceService;
   }
}
