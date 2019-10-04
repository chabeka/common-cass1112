package fr.urssaf.image.sae.rnd.dao.support;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.rnd.modele.Correspondance;
import fr.urssaf.image.sae.rnd.modele.EtatCorrespondance;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-rnd-test.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CorrespondancesRndSupportTest {

  @Autowired
  private CorrespondancesRndSupport correspondancesRndSupport;

  @Autowired
  private CassandraServerBean server;

  @Autowired
  private JobClockSupport jobClockSupport;

  @After
  public void after() throws Exception {
    server.resetDataOnly();
  }

  @Test
  public void init() {
    try {
      if (server.isCassandraStarted()) {
        server.resetData();
      }
      Assert.assertTrue(true);

    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAjouterCorrespondancesRndSuccess() {

    final Correspondance correspondance = new Correspondance();
    correspondance.setCodeDefinitif("1.1.1.1.1");
    correspondance.setCodeTemporaire("a.a.a.a.a");
    correspondance.setDateDebutMaj(new Date());
    correspondance.setDateFinMaj(new Date());
    correspondance.setEtat(EtatCorrespondance.CREATED);
    correspondance.setVersionCourante("11.4");

    correspondancesRndSupport.ajouterCorrespondance(correspondance,
                                                    jobClockSupport.currentCLock());

    Correspondance correspondanceTrouvee = null;

    try {
      correspondanceTrouvee = correspondancesRndSupport.find("a.a.a.a.a", "11.4");
      Assert.assertEquals("Les correspondances doivent être identiques",
                          correspondanceTrouvee.getCodeTemporaire(), correspondance
                          .getCodeTemporaire());
      Assert.assertEquals("Les correspondances doivent être identiques",
                          correspondanceTrouvee.getCodeDefinitif(), correspondance
                          .getCodeDefinitif());
    } catch (final Exception exception) {
      Assert.fail("aucune erreur attendue");
    }

    try {
      correspondanceTrouvee = correspondancesRndSupport.find("b.a.a.a.a", "11.4");
      Assert.assertEquals("La correspondance doit être null", null,
                          correspondanceTrouvee);
    } catch (final Exception exception) {
      Assert.fail("aucune erreur attendue");
    }
  }

  @Test
  public void testGetAllCorrespondancesSuccess() {

    final Correspondance correspondance1 = new Correspondance();
    correspondance1.setCodeDefinitif("1.1.1.1.1");
    correspondance1.setCodeTemporaire("a.a.a.a.a");
    correspondance1.setDateDebutMaj(new Date());
    correspondance1.setDateFinMaj(new Date());
    correspondance1.setEtat(EtatCorrespondance.CREATED);

    correspondancesRndSupport.ajouterCorrespondance(correspondance1,
                                                    jobClockSupport.currentCLock());

    final Correspondance correspondance2 = new Correspondance();
    correspondance2.setCodeDefinitif("2.2.2.2.2");
    correspondance2.setCodeTemporaire("b.b.b.b.b");
    correspondance2.setDateDebutMaj(new Date());
    correspondance2.setDateFinMaj(new Date());
    correspondance2.setEtat(EtatCorrespondance.CREATED);

    correspondancesRndSupport.ajouterCorrespondance(correspondance2,
                                                    jobClockSupport.currentCLock());

    List<Correspondance> listeCorrespondances = null;

    try {
      listeCorrespondances = correspondancesRndSupport
          .getAllCorrespondances();

      assertNotNull("La liste des correspondances ne doit pas être nulle", listeCorrespondances);
      Assert.assertEquals("Il doit y avoir 2 correspondance", 2,
                          listeCorrespondances.size());

    } catch (final Exception exception) {
      Assert.fail("aucune erreur attendue");
    }

  }

}
