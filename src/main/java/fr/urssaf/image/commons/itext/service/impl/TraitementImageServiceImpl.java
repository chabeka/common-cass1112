package fr.urssaf.image.commons.itext.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

import fr.urssaf.image.commons.itext.exception.ExtractionException;
import fr.urssaf.image.commons.itext.listener.ImageRenderListener;
import fr.urssaf.image.commons.itext.service.TraitementImageService;
import fr.urssaf.image.commons.itext.utils.ImageUtils;

/**
 * Service implémentant l'interface {@link TraitementImageService}.
 */
@Service
public class TraitementImageServiceImpl implements TraitementImageService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TraitementImageServiceImpl.class);

   /**
    * Seuil pour considerer une page comme page en plein ecran.
    */
   private final Float SEUIL_FULL_SCREEN = Float.valueOf(0.75f);

   /**
    * {@inheritDoc}
    */
   @Override
   public void extractImages(final File fichier, final String repExtraction)
         throws ExtractionException {
      String trcPrefix = "extractImages(file)";
      LOGGER.debug("{} - début", trcPrefix);

      FileInputStream fileInputStream = null;
      try {
         fileInputStream = new FileInputStream(fichier);

         // extraction des images
         extratImagesFromPdf(fileInputStream, repExtraction);

      } catch (FileNotFoundException e) {
         throw new ExtractionException(e);
      } catch (RuntimeException e) {
         // catch les runtimes
         throw new ExtractionException(e);
      }

      LOGGER.debug("{} - fin", trcPrefix);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void extractImages(final byte[] contenuFichier,
         final String repExtraction) throws ExtractionException {
      String trcPrefix = "extractImages(byte[])";
      LOGGER.debug("{} - début", trcPrefix);

      try {
         ByteArrayInputStream inputStream = new ByteArrayInputStream(
               contenuFichier);

         // extraction des images
         extratImagesFromPdf(inputStream, repExtraction);

      } catch (RuntimeException e) {
         // catch les runtimes
         throw new ExtractionException(e);
      }

      LOGGER.debug("{} - fin", trcPrefix);
   }

   /**
    * Methode permettant de parcourir le fichier pdf afin d'extraire les images.
    * 
    * @param inputStream
    *           inputStream
    * @param repExtraction
    *           repertoire de sortie
    * @throws ExtractionException
    *            Exception levee lors d'une erreur d'extraction
    */
   private void extratImagesFromPdf(final InputStream inputStream,
         final String repExtraction) throws ExtractionException {

      String trcPrefix = "extratImagesFromPdf";
      LOGGER.debug("{} - début", trcPrefix);

      int numeroDePage = 0;
      try {
         PdfReader reader = new PdfReader(inputStream);

         // recupere le nombre total de page
         int nbTotalPage = reader.getNumberOfPages();

         // extraction des images
         int nbObjects = reader.getXrefSize();
         PdfObject object;
         PRStream stream;
         // Look for image and manipulate image stream
         for (int i = 0; i < nbObjects; i++) {
            object = reader.getPdfObject(i);
            if (object == null || !object.isStream())
               continue;
            stream = (PRStream) object;
            PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
            if (pdfsubtype != null
                  && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
               PdfImageObject image = new PdfImageObject(stream);

               BufferedImage bi = image.getBufferedImage();
               if (bi == null)
                  continue;

               int rotation = reader.getPageRotation(numeroDePage + 1);

               if (rotation != 0) {
                  // applique la rotation
                  LOGGER.debug("{} - Rotation de l'image {} - angle {}",
                        new Object[] { trcPrefix, numeroDePage + 1, rotation });
                  try {
                     bi = ImageUtils.rotate(bi, rotation);
                  } catch (Throwable ex) {
                     LOGGER.warn("{}", ex.getMessage());
                  }
               }

               LOGGER.debug("{} - Extraction de l'image {}", new Object[] {
                     trcPrefix, numeroDePage + 1 });
               extractOneImage(bi, numeroDePage + 1, nbTotalPage, repExtraction);

               numeroDePage++;
            }
         }

         inputStream.close();

      } catch (IOException e) {
         throw new ExtractionException(e.getCause());
      }

      LOGGER.debug("{} - fin", trcPrefix);
   }

   /**
    * Methode permettant d'extraire une image dans le repertoire de sortie.
    * 
    * @param image
    *           image a extraire
    * @param pageNumber
    *           numero de la page
    * @param nbTotalPage
    *           nombre total de page
    * @param repExtraction
    *           repertoire de sortie
    * @throws IOException
    *            Exception levée en cas d'erreur d'ecriture
    */
   private void extractOneImage(BufferedImage image, final int pageNumber,
         final int nbTotalPage, final String repExtraction) throws IOException {

      String path = "%s/%0" + Integer.toString(nbTotalPage).length() + "d.%s";
      String filename;

      if (image != null) {
         filename = String.format(path, repExtraction, pageNumber, "jpg");
         FileOutputStream outputStream = new FileOutputStream(new File(filename));
         ImageIO.write(image, "jpg", outputStream);
         outputStream.close();
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isFullImagePdf(final File fichier) {
      String trcPrefix = "isFullImagePdf(file)";
      LOGGER.debug("{} - début", trcPrefix);
      boolean isFullScreenImages = true;

      FileInputStream fileInputStream = null;
      try {
         fileInputStream = new FileInputStream(fichier);

         isFullScreenImages = isFullScreenImagesFromPdf(fileInputStream);

      } catch (IOException e) {
         LOGGER.error(e.getMessage());
         // le pdf ne peut pas être analysee
         isFullScreenImages = false;
      } catch (RuntimeException e) {
         // catch les runtimes
         LOGGER.error(e.getMessage());
         // le pdf ne peut pas être analysee
         isFullScreenImages = false;
      }

      LOGGER.debug("{} - fin (return {})", trcPrefix, isFullScreenImages);
      return isFullScreenImages;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isFullImagePdf(final byte[] contenuFichier) {
      String trcPrefix = "isFullImagePdf(byte[])";
      LOGGER.debug("{} - début", trcPrefix);
      boolean isFullScreenImages = true;

      try {
         ByteArrayInputStream inputStream = new ByteArrayInputStream(
               contenuFichier);

         isFullScreenImages = isFullScreenImagesFromPdf(inputStream);

      } catch (IOException e) {
         LOGGER.error(e.getMessage());
         // le pdf ne peut pas être analysee
         isFullScreenImages = false;
      } catch (RuntimeException e) {
         // catch les runtimes
         LOGGER.error(e.getMessage());
         // le pdf ne peut pas être analysee
         isFullScreenImages = false;
      }

      LOGGER.debug("{} - fin (return {})", trcPrefix, isFullScreenImages);
      return isFullScreenImages;
   }

   /**
    * Methode permettant de verifier que le pdf comporte que des images pleins
    * ecrans.
    * 
    * @param inputStream
    *           inputStream
    * @return boolean (vrai si toutes les images sont plein ecran)
    * @throws IOException
    *            Exception d'entree / sortie lors de la lecture du pdf
    */
   private boolean isFullScreenImagesFromPdf(final InputStream inputStream)
         throws IOException {

      String trcPrefix = "isFullScreenImagesFromPdf";
      LOGGER.debug("{} - début", trcPrefix);
      boolean isFullScreenImages = true;

      PdfReader reader = new PdfReader(inputStream);
      PdfReaderContentParser parser = new PdfReaderContentParser(reader);
      ImageRenderListener listener = new ImageRenderListener();

      // boucle sur l'ensemble des pages du pdf
      for (int pageNumber = 1; pageNumber <= reader.getNumberOfPages(); pageNumber++) {

         // recupere la page et regarde si la page n'a pas d'image
         PdfDictionary page = reader.getPageN(pageNumber);
         if (page.getAsDict(PdfName.RESOURCES) != null
               && page.getAsDict(PdfName.RESOURCES).getAsDict(PdfName.XOBJECT) != null) {

            // On a des resources sur la page
            PdfDictionary xObject = page.getAsDict(PdfName.RESOURCES)
                  .getAsDict(PdfName.XOBJECT);

            int compteurImage = 0;
            // On boucle sur les resources
            for (PdfName objectName : xObject.getKeys()) {
               PdfStream stream = xObject.getAsStream(objectName);
               if (stream.getAsName(PdfName.SUBTYPE).equals(PdfName.IMAGE)) {
                  compteurImage++;
               }
            }

            if (compteurImage != 1) {
               // la page ne contient pas de resources images, ou en contient
               // plus d'une
               // donc, on considere que l'image n'est pas fullscrean
               isFullScreenImages = false;
               LOGGER.debug("La page {} ne comporte pas une seule image ({})",
                     pageNumber, compteurImage);
               break;
            } else {
               // recupere la taille de la page
               Rectangle pageSize = reader.getPageSize(pageNumber);

               // effectue le rendu de l'image
               parser.processContent(pageNumber, listener);

               // on calcule le ratio en largeur et en hauteur
               float ratioX = listener.getWidth() / pageSize.getWidth();
               float ratioY = listener.getHeight() / pageSize.getHeight();

               if (ratioX < SEUIL_FULL_SCREEN.floatValue()
                     || ratioY < SEUIL_FULL_SCREEN.floatValue()) {
                  // l'image n'est pas plein ecran (pas 75% de la taille de la
                  // page)
                  isFullScreenImages = false;
                  LOGGER.debug(
                        "La page {} ne comporte pas une image trop petite ({}, {})",
                        new Object[] { pageNumber, ratioX, ratioY });
                  break;
               }
            }

         } else {
            // la page ne contient pas de resources, donc pas d'image
            isFullScreenImages = false;
            LOGGER.debug("La page {} ne comporte pas d'image", pageNumber);
            break;
         }
      }

      reader.close();

      inputStream.close();

      LOGGER.debug("{} - fin (return {})", trcPrefix, isFullScreenImages);
      return isFullScreenImages;
   }

}
