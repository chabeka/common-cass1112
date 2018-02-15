package fr.urssaf.image.commons.spring.batch.support;

import java.io.File;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.commons.spring.batch.support.stax.BibliothequeXMLSupport;

public final class BibliothequeXMLTest {

   private BibliothequeXMLSupport support;

   @Before
   public void before() {

      support = new BibliothequeXMLSupport();
   }

   private int countItems;

   @Test
   public void createBibliothequeFileXML() {

      Assert.assertTrue("'countItems' doit être renseigné", countItems > 0);

      File output = new File(SystemUtils.getJavaIoTmpDir(),
            "batch-exemple/bibliotheque_" + countItems + ".xml");

      FileUtils.deleteQuietly(output);

      support.writer(output, countItems);
   }

   @Test
   public void copyBibliothequeFileXML() {

      Assert.assertTrue("'countItems' doit être renseigné", countItems > 0);

      File input = new File(SystemUtils.getJavaIoTmpDir(),
            "batch-exemple/bibliotheque_" + countItems + ".xml");

      File output = new File(SystemUtils.getJavaIoTmpDir(),
            "batch-exemple/out/bibliotheque_" + countItems + ".xml");

      FileUtils.deleteQuietly(output);

      support.writer(output, input);

   }

}
