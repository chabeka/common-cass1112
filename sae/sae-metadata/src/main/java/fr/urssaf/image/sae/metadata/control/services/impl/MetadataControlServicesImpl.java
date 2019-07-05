package fr.urssaf.image.sae.metadata.control.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.UncheckedExecutionException;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.messages.MetadataMessageHandler;
import fr.urssaf.image.sae.metadata.referential.model.Dictionary;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.DictionaryService;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.utils.Utils;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;
import fr.urssaf.image.sae.trace.model.TraceToCreate;
import fr.urssaf.image.sae.trace.service.DispatcheurService;
import fr.urssaf.image.sae.trace.utils.HostnameUtil;

/**
 * Classe qui implémente les sevices de l'interface
 * {@link MetadataControlServices}
 */
@Service
@Qualifier("metadataControlServices")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals", "PMD.TooManyMethods" })
public class MetadataControlServicesImpl implements MetadataControlServices {
   @Autowired
   @Qualifier("ruleFactory")
   private MetadataRuleFactory ruleFactory;

   @Autowired
   @Qualifier("metadataReferenceDAO")
   private MetadataReferenceDAO referenceDAO;

   @Autowired
   private DictionaryService dictionaryService;

   @Autowired
   private DispatcheurService dispatcheurService;

   /**
    * Traçabilité : le code de l'événement pour le trim de métadonnées
    */
   private static final String TRACE_CODE_EVT_TRIM_METADATA = "META_VAL_ESPACE|INFO";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(MetadataControlServicesImpl.class);

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final List<MetadataError> checkArchivableMetadata(
         final SAEDocument saeDoc) {

      return checkArchivableMetadataList(saeDoc.getMetadatas());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final List<MetadataError> checkExistingMetadata(
         final UntypedDocument untypedDoc) {
      return checkExistingMetadataList(untypedDoc.getUMetadatas());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final List<MetadataError> checkMetadataValueTypeAndFormat(
         final UntypedDocument untypedDoc) {
      return checkMetadataListValueTypeAndFormat(untypedDoc.getUMetadatas());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings( { "PMD.AvoidInstantiatingObjectsInLoops",
   "PMD.DataflowAnomalyAnalysis" })
   public final List<MetadataError> checkRequiredForArchivalMetadata(
         final SAEDocument saeDoc) {
      return checkRequiredForArchivalMetadataList(saeDoc.getMetadatas());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final List<MetadataError> checkConsultableMetadata(
         final List<SAEMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      for (SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            if (!ruleFactory.getConsultableRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.consultable"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.not.consultable", metadata.getLongCode())));
            }
         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), metadata
                  .getLongCode(), MetadataMessageHandler
                  .getMessage("metadata.referentiel.retrieve")));
         }
      }
      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final List<MetadataError> checkSearchableMetadata(
         final List<SAEMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      for (SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            if (!ruleFactory.getSearchableRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.searchable"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.not.searchable", metadata.getLongCode())));
            }

         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), metadata
                  .getLongCode(), MetadataMessageHandler
                  .getMessage("metadata.referentiel.retrieve")));
         }

      }
      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final List<MetadataError> checkDuplicateMetadata(
         final List<UntypedMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      for (UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         if (!Utils.hasDuplicate(metadata, metadatas)) {
            final MetadataError error = new MetadataError(
                  MetadataMessageHandler
                  .getMessage("metadata.control.duplicate"), metadata
                  .getLongCode(), MetadataMessageHandler.getMessage(
                        "metadata.duplicated", metadata.getLongCode()));
            if (!Utils.exist(error, errors)) {
               errors.add(error);
            }
         }
      }
      return errors;
   }

   /**
    * 
    * @param factory
    *           : La fabrique des régles.
    */
   public final void setRuleFactory(final MetadataRuleFactory factory) {
      this.ruleFactory = factory;
   }

   /**
    * 
    * @return La fabrique des régles.
    */
   public final MetadataRuleFactory getRuleFactory() {
      return ruleFactory;
   }

   /**
    * 
    * @param referenceDAO
    *           : Les services de manipulation des métadonnées de référentiel
    *           des métadonnées.
    */
   public final void setReferenceDAO(final MetadataReferenceDAO referenceDAO) {
      this.referenceDAO = referenceDAO;
   }

   /**
    * Les services de manipulation des métadonnées de référentiel des
    * métadonnées.
    * 
    * @return Les services de manipulation des métadonnées de référentiel des
    *         métadonnées.
    */
   public final MetadataReferenceDAO getReferenceDAO() {
      return referenceDAO;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings( { "PMD.AvoidInstantiatingObjectsInLoops",
   "PMD.DataflowAnomalyAnalysis" })
   public final List<MetadataError> checkRequiredForStorageMetadata(
         final SAEDocument saeDoc) {

      return checkRequiredForStorageMetadataList(saeDoc.getMetadatas());

   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings( { "PMD.AvoidInstantiatingObjectsInLoops",
   "PMD.DataflowAnomalyAnalysis" })
   public final List<MetadataError> checkExistingQueryTerms(
         final List<String> longCodes) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      for (String codeLong : Utils.nullSafeIterable(longCodes)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(codeLong);
            if (!ruleFactory.getExistingRule().isSatisfiedBy(codeLong,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.existing"), codeLong,
                     MetadataMessageHandler.getMessage("metadata.not.exist",
                           codeLong)));
            }
         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), codeLong,
                  MetadataMessageHandler.getMessage(
                        "metadata.referentiel.retrieve", codeLong)));
         }

      }
      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   public final List<MetadataError> checkMetadataRequiredValue(
         final UntypedDocument untypedDoc) {

      return checkMetadataListRequiredValue(untypedDoc.getUMetadatas());
   }

   /**
    * Construit un objet de type {@link MetadataControlServicesImpl }
    * 
    * @param ruleFactory
    *           : La factory des règles
    * @param referenceDAO
    *           : Le dao du référentiel des métadonnées.
    */
   public MetadataControlServicesImpl(final MetadataRuleFactory ruleFactory,
         final MetadataReferenceDAO referenceDAO) {
      this.ruleFactory = ruleFactory;
      this.referenceDAO = referenceDAO;
   }

   /**
    * Construit un objet de type {@link MetadataControlServicesImpl }
    */
   public MetadataControlServicesImpl() {
      // ici on ne fait rien
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkMetadataValueFromDictionary(
         final UntypedDocument document) {
      return checkMetadataListValueFromDictionary(document.getUMetadatas());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkArchivableMetadataList(
         List<SAEMetadata> metadatas) {

      final List<MetadataError> errors = new ArrayList<MetadataError>();
      for (SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            if (!ruleFactory.getArchivableRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.archivage"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.not.archivable", metadata.getLongCode())));
            }
         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), metadata
                  .getLongCode(), MetadataMessageHandler.getMessage(
                        "metadata.referentiel.retrieve", metadata.getLongCode())));
         }
      }
      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkRequiredForArchivalMetadataList(
         List<SAEMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      try {
         final Map<String, MetadataReference> references = referenceDAO
               .getRequiredForArchivalMetadataReferences();
         checkRequired(metadatas, errors, references);
      } catch (ReferentialException refExcept) {
         errors.add(new MetadataError(MetadataMessageHandler
               .getMessage("metadata.referentiel.error"), null,
               MetadataMessageHandler
               .getMessage("metadata.referentiel.retrieve")));
      }

      return errors;
   }

   /**
    * Permet de controller la presence des métadonnées obligatoires.
    * 
    * @param metadatas
    *           : La liste des métadonnées
    * @param errors
    *           : La liste des erreurs
    * @param references
    *           : Les métadonnées du référentiel
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   private void checkRequired(final List<SAEMetadata> metadatas,
         final List<MetadataError> errors,
         final Map<String, MetadataReference> references) {
      for (Entry<String, MetadataReference> metadata : Utils.nullSafeMap(
            references).entrySet()) {
         if (!Utils.isInRequiredList(metadata.getValue(), metadatas)) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.control.required"), metadata.getValue()
                  .getLongCode(), MetadataMessageHandler.getMessage(
                        "metadata.required", metadata.getValue().getLongCode())));
         }
      }
      for (SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            if (!ruleFactory.getRequiredValueRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.value.required"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.value.required", metadata.getLongCode())));
            }
         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), metadata
                  .getLongCode(), MetadataMessageHandler.getMessage(
                        "metadata.referentiel.retrieve", metadata.getLongCode())));
         }
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkExistingMetadataList(
         List<UntypedMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      for (UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            if (!ruleFactory.getExistingRule().isSatisfiedBy(
                  metadata.getLongCode(), reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.existing"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.not.exist", metadata.getLongCode())));
            }
         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), metadata
                  .getLongCode(), MetadataMessageHandler.getMessage(
                        "metadata.referentiel.retrieve", metadata.getLongCode())));
         }

      }
      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkMetadataListRequiredValue(
         List<UntypedMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      for (UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            // ici on contrôler uniquement les metadonnées renseignées.

            if (StringUtils.isEmpty(metadata.getValue().trim())
                  && reference.isRequiredForStorage()) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.value.required"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.value.required", metadata.getLongCode())));
            }
         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), metadata
                  .getLongCode(), MetadataMessageHandler
                  .getMessage("metadata.referentiel.retrieve")));
         }

      }
      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkMetadataListValueFromDictionary(
         List<UntypedMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();

      try {
         // récupération des métadonnées du document
         List<UntypedMetadata> documentMetaData = metadatas;
         // pour chaque métadonnée du document on va vérifier qu'elle est
         // présente dans la liste de métadonnées définie
         for (UntypedMetadata docMeta : documentMetaData) {
            MetadataReference meta = referenceDAO.getByLongCode(docMeta
                  .getLongCode());
            // on parcours toutes les métadonnées définie pour vérifier la
            // présence de la métadonnée du document
            if (meta.getHasDictionary()) {

               try {
                  Dictionary dict = dictionaryService.find(meta
                        .getDictionaryName());
                  List<String> dictMeta = dict.getEntries();
                  if (!dictMeta.contains(docMeta.getValue())) {
                     errors.add(new MetadataError(MetadataMessageHandler
                           .getMessage("metadata.control.value.not.valid"),
                           docMeta.getLongCode(), MetadataMessageHandler
                           .getMessage(
                                 "metadata.dictionary.value.not.valid",
                                 docMeta.getLongCode())));
                  }

               } catch (UncheckedExecutionException e) {

                  throw new MetadataRuntimeException(MetadataMessageHandler
                        .getMessage("metadata.dictionary.not.valid", meta
                              .getDictionaryName()), e);
               }
            }

         }

      } catch (ReferentialException e) {
         errors.add(new MetadataError(MetadataMessageHandler
               .getMessage("metadata.referentiel.error"), "ALL_METADATAS",
               MetadataMessageHandler
               .getMessage("metadata.referentiel.retrieve")));
      }
      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkMetadataListValueTypeAndFormat(
         List<UntypedMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      for (UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            if (StringUtils.isEmpty(metadata.getValue().trim())) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.value.required"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.value.required", metadata.getLongCode())));
            }
            if (!ruleFactory.getValueTypeRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.type"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.bad.type", metadata.getLongCode())));
            }
            if (!ruleFactory.getValueLengthRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.Length"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.length.not.verified", metadata.getLongCode(),
                           reference.getLength())));
            }
         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), metadata
                  .getLongCode(), MetadataMessageHandler
                  .getMessage("metadata.referentiel.retrieve")));
         }

      }
      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkMetadataListValueTypeAndFormatForTransfertMasse(
         List<UntypedMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      for (UntypedMetadata metadata : Utils.nullSafeIterable(metadatas)) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            if (!ruleFactory.getRequiredValueTypeRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.value.required"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.value.required", metadata.getLongCode())));
            }
            if (!ruleFactory.getValueTypeRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.type"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.bad.type", metadata.getLongCode())));
            }
            if (!ruleFactory.getValueLengthRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.Length"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.length.not.verified", metadata.getLongCode(),
                           reference.getLength())));
            }
            //}
         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), metadata
                  .getLongCode(), MetadataMessageHandler
                  .getMessage("metadata.referentiel.retrieve")));
         }

      }
      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkRequiredForStorageMetadataList(
         List<SAEMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();
      try {
         final Map<String, MetadataReference> references = referenceDAO
               .getRequiredForStorageMetadataReferences();
         checkRequired(metadatas, errors, references);
      } catch (ReferentialException refExcept) {
         errors.add(new MetadataError(MetadataMessageHandler
               .getMessage("metadata.referentiel.error"), null,
               MetadataMessageHandler
               .getMessage("metadata.referentiel.retrieve")));
      }

      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkModifiableMetadataList(
         List<UntypedMetadata> metadatas) {
      final List<MetadataError> errors = new ArrayList<MetadataError>();

      for (UntypedMetadata metadata : metadatas) {
         try {
            final MetadataReference reference = referenceDAO
                  .getByLongCode(metadata.getLongCode());
            if (!ruleFactory.getModifiableRule().isSatisfiedBy(metadata,
                  reference)) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.modifiable"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.not.modifiable", metadata.getLongCode())));
            }
         } catch (ReferentialException refExcept) {
            errors.add(new MetadataError(MetadataMessageHandler
                  .getMessage("metadata.referentiel.error"), metadata
                  .getLongCode(), MetadataMessageHandler.getMessage(
                        "metadata.referentiel.retrieve", metadata.getLongCode())));
         }
      }

      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<MetadataError> checkSupprimableMetadatas(
         List<UntypedMetadata> metadatas) {

      List<MetadataError> errors = new ArrayList<MetadataError>();
      try {
         final Map<String, MetadataReference> references = referenceDAO
               .getRequiredForStorageMetadataReferences();

         for (UntypedMetadata metadata : metadatas) {

            if (Utils.isRequired(references, metadata.getLongCode())) {
               errors.add(new MetadataError(MetadataMessageHandler
                     .getMessage("metadata.control.required"), metadata
                     .getLongCode(), MetadataMessageHandler.getMessage(
                           "metadata.required", metadata.getLongCode())));
            }
         }

      } catch (ReferentialException exception) {
         errors.add(new MetadataError(MetadataMessageHandler
               .getMessage("metadata.referentiel.error"), null,
               MetadataMessageHandler
               .getMessage("metadata.referentiel.retrieve")));
      }

      return errors;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<SAEMetadata> trimMetadata(List<SAEMetadata> metadatas, String contratService, List<String> listePagms, String login) {
      final Map<String, MetadataReference> referenceLeft = referenceDAO
            .getLeftTrimableMetadataReference();
      final Map<String, MetadataReference> referenceRight = referenceDAO
            .getRightTrimableMetadataReference();

      List<SAEMetadata> metadatasTrim = new ArrayList<SAEMetadata>();

      // Parcourir et trimer

      for (SAEMetadata metadata : metadatas) {
         SAEMetadata trimMeta = new SAEMetadata();
         trimMeta.setShortCode(metadata.getShortCode());
         trimMeta.setLongCode(metadata.getLongCode());
         trimMeta.setValue(metadata.getValue());
         boolean trim = false;
         if (Utils.isRequired(referenceLeft, metadata.getLongCode())) {
            trimMeta.setValue(trimMeta.getValue().toString().replaceAll("^\\s+", ""));
            trimMeta.setValue(trimMeta.getValue().toString().replaceAll("^[\r\n]+", ""));
            trim = true;
         }
         if (Utils.isRequired(referenceRight, metadata.getLongCode())) {
            trimMeta.setValue(trimMeta.getValue().toString().replaceAll("\\s+$", ""));
            trimMeta.setValue(trimMeta.getValue().toString().replaceAll("[\r\n]+$", ""));
            trim = true;
         }

         if (trim && !metadata.getValue().equals(trimMeta.getValue())) {
            ecrireTraces(metadata.getLongCode(), metadata.getValue().toString(), trimMeta
                  .getValue().toString(), metadatas, contratService, listePagms, login);
         }

         metadatasTrim.add(trimMeta);
      }
      return metadatasTrim;
   }

 
   private void ecrireTraces(String metaCode, String metaValeur,
         String valeurTrim, List<SAEMetadata> listeMeta, String contratService, List<String> listePagms, String login) {
      
      // Instantiation de l'objet TraceToCreate
      TraceToCreate traceToCreate = new TraceToCreate();

      // Contrat de service
      traceToCreate.setContrat(contratService);
      
      // PAGMs
      traceToCreate.setPagms(listePagms);
      
      // Login
      traceToCreate.setLogin(login);
       
      // Code de l'événement
      traceToCreate.setCodeEvt(TRACE_CODE_EVT_TRIM_METADATA);

      // Contexte
      traceToCreate.setContexte("controleMetadatas");

      // Info supplémentaire : Hostname et IP du serveur sur lequel tourne
      // ce code
      traceToCreate.getInfos().put("saeServeurHostname",
            HostnameUtil.getHostname());
      traceToCreate.getInfos().put("saeServeurIP", HostnameUtil.getIP());

      traceToCreate.getInfos().put("metaCode", metaCode);
      traceToCreate.getInfos().put("metaValeur", "\"" + metaValeur + "\"");
      traceToCreate.getInfos().put("metaValeurTrim", "\"" + valeurTrim + "\"");
      String liste = "[";
      for (SAEMetadata meta : listeMeta) {
         liste = liste.concat("(" + meta.getLongCode() + ":" + meta.getValue()
               + ");");
      }
      liste = StringUtils.left(liste, liste.length() - 1).concat("]");

      traceToCreate.getInfos().put("metaListeComplete", liste);

      // Appel du dispatcheur
      dispatcheurService.ajouterTrace(traceToCreate);

   }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MetadataError> checkNonArchivableMetadata(final List<SAEMetadata> metadatas) {
    final List<MetadataError> errors = new ArrayList<MetadataError>();
    for (SAEMetadata metadata : Utils.nullSafeIterable(metadatas)) {
       try {
          final MetadataReference reference = referenceDAO
                .getByLongCode(metadata.getLongCode());
          if (ruleFactory.getArchivableRule().isSatisfiedBy(metadata,
                reference)) {
            errors.add(new MetadataError(MetadataMessageHandler
                   .getMessage("metadata.control.archivage"), metadata
                   .getLongCode(), MetadataMessageHandler.getMessage(
                         "metadata.archivable", metadata.getLongCode())));
          }
       } catch (ReferentialException refExcept) {
         errors.add(new MetadataError(MetadataMessageHandler
                .getMessage("metadata.referentiel.error"), metadata
                .getLongCode(), MetadataMessageHandler.getMessage(
                      "metadata.referentiel.retrieve", metadata.getLongCode())));
       }
       }
    
    return errors;
 
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MetadataError> checkExistingStorageMetadataList(List<StorageMetadata> metadatas) {
    final List<MetadataError> errors = new ArrayList<MetadataError>();
    for (StorageMetadata metadata : Utils.nullSafeIterable(metadatas)) {
       try {
          final MetadataReference reference = referenceDAO
                .getByShortCode(metadata.getShortCode());
          if (!ruleFactory.getExistingRule().isSatisfiedBy(
                metadata.getShortCode(), reference)) {
             errors.add(new MetadataError(MetadataMessageHandler
                   .getMessage("metadata.control.existing"), metadata
                   .getShortCode(), MetadataMessageHandler.getMessage(
                         "metadata.not.exist", metadata.getShortCode())));
          }
       } catch (ReferentialException refExcept) {
          errors.add(new MetadataError(MetadataMessageHandler
                .getMessage("metadata.referentiel.error"), metadata
                .getShortCode(), MetadataMessageHandler.getMessage(
                      "metadata.referentiel.retrieve", metadata.getShortCode())));
       }
    }
    return errors;
 }

}
