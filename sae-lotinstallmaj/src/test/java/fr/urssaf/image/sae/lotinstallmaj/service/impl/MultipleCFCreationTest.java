package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;





@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-sae-lotinstallmaj.xml"})
@ContextConfiguration(locations = { "/applicationContext-sae-lotinstallmaj-multiple-cf-test.xml"})

public class MultipleCFCreationTest {
// LOGGER
private static final Logger LOG = LoggerFactory
      .getLogger(MultipleCFCreationTest.class);

   
   @Autowired
   private SAECassandraService service;
   /**
    * Test de creation de CF multiple pour reproduire l'echec de déploiement en prod. 
    */
   @Test
   public void createMultipleCF(){
      
      
      // liste des CF existants
      List<ColumnFamilyDefinition> cfDefsTrue = new ArrayList<ColumnFamilyDefinition>();
      LOG.debug("Insertion dans le KeySpace {} du cluster {}",  service.getKeySpaceName(), service.getCluster().getName());
      for(int i=550; i<600;i++){
      ColumnFamilyDefinition c = HFactory.createColumnFamilyDefinition(service.getKeySpaceName(),
            "CF_"+i, ComparatorType.UTF8TYPE);
      // DroitContratService
      cfDefsTrue.add(c);  
      }
      service.createColumnFamilyFromList(cfDefsTrue, true);
   }
   
}
