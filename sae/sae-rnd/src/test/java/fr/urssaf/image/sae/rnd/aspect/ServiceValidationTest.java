package fr.urssaf.image.sae.rnd.aspect;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;
import fr.urssaf.image.sae.rnd.service.RndService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class ServiceValidationTest {

   @Autowired
   private CorrespondancesRndSupport correspondancesRndSupport;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private SaeBddSupport saeBddSupport;

   @Autowired
   private RndService rndService;

   @Autowired
   private JobClockSupport jobClockSupport;

   @Test
   public void testMethodesGetRndService() throws CodeRndInexistantException {

      // Recherche avec codeRnd null
      try {
         rndService.getCodeActivite(null);
         Assert.fail("le test doit échouer : codeRnd null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument codeRnd n'est pas valide : codeRnd null ou vide", e
                     .getMessage());
      }
      
      try {
         rndService.getCodeFonction(null);
         Assert.fail("le test doit échouer : codeRnd null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument codeRnd n'est pas valide : codeRnd null ou vide", e
                     .getMessage());
      }
      
      try {
         rndService.getDureeConservation(null);
         Assert.fail("le test doit échouer : codeRnd null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument codeRnd n'est pas valide : codeRnd null ou vide", e
                     .getMessage());
      }
      
      try {
         rndService.getTypeDocument(null);
         Assert.fail("le test doit échouer : codeRnd null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument codeRnd n'est pas valide : codeRnd null ou vide", e
                     .getMessage());
      }

      // Recherche avec codeRnd vide
      try {
         rndService.getCodeActivite("");
         Assert.fail("le test doit échouer : codeRnd null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument codeRnd n'est pas valide : codeRnd null ou vide", e
                     .getMessage());
      }
      
      try {
         rndService.getCodeFonction("");
         Assert.fail("le test doit échouer : codeRnd null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument codeRnd n'est pas valide : codeRnd null ou vide", e
                     .getMessage());
      }
      
      try {
         rndService.getDureeConservation("");
         Assert.fail("le test doit échouer : codeRnd null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument codeRnd n'est pas valide : codeRnd null ou vide", e
                     .getMessage());
      }
      
      try {
         rndService.getTypeDocument("");
         Assert.fail("le test doit échouer : codeRnd null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument codeRnd n'est pas valide : codeRnd null ou vide", e
                     .getMessage());
      }

    }

   @Test
   public void testUpdateVerion() throws SaeBddRuntimeException {

      // Version null
      try {
         saeBddSupport.updateVersionRnd(null);
         Assert.fail("le test doit échouer : versionRnd = 0");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals("l'argument versionRnd n'est pas valide", e
               .getMessage());
      }

      VersionRnd version = new VersionRnd();

      // Date de mise à jour null
      version.setVersionEnCours("11.4");
      try {
         saeBddSupport.updateVersionRnd(version);
         Assert.fail("le test doit échouer : date de mise à jour null");
      } catch (IllegalArgumentException e) {
         Assert
               .assertEquals(
                     "l'argument versionRnd n'est pas valide : date de mise à jour null",
                     e.getMessage());
      }

      // Numéro de version null
      version.setDateMiseAJour(new Date());
      version.setVersionEnCours(null);
      try {
         saeBddSupport.updateVersionRnd(version);
         Assert.fail("le test doit échouer : numero de version à jour null");
      } catch (IllegalArgumentException e) {
         Assert
               .assertEquals(
                     "l'argument versionRnd n'est pas valide : numéro version null ou vide",
                     e.getMessage());
      }

   }

   @Test
   public void testAjouterRnd() {

      TypeDocument typeDoc = new TypeDocument();
      typeDoc.setCloture(false);
      typeDoc.setCode("1.1.1.1.1");
      typeDoc.setCodeActivite("1");
      typeDoc.setCodeFonction("1");
      typeDoc.setDureeConservation(3000);
      typeDoc.setLibelle("libellé");
      typeDoc.setType(TypeCode.NON_ARCHIVABLE_AED);

      // Clock 0
      try {
         rndSupport.ajouterRnd(typeDoc, 0);
         Assert.fail("le test doit échouer : clock = 0");

      } catch (IllegalArgumentException e) {
         Assert.assertEquals("l'argument clock n'est pas valide", e
               .getMessage());
      }

      // typeDoc null
      try {
         rndSupport.ajouterRnd(null, jobClockSupport.currentCLock());
         Assert.fail("le test doit échouer : typeDoc null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals("l'argument typeDoc n'est pas valide", e
               .getMessage());
      }

      // code RND vide
      typeDoc.setCode("");
      try {
         rndSupport.ajouterRnd(typeDoc, jobClockSupport.currentCLock());
         Assert.fail("le test doit échouer : typeDoc null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument typeDoc n'est pas valide : code Rnd null ou vide", e
                     .getMessage());
      }
      typeDoc.setCode("1.1.1.1.1");

      // code RND null
      typeDoc.setCode(null);
      try {
         rndSupport.ajouterRnd(typeDoc, jobClockSupport.currentCLock());
         Assert.fail("le test doit échouer : code Rnd null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals(
               "l'argument typeDoc n'est pas valide : code Rnd null ou vide", e
                     .getMessage());
      }
      typeDoc.setCode("1.1.1.1.1");

      // Code fonction null (avec type de document non temporaire)
      typeDoc.setCodeFonction(null);
      try {
         rndSupport.ajouterRnd(typeDoc, jobClockSupport.currentCLock());
         Assert.fail("le test doit échouer : codeFonction null");
      } catch (IllegalArgumentException e) {
         Assert
               .assertEquals(
                     "l'argument typeDoc n'est pas valide : codeFonction null ou vide alors que le type de document n'est pas TEMPORAIRE",
                     e.getMessage());
      }
      typeDoc.setCodeFonction("1");

      // Durée de conservation à 0
      typeDoc.setDureeConservation(0);
      try {
         rndSupport.ajouterRnd(typeDoc, jobClockSupport.currentCLock());
         Assert.fail("le test doit échouer : dureeConservation <= 0");
      } catch (IllegalArgumentException e) {
         Assert
               .assertEquals(
                     "l'argument typeDoc n'est pas valide : dureeConservation négatif ou nul",
                     e.getMessage());
      }

   }

   @Test
   public void testAjouterCorrespondance() {

      Correspondance correspondance = new Correspondance();
      correspondance.setCodeDefinitif("1.1.1.1.1");
      correspondance.setCodeTemporaire("a.a.a.a.a");
      correspondance.setDateDebutMaj(new Date());
      correspondance.setDateFinMaj(new Date());
      correspondance.setEtat(EtatCorrespondance.CREATED);

      // Clock 0
      try {
         correspondancesRndSupport.ajouterCorrespondance(correspondance, 0);
         Assert.fail("le test doit échouer : clock = 0");

      } catch (IllegalArgumentException e) {
         Assert.assertEquals("l'argument clock n'est pas valide", e
               .getMessage());
      }

      // Correspondance null
      try {
         correspondancesRndSupport.ajouterCorrespondance(null, jobClockSupport
               .currentCLock());
         Assert.fail("le test doit échouer : correspondance null");
      } catch (IllegalArgumentException e) {
         Assert.assertEquals("l'argument correspondance n'est pas valide", e
               .getMessage());
      }

      // Code définitif null
      correspondance.setCodeDefinitif("");
      try {
         correspondancesRndSupport.ajouterCorrespondance(correspondance,
               jobClockSupport.currentCLock());
         Assert.fail("le test doit échouer : code définitif vide");
      } catch (IllegalArgumentException e) {
         Assert
               .assertEquals(
                     "l'argument correspondance n'est pas valide : code définitif null ou vide",
                     e.getMessage());
      }

      // Code temporaire null
      correspondance.setCodeDefinitif("1.1.1.1.1");
      correspondance.setCodeTemporaire("");
      try {
         correspondancesRndSupport.ajouterCorrespondance(correspondance,
               jobClockSupport.currentCLock());
         Assert.fail("le test doit échouer : code temporaire vide");
      } catch (IllegalArgumentException e) {
         Assert
               .assertEquals(
                     "l'argument correspondance n'est pas valide : code temporaire null ou vide",
                     e.getMessage());
      }

   }

}
