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
import fr.urssaf.image.sae.bo.model.bo.SAEVirtualDocument;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.mapping.utils.Utils;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.rnd.exception.CodeRndInexistantException;
import fr.urssaf.image.sae.rnd.service.RndService;
import fr.urssaf.image.sae.services.enrichment.SAEEnrichmentMetadataService;
import fr.urssaf.image.sae.services.enrichment.dao.impl.SAEMetatadaFinderUtils;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.SAEEnrichmentEx;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Classe concrète pour l’enrichissement des métadonnées.
 */
@Service
@Qualifier("saeEnrichmentMetadataService")
@SuppressWarnings( { "PMD.CyclomaticComplexity", "PMD.LongVariable" })
public class SAEEnrichmentMetadataServiceImpl implements
      SAEEnrichmentMetadataService {
   private static final Logger LOGGER = LoggerFactory
         .getLogger(SAEEnrichmentMetadataServiceImpl.class);

   @Autowired
   private RndService rndService;

   @Autowired
   private ParametersService parametersService;

   @Autowired
   @Qualifier("metadataReferenceDAO")
   private MetadataReferenceDAO metadataReferenceDAO;

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

      String fileName = saeDoc.getFileName();
      String filePath = saeDoc.getFilePath();
      completedMetadatas(saeDoc.getMetadatas(), fileName, filePath);

      LOGGER.debug("{} - Sortie", prefixeTrc);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void enrichmentVirtualMetadata(SAEVirtualDocument document)
         throws SAEEnrichmentEx, ReferentialRndException, UnknownCodeRndEx {
      String trcPrefix = "enrichmentVirtualMetadata";
      LOGGER.debug("{} - début", trcPrefix);

      String filePath = document.getReference().getFilePath();
      String fileName = FilenameUtils.getBaseName(filePath);
      String startPage = String.valueOf(document.getStartPage());
      String endPage = String.valueOf(document.getEndPage());
      fileName = fileName.concat("_").concat(startPage).concat("_").concat(
            endPage);

      completedMetadatas(document.getMetadatas(), fileName, null);

      LOGGER.debug("{} - fin", trcPrefix);

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
    * @throws CodeRndInexistantException
    */
   private void authorizedCodeRnd(String rndValue)
         throws CodeRndInexistantException {
      
      if (rndService.isCloture(rndValue)) {
         throw new CodeRndInexistantException("Le code RND " + rndValue + " n'est pas autorisé à l'archivage (code clôturé).");
      }
   }

   private void completedMetadatas(List<SAEMetadata> metadatas, String rndCode,
         String filePath, String fileName) throws ReferentialException,
         CodeRndInexistantException, ParseException, ParameterNotFoundException {
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
            String codeActiviteValue = rndService.getCodeActivite(rndCode);

            if (StringUtils.isNotBlank(codeActiviteValue)) {

               saeMetadata.setValue(codeActiviteValue);
               metadatas.add(saeMetadata);
               LOGGER
                     .debug(
                           "{} - Enrichissement des métadonnées : ajout de la métadonnée CodeActivite  valeur : {}",
                           prefixeTrc, rndService.getCodeActivite(rndCode));
            }

         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.CODE_FONCTION.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.CODE_FONCTION.getLongCode())
                  .getShortCode());
            saeMetadata.setValue(rndService.getCodeFonction(rndCode));
            metadatas.add(saeMetadata);
            LOGGER
                  .debug(
                        "{} - Enrichissement des métadonnées : ajout de la métadonnée CodeFonction   valeur : {}",
                        prefixeTrc, rndService.getCodeFonction(rndCode));
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode())) {
            LOGGER
                  .debug(
                        "{} - Durée de conservation pour le code RND {} : {} (jours)",
                        new Object[] { prefixeTrc, rndCode,
                              rndService.getDureeConservation(rndCode) });
            if (SAEMetatadaFinderUtils.dateMetadataFinder(metadatas,
                  SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION.getLongCode()) == null) {
               LOGGER
                     .debug(
                           "{} - DateDebutConservation n'est pas spécifiée par l'application cliente, "
                                 + "le calcule de DateFinConservation est basé sur la date du jour + Durée de conservation.",
                           prefixeTrc);
               Date dateFinConcervation = DateUtils.addDays(new Date(),
                     rndService.getDureeConservation(rndCode));
               LOGGER.debug("{} - Date de fin de conservation calculée : {}",
                     prefixeTrc, Utils.dateToString(dateFinConcervation));
               saeMetadata.setValue(dateFinConcervation);
            } else {
               Date dateFinConcervation = DateUtils.addDays(
                     SAEMetatadaFinderUtils.dateMetadataFinder(metadatas,
                           SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION
                                 .getLongCode()), rndService
                           .getDureeConservation(rndCode));
               saeMetadata.setValue(dateFinConcervation);
               LOGGER.debug("{} - Date de fin de conservation calculée : {}",
                     prefixeTrc, Utils.dateToString(dateFinConcervation));
            }
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.DATE_FIN_CONSERVATION.getLongCode())
                  .getShortCode());
            metadatas.add(saeMetadata);
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.NOM_FICHIER.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.NOM_FICHIER.getLongCode())
                  .getShortCode());
            if (filePath == null) {
               saeMetadata.setValue(fileName);
            } else {
               saeMetadata.setValue(FilenameUtils.getName(FilenameUtils
                     .separatorsToSystem(filePath)));
            }
            metadatas.add(saeMetadata);
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.DATE_ARCHIVAGE.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.DATE_ARCHIVAGE.getLongCode())
                  .getShortCode());
            saeMetadata.setValue(new Date());
            metadatas.add(saeMetadata);
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION.getLongCode())) {
            if (SAEMetatadaFinderUtils.dateMetadataFinder(metadatas,
                  SAEArchivalMetadatas.DATE_DEBUT_CONSERVATION.getLongCode()) == null) {
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
               metadatas.add(saeMetadata);
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
            metadatas.add(saeMetadata);
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.CONTRAT_DE_SERVICE.getLongCode())) {
            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.CONTRAT_DE_SERVICE.getLongCode())
                  .getShortCode());

            LOGGER.debug("{} - Récupération des droits", prefixeTrc);
            AuthenticationToken token = AuthenticationContext
                  .getAuthenticationToken();
            VIContenuExtrait extrait = (VIContenuExtrait) token.getPrincipal();
            LOGGER.debug("{} - Mise a jour du code application", prefixeTrc);
            String codeContrat = extrait.getCodeAppli();
            saeMetadata.setValue(codeContrat);
            metadatas.add(saeMetadata);
            LOGGER
                  .debug(
                        "{} - Enrichissement des métadonnées : ajout de la métadonnée ContratDeService valeur: {}",
                        prefixeTrc, saeMetadata.getValue());
         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.VERSION_RND.getLongCode())) {

            saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                  SAEArchivalMetadatas.VERSION_RND.getLongCode())
                  .getShortCode());

            saeMetadata.setValue(parametersService.getVersionRndNumero());

            metadatas.add(saeMetadata);
            LOGGER
                  .debug(
                        "{} - Enrichissement des métadonnées : ajout de la métadonnée VersionRND valeur : {}",
                        prefixeTrc, saeMetadata.getValue());

         } else if (metadata.getLongCode().equals(
               SAEArchivalMetadatas.NOM_FICHIER.getLongCode())) {
            if (SAEMetatadaFinderUtils.codeMetadataFinder(metadatas,
                  SAEArchivalMetadatas.NOM_FICHIER.getLongCode()) == null) {
               LOGGER
                     .debug(
                           "{} - La métadonnée NOM_FICHIER n'est pas spécifiée par l'application cliente",
                           prefixeTrc);
               saeMetadata.setShortCode(metadataReferenceDAO.getByLongCode(
                     SAEArchivalMetadatas.NOM_FICHIER.getLongCode())
                     .getShortCode());
               String name = "";
               if (StringUtils.isEmpty(filePath)) {
                  name = fileName;
               } else {
                  name = FilenameUtils.getBaseName(filePath);
               }
               saeMetadata.setValue(name);
               metadatas.add(saeMetadata);
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

   private void completedMetadatas(List<SAEMetadata> metadatas,
         String docFileName, String docFilePath)
         throws ReferentialRndException, UnknownCodeRndEx, SAEEnrichmentEx {

      String trcPrefix = "completedMetadatas()";
      LOGGER.debug("{} - Début Enrichissement des métadonnées", trcPrefix);
      LOGGER.debug("{} - Paramètre saeDocument : \"{}\"", trcPrefix, metadatas
            .toString());

      String rndValue = SAEMetatadaFinderUtils.codeMetadataFinder(metadatas,
            SAEArchivalMetadatas.CODE_RND.getLongCode());

      String fileName = SAEMetatadaFinderUtils.codeMetadataFinder(metadatas,
            SAEArchivalMetadatas.NOM_FICHIER.getLongCode());

      try {
         if (!StringUtils.isEmpty(rndValue)) {
            LOGGER.debug("{} - Début de la vérification : "
                  + "Le type de document est autorisé en archivage", metadatas);
            authorizedCodeRnd(rndValue);
            LOGGER.debug("{} - Fin de la vérification : "
                  + "Le type de document est autorisé en archivage", metadatas);
            LOGGER.debug("{} - Métadonnées avant enrichissement : {}",
                  metadatas, metadatas.toString());
            completedMetadatas(metadatas, rndValue, docFileName, docFilePath);
            LOGGER.debug("{} - Métadonnées après enrichissement : {}",
                  metadatas, metadatas.toString());
         }
         if (!StringUtils.isBlank(fileName)) {

            LOGGER.debug("{} - Métadonnées avant enrichissement : {}",
                  metadatas, metadatas.toString());
            completedMetadatas(metadatas, fileName, docFileName, docFilePath);
            LOGGER.debug("{} - Métadonnées après enrichissement : {}",
                  metadatas, metadatas.toString());
         }
      } catch (CodeRndInexistantException e) {
         LOGGER.debug(
               "{} - Le code RND {} ne fait pas partie des codes autorisés",
               metadatas, rndValue);
         throw new UnknownCodeRndEx(e.getMessage(), e);
      } catch (ParseException e) {
         throw new SAEEnrichmentEx(e.getMessage(), e);
      } catch (ReferentialException e) {
         throw new SAEEnrichmentEx(e.getMessage(), e);
      } catch (ParameterNotFoundException e) {
         throw new SAEEnrichmentEx(e.getMessage(), e);
      }
      // Traces debug - sortie méthode
      LOGGER.debug("{} - Fin Enrichissement des métadonnées", trcPrefix);
   }
}
