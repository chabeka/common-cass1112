/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.DocInfoService;
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

      } finally {
         cassandraSupport.disconnect();
      }

   }

}
