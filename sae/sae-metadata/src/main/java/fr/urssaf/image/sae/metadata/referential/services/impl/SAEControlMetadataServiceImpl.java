/**
 * 
 */
package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.metadata.exceptions.LongCodeNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.messages.MetadataMessageHandler;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.referential.services.SAEControlMetadataService;

/**
 * Fournit les contrôles sur les metadatas
 * 
 */
@Service
public class SAEControlMetadataServiceImpl implements SAEControlMetadataService {

   @Autowired
   private MetadataReferenceDAO referenceDAO;

   /**
    * Caractère de séparation des éléments
    */
   private static final String SEPARATOR_CHAR = ", ";

   /**
    * {@inheritDoc}
    * 
    */
   @Override
   public final void controlLongCodeExist(List<String> listLongCode)
         throws ReferentialException, LongCodeNotFoundException {

      Map<String, MetadataReference> mapRef = referenceDAO
            .getAllMetadataReferences();

      Collection<String> result = getDifference(mapRef.values(), listLongCode);
      List<String> values = new ArrayList<String>(result);

      if (CollectionUtils.isNotEmpty(result)) {

         String listCodes = StringUtils.join(values, SEPARATOR_CHAR);
         Collections.sort(values);

         throw new LongCodeNotFoundException(MetadataMessageHandler.getMessage(
               "metadata.list.not.exist", listCodes), values);
      }

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void controlLongCodeIsAFConsultation(List<String> listLongCode)
         throws LongCodeNotFoundException, ReferentialException {

      Map<String, MetadataReference> mapRef = referenceDAO
            .getConsultableMetadataReferences();

      Collection<String> result = getDifference(mapRef.values(), listLongCode);
      List<String> values = new ArrayList<String>(result);

      if (CollectionUtils.isNotEmpty(result)) {
         String listCodes = StringUtils.join(values, SEPARATOR_CHAR);
         Collections.sort(values);

         throw new LongCodeNotFoundException(MetadataMessageHandler.getMessage(
               "metadata.list.not.consultable", listCodes), values);
      }

   }

   /**
    * Retourne les éléments présent dans listLongCode et absents de values
    * 
    * @param values
    *           liste des éléments à supprimer de la liste des valeurs
    * @param listLongCode
    *           liste des éléments à épurer
    * 
    * @return la liste des éléments résiduels de listLongCodes
    */
   @SuppressWarnings("unchecked")
   private Collection<String> getDifference(
         Collection<MetadataReference> values, List<String> listLongCode) {

      Collection<String> colRef = extractLongRefFromMetaDataRef(values);
      // le hashset permet de supprimer les eventuels doublon de la liste des code long demandes
      Collection<String> colLongCode = new HashSet<String>(listLongCode);

      Collection<String> result = CollectionUtils.subtract(colLongCode, colRef);

      return result;
   }

   /**
    * extrait la liste des codes long des metadata passées en paramètre
    * 
    * @param values
    *           liste des metadata
    * @return la liste des codes longs contenus dans les metadatas
    */
   private List<String> extractLongRefFromMetaDataRef(
         Collection<MetadataReference> values) {

      List<String> listRef = null;

      if (values != null) {
         listRef = new ArrayList<String>();

         for (MetadataReference metaRef : values) {
            listRef.add(metaRef.getLongCode());
         }
      }

      return listRef;
   }
}
