package fr.urssaf.image.commons.itext.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class ImageUtilsTest {
   
   @Test
   public void rotate_image_binary() throws IOException {
      
      File img = new File("src/test/resources/images/image-binary.png");
      
      BufferedImage in = ImageIO.read(img);
      Assert.assertEquals("L'image en entree n'a pas le bon type", in.getType(), BufferedImage.TYPE_BYTE_BINARY);
      
      BufferedImage out = ImageUtils.rotate(in, 90);
      Assert.assertEquals("L'image en sortie n'a pas le bon type", in.getType(), BufferedImage.TYPE_BYTE_BINARY);
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getWidth(), out.getHeight());
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getHeight(), out.getWidth());
      
      // decommenter pour controle visuel
      //ImageIO.write(out, "png", new File("src/test/resources/images/image-binary-rotate.png"));
   }
   
   @Test
   public void rotate_image_indexee() throws IOException {
      
      File img = new File("src/test/resources/images/image-indexee.png");
      
      BufferedImage in = ImageIO.read(img);
      Assert.assertEquals("L'image en entree n'a pas le bon type", in.getType(), BufferedImage.TYPE_BYTE_INDEXED);
      
      BufferedImage out = ImageUtils.rotate(in, 90);
      Assert.assertEquals("L'image en sortie n'a pas le bon type", in.getType(), BufferedImage.TYPE_BYTE_INDEXED);
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getWidth(), out.getHeight());
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getHeight(), out.getWidth());
      
      // decommenter pour controle visuel
      //ImageIO.write(out, "png", new File("src/test/resources/images/image-indexee-rotate.png"));
   }
   
   @Test
   public void rotate_image_gray() throws IOException {
      
      File img = new File("src/test/resources/images/image-gray.png");
      
      BufferedImage in = ImageIO.read(img);
      Assert.assertEquals("L'image en entree n'a pas le bon type", in.getType(), BufferedImage.TYPE_BYTE_GRAY);
      
      BufferedImage out = ImageUtils.rotate(in, 90);
      Assert.assertEquals("L'image en sortie n'a pas le bon type", in.getType(), BufferedImage.TYPE_BYTE_GRAY);
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getWidth(), out.getHeight());
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getHeight(), out.getWidth());
      
      // decommenter pour controle visuel
      //ImageIO.write(out, "png", new File("src/test/resources/images/image-gray-rotate.png"));
   }
   
   @Test
   public void rotate_image_ushort_gray() throws IOException {
      
      File img = new File("src/test/resources/images/image-ushort-gray.png");
      
      BufferedImage in = ImageIO.read(img);
      Assert.assertEquals("L'image en entree n'a pas le bon type", in.getType(), BufferedImage.TYPE_USHORT_GRAY);
      
      BufferedImage out = ImageUtils.rotate(in, 90);
      Assert.assertEquals("L'image en sortie n'a pas le bon type", in.getType(), BufferedImage.TYPE_USHORT_GRAY);
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getWidth(), out.getHeight());
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getHeight(), out.getWidth());
      
      // decommenter pour controle visuel
      //ImageIO.write(out, "png", new File("src/test/resources/images/image-ushort-gray-rotate.png"));
   }
   
   @Test
   public void rotate_image_bgr() throws IOException {
      
      File img = new File("src/test/resources/images/image-3byte-bgr.jpg");
      
      BufferedImage in = ImageIO.read(img);
      Assert.assertEquals("L'image en entree n'a pas le bon type", in.getType(), BufferedImage.TYPE_3BYTE_BGR);
      
      BufferedImage out = ImageUtils.rotate(in, 90);
      Assert.assertEquals("L'image en sortie n'a pas le bon type", in.getType(), BufferedImage.TYPE_3BYTE_BGR);
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getWidth(), out.getHeight());
      Assert.assertEquals("La rotation ne semble pas avoir marchee", in.getHeight(), out.getWidth());
      
      // decommenter pour controle visuel
      //ImageIO.write(out, "jpg", new File("src/test/resources/images/image-3byte-bgr-rotate.jpg"));
   }
}
