package fr.urssaf.image.sae.services.batch.restore.support.lucene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.batch.restore.exception.RestoreMasseParamValidationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-restoremasse-test.xml" })
public class RestoreParamValidationSupportTest {

   @Autowired
   private RestoreParamValidationSupport support;
   
   @Test(expected = IllegalArgumentException.class)
   public void testVerificationDroitRestoreIdSuppressionObligatoire()
         throws RestoreMasseParamValidationException {

      support.verificationDroitRestore(null);
      Assert.fail("sortie aspect attendue");
   }
   
   private void setAuthentification(Prmd prmd) {
      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "restore_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("restore_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }
   
   @Test(expected = RestoreMasseParamValidationException.class)
   public void testVerificationDroitRestoreUnknownMeta()
         throws RestoreMasseParamValidationException {
      
      Prmd prmd = new Prmd();
      // test moche : pour provoquer cette erreur, on met un prmd restreint sur une metadonnee inconnue
      prmd.setLucene("Metadata:1234");
      setAuthentification(prmd);

      support.verificationDroitRestore(UUID.randomUUID());
      Assert.fail("sortie d'exception 'unknown metadata'");
   }
   
   @Test(expected = RestoreMasseParamValidationException.class)
   public void testVerificationDroitRestoreNonSearcheableMeta()
         throws RestoreMasseParamValidationException {
      
      Prmd prmd = new Prmd();
      // test moche : pour provoquer cette erreur, on met un prmd restreint sur une metadonnee non rechercheable
      prmd.setLucene("NomFichier:1234");
      setAuthentification(prmd);

      support.verificationDroitRestore(UUID.randomUUID());
      Assert.fail("sortie d'exception 'metadata unauthorized to search'");
   }
   
   @Test
   public void testVerificationDroitRestoreValide()
         throws RestoreMasseParamValidationException {
      
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      setAuthentification(prmd);

      String requeteCourt = support.verificationDroitRestore(UUID.randomUUID());
      
      Assert.assertNotNull("La requete lucene avec libellé court doit être remplie", requeteCourt);
   }
   
   @Test
   public void testVerificationDroitRestoreValideAndPrmdRestreint()
         throws RestoreMasseParamValidationException {
      
      Prmd prmd = new Prmd();
      prmd.setLucene("ApplicationTraitement:TOTO AND DomaineCotisant:true");
      setAuthentification(prmd);

      String requeteCourt = support.verificationDroitRestore(UUID.randomUUID());
      
      Assert.assertNotNull("La requete lucene avec libellé court doit être remplie", requeteCourt);
   }
}
