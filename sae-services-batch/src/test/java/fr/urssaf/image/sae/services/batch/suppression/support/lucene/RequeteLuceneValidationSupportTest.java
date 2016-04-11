package fr.urssaf.image.sae.services.batch.suppression.support.lucene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.batch.suppression.exception.SuppressionMasseRequeteValidationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-suppressionmasse-test.xml" })
public class RequeteLuceneValidationSupportTest {

   @Autowired
   private RequeteLuceneValidationSupport support;
   
   @Test(expected = IllegalArgumentException.class)
   public void testValidationRequeteObligatoire()
         throws SuppressionMasseRequeteValidationException {

      support.validationRequeteLucene(null);
      Assert.fail("sortie aspect attendue");
   }
   
   @Test(expected = SuppressionMasseRequeteValidationException.class)
   public void testValidationRequeteLuceneInvalide()
         throws SuppressionMasseRequeteValidationException {
      
      support.validationRequeteLucene("Siret:123456 AND IdTraitementMasse:41882:050200023");
      Assert.fail("sortie d'exception 'requete non valide'");
   }
   
   @Test
   public void testValidationRequeteLuceneValide()
         throws SuppressionMasseRequeteValidationException {
      
      support.validationRequeteLucene("Siret:123456");
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testVerificationDroitRequeteLuceneObligatoire()
         throws SuppressionMasseRequeteValidationException {

      support.verificationDroitRequeteLucene(null);
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
      String[] roles = new String[] { "suppression_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("suppression_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }
   
   @Test(expected = SuppressionMasseRequeteValidationException.class)
   public void testVerificationDroitRequeteLuceneUnknownMeta()
         throws SuppressionMasseRequeteValidationException {
      
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      setAuthentification(prmd);

      support.verificationDroitRequeteLucene("Metadata:1234");
      Assert.fail("sortie d'exception 'unknown metadata'");
   }
   
   @Test(expected = SuppressionMasseRequeteValidationException.class)
   public void testVerificationDroitRequeteLuceneNonSearcheableMeta()
         throws SuppressionMasseRequeteValidationException {
      
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      setAuthentification(prmd);

      support.verificationDroitRequeteLucene("NomFichier:1234");
      Assert.fail("sortie d'exception 'metadata unauthorized to search'");
   }
   
   @Test
   public void testVerificationDroitRequeteLuceneValide()
         throws SuppressionMasseRequeteValidationException {
      
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      setAuthentification(prmd);

      String requeteCourt = support.verificationDroitRequeteLucene("Siret:123456");
      
      Assert.assertNotNull("La requete lucene avec libellé court doit être remplie", requeteCourt);
   }
   
   @Test
   public void testVerificationDroitRequeteLuceneValideAndPrmdRestreint()
         throws SuppressionMasseRequeteValidationException {
      
      Prmd prmd = new Prmd();
      prmd.setLucene("ApplicationTraitement:TOTO AND DomaineCotisant:true");
      setAuthentification(prmd);

      String requeteCourt = support.verificationDroitRequeteLucene("Siret:123456");
      
      Assert.assertNotNull("La requete lucene avec libellé court doit être remplie", requeteCourt);
   }
}
