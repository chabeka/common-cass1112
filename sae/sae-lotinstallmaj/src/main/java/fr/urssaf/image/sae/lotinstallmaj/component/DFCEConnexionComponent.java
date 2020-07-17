package fr.urssaf.image.sae.lotinstallmaj.component;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.runtime.DFCERuntimeException;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.sae.admin.dfce.exploit.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.admin.dfce.exploit.messages.ResourceMessagesUtils;
import fr.urssaf.image.sae.admin.dfce.exploit.services.AbstractService;
import fr.urssaf.image.sae.admin.dfce.exploit.utils.BaseUtils;
import net.docubase.toolkit.service.ServiceProvider;

@Component
public class DFCEConnexionComponent extends AbstractService {

   @Autowired
   private DFCEConnection dfceConnexionParameter;

   /**
    * @return the dfceConnexionParameter
    */
   public DFCEConnection getDfceConnexionParameter() {
      return dfceConnexionParameter;
   }

   private static final Logger LOG = LoggerFactory.getLogger(DFCEConnexionComponent.class);

   ServiceProvider serviceProvider;

   /**
    * Tester la connexion en utilisant DFCEServices
    * 
    * @return
    */
   public boolean tester() {
      boolean connexionStatus = false;
      try {
         buildAndOpenSAEConnexion(dfceConnexionParameter);
         connexionStatus = true;
         LOG.info("Connexion OK à la webapp DFCE.");
      }
      catch (final ConnectionServiceEx e) {
         LOG.info("Problème de connexion à la webapp DFCE dans Tomcat... Veuillez verifier que la webapp est bien démarrée!");
      }
      finally {
         closeSAEConnection();
      }

      return connexionStatus;
   }

   /**
    * Tester la connexion à DFCE en passant par le provider car la base SAE-INT
    * n'est pas encore créée
    * 
    * @return
    */
   public boolean testerWithServiceProvider() throws Exception {
      // Connexion à DFCE, sans passer par dfceServices, car la base SAE n'existe pas encore
      serviceProvider = ServiceProvider.newServiceProvider();
      boolean connexionStatus = false;
      try {
         final String url = BaseUtils.buildUrlForConnection(dfceConnexionParameter);
         LOG.debug("TestConnexion au serveur DFCE {}", url);
         serviceProvider.connect(dfceConnexionParameter.getLogin(),
                                 dfceConnexionParameter.getPassword(),
                                 url,
                                 dfceConnexionParameter.getTimeout());
         LOG.debug("Connexion OK");
         connexionStatus = true;
      }
      catch (final MalformedURLException malURLException) {
         LOG.error("Bad Request : {}", ResourceMessagesUtils.loadMessage("url.connection.malformed"));
      }
      catch (final DFCERuntimeException ex) {
         LOG.error("Erreur de connexion détails : {}", ex.getMessage());
      }
      finally {
         // Deconnexion si on a une session active
         if (serviceProvider.isSessionActive()) {
            serviceProvider.disconnect();
         }
      }

      return connexionStatus;
   }

   /**
    * Connexion à DFCE
    */
   public void connectDfce() {
      serviceProvider = ServiceProvider.newServiceProvider();
      final String url = dfceConnexionParameter.getUrlToolkit();
      LOG.debug("TestConnexion au serveur DFCE {}", url);
      serviceProvider.connect(dfceConnexionParameter.getLogin(),
                              dfceConnexionParameter.getPassword(),
                              url,
                              dfceConnexionParameter.getTimeout());
      LOG.debug("Connexion OK");
   }

   /**
    * Déconnexion à DFCE
    */
   public void disconnectDfce() {
      serviceProvider.disconnect();
   }

   public ServiceProvider getServiceProvider() {
      return serviceProvider;
   }
}
