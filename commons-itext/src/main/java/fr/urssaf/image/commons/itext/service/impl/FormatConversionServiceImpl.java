package fr.urssaf.image.commons.itext.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;

import fr.urssaf.image.commons.itext.exception.FormatConversionException;
import fr.urssaf.image.commons.itext.exception.FormatConversionParametrageException;
import fr.urssaf.image.commons.itext.exception.FormatConversionRuntimeException;
import fr.urssaf.image.commons.itext.model.FormatConversionParametres;
import fr.urssaf.image.commons.itext.service.FormatConversionService;
import fr.urssaf.image.commons.itext.utils.FormatConversionUtils;

/**
 * Service permettant de réaliser des conversions de fichiers.
 */
@Service
public class FormatConversionServiceImpl implements FormatConversionService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(FormatConversionServiceImpl.class);

   /**
    * {@inheritDoc}
    */
   public final byte[] conversionTiffToPdf(final File fichier,
         final Integer numeroPage, final Integer nombrePages)
         throws FormatConversionException, FormatConversionParametrageException {
      InputStream stream;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
         LOGGER.debug("Conversion d'un Tiff en Pdf a partir d'un fichier {}",
               new String[] { fichier.getPath() });
         stream = new FileInputStream(fichier);
         convertir(numeroPage, nombrePages, stream, outputStream);
         stream.close();
      } catch (FileNotFoundException e) {
         throw new FormatConversionException(e);
      } catch (IOException e) {
         throw new FormatConversionException(e);
      } catch (DocumentException e) {
         throw new FormatConversionException(e);
      } catch (RuntimeException e) { 
         throw new FormatConversionRuntimeException(e);
      }

      return outputStream.toByteArray();
   }

   /**
    * {@inheritDoc}
    */
   public final byte[] conversionTiffToPdf(byte[] fichier, Integer numeroPage,
         Integer nombrePages) throws FormatConversionException,
         FormatConversionParametrageException {
      InputStream stream;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
         LOGGER
               .debug("Conversion d'un Tiff en Pdf a partir d'un tableau de byte");
         stream = new ByteArrayInputStream(fichier);
         convertir(numeroPage, nombrePages, stream, outputStream);
         stream.close();
      } catch (IOException e) {
         throw new FormatConversionException(e);
      } catch (DocumentException e) {
         throw new FormatConversionException(e);
      } catch (RuntimeException e) { 
         throw new FormatConversionRuntimeException(e);
      }

      return outputStream.toByteArray();
   }
   
   /**
    * {@inheritDoc}
    */
   public final byte[] splitPdf(final File fichier,
         final Integer numeroPage, final Integer nombrePages)
         throws FormatConversionException, FormatConversionParametrageException {
      InputStream stream;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
         LOGGER.debug("Split d'un Pdf a partir d'un fichier {}",
               new String[] { fichier.getPath() });
         stream = new FileInputStream(fichier);
         split(numeroPage, nombrePages, stream, outputStream);
         stream.close();
      } catch (FileNotFoundException e) {
         throw new FormatConversionException(e);
      } catch (IOException e) {
         throw new FormatConversionException(e);
      } catch (DocumentException e) {
         throw new FormatConversionException(e);
      } catch (RuntimeException e) { 
         throw new FormatConversionRuntimeException(e);
      }

      return outputStream.toByteArray();
   }

   /**
    * {@inheritDoc}
    */
   public final byte[] splitPdf(byte[] fichier, Integer numeroPage,
         Integer nombrePages) throws FormatConversionException,
         FormatConversionParametrageException {
      InputStream stream;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
         LOGGER
               .debug("Split d'un Pdf a partir d'un tableau de byte");
         stream = new ByteArrayInputStream(fichier);
         split(numeroPage, nombrePages, stream, outputStream);
         stream.close();
      } catch (IOException e) {
         throw new FormatConversionException(e);
      } catch (DocumentException e) {
         throw new FormatConversionException(e);
      } catch (RuntimeException e) { 
         throw new FormatConversionRuntimeException(e);
      }

      return outputStream.toByteArray();
   }

   /**
    * Methode de convertion
    * 
    * @param numeroPage
    *           numero de page demande
    * @param nombrePages
    *           nombre de page demandes
    * @param stream
    *           document a convertir
    * @param outputStream
    *           document converti
    * @throws IOException
    *            exception d'entrees / sorties
    * @throws FormatConversionParametrageException
    *            exception de parametrages
    * @throws DocumentException
    *            exception de conversion itext
    */
   private void convertir(final Integer numeroPage, final Integer nombrePages,
         final InputStream stream, final ByteArrayOutputStream outputStream)
         throws IOException, FormatConversionParametrageException,
         DocumentException {
      RandomAccessFileOrArray randomAccessFile;
      randomAccessFile = new RandomAccessFileOrArray(stream);

      LOGGER.debug("Numero de page demande : {}, Nombre de page demande : {}",
            new Integer[] { numeroPage, nombrePages });

      // recupere le nombre de pages total
      final int nbPagesTotal = TiffImage.getNumberOfPages(randomAccessFile);
      LOGGER.debug("Nombre total de pages du Tiff : {}", nbPagesTotal);

      // calcule les parametres de conversion
      final FormatConversionParametres parametres = FormatConversionUtils
            .getParametresConversion(numeroPage, nombrePages, nbPagesTotal);

      LOGGER.debug("Numero de page de debut : {}, Numero de page de fin : {}",
            new Integer[] { parametres.getNumeroPageDebut(),
                  parametres.getNumeroPageFin() });

      Document document = new Document();
      PdfWriter.getInstance(document, outputStream);
      document.open();

      // effectue la conversion
      Image image;
      for (int index = parametres.getNumeroPageDebut(); index <= parametres
            .getNumeroPageFin(); index++) {
         // recupere l'image de la page en cours
         image = TiffImage.getTiffImage(randomAccessFile, index);
         // reinitialise la position de l'image
         image.setAbsolutePosition(0, 0);
         // specifie la taille de la page
         Rectangle pageSize = new Rectangle(image.getWidth(), image.getHeight());
         document.setPageSize(pageSize);
         // ajout de l'image au document
         document.newPage();
         document.add(image);
      }
      document.close();
   }
   
   /**
    * Methode de split d'un Pdf
    * 
    * @param numeroPage
    *           numero de page demande
    * @param nombrePages
    *           nombre de page demandes
    * @param stream
    *           document a convertir
    * @param outputStream
    *           document converti
    * @throws IOException
    *            exception d'entrees / sorties
    * @throws FormatConversionParametrageException
    *            exception de parametrages
    * @throws DocumentException
    *            exception de conversion itext
    */
   private void split(final Integer numeroPage, final Integer nombrePages,
         final InputStream stream, final ByteArrayOutputStream outputStream)
         throws IOException, FormatConversionParametrageException,
         DocumentException {
      
      LOGGER.debug("Numero de page demande : {}, Nombre de page demande : {}",
            new Integer[] { numeroPage, nombrePages });
      
      if (numeroPage != null || nombrePages != null) {
         PdfReader reader = new PdfReader(stream);
         
         // recupere le nombre de pages total
         final int nbPagesTotal = reader.getNumberOfPages();
         LOGGER.debug("Nombre total de pages du Pdf : {}", nbPagesTotal);
   
         // calcule les parametres de conversion
         final FormatConversionParametres parametres = FormatConversionUtils
               .getParametresConversion(numeroPage, nombrePages, nbPagesTotal);
   
         LOGGER.debug("Numero de page de debut : {}, Numero de page de fin : {}",
               new Integer[] { parametres.getNumeroPageDebut(),
                     parametres.getNumeroPageFin() });
   
         Document document = new Document();
         PdfCopy writer = new PdfCopy(document, outputStream);
         document.open();
   
         // effectue le split
         for (int index = parametres.getNumeroPageDebut(); index <= parametres
               .getNumeroPageFin(); index++) {
            // recupere la page en cours
            PdfImportedPage page = writer.getImportedPage(reader, index);
            // ajout de l'image au document
            writer.addPage(page);
         }
         document.close();
         writer.close();
      } else {
         LOGGER.debug("Document complet -> pas de split");
         IOUtils.copy(stream, outputStream);
      }
   }
}
