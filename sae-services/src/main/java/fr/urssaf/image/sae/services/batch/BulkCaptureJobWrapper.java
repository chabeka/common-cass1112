package fr.urssaf.image.sae.services.batch;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.Assert;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.ecde.service.EcdeServices;
import fr.urssaf.image.sae.services.jmx.CommonIndicator;
import fr.urssaf.image.sae.services.util.FormatUtils;
import fr.urssaf.image.sae.storage.model.jmx.BulkProgress;
import fr.urssaf.image.sae.storage.model.jmx.JmxIndicator;

/**
 * Wrapper pour les jobs de capture en masse.
 * 
 * @author Rhofir
 */
@Deprecated
public class BulkCaptureJobWrapper extends CommonIndicator {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(BulkCaptureJobWrapper.class);
   private String ecdeUrl;
   private final EcdeServices ecdeServices;
   private final BulkCaptureJob bulkCaptureJob;
   private String logContexteUUID;

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
    * Méthode d'exécution du traitement d'archivage en masse
    */
   public final void execute() {
      // Appeler le service ECDE de récupération du sommaire.xml à partir de
      // l'URL
      try {
         MDC.put("log_contexte_uuid", getLogContexteUUID());
         Assert.notNull(getLogContexteUUID(),
               "L'indentifiant du traiement en masse doit être non null.");
         String prefixeTrc = "run()";
         final String debutFlag = "debut_traitement.flag";
         LOGGER.debug("{} - Début", prefixeTrc);
         getIndicator().setJmxTreatmentState(BulkProgress.BEGIN_OF_ARCHIVE);
         bulkCaptureJob.setIndicator(getIndicator());
         URI ecdeUri = new URI(ecdeUrl);
         getIndicator().setJmxTreatmentState(BulkProgress.READING_DOCUMENTS);
         // Ajout d'un fichier début traitement debut_traitement.flag
         // Ajout du host name du serveur qui a pris le traitement
         // UUID du traitement en masse
         LOGGER.debug("{} - Début de création du fichier ({})", prefixeTrc,
               debutFlag);
         StringBuffer urlFlag = new StringBuffer(ecdeServices
               .convertSommaireToFile(ecdeUri).getParent());
         urlFlag.append("/");
         urlFlag.append(debutFlag);
         LOGGER.debug("{} - Chemin du du fichier ({}) : ({})", new Object[] {
               prefixeTrc, debutFlag, urlFlag.toString() });
         File bulkStartFile = new File(urlFlag.toString());
         Collection<String> bulkCaptureInfos = new ArrayList<String>();
         InetAddress hostInfo = InetAddress.getLocalHost();
         bulkCaptureInfos.add("idTraitementMasse=" + getLogContexteUUID());
         bulkCaptureInfos.add("heureDebutTraitementEnMasse="
               + FormatUtils.dateToString(new Date()));
         bulkCaptureInfos.add("hostnameServeurAppli=" + hostInfo.getHostName());
         bulkCaptureInfos.add("hostIP=" + hostInfo.getHostAddress());
         FileUtils.writeLines(bulkStartFile, bulkCaptureInfos, "\r");
         LOGGER.debug("{} - Fin de création du fichier ({})", prefixeTrc,
               debutFlag);
         Sommaire sommaireEcde = ecdeServices.fetchSommaireByUri(ecdeUri);
         getIndicator().setJmxExternalIdTreatment(
               retrieveExternalTreatmentId(sommaireEcde));
         Resultats resultatEcde = bulkCaptureJob.bulkCapture(sommaireEcde,
               getLogContexteUUID());
         // Appeler le service ECDE de persistance du résultat
         getIndicator().setJmxTreatmentState(
               BulkProgress.GENERATION_RESULT_FILE);
         ecdeServices.persistResultat(resultatEcde);
         getIndicator().setJmxTreatmentState(BulkProgress.END_OF_ARCHIVE);

         LOGGER.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode
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

   /**
    * @return l'indicateur du job de l'archivage.
    */
   public JmxIndicator retrieveJmxBulkCaptureJobWrapperIndicator() {

      return bulkCaptureJob.retrieveJmxBulkCaptureJobIndicator();
   }

   /**
    * Permet de retourner l'identifiant du traitement de l'utilisateur.
    * 
    * @param sommaireEcde
    *           : le sommaire
    * @return L'identifiant du traitement de l'utilisateur.
    */
   public String retrieveExternalTreatmentId(final Sommaire sommaireEcde) {
      String id = BulkProgress.NO_TREATMENT_ID_FOUND.toString();
      if (sommaireEcde != null && sommaireEcde.getDocuments().size() != 0) {
         UntypedDocument doc = sommaireEcde.getDocuments().get(0);
         if (doc != null && doc.getUMetadatas() != null) {
            for (UntypedMetadata metadata : doc.getUMetadatas()) {
               if (metadata.getLongCode().trim().equals("IdTraitementMasse")) {
                  id = metadata.getValue();
                  break;
               }
            }
         }
      }
      return id;
   }

   /**
    * @return L'identifiant du contexte logbak.
    */
   public String getLogContexteUUID() {
      return logContexteUUID;
   }

   /**
    * 
    * @param logContexteUUID
    *           : L'identifiant du contexte logbak.
    */
   public void setLogContexteUUID(String logContexteUUID) {
      this.logContexteUUID = logContexteUUID;
   }

}
