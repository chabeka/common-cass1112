package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-lotinstallmaj-test.xml" })
public class RefMetaInitialisationServiceTest {

   @Autowired
   private RefMetaInitialisationService refMetaService;

   @Test
   public void chargeFichierMeta_test() {

      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();

      Assert.assertEquals("Le nombre de métadonnées attendu est incorrect", 55,
            metadonnees.size());

   }

   @Test
   public void genereFichierXmlAncienneVersion_test() {

      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();

      List<String> lignes = refMetaService
            .genereFichierXmlAncienneVersionRefMeta(metadonnees);

      // Ecriture dans un fichier temporaire, pour mieux visualiser
      // try {
      // File fileTemp = new File("c:/divers/refmeta_verif1.xml");
      // FileUtils.writeLines(fileTemp, lignes);
      // } catch (IOException e) {
      // throw new MajLotRuntimeException(e);
      // }

      Assert.assertEquals("Le nombre de lignes attendu est incorrect", 883,
            lignes.size());

   }

   @Test
   public void verification1_test() {

      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();

      refMetaService.verification1(metadonnees);
   }

   @Test
   public void genereFichierXmlAncienneVersionBaseDfce_test() {

      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();

      List<String> lignes = refMetaService
            .genereFichierXmlAncienneVersionBaseDfce(metadonnees);

      // Ecriture dans un fichier temporaire, pour mieux visualiser
      // try {
      // File fileTemp = new File("c:/divers/refmeta_verif2.xml");
      // FileUtils.writeLines(fileTemp, lignes);
      // } catch (IOException e) {
      // throw new MajLotRuntimeException(e);
      // }

      Assert.assertEquals("Le nombre de lignes attendu est incorrect", 387,
            lignes.size());

   }

   @Test
   public void verification2_test() {

      List<MetadataReference> metadonnees = refMetaService.chargeFichierMeta();

      refMetaService.verification2(metadonnees);
   }

}
