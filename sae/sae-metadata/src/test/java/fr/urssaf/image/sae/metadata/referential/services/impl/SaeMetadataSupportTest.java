package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.metadata.dfce.ServiceProviderSupportMetadata;
import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;

/**
 * classe de test du service {@link DictionaryServiceImpl}
 * 
 */
//FIXME : réactiver les testes après la mise en place des fichiers application contexte
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-metadata-test.xml" })
public class SaeMetadataSupportTest {
   
   @Autowired
   SaeMetadataSupport metaSupport;
   
   @Autowired
   SaeMetaDataService service;
   
   @Autowired
   private CassandraServerBean server;
   @Autowired
   private JobClockSupport clock;
   
   @Autowired
   private ServiceProviderSupportMetadata provider;


   @Before
   public void before() {
      provider.connect();
   }
   
   @After
   public void after() throws Exception {
      provider.disconnect();
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
      metaDest.setDefaultConsultable(Boolean.FALSE);
      metaDest.setDescription("meta test");
      metaDest.setInternal(Boolean.TRUE);
      metaDest.setLabel("creation Meta");
      metaDest.setLength(10);
      metaDest.setLongCode("metadonne2");
      metaDest.setPattern(StringUtils.EMPTY);
      metaDest.setRequiredForArchival(Boolean.FALSE);
      metaDest.setRequiredForStorage(Boolean.FALSE);
      metaDest.setSearchable(Boolean.TRUE);
      metaDest.setShortCode("meta2");
      metaDest.setType("Srting");
      metaDest.setHasDictionary(Boolean.FALSE);
      metaDest.setDictionaryName(StringUtils.EMPTY);
      metaDest.setIsIndexed(Boolean.TRUE);
      metaDest.setTransferable(Boolean.TRUE);
      
      
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
      Assert.assertEquals("transferable faux",metaDest.getTransferable(), metafind.getTransferable());
   }
   
   /**
    * Teste permettant de vérifier que tous les éléments de la CF sont renvoyés
    */
   
   @Test
   public void findAllMetaTest(){
      List<MetadataReference> listeMeta = metaSupport.findAll();
      Assert.assertEquals(63,listeMeta.size());
   }
   
   @Test
   public void modifyMetaTest(){
      
   }
   
   @Test(expected=MetadataRuntimeException.class)
   public void createSameShortCode() {
      
      MetadataReference metaDest = new MetadataReference();

      metaDest.setArchivable(Boolean.TRUE);
      metaDest.setConsultable(Boolean.TRUE);
      metaDest.setDefaultConsultable(Boolean.FALSE);
      metaDest.setDescription("meta test");
      metaDest.setInternal(Boolean.TRUE);
      metaDest.setLabel("creation Meta");
      metaDest.setLength(10);
      metaDest.setLongCode("metadonne4");
      metaDest.setPattern(StringUtils.EMPTY);
      metaDest.setRequiredForArchival(Boolean.FALSE);
      metaDest.setRequiredForStorage(Boolean.FALSE);
      metaDest.setSearchable(Boolean.TRUE);
      metaDest.setShortCode("meta2");
      metaDest.setType("Srting");
      metaDest.setHasDictionary(Boolean.FALSE);
      metaDest.setDictionaryName(StringUtils.EMPTY);
      metaDest.setIsIndexed(Boolean.TRUE);
      
      
      try {
         metaSupport.create(metaDest, clock.currentCLock());
      } catch (Exception e) {
         Assert.fail("erreur non attendue");
      }
      
      metaDest = new MetadataReference();

      metaDest.setArchivable(Boolean.TRUE);
      metaDest.setConsultable(Boolean.TRUE);
      metaDest.setDefaultConsultable(Boolean.FALSE);
      metaDest.setDescription("meta test");
      metaDest.setInternal(Boolean.TRUE);
      metaDest.setLabel("creation Meta");
      metaDest.setLength(10);
      metaDest.setLongCode("metadonne3");
      metaDest.setPattern(StringUtils.EMPTY);
      metaDest.setRequiredForArchival(Boolean.FALSE);
      metaDest.setRequiredForStorage(Boolean.FALSE);
      metaDest.setSearchable(Boolean.TRUE);
      metaDest.setShortCode("meta2");
      metaDest.setType("Srting");
      metaDest.setHasDictionary(Boolean.FALSE);
      metaDest.setDictionaryName(StringUtils.EMPTY);
      metaDest.setIsIndexed(Boolean.TRUE);
      
      
      metaSupport.create(metaDest, clock.currentCLock());
   }
   
}
