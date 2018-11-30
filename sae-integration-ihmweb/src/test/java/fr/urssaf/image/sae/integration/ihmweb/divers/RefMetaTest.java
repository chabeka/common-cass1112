package fr.urssaf.image.sae.integration.ihmweb.divers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.integration.ihmweb.modele.refmeta.ListeMetadonneesType;
import fr.urssaf.image.sae.integration.ihmweb.modele.refmeta.MetadonneeType;
import fr.urssaf.image.sae.integration.ihmweb.modele.refmeta.ObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.utils.BooleanUtils;
import fr.urssaf.image.sae.integration.ihmweb.utils.JAXBUtils;

/**
 * Outils pour le référentiel des métadonnées
 */
public class RefMetaTest {

   private static final Logger LOG = LoggerFactory.getLogger(RefMetaTest.class);

   /**
    * Génération d'un fichier ReferentielMetadonnees.xml dans le répertoire
    * temporaire de l'OS, à partir d'un export Excel du référentiel des
    * métadonnées, au format CSV (NB : la 1ère ligne du CSV doit contenir les
    * noms des colonnes)
    * 
    * Le fichier CSV est à placer dans le répertoire
    * src/test/resources/referentiel_metadonnees/
    */
   @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
   @Ignore("Ce n'est pas un TU, mais un moyen de construire le fichier XML du référentiel des métadonnées")
   @Test
   public void csvToxml() throws IOException, URISyntaxException,
         JAXBException, SAXException {

      ClassPathResource csv = new ClassPathResource(
            "referentiel_metadonnees/Metadonnees_1_8.csv");

      String repTempOs = SystemUtils.getJavaIoTmpDir().getAbsolutePath();
      String fichierXmlSortie = FilenameUtils.concat(repTempOs,
            "ReferentielMetadonnees.xml");

      List<String> lignes = FileUtils.readLines(csv.getFile(),
            CharEncoding.UTF_8);

      ObjectFactory factory = new ObjectFactory();
      ListeMetadonneesType listeMetas = factory.createListeMetadonneesType();

      // Attention : 1ère ligne = en-tête de colonne
      // codeLong;codeCourt;archivablePossible;archivableObligatoire;consulteeParDefaut;consultable;critereRecherche;client;obligatoireAuStockage;typeDfce
      String ligne;
      String parts[];
      MetadonneeType meta;
      for (int i = 1; i < lignes.size(); i++) {

         ligne = lignes.get(i);

         // System.out.println(ligne);

         parts = StringUtils.split(ligne, ';');

         meta = factory.createMetadonneeType();
         listeMetas.getMetadonnee().add(meta);

         meta.setCodeLong(StringUtils.trim(parts[0]));
         meta.setCodeCourt(StringUtils.trim(parts[1]));
         meta.setArchivablePossible(BooleanUtils.ouiNonToBoolean(parts[2]));
         meta.setArchivableObligatoire(BooleanUtils.ouiNonToBoolean(parts[3]));
         meta.setConsulteeParDefaut(BooleanUtils.ouiNonToBoolean(parts[4]));
         meta.setConsultable(BooleanUtils.ouiNonToBoolean(parts[5]));
         meta.setCritereRecherche(BooleanUtils.ouiNonToBoolean(parts[6]));
         meta.setClient(BooleanUtils.ouiNonToBoolean(parts[7]));
         meta.setObligatoireAuStockage(BooleanUtils.ouiNonToBoolean(parts[8]));
         meta.setTypeDfce(parts[9]);

      }

      String xsdSchemaPath = new ClassPathResource(
            "/ReferentielMetadonnees/ReferentielMetadonnees.xsd").getFile()
            .getAbsolutePath();
      File xsdSchema = new File(xsdSchemaPath);
      File output = new File(fichierXmlSortie);
      JAXBElement<ListeMetadonneesType> jaxbElement = factory
            .createReferentielMetadonnees(listeMetas);
      JAXBUtils.marshal(jaxbElement, output, xsdSchema, null);

      LOG.debug(fichierXmlSortie);

   }

}
