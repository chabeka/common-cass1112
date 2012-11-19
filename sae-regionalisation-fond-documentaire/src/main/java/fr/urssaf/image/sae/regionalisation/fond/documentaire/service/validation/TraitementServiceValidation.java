package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * fr.urssaf.image.sae.regionalisation.fond.documentaire.service.
 * TraitementService.<br>
 * La validation est basée sur la programmation aspect
 * 
 * 
 */
@Aspect
public class TraitementServiceValidation {

   private static final String CLASS = "fr.urssaf.image.sae.regionalisation.fond.documentaire.service.TraitementService.";

   private static final String WRITE_ORG_METHOD = "execution(void " + CLASS
         + "writeCodesOrganismes(*))" + "&& args(filePath)";

   private static final String WRITE_UUID_METHOD = "execution(void " + CLASS
         + "writeDocUuidsToUpdate(*,*))"
         + "&& args(outputPath, propertiesPath)";

   private static final String UPDATE_DOCUMENT_METHOD = "execution(void "
         + CLASS + "updateDocuments(*,*,*,*))"
         + "&& args(inputPath, propertiesPath, first, last)";

   /**
    * Validation de la méthode de writeCodesOrganismes <br>
    * 
    * @param filePath
    *           chemin du fichier de sortie
    */
   @Before(WRITE_ORG_METHOD)
   public final void writeCodesOrganismes(String filePath) {

      if (StringUtils.isBlank(filePath)) {
         throw new IllegalArgumentException(
               "le paramètre filePath doit être renseigné");
      }
   }

   /**
    * Validation de la méthode de writeDocUuidsToUpdate <br>
    * 
    * @param outputPath
    *           chemin du fichier de sortie
    * @param propertiesPath
    *           fichier de correspondances
    */
   @Before(WRITE_UUID_METHOD)
   public final void writeUuids(String outputPath, String propertiesPath) {

      if (StringUtils.isBlank(outputPath)) {
         throw new IllegalArgumentException(
               "le paramètre fichier de sortie doit être renseigné");
      }

      if (StringUtils.isBlank(propertiesPath)) {
         throw new IllegalArgumentException(
               "le paramètre fichier de propriétés doit être renseigné");
      }
   }

   /**
    * Validation de la méthode de writeDocUuidsToUpdate <br>
    * 
    * @param inputPath
    *           chemin du fichier d'entrée
    * @param propertiesPath
    *           fichier de correspondances
    * @param first
    *           index du premier enregistrement
    * @param last
    *           index du dernier enregistrement
    */
   @Before(UPDATE_DOCUMENT_METHOD)
   public final void updateDocument(String inputPath, String propertiesPath,
         int first, int last) {

      if (StringUtils.isBlank(inputPath)) {
         throw new IllegalArgumentException(
               "le paramètre fichier d'entrée doit être renseigné");
      }

      if (StringUtils.isBlank(propertiesPath)) {
         throw new IllegalArgumentException(
               "le paramètre fichier de propriétés doit être renseigné");
      }
   }

}
