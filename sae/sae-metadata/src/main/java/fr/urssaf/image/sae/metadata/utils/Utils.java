package fr.urssaf.image.sae.metadata.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Cette classe contient des méthodes utilitaires
 * 
 * @author akenore
 * 
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class Utils {
   /**
    * Simplifie l'écriture des boucles foreach quand l'argument peut être
    * {@code null}.
    * 
    * @param <T>
    *           le type des éléments
    * @param anIterable
    *           les éléments à parcourir
    * @return les éléments, ou une collection vide si l'argument était null
    */
   @SuppressWarnings("PMD.OnlyOneReturn")
   public static <T> Iterable<T> nullSafeIterable(final Iterable<T> anIterable) {
      if (anIterable == null) {
         return Collections.emptyList();
      } else {
         return anIterable;
      }
   }

   /**
    * Simplifie l'écriture des map
    * 
    * @param map
    *           le type des éléments
    * @param <K>
    *           : type
    * @param <V>
    *           : type
    * @return les éléments, ou une map vide si l'argument était null
    */
   @SuppressWarnings("PMD.OnlyOneReturn")
   public static <K, V> Map<K, V> nullSafeMap(final Map<K, V> map) {
      if (map == null) {
         return Collections.emptyMap();
      } else {
         return map;
      }
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private Utils() {
      assert false;
   }

   /**
    * Permet d'identifier les doublons
    * 
    * @param metadata
    *           : La métadonnée
    * @param metadatas
    *           : La liste des métadonnées.
    * @return True si la liste contient un doublon de la métadonnée fournit en
    *         entrée.
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static boolean hasDuplicate(final UntypedMetadata metadata,
         final List<UntypedMetadata> metadatas) {
      int count = 0;
      for (UntypedMetadata data : metadatas) {
         if (data.getLongCode().trim().equals(metadata.getLongCode().trim())) {
            count++;
         }
      }
      return count == 1;
   }

   /**
    * Permet d'identifier les doublons dans la liste des errors ceci pour ne pas
    * les déclarer en double.
    * 
    * @param error
    *           : L'erreur
    * @param errors
    *           : La liste des erreurs.
    * @return True si la liste contient un doublon de l'erreur fournit en
    *         entrée.
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static boolean exist(final MetadataError error,
         final List<MetadataError> errors) {
      int count = 0;
      for (MetadataError err : errors) {
         if (err.getLongCode().contains(error.getLongCode())) {
            count++;
         }
      }
      return count == 1;
   }

   /**
    * Permet de vérifier qu'une métadonnée est bien dans une liste spécifique de
    * métadonnée.
    * 
    * @param metadata
    *           : La métadonnée
    * @param metadatas
    *           : La liste des métadonnées.
    * @return True si la liste contient un bien de la métadonnée fournit en
    *         entrée.
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public static boolean isInRequiredList(final MetadataReference metadata,
         final List<SAEMetadata> metadatas) {
      boolean found = false;
      for (SAEMetadata data : Utils.nullSafeIterable(metadatas)) {
         if (data.getLongCode().trim().equals(metadata.getLongCode().trim())) {
            found = true;
            break;
         }
      }
      return found;
   }

   /**
    * Permet qu'une métadonnée est dans la liste de métadonnées
    * @param references
    *           la liste des métadonnées
    * @param longCode
    *           le code long de la métadonnée
    * 
    * @return True si la liste contient la métadonnée
    */
   public static boolean isRequired(Map<String, MetadataReference> references,
         String longCode) {
      boolean found = false;

      int index = 0;
      List<MetadataReference> listReferences = new ArrayList<MetadataReference>(
            nullSafeMap(references).values());
      while (!found && index < listReferences.size()) {
         if (longCode.equals(listReferences.get(index).getLongCode())) {
            found = true;
         }
         index++;
      }

      return found;
   }
}
