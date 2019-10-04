package fr.urssaf.image.sae.commons.service.cql;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.utils.GestionModeApiUtils;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.commons.utils.Constantes;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-commons-test.xml"})
public class ParametersCqlServiceTest {

  @Autowired
  private ParametersService parametersService;

  @Autowired
  private CassandraServerBean server;

  private final Date date = new Date();
  @Before
  public void begin() throws Exception {
    GestionModeApiUtils.setModeApiCql(Constantes.CF_PARAMETERS);
  }

  @After
  public void end() throws Exception {
    server.resetData(true, MODE_API.DATASTAX);
  }

  @Test
  public void testJournalisationEvtDate() throws ParameterNotFoundException {

    final Date storedDate = DateUtils.addDays(date, -2);
    parametersService.setJournalisationEvtDate(storedDate);

    final Date dateRecup = parametersService.getJournalisationEvtDate();
    final Date dateAttendue = DateUtils.truncate(storedDate, Calendar.DATE);

    Assert.assertEquals("la date doit etre correcte", dateAttendue, dateRecup);

  }

  @Test
  public void testPurgeEvtDate() throws ParameterNotFoundException {
    final Date storedDate = DateUtils.addDays(date, -3);
    parametersService.setPurgeEvtDate(storedDate);

    final Date dateRecup = parametersService.getPurgeEvtDate();
    final Date dateAttendue = DateUtils.truncate(storedDate, Calendar.DATE);

    Assert.assertEquals("la date doit etre correcte", dateAttendue, dateRecup);

  }

  @Test
  public void testPurgeExploitDate() throws ParameterNotFoundException {
    final Date storedDate = DateUtils.addDays(date, -4);
    parametersService.setPurgeExploitDate(storedDate);

    final Date dateRecup = parametersService.getPurgeExploitDate();
    final Date dateAttendue = DateUtils.truncate(storedDate, Calendar.DATE);

    Assert.assertEquals("la date doit etre correcte", dateAttendue, dateRecup);

  }

  @Test
  public void testPurgeSecuDate() throws ParameterNotFoundException {
    final Date storedDate = DateUtils.addDays(date, -5);
    parametersService.setPurgeSecuDate(storedDate);

    final Date dateRecup = parametersService.getPurgeSecuDate();
    final Date dateAttendue = DateUtils.truncate(storedDate, Calendar.DATE);

    Assert.assertEquals("la date doit etre correcte", dateAttendue, dateRecup);

  }

  @Test
  public void testPurgeTechDate() throws ParameterNotFoundException {
    final Date storedDate = DateUtils.addDays(date, -6);
    parametersService.setPurgeTechDate(storedDate);

    final Date dateRecup = parametersService.getPurgeTechDate();
    final Date dateAttendue = DateUtils.truncate(storedDate, Calendar.DATE);

    Assert.assertEquals("la date doit etre correcte", dateAttendue, dateRecup);

  }

  @Test
  public void testPurgeEvtDuree() throws ParameterNotFoundException {
    final Integer storedDuree = 1;
    parametersService.setPurgeEvtDuree(storedDuree);

    final Integer duree = parametersService.getPurgeEvtDuree();

    Assert.assertEquals("la durée doit etre correcte", storedDuree, duree);

  }

  @Test
  public void testPurgeExploitDuree() throws ParameterNotFoundException {
    final Integer storedDuree = 2;
    parametersService.setPurgeExploitDuree(storedDuree);

    final Integer duree = parametersService.getPurgeExploitDuree();

    Assert.assertEquals("la durée doit etre correcte", storedDuree, duree);

  }

  @Test
  public void testPurgeSecuDuree() throws ParameterNotFoundException {
    final Integer storedDuree = 3;
    parametersService.setPurgeSecuDuree(storedDuree);

    final Integer duree = parametersService.getPurgeSecuDuree();

    Assert.assertEquals("la durée doit etre correcte", storedDuree, duree);

  }

  @Test
  public void testPurgeTechDuree() throws ParameterNotFoundException {
    final Integer storedDuree = 4;
    parametersService.setPurgeTechDuree(storedDuree);

    final Integer duree = parametersService.getPurgeTechDuree();

    Assert.assertEquals("la durée doit etre correcte", storedDuree, duree);

  }

  @Test
  public void testJournalisationEvtRunning() throws ParameterNotFoundException {
    final Boolean storedRunning = false;
    parametersService.setJournalisationEvtIsRunning(storedRunning);

    final Boolean running = parametersService.isJournalisationEvtIsRunning();

    Assert.assertEquals("l'indicateur doit etre correct", storedRunning, running);

  }

  @Test
  public void testPurgeEvtRunning() throws ParameterNotFoundException {
    final Boolean storedRunning = true;
    parametersService.setPurgeEvtIsRunning(storedRunning);

    final Boolean running = parametersService.isPurgeEvtIsRunning();

    Assert.assertEquals("l'indicateur doit etre correct", storedRunning, running);

  }

  @Test
  public void testPurgeExploitRunning() throws ParameterNotFoundException {
    final Boolean storedRunning = false;
    parametersService.setPurgeExploitIsRunning(storedRunning);

    final Boolean running = parametersService.isPurgeExploitIsRunning();

    Assert.assertEquals("l'indicateur doit etre correct", storedRunning, running);

  }

  @Test
  public void testPurgeSecuRunning() throws ParameterNotFoundException {
    final Boolean storedRunning = true;
    parametersService.setPurgeSecuIsRunning(storedRunning);

    final Boolean running = parametersService.isPurgeSecuIsRunning();

    Assert.assertEquals("l'indicateur doit etre correct", storedRunning, running);

  }

  @Test
  public void testPurgeTechRunning() throws ParameterNotFoundException {
    final Boolean storedRunning = false;
    parametersService.setPurgeTechIsRunning(storedRunning);

    final Boolean running = parametersService.isPurgeTechIsRunning();

    Assert.assertEquals("l'indicateur doit etre correct", storedRunning, running);

  }

  @Test
  public void testHash() throws ParameterNotFoundException {
    final String storedHash = "12345678901234567890";

    parametersService.setJournalisationEvtHashJournPrec(storedHash);

    final String hash = parametersService.getJournalisationEvtHashJournPrec();

    Assert.assertEquals("le hash doit etre correct", storedHash, hash);
  }

  @Test
  public void testId() throws ParameterNotFoundException {
    final String storedId = UUID.randomUUID().toString();

    parametersService.setJournalisationEvtIdJournPrec(storedId);

    final String id = parametersService.getJournalisationEvtIdJournPrec();

    Assert.assertEquals("le hash doit etre correct", storedId, id);
  }

  @Test
  public void testMetaApplProd() throws ParameterNotFoundException {
    final String storedMeta = "applProd";

    parametersService.setJournalisationEvtMetaApplProd(storedMeta);

    final String meta = parametersService.getJournalisationEvtMetaApplProd();

    Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
  }

  @Test
  public void testMetaApplTrait() throws ParameterNotFoundException {
    final String storedMeta = "applTrait";

    parametersService.setJournalisationEvtMetaApplTrait(storedMeta);

    final String meta = parametersService.getJournalisationEvtMetaApplTrait();

    Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
  }

  @Test
  public void testMetaCodeOrga() throws ParameterNotFoundException {
    final String storedMeta = "codeOrga";

    parametersService.setJournalisationEvtMetaCodeOrga(storedMeta);

    final String meta = parametersService.getJournalisationEvtMetaCodeOrga();

    Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
  }

  @Test
  public void testMetaCodeRnd() throws ParameterNotFoundException {
    final String storedMeta = "codeRnd";

    parametersService.setJournalisationEvtMetaCodeRnd(storedMeta);

    final String meta = parametersService.getJournalisationEvtMetaCodeRnd();

    Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
  }

  @Test
  public void testMetaTitre() throws ParameterNotFoundException {
    final String storedMeta = "titre sauvegardé";

    parametersService.setJournalisationEvtMetaTitre(storedMeta);

    final String meta = parametersService.getJournalisationEvtMetaTitre();

    Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
  }

  @Test
  public void testVersionRndDateMaj() throws ParameterNotFoundException {
    final Date storedDate = DateUtils.addHours(date, 1);
    parametersService.setVersionRndDateMaj(storedDate);

    final Date dateRecup = parametersService.getVersionRndDateMaj();

    Assert.assertEquals("la date doit etre correcte", storedDate, dateRecup);

  }

  @Test
  public void testVersionRndNumero() throws ParameterNotFoundException {
    final String numVersion = "11.4";
    parametersService.setVersionRndNumero(numVersion);

    final String numVersionRecup = parametersService.getVersionRndNumero();

    Assert.assertEquals("la numéro de version RND doit être correct", numVersion, numVersionRecup);

  }

  @Test
  public void testPurgeCorbeilleDuree() throws ParameterNotFoundException {
    final Integer duree = 3;
    parametersService.setPurgeCorbeilleDuree(duree);

    final Integer dureeRetour = parametersService.getPurgeCorbeilleDuree();

    Assert.assertEquals("la durée doit etre correcte", duree, dureeRetour);

  }

  @Test
  public void testPurgeCorbeilleRunning() throws ParameterNotFoundException {
    final Boolean storedRunning = true;
    parametersService.setPurgeCorbeilleIsRunning(storedRunning);

    final Boolean running = parametersService.isPurgeCorbeilleIsRunning();

    Assert.assertEquals("l'indicateur doit etre correct", storedRunning, running);

  }

  @Test
  public void testPurgeCorbeilleDateDebut() throws ParameterNotFoundException {
    final Date storedDate = DateUtils.addDays(date, -3);
    parametersService.setPurgeCorbeilleDateDebutPurge(storedDate);

    final Date dateRecup = parametersService.getPurgeCorbeilleDateDebutPurge();

    final Date dateAttendue = DateUtils.truncate(storedDate, Calendar.DATE);

    Assert.assertEquals("la date doit etre correcte", dateAttendue, dateRecup);

  }

  @Test
  public void testPurgeCorbeilleDateLancement() throws ParameterNotFoundException {
    final Date storedDate = DateUtils.addDays(date, -6);
    parametersService.setPurgeCorbeilleDateLancement(storedDate);
    final Date dateRecup = parametersService.getPurgeCorbeilleDateLancement();

    Assert.assertEquals("la date doit etre correcte", storedDate, dateRecup);

  }

  @Test
  public void testPurgeCorbeilleDateSucces() throws ParameterNotFoundException {
    final Date storedDate = DateUtils.addDays(date, -6);
    parametersService.setPurgeCorbeilleDateSucces(storedDate);
    final Date dateRecup = parametersService.getPurgeCorbeilleDateSucces();

    Assert.assertEquals("la date doit etre correcte", storedDate, dateRecup);

  }
}
