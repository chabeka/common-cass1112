package fr.urssaf.image.commons.itext.listener;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * Listener pour gerer le rendu des images.
 * Ce listener est utilise pour recuperer la taille des images.
 */
public class ImageRenderListener implements RenderListener {

   /**
    * Largeur en pixel.
    */
   private Float width = Float.valueOf(0);
   
   /**
    * Hauteur en pixel.
    */
   private Float height = Float.valueOf(0);
   
   /**
    * {@inheritDoc}
    */
   @Override
   public void beginTextBlock() {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void renderText(TextRenderInfo renderInfo) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void endTextBlock() {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void renderImage(ImageRenderInfo renderInfo) {
      // recupere dans la matrice la largeur et la hauteur
      width = renderInfo.getImageCTM().get(Matrix.I11);
      height = renderInfo.getImageCTM().get(Matrix.I22);
   }

   /**
    * Getter sur la largeur en pixel.
    * @return Float
    */
   public final Float getWidth() {
      return width;
   }
   
   /**
    * Getter sur la hauteur en pixel.
    * @return Float
    */
   public final Float getHeight() {
      return height;
   }
}
