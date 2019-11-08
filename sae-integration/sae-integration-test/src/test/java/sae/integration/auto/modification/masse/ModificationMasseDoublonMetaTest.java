package sae.integration.auto.modification.masse;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.job.JobManager;
import sae.integration.util.ArchivageUtils;
import sae.integration.util.ArchivageValidationUtils;
import sae.integration.util.CleanHelper;
import sae.integration.util.ModificationSommaireBuilder;
import sae.integration.util.SoapBuilder;
import sae.integration.util.XMLHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.xml.modele.NonIntegratedDocumentType;
import sae.integration.xml.modele.ResultatsType;

/**
 * Test un traitement de modification de masse en passant des métas en doublon
 */
public class ModificationMasseDoublonMetaTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ModificationMasseDoublonMetaTest.class);


   @Test
   /**
    * On archive 3 documents en GNT.
    * On crée un sommaire avec 3 documents à modifier.
    * Sur le 2ème document, on tente de modifier Denomination plusieurs fois
    * Sur le 3ème document, on tente de modifier et supprimer Denomination
    * On vérifie que seul le 1er document est modifié
    * 
    * @throws Exception
    */
   public void doublonMeta() throws Exception {
      final Environment environnementGNT = Environments.GNT_PIC;
      final SaeServicePortType gntService = SaeServiceStubFactory.getServiceForDevToutesActions(environnementGNT.getUrl());

      // Archivage de 3 documents en GNT
      final List<ListeMetadonneeType> metasList = new ArrayList<>();
      final List<String> uuids = new ArrayList<>();
      for (int i = 0; i < 3; i++) {
         final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
         final ListeMetadonneeType metas = RandomData.getRandomMetadatas();
         request.setMetadonnees(metas);
         request.setDataFile(TestData.getTxtFile(metas));
         metasList.add(metas);
         // Lancement de l'archivage
         uuids.add(ArchivageUtils.sendArchivageUnitaire(gntService, request));
      }

      // Création du sommaire de modification de masse
      final ModificationSommaireBuilder builder = new ModificationSommaireBuilder();
      // Pour le doc 0 : on modifie le Siren et la dénomination
      final ListeMetadonneeType modifiedMetas0 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas0, "Siren", "123456789");
      SoapBuilder.addMeta(modifiedMetas0, "Denomination", "titi");
      builder.addDocument(uuids.get(0), modifiedMetas0);

      // Pour le doc 1 : on tente de modifier plusieurs fois la dénomination
      final ListeMetadonneeType modifiedMetas1 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas1, "Denomination", "titi");
      SoapBuilder.addMeta(modifiedMetas1, "Siren", "123456789");
      SoapBuilder.addMeta(modifiedMetas1, "Denomination", "toto");
      builder.addDocument(uuids.get(1), modifiedMetas1);

      // Pour le doc 2 : on modifier et supprimer la dénomination
      final ListeMetadonneeType modifiedMetas2 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas2, "Denomination", "test");
      SoapBuilder.addMeta(modifiedMetas2, "Denomination", "");
      builder.addDocument(uuids.get(2), modifiedMetas2);
      final String sommaireContent = builder.build();

      try (final JobManager job = new JobManager(environnementGNT)) {
         // Préparation et lancement du job
         final UUID jobId = job.getJobId();
         LOGGER.info("Lancement du job {}", jobId);
         job.launchModificationMasse(sommaireContent);

         // Récupération du log du traitement et du resultats.xml pour debug
         final String log = job.getJobLog();
         LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", log);
         final String resultatsXML = job.getResultatsXML();
         LOGGER.debug("Contenu du fichier resultats.xml :\r\n {}\r\n", resultatsXML);

         // On vérifie que le document 0 a bien été modifié
         LOGGER.info("Validation du document {}", uuids.get(0));
         final ListeMetadonneeType finalMetas0 = metasList.get(0);
         SoapBuilder.setMetaValue(finalMetas0, "Siren", "123456789");
         SoapBuilder.setMetaValue(finalMetas0, "Denomination", "titi");
         ArchivageValidationUtils.validateDocument(gntService, uuids.get(0), finalMetas0);

         // On vérifie que le document 1 n'a pas été modifié
         LOGGER.info("Validation du document {}", uuids.get(1));
         final ListeMetadonneeType finalMetas1 = metasList.get(1);
         ArchivageValidationUtils.validateDocument(gntService, uuids.get(1), finalMetas1);

         // On vérifie que le document 2 n'a pas été modifié
         LOGGER.info("Validation du document {}", uuids.get(2));
         final ListeMetadonneeType finalMetas2 = metasList.get(2);
         ArchivageValidationUtils.validateDocument(gntService, uuids.get(2), finalMetas2);

         // Vérification du contenu du fichier resultats.xml
         final ResultatsType resultat = XMLHelper.parseResultatsXML(resultatsXML);
         Assert.assertEquals(3, (int) resultat.getInitialDocumentsCount());
         Assert.assertEquals(1, (int) resultat.getIntegratedDocumentsCount());

         final NonIntegratedDocumentType nonIntegratedDocument1 = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(0);
         Assert.assertEquals(uuids.get(1), nonIntegratedDocument1.getObjetNumerique().getUUID());
         final String libErreur = nonIntegratedDocument1.getErreurs().getErreur().get(0).getLibelle();
         assertThat(libErreur, containsString("La ou les métadonnées suivantes sont renseignées plusieurs fois : Denomination"));

         final NonIntegratedDocumentType nonIntegratedDocument2 = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(1);
         Assert.assertEquals(uuids.get(2), nonIntegratedDocument2.getObjetNumerique().getUUID());
         final String libErreur2 = nonIntegratedDocument2.getErreurs().getErreur().get(0).getLibelle();
         assertThat(libErreur2, containsString("La ou les métadonnées suivantes sont renseignées plusieurs fois : Denomination"));

      }
      finally {
         LOGGER.info("Suppression des documents");
         for (int i = 0; i < 3; i++) {
            CleanHelper.deleteOneDocument(gntService, uuids.get(i));
         }
      }
   }


}
