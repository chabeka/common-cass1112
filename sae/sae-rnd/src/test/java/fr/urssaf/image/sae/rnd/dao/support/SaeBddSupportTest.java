package fr.urssaf.image.sae.rnd.dao.support;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.rnd.dao.support.CorrespondancesRndSupport;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.dao.support.SaeBddSupport;
import fr.urssaf.image.sae.rnd.exception.SaeBddRuntimeException;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.rnd.modele.VersionRnd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
public class SaeBddSupportTest {

   @Autowired
   private SaeBddSupport saeBddSupport;

   @Autowired
   private RndSupport rndSupport;

   @Autowired
   private CorrespondancesRndSupport correspondanceRndSupport;

   @Autowired
   private JobClockSupport jobClockSupport;

   @Autowired
   private CassandraServerBean server;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test
   public void testUpdateVersionRnd() throws SaeBddRuntimeException, ParameterNotFoundException {

      VersionRnd versionRnd = new VersionRnd();
      Date date = new Date();
      versionRnd.setDateMiseAJour(date);
      versionRnd.setVersionEnCours("11.4");

      // 1ère création du paramètre
      saeBddSupport.updateVersionRnd(versionRnd);

      VersionRnd versionRecup = saeBddSupport.getVersionRnd();

      assertNotNull("La version récupérée ne doit pas être nulle", versionRecup);

      Assert.assertEquals("Le numéro de version doit être identique",
            versionRecup.getVersionEnCours(), versionRnd.getVersionEnCours());

      Assert.assertEquals("La date de MAJ de la version doit être identique",
            versionRecup.getDateMiseAJour(), versionRnd.getDateMiseAJour());

      // Modification d'un paramètre
      versionRnd.setDateMiseAJour(new Date());
      versionRnd.setVersionEnCours("11.5");
      saeBddSupport.updateVersionRnd(versionRnd);
      versionRecup = saeBddSupport.getVersionRnd();

      assertNotNull("La version récupérée ne doit pas être nulle", versionRecup);

      Assert.assertEquals("Le numéro de version doit être identique",
            versionRecup.getVersionEnCours(), versionRnd.getVersionEnCours());

      Assert.assertEquals("La date de MAJ de la version doit être identique",
            versionRecup.getDateMiseAJour(), versionRnd.getDateMiseAJour());

   }

   @Test
   public void testUpdateCorrespondances() throws SaeBddRuntimeException {

      Map<String, String> listeCorrespondances = new HashMap<String, String>();

      String codeTempo1 = "t.1.1.1.1";
      String codeTempo2 = "t.2.2.2.2";
      String codeDef1 = "d.1.1.1.1";
      String codeDef2 = "d.2.2.2.2";

      listeCorrespondances.put(codeTempo1, codeDef1);
      listeCorrespondances.put(codeTempo2, codeDef2);

      // On ajoute les types de document pour vérifier que son état est bien
      // clôturé après la mise à jour
      TypeDocument typeDoc1 = new TypeDocument();
      typeDoc1.setCode(codeTempo1);
      typeDoc1.setCodeActivite("1");
      typeDoc1.setCodeFonction("1");
      typeDoc1.setDureeConservation(300);
      typeDoc1.setLibelle("Type tempo 1");
      typeDoc1.setCloture(false);
      typeDoc1.setType(TypeCode.TEMPORAIRE);

      TypeDocument typeDoc2 = new TypeDocument();
      typeDoc2.setCode(codeTempo2);
      typeDoc2.setCodeActivite("2");
      typeDoc2.setCodeFonction("2");
      typeDoc2.setDureeConservation(300);
      typeDoc2.setLibelle("Type tempo 2");
      typeDoc2.setCloture(false);
      typeDoc2.setType(TypeCode.TEMPORAIRE);

      rndSupport.ajouterRnd(typeDoc1, jobClockSupport.currentCLock());
      rndSupport.ajouterRnd(typeDoc2, jobClockSupport.currentCLock());

      saeBddSupport.updateCorrespondances(listeCorrespondances);

      List<Correspondance> listeRecup = saeBddSupport.getAllCorrespondances();

      for (Correspondance correspondance : listeRecup) {
         Assert.assertEquals("Les correspondances doivent être égales",
               listeCorrespondances.get(correspondance.getCodeTemporaire()),
               correspondance.getCodeDefinitif());
         Assert.assertEquals(
               "Les correspondances doivent être à l'état CREATED", "CREATED",
               correspondance.getEtat().toString());
      }

      // On vérifie que les types de doc sont clôturés
      TypeDocument typeDocRecup1 = rndSupport.getRnd(codeTempo1);
      Assert.assertEquals("Le type de doc tempo1 doit être clôturé",
            typeDocRecup1.isCloture(), true);
      TypeDocument typeDocRecup2 = rndSupport.getRnd(codeTempo2);
      Assert.assertEquals("Le type de doc tempo2 doit être clôturé",
            typeDocRecup2.isCloture(), true);

   }

   @Test
   public void testStartMajCorrespondances() throws SaeBddRuntimeException {

      Correspondance correspondance = new Correspondance();
      correspondance.setCodeDefinitif("1.1.1.1.1");
      correspondance.setCodeTemporaire("1.1.1.1.1");
      correspondance.setEtat(EtatCorrespondance.CREATED);

      correspondanceRndSupport.ajouterCorrespondance(correspondance,
            jobClockSupport.currentCLock());

      saeBddSupport.startMajCorrespondance(correspondance);
      List<Correspondance> liste = correspondanceRndSupport
            .getAllCorrespondances();

      Assert.assertEquals("La correspondance doit être à l'état STARTING",
            liste.get(0).getEtat(), EtatCorrespondance.STARTING);

      assertNotNull("La date de début ne doit plus être nulle", liste.get(0)
            .getDateDebutMaj());

   }

   @Test
   public void testUpdateRnd() throws SaeBddRuntimeException {

      TypeDocument typeDoc1 = new TypeDocument();
      typeDoc1.setCloture(false);
      typeDoc1.setCode("1.1.1.1.1");
      typeDoc1.setCodeActivite("1");
      typeDoc1.setCodeFonction("2");
      typeDoc1.setDureeConservation(300);
      typeDoc1.setLibelle("Libellé 1.1.1.1.1");
      typeDoc1.setType(TypeCode.ARCHIVABLE_AED);

      TypeDocument typeDoc2 = new TypeDocument();
      typeDoc2.setCloture(false);
      typeDoc2.setCode("2.2.2.2.2");
      typeDoc2.setCodeActivite("1");
      typeDoc2.setCodeFonction("2");
      typeDoc2.setDureeConservation(300);
      typeDoc2.setLibelle("Libellé 2.2.2.2.2");
      typeDoc2.setType(TypeCode.ARCHIVABLE_AED);

      List<TypeDocument> liste = new ArrayList<TypeDocument>();
      liste.add(typeDoc1);
      liste.add(typeDoc2);

      saeBddSupport.updateRnd(liste);
      TypeDocument typeDocRecup1 = rndSupport.getRnd("1.1.1.1.1");
      TypeDocument typeDocRecup2 = rndSupport.getRnd("2.2.2.2.2");

      assertNotNull("Le type de doc 1 doit exister", typeDocRecup1);
      assertNotNull("Le type de doc 2 doit exister", typeDocRecup2);

      Assert.assertEquals("Les types de doc doivent être égaux", typeDoc1,
            typeDocRecup1);
      Assert.assertEquals("Les types de doc doivent être égaux", typeDoc2,
            typeDocRecup2);

   }

}
