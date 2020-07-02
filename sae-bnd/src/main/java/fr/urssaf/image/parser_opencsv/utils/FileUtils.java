package fr.urssaf.image.parser_opencsv.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;

import fr.urssaf.image.parser_opencsv.application.exception.BNDScriptRuntimeException;
import fr.urssaf.image.parser_opencsv.application.exception.CountNbrePageFileException;

public class FileUtils {

   private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

   private FileUtils() {

   }

   public static final String getHash(final String filePath) throws IOException {
      final File f = new File(filePath);
      final InputStream contenu = new FileInputStream(f);

      final String hash = DigestUtils.sha1Hex(contenu);

      return hash;
   }

   /**
    * Compte le nombre de pages dans un fichier PDF
    * 
    * @param filename
    * @return
    * @throws CountNbrePageFileException
    * @throws IOException
    */
   public static int countNbPagesPDF(final String filename) throws CountNbrePageFileException {
      int count = 0;
      PDDocument doc;
      try {
         doc = PDDocument.load(new File(filename));
         count = doc.getNumberOfPages();
      }
      catch (final IOException e) {
         throw new CountNbrePageFileException(e);
      }

      return count;
   }

   /**
    * Compte le nombre de page dans un fichier TIFF
    * 
    * @param filename
    * @return
    * @throws CountNbrePageFileException
    */
   public static int countNbPagesTIFF(final String filename) throws CountNbrePageFileException {

      final File file = new File(filename);
      int count = 0;

      try (SeekableStream s = new FileSeekableStream(file)) {
         final TIFFDecodeParam param = null;
         final ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
         count = dec.getNumPages();
      }
      catch (final IOException e) {
         throw new CountNbrePageFileException(e);
      }
      return count;
   }

   /**
    * Compte le nombre de page dans un fichier DOC
    * 
    * @param filename
    * @return
    * @throws CountNbrePageFileException
    */
   public static int countNbPagesDoc(final String filename) throws CountNbrePageFileException {
      int count = 0;
      try (HWPFDocument wordDoc = new HWPFDocument(new FileInputStream(filename))) {
         count = wordDoc.getSummaryInformation().getPageCount();
      }
      catch (final IOException e) {
         throw new CountNbrePageFileException(e);
      }

      return count;
   }

   /**
    * Compte le nombre de page dans un fichier DOCX
    * 
    * @param filename
    * @return
    * @throws CountNbrePageFileException
    */
   public static final int countNbPagesDocx(final String absolutePath) throws CountNbrePageFileException {
      int count = 0;
      try (XWPFDocument docx = new XWPFDocument(new FileInputStream(absolutePath))) {
         count = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
      }
      catch (final IOException e) {
         throw new CountNbrePageFileException(e);
      }

      return count;
   }

   /**
    * Calcul du nombre de pages d'un fichier RTF
    * 
    * @param absolutePath
    * @return
    * @throws CountNbrePageFileException
    */
   public static final int countNbPagesRTF(final String absolutePath) throws CountNbrePageFileException {
      int count = 0;
      final ByteArrayOutputStream bo = new ByteArrayOutputStream();

      final File converterFolder = new File("converter");
      final IConverter converter = LocalConverter.builder()
            .baseFolder(converterFolder)
            .workerPool(20, 25, 2, TimeUnit.SECONDS)
            .processTimeout(5, TimeUnit.SECONDS)
            .build();

      File destination = null;
      try {
         destination = File.createTempFile("rtf_to_pdf_", ".pdf", converterFolder);
      }
      catch (final IOException e) {
         throw new BNDScriptRuntimeException("Impossible de créer le fichier temporaire pour la conversion du RTF en PDF", e);
      }

      try (
            final InputStream in = new BufferedInputStream(new FileInputStream(new File(absolutePath)));
            final OutputStream outputStream = new FileOutputStream(destination);) {

         LOGGER.info("Debut de la conversion du fichier RTF en PDF");
         final Future<Boolean> conversion = converter
               .convert(in)
               .as(DocumentType.RTF)
               .to(bo)
               .as(DocumentType.PDF)
               .prioritizeWith(1000)
               .schedule();
         final boolean finish = conversion.get();
         if (finish) {
            LOGGER.info("La conversion du RTF {} en PDF s'est bien passé", absolutePath);
         } else {
            LOGGER.error("La conversion du RTF {} en PDF a échouée", absolutePath);
         }

         bo.writeTo(outputStream);
         LOGGER.info("Fin d'écriture du PDF");
         LOGGER.info("Calcul du nombre de pages du PDF obtenue à base du RTF");
         count = countNbPagesPDF(destination.getAbsolutePath());
      }
      catch (final IOException | ExecutionException e) {
         throw new CountNbrePageFileException(e);
      }
      catch (final InterruptedException e) {
         LOGGER.error("Une erreur est survenue lors de la conversion du RTF en PDF. Details : {}", e.getMessage());
         Thread.currentThread().interrupt();
      }
      finally {
         converter.shutDown();
         if (destination.delete()) {
            LOGGER.info("Suppression du fichier PDF temporaire de conversion");
         }
      }

      return count;
   }
}
