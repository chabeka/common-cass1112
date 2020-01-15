/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.commons;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.SequencesCql;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.commons.bo.Parameter;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.bo.cql.ParameterCql;
import fr.urssaf.image.sae.commons.support.ParametersSupport;
import fr.urssaf.image.sae.commons.support.cql.ParametersCqlSupport;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import fr.urssaf.image.sae.utils.CompareUtils;




/**
 * (AC75095351) Classe de test migration des parameters
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-migration-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)

public class MigrationParametersTest {

  @Autowired
  JobClockSupport clockSupport;

  @Autowired
  private ParametersCqlSupport supportCql;

  @Autowired
  private ParametersSupport supportThrift;

  @Autowired
  MigrationParameters migrationParameters;

  @Autowired
  private CassandraServerBean server;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationParametersTest.class);



  final ParameterRowType PARAMETER_ROW_TYPE = ParameterRowType.CORBEILLE;

  ParameterType[] listTypes = {ParameterType.PURGE_CORBEILLE_DATE_DEBUT_PURGE, ParameterType.PURGE_CORBEILLE_DATE_LANCEMENT,
                               ParameterType.PURGE_CORBEILLE_DATE_SUCCES,
                               ParameterType.PURGE_CORBEILLE_DUREE, ParameterType.PURGE_CORBEILLE_IS_RUNNING};

  Object[] listValues = {"Fri May 31 00:00:00 CEST 2019", "Thu Jun 20 03:52:13 CEST 2019", "Thu Jun 20 03:52:13 CEST 2019",
                         Long.valueOf("20"), new Boolean(false)};

  @After
  public void after() throws Exception {
    server.resetData();
  }

  /**
   * Migration des données Parameters vers parameterscql
   */
  @Test
  public void migrationFromThriftToCql() {
    try {

      populateTableThrift();
      final List<ParameterCql> listAllFromThrift = migrationParameters.getListParametersCqlFromThrift();

      migrationParameters.migrationFromThriftToCql();

      final List<ParameterCql> listCql = supportCql.findAll();

      Assert.assertEquals(listAllFromThrift.size(), listTypes.length);
      Assert.assertEquals(listAllFromThrift.size(), listCql.size());
      Assert.assertTrue(CompareUtils.compareListsGeneric(listAllFromThrift, listCql));

    }
    catch (final Exception ex) {

      LOGGER.debug("exception=" + ex);
      Assert.assertTrue(false);
    }
  }

  private void populateTableThrift() {


    Parameter parameter;
    int i = 0;
    for (final ParameterType code : listTypes) {
      parameter = new Parameter(listTypes[i], listValues[i]);

      supportThrift.create(parameter, PARAMETER_ROW_TYPE, new Date().getTime());
      i++;
    }

  }

  /**
   * Migration des données parameterscql vers DroitParameters
   */
  @Test
  public void migrationFromCqlTothrift() {

    populateTableCql();
    migrationParameters.migrationFromCqlTothrift();
    final List<ParameterCql> listAllFromThrift = migrationParameters.getListParametersCqlFromThrift();
    final List<ParameterCql> listCql = supportCql.findAll();
    Assert.assertEquals(listAllFromThrift.size(), listCql.size());
    Assert.assertTrue(CompareUtils.compareListsGeneric(listAllFromThrift, listCql));

  }

  /**
   * On crée les enregistrements dans la table droitparameterscql
   */
  private void populateTableCql() {

    int i = 0;
    for (final ParameterType code : listTypes) {
      final ParameterCql parametersCql = new ParameterCql();
      parametersCql.setTypeParameters(ParameterRowType.CORBEILLE);
      parametersCql.setName(listTypes[i]);
      parametersCql.setValue(listValues[i]);
      supportCql.create(parametersCql, clockSupport.currentCLock());
      i++;
    }

  }
  @Test
  public void diffAddTest() {

    populateTableThrift();
    migrationParameters.migrationFromThriftToCql();

    final List<ParameterCql> listThrift = migrationParameters.getListParametersCqlFromThrift();

     ParameterCql parameterCql = new ParameterCql();
     parameterCql.setTypeParameters(ParameterRowType.CORBEILLE);
    parameterCql.setName(ParameterType.JOURNALISATION_EVT_META_TITRE);
    parameterCql.setValue("Fri May 31 00:00:00 CEST 2019");
    supportCql.create(parameterCql);
    final List<ParameterCql> listCql = supportCql.findAll();
    final Diff diff = migrationParameters.compareParameters(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("NewObject{ new object: fr.urssaf.image.sae.commons.bo.cql.ParameterCql/JOURNALISATION_EVT_META_TITRE,parametresCorbeille }"));

  }

  @Test
  public void diffDescTest() {

    populateTableThrift();
    migrationParameters.migrationFromThriftToCql();

    final List<ParameterCql> listThrift =  migrationParameters.getListParametersCqlFromThrift();
    final List<ParameterCql> listCql = supportCql.findAll();
    listCql.get(0).setValue("Thu Jun 20 03:52:13 CEST 2020");
    final Diff diff = migrationParameters.compareParameters(listThrift, listCql);
    Assert.assertTrue(diff.hasChanges());
    final String changes = diff.getChanges().get(0).toString();
    Assert.assertTrue(changes.equals("ValueChange{ 'value' value changed from 'Fri May 31 00:00:00 CEST 2019' to 'Thu Jun 20 03:52:13 CEST 2020' }"));

  }
 

}
