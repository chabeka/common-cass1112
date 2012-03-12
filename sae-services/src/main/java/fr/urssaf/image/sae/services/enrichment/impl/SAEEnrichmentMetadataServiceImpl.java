package fr.urssaf.image.sae.services.enrichment.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.bo.SAEDocument;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.mapping.utils.Utils;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.enrichment.dao.RNDReferenceDAO;
import fr.urssaf.image.sae.services.enrichment.dao.impl.SAEMetatadaFinderUtils;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;

/**
 * Classe concrète pour l’enrichissement des métadonnées.
 * 
 * @author rhofir.
 */
@Service
@Qualifier("saeEnrichmentMetadataService")
@SuppressWarnings( { "PMD.CyclomaticComplexity", "PMD.LongVariable" })
public class SAEEnrichmentMetadataServiceImpl implements
      SAEEnrichmentMetadataService {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAEEnrichmentMetadataServiceImpl.class);
   @Autowired
   @Qualifier("rndReferenceDAO")
   private RNDReferenceDAO rndReferenceDAO;
   @Autowired
   @Qualifier("metadataReferenceDAO")
   private MetadataReferenceDAO metadataReferenceDAO;

   /**
    * @return Le service RND reference.
    */
   public final RNDReferenceDAO getRndReferenceDAO() {
      return rndReferenceDAO;
   }

   /**
    * @param rndReferenceDAO
    *           : Le service RND reference.
    */
   public final void setRndReferenceDAO(RNDReferenceDAO rndReferenceDAO) {
      this.rndReferenceDAO = rndReferenceDAO;
   }

   /**
    * @return Le service metadataReferenceDAO.
    */
   public final MetadataReferenceDAO getMetadataReferenceDAO() {
      return metadataReferenceDAO;
   }

   /**
    * @param metadataReferenceDAO
    *           : Le service metadataReferenceDAO.
    */
   public final void setMetadataReferenceDAO(
         MetadataReferenceDAO metadataReferenceDAO) {
      this.metadataReferenceDAO = metadataReferenceDAO;
   }

   @Override
   public final void enrichmentMetadata(SAEDocument saeDoc)
         throws SAEEnrichmentEx, ReferentialRndException, UnknownCodeRndEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "enrichmentMetadata()";
      LOGGER.debug("{} - Début Enrichissement des métadonnées", prefixeTrc);
      LOGGER.debug("{} - Paramètre saeDocument : \"{}\"", prefixeTrc, saeDoc
            .toString());
      // Fin des traces debug - entrée méthode
      List<SAEMetadata> saeMetadatas = saeDoc.getMetadatas();

      String rndValue = SAEMetatadaFinderUtils.codeMetadataFinder(saeMetadatas,
            SAEArchivalMetadatas.CODE_RND.getLongCode());
      
      String fileName = SAEMetatadaFinderUtils.codeMetadataFinder(saeMetadatas,
            SAEArchivalMetadatas.NOM_FICHIER.getLongCode());
      
      try {
         if (!StringUtils.isEmpty(rndValue)) {
            LOGGER
                  .debug("{} - Début de la vérification : "
                        + "Le type de document est autorisé en archivage",
                        prefixeTrc);
            authorizedCodeRnd(rndValue);
            LOGGER
                  .debug("{} - Fin de la vérification : "
                        + "Le type de document est autorisé en archivage",
                        prefixeTrc);
            LOGGER.debug("{} - Métadonnées avant enrichissement : {}",
                  prefixeTrc, saeDoc.getMetadatas().toString());
            completedMetadatas(saeDoc, rndValue);
            LOGGER.debug("{} - Métadonnées après enrichissement : {}",
                  prefixeTrc, saeDoc.getMetadatas().toString());
         }
         if (!StringUtils.isBlank(fileName)) {
            
            LOGGER.debug("{} - Métadonnées avant enrichissement : {}",
                  prefixeTrc, saeDoc.getMetadatas().toString());
            completedMetadatas(saeDoc, fileName);
            LOGGER.debug("{} - Métadonnées après enrichissement : {}",
                  prefixeTrc, saeDoc.getMetadatas().toString());
         }
      } catch (ReferentialRndException e) {
         throw new ReferentialRndException(e.getMessage(), e);
      } catch (UnknownCodeRndEx e) {
         LOGGER.debug(
               "{} - Le code RND {} ne fait pas partie des codes autorisés",
               prefixeTrc, rndValue);
         throw new UnknownCodeRndEx(e.getMessage(), e);
      } catch (ParseException e) {
         throw new SAEEnrichmentEx(e.getMessage(), e);
      } catch (ReferentialException e) {
         throw new SAEEnrichmentEx(e.getMessage(), e);
      }
      // Traces debug - sortie méthode
      LOGGER.debug("{} - Fin Enrichissement des métadonnées", prefixeTrc);
      LOGGER.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
   }

   /**
    * Vérifier que le CodeRnd exist dans la liste autorisé.
    * 
    * @param rndValue
    *           : codeRnd spécifié par l'application cliente.
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    */
   private void authorizedCodeRnd(String rndValue)
         throws ReferentialRndException, UnknownCodeRndEx {
      rndReferenceDAO.getActivityCodeByRnd(rndValue);
   }

   /**
    * Permet de compléter les métadonnées non specifiable qui sont :
    * <ul>
    * <li>VersionRND</li>
    * <li>CodeFonction</li>
    * <li>CodeActivite</li>
    * <li>DateDebutConservation</li>
    * <li>DateFinConservation</li>
    * <li>NomFichier</li>
    * <li>DocumentVirtuel</li>
    * <li>ContratDeService</li>
    * <li>DateArchivage</li>
    * </ul>
    * 
    * @param document
    *           : le document retourné par DFCE.
    * @param metadata
    *           : La métadonnée désirés.
    * @throws UnknownCodeRndEx
    *            {@link UnknownCodeRndEx}
    * @throws ReferentialRndException
    *            {@link ReferentialRndException}
    * @throws ParseException
    *            {@link ParseException}
    * @throws ReferentialException
    *            {@link ReferentialException}
    */
   // CHECKSTYLE:OFF
   @SuppressWarnings( { "PMD.AvoidInstantiatingObjectsInLoops",
         "PMD.ExcessiveMethodLength", "PMD.CollapsibleIfStatements" })
   private void completedMetadatas(SAEDocument saeDocument, String rndCode)
         throws ReferentialRndException, UnknownCodeRndEx, ParseException,
         ReferentialException {
      // Traces debug - entrée méthode
      String prefixeTrc = "completedMetadatas()";
      LOGGER.debug("{} - Début", prefixeTrc);
      LOGGER.debug("{} - Code RND de référence : \"{}\"", prefixeTrc, rndCode);
      // Fin des traces debug - entrée méthode
      SAEMetadata saeMetadata = null;
      for (SAEArchivalMetadatas metadata : SAEArchivalMetadatas.values()) {
         saeMetadata = new SAEMetadata();
         saeMetadata.setLongCode(metadata.getLongCode());
         metadata = SAEMetatadaFinderUtils
               .metadtaFinder(metadata.getLongCode());

         if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.CODE_ACTIVITE.getLongCode())
                  .getShortCode());
            saeMetadata.setValue(rndReferenceDAO.getActivityCodeByRnd(rndCode));
            saeDocument.getMetadatas().add(saeMetadata);
            LOGGER
                  .debug(
                        "{} - Enrichissement des métadonnées : ajout de la métadonnée CodeActivite  valeur : {}",
                        prefixeTrc, rndReferenceDAO
                              .getActivityCodeByRnd(rndCode));
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.CODE_FONCTION.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.CODE_FONCTION.getLongCode())
                  .getShortCode());
            saeMetadata.setValue(rndReferenceDAO.getFonctionCodeByRnd(rndCode));
            saeDocument.getMetadatas().add(saeMetadata);
            LOGGER
                  .debug(
                        "{} - Enrichissement des métadonnées : ajout de la métadonnée CodeFonction   valeur : {}",
                        prefixeTrc, rndReferenceDAO
                              .getFonctionCodeByRnd(rndCode));
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode())) {
            LOGGER
                  .debug(
                        "{} - Durée de conservation pour le code RND {} : {} (jours)",
                        new Object[] { prefixeTrc, rndCode,
                              rndReferenceDAO.getStorageDurationByRnd(rndCode) });
            if (SAEMetatadaFinderUtils.dateMetadataFinder(saeDocument
                  .getMetadatas(), SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION
                  .getLongCode()) == null) {
               LOGGER
                     .debug(
                           "{} - DateDebutConservation n'est pas spécifiée par l'application cliente, "
                                 + "le calcule de DateFinConservation est basé sur la date du jour + Durée de conservation.",
                           prefixeTrc);
               Date dateFinConcervation = DateUtils.addDays(new Date(),
                     rndReferenceDAO.getStorageDurationByRnd(rndCode));
               LOGGER.debug("{} - Date de fin de conservation calculée : {}",
                     prefixeTrc, Utils.dateToString(dateFinConcervation));
               saeMetadata.setValue(dateFinConcervation);
            } else {
               Date dateFinConcervation = DateUtils.addDays(
                     SAEMetatadaFinderUtils.dateMetadataFinder(saeDocument
                           .getMetadatas(),
                           SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION
                                 .getLongCode()), rndReferenceDAO
                           .getStorageDurationByRnd(rndCode));
               saeMetadata.setValue(dateFinConcervation);
               LOGGER.debug("{} - Date de fin de conservation calculée : {}",
                     prefixeTrc, Utils.dateToString(dateFinConcervation));
            }
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode())
                  .getShortCode());
            saeDocument.getMetadatas().add(saeMetadata);
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.NOM_FICHIER.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.NOM_FICHIER.getLongCode())
                  .getShortCode());
            if (saeDocument.getFilePath() != null) {
               saeMetadata.setValue(FilenameUtils.getName(FilenameUtils
                  .separatorsToSystem(saeDocument.getFilePath())));
            } else {
               saeMetadata.setValue(saeDocument.getFileName());
            }
            saeDocument.getMetadatas().add(saeMetadata);
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.DATE_ARCHIVAGE.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.DATE_ARCHIVAGE.getLongCode())
                  .getShortCode());
            saeMetadata.setValue(new Date());
            saeDocument.getMetadatas().add(saeMetadata);
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION.getLongCode())) {
            if (SAEMetatadaFinderUtils.dateMetadataFinder(saeDocument
                  .getMetadatas(), SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION
                  .getLongCode()) == null) {
               LOGGER
                     .debug(
                           "{} - DateDebutConservation n'est pas spécifiée par l'application cliente. "
                                 + "Sa valeur est égale à la date d'archivage (date du jour).",
                           prefixeTrc);
               saeMetadata.setShortCode(metadataReferenceDAO
                     .getByLongCode(
                           SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION
                                 .getLongCode()).getShortCode());
               // La date DATEDEBUTCONSERVATION est égale à la date
               // d'archivage.
               saeMetadata.setValue(new Date());
               saeDocument.getMetadatas().add(saeMetadata);
            }
            LOGGER.debug(
                  "{} - DateDebutConservation est spécifiée par l'application cliente. "
                        + "On ne l'écrase pas avec DateArchivage.", prefixeTrc);
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.DOCUMENT_VIRTUEL.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.DOCUMENT_VIRTUEL.getLongCode())
                  .getShortCode());
            saeMetadata.setValue(false);
            saeDocument.getMetadatas().add(saeMetadata);
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.CONTRAT_DE_SERVICE.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.CONTRAT_DE_SERVICE.getLongCode())
                  .getShortCode());
            // FIXME attente de spécification.
            saeMetadata.setValue("ATT_PROD_001");
            saeDocument.getMetadatas().add(saeMetadata);
            LOGGER
                  .debug(
                        "{} - Enrichissement des métadonnées : ajout de la métadonnée ContratDeService valeur: {}",
                        prefixeTrc, saeMetadata.getValue());
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.VERSION_RND.getLongCode())) {
            if (SAEMetatadaFinderUtils.codeMetadataFinder(saeDocument
                  .getMetadatas(), SAEArchivalMetadatas.VERSION_RND
                  .getLongCode()) == null) {
               LOGGER
                     .debug(
                           "{} - La métadonnée VersionRND n'est pas spécifiée par l'application cliente",
                           prefixeTrc);
               saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                     SAEArchivalMetadatas.VERSION_RND.getLongCode())
                     .getShortCode());
               saeMetadata.setValue(rndReferenceDAO.getTypeDocument(rndCode)
                     .getVersionRnd());
               saeDocument.getMetadatas().add(saeMetadata);
               LOGGER
                     .debug(
                           "{} - Enrichissement des métadonnées : ajout de la métadonnée VersionRND valeur : {}",
                           prefixeTrc, saeMetadata.getValue());
            }
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.NOM_FICHIER.getLongCode())) {
            if (SAEMetatadaFinderUtils.codeMetadataFinder(saeDocument
                  .getMetadatas(), SAEArchivalMetadatas.NOM_FICHIER
                  .getLongCode()) == null) {
               LOGGER
                     .debug(
                           "{} - La métadonnée NOM_FICHIER n'est pas spécifiée par l'application cliente",
                           prefixeTrc);
               saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                     SAEArchivalMetadatas.NOM_FICHIER.getLongCode())
                     .getShortCode());
               String name = "";
               if (StringUtils.isEmpty(saeDocument.getFilePath())) {
                  name = saeDocument.getFileName();
               } else {
                  name = FilenameUtils.getBaseName(saeDocument.getFilePath());
               }
               saeMetadata.setValue(name);
               saeDocument.getMetadatas().add(saeMetadata);
               LOGGER
                     .debug(
                           "{} - Enrichissement des métadonnées : ajout du nom de fichier valeur : {}",
                           prefixeTrc, name);
            }
         }
         
      }
      // Traces debug - sortie méthode
      LOGGER.debug("{} - Sortie", prefixeTrc);
      // Fin des traces debug - sortie méthode
   }
   // CHECKSTYLE:ON
}
