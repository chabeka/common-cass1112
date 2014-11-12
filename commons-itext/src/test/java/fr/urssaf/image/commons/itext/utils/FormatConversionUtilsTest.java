package fr.urssaf.image.commons.itext.utils;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import fr.urssaf.image.commons.itext.exception.FormatConversionParametrageException;
import fr.urssaf.image.commons.itext.model.FormatConversionParametres;

@RunWith(BlockJUnit4ClassRunner.class)
public class FormatConversionUtilsTest {

   @Test(expected=FormatConversionParametrageException.class)
   public void numeroPageDebutNegatif() throws FormatConversionParametrageException {
      FormatConversionUtils.getParametresConversion(Integer.valueOf(-2), null, 20);
   }
   
   @Test(expected=FormatConversionParametrageException.class)
   public void numeroPageDebutTropGrand() throws FormatConversionParametrageException {
      FormatConversionUtils.getParametresConversion(Integer.valueOf(24), null, 20);
   }
   
   @Test(expected=FormatConversionParametrageException.class)
   public void nombrePageZero() throws FormatConversionParametrageException {
      FormatConversionUtils.getParametresConversion(Integer.valueOf(3), Integer.valueOf(0), 20);
   }
   
   @Test
   public void testSuccess() throws FormatConversionParametrageException {
      
      // Cas de test 1 : 10 pages a partir de la page 3
      FormatConversionParametres param = FormatConversionUtils.getParametresConversion(Integer.valueOf(3), Integer.valueOf(10), 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 3", 3, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 12", 12, param.getNumeroPageFin());
      
      // Cas de test 2 : -3 pages a partir de la page 10
      param = FormatConversionUtils.getParametresConversion(Integer.valueOf(10), Integer.valueOf(-3), 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 8", 8, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 10", 10, param.getNumeroPageFin());
      
      // Cas de test 3 : toutes les pages a partir de la page 2
      param = FormatConversionUtils.getParametresConversion(Integer.valueOf(2), null, 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 2", 2, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 20", 20, param.getNumeroPageFin());
      
      // Cas de test 4 : les 10 premieres pages
      param = FormatConversionUtils.getParametresConversion(null, Integer.valueOf(10), 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 1", 1, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 10", 10, param.getNumeroPageFin());
      
      // Cas de test 5 : les 10 dernieres pages
      param = FormatConversionUtils.getParametresConversion(null, Integer.valueOf(-10), 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 10", 10, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 20", 20, param.getNumeroPageFin());
      
      // Cas de test 6 : toutes les pages
      param = FormatConversionUtils.getParametresConversion(null, null, 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 1", 1, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 20", 20, param.getNumeroPageFin());
      
      // Cas de test 7 : 25 pages a partir de la page 3
      param = FormatConversionUtils.getParametresConversion(Integer.valueOf(3), Integer.valueOf(25), 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 3", 3, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 20", 20, param.getNumeroPageFin());
      
      // Cas de test 8 : -10 pages a partir de la page 2
      param = FormatConversionUtils.getParametresConversion(Integer.valueOf(2), Integer.valueOf(-10), 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 1", 1, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 2", 2, param.getNumeroPageFin());
      
      // Cas de test 9 : les 25 premieres pages
      param = FormatConversionUtils.getParametresConversion(null, Integer.valueOf(25), 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 1", 1, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 20", 20, param.getNumeroPageFin());
      
      // Cas de test 10 : les 25 dernieres pages
      param = FormatConversionUtils.getParametresConversion(null, Integer.valueOf(-25), 20);
      Assert.assertEquals("Le numero de page de debut devrait etre 1", 1, param.getNumeroPageDebut());
      Assert.assertEquals("Le numero de page de debut devrait etre 20", 20, param.getNumeroPageFin());
      
   }
}
