package fr.urssaf.image.sae.storage.dfce.data.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageMetadata;

/**
 * Classe utilitaire proposant des services de contrôle des éléments des
 * documents DFCE et ceux des documents SAE.
 * 
 *@author rhofir, kenore
 */
public final class CheckDataUtils {

   /**
    * Comparer le SHA1 de deux documents.
    * 
    * @param firstSaeDocument
    *           : premier document deux de type {@link byte}
    * @param secondSaeDocument
    *           : Deuxième document un de type {@link byte}
    * @return boolean True si les deux sha1 sont identique sinon false
    * @throws UnsupportedEncodingException
    *            Exception levé quand l'encodage n'est pas supporter.
    * @throws NoSuchAlgorithmException
    *            Exception levé quand l'algorthme n'est pas supporter.
    */
   public static boolean checkDocumentSha1(final byte[] firstSaeDocument,
        final byte[] secondSaeDocument) throws NoSuchAlgorithmException,
         UnsupportedEncodingException {
      Assert.assertNotNull("firstSaeDocument ne doit pas être null", firstSaeDocument);
      Assert.assertNotNull("secondSaeDocument ne doit pas être null", secondSaeDocument);
      return DigestUtils.shaHex(firstSaeDocument).equals(
            DigestUtils.shaHex(secondSaeDocument));
   }

   /**
    * Compare deux listes de métadonnées.
    * 
    * @param entryMetadatas
    *           : Premier liste des métadonnées.
    * @param desiredMetadatas
    *           : Deuxième liste des métadonnées.
    * @return true si les deux listes sont identique sinon false
    */
   public static boolean checkMetaDatas(final List<StorageMetadata> entryMetadatas,
       final  List<StorageMetadata> desiredMetadatas) {
      Assert.assertNotNull("entryMetadatas ne doit pas être null",
            entryMetadatas);
      Assert.assertNotNull("desiredMetadatas  ne doit pas être null",
            desiredMetadatas);
      boolean isMetaDatasEquals = true;
      for (StorageMetadata desiredMetadata : Utils
            .nullSafeIterable(desiredMetadatas)) {
         final Object saeValue = desiredMetadata.getValue();
         final String codeMetaData = desiredMetadata.getShortCode();
         for (StorageMetadata entryMetadata : Utils
               .nullSafeIterable(entryMetadatas)) {
            final String dfceCodeMetaData = entryMetadata.getShortCode();
            if (dfceCodeMetaData.equals(codeMetaData)
                  && !compareTwoObjects(saeValue, entryMetadata.getValue())) {
               isMetaDatasEquals = false;
               break;
            }
         }
      }
      return isMetaDatasEquals;
   }

   /**
    * Compare deux listes de métadonnées.
    * 
    * @param entryMetadatas
    *           : Premier liste des métadonnées.
    * @param desiredMetadatas
    *           : Deuxième liste des métadonnées.
    * @return true si les deux listes sont identique sinon false
    */
   public static boolean checkDesiredMetaDatas(final 
         List<StorageMetadata> entryMetadatas,
       final  List<StorageMetadata> desiredMetadatas) {
      Assert.assertNotNull("EntryMetadatas ne doit pas être null",
            entryMetadatas);
      Assert.assertNotNull("DesiredMetadatas  ne doit pas être null",
            desiredMetadatas);
      Assert.assertEquals("La tailles des deux listes doivent être égale :",
            entryMetadatas.size(), desiredMetadatas.size());
      int nbrMDEqualas = 0;
      for (StorageMetadata desiredMetadata : Utils
            .nullSafeIterable(desiredMetadatas)) {
         final String codeMetaData = desiredMetadata.getShortCode();
         for (StorageMetadata entryMetadata : Utils
               .nullSafeIterable(entryMetadatas)) {
            final String dfceCodeMetaData = entryMetadata.getShortCode();
            if (dfceCodeMetaData.equals(codeMetaData)) {
               nbrMDEqualas++;
            }
         }
      }
      if (entryMetadatas.size() != nbrMDEqualas) {
         Assert
               .fail("Les codes des métadonnées des deux listes doivent être identique.");
      }
      return true;
   }

   /**
    * Compare deux objets dynamiquement des types :<br>
    * <i>{@link Integer}</i><br>
    * <i>{@link Date}</i><br>
    * <i>{@link String}</i><br>
    * 
    * @param firstObject
    *           : Le premier Objet
    * @param secondObject
    *           : Le deuxième Objet
    * @return true si les deux objets sont identiques.
    */
   private static boolean compareTwoObjects(final Object firstObject,
		   final  Object secondObject) {
      boolean isSame = true;
      if (firstObject instanceof Integer) {
         if (firstObject.hashCode() != Integer.valueOf((String) secondObject)
               .hashCode()) {
            isSame = false;
         }
      } else if (firstObject instanceof Date) {
         if (!DateUtils.isSameDay((Date) firstObject, (Date) secondObject)) {
            isSame = false;
         }
      } else {
         if (!firstObject.equals(secondObject)) {
            isSame = false;
         }
      }
      return isSame;
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private CheckDataUtils() {
      assert false;
   }
}
