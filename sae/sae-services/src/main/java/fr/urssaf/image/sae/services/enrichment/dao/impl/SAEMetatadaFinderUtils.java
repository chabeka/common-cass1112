package fr.urssaf.image.sae.services.enrichment.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.services.enrichment.xml.model.SAEArchivalMetadatas;

/**
 * Classe utilitaire pour la recherche des métadonnées à partir d'une liste de
 * SAEMetadta.
 */
public final class SAEMetatadaFinderUtils {

   /**
    * Permet de trouver la métadonnée dans l'enumération des
    * SAEEnrichmentMetadatas
    * 
    * @param longCode
    *           : Le code cherché
    * @return Le code long métier correspondant à la métadonnéee cherché.
    */
   @SuppressWarnings("PMD.OnlyOneReturn")
   public static SAEArchivalMetadatas metadtaFinder(final String longCode) {
      for (SAEArchivalMetadatas metadata : SAEArchivalMetadatas.values()) {
         if (metadata.getLongCode().equalsIgnoreCase(longCode)) {
            return metadata;
         }
      }
      return null;
   }

   /**
    * Permet d'extraire la métadonnée code RND à partir d'une liste de
    * métadonnée.
    * 
    * @param metadata
    *           : Une métadonnée.
    * @param codeLong
    *           : Le code long de la métadonnée.
    * @return Valeur du code long.
    */
   private static Object valueMetadataFinder(SAEMetadata metadata,
         String codeLong) {
      Object codeRndValue = null;
      final SAEArchivalMetadatas technical = metadataFinder(metadata
            .getLongCode());
      if (codeLong.equals(technical.getLongCode())) {
         codeRndValue = (Object) metadata.getValue();
      }
      return codeRndValue;
   }

   /**
    * Permet de trouver le bon type dans l'enumération des types.
    * 
    * @param shortCode
    *           : Le code court cherché
    * @return Le type métier correspondant au type cherché.
    */
   @SuppressWarnings("PMD.OnlyOneReturn")
   private static SAEArchivalMetadatas metadataFinder(final String longCode) {
      for (SAEArchivalMetadatas technical : SAEArchivalMetadatas.values()) {
         if (technical.getLongCode().equals(longCode)) {
            return technical;
         }
      }
      return SAEArchivalMetadatas.NOVALUE;
   }

   /**
    * Récupére la valeur de la métadonnée.
    * 
    * @param saeMetadatas
    *           : liste des métadonnées.
    * @param codeLong
    *           : Code long.
    * @return Valeur de la métadonnée.
    */
   public static String codeMetadataFinder(List<SAEMetadata> saeMetadatas,
         String codeLong) {
      String metadataValue = null;
      for (SAEMetadata saeMetadata : saeMetadatas) {
         if (StringUtils.isEmpty(metadataValue)) {
            metadataValue = (String) valueMetadataFinder(saeMetadata, codeLong);
         } else {
            break;
         }
      }
      return metadataValue;
   }

   

   /**
    * Récupére la valeur de la métadonnée.
    * 
    * @param saeMetadatas
    *           : liste des métadonnées.
    * @param codeLong
    *           : Code long.
    * @return Valeur de la métadonnée.
    */
   public static Date dateMetadataFinder(List<SAEMetadata> saeMetadatas,
         String codeLong) {
      Date metadataValue = null;
      for (SAEMetadata saeMetadata : saeMetadatas) {
         if (metadataValue == null) {
            metadataValue = (Date) valueMetadataFinder(saeMetadata, codeLong);
         } else {
            break;
         }
      }
      return metadataValue;
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private SAEMetatadaFinderUtils() {
      assert false;
   }
}
