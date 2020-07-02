package fr.urssaf.image.parser_opencsv.application.reader.correspondance;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Parser permettant de lire un fichier CSV en provenance de la BND
 */
public class CorrespondanceReaderBuilder {

   private static final char SEPARATOR = ';';

   private FileInputStream fileInputStream;

   private InputStreamReader inputStreamReader;

   private final String filePath;

   private static final Logger LOGGER = LoggerFactory.getLogger(CorrespondanceReaderBuilder.class);

   public CorrespondanceReaderBuilder(final String filePath) {
      this.filePath = filePath;
   }

   /**
    * Configuration du parseur de fichier CSV de correspondance pour les code caisse SSTI
    * 
    * @param csvFilePath
    *           chemin du fichier CSV
    * @return le builder permettant de construire un parseur CSV
    * @throws IOException
    */
   public CSVReader getCsvBuilder() throws IOException {

      fileInputStream = new FileInputStream(filePath);
      inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

      LOGGER.info("Encoding {} ", inputStreamReader.getEncoding());

      final CSVReaderBuilder builderCSV = new CSVReaderBuilder(inputStreamReader)
            .withKeepCarriageReturn(false)
            .withSkipLines(1);
      builderCSV.withCSVParser(
                               new CSVParserBuilder()
                               .withSeparator(SEPARATOR)
                               .build());

      return builderCSV.build();
   }

   public void closeStream() throws IOException {
      if (fileInputStream != null) {
         fileInputStream.close();
      }
      if (inputStreamReader != null) {
         inputStreamReader.close();
      }
   }

}
