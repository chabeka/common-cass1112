package fr.urssaf.image.sae.webservices.cs;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.axiom.attachments.utils.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.RechercheResponseType;
import fr.urssaf.image.sae.webservices.security.SecurityConfiguration;
import fr.urssaf.image.sae.webservices.service.ArchivageUnitairePJService;
import fr.urssaf.image.sae.webservices.service.ConsultationService;
import fr.urssaf.image.sae.webservices.service.RechercheService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModelFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;

/**
 * Test du contrat de service d'accrochage pour CTC
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class CSPAJETest {

   private static final Logger LOG = LoggerFactory.getLogger(CSPAJETest.class);

   private static final String ISSUER = "CS_PAJE_ACCROCHAGE";

   private static final String PAGM_ARCH_UNI = "PAJE_SEPA_ARCHIVAGE_UNITAIRE";

   private static final String PAGM_CONS_RECH = "PAJE_SEPA_RECHERCHE_CONSULTATION";

   @Autowired
   private SecurityConfiguration securityConfiguration;

   private List<String> buildTousPagm() {
      List<String> pagms = new ArrayList<String>();
      pagms.add(PAGM_ARCH_UNI);
      pagms.add(PAGM_CONS_RECH);
      return pagms;
   }

   @Test
   @Ignore
   public void mandatSEPA_success() throws IOException {

      // Création du Stub d'accès au WS avec le bon CS
      SaeServiceStub stub = securityConfiguration.createSaeServiceStub(ISSUER,
            buildTousPagm());

      // Construction de la liste des métadonnées pour l'archivage
      List<Metadata> metadatas = new ArrayList<Metadata>();
      // méta liées au PRMD
      metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "1.2.2.4.12"));
      metadatas.add(ObjectModelFactory.createMetadata("ApplicationProductrice",
            "PAJE"));
      metadatas.add(ObjectModelFactory.createMetadata("ApplicationTraitement",
            "PAJE"));
      metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
            "fmt/354"));
      // autres méta liées à SEPA
      metadatas.add(ObjectModelFactory.createMetadata("Titre", "Mandat SEPA"));
      metadatas.add(ObjectModelFactory.createMetadata("RUM", "459796816"));
      // autres méta
      metadatas.add(ObjectModelFactory.createMetadata("Siren", "0123497841"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeProprietaire", "CER69"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeGestionnaire", "CER69"));
      metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
            "2012-11-20"));
      metadatas.add(ObjectModelFactory.createMetadata("Hash",
            "4bf2ddbd82d5fd38e821e6aae434ac989972a043"));
      metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
      metadatas.add(ObjectModelFactory.createMetadata("NbPages", "1"));

      // Appel du service d'archivage unitaire
      // Le test sera OK si pas de Soap fault levée
      ArchivageUnitairePJService archivageService = new ArchivageUnitairePJService(
            stub);
      String nomFichier = "mandat_sepa_fake.pdf";
      byte[] contenu = IOUtils.getStreamAsByteArray(new ClassPathResource(
            "storage/attestation.pdf").getInputStream());
      LOG.debug("Appel au service d'archivage unitaire");
      UUID idArchive = archivageService.archivageUnitairePJavecRetourUUID(
            nomFichier, contenu, metadatas);
      LOG.debug("UUID du document archivé: {}", idArchive);

      // Appel du service de consultation
      // Le test sera OK si pas de Soap fault levée
      ConsultationService consultationService = new ConsultationService(stub);
      LOG.debug("Appel au service de consultation");
      consultationService.consultation(idArchive.toString());
      LOG.debug("Le document a été retrouvé à la consultation");

      // Appel du service de recherche
      // Le test sera OK s'il y a au moins 1 résultat de recherche
      RechercheService rechercheService = new RechercheService(stub);
      LOG.debug("Appel au service de recherche");
      RechercheResponseType rechercheResponse = rechercheService
            .recherche("RUM:459796816");
      int nbResult = rechercheResponse.getResultats().getResultat().length;
      LOG.debug("Nombre de documents en résultats de recherche: {}", nbResult);
      assertTrue("La recherche n'a pas ramené de résultats", nbResult >= 1);

   }
}
