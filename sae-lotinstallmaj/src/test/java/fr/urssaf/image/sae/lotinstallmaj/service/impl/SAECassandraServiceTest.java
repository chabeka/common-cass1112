package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotGeneralException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-lotinstallmaj-test.xml" })
public class SAECassandraServiceTest {
   
   private SAECassandraDao saeDao;

  // private CassandraConfig config;
   @Autowired
   private SAECassandraService service;

   @Before
   public final void init() throws Exception, IOException,
         InterruptedException, ConfigurationException {
      saeDao = EasyMock.createMock(SAECassandraDao.class);
      service.setSaeDao(saeDao);
   }

   @After
   public void end() {
      EasyMock.reset(saeDao);
      // EasyMock.reset(keyspace);
   }

   /**
    * Cas de test : on vérifie que les runtimes sont catchés
    * 
    * @throws HectorException
    */
   @Test
   public void createColumnFamillyFromDefinitionTestException() {

      // pour tester l'existence d'un CF on instancie une CF qu'on va ajouter à la liste des CF à créer et à la liste des CF existants
      ColumnFamilyDefinition c = HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitContratService", ComparatorType.UTF8TYPE);
      
      // liste des CF existants
      List<ColumnFamilyDefinition> cfDefsTrue = new ArrayList<ColumnFamilyDefinition>();
      // DroitContratService
      cfDefsTrue.add(c);      

   // liste des CF inexistants
      List<ColumnFamilyDefinition> cfDefsFalse = new ArrayList<ColumnFamilyDefinition>();
      cfDefsFalse.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitPagma1", ComparatorType.UTF8TYPE));
      cfDefsFalse.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitPagmp1", ComparatorType.UTF8TYPE));
      cfDefsFalse.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitPagmp2", ComparatorType.UTF8TYPE));

      // résultat attendu pour les CF existants et inexistants
      EasyMock.expect(
            saeDao.getColumnFamilyDefintion()).andReturn(cfDefsTrue).times(1);
      EasyMock.expect(
            saeDao.getColumnFamilyDefintion()).andReturn(cfDefsFalse).times(3);
      
      // résultat attendu pour la création de CF. 
      saeDao.createColumnFamily(EasyMock
            .anyObject(ColumnFamilyDefinition.class), EasyMock.anyBoolean());
      EasyMock.expectLastCall().times(1);
      EasyMock.expectLastCall().andThrow(new HectorException("erreur")).times(1);
      EasyMock.expectLastCall().times(2);
      // enregistrement du scénario
      EasyMock.replay(saeDao);

      // données de test

      // Liste des CF à créer 
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

      // DroitContratService
      cfDefs.add(c);

      // DroitPagm
      cfDefs.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitPagm", ComparatorType.UTF8TYPE));

      // DroitPagma
      cfDefs.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitPagma", ComparatorType.UTF8TYPE));

      // DroitPagmp
      cfDefs.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitPagmp", ComparatorType.UTF8TYPE));

      service.createColumnFamilyFromList(cfDefs, true);

      EasyMock.verify(saeDao);

   }

   /**
    * Cas de test : on vérifie la sortie en cas de succès.
    * 
    * @throws HectorException
    */
   @Test
   public void createColumnFamillyFromDefinition3foisTestException()
         throws MajLotGeneralException {
      // on provoque 2 exceptions pour vérifier qu'il y a bien 3 tentatives
      saeDao.createColumnFamily(EasyMock
            .anyObject(ColumnFamilyDefinition.class), EasyMock.anyBoolean());
      EasyMock.expectLastCall().andThrow(new HectorException("erreur"))
            .times(2);
      // la dernière tentative doit être un succès
      saeDao.createColumnFamily(EasyMock
            .anyObject(ColumnFamilyDefinition.class), EasyMock.anyBoolean());
      EasyMock.expectLastCall().times(1);
      // enregistrement du scénario
      EasyMock.replay(saeDao);
      // création d'une définition quelconque pour pouvoir appeller la méthode
      ColumnFamilyDefinition cfDefs = HFactory.createColumnFamilyDefinition(
            service.getKeySpaceName(), "DroitContratService",
            ComparatorType.UTF8TYPE);
      // appel de la creation de column family avec 3 tentatives
      service.createColumnFamillyFromDefinition(cfDefs, true, 3);
      // vérification du résultat
      EasyMock.verify(saeDao);
   }
/**
 * On vérifie la condition de sortie après 3 échecs et le passage à la création du la prochaine CF.
 */
   @Test
   public void createColumnFamillyFromListTestException() {

      saeDao.createColumnFamily(EasyMock
            .anyObject(ColumnFamilyDefinition.class), EasyMock.anyBoolean());
      EasyMock.expectLastCall().andThrow(new HectorException("erreur"))
            .times(9);
      // liste des CF inexistants
      List<ColumnFamilyDefinition> cfDefsFalse = new ArrayList<ColumnFamilyDefinition>();
      cfDefsFalse.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitPagma1", ComparatorType.UTF8TYPE));
      EasyMock.expect(
            saeDao.getColumnFamilyDefintion()).andReturn(cfDefsFalse).times(3);
      // enregistrement du scénario
      EasyMock.replay(saeDao);

      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

      // DroitContratService
      cfDefs.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitContratService", ComparatorType.UTF8TYPE));

      // DroitPagm
      cfDefs.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitPagm", ComparatorType.UTF8TYPE));

      // DroitPagma
      cfDefs.add(HFactory.createColumnFamilyDefinition("LotInstallMaj",
            "DroitPagma", ComparatorType.UTF8TYPE));


      service.createColumnFamilyFromList(cfDefs, true);
      // vérification du résultat
      EasyMock.verify(saeDao);
   }
}
