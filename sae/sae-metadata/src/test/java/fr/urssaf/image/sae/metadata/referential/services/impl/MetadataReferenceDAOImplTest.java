package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.io.IOException;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;

/**
 * Contient les tests sur les services de manipulation du referentiel des
 * métadonnées.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
@SuppressWarnings("PMD")
public class MetadataReferenceDAOImplTest {

   @Autowired
   private CassandraServerBean server;

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   @Before
   public void before() throws Exception {
      server.resetData();
   }

   @After
   public void after() throws Exception {
      server.resetData();
   }

   /**
    * Permet de tester la récupération des métadonnées du référentiel.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getAllMetadataReferences() throws IOException,
         ReferentialException {

      Map<String, MetadataReference> metadonnees = referenceDAO
            .getAllMetadataReferences();

      Assert.assertEquals("Le nombre de métadonnées attendues est incorrect",
            61, metadonnees.size());

      String metaNonTrouve = "Métadonnéee %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "Titre"), metadonnees
            .containsKey("Titre"));
      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siren"), metadonnees
            .containsKey("Siren"));
      Assert.assertTrue(String.format(metaNonTrouve, "NniEmployeur"),
            metadonnees.containsKey("NniEmployeur"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroPersonne"),
            metadonnees.containsKey("NumeroPersonne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Denomination"),
            metadonnees.containsKey("Denomination"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeCategorieV2"),
            metadonnees.containsKey("CodeCategorieV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeSousCategorieV2"),
            metadonnees.containsKey("CodeSousCategorieV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteInterne"),
            metadonnees.containsKey("NumeroCompteInterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteExterne"),
            metadonnees.containsKey("NumeroCompteExterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siret"), metadonnees
            .containsKey("Siret"));
      Assert.assertTrue(String.format(metaNonTrouve, "PseudoSiret"),
            metadonnees.containsKey("PseudoSiret"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroStructure"),
            metadonnees.containsKey("NumeroStructure"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroRecours"),
            metadonnees.containsKey("NumeroRecours"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroIntControle"),
            metadonnees.containsKey("NumeroIntControle"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCreation"),
            metadonnees.containsKey("DateCreation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateReception"),
            metadonnees.containsKey("DateReception"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationProductrice"),
            metadonnees.containsKey("ApplicationProductrice"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationTraitement"),
            metadonnees.containsKey("ApplicationTraitement"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeProprietaire"), metadonnees
            .containsKey("CodeOrganismeProprietaire"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeGestionnaire"), metadonnees
            .containsKey("CodeOrganismeGestionnaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "SiteAcquisition"),
            metadonnees.containsKey("SiteAcquisition"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "VersionRND"), metadonnees
            .containsKey("VersionRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeFonction"),
            metadonnees.containsKey("CodeFonction"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeActivite"),
            metadonnees.containsKey("CodeActivite"));
      Assert.assertTrue(String.format(metaNonTrouve, "DureeConservation"),
            metadonnees.containsKey("DureeConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateFinConservation"),
            metadonnees.containsKey("DateFinConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "Gel"), metadonnees
            .containsKey("Gel"));
      Assert.assertTrue(
            String.format(metaNonTrouve, "TracabilitePreArchivage"),
            metadonnees.containsKey("TracabilitePreArchivage"));
      Assert.assertTrue(String
            .format(metaNonTrouve, "TracabilitePostArchivage"), metadonnees
            .containsKey("TracabilitePostArchivage"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "TypeHash"), metadonnees
            .containsKey("TypeHash"));
      Assert.assertTrue(String.format(metaNonTrouve, "NbPages"), metadonnees
            .containsKey("NbPages"));
      Assert.assertTrue(String.format(metaNonTrouve, "NomFichier"), metadonnees
            .containsKey("NomFichier"));
      Assert.assertTrue(String.format(metaNonTrouve, "FormatFichier"),
            metadonnees.containsKey("FormatFichier"));
      Assert.assertTrue(String.format(metaNonTrouve, "TailleFichier"),
            metadonnees.containsKey("TailleFichier"));
      Assert.assertTrue(String
            .format(metaNonTrouve, "IdTraitementMasseInterne"), metadonnees
            .containsKey("IdTraitementMasseInterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "IdTraitementMasse"),
            metadonnees.containsKey("IdTraitementMasse"));
      Assert.assertTrue(String.format(metaNonTrouve, "DocumentVirtuel"),
            metadonnees.containsKey("DocumentVirtuel"));
      Assert.assertTrue(String.format(metaNonTrouve, "StartPage"), metadonnees
            .containsKey("StartPage"));
      Assert.assertTrue(String.format(metaNonTrouve, "EndPage"), metadonnees
            .containsKey("EndPage"));
      Assert.assertTrue(String.format(metaNonTrouve, "ContratDeService"),
            metadonnees.containsKey("ContratDeService"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateArchivage"),
            metadonnees.containsKey("DateArchivage"));
      Assert.assertTrue(String.format(metaNonTrouve, "VersionNumber"),
            metadonnees.containsKey("VersionNumber"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateModification"),
            metadonnees.containsKey("DateModification"));
      Assert.assertTrue(String.format(metaNonTrouve, "JetonDePreuve"),
            metadonnees.containsKey("JetonDePreuve"));
      Assert.assertTrue(String.format(metaNonTrouve, "RUM"), metadonnees
            .containsKey("RUM"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateSignature"),
            metadonnees.containsKey("DateSignature"));
      Assert.assertTrue(String.format(metaNonTrouve, "ReferenceDocumentaire"),
            metadonnees.containsKey("ReferenceDocumentaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCourrierV2"),
            metadonnees.containsKey("DateCourrierV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "RIBA"), metadonnees
            .containsKey("RIBA"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeProduitV2"),
            metadonnees.containsKey("CodeProduitV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeTraitementV2"),
            metadonnees.containsKey("CodeTraitementV2"));

   }

   /**
    * Permet de tester la récupération des métadonnées consultables du
    * référentiel.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getConsultableMetadataReferences() throws IOException,
         ReferentialException {

      Map<String, MetadataReference> metadonnees = referenceDAO
            .getConsultableMetadataReferences();

      Assert.assertEquals(
            "Le nombre de métadonnées consultables attendues est incorrect",
            56, metadonnees.size());

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isConsultable());
      }

      String metaNonTrouve = "Métadonnéee consultable %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "Titre"), metadonnees
            .containsKey("Titre"));
      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siren"), metadonnees
            .containsKey("Siren"));
      Assert.assertTrue(String.format(metaNonTrouve, "NniEmployeur"),
            metadonnees.containsKey("NniEmployeur"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroPersonne"),
            metadonnees.containsKey("NumeroPersonne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Denomination"),
            metadonnees.containsKey("Denomination"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeCategorieV2"),
            metadonnees.containsKey("CodeCategorieV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeSousCategorieV2"),
            metadonnees.containsKey("CodeSousCategorieV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteInterne"),
            metadonnees.containsKey("NumeroCompteInterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteExterne"),
            metadonnees.containsKey("NumeroCompteExterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siret"), metadonnees
            .containsKey("Siret"));
      Assert.assertTrue(String.format(metaNonTrouve, "PseudoSiret"),
            metadonnees.containsKey("PseudoSiret"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroStructure"),
            metadonnees.containsKey("NumeroStructure"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroRecours"),
            metadonnees.containsKey("NumeroRecours"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroIntControle"),
            metadonnees.containsKey("NumeroIntControle"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCreation"),
            metadonnees.containsKey("DateCreation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateReception"),
            metadonnees.containsKey("DateReception"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationProductrice"),
            metadonnees.containsKey("ApplicationProductrice"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationTraitement"),
            metadonnees.containsKey("ApplicationTraitement"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeProprietaire"), metadonnees
            .containsKey("CodeOrganismeProprietaire"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeGestionnaire"), metadonnees
            .containsKey("CodeOrganismeGestionnaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "SiteAcquisition"),
            metadonnees.containsKey("SiteAcquisition"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "VersionRND"), metadonnees
            .containsKey("VersionRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeFonction"),
            metadonnees.containsKey("CodeFonction"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeActivite"),
            metadonnees.containsKey("CodeActivite"));
      Assert.assertTrue(String.format(metaNonTrouve, "DureeConservation"),
            metadonnees.containsKey("DureeConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateFinConservation"),
            metadonnees.containsKey("DateFinConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "Gel"), metadonnees
            .containsKey("Gel"));
      Assert.assertTrue(
            String.format(metaNonTrouve, "TracabilitePreArchivage"),
            metadonnees.containsKey("TracabilitePreArchivage"));
      Assert.assertTrue(String
            .format(metaNonTrouve, "TracabilitePostArchivage"), metadonnees
            .containsKey("TracabilitePostArchivage"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "TypeHash"), metadonnees
            .containsKey("TypeHash"));
      Assert.assertTrue(String.format(metaNonTrouve, "NbPages"), metadonnees
            .containsKey("NbPages"));
      Assert.assertTrue(String.format(metaNonTrouve, "NomFichier"), metadonnees
            .containsKey("NomFichier"));
      Assert.assertTrue(String.format(metaNonTrouve, "FormatFichier"),
            metadonnees.containsKey("FormatFichier"));
      Assert.assertTrue(String.format(metaNonTrouve, "TailleFichier"),
            metadonnees.containsKey("TailleFichier"));
      Assert.assertTrue(String
            .format(metaNonTrouve, "IdTraitementMasseInterne"), metadonnees
            .containsKey("IdTraitementMasseInterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "IdTraitementMasse"),
            metadonnees.containsKey("IdTraitementMasse"));
      Assert.assertTrue(String.format(metaNonTrouve, "ContratDeService"),
            metadonnees.containsKey("ContratDeService"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateArchivage"),
            metadonnees.containsKey("DateArchivage"));
      Assert.assertTrue(String.format(metaNonTrouve, "JetonDePreuve"),
            metadonnees.containsKey("JetonDePreuve"));
      Assert.assertTrue(String.format(metaNonTrouve, "RUM"), metadonnees
            .containsKey("RUM"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateSignature"),
            metadonnees.containsKey("DateSignature"));
      Assert.assertTrue(String.format(metaNonTrouve, "ReferenceDocumentaire"),
            metadonnees.containsKey("ReferenceDocumentaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCourrierV2"),
            metadonnees.containsKey("DateCourrierV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "RIBA"), metadonnees
            .containsKey("RIBA"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeProduitV2"),
            metadonnees.containsKey("CodeProduitV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeTraitementV2"),
            metadonnees.containsKey("CodeTraitementV2"));

   }

   /**
    * Permet de tester la récupération des métadonnées consultables par défaut
    * du référentiel.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getDefaultConsultableMetadataReferences() throws IOException,
         ReferentialException {

      Map<String, MetadataReference> metadonnees = referenceDAO
            .getDefaultConsultableMetadataReferences();

      Assert
            .assertEquals(
                  "Le nombre de métadonnées consultables par défaut attendues est incorrect",
                  12, metadonnees.size());

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isDefaultConsultable());
      }

      String metaNonTrouve = "Métadonnéee consultable par défaut %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "Titre"), metadonnees
            .containsKey("Titre"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCreation"),
            metadonnees.containsKey("DateCreation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateReception"),
            metadonnees.containsKey("DateReception"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeProprietaire"), metadonnees
            .containsKey("CodeOrganismeProprietaire"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeGestionnaire"), metadonnees
            .containsKey("CodeOrganismeGestionnaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "NomFichier"), metadonnees
            .containsKey("NomFichier"));
      Assert.assertTrue(String.format(metaNonTrouve, "FormatFichier"),
            metadonnees.containsKey("FormatFichier"));
      Assert.assertTrue(String.format(metaNonTrouve, "TailleFichier"),
            metadonnees.containsKey("TailleFichier"));
      Assert.assertTrue(String.format(metaNonTrouve, "ContratDeService"),
            metadonnees.containsKey("ContratDeService"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateArchivage"),
            metadonnees.containsKey("DateArchivage"));

   }

   /**
    * Permet de tester la récupération des métadonnées obligatoire pour
    * l'archivage du référentiel.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getRequiredForArchivalMetadataReferences() throws IOException,
         ReferentialException {

      Map<String, MetadataReference> metadonnees = referenceDAO
            .getRequiredForArchivalMetadataReferences();

      Assert
            .assertEquals(
                  "Le nombre de métadonnées obligatoires à l'archivage attendues est incorrect",
                  10, metadonnees.size());

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isRequiredForArchival());
      }

      String metaNonTrouve = "Métadonnéee obligatoire à l'archivage %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "Titre"), metadonnees
            .containsKey("Titre"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCreation"),
            metadonnees.containsKey("DateCreation"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationProductrice"),
            metadonnees.containsKey("ApplicationProductrice"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeProprietaire"), metadonnees
            .containsKey("CodeOrganismeProprietaire"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeGestionnaire"), metadonnees
            .containsKey("CodeOrganismeGestionnaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "TypeHash"), metadonnees
            .containsKey("TypeHash"));
      Assert.assertTrue(String.format(metaNonTrouve, "NbPages"), metadonnees
            .containsKey("NbPages"));
      Assert.assertTrue(String.format(metaNonTrouve, "FormatFichier"),
            metadonnees.containsKey("FormatFichier"));

   }

   /**
    * Permet de tester la récupération des métadonnées obligatoire pour le
    * stockage du référentiel.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getRequiredForStorageMetadataReferences() throws IOException,
         ReferentialException {

      Map<String, MetadataReference> metadonnees = referenceDAO
            .getRequiredForStorageMetadataReferences();

      Assert
            .assertEquals(
                  "Le nombre de métadonnées obligatoires au stockage attendues est incorrect",
                  16, metadonnees.size());

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isRequiredForStorage());
      }

      String metaNonTrouve = "Métadonnéee obligatoire au stockage %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "Titre"), metadonnees
            .containsKey("Titre"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCreation"),
            metadonnees.containsKey("DateCreation"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationProductrice"),
            metadonnees.containsKey("ApplicationProductrice"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeProprietaire"), metadonnees
            .containsKey("CodeOrganismeProprietaire"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeGestionnaire"), metadonnees
            .containsKey("CodeOrganismeGestionnaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "VersionRND"), metadonnees
            .containsKey("VersionRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateFinConservation"),
            metadonnees.containsKey("DateFinConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "TypeHash"), metadonnees
            .containsKey("TypeHash"));
      Assert.assertTrue(String.format(metaNonTrouve, "NbPages"), metadonnees
            .containsKey("NbPages"));
      Assert.assertTrue(String.format(metaNonTrouve, "FormatFichier"),
            metadonnees.containsKey("FormatFichier"));
      Assert.assertTrue(String.format(metaNonTrouve, "DocumentVirtuel"),
            metadonnees.containsKey("DocumentVirtuel"));
      Assert.assertTrue(String.format(metaNonTrouve, "ContratDeService"),
            metadonnees.containsKey("ContratDeService"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateArchivage"),
            metadonnees.containsKey("DateArchivage"));

   }

   /**
    * Permet de tester la récupération des métadonnées autorisée à la rechercher
    * du référentiel.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getSearchableMetadataReferences() throws IOException,
         ReferentialException {

      Map<String, MetadataReference> metadonnees = referenceDAO
            .getSearchableMetadataReferences();

      Assert
            .assertEquals(
                  "Le nombre de métadonnées utilisables en critère de recherche attendues est incorrect",
                  40, metadonnees.size());

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isSearchable());
      }

      String metaNonTrouve = "Métadonnéee %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "Titre"), metadonnees
            .containsKey("Titre"));
      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siren"), metadonnees
            .containsKey("Siren"));
      Assert.assertTrue(String.format(metaNonTrouve, "NniEmployeur"),
            metadonnees.containsKey("NniEmployeur"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroPersonne"),
            metadonnees.containsKey("NumeroPersonne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Denomination"),
            metadonnees.containsKey("Denomination"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteInterne"),
            metadonnees.containsKey("NumeroCompteInterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteExterne"),
            metadonnees.containsKey("NumeroCompteExterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siret"), metadonnees
            .containsKey("Siret"));
      Assert.assertTrue(String.format(metaNonTrouve, "PseudoSiret"),
            metadonnees.containsKey("PseudoSiret"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroStructure"),
            metadonnees.containsKey("NumeroStructure"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroRecours"),
            metadonnees.containsKey("NumeroRecours"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroIntControle"),
            metadonnees.containsKey("NumeroIntControle"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCreation"),
            metadonnees.containsKey("DateCreation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateReception"),
            metadonnees.containsKey("DateReception"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationProductrice"),
            metadonnees.containsKey("ApplicationProductrice"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationTraitement"),
            metadonnees.containsKey("ApplicationTraitement"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeProprietaire"), metadonnees
            .containsKey("CodeOrganismeProprietaire"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeGestionnaire"), metadonnees
            .containsKey("CodeOrganismeGestionnaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "SiteAcquisition"),
            metadonnees.containsKey("SiteAcquisition"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeFonction"),
            metadonnees.containsKey("CodeFonction"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeActivite"),
            metadonnees.containsKey("CodeActivite"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateFinConservation"),
            metadonnees.containsKey("DateFinConservation"));
      Assert.assertTrue(String
            .format(metaNonTrouve, "IdTraitementMasseInterne"), metadonnees
            .containsKey("IdTraitementMasseInterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "IdTraitementMasse"),
            metadonnees.containsKey("IdTraitementMasse"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateArchivage"),
            metadonnees.containsKey("DateArchivage"));
      Assert.assertTrue(String.format(metaNonTrouve, "RUM"), metadonnees
            .containsKey("RUM"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateSignature"),
            metadonnees.containsKey("DateSignature"));
      Assert.assertTrue(String.format(metaNonTrouve, "ReferenceDocumentaire"),
            metadonnees.containsKey("ReferenceDocumentaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCourrierV2"),
            metadonnees.containsKey("DateCourrierV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "RIBA"), metadonnees
            .containsKey("RIBA"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeProduitV2"),
            metadonnees.containsKey("CodeProduitV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeTraitementV2"),
            metadonnees.containsKey("CodeTraitementV2"));

   }

   /**
    * Permet de tester la récupération des métadonnées archivables du
    * référentiel.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getArchivableMetadataReferences() throws IOException,
         ReferentialException {

      Map<String, MetadataReference> metadonnees = referenceDAO
            .getArchivableMetadataReferences();

      Assert
            .assertEquals(
                  "Le nombre de métadonnées spécifiables à l'archivage attendues est incorrect",
                  44, metadonnees.size());

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isArchivable());
      }

      String metaNonTrouve = "Métadonnéee %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "Titre"), metadonnees
            .containsKey("Titre"));
      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siren"), metadonnees
            .containsKey("Siren"));
      Assert.assertTrue(String.format(metaNonTrouve, "NniEmployeur"),
            metadonnees.containsKey("NniEmployeur"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroPersonne"),
            metadonnees.containsKey("NumeroPersonne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Denomination"),
            metadonnees.containsKey("Denomination"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeCategorieV2"),
            metadonnees.containsKey("CodeCategorieV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeSousCategorieV2"),
            metadonnees.containsKey("CodeSousCategorieV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteInterne"),
            metadonnees.containsKey("NumeroCompteInterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteExterne"),
            metadonnees.containsKey("NumeroCompteExterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siret"), metadonnees
            .containsKey("Siret"));
      Assert.assertTrue(String.format(metaNonTrouve, "PseudoSiret"),
            metadonnees.containsKey("PseudoSiret"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroStructure"),
            metadonnees.containsKey("NumeroStructure"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroRecours"),
            metadonnees.containsKey("NumeroRecours"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroIntControle"),
            metadonnees.containsKey("NumeroIntControle"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCreation"),
            metadonnees.containsKey("DateCreation"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateReception"),
            metadonnees.containsKey("DateReception"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationProductrice"),
            metadonnees.containsKey("ApplicationProductrice"));
      Assert.assertTrue(String.format(metaNonTrouve, "ApplicationTraitement"),
            metadonnees.containsKey("ApplicationTraitement"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeProprietaire"), metadonnees
            .containsKey("CodeOrganismeProprietaire"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeGestionnaire"), metadonnees
            .containsKey("CodeOrganismeGestionnaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "SiteAcquisition"),
            metadonnees.containsKey("SiteAcquisition"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(
            String.format(metaNonTrouve, "TracabilitePreArchivage"),
            metadonnees.containsKey("TracabilitePreArchivage"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "TypeHash"), metadonnees
            .containsKey("TypeHash"));
      Assert.assertTrue(String.format(metaNonTrouve, "NbPages"), metadonnees
            .containsKey("NbPages"));
      Assert.assertTrue(String.format(metaNonTrouve, "FormatFichier"),
            metadonnees.containsKey("FormatFichier"));
      Assert.assertTrue(String.format(metaNonTrouve, "IdTraitementMasse"),
            metadonnees.containsKey("IdTraitementMasse"));
      Assert.assertTrue(String.format(metaNonTrouve, "JetonDePreuve"),
            metadonnees.containsKey("JetonDePreuve"));
      Assert.assertTrue(String.format(metaNonTrouve, "RUM"), metadonnees
            .containsKey("RUM"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateSignature"),
            metadonnees.containsKey("DateSignature"));
      Assert.assertTrue(String.format(metaNonTrouve, "ReferenceDocumentaire"),
            metadonnees.containsKey("ReferenceDocumentaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCourrierV2"),
            metadonnees.containsKey("DateCourrierV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "RIBA"), metadonnees
            .containsKey("RIBA"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeProduitV2"),
            metadonnees.containsKey("CodeProduitV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeTraitementV2"),
            metadonnees.containsKey("CodeTraitementV2"));

   }

   /**
    * Permet de tester la récupération des métadonnées archivables du
    * référentiel.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getModifiableMetadataReferences() throws IOException,
         ReferentialException {

      Map<String, MetadataReference> metadonnees = referenceDAO
            .getModifiableMetadataReferences();

      Assert
            .assertEquals(
                  "Le nombre de métadonnées spécifiables à l'archivage attendues est incorrect",
                  27, metadonnees.size());

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isModifiable());
      }

      String metaNonTrouve = "Métadonnéee %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "Titre"), metadonnees
            .containsKey("Titre"));
      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siren"), metadonnees
            .containsKey("Siren"));
      Assert.assertTrue(String.format(metaNonTrouve, "NniEmployeur"),
            metadonnees.containsKey("NniEmployeur"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroPersonne"),
            metadonnees.containsKey("NumeroPersonne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Denomination"),
            metadonnees.containsKey("Denomination"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeCategorieV2"),
            metadonnees.containsKey("CodeCategorieV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeSousCategorieV2"),
            metadonnees.containsKey("CodeSousCategorieV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteInterne"),
            metadonnees.containsKey("NumeroCompteInterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroCompteExterne"),
            metadonnees.containsKey("NumeroCompteExterne"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siret"), metadonnees
            .containsKey("Siret"));
      Assert.assertTrue(String.format(metaNonTrouve, "PseudoSiret"),
            metadonnees.containsKey("PseudoSiret"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroStructure"),
            metadonnees.containsKey("NumeroStructure"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroRecours"),
            metadonnees.containsKey("NumeroRecours"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroIntControle"),
            metadonnees.containsKey("NumeroIntControle"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeProprietaire"), metadonnees
            .containsKey("CodeOrganismeProprietaire"));
      Assert.assertTrue(String.format(metaNonTrouve,
            "CodeOrganismeGestionnaire"), metadonnees
            .containsKey("CodeOrganismeGestionnaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "SiteAcquisition"),
            metadonnees.containsKey("SiteAcquisition"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "RUM"), metadonnees
            .containsKey("RUM"));
      Assert.assertTrue(String.format(metaNonTrouve, "ReferenceDocumentaire"),
            metadonnees.containsKey("ReferenceDocumentaire"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateCourrierV2"),
            metadonnees.containsKey("DateCourrierV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "RIBA"), metadonnees
            .containsKey("RIBA"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeProduitV2"),
            metadonnees.containsKey("CodeProduitV2"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeTraitementV2"),
            metadonnees.containsKey("CodeTraitementV2"));

   }

   /**
    * Permet de tester la récupération d'une métadonnées du référentiel à partir
    * du code long.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getByLongCode() throws IOException, ReferentialException {

      MetadataReference metadata = referenceDAO.getByLongCode("CodeRND");

      Assert.assertNotNull("La recherche par getByLongCode() n'a pas abouti",
            metadata);

      Assert.assertEquals(
            "La propriété longCode de la métadonnée est incorrect", "CodeRND",
            metadata.getLongCode());

      Assert.assertEquals(
            "La propriété songCode de la métadonnée est incorrect",
            "SM_DOCUMENT_TYPE", metadata.getShortCode());

   }

   /**
    * Permet de tester la récupération d'une métadonnées du référentiel à partir
    * du code court.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getByShortCode() throws IOException, ReferentialException {

      MetadataReference metadata = referenceDAO
            .getByShortCode("SM_DOCUMENT_TYPE");

      Assert.assertNotNull("La recherche par getByShortCode() n'a pas abouti",
            metadata);

      Assert.assertEquals(
            "La propriété longCode de la métadonnée est incorrect", "CodeRND",
            metadata.getLongCode());

      Assert.assertEquals(
            "La propriété songCode de la métadonnée est incorrect",
            "SM_DOCUMENT_TYPE", metadata.getShortCode());

   }
   
   /**
    * Permet de tester la récupération des métadonnées transférables
    * du reférentiel.
    * 
    * @throws IOException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    * @throws ReferentialException
    *            Exception levée lorsqu'il y'a un dysfonctionnement.
    */
   @Test
   public void getTransferableMetadataReference() throws ReferentialException {
      
      Map<String, MetadataReference> metadonnees = referenceDAO
         .getTransferableMetadataReference();

      Assert
      .assertEquals(
            "Le nombre de métadonnées transférables attendues est incorrect",
            48, metadonnees.size());

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().getTransferable());
      }
      
      String metaNonTrouve = "Métadonnéee %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      
      Assert.assertFalse(String.format(metaNonTrouve, "DateArchivage"), metadonnees
            .containsKey("DateArchivage"));
   }

}
