package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.metadata.exceptions.MetadataReferenceNotFoundException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.SaeMetaDataService;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.base.CategoryDataType;
import net.docubase.toolkit.model.reference.Category;

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
   private final DFCEServices dfceServices;

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
   public SaeMetaDataServiceImpl(final SaeMetadataSupport saeMetadataSupport,
                                 final JobClockSupport clockSupport,
                                 final DFCEServices dfceServices) {
      this.saeMetadatasupport = saeMetadataSupport;
      this.clockSupport = clockSupport;
      this.dfceServices = dfceServices;
   }

   @Override
   public final void create(final MetadataReference metadata) {

      LOGGER.debug("{} - Création de la métadonnée dans DFCE", metadata
                   .getLongCode());
      // création de la métadonnée dans DFCE
      final Base base = createBase(metadata);
      dfceServices.updateBase(base);

      // si l'insertion dans DFCE s'est bien passé on créé la métadonné dans le
      // SAE
      LOGGER.debug("{} - Création de la Métadonnée", TRC_CREATE);
      saeMetadatasupport.create(metadata, clockSupport.currentCLock());

   }

   @Override
   public final void modify(final MetadataReference metadata)
         throws MetadataReferenceNotFoundException {

      LOGGER
      .debug("{} - Modification de la métadonnée", metadata.getLongCode());

      // On vérifie que la métadonné existe si ce n'est pas le
      // cas on sort en exception
      final MetadataReference meta = saeMetadatasupport.find(metadata.getLongCode());
      if (meta == null) {
         throw new MetadataReferenceNotFoundException(metadata.getLongCode());
      } else {

         // Si la métadonnée est une méta système, on n'appelle pas la
         // modification
         // dans DFCE
         if (!metadata.isInternal()) {
            dfceServices.updateBase(createBase(metadata));
         }

         saeMetadatasupport.modify(metadata, clockSupport.currentCLock());
      }
   }

   private Base createBase(final MetadataReference metadata) {
      final ToolkitFactory toolkit = ToolkitFactory.getInstance();

      final Category categoryDfce = dfceServices.findOrCreateCategory(
                                                                      metadata.getShortCode(),
                                                                      CategoryDataType.valueOf(StringUtils.upperCase(metadata
                                                                                                                     .getType())));

      final Base base = dfceServices.getBase();
      BaseCategory baseCategory;
      boolean ajout;
      if (base.getBaseCategory(categoryDfce.getName()) == null) {
         // ajout de la category a la base
         baseCategory = toolkit.createBaseCategory(
                                                   categoryDfce, metadata.getIsIndexed());
         ajout = true;
      } else {
         // modif de la category a la base
         baseCategory = base.getBaseCategory(categoryDfce.getName());
         baseCategory.setIndexed(metadata.getIsIndexed());
         ajout = false;
      }

      baseCategory.setEnableDictionary(Boolean.FALSE);
      baseCategory.setMaximumValues(MAX_VALUES);
      // On met toujours la valeur min à 0 (même si la méta à ajouter est
      // requise au stockage) car on considère qu'on est sur une
      // mise à jour de base DFCE et dans ce cas, on ne peut pas ajouter de
      // métadonnée obligatoire (cf doc toolkit). La partie métadonnée
      // obligatoire est donc gérée uniquement par la surcouche SAE.
      baseCategory.setMinimumValues(0);
      baseCategory.setSingle(Boolean.FALSE);
      if (ajout) {
         base.addBaseCategory(baseCategory);
         LOGGER.debug("Métadonnée créée dans DFCE");
      } else {
         LOGGER.debug("Métadonnée modifiée dans DFCE");
      }

      return base;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final MetadataReference find(final String codeLong) {
      return saeMetadatasupport.find(codeLong);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataReference> findAll() {
      return saeMetadatasupport.findAll();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataReference> findAllMetadatasConsultables() {
      return saeMetadatasupport.findMetadatasConsultables();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataReference> findAllMetadatasRecherchables() {
      return saeMetadatasupport.findMetadatasRecherchables();
   }

}
