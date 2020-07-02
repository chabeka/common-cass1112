package fr.urssaf.image.parser_opencsv.application.constantes;

public class FileConst {

   public static final String SOMMAIRE_FILE_NAME = "sommaire.xml";

   public static final String CSV_FILE_NAME = "users_quote.csv";

   public static final String CSV_REAL_FILE_NAME = "extraction.csv";

   public static final String CSV_CORRESPONDANCE_CAISSE = "correspondance_code_caisse_code_orga_pro.csv";

   private FileConst() {
      // Cette classe ne peut être instanciée
   }

   public static final class Extension {

      private Extension() {
         // Cette classe ne peut être instanciée
      }

      public static final String PDF = ".pdf";

      public static final String BIN = ".bin";

   }

   public static final class Mime {

      private Mime() {
         // Cette classe ne peut être instanciée
      }

      public static final String PDF = "application/pdf";

      public static final String PDF_ID = "fmt/354";

      public static final String TIFF_ID = "fmt/353";

      public static final String DOC_ID = "doc";

      public static final String DOCX_ID = "docx";

      public static final Object RTF_ID = "id-rtf";

   }
}
