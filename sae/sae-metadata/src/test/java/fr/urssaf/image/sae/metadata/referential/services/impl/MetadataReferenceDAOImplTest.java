package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
   private MetadataReferenceDAO referenceDAO;

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
            metadonnees.size(), 13);

      String metaNonTrouve = "Métadonnéee %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "IdTraitementMasse"),
            metadonnees.containsKey("IdTraitementMasse"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroIntControle"),
            metadonnees.containsKey("NumeroIntControle"));
      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      Assert.assertTrue(String.format(metaNonTrouve, "VersionNumber"),
            metadonnees.containsKey("VersionNumber"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeActivite"),
            metadonnees.containsKey("CodeActivite"));
      Assert.assertTrue(String.format(metaNonTrouve, "VersionRND"), metadonnees
            .containsKey("VersionRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "StartPage"), metadonnees
            .containsKey("StartPage"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "Gel"), metadonnees
            .containsKey("Gel"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeFonction"),
            metadonnees.containsKey("CodeFonction"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siret"), metadonnees
            .containsKey("Siret"));

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
            metadonnees.size(), 11);

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isConsultable());
      }

      String metaNonTrouve = "Métadonnéee consultable %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "IdTraitementMasse"),
            metadonnees.containsKey("IdTraitementMasse"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroIntControle"),
            metadonnees.containsKey("NumeroIntControle"));
      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeActivite"),
            metadonnees.containsKey("CodeActivite"));
      Assert.assertTrue(String.format(metaNonTrouve, "VersionRND"), metadonnees
            .containsKey("VersionRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "Gel"), metadonnees
            .containsKey("Gel"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeFonction"),
            metadonnees.containsKey("CodeFonction"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siret"), metadonnees
            .containsKey("Siret"));

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
                  metadonnees.size(), 2);

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isDefaultConsultable());
      }

      String metaNonTrouve = "Métadonnéee consultable par défaut %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));

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
                  metadonnees.size(), 2);

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isRequiredForArchival());
      }

      String metaNonTrouve = "Métadonnéee obligatoire à l'archivage %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));

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
                  metadonnees.size(), 5);

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isRequiredForStorage());
      }

      String metaNonTrouve = "Métadonnéee obligatoire au stockage %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "VersionRND"), metadonnees
            .containsKey("VersionRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeFonction"),
            metadonnees.containsKey("CodeFonction"));

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
                  metadonnees.size(), 8);

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isSearchable());
      }

      String metaNonTrouve = "Métadonnéee %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "IdTraitementMasse"),
            metadonnees.containsKey("IdTraitementMasse"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroIntControle"),
            metadonnees.containsKey("NumeroIntControle"));
      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeActivite"),
            metadonnees.containsKey("CodeActivite"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeFonction"),
            metadonnees.containsKey("CodeFonction"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siret"), metadonnees
            .containsKey("Siret"));

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
                  metadonnees.size(), 8);

      for (Map.Entry<String, MetadataReference> metadata : metadonnees
            .entrySet()) {
         Assert.assertTrue(metadata.getValue().isArchivable());
      }

      String metaNonTrouve = "Métadonnéee %s non trouvée";

      Assert.assertTrue(String.format(metaNonTrouve, "IdTraitementMasse"),
            metadonnees.containsKey("IdTraitementMasse"));
      Assert.assertTrue(String.format(metaNonTrouve, "NumeroIntControle"),
            metadonnees.containsKey("NumeroIntControle"));
      Assert.assertTrue(String.format(metaNonTrouve, "Periode"), metadonnees
            .containsKey("Periode"));
      Assert.assertTrue(String.format(metaNonTrouve, "CodeRND"), metadonnees
            .containsKey("CodeRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "VersionRND"), metadonnees
            .containsKey("VersionRND"));
      Assert.assertTrue(String.format(metaNonTrouve, "Hash"), metadonnees
            .containsKey("Hash"));
      Assert.assertTrue(String.format(metaNonTrouve, "DateDebutConservation"),
            metadonnees.containsKey("DateDebutConservation"));
      Assert.assertTrue(String.format(metaNonTrouve, "Siret"), metadonnees
            .containsKey("Siret"));

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

}
