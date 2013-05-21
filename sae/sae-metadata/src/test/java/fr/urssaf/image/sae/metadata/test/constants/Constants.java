package fr.urssaf.image.sae.metadata.test.constants;

import java.io.File;

/**
 * Cette classe contient la liste des constantes utilisées dans l'application.
 * 
 * @author akenore.
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class Constants {
   // Fichier pour test la méthode checkArchivableMetadata
   public static final File ARCHIVABLE_FILE_1 = new File(
         "src/test/resources/Archivable/checkArchivable_1.xml");
   // Fichier pour test la méthode checkArchivableMetadata
   public static final File ARCHIVABLE_FILE_2 = new File(
         "src/test/resources/Archivable/checkArchivable_2.xml");
   // Fichier pour test la méthode checkSearchableMetadata
   public static final File SEARCHABLE_FILE_1 = new File(
         "src/test/resources/Searchable/checkSearchable_1.xml");
   // Fichier pour test la méthode checkSearchableMetadata
   public static final File SEARCHABLE_FILE_2 = new File(
         "src/test/resources/Searchable/checkSearchable_2.xml");

   // Fichier pour test la méthode checkConsultableMetadata
   @SuppressWarnings("PMD.LongVariable")
   public static final File CONSULTABLE_FILE_1 = new File(
         "src/test/resources/Consultable/checkConsultable_1.xml");
   // Fichier pour test la méthode checkConsultableMetadata
   @SuppressWarnings("PMD.LongVariable")
   public static final File CONSULTABLE_FILE_2 = new File(
         "src/test/resources/Consultable/checkConsultable_2.xml");

   // Fichier pour test la méthode checkConsultableMetadata
   @SuppressWarnings("PMD.LongVariable")
   public static final File SUPPRIMABLE_FILE_1 = new File(
         "src/test/resources/Supprimable/checkSupprimable_1.xml");
   // Fichier pour test la méthode checkConsultableMetadata
   @SuppressWarnings("PMD.LongVariable")
   public static final File SUPPRIMABLE_FILE_2 = new File(
         "src/test/resources/Supprimable/checkSupprimable_2.xml");

   // Fichier pour test la méthode checkConsultableMetadata
   @SuppressWarnings("PMD.LongVariable")
   public static final File MODIFIABLE_FILE_1 = new File(
         "src/test/resources/Modifiable/checkModifiable_1.xml");
   // Fichier pour test la méthode checkConsultableMetadata
   @SuppressWarnings("PMD.LongVariable")
   public static final File MODIFIABLE_FILE_2 = new File(
         "src/test/resources/Modifiable/checkModifiable_2.xml");

   // Fichier pour test la méthode checkDuplicateMetadata
   public static final File DUPLICATE_FILE_1 = new File(
         "src/test/resources/Duplicate/checkDuplicate_1.xml");
   // Fichier pour test la méthode checkDuplicateMetadata
   public static final File DUPLICATE_FILE_2 = new File(
         "src/test/resources/Duplicate/checkDuplicate_2.xml");

   // Fichier pour test la méthode checkExistingMetadata
   public static final File EXISTING_FILE_1 = new File(
         "src/test/resources/Existing/checkExisting_1.xml");
   // Fichier pour test la méthode checkExistingMetadata
   public static final File EXISTING_FILE_2 = new File(
         "src/test/resources/Existing/checkExisting_2.xml");
   // Fichier pour test la méthode checkRequiredForArchivalMetadata
   public static final File REQUIRED_FILE_1 = new File(
         "src/test/resources/Required/checkRequired_1.xml");
   // Fichier pour test la méthode checkRequiredForArchivalMetadata
   public static final File REQUIRED_FILE_2 = new File(
         "src/test/resources/Required/checkRequired_2.xml");

   // Fichier pour test la méthode checkRequiredForStorageMetadata
   public static final File REQUIRED_FILE_3 = new File(
         "src/test/resources/Required/checkRequired_3.xml");
   // Fichier pour test la méthode checkRequiredForStorageMetadata
   public static final File REQUIRED_FILE_4 = new File(
         "src/test/resources/Required/checkRequired_4.xml");

   // Fichier pour test la méthode checkTypeAndFormatMetadata
   @SuppressWarnings("PMD.LongVariable")
   public static final File TYPE_FORMAT_FILE_1 = new File(
         "src/test/resources/TypeAndFormat/checkTypeAndFormat_1.xml");
   // Fichier pour test la méthode checkTypeAndFormatMetadata
   @SuppressWarnings("PMD.LongVariable")
   public static final File TYPE_FORMAT_FILE_2 = new File(
         "src/test/resources/TypeAndFormat/checkTypeAndFormat_2.xml");
   // Fichier pour test la recherche de code long et code court.
   @SuppressWarnings("PMD.LongVariable")
   public static final File SHORT_LONG_CODE = new File(
         "src/test/resources/Performance/short_long_code_test.xml");

   /** Cette classe n'est pas faite pour être instanciée. */
   private Constants() {
      assert false;
   }

}
