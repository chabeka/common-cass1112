package fr.urssaf.image.sae.commons.service;

import java.util.Date;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-commons-test.xml" })
public class ParametersServiceTest {

   @Autowired
   private ParametersService parametersService;

   @Autowired
   private CassandraServerBean server;

   private Date date = new Date();

   @After
   public void end() throws Exception {
      server.resetData();
   }

   @Test
   public void testJournalisationEvtDate() throws ParameterNotFoundException {
      Date storedDate = DateUtils.addHours(date, 1);
      parametersService.setJournalisationEvtDate(storedDate);

      Date dateRecup = parametersService.getJournalisationEvtDate();

      Assert.assertEquals("la date doit etre correcte", storedDate, dateRecup);

   }

   @Test
   public void testPurgeEvtDate() throws ParameterNotFoundException {
      Date storedDate = DateUtils.addHours(date, 2);
      parametersService.setPurgeEvtDate(storedDate);

      Date dateRecup = parametersService.getPurgeEvtDate();

      Assert.assertEquals("la date doit etre correcte", storedDate, dateRecup);

   }

   @Test
   public void testPurgeExploitDate() throws ParameterNotFoundException {
      Date storedDate = DateUtils.addHours(date, 3);
      parametersService.setPurgeExploitDate(storedDate);

      Date dateRecup = parametersService.getPurgeExploitDate();

      Assert.assertEquals("la date doit etre correcte", storedDate, dateRecup);

   }

   @Test
   public void testPurgeSecuDate() throws ParameterNotFoundException {
      Date storedDate = DateUtils.addHours(date, 4);
      parametersService.setPurgeSecuDate(storedDate);

      Date dateRecup = parametersService.getPurgeSecuDate();

      Assert.assertEquals("la date doit etre correcte", storedDate, dateRecup);

   }

   @Test
   public void testPurgeTechDate() throws ParameterNotFoundException {
      Date storedDate = DateUtils.addHours(date, 5);
      parametersService.setPurgeTechDate(storedDate);

      Date dateRecup = parametersService.getPurgeTechDate();

      Assert.assertEquals("la date doit etre correcte", storedDate, dateRecup);

   }

   @Test
   public void testPurgeEvtDuree() throws ParameterNotFoundException {
      Integer storedDuree = 1;
      parametersService.setPurgeEvtDuree(storedDuree);

      Integer duree = parametersService.getPurgeEvtDuree();

      Assert.assertEquals("la durée doit etre correcte", storedDuree, duree);

   }

   @Test
   public void testPurgeExploitDuree() throws ParameterNotFoundException {
      Integer storedDuree = 2;
      parametersService.setPurgeExploitDuree(storedDuree);

      Integer duree = parametersService.getPurgeExploitDuree();

      Assert.assertEquals("la durée doit etre correcte", storedDuree, duree);

   }

   @Test
   public void testPurgeSecuDuree() throws ParameterNotFoundException {
      Integer storedDuree = 3;
      parametersService.setPurgeSecuDuree(storedDuree);

      Integer duree = parametersService.getPurgeSecuDuree();

      Assert.assertEquals("la durée doit etre correcte", storedDuree, duree);

   }

   @Test
   public void testPurgeTechDuree() throws ParameterNotFoundException {
      Integer storedDuree = 4;
      parametersService.setPurgeTechDuree(storedDuree);

      Integer duree = parametersService.getPurgeTechDuree();

      Assert.assertEquals("la durée doit etre correcte", storedDuree, duree);

   }

   @Test
   public void testJournalisationEvtRunning() throws ParameterNotFoundException {
      Boolean storedRunning = false;
      parametersService.setJournalisationEvtIsRunning(storedRunning);

      Boolean running = parametersService.isJournalisationEvtIsRunning();

      Assert.assertEquals("l'indicateur doit etre correct", storedRunning,
            running);

   }

   @Test
   public void testPurgeEvtRunning() throws ParameterNotFoundException {
      Boolean storedRunning = true;
      parametersService.setPurgeEvtIsRunning(storedRunning);

      Boolean running = parametersService.isPurgeEvtIsRunning();

      Assert.assertEquals("l'indicateur doit etre correct", storedRunning,
            running);

   }

   @Test
   public void testPurgeExploitRunning() throws ParameterNotFoundException {
      Boolean storedRunning = false;
      parametersService.setPurgeExploitIsRunning(storedRunning);

      Boolean running = parametersService.isPurgeExploitIsRunning();

      Assert.assertEquals("l'indicateur doit etre correct", storedRunning,
            running);

   }

   @Test
   public void testPurgeSecuRunning() throws ParameterNotFoundException {
      Boolean storedRunning = true;
      parametersService.setPurgeSecuIsRunning(storedRunning);

      Boolean running = parametersService.isPurgeSecuIsRunning();

      Assert.assertEquals("l'indicateur doit etre correct", storedRunning,
            running);

   }

   @Test
   public void testPurgeTechRunning() throws ParameterNotFoundException {
      Boolean storedRunning = false;
      parametersService.setPurgeTechIsRunning(storedRunning);

      Boolean running = parametersService.isPurgeTechIsRunning();

      Assert.assertEquals("l'indicateur doit etre correct", storedRunning,
            running);

   }

   @Test
   public void testHash() throws ParameterNotFoundException {
      String storedHash = "12345678901234567890";

      parametersService.setJournalisationEvtHashJournPrec(storedHash);

      String hash = parametersService.getJournalisationEvtHashJournPrec();

      Assert.assertEquals("le hash doit etre correct", storedHash, hash);
   }

   @Test
   public void testId() throws ParameterNotFoundException {
      String storedId = UUID.randomUUID().toString();

      parametersService.setJournalisationEvtIdJournPrec(storedId);

      String id = parametersService.getJournalisationEvtIdJournPrec();

      Assert.assertEquals("le hash doit etre correct", storedId, id);
   }

   @Test
   public void testMetaApplProd() throws ParameterNotFoundException {
      String storedMeta = "applProd";

      parametersService.setJournalisationEvtMetaApplProd(storedMeta);

      String meta = parametersService.getJournalisationEvtMetaApplProd();

      Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
   }

   @Test
   public void testMetaApplTrait() throws ParameterNotFoundException {
      String storedMeta = "applTrait";

      parametersService.setJournalisationEvtMetaApplTrait(storedMeta);

      String meta = parametersService.getJournalisationEvtMetaApplTrait();

      Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
   }

   @Test
   public void testMetaCodeOrga() throws ParameterNotFoundException {
      String storedMeta = "codeOrga";

      parametersService.setJournalisationEvtMetaCodeOrga(storedMeta);

      String meta = parametersService.getJournalisationEvtMetaCodeOrga();

      Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
   }

   @Test
   public void testMetaCodeRnd() throws ParameterNotFoundException {
      String storedMeta = "codeRnd";

      parametersService.setJournalisationEvtMetaCodeRnd(storedMeta);

      String meta = parametersService.getJournalisationEvtMetaCodeRnd();

      Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
   }

   @Test
   public void testMetaTitre() throws ParameterNotFoundException {
      String storedMeta = "titre sauvegardé";

      parametersService.setJournalisationEvtMetaTitre(storedMeta);

      String meta = parametersService.getJournalisationEvtMetaTitre();

      Assert.assertEquals("le hash doit etre correct", storedMeta, meta);
   }

   @Test
   public void testVersionRndDateMaj() throws ParameterNotFoundException {
      Date storedDate = DateUtils.addHours(date, 1);
      parametersService.setVersionRndDateMaj(storedDate);

      Date dateRecup = parametersService.getVersionRndDateMaj();

      Assert.assertEquals("la date doit etre correcte", storedDate, dateRecup);

   }
   
   @Test
   public void testVersionRndNumero() throws ParameterNotFoundException {
      String numVersion = "11.4";
      parametersService.setVersionRndNumero(numVersion);

      String numVersionRecup = parametersService.getVersionRndNumero();

      Assert.assertEquals("la numéro de version RND doit être correct", numVersion, numVersionRecup);

   }
   
}
