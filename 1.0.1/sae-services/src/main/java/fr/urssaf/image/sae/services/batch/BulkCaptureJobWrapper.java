/**
 * 
 */
package fr.urssaf.image.sae.services.batch;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.ecde.service.EcdeServices;

/**
 * Wrapper pour les jobs de capture en masse.
 * 
 * @author Rhofir
 */
public class BulkCaptureJobWrapper implements Runnable {
   private static final Logger LOGGER = Logger
         .getLogger(BulkCaptureJobWrapper.class);
   private String ecdeUrl;
   private final EcdeServices ecdeServices;
   private final BulkCaptureJob bulkCaptureJob;

   /**
    * Constructeur.
    * 
    * @param urlEcde
    *           : Url du somaire.xml
    * @param ecdeServices
    *           : Le service ECDE.
    */
   public BulkCaptureJobWrapper(final String urlEcde,
         final EcdeServices ecdeServices, final BulkCaptureJob bulkCaptureJob) {
      this.ecdeUrl = urlEcde;
      this.ecdeServices = ecdeServices;
      this.bulkCaptureJob = bulkCaptureJob;
   }

   /**
    * @see java.lang.Runnable#run()
    */
   public final void run() {
      // Appeler le service ECDE de récupération du sommaire.xml à partir de
      // l'URL
      try {
   	  
         URI ecdeUri = new URI(ecdeUrl);
         Sommaire sommaireEcde = ecdeServices.fetchSommaireByUri(ecdeUri);
         // Début traitement par BOB
         Resultats resultatEcde = bulkCaptureJob.bulkCapture(sommaireEcde);
         // Fin traitement par BOB
         // Appeler le service ECDE de persistance du résultat
         ecdeServices.persistResultat(resultatEcde);
      } catch (URISyntaxException except) {
         LOGGER.error(except.getMessage());
      } catch (EcdeGeneralException except) {
         LOGGER.error(except.getMessage());
      } catch (IOException except) {
         LOGGER.error(except.getMessage());
      }
   }

   /**
    * @param ecdeUrl
    *           : URL du sommaire.
    */
   public final void setEcdeUrl(final String ecdeUrl) {
      this.ecdeUrl = ecdeUrl;
   }

   /**
    * @return URL du sommaire.
    */
   public final String getEcdeUrl() {
      return ecdeUrl;
   }

}
