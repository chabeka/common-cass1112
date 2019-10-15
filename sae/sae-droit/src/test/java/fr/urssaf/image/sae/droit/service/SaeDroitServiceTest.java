/**
 * 
 */
package fr.urssaf.image.sae.droit.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.model.SaePagm;
import fr.urssaf.image.sae.droit.model.SaePagma;
import fr.urssaf.image.sae.droit.model.SaePagmp;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-droit-test.xml" })
public class SaeDroitServiceTest {

  private static final String EXCEPTION_ATTENDUE = "exception attendue";
  private static final String ARG_REQUIRED = "argument.required";
  private static final String TYPE_CORRECT = "type de l'exception correcte";
  private static final String MESSAGE_CORRECT = "le message de l'exception doit etre correct";

  @Autowired
  // @Qualifier("saeDroitServiceFacadeImpl")
  private SaeDroitService service;

  @Autowired
  // @Qualifier("saeActionUnitaireServiceFacadeImpl")
  private SaeActionUnitaireService serviceAction;

  @Test
  public void testLoadIdObligatoire() {

    try {
      service.loadSaeDroits(null, Arrays.asList(new String[] { "pagm1" }));
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertEquals(MESSAGE_CORRECT, getMessage("identifiant client"),
                          e.getMessage());
    }

  }

  @Test
  public void testLoadListObligatoire() {

    try {
      service.loadSaeDroits("id", null);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertEquals(MESSAGE_CORRECT, getMessage("liste des pagms"), e
                          .getMessage());
    }

  }

  @Test
  public void testCreateContratObligatoire() {

    try {

      final List<SaePagm> list = new ArrayList<>();
      list.add(new SaePagm());

      service.createContratService(null, list);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertEquals(MESSAGE_CORRECT, getMessage("contrat"), e
                          .getMessage());
    }

  }

  @Test
  public void testCreateCodeContratObligatoire() {

    try {
      final ServiceContract contract = new ServiceContract();
      contract.setDescription("description");
      contract.setLibelle("libellé");
      contract.setViDuree(Long.valueOf(12));
      final List<SaePagm> list = new ArrayList<>();
      list.add(new SaePagm());

      service.createContratService(contract, list);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertEquals(MESSAGE_CORRECT,
                          getMessage("code client contrat"), e.getMessage());
    }

  }

  @Test
  public void testCreateDescriptionContratObligatoire() {

    try {
      final ServiceContract contract = new ServiceContract();
      contract.setCodeClient("codeClient");
      contract.setLibelle("libellé");
      contract.setViDuree(Long.valueOf(12));
      final List<SaePagm> list = new ArrayList<>();
      list.add(new SaePagm());
      service.createContratService(contract, list);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertEquals(MESSAGE_CORRECT,
                          getMessage("description contrat"), e.getMessage());
    }

  }

  @Test
  public void testCreateLibelleContratObligatoire() {

    try {
      final ServiceContract contract = new ServiceContract();
      contract.setCodeClient("codeClient");
      contract.setDescription("description");
      contract.setViDuree(Long.valueOf(12));
      final List<SaePagm> list = new ArrayList<>();
      list.add(new SaePagm());

      service.createContratService(contract, list);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertEquals(MESSAGE_CORRECT, getMessage("libellé contrat"), e
                          .getMessage());
    }

  }

  @Test
  public void testCreateDureeContratObligatoire() {

    try {
      final ServiceContract contract = new ServiceContract();
      contract.setCodeClient("codeClient");
      contract.setDescription("description");
      contract.setLibelle("libellé");
      final List<SaePagm> list = new ArrayList<>();
      list.add(new SaePagm());

      service.createContratService(contract, list);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertEquals(MESSAGE_CORRECT, getMessage("durée contrat"), e
                          .getMessage());
    }

  }

  @Test
  public void testCreateListContratObligatoire() {

    try {
      final ServiceContract contract = new ServiceContract();
      contract.setCodeClient("codeClient");
      contract.setDescription("description");
      contract.setLibelle("libellé");
      contract.setViDuree(Long.valueOf(12));

      service.createContratService(contract, null);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertEquals(MESSAGE_CORRECT, getMessage("liste des pagms"), e
                          .getMessage());
    }

  }

  @Test
  public void testCreatePkiObligatoire() {

    try {
      final ServiceContract contract = new ServiceContract();
      contract.setCodeClient("codeClient");
      contract.setDescription("description");
      contract.setLibelle("libellé");
      contract.setViDuree(Long.valueOf(12));
      final List<SaePagm> list = new ArrayList<>();
      list.add(new SaePagm());

      service.createContratService(contract, list);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertEquals(MESSAGE_CORRECT,
                          getMessage("le nom de la PKI ou la liste des PKI"), e
                          .getMessage());
    }

  }

  @Test
  public void testCreateCertificatClientObligatoire() {

    try {
      final ServiceContract contract = new ServiceContract();
      contract.setCodeClient("codeClient");
      contract.setDescription("description");
      contract.setLibelle("libellé");
      contract.setViDuree(Long.valueOf(12));
      final List<SaePagm> list = new ArrayList<>();
      list.add(new SaePagm());
      contract.setIdPki("pki");
      contract.setVerifNommage(true);

      service.createContratService(contract, list);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert
      .assertEquals(
                    MESSAGE_CORRECT,
                    getMessage("le certificat client ou la liste des certificats clients"),
                    e.getMessage());
    }

  }

  @Test
  public void testExistsContratObligatoire() {

    try {

      service.contratServiceExists(null);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertTrue("message de l'exception contient code client", e
                        .getMessage().contains("code client"));
    }

  }

  @Test
  public void testGetServiceContratObligatoire() {

    try {

      service.getServiceContract(null);
      Assert.fail(EXCEPTION_ATTENDUE);

    } catch (final Exception e) {
      Assert.assertEquals(TYPE_CORRECT, IllegalArgumentException.class, e
                          .getClass());
      Assert.assertTrue("message de l'exception contient code client", e
                        .getMessage().contains("code client"));
    }

  }

  @Test
  public void testModifierContratService() {

    final ServiceContract contract = new ServiceContract();
    contract.setDescription("description");
    contract.setLibelle("libellé");
    contract.setCodeClient("code");
    contract.setViDuree(Long.valueOf(12));
    final List<SaePagm> list = new ArrayList<>();
    final SaePagm saePagm = new SaePagm();
    saePagm.setCode("code");
    saePagm.setDescription("description");
    final SaePagma pagma = new SaePagma();
    final List<String> listeAu = new ArrayList<>();
    listeAu.add("codeAction");
    pagma.setActionUnitaires(listeAu);
    pagma.setCode("code");
    saePagm.setPagma(pagma);
    final SaePagmp pagmp = new SaePagmp();
    pagmp.setCode("code");
    pagmp.setDescription("description");
    pagmp.setPrmd("prmd");
    saePagm.setPagmp(pagmp);
    list.add(saePagm);
    final List<String> listPki = new ArrayList<>();
    listPki.add("pki");
    contract.setListPki(listPki);

    final ActionUnitaire actionUnitaire = new ActionUnitaire();
    actionUnitaire.setCode("codeAction");
    actionUnitaire.setDescription("description action");

    serviceAction.createActionUnitaire(actionUnitaire);

    service.createContratService(contract, list);

    contract.setDescription("description 2");

    service.modifierContratService(contract);

    service.refrechContratsCache("code");

    final ServiceContract contractModifie = service.getServiceContract("code");
    Assert.assertEquals("description 2", contractModifie.getDescription());

  }

  private String getMessage(final String value) {
    return ResourceMessagesUtils.loadMessage(ARG_REQUIRED, value);
  }
}
