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

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.metadata.dfce.ServiceProviderSupportMetadata;
import fr.urssaf.image.sae.metadata.exceptions.MetadataReferenceNotFoundException;
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
   private final ServiceProviderSupportMetadata serviceProviderSupport;
   private DfceConfig dfceConfig;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SaeMetaDataServiceImpl.class);

   private static final int MAX_VALUES = 1;

   private static final String TRC_CREATE = "create()";

   /**
    * Constructeur du service
    * 
    * @param saeMetadataSupport
    *           la classe support
    * @param clockSupport
    *           {@link JobClockSupport}
    * @param serviceProviderSupport
    *           {@link ServiceProviderSupportMetadata}
    * @param dfceConfig
    *           {@link DfceConfig}
    */
   @Autowired
   public SaeMetaDataServiceImpl(SaeMetadataSupport saeMetadataSupport,
         JobClockSupport clockSupport,
         ServiceProviderSupportMetadata serviceProviderSupport,
         DfceConfig dfceConfig) {
      this.saeMetadatasupport = saeMetadataSupport;
      this.clockSupport = clockSupport;
      this.serviceProviderSupport = serviceProviderSupport;
      this.dfceConfig = dfceConfig;
   }

   @Override
   public final void create(MetadataReference metadata) {

      LOGGER.debug("{} - Création de la métadonnée dans DFCE", metadata
            .getLongCode());
      serviceProviderSupport.connect();
      // création de la métadonnée dans DFCE
      serviceProviderSupport.getBaseAdministrationService().updateBase(
            createBase(metadata));

      // si l'insertion dans DFCE s'est bien passé on créé la métadonné dans le
      // SAE
      LOGGER.debug("{} - Création de la Métadonnée", TRC_CREATE);
      saeMetadatasupport.create(metadata, clockSupport.currentCLock());

   }

   @Override
   public final void modify(MetadataReference metadata)
         throws MetadataReferenceNotFoundException {

      LOGGER
            .debug("{} - Modification de la métadonnée", metadata.getLongCode());
      serviceProviderSupport.connect();

      // On vérifie que la métadonné existe si ce n'est pas le
      // cas on sort en exception
      MetadataReference meta = saeMetadatasupport.find(metadata.getLongCode());
      if (meta == null) {
         throw new MetadataReferenceNotFoundException(metadata.getLongCode());
      } else {

         // Si la métadonnée est une méta système, on n'appelle pas la
         // modification
         // dans DFCE
         if (!metadata.isInternal()) {
            serviceProviderSupport.getBaseAdministrationService().updateBase(
                  createBase(metadata));
         }

         saeMetadatasupport.create(metadata, clockSupport.currentCLock());
      }
   }

   private Base createBase(MetadataReference metadata) {
      Base base = serviceProviderSupport.getBaseAdministrationService()
            .getBase(dfceConfig.getBasename());
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
      // On met toujours la valeur min à 0 (même si la méta à ajouter est
      // requise au stockage) car on considère qu'on est sur une
      // mise à jour de base DFCE et dans ce cas, on ne peut pas ajouter de
      // métadonnée obligatoire (cf doc toolkit). La partie métadonnée
      // obligatoire est donc gérée uniquement par la surcouche SAE.
      baseCategory.setMinimumValues(0);
      baseCategory.setSingle(Boolean.FALSE);
      base.addBaseCategory(baseCategory);
      LOGGER.debug("Métadonné crééé dans DFCE");

      return base;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final MetadataReference find(String codeLong) {
      return saeMetadatasupport.find(codeLong);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataReference> findAll() {
      return saeMetadatasupport.findAll();
   }

}
