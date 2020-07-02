package fr.urssaf.image.parser_opencsv.application.writer.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

/**
 * Classe permettant d'écrire le fichier CSV contenant les lignes en erreur
 */
public class CSVErrorWriter {

   private CSVWriter writer;

   private FileWriter fileWriter;

   private String sourceDirectory;

   private String fileName;

   private final static char SEPARATOR = '|';

   public CSVErrorWriter(final String directory) {
      this();
      sourceDirectory = directory;
   }

   public CSVErrorWriter() {
      super();
   }

   /**
    * Ouverture du flux pour l'écriture des lignes du CSV
    * 
    * @throws IOException
    */
   public void openErrorfile() throws IOException {
      final File fileError = File.createTempFile("bnd_error_", ".csv", new File(sourceDirectory));
      fileName = fileError.getAbsoluteFile().getName();
      fileWriter = new FileWriter(fileError.getAbsoluteFile());
      writer = new CSVWriter(
                             fileWriter,
                             SEPARATOR,
                             CSVWriter.NO_QUOTE_CHARACTER);
   }

   /**
    * Ouverture du flux du fichier avec spécification de repertoire où sera créé le fichier d'erreur
    * 
    * @param sourceDirectory
    * @throws IOException
    */
   public void openErrorfile(final String sourceDirectory) throws IOException {
      final File fileError = File.createTempFile("bnd_error_", ".csv", new File(sourceDirectory));
      fileName = fileError.getAbsoluteFile().getName();
      fileWriter = new FileWriter(fileError.getAbsoluteFile());
      writer = new CSVWriter(fileWriter, SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
   }

   /**
    * Ecrire une ligne dans le fichier CSV
    * 
    * @param nextLine
    */
   public void write(final String[] nextLine) {
      writer.writeNext(nextLine);
   }

   /**
    * @return the pathFile
    */
   public String getSourceDirectory() {
      return sourceDirectory;
   }

   /**
    * Ferme tous les flux
    * 
    * @throws IOException
    */
   public void close() throws IOException {

      if (fileWriter != null) {
         fileWriter.close();
      }

   }

   /**
    * @return the filePath
    */
   public String getFileName() {
      return fileName;
   }

   /**
    * @param filePath the filePath to set
    */
   public void setFileName(final String filePath) {
      fileName = filePath;
   }
}
