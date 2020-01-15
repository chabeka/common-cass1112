/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droits;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.javers.core.diff.Diff;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.droit.MigrationPrmd;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.droit.dao.support.PrmdSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.PrmdCqlSupport;
import fr.urssaf.image.sae.utils.CompareUtils;



/**
 * (AC75095351) Classe de test migration des prmd
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationPrmdTest {

  private static final Date DATE = new Date();

  @Autowired
  private PrmdCqlSupport supportCql;

  @Autowired
  private PrmdSupport supportThrift;

  @Autowired
  MigrationPrmd migrationPrmd;

  @Autowired
  private CassandraServerBean server;


  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPrmdTest.class);

  String[] listCode = {"PRMD_V2_PI06I_PI06_L09", "PRMD_V2_PCA1W_PCA1_L07", "PRMD_V2_PD71D_PD71_L03",
                       "PRMD_V2_PI01H_PI01_L07", "PRMD_V2_QP38A_RP38_L01", "PRMD_V2_QCA1B_QCA1_L21"};

  String[] listDescription = {"PI06I PI06.L09", "V2 - PCA1W PCA1.L07", "V2 - PD71D PD71.L035",
                              "V2 - PI01H PI01.L07", "QP38A RP38.L01", "V2 - QCA1B QCA1.L21"};

  String[] listLucene = {"CodeRND:2.1.4.5.1 AND ApplicationProductrice:ADELAIDE AND DomaineCotisant:true",
                         "(CodeRND:3.2.1.5.1 OR CodeRND:3.1.2.1.1 OR CodeRND:3.1.2.4.1 OR CodeRND:3.1.3.1.2 OR CodeRND:3.1.3.2.3 OR CodeRND:3.1.3.A.X"
                             + " OR CodeRND:3.2.1.1.4 OR CodeRND:3.2.1.5.2 OR CodeRND:3.2.2.3.1 OR CodeRND:3.2.2.3.5 OR CodeRND:3.2.3.2.2 OR CodeRND:3.2.4.1.0"
                             + " OR CodeRND:3.2.4.1.8 OR CodeRND:3.2.4.C.X OR CodeRND:3.3.1.1.2 OR CodeRND:3.3.1.1.2 OR CodeRND:3.B.X.X.X OR CodeRND:3.D.X.X.X)"
                             + " AND ApplicationProductrice:ADELAIDE AND DomaineCotisant:true",
                             "CodeRND:2.1.1.2.1 AND ApplicationProductrice:ADELAIDE AND DomaineCotisant:true",
                             "CodeRND:1.2.1.C.X AND ApplicationProductrice:ADELAIDE AND DomaineCotisant:true",
                             "CodeRND:2.2.3.2.2 AND ApplicationProductrice:ADELAIDE AND DomaineCotisant:true",
  "CodeRND:3.1.3.1.2 AND ApplicationProductrice:ADELAIDE AND DomaineCotisant:true"};

  String[] listMetadata = {"<?xml version='1.0' encoding='UTF-8'?><map><entry><string>CodeRND</string><list><string>2.1.4.5.1</string></list></entry><entry><string>FormatFichier</string><list><string>fmt/354</string></list></entry><entry><string>ApplicationProductrice</string><list><string>ADELAIDE</string></list></entry><entry><string>DomaineCotisant</string><list><string>true</string></list></entry></map>",
                           "<?xml version='1.0' encoding='UTF-8'?><map><entry><string>CodeRND</string><list><string>3.2.1.5.1</string><string>3.1.2.1.1</string><string>3.1.2.4.1</string><string>3.1.3.1.2</string><string>3.1.3.2.3</string><string>3.1.3.A.X</string><string>3.2.1.1.4</string><string>3.2.1.5.2</string><string>3.2.2.3.1</string><string>3.2.2.3.5</string><string>3.2.3.2.2</string><string>3.2.4.1.0</string><string>3.2.4.1.8</string><string>3.2.4.C.X</string><string>3.3.1.1.2</string><string>3.3.1.1.2</string><string>3.B.X.X.X</string><string>3.D.X.X.X</string></list></entry><entry><string>FormatFichier</string><list><string>fmt/354</string></list></entry><entry><string>ApplicationProductrice</string><list><string>ADELAIDE</string></list></entry><entry><string>DomaineCotisant</string><list><string>true</string></list></entry></map>",
                           "<?xml version='1.0' encoding='UTF-8'?><map><entry><string>CodeRND</string><list><string>2.1.1.2.1</string></list></entry><entry><string>FormatFichier</string><list><string>fmt/354</string></list></entry><entry><string>ApplicationProductrice</string><list><string>ADELAIDE</string></list></entry><entry><string>DomaineCotisant</string><list><string>true</string></list></entry></map>",
                           "<?xml version='1.0' encoding='UTF-8'?><map><entry><string>CodeRND</string><list><string>1.2.1.C.X</string></list></entry><entry><string>FormatFichier</string><list><string>fmt/354</string></list></entry><entry><string>ApplicationProductrice</string><list><string>ADELAIDE</string></list></entry><entry><string>DomaineCotisant</string><list><string>true</string></list></entry></map>",
                           "<?xml version='1.0' encoding='UTF-8'?><map><entry><string>CodeRND</string><list><string>2.2.3.2.2</string></list></entry><entry><string>FormatFichier</string><list><string>fmt/354</string></list></entry><entry><string>ApplicationProductrice</string><list><string>ADELAIDE</string></list></entry><entry><string>DomaineCotisant</string><list><string>true</string></list></entry></map>",
  "<?xml version='1.0' encoding='UTF-8'?><map><entry><string>CodeRND</string><list><string>3.1.3.1.2</string></list></entry><entry><string>FormatFichier</string><list><string>fmt/354</string></list></entry><entry><string>ApplicationProductrice</string><list><string>ADELAIDE</string></list></entry><entry><string>DomaineCotisant</string><list><string>true</string></list></entry></map>"};

  @After
  public void after() throws Exception {
    server.resetData(true, MODE_API.HECTOR);
    server.resetData(false, MODE_API.DATASTAX);
  }

  /**
   * Migration des données DroitPrmd vers droitprmdcql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {
      populateTableThrift();
      migrationPrmd.migrationFromThriftToCql();
      final List<Prmd> listThrift = supportThrift.findAll();
      final List<Prmd> listCql = supportCql.findAll();

      Assert.assertEquals(listThrift.size(), listCode.length);
      Assert.assertEquals(listThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
    }
    catch (final Exception ex) {
      LOGGER.debug("exception=" + ex);
    }
  }

  /**
   * Migration des données droitprmdcql vers DroitPrmd
   */
  @Test
  public void migrationFromCqlTothrift() throws UnsupportedEncodingException {

    populateTableCql();
    migrationPrmd.migrationFromCqlTothrift();

    final List<Prmd> listThrift = supportThrift.findAll();
    final List<Prmd> listCql = supportCql.findAll();

    Assert.assertEquals(listCql.size(), listCode.length);
    Assert.assertEquals(listThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listThrift, listCql));
  }

  /**
   * On crée les enregistrements dans la table droitprmdcql
   */
  private void populateTableCql() throws UnsupportedEncodingException {
    int i = 0;
    for (final String code : listCode) {
      final Prmd prmd = new Prmd();
      prmd.setCode(code);
      prmd.setDescription(listDescription[i]);
      prmd.setLucene(listLucene[i]);
      prmd.setMetadata(MapSerializer.get().fromBytes(listMetadata[i].getBytes("UTF-8")));

      supportCql.create(prmd);
      i++;
    }
  }

  /**
   * On crée les enregistrements dans la table DroitPrmd
   */
  private void populateTableThrift() throws UnsupportedEncodingException {
    int i = 0;
    for (final String code : listCode) {
      final Prmd prmd = new Prmd();
      prmd.setCode(code);
      prmd.setDescription(listDescription[i]);
      prmd.setLucene(listLucene[i]);
      prmd.setMetadata(MapSerializer.get().fromBytes(listMetadata[i].getBytes("UTF-8")));

      supportThrift.create(prmd, new Date().getTime());
      i++;
    }
  }
  @Test
  public void diffAddTest() throws Exception {
    populateTableThrift();
    migrationPrmd.migrationFromThriftToCql();
    final List<Prmd> listThrift = supportThrift.findAll();
    final Prmd prmd = new Prmd();
    prmd.setCode("CODEADD");
    supportCql.create(prmd);
    final List<Prmd> listCql = supportCql.findAll();
    final Diff diff = migrationPrmd.comparePrmds(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: fr.urssaf.image.sae.droit.dao.model.Prmd/CODEADD }"));
  }

  @Test
  public void diffDescTest() throws Exception {
    populateTableThrift();
    migrationPrmd.migrationFromThriftToCql();

    final List<Prmd> listThrift = supportThrift.findAll();
    final List<Prmd> listCql = supportCql.findAll();

    listCql.get(0).setDescription("DESCDIFF");
    
    final Diff diff = migrationPrmd.comparePrmds(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
     Assert.assertTrue(changes.equals("ValueChange{ 'description' value changed from 'QP38A RP38.L01' to 'DESCDIFF' }"));
  }
}
