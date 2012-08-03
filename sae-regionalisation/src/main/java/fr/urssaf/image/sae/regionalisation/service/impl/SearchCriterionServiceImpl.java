package fr.urssaf.image.sae.regionalisation.service.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVReader;
import fr.urssaf.image.sae.regionalisation.bean.Metadata;
import fr.urssaf.image.sae.regionalisation.bean.SearchCriterion;
import fr.urssaf.image.sae.regionalisation.dao.MetadataDao;
import fr.urssaf.image.sae.regionalisation.dao.SearchCriterionDao;
import fr.urssaf.image.sae.regionalisation.service.SearchCriterionService;

/**
 * Implémentation du service {@link SearchCriterionService}
 * 
 * 
 */
@Service
public class SearchCriterionServiceImpl implements SearchCriterionService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(SearchCriterionService.class);

   private final MetadataDao metadataDao;

   private final SearchCriterionDao searchCriterionDao;

   /**
    * 
    * @param searchCriterionDao
    *           DAO des critères de recherche
    * @param metadataDao
    *           DAO sur les métadonnées associées aux critères de recherche
    */
   @Autowired
   public SearchCriterionServiceImpl(SearchCriterionDao searchCriterionDao,
         MetadataDao metadataDao) {

      this.searchCriterionDao = searchCriterionDao;
      this.metadataDao = metadataDao;
   }

   private static final char SEPARATOR = '$';

   /**
    * {@inheritDoc} <br>
    * exemple de fichiers cvs traités:
    * 
    * <pre>
    *    Annuaire des numéros de compte externe
    *    Reférence fixe$Ancien code organisme$ancien numéro de cpte ext.$nouveau numéro de cpte exter$nouveau code organisme
    *    COTI_CLE$630$630000000004296911$837000000000012658$837
    *    Annuaire des numéros internes
    *    CPTE_CLE$630$0023552$0009586$837
    *    Reférence fixe$Ancien code organisme$ancien numéro interne$nouveau numéro interne$nouveau code organisme
    *    PERS_CLE$150$0015197$2015197$837
    *    Reférence fixe$Ancien code organisme$ancien numéro de personne$nouveau numéro de personne$nouveau code organisme
    *    STRU_CLE$030$0000368113$0000021327$837
    *    Reférence fixe$Ancien code organisme$ancien numéro de structure$nouveau numéro de structure$nouveau code organisme
    * </pre>
    * 
    */
   @Override
   public final void enregistrerSearchCriterion(File searchCriterionCvs)
         throws IOException {

      FileReader reader = new FileReader(searchCriterionCvs);

      CSVReader csvReader = new CSVReader(reader, SEPARATOR);

      String[] line;

      while ((line = csvReader.readNext()) != null) {

         LOGGER.debug(ArrayUtils.toString(line));

         String ref = line[0];

         String[] args = (String[]) ArrayUtils.subarray(line, 1, line.length);

         if ("COTI_CLE".equals(ref)) {

            saveCompteExterne(args);

         } else if ("CPTE_CLE".equals(ref)) {

            saveNumeroInterne(args);

         } else if ("PERS_CLE".equals(ref)) {

            saveNumeroPersonne(args);

         } else {

            LOGGER.warn("la référence {} n'est pas prise en compte.", ref);
         }

      }

   }

   private static final int COG_INDEX = 3;

   @Transactional
   private void saveCompteExterne(String[] args) {

      SearchCriterion searchCriterion = new SearchCriterion();

      String lucene = "nce:" + args[1];
      searchCriterion.setLucene(lucene);
      searchCriterion.setUpdated(false);

      searchCriterionDao.save(searchCriterion);

      LOGGER.debug("persistance: {}", searchCriterion);

      List<Metadata> metadatas = new ArrayList<Metadata>();

      Metadata metadata0 = new Metadata();

      metadata0.setCode("cog");
      metadata0.setValue(args[COG_INDEX]);
      metadata0.setFlag(true);

      metadatas.add(metadata0);

      Metadata metadata1 = new Metadata();

      metadata1.setCode("nce");
      metadata1.setValue(args[2]);
      metadata1.setFlag(true);

      metadatas.add(metadata1);

      LOGGER.debug("persistance métadonnée du critère de recherche {}: {}",
            searchCriterion.getId(), metadatas);

      metadataDao.save(searchCriterion.getId(), metadatas);
   }

   @Transactional
   private void saveNumeroInterne(String[] args) {

      SearchCriterion searchCriterion = new SearchCriterion();

      String lucene = "nci:" + args[1];
      searchCriterion.setLucene(lucene);
      searchCriterion.setUpdated(false);

      searchCriterionDao.save(searchCriterion);

      LOGGER.debug("persistance: {}", searchCriterion);

      List<Metadata> metadatas = new ArrayList<Metadata>();

      Metadata metadata0 = new Metadata();

      metadata0.setCode("cog");
      metadata0.setValue(args[COG_INDEX]);
      metadata0.setFlag(true);

      metadatas.add(metadata0);

      Metadata metadata1 = new Metadata();

      metadata1.setCode("nci");
      metadata1.setValue(args[2]);
      metadata1.setFlag(true);

      metadatas.add(metadata1);

      LOGGER.debug("persistance métadonnée du critère de recherche {}: {}",
            searchCriterion.getId(), metadatas);

      metadataDao.save(searchCriterion.getId(), metadatas);

   }

   @Transactional
   private void saveNumeroPersonne(String[] args) {

      SearchCriterion searchCriterion = new SearchCriterion();

      String lucene = "npe:" + args[1];
      searchCriterion.setLucene(lucene);
      searchCriterion.setUpdated(false);

      searchCriterionDao.save(searchCriterion);

      LOGGER.debug("persistance: {}", searchCriterion);

      List<Metadata> metadatas = new ArrayList<Metadata>();

      Metadata metadata0 = new Metadata();

      metadata0.setCode("cog");
      metadata0.setValue(args[COG_INDEX]);
      metadata0.setFlag(true);

      metadatas.add(metadata0);

      Metadata metadata1 = new Metadata();

      metadata1.setCode("npe");
      metadata1.setValue(args[2]);
      metadata1.setFlag(true);

      metadatas.add(metadata1);

      LOGGER.debug("persistance métadonnée du critère de recherche {}: {}",
            searchCriterion.getId(), metadatas);

      metadataDao.save(searchCriterion.getId(), metadatas);

   }

}
