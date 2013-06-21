package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.List;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.utils.ZookeeperUtils;
import fr.urssaf.image.sae.metadata.dfce.ServiceProviderSupportMetadata;
import fr.urssaf.image.sae.metadata.exceptions.MetadataReferenceException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataReferenceNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.referential.model.DfceConfig;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;

/**
 * Classe d'implémentation du service SaeMetadataService. Cette classe est un
 * singleton et peut être accessible via le mécanisme d'injection IOC avec
 * l'annotation @Autowired
 * 
 */
@Service
@SuppressWarnings("PMD.PreserveStackTrace")
public class SaeMetaDataServiceImpl implements SaeMetaDataService {

   private final SaeMetadataSupport saeMetadatasupport;
   private final JobClockSupport clockSupport;
   private final CuratorFramework curator;
   private final ServiceProviderSupportMetadata serviceProviderSupport;

   @Autowired
   private DfceConfig dfceConfig;


   private static final Logger LOGGER = LoggerFactory
         .getLogger(SaeMetaDataServiceImpl.class);

   private static final int MAX_VALUES = 1;
   private static final String PREFIXE_META = "/DroitMeta/";

   private static final String TRC_CREATE = "create()";

   /**
    * Constructeur du service
    * 
    * @param saeMetadataSupport
    *           la classe support
    * @param clockSupport
    *           {@link JobClockSupport}
    * @param curator
    *           {@link CuratorFramework}
    * @param serviceProviderSupport
    *           {@link ServiceProviderSupport}
    */
   @Autowired
   public SaeMetaDataServiceImpl(SaeMetadataSupport saeMetadataSupport,
         JobClockSupport clockSupport, CuratorFramework curator,
         ServiceProviderSupportMetadata serviceProviderSupport) {
      this.saeMetadatasupport = saeMetadataSupport;
      this.clockSupport = clockSupport;
      this.curator = curator;
      this.serviceProviderSupport = serviceProviderSupport;
   }

   @Override
   public final void create(MetadataReference metadata) {

      String resourceName = PREFIXE_META + metadata.getShortCode();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curator, resourceName);
      LOGGER.debug("{} - Création de la métadonnée dans DFCE", metadata
            .getLongCode());
      serviceProviderSupport.connect();
      // création de la métadonnée dans DFCE
      serviceProviderSupport.getBaseAdministrationService().updateBase(
            createBase(metadata));

      // si l'insertion dans DFCE s'est bien passé on créé la métadonné dans le
      // SAE
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
   public final void modify(MetadataReference metadata) throws MetadataReferenceNotFoundException {
      serviceProviderSupport.connect();
      LOGGER
            .debug("{} - Modification de la métadonnée", metadata.getLongCode());
      serviceProviderSupport.getBaseAdministrationService().updateBase(
            createBase(metadata));
      // On vérifie si la métadonnée que la métadonné existe si ce n'est pas le
      // cas on sort en exception
      MetadataReference meta = saeMetadatasupport.find(metadata.getLongCode());
      if (meta == null) {
         throw new MetadataReferenceNotFoundException(metadata.getLongCode());
      } else {
         saeMetadatasupport.create(metadata, clockSupport.currentCLock());
      }
   }

   private Base createBase(MetadataReference metadata) {
      Base base = serviceProviderSupport.getBaseAdministrationService()
      .getBase( dfceConfig.getBasename());
      serviceProviderSupport.connect();
      final ToolkitFactory toolkit = ToolkitFactory.getInstance();
      
      final Category categoryDfce = serviceProviderSupport
            .getStorageAdministrationService().findOrCreateCategory(
                  metadata.getShortCode(),
                  CategoryDataType.valueOf(StringUtils.upperCase(metadata
                        .getType())));
      
      final BaseCategory baseCategory = toolkit.createBaseCategory(
            categoryDfce, metadata.getIsIndexed());
      baseCategory.setEnableDictionary(Boolean.FALSE);
      baseCategory.setMaximumValues(MAX_VALUES);
      if (metadata.isRequiredForStorage()) {
         baseCategory.setMinimumValues(1);
      } else {
         baseCategory.setMinimumValues(0);
      }
      baseCategory.setSingle(Boolean.FALSE);
      base.addBaseCategory(baseCategory);
      LOGGER.debug("Métadonné crééé dans DFCE");
      
      return base;
   }

   private void checkLock(ZookeeperMutex mutex, MetadataReference metadata) {

      if (!ZookeeperUtils.isLock(mutex)) {

         MetadataReference storedMeta = saeMetadatasupport.find(metadata
               .getLongCode());

         if (storedMeta == null) {
            throw new MetadataReferenceException("La métadonnée "
                  + metadata.getLongCode() + " n'a pas été créé");
         }

         if (!storedMeta.equals(metadata)) {
            throw new MetadataRuntimeException("La métadonnée "
                  + metadata.getLongCode() + " a déjà été créé");
         }

      }

   }

   @Override
   public MetadataReference find(String codeLong) {
      return saeMetadatasupport.find(codeLong);
   }

   @Override
   public List<MetadataReference> findAll() {
      return saeMetadatasupport.findAll();
   }
   
   

}
