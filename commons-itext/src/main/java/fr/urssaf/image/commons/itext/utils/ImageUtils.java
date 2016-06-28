package fr.urssaf.image.commons.itext.utils;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;


/**
 * Classe utilitaire contenant des méthodes de retraitement des images.
 */
public final class ImageUtils {

   /**
    * Constructeur privee.
    */
   private ImageUtils() {
      super();
   }
   
   /**
    * Methode permettant de faire une rotation d'une image selon un angle.
    * 
    * (found at: http://flyingdogz.wordpress.com/2008/02/11/image-rotate-in-java-2-easier-to-use/)
    * 
    * @param image
    *           image a tourner
    * @param angle
    *           angle en degree
    * @return BufferedImage
    */
   public static BufferedImage rotate(BufferedImage image, float angle) {
      float radianAngle = (float) Math.toRadians(angle);

      float sin = (float) Math.abs(Math.sin(radianAngle));
      float cos = (float) Math.abs(Math.cos(radianAngle));

      int w = image.getWidth();
      int h = image.getHeight();

      int neww = (int) Math.round(w * cos + h * sin);
      int newh = (int) Math.round(h * cos + w * sin);

      BufferedImage result = createNewImage(image, neww, newh);
      Graphics2D g = result.createGraphics();

      // -----------------------MODIFIED--------------------------------------
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
      // ---------------------------------------------------------------------

      // -----------------------MODIFIED, BUT NOT CRUCIAL---------------------
      AffineTransform at = AffineTransform.getTranslateInstance((neww - w) / 2,
            (newh - h) / 2);
      at.rotate(radianAngle, w / 2, h / 2);
      // ---------------------------------------------------------------------

      g.drawRenderedImage(image, at);
      g.dispose();

      return result;
   }
   
   /**
    * Methode permettant de creer une nouvelle image en fonction de l'image
    * initial.
    * 
    * @param image
    *           image d'origine
    * @param width
    *           largeur de l'image a creer
    * @param height
    *           hauteur de l'image a creer
    * @return BufferedImage
    */
   private static BufferedImage createNewImage(BufferedImage image, int width,
         int height) {
      BufferedImage result;
      if (image.getType() == BufferedImage.TYPE_BYTE_BINARY
            || image.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
         result = new BufferedImage(width, height, image.getType(),
               (IndexColorModel) image.getColorModel());
      } else if (image.getType() == BufferedImage.TYPE_BYTE_GRAY
            || image.getType() == BufferedImage.TYPE_USHORT_GRAY) {
         result = new BufferedImage(width, height, image.getType());
      } else {
         // -----------------------MODIFIED--------------------------------------
         GraphicsEnvironment ge = GraphicsEnvironment
               .getLocalGraphicsEnvironment();
         if (!ge.isHeadless()) {
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            // ---------------------------------------------------------------------
   
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            
            result = gc.createCompatibleImage(width, height,
                  image.getTransparency());
         } else {
            result = new BufferedImage(width, height, image.getType());
         }
      }
      return result;
   }
}
