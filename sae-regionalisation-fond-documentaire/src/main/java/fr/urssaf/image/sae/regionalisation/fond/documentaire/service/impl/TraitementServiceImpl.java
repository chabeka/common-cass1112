/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.common.Constants;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.DocInfoService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.TermInfoRangeUuidService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.TraitementService;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;

/**
 * Implémentation de l'interface {@link TraitementService}
 * 
 */
@Component
public class TraitementServiceImpl implements TraitementService {

   @Autowired
   private DocInfoService docInfoService;

   @Autowired
   private TermInfoRangeUuidService termInfoService;

   @Autowired
   private CassandraSupport cassandraSupport;

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraitementServiceImpl.class);

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeCodesOrganismes(String filePath) {

      String trcPrefix = "writeCodesOrganismes";

      try {
         cassandraSupport.connect();

         LOGGER.debug("{} - récupération des codes organismes", trcPrefix);

         List<String> codes = docInfoService.getCodesOrganismes();
         File file = new File(filePath);

         LOGGER.debug(
               "{} - Ecriture des codes organismes récupérés dans le fichier",
               trcPrefix);

         FileUtils.writeLines(file, codes);

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);

      } catch (CassandraException exception) {
         throw new ErreurTechniqueException(exception);

      } finally {
         cassandraSupport.disconnect();
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void writeDocUuidsToUpdate(String outputPath,
         String propertiesFilePath) {

      String trcPrefix = "writeDocUuidsToUpdate";
      InputStream inStream = null;

      try {
         cassandraSupport.connect();

         File file = new File(propertiesFilePath);
         inStream = FileUtils.openInputStream(file);

         Properties properties = new Properties();
         properties.load(inStream);

         LOGGER.debug(
               "{} - récupération de tous les documents et des métadonnées",
               trcPrefix);

         List<Map<String, String>> documents = termInfoService.getInfosDoc();
         List<String> uuids = new ArrayList<String>();

         for (Map<String, String> document : documents) {
            if (properties.keySet().contains(
                  document.get(Constants.CODE_ORG_GEST))
                  || properties.keySet().contains(
                        document.get(Constants.CODE_ORG_PROP))) {
               uuids.add(document.get(Constants.UUID));
            }
         }

         File fileOutput = new File(outputPath);
         FileUtils.writeLines(fileOutput, uuids);

      } catch (IOException exception) {
         throw new ErreurTechniqueException(exception);

      } finally {

         if (inStream != null) {
            try {
               inStream.close();
            } catch (IOException exception) {
               LOGGER.info("{} - Impossible de fermer le flux", trcPrefix);
            }
         }

         cassandraSupport.disconnect();
      }

   }

}
