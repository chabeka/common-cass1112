package fr.urssaf.image.sae.commons.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;

import junit.framework.Assert;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StaxWriteUtilsTest {

   private static final String NAMESP = "http://www.cirtil.fr/sae/test";
   private static final String PREFIX = "";
   private File createdFile;

   @Before
   public void init() throws IOException {
      createdFile = File.createTempFile("generated", ".xml");
   }

   @After
   public void end() {
      FileUtils.deleteQuietly(createdFile);
   }

   @Test
   public void generateFileTest() throws XMLStreamException, IOException {

      OutputStream outputStream = null;
      XMLEventWriter writer = null;

      try {
         XMLEventFactory eventFactory = XMLEventFactory.newInstance();
         outputStream = new FileOutputStream(createdFile);

         writer = StaxWriteUtils.loadWriter(outputStream);

         StaxWriteUtils writeUtils = new StaxWriteUtils(eventFactory, writer);

         writeFile(writeUtils);

         String sha1Attendu = "9cfe23a6afc76e4b939f39cdc47a598378c3abac";
         String sha1Obtenu = calculeSha1(createdFile);

         Assert.assertEquals("le sha1 doit etre correct", sha1Attendu,
               sha1Obtenu);

      } finally {

         if (writer != null) {
            try {
               writer.close();

            } catch (XMLStreamException exception) {
               exception.printStackTrace();
            }
         }

         if (outputStream != null) {
            try {
               outputStream.close();

            } catch (IOException exception) {
               exception.printStackTrace();
            }
         }

      }
   }

   private void writeFile(StaxWriteUtils writeUtils) throws XMLStreamException {

      writeUtils.startDocument();
      writeUtils.addStartElement("stax", PREFIX, NAMESP);
      writeUtils.addDefaultPrefix(NAMESP);

      writeUtils.addStartTag("element", PREFIX, NAMESP);

      writeUtils.createTag("valeur", "valeurTagStax", PREFIX, NAMESP);

      writeUtils.addEndTag("element", PREFIX, NAMESP);
      writeUtils.addEndElement("stax", PREFIX, NAMESP);

   }

   private String calculeSha1(File file) throws IOException {

      FileInputStream fis = new FileInputStream(file);
      try {

         return DigestUtils.shaHex(fis);

      } finally {
         if (fis != null) {
            fis.close();
         }
      }

   }
}
