package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.messages.MetadataMessageHandler;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.referential.services.XmlDataService;
import fr.urssaf.image.sae.metadata.utils.Utils;

/**
 * Classe qui implémente l'interface
 * {@link fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO
 * MetadataReferenceService}
 * 
 * @author akenore
 * 
 */
@Service
@Qualifier("metadataReferenceDAO")
public class MetadataReferenceDAOImpl implements MetadataReferenceDAO {
   @Autowired
   @Qualifier("xmlDataService")
   private XmlDataService xmlDataService;

   @Autowired
   private ApplicationContext context;

   private static Map<String, MetadataReference> ALL_METADATA_REFERENCES;

   /**
    * @return Le context.
    */
   public final ApplicationContext getContext() {
      return context;
   }

   /**
    * @param context
    *           : le context
    */
   public final void setContext(final ApplicationContext context) {
      this.context = context;
   }

   /**
    * @return Le service Xml
    */
   public final XmlDataService getXmlDataService() {
      return xmlDataService;
   }

   /**
    * @param xmlDataService
    *           : Le service Xml
    */
   public final void setXmlDataService(final XmlDataService xmlDataService) {
      this.xmlDataService = xmlDataService;
   }

   /**
    * {@inheritDoc}
    * 
    * @throws ReferentialException
    *            Exception lever lorsque la récupération des métadonnées ne sont
    *            pas disponibles.
    */
   public final Map<String, MetadataReference> getAllMetadataReferences()
         throws ReferentialException {

      final Resource referentiel = context
            .getResource("classpath:MetadataReferential.xml");

      try {
         synchronized (this) {
            if (ALL_METADATA_REFERENCES == null) {
               ALL_METADATA_REFERENCES = xmlDataService
                     .referentialReader(referentiel.getInputStream());
            }
            return ALL_METADATA_REFERENCES;
         }

      } catch (IOException e) {
         throw new ReferentialException(MetadataMessageHandler.getMessage(
               "referential.file.notfound", referentiel.getFilename()), e);
      }

   }

   /**
    * {@inheritDoc}
    * 
    */
   public final Map<String, MetadataReference> getConsultableMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> csltMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();

      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
            referentiel).entrySet()) {
         if (metaData.getValue().isConsultable()) {
            csltMetaDatas.put(metaData.getKey(), metaData.getValue());
         }
      }
      return csltMetaDatas;
   }

   /**
    * {@inheritDoc}
    * 
    */
   public final Map<String, MetadataReference> getSearchableMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> srchMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
            referentiel).entrySet()) {
         if (metaData.getValue().isSearchable()) {
            srchMetaDatas.put(metaData.getKey(), metaData.getValue());
         }
      }
      return srchMetaDatas;
   }

   /**
    * {@inheritDoc}
    * 
    */
   public final Map<String, MetadataReference> getArchivableMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> archMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
            referentiel).entrySet()) {
         if (metaData.getValue().isArchivable()) {
            archMetaDatas.put(metaData.getKey(), metaData.getValue());
         }
      }
      return archMetaDatas;
   }

   /**
    * {@inheritDoc}
    * 
    */
   public final MetadataReference getByLongCode(final String longCode)
         throws ReferentialException {

      return getAllMetadataReferences().get(longCode);
   }

   /**
    * {@inheritDoc}
    * 
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public final MetadataReference getByShortCode(final String shortCode)
         throws ReferentialException {
      MetadataReference metadatafound = null;
      for (Entry<String, MetadataReference> reference : Utils.nullSafeMap(
            getAllMetadataReferences()).entrySet()) {
         if (reference.getValue().getShortCode().equals(shortCode)) {
            metadatafound = reference.getValue();
         }
      }
      return metadatafound;
   }

   /**
    * {@inheritDoc}
    */
   public final Map<String, MetadataReference> getRequiredForStorageMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> reqMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
            referentiel).entrySet()) {
         if (metaData.getValue().isRequiredForStorage()) {
            reqMetaDatas.put(metaData.getKey(), metaData.getValue());
         }
      }
      return reqMetaDatas;
   }

   /**
    * {@inheritDoc}
    */
   public final Map<String, MetadataReference> getDefaultConsultableMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> reqMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
            referentiel).entrySet()) {
         if (metaData.getValue().isDefaultConsultable()) {
            reqMetaDatas.put(metaData.getKey(), metaData.getValue());
         }
      }
      return reqMetaDatas;
   }

   /**
    * {@inheritDoc}
    */
   public final Map<String, MetadataReference> getRequiredForArchivalMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> reqMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
            referentiel).entrySet()) {
         if (metaData.getValue().isRequiredForArchival()) {
            reqMetaDatas.put(metaData.getKey(), metaData.getValue());
         }
      }
      return reqMetaDatas;
   }

   /**
    * Construit un objet de type {@link MetadataReferenceDAOImpl}
    * 
    * @param xmlDataService
    *           : Le service de serialization des fichiers xml.
    * @param context
    *           : Le context applicatif.
    */
   public MetadataReferenceDAOImpl(final XmlDataService xmlDataService,
         final ApplicationContext context) {
      this.xmlDataService = xmlDataService;
      this.context = context;
   }

   /**
    * Construit un objet de type {@link MetadataReferenceDAOImpl}
    */
   public MetadataReferenceDAOImpl() {
      // ici on ne fait rien
   }

}
