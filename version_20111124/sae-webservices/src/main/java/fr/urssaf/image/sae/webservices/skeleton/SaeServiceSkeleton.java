/**
 * SaeServiceSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 * 
 * Le fichier est ensuite mis à jour manuellement lors de l'évolution du WSDL
 * 
 */
package fr.urssaf.image.sae.webservices.skeleton;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.PingRequest;
import fr.cirtil.www.saeservice.PingResponse;
import fr.cirtil.www.saeservice.PingSecureRequest;
import fr.cirtil.www.saeservice.PingSecureResponse;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.urssaf.image.sae.webservices.SaeService;
import fr.urssaf.image.sae.webservices.SaeStorageService;
import fr.urssaf.image.sae.webservices.exception.CaptureAxisFault;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;
import fr.urssaf.image.sae.webservices.service.WSCaptureService;
import fr.urssaf.image.sae.webservices.service.WSConsultationService;
import fr.urssaf.image.sae.webservices.service.WSRechercheService;
import fr.urssaf.image.sae.webservices.service.impl.WSCaptureServiceImpl;

/**
 * Skeleton du web service coté serveur<br>
 * La configuration dans <code>services.xml</code>
 * 
 * <pre>
 * 
 * &lt;service name="SaeService">
 *       &lt;messageReceivers>
 *          &lt;messageReceiver mep="http://www.w3.org/ns/wsdl/in-out"
 *             class="fr.urssaf.image.sae.webservices.skeleton.SaeServiceMessageReceiverInOut" />
 *       &lt;/messageReceivers>
 *       &lt;parameter name="ServiceObjectSupplier">org.apache.axis2.extensions.spring.receivers.SpringAppContextAwareObjectSupplier
 *       &lt;/parameter>
 *       &lt;parameter name="SpringBeanName" locked="false">saeServiceSkeleton
 *       &lt;/parameter>
 *       &lt;parameter name="useOriginalwsdl">true&lt;/parameter>
 *       &lt;parameter name="modifyUserWSDLPortAddress">false&lt;/parameter>
 *       &lt;operation name="Ping" mep="http://www.w3.org/ns/wsdl/in-out"
 *          namespace="http://www.cirtil.fr/saeService">
 *          &lt;actionMapping>Ping&lt;/actionMapping>
 *          &lt;outputActionMapping>Ping&lt;/outputActionMapping>
 *       &lt;/operation>
 *       &lt;operation name="PingSecure" mep="http://www.w3.org/ns/wsdl/in-out" namespace="http://www.cirtil.fr/saeService">
 *             &lt;actionMapping>PingSecure&lt;/actionMapping>
 *             &lt;outputActionMapping>PingSecure&lt;/outputActionMapping>
 *         &lt;/operation>
 *    &lt;/service>
 * 
 * </pre>
 * 
 * Code généré à partir du plugin maven
 * <code>axis2-wsdl2code-maven-plugin</code><br>
 * <br>
 * Il est nécessaire d'injecter dans les endpoints générés l'implémentation des
 * services
 * 
 */
@Component
public class SaeServiceSkeleton implements SaeServiceSkeletonInterface {

   private final SaeService service;
   private static final Logger LOG = LoggerFactory
         .getLogger(WSCaptureServiceImpl.class);

   private final SaeStorageService storageService;

   @Autowired
   private WSConsultationService consultation;

   @Autowired
   private WSRechercheService search;
   @Autowired
   private WSCaptureService capture;

   /**
    * Instanciation du service {@link SaeService}
    * 
    * @param service
    *           implémentation des services web
    * @param storageService
    *           implémentation de {@link SaeStorageService}
    */
   @Autowired
   public SaeServiceSkeleton(SaeService service,
         SaeStorageService storageService) {

      Assert.notNull(service, "service is required");
      Assert.notNull(storageService, "storageService is required");

      this.service = service;
      this.storageService = storageService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final PingResponse ping(PingRequest pingRequest) {

      PingResponse response = new PingResponse();

      response.setPingString(service.ping());

      return response;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final PingSecureResponse pingSecure(PingSecureRequest pingRequest) {

      PingSecureResponse response = new PingSecureResponse();

      response.setPingString(service.pingSecure());

      return response;
   }

   /**
    * {@inheritDoc}
    * 
    * @throws CaptureAxisFault
    */
   @Override
   public final ArchivageUnitaireResponse archivageUnitaireSecure(
         ArchivageUnitaire request) throws CaptureAxisFault {
      // Mise en place du contexte pour les traces
      buildLogContext();
      try {
         // Traces debug - entrée méthode
         String prefixeTrc = "Opération archivageUnitaireSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode
         ArchivageUnitaireResponse response = capture
               .archivageUnitaire(request);
         // Traces debug - sortie méthode
         if (response != null
               && response.getArchivageUnitaireResponse() != null) {
            LOG.debug("{} - Valeur de retour : \"{}\"", prefixeTrc, response
                  .getArchivageUnitaireResponse().getIdArchive());
         } else {
            LOG.debug("{} - Valeur de retour : null", prefixeTrc);
         }
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode
         return response;
      } finally {
         // Nettoyage du contexte pour les logs
         clearLogContext();
      }
   }

   /**
    * {@inheritDoc}
    * 
    * @throws CaptureAxisFault
    */
   @Override
   public final ArchivageMasseResponse archivageMasseSecure(
         ArchivageMasse request) throws CaptureAxisFault {
      // Mise en place du contexte pour les traces
      buildLogContext();
      try {
         // Traces debug - entrée méthode
         String prefixeTrc = "Opération archivageMasseSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode
         ArchivageMasseResponse response = capture.archivageEnMasse(request);
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode
         return response;
      } finally {
         // Nettoyage du contexte pour les logs
         clearLogContext();
      }
   }

   /**
    * {@inheritDoc}
    * 
    * @throws RechercheAxis2Fault
    */
   @Override
   public final RechercheResponse rechercheSecure(Recherche request)
         throws RechercheAxis2Fault {
      // Mise en place du contexte pour les traces
      buildLogContext();
      try {
         // Traces debug - entrée méthode
         String prefixeTrc = "Opération rechercheSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode
         RechercheResponse response = search.search(request);
         // Traces debug - sortie méthode
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode
         return response;
      } finally {
         // Nettoyage du contexte pour les logs
         clearLogContext();
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ConsultationResponse consultationSecure(Consultation request)
         throws ConsultationAxisFault {
      buildLogContext();
      try {
         // Traces debug - entrée méthode
         String prefixeTrc = "Opération consultationSecure()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode
         ConsultationResponse response = consultation.consultation(request);
         LOG.debug("{} - Sortie", prefixeTrc);
         // Fin des traces debug - sortie méthode
         return response;
      } finally {
         // Nettoyage du contexte pour les logs
         clearLogContext();
      }
   }

   /**
    * Mise en place du Log contexte.
    */
   private void buildLogContext() {
      String contexteLog = UUID.randomUUID().toString();
      MDC.put("log_contexte_uuid", contexteLog);
   }

   /**
    * Nettoie le contexte pour le logback.
    */
   private void clearLogContext() {
      MDC.clear();
   }
}
