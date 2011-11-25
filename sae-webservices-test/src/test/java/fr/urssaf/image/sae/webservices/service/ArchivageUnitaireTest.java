package fr.urssaf.image.sae.webservices.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.EcdeManager;
import fr.urssaf.image.sae.webservices.configuration.SecurityConfiguration;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitaireResponseType;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModelFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;
import fr.urssaf.image.sae.webservices.util.AuthenticateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ArchivageUnitaireTest {

   private static final Logger LOG = Logger
         .getLogger(ArchivageUnitaireTest.class);

   @Autowired
   private ArchivageUnitaireService archivage;

   @Autowired
   private ConsultationService consultation;

   @BeforeClass
   public static void beforeClass() throws ConfigurationException, IOException {

      EcdeManager.cleanEcde();
   }

   @After
   public final void after() {

      SecurityConfiguration.cleanSecurityContext();
   }

   @Test
   public void archivageUnitaire_success() throws URISyntaxException,
         FileNotFoundException, IOException {

      AuthenticateUtils.authenticate("ROLE_TOUS");

      // enregistrement du fichier dans l'ECDE
      File srcFile = new File("src/test/resources/storage/attestation.pdf");
      EcdeManager.copyFile(srcFile,
            "DCL001/19991231/3/documents/attestation.pdf");

      // affichage du Hash du fichier
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));

      LOG
            .debug("le hash du document 'attestation.pdf' à archiver est: "
                  + hash);

      // appel du service archivage unitaire

      List<Metadata> metadatas = new ArrayList<Metadata>();

      metadatas.add(ObjectModelFactory.createMetadata("ApplicationProductrice",
            "ADELAIDE"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeProprietaire", "CER69"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(ObjectModelFactory.createMetadata("VersionRND", "11.1"));
      metadatas.add(ObjectModelFactory.createMetadata("NbPages", "2"));
      metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
            "fmt/1354"));
      metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
            "2012-01-01"));
      metadatas.add(ObjectModelFactory.createMetadata("Titre",
            "Attestation de vigilance"));
      metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
      metadatas.add(ObjectModelFactory.createMetadata("Hash", hash));
      metadatas.add(ObjectModelFactory.createMetadata("DateReception",
            "1999-11-25"));
      metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
            "2011-09-02"));

      URI urlEcde = URI
            .create("ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation.pdf");

      ArchivageUnitaireResponseType archivageResponse = archivage
            .archivageUnitaire(urlEcde, metadatas);

      // récupération de l'uuid d'archivage
      String idArchive = archivageResponse.getIdArchive().getUuidType();

      LOG.debug("UUID du document archivé: " + idArchive.toLowerCase());

      Assert.assertNotNull("L'identifiant d'archivage doit être renseigné",
            idArchive);

      // vérification du contenu du document et des métadonnées

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("Titre", "Attestation de vigilance");
      expectedMetadatas.put("DateCreation", "2012-01-01");
      expectedMetadatas.put("DateReception", "1999-11-25");
      expectedMetadatas.put("CodeOrganismeGestionnaire", "UR750");
      expectedMetadatas.put("CodeOrganismeProprietaire", "CER69");
      expectedMetadatas.put("CodeRND", "2.3.1.1.12");
      expectedMetadatas.put("NomFichier", "attestation.pdf");
      expectedMetadatas.put("FormatFichier", "fmt/1354");
      expectedMetadatas.put("ContratDeService", "ATT_PROD_001");
      expectedMetadatas.put("Hash", hash);
      expectedMetadatas.put("TailleFichier", Long.toString(FileUtils
            .sizeOf(srcFile)));

      ConsultationTest consultationTest = new ConsultationTest();

      consultationTest.consultation(consultation.consultation(idArchive),
            expectedMetadatas, srcFile);

   }
}
