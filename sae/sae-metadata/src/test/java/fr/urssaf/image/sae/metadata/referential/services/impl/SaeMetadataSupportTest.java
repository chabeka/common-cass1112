package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;

/**
 * classe de test du service {@link DictionaryServiceImpl}
 * 
 */
//FIXME : réactiver les testes après la mise en place des fichiers application contexte
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class SaeMetadataSupportTest {
   
   @Autowired
   SaeMetadataSupport metaSupport;
   @Autowired
   private CassandraServerBean server;
   @Autowired
   private JobClockSupport clock;
   
   @After
   public void after() throws Exception {
      server.resetData();
   }
   
   /**
    * test permettant de verifier qu'une métadonnée est bien enregistré en base et qu'il est possible de la récupérer
    * @throws DictionaryNotFoundException
    */
   @Test
   public void createMetaTest() throws DictionaryNotFoundException{

      MetadataReference metaDest = new MetadataReference();

      metaDest.setArchivable(Boolean.TRUE);
      metaDest.setConsultable(Boolean.TRUE);
      metaDest.setDefaultConsultable(Boolean.TRUE);
      metaDest.setDescription("meta test");
      metaDest.setInternal(Boolean.TRUE);
      metaDest.setLabel("creation Meta");
      metaDest.setLength(10);
      metaDest.setLongCode("metadonne");
      metaDest.setPattern(StringUtils.EMPTY);
      metaDest.setRequiredForArchival(Boolean.TRUE);
      metaDest.setRequiredForStorage(Boolean.TRUE);
      metaDest.setSearchable(Boolean.TRUE);
      metaDest.setShortCode("meta");
      metaDest.setType("Srting");
      metaDest.setHasDictionary(Boolean.FALSE);
      metaDest.setDictionaryName(StringUtils.EMPTY);
      metaDest.setIsIndexed(Boolean.TRUE);
      
      
      metaSupport.create(metaDest, clock.currentCLock());
      MetadataReference metafind = metaSupport.find(metaDest.getLongCode());
      Assert.assertNotNull(metafind);
      Assert.assertEquals("description fausse",metaDest.getDescription(), metafind.getDescription());
      Assert.assertEquals("code court faux",metaDest.getShortCode(), metafind.getShortCode());
      Assert.assertEquals("code long faux",metaDest.getLongCode(), metafind.getLongCode());
      Assert.assertEquals("nom du dictionnaire faux", metaDest.getDictionaryName(), metafind.getDictionaryName());
      Assert.assertEquals("consultable faux",metaDest.isConsultable(), metafind.isConsultable());
      Assert.assertEquals("default consultable faux",metaDest.isDefaultConsultable(), metafind.isDefaultConsultable());
      Assert.assertEquals("recherchable faux",metaDest.isSearchable(), metafind.isSearchable());
      Assert.assertEquals("obligatoire à l'archivage faux",metaDest.isRequiredForArchival(), metafind.isRequiredForArchival());
      Assert.assertEquals("obligatoire au stockage faux",metaDest.isRequiredForStorage(), metafind.isRequiredForStorage());
      Assert.assertEquals("valeur interne faux",metaDest.isInternal(), metafind.isInternal());
      Assert.assertEquals("recherchable faux",metaDest.isSearchable(), metafind.isSearchable());
   }
   
   /**
    * Teste permettant de vérifier que tous les éléments de la CF sont renvoyés
    */
   
   @Test
   public void findAllMetaTest(){
      List<MetadataReference> listeMeta = metaSupport.findAll();
      Assert.assertEquals(55,listeMeta.size());
   }
   
   @Test
   public void modifyMetaTest(){
      
   }
}
