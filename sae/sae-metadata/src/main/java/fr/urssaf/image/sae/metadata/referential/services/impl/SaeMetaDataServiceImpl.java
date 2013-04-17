package fr.urssaf.image.sae.metadata.referential.services.impl;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.docubase.dfce.exception.ObjectAlreadyExistsException;
import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.utils.ZookeeperUtils;
import fr.urssaf.image.sae.metadata.exceptions.MetadataReferenceException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
import fr.urssaf.image.sae.storage.model.connection.StorageConnectionParameter;

/**
 * Classe d'implémentation du service SaeMetadataService. Cette classe est un
 * singleton et peut être accessible via le mécanisme d'injection IOC avec
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
   private StorageConnectionParameter storageParam;
   
   private static final Logger LOGGER = LoggerFactory
   .getLogger(SaeMetaDataServiceImpl.class);
   
   private int MAX_VALUES=1;
   private String PREFIXE_META ="/DroitMeta/";
   
   private static final String TRC_CREATE = "create()";
   
   @Autowired
   public SaeMetaDataServiceImpl(SaeMetadataSupport saeMetadataSupport, JobClockSupport clockSupport, CuratorFramework curator, ServiceProvider serviceProvider){
      this.saeMetadatasupport = saeMetadataSupport;
      this.clockSupport = clockSupport;
      this.curator = curator;
      this.serviceProvider = serviceProvider;
   }
   
   
   
   @Override
   public void create(MetadataReference metadata) {
      
      String resourceName = PREFIXE_META + metadata.getShortCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curator,
            resourceName);

      // création de la métadonnée dans DFCE
      try {
         serviceProvider.getBaseAdministrationService().createBase(createBase(metadata));
      } catch (ObjectAlreadyExistsException e) {
         throw  new MetadataRuntimeException(e.getMessage());
      }

      // si l'insertion dans DFCE s'est bien passé on créé la métadonné dans le SAE
      try {
         
         LOGGER.debug("{} - Lock Zookeeper", TRC_CREATE);
         ZookeeperUtils.acquire(mutex, resourceName);
         LOGGER.debug("{} - Création de la Métadonnée", TRC_CREATE);
         saeMetadatasupport.create(metadata, clockSupport.currentCLock());
         
         checkLock(mutex, metadata);

      } finally {
         mutex.release();
      }
   }

   @Override
   public void moify(MetadataReference metadata) {
      
      serviceProvider.getBaseAdministrationService().updateBase(createBase(metadata));
      saeMetadatasupport.create(metadata, clockSupport.currentCLock());      
   }
   
   private Base createBase(MetadataReference metadata){
      Base base = serviceProvider.getBaseAdministrationService().getBase(
            storageParam.getStorageBase().getBaseName());

      final ToolkitFactory toolkit = ToolkitFactory.getInstance();
         final Category categoryDfce = serviceProvider
               .getStorageAdministrationService().findOrCreateCategory(
                     metadata.getShortCode(), CategoryDataType.valueOf(StringUtils.upperCase(metadata.getType())));
         final BaseCategory baseCategory = toolkit.createBaseCategory(
               categoryDfce, metadata.getIsIndexed());
         baseCategory.setEnableDictionary(metadata.getHasDictionary());
         baseCategory.setMaximumValues(MAX_VALUES);
         if(metadata.isRequiredForStorage()){
            baseCategory.setMinimumValues(1);
         }else{
            baseCategory.setMinimumValues(0);
         }
         baseCategory.setSingle(metadata.isRequiredForStorage());
         
         return base;
   }
   
   
   private void checkLock(ZookeeperMutex mutex, MetadataReference metadata) {

      if (!ZookeeperUtils.isLock(mutex)) {

         MetadataReference storedMeta = saeMetadatasupport.find(metadata.getShortCode());

         if (storedMeta == null) {
            throw new MetadataReferenceException("La métadonnée "+ metadata.getShortCode()
                  + " n'a pas été créé");
         }

         if (!storedMeta.equals(metadata)) {
            throw new MetadataRuntimeException("La métadonnée "+ metadata.getShortCode()+" a déjà été créé");
         }

      }

   }

}
