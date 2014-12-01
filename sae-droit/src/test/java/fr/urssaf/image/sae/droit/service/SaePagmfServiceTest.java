package fr.urssaf.image.sae.droit.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Pagmf;
import fr.urssaf.image.sae.droit.dao.support.PagmfSupport;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.exception.FormatControlProfilNotFoundException;
import fr.urssaf.image.sae.droit.exception.PagmfNotFoundException;

/**
 * 
 * Classe Test de la classe {@link SaePagmfService}
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaePagmfServiceTest {

   private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";
   private static final String RESULTAT_INCORRECT = "Le resultat est incorrect";

   private static final String CODE_DROIT_PAGMF = "INT_PAGM_ATT_VIGI_ALL_PAGMF";
   private static final String CODE_DROIT_PAGMF_ERREUR = "INT_PAGM_ATT_VIGI_ALL_PAG";

   private static final String CODE_FORMAT_CONTROL_PROFIL = "INT_FORMAT_PROFIL_ATT_VIGI";

   private static final String DESCRIPTION = "description";

   @Autowired
   private SaePagmfService saePagmfService;

   @Autowired
   private PagmfSupport support;

   @Test
   public void getPagmfSuccess() throws PagmfNotFoundException {

      Pagmf pagmf = saePagmfService.getPagmf(CODE_DROIT_PAGMF);

      Assert.assertNotNull(pagmf);
      Assert.assertEquals(RESULTAT_INCORRECT, "INT_PAGM_ATT_VIGI_ALL_PAGMF",
            pagmf.getCodePagmf());
      Assert.assertEquals(RESULTAT_INCORRECT, CODE_FORMAT_CONTROL_PROFIL, pagmf
            .getCodeFormatControlProfil());
      Assert.assertEquals(RESULTAT_INCORRECT,
            "Contrôle sur les fichiers fournis par l'attestation vigilance.",
            pagmf.getDescription());
   }

   @Test
   public void getPagmfNotFound() {
      try {
         saePagmfService.getPagmf(CODE_DROIT_PAGMF_ERREUR);
      } catch (PagmfNotFoundException except) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "Aucun Pagmf n'a été trouvé avec l'identifiant : INT_PAGM_ATT_VIGI_ALL_PAG.",
                     except.getMessage());
      }
   }

   @Test(expected = DroitRuntimeException.class)
   public void addPagmfFailureFormatControlProfilInexistant() {
      Pagmf pagmf = new Pagmf();
      pagmf.setDescription(DESCRIPTION);
      pagmf.setCodePagmf("codePagmf");
      pagmf.setCodeFormatControlProfil("formatProfile");

      saePagmfService.addPagmf(pagmf);

   }

   @Test
   public void addPagmfSuccess() throws FormatControlProfilNotFoundException,
         PagmfNotFoundException {
      Pagmf pagmf = new Pagmf();
      pagmf.setDescription(DESCRIPTION);
      pagmf.setCodePagmf("codePagmf");
      pagmf.setCodeFormatControlProfil(CODE_FORMAT_CONTROL_PROFIL);

      saePagmfService.addPagmf(pagmf);

      Pagmf pagmfTrouve = saePagmfService.getPagmf(pagmf.getCodePagmf());

      Assert.assertNotNull(pagmfTrouve);
      Assert.assertEquals(RESULTAT_INCORRECT, "codePagmf", pagmfTrouve
            .getCodePagmf());
      Assert.assertEquals(RESULTAT_INCORRECT, CODE_FORMAT_CONTROL_PROFIL,
            pagmfTrouve.getCodeFormatControlProfil());
      Assert.assertEquals(RESULTAT_INCORRECT, "description", pagmfTrouve
            .getDescription());
      saePagmfService.deletePagmf(pagmfTrouve.getCodePagmf());
   }

   @Test
   public void deletePagmfSuccess()
         throws FormatControlProfilNotFoundException, PagmfNotFoundException {

      Pagmf pagmf = new Pagmf();
      pagmf.setDescription(DESCRIPTION);
      pagmf.setCodePagmf("codePagmf2");
      pagmf.setCodeFormatControlProfil(CODE_FORMAT_CONTROL_PROFIL);

      saePagmfService.addPagmf(pagmf);

      Pagmf pagmfTrouve = saePagmfService.getPagmf(pagmf.getCodePagmf());

      Assert.assertNotNull(pagmfTrouve);

      saePagmfService.deletePagmf(pagmfTrouve.getCodePagmf());

      Pagmf pagmfNonTrouve = support.find(pagmf.getCodePagmf());

      Assert.assertNull(pagmfNonTrouve);

   }

   @Test
   public void deletePagmfFailure() {

      try {
         saePagmfService.deletePagmf("codePagmf2");
      } catch (PagmfNotFoundException except) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT,
               "Le Pagmf à supprimer : [codePagmf2] n'existe pas en base.",
               except.getMessage());
      }

   }

   @Test
   public void getAllPagmfs() throws FormatControlProfilNotFoundException,
         PagmfNotFoundException {

      List<Pagmf> listPagmf = saePagmfService.getAllPagmf();

      Assert.assertNotNull(listPagmf);
      Assert.assertEquals("Le nombre d'éléments est incorrect.", 1, listPagmf
            .size());

   }

}
