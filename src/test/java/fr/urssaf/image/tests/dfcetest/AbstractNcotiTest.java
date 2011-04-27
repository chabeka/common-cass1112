package fr.urssaf.image.tests.dfcetest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import net.docubase.rheatoolkit.RheaToolkit;
import net.docubase.rheatoolkit.RheaToolkitException;
import net.docubase.rheatoolkit.session.UserSession;
import net.docubase.rheatoolkit.session.UserSessionFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Code rajouté dans la DS4/Toolkit de manière temporaire</br>
 * 
 * @author lcella
 * 
 */
public abstract class AbstractNcotiTest {
   // Chaque classe fille à son logger
   protected Logger LOGGER = LoggerFactory.getLogger(getClass());

   /** Nom de la base GED pour ce test */
   protected static final String BASEID = "NCOTI";

   /** Login utilis� pour tout les tests */
   protected static final String ADM_LOGIN = "_ADMIN";

   /** Password */
   protected static final String ADM_PASSWORD = "DOCUBASE";

   @BeforeClass
   public static void initRhea() throws Exception {
      Properties parameters = new Properties();
      parameters.setProperty("service.access", "cer69-ds4int.cer69.recouv:4020");
      RheaToolkit.initialize(parameters);
   }

   @AfterClass
   public static void haltRhea() throws Exception {
      RheaToolkit.shutdown();
   }

   /**
    * G�n�re une session
    * 
    * @param login
    * @param pwd
    * @return
    * @throws RheaToolkitException
    */
   protected static UserSession getSession(String login, String pwd) throws RheaToolkitException {
      UserSession us = null;
      Locale[] list = new Locale[2];
      list[0] = Locale.FRENCH;
      list[1] = Locale.ENGLISH;

      File path = new File(System.getProperty("java.io.tmpdir") + "/" + login);
      try {
         us = UserSessionFactory.connect(login, pwd, list, path);
      } catch (RheaToolkitException e) {
         System.out.println(e.getMessage());
         throw e;
      }
      return us;
   }

   /**
    * Log console prenant en compte le thread
    * 
    * @param message
    */
   protected static void log(Object message) {
      System.out.println("[" + Thread.currentThread().getName() + "] " + message);
   }

   /*
    * On met ce code ici pour pouvoir g�n�rer des valeurs al�atoires aussi bien
    * dans les tests de stockage ou de recherche
    */
   static final String DIGITS = "0123456789";

   String randomNum(Random rand, int len) {
      StringBuilder sb = new StringBuilder(len);
      for (int i = 0; i < len; i++) {
         int index = rand.nextInt(DIGITS.length());
         sb.append(DIGITS.substring(index, index + 1));
      }
      return sb.toString();
   }

   String randomNum(int len) {
      return randomNum(rnd, len);
   }

   static Random rnd = new Random();

   /**
    * Contenu de notre document
    */
   protected static final byte[] DOC_CONTENT = "Ceci est un petit document".getBytes();

   /**
    * G�n�re un fichier "document" � la vol�e.
    * 
    * @return
    * @throws Exception
    */
   protected static File getAFile() throws Exception {
      File newDoc = File.createTempFile("doc" + System.nanoTime(), "txt");
      FileOutputStream fos = new FileOutputStream(newDoc);
      fos.write(DOC_CONTENT);
      fos.flush();
      fos.close();
      newDoc.deleteOnExit();
      return newDoc;
   }
}
