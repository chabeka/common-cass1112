/**
 * 
 */
package fr.urssaf.image.sae.vi.component;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.saml.modele.SignatureVerificationResult;
import fr.urssaf.image.sae.vi.exception.VICertificatException;
import fr.urssaf.image.sae.vi.service.WebServiceVIValidateService;
import fr.urssaf.image.sae.vi.util.CertificatUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-vi-test.xml" })
public class WebServiceViValidateArgumentsTest {

   @Autowired
   private WebServiceVIValidateService service;
   
   @Test
   public void test_failure_contrat_obligatoire() throws VICertificatException {
      
      
      try {
         service.validateCertificates(null, null);

         Assert.fail("exception attendue");
      } catch (IllegalArgumentException e) {
         Assert.assertTrue("le message d'erreur doit concerner le contrat", e
               .getMessage().contains("contrat"));
      }
   }

   @Test
   public void test_failure_pki_obligatoire() throws VICertificatException {
      ServiceContract contract = new ServiceContract();

      try {
         service.validateCertificates(contract, null);

         Assert.fail("exception attendue");
      } catch (IllegalArgumentException e) {
         Assert.assertTrue("le message d'erreur doit concerner la pki", e
               .getMessage().contains("PKI"));
      }
   }
   
   @Test
   public void test_failure_certificat_obligatoire() throws VICertificatException {
      ServiceContract contract = new ServiceContract();
      contract.setIdPki("pki");
      contract.setVerifNommage(true);

      try {
         service.validateCertificates(contract, null);

         Assert.fail("exception attendue");
      } catch (IllegalArgumentException e) {
         Assert.assertTrue("le message d'erreur doit concerner le certificat client", e
               .getMessage().contains("certificat client"));
      }
   }
   
   @Test
   public void test_failure_certificats_physiques_obligatoire() throws VICertificatException {
      ServiceContract contract = new ServiceContract();
      contract.setIdPki("pki");
      contract.setVerifNommage(false);

      try {
         service.validateCertificates(contract, null);

         Assert.fail("exception attendue");
      } catch (IllegalArgumentException e) {
         Assert.assertTrue("le message d'erreur doit concerner le certificat client", e
               .getMessage().contains("certificats entrés en jeu"));
      }
   }
   
   @Test
   public void test_failure_ac_obligatoire() throws VICertificatException {
      ServiceContract contract = new ServiceContract();
      contract.setIdPki("pki");
      contract.setVerifNommage(false);
      SignatureVerificationResult result = new SignatureVerificationResult();
      
      try {
         service.validateCertificates(contract, result);

         Assert.fail("exception attendue");
      } catch (IllegalArgumentException e) {
         Assert.assertTrue("le message d'erreur doit concerner le certificat client", e
               .getMessage().contains("AC racine"));
      }
   }
   
   
   @Test
   public void test_failure_certif_physique_obligatoire() throws VICertificatException {
      ServiceContract contract = new ServiceContract();
      contract.setIdPki("pki");
      contract.setVerifNommage(false);
      SignatureVerificationResult result = new SignatureVerificationResult();
      result.setCertificat(CertificatUtils.getCertificat());
      try {
         service.validateCertificates(contract, result);

         Assert.fail("exception attendue");
      } catch (IllegalArgumentException e) {
         Assert.assertTrue("le message d'erreur doit concerner le certificat client", e
               .getMessage().contains("AC racine"));
      }
   }
   
   

}
