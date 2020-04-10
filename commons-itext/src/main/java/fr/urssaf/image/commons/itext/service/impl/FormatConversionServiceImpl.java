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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;

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
   @Override
   public final byte[] conversionTiffToPdf(final File fichier,
                                           final Integer numeroPage, final Integer nombrePages)
         throws FormatConversionException, FormatConversionParametrageException {
      InputStream stream;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
         LOGGER.debug("Conversion d'un Tiff en Pdf a partir d'un fichier {}",
                      new String[] {fichier.getPath()});
         stream = new FileInputStream(fichier);
         convertir(numeroPage, nombrePages, stream, outputStream);
         stream.close();
      }
      catch (final FileNotFoundException e) {
         throw new FormatConversionException(e);
      }
      catch (final IOException e) {
         throw new FormatConversionException(e);
      }
      catch (final DocumentException e) {
         throw new FormatConversionException(e);
      }
      catch (final RuntimeException e) {
         throw new FormatConversionRuntimeException(e);
      }

      return outputStream.toByteArray();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final byte[] conversionTiffToPdf(final byte[] fichier, final Integer numeroPage,
                                           final Integer nombrePages)
         throws FormatConversionException,
         FormatConversionParametrageException {
      InputStream stream;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
         LOGGER
               .debug("Conversion d'un Tiff en Pdf a partir d'un tableau de byte");
         stream = new ByteArrayInputStream(fichier);
         convertir(numeroPage, nombrePages, stream, outputStream);
         stream.close();
      }
      catch (final IOException e) {
         throw new FormatConversionException(e);
      }
      catch (final DocumentException e) {
         throw new FormatConversionException(e);
      }
      catch (final RuntimeException e) {
         throw new FormatConversionRuntimeException(e);
      }

      return outputStream.toByteArray();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final byte[] splitPdf(final File fichier,
                                final Integer numeroPage, final Integer nombrePages)
         throws FormatConversionException, FormatConversionParametrageException {
      InputStream stream;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
         LOGGER.debug("Split d'un Pdf a partir d'un fichier {}",
                      new String[] {fichier.getPath()});
         stream = new FileInputStream(fichier);
         split(numeroPage, nombrePages, stream, outputStream);
         stream.close();
      }
      catch (final FileNotFoundException e) {
         throw new FormatConversionException(e);
      }
      catch (final IOException e) {
         throw new FormatConversionException(e);
      }
      catch (final DocumentException e) {
         throw new FormatConversionException(e);
      }
      catch (final RuntimeException e) {
         throw new FormatConversionRuntimeException(e);
      }

      return outputStream.toByteArray();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final byte[] splitPdf(final byte[] fichier, final Integer numeroPage,
                                final Integer nombrePages)
         throws FormatConversionException,
         FormatConversionParametrageException {
      InputStream stream;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
         LOGGER
               .debug("Split d'un Pdf a partir d'un tableau de byte");
         stream = new ByteArrayInputStream(fichier);
         split(numeroPage, nombrePages, stream, outputStream);
         stream.close();
      }
      catch (final IOException e) {
         throw new FormatConversionException(e);
      }
      catch (final DocumentException e) {
         throw new FormatConversionException(e);
      }
      catch (final RuntimeException e) {
         throw new FormatConversionRuntimeException(e);
      }

      return outputStream.toByteArray();
   }

   /**
    * Méthode de conversion
    * 
    * @param numeroPage
    *           numéro de page demande
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
                   new Integer[] {numeroPage, nombrePages});

      // récupère le nombre de pages total
      final int nbPagesTotal = TiffImage.getNumberOfPages(randomAccessFile);
      LOGGER.debug("Nombre total de pages du Tiff : {}", nbPagesTotal);

      // calcule les paramètres de conversion
      final FormatConversionParametres parametres = FormatConversionUtils
                                                                         .getParametresConversion(numeroPage, nombrePages, nbPagesTotal);

      LOGGER.debug("Numero de page de debut : {}, Numero de page de fin : {}",
                   new Integer[] {parametres.getNumeroPageDebut(),
                                  parametres.getNumeroPageFin()});

      final Document document = new Document();
      PdfWriter.getInstance(document, outputStream);
      document.open();

      // effectue la conversion
      Image image;
      for (int index = parametres.getNumeroPageDebut(); index <= parametres.getNumeroPageFin(); index++) {
         // récupère l'image de la page en cours
         final boolean recoverFromImageErreur = true;
         image = TiffImage.getTiffImage(randomAccessFile, recoverFromImageErreur, index);
         // réinitialise la position de l'image
         image.setAbsolutePosition(0, 0);
         // spécifie la taille de la page
         final Rectangle pageSize = new Rectangle(image.getWidth(), image.getHeight());
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
                   new Integer[] {numeroPage, nombrePages});

      if (numeroPage != null || nombrePages != null) {
         final PdfReader reader = new PdfReader(stream);
         //Anomalie #459904: 
         //https://stackoverflow.com/questions/17691013/pdfreader-not-opened-with-owner-password-error-in-itext
         PdfReader.unethicalreading = true;

         // recupere le nombre de pages total
         final int nbPagesTotal = reader.getNumberOfPages();
         LOGGER.debug("Nombre total de pages du Pdf : {}", nbPagesTotal);

         // calcule les parametres de conversion
         final FormatConversionParametres parametres = FormatConversionUtils
                                                                            .getParametresConversion(numeroPage, nombrePages, nbPagesTotal);

         LOGGER.debug("Numero de page de debut : {}, Numero de page de fin : {}",
                      new Integer[] {parametres.getNumeroPageDebut(),
                                     parametres.getNumeroPageFin()});

         final Document document = new Document();
         final PdfCopy writer = new PdfCopy(document, outputStream);
         document.open();

         // effectue le split
         for (int index = parametres.getNumeroPageDebut(); index <= parametres
                                                                              .getNumeroPageFin(); index++) {
            // recupere la page en cours
            final PdfImportedPage page = writer.getImportedPage(reader, index);
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
