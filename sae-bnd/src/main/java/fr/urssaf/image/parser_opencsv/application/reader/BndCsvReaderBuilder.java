package fr.urssaf.image.parser_opencsv.application.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Parser permettant de lire un fichier CSV en provenance de la BND pour écrire un
 * fichier sommaire.xml
 */
public class BndCsvReaderBuilder {

   private final static char SEPARATOR = '|';

   private FileInputStream fileInputStream;

   private InputStreamReader inputStreamReader;

   @Value("${bnd.source.path}")
   private String csvFilePath;

   private static final Logger LOGGER = LoggerFactory.getLogger(BndCsvReaderBuilder.class);


   /**
    * Configuration du parseur de fichier CSV
    * 
    * @param csvFilePath
    *           chemin du fichier CSV
    * @return le builder permettant de construire un parseur CSV
    * @throws IOException
    */
   public CSVReader getCsvBuilder(final String csvFileName) throws IOException {

      fileInputStream = new FileInputStream(csvFilePath + csvFileName);
      inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1);

      LOGGER.info("Encoding {} ", inputStreamReader.getEncoding());

      final CSVReaderBuilder builderCSV = new CSVReaderBuilder(inputStreamReader)
            .withKeepCarriageReturn(false)
            .withSkipLines(1);
      builderCSV.withCSVParser(
            new CSVParserBuilder()
            // .withQuoteChar(QUOTE_CHAR)
            .withSeparator(SEPARATOR)
            // .withStrictQuotes(true)
            .build());

      return builderCSV.build();
   }

   /**
    * @return the fileInputStream
    */
   public FileInputStream getFileInputStream() {
      return fileInputStream;
   }

   /**
    * @param fileInputStream
    *           the fileInputStream to set
    */
   public void setFileInputStream(final FileInputStream fileInputStream) {
      this.fileInputStream = fileInputStream;
   }

   /**
    * @return the inputStreamReader
    */
   public InputStreamReader getInputStreamReader() {
      return inputStreamReader;
   }

   /**
    * @param inputStreamReader
    *           the inputStreamReader to set
    */
   public void setInputStreamReader(final InputStreamReader inputStreamReader) {
      this.inputStreamReader = inputStreamReader;
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
