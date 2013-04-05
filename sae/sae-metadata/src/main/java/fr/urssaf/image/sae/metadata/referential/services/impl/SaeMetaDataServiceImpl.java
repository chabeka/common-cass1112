package fr.urssaf.image.sae.metadata.referential.services.impl;

import net.docubase.toolkit.service.ServiceProvider;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;

/**
 * Classe d'implémentation du service SaeMetadataService. Cette classe est un
 * singleton et peut être accessible via le mécanisme d'ijection IOC avec
 * l'annotation @Autowired
 * 
 * 
 */
public class SaeMetaDataServiceImpl implements SaeMetaDataService {

   private SaeMetadataSupport saeMetadatasupport;
   private JobClockSupport clockSupport;
   private CuratorFramework curator;
   private ServiceProvider serviceProvider;
   
   @Autowired
   public SaeMetaDataServiceImpl(SaeMetadataSupport saeMetadataSupport, JobClockSupport clockSupport, CuratorFramework curator, ServiceProvider serviceProvider){
      
   }
   
   
   
   @Override
   public void create(MetadataReference value) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void moify(MetadataReference value) {
      // TODO Auto-generated method stub
      
   }

}
