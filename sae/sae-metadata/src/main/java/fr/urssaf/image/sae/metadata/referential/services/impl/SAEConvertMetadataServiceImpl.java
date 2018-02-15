/**
 * 
 */
package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.messages.MetadataMessageHandler;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.referential.services.SAEConvertMetadataService;

/**
 * Service permettant de convertir des codes de métadonnées selon le référentiel
 * des métadonnées
 * 
 */
@Service
public class SAEConvertMetadataServiceImpl implements SAEConvertMetadataService {

   @Autowired
   private MetadataReferenceDAO metaRefD;

   /**
    * Caractère de séparation lors de la concaténation des metasdatas non
    * supportées
    */
   private static final String SEPARATOR_CHAR = ", ";

   /**
    * {@inheritDoc}
    */
   @Override
   public final Map<String, String> longCodeToShortCode(
         List<String> listCodeMetadata) throws LongCodeNotFoundException {

      Map<String, String> map = new HashMap<String, String>();
      List<String> errorList = new ArrayList<String>();

      for (String codeLong : listCodeMetadata) {
         // recuperer MetaDataReference qui nous renvoie un objet avec
         // codeCourt
         // et codeLong
         // MetadataReference metadaReference = new MetadataReference();
         try {
            if (metaRefD.getByLongCode(codeLong) == null) {
               errorList.add(codeLong);
            } else {
               MetadataReference metadaReference = metaRefD
                     .getByLongCode(codeLong);
               // et ensuite recup le code court
               String codeCourt = metadaReference.getShortCode();
               // ajout dans une Map<String, String>
               map.put(codeCourt, codeLong);
            }
         } catch (ReferentialException except) {
            errorList.add(codeLong);
         }
      }

      if (!errorList.isEmpty()) {
         String value = StringUtils.join(errorList, SEPARATOR_CHAR);
         String message = MetadataMessageHandler.getMessage(
               "codelong.notfound.error", value);
         throw new LongCodeNotFoundException(message, errorList);
      }

      return map;
   }

}
