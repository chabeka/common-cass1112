package fr.urssaf.image.tests.dfcetest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.docubase.rheatoolkit.RheaToolkit;
import net.docubase.rheatoolkit.RheaToolkitException;
import net.docubase.rheatoolkit.base.BaseIndex;
import net.docubase.rheatoolkit.domain.DomainFactory;
import net.docubase.rheatoolkit.session.UserSession;
import net.docubase.rheatoolkit.session.UserSessionFactory;
import net.docubase.rheatoolkit.session.base.Document;
import net.docubase.rheatoolkit.session.base.DocumentStore;
import net.docubase.rheatoolkit.session.base.DocumentTag;
import net.docubase.rheatoolkit.session.base.UserBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DocubaseHelper {
   public static final String NUMERIC = "0123456789";
   public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
   public static final String ALPHA_NUM = ALPHA + NUMERIC;
   private static final byte[] DOC_CONTENT = "Ceci est un petit document!".getBytes();
   private static final String[] CAT_NAMES = Categories.getNames(); // alias

   private static Random rand = new Random();
   private static final Logger LOGGER = LoggerFactory.getLogger(DocubaseHelper.class);

   private DocubaseHelper() {}
   
   public static void initToolkit() throws RheaToolkitException {
      Properties parameters = new Properties();
      parameters.setProperty("service.access", "cer69-ds4int:4020");
      // parameters.setProperty("service.encoding", "UTF8");
      // parameters.setProperty("service.retry", "5"); // nombre de tentatives
      // de connection (5)
      // parameters.setProperty("service.timeout", "10000"); //Timeout de la
      // connexion (10000 i.e. 10s)
      if (RheaToolkit.isInitialized()) {
         RheaToolkit.shutdown();
      }
      RheaToolkit.initialize(parameters);
   }

   public static List<Document> insertManyDocs(int num, UserBase base, String appliSource)
         throws Exception {
      List<Document> docs = new ArrayList<Document>();

      for (int i = 0; i < num; i++) {
         Document doc = insertDoc(base, appliSource);
         docs.add(doc);
      }
      return docs;
   }

   public static Document insertDoc(UserBase base, String docTitle) throws Exception {
      Document doc;
      DocumentTag tag;
      DocumentStore store;
      MyTagControlError control;

      File newDoc = getAFile();
      base.getBaseDefinition().getProfile().setDocumentTitleModify(true);
      tag = createTagWithRandomData(base, docTitle);

      tag.setDocFileName(newDoc.getName());
      store = new DocumentStore(tag, newDoc);
      control = new MyTagControlError(base.getBaseDefinition().getIndex());
      doc = store.store(control);
      return doc;
   }

   public static DocumentTag createTagWithRandomData(UserBase base, String docTitle)
         throws RheaToolkitException {

      BaseIndex index = base.getBaseDefinition().getIndex();
      DocumentTag tag = new DocumentTag(base);

      tag.setDocTitle(docTitle);
      tag.setCreateDate(new Date());
      tag.setDocType(Math.random() > 0.5 ? "PDF" : "DOC"); 
      tag.addCriterion(index.getCategory(CAT_NAMES[0]), true, Math.random() > 0.5 ? ".pdf" : ".doc");
      tag.addCriterion(index.getCategory(CAT_NAMES[1]), true,
            generateString("libTypeDoc".toLowerCase(), 50, 1000));
      tag.addCriterion(index.getCategory(CAT_NAMES[2]), true, randomNum(9)); // Num
                                                                             // compte
      tag.addCriterion(index.getCategory(CAT_NAMES[3]), true, randomNum(13)); // Siret
      if (Math.random() > .9) {
         tag.addCriterion(index.getCategory(CAT_NAMES[4]), true, randomNum(15)); // NNI
         // salarié
      }
      // Période
      if (Math.random() > .2) {
         tag.addCriterion(index.getCategory(CAT_NAMES[5]), true, randomNum(4));
      }
      // C6="Code organisme du compte",IE,1,1
      tag.addCriterion(index.getCategory(CAT_NAMES[6]), true, "ur" + randomNum(3));
      // C7="Code URSSAF",IE,1,1
      tag.addCriterion(index.getCategory(CAT_NAMES[7]), true, "ur" + randomNum(3));
      // C8="No Personne",I,0,1
      if (Math.random() > .95) {
         tag.addCriterion(index.getCategory(CAT_NAMES[8]), true, randomNum(9));
      }
      // C9="No Affaire",I,0,1
      if (Math.random() > .95) {
         tag.addCriterion(index.getCategory(CAT_NAMES[9]), true, randomNum(6));
      }
      // C10="Denomination du compte",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[10]), true, randomString(50));
      // C11="No Piece",I,0,1
      if (Math.random() > .5) {
         tag.addCriterion(index.getCategory(CAT_NAMES[11]), true, randomAlphaNum(12));
      }
      // C12="Application Source",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[12]), true, docTitle);// generateAppliSource());
      // C13="No Lot",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[13]), true, randomNum(6));
      // C14="Code Commune",I,0,1
      if (Math.random() > .95) {
         tag.addCriterion(index.getCategory(CAT_NAMES[14]), true, randomAlphaNum(6));
      }
      // C15="Categorie",I,0,1
      if (Math.random() > .98) {
         tag.addCriterion(index.getCategory(CAT_NAMES[15]), true, randomNum(5));
      }
      // C16="No Groupe",I,0,1
      if (Math.random() > .2) {
         tag.addCriterion(index.getCategory(CAT_NAMES[16]), true, randomNum(18));
      }
      // C17="Date d origine",I,1,1
      tag.addCriterion(index.getCategory(CAT_NAMES[17]), true, generateDate());
      // C18="Date Traitement",I,0,1
      if (Math.random() > .2) {
         tag.addCriterion(index.getCategory(CAT_NAMES[18]), true, generateDate());
      }
      // C19="Date d Effet",I,0,1
      if (Math.random() > .2)
         tag.addCriterion(index.getCategory(CAT_NAMES[19]), true, generateDate());
      // C20="Journee Comptable",I,0,1
      // C21="No Structure CNTX",I,0,1
      if (Math.random() > .2)
         tag.addCriterion(index.getCategory(CAT_NAMES[21]), true, randomNum(10));
      // C22="No Partenaire",I,0,1
      if (Math.random() > .2)
         tag.addCriterion(index.getCategory(CAT_NAMES[22]), true, randomNum(7));
      // C23="No Intervention CTRL",I,0,1
      if (Math.random() > .05)
         tag.addCriterion(index.getCategory(CAT_NAMES[23]), true, randomNum(5));
      // C24="No Allocataire",I,0,1
      // C25="Code utilisateur indexation",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[25]), true, "ur" + randomNum(8));
      // C26="Code utilisateur stockage",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[26]), true, "ur" + randomNum(8));
      // C27="No Dossier",I,0,1
      if (Math.random() > .05) {
         // C28="Date verification",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[28]), true, generateDate());
         // C29="Agent verificateur",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[29]), true, "ur" + randomNum(8));
         // C30="Code verification",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[30]), true, randomNum(3));
      }
      // C31="Date de classement GED",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[31]), true, generateDate());
      // C32="No de compte Interne",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[32]), true, randomNum(7));
      // C33="Ancien numero de compte",I,0,1
      if (Math.random() > .05)
         tag.addCriterion(index.getCategory(CAT_NAMES[33]), true, randomNum(10));
      // C34="Type de document d archivage",IE,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[34]), true, generateTypeDoc());
      // C35="No Id Archivage",I,0,1
      if (Math.random() > .4)
         tag.addCriterion(index.getCategory(CAT_NAMES[35]), true, "ur" + randomNum(18));
      // C36="Certificat",I,0,1
      // C37="Code organisme mutualise",IE,0,1
      if (Math.random() > .1)
         tag.addCriterion(index.getCategory(CAT_NAMES[37]), true, "ur" + randomNum(3));
      if (Math.random() > .05) {
         // C38="Nom naissance salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[38]), true, randomString(10));
         // C39="Nom marital salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[39]), true, randomString(10));
         // C40="Prenom salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[40]), true, randomString(10));
         // C41="Code interne salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[41]), true, randomAlphaNum(10));
         // C42="Nature de l emploi",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[42]), true, randomNum(5));
         // C43="Date naissance salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[43]), true, generateDate());
      }
      // C44="No de Compostage",I,0,1
      if (Math.random() > .1)
         tag.addCriterion(index.getCategory(CAT_NAMES[44]), true, randomNum(11));
      // C45="Date de versement",I,0,1
      if (Math.random() > .1)
         tag.addCriterion(index.getCategory(CAT_NAMES[45]), true, generateDate());
      // C46="No Volet Social",I,0,1
      if (Math.random() > .05)
         tag.addCriterion(index.getCategory(CAT_NAMES[46]), true, randomAlphaNum(10));
      // C47="Fonction RFP",IE,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[47]), true,
            new Integer(rand.nextInt(10) + 1).toString());
      // C48="Activite RFP",IE,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[48]), true, randomAlphaNum(1));
      // C49="Nom de personne",I,0,1
      if (Math.random() > .05)
         tag.addCriterion(index.getCategory(CAT_NAMES[49]), true, randomString(50));
      // C50="Id Complementaire",I,0,1
      // C51="NNI employeur",I,0,1
      if (Math.random() > .01)
         tag.addCriterion(index.getCategory(CAT_NAMES[51]), true, randomNum(15));
      // C52="Numero de Recours",I,0,1

      return tag;
   }

   public static void call() throws Exception {
      UserBase base = session.getBaseManager().getBase(DomainFactory.getDomain(1), "NCOTI");
      BaseIndex index = base.getBaseDefinition().getIndex();

      MyTagControlError control = new MyTagControlError(base.getBaseDefinition().getIndex());
      File newDoc = getAFile();
      
      /*
       * Note pour plus tard pour les recherches : c0 catégorie très peu
       * discriminante. c3 fortement discriminante.
       */

      for (int i = 1; i <= 2; i++) {
         DocumentTag tag = new DocumentTag(base);
         // tag.setDocTitle("Document #" + i);

         tag.setDocType(i % 2 == 0 ? "TXT" : "DOC");
         tag.addCriterion(index.getCategory(CAT_NAMES[0]), true, i % 2 == 0 ? ".txt" : ".doc");

         tag.addCriterion(index.getCategory(CAT_NAMES[1]), true,
               generateString("libTypeDoc".toLowerCase(), 50, 1000));
         tag.addCriterion(index.getCategory(CAT_NAMES[2]), true, randomNum(9)); // Num
                                                                                // compte

         tag.addCriterion(index.getCategory(CAT_NAMES[3]), true, randomNum(13)); // Siret
         if (Math.random() > .9) {
            tag.addCriterion(index.getCategory(CAT_NAMES[4]), true, randomNum(15)); // NNI
                                                                                    // salarié
         }
         // Période
         if (Math.random() > .2) {
            tag.addCriterion(index.getCategory(CAT_NAMES[5]), true, randomNum(4));
         }
         // C6="Code organisme du compte",IE,1,1
         tag.addCriterion(index.getCategory(CAT_NAMES[6]), true, "ur" + randomNum(3));
         // C7="Code URSSAF",IE,1,1
         tag.addCriterion(index.getCategory(CAT_NAMES[7]), true, "ur" + randomNum(3));
         // C8="No Personne",I,0,1
         if (Math.random() > .95) {
            tag.addCriterion(index.getCategory(CAT_NAMES[8]), true, randomNum(9));
         }
         // C9="No Affaire",I,0,1
         if (Math.random() > .95) {
            tag.addCriterion(index.getCategory(CAT_NAMES[9]), true, randomNum(6));
         }
         // C10="Denomination du compte",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[10]), true, randomString(50));
         // C11="No Piece",I,0,1
         if (Math.random() > .5) {
            tag.addCriterion(index.getCategory(CAT_NAMES[11]), true, randomAlphaNum(12));
         }
         // C12="Application Source",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[12]), true, generateAppliSource());
         // C13="No Lot",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[13]), true, randomNum(6));
         // C14="Code Commune",I,0,1
         if (Math.random() > .95) {
            tag.addCriterion(index.getCategory(CAT_NAMES[14]), true, randomAlphaNum(6));
         }
         // C15="Categorie",I,0,1
         if (Math.random() > .98) {
            tag.addCriterion(index.getCategory(CAT_NAMES[15]), true, randomNum(5));
         }
         // C16="No Groupe",I,0,1
         if (Math.random() > .2) {
            tag.addCriterion(index.getCategory(CAT_NAMES[16]), true, randomNum(18));
         }
         // C17="Date d origine",I,1,1
         tag.addCriterion(index.getCategory(CAT_NAMES[17]), true, generateDate());
         // C18="Date Traitement",I,0,1
         if (Math.random() > .2) {
            tag.addCriterion(index.getCategory(CAT_NAMES[18]), true, generateDate());
         }
         // C19="Date d Effet",I,0,1
         if (Math.random() > .2)
            tag.addCriterion(index.getCategory(CAT_NAMES[19]), true, generateDate());
         // C20="Journee Comptable",I,0,1
         // C21="No Structure CNTX",I,0,1
         if (Math.random() > .2)
            tag.addCriterion(index.getCategory(CAT_NAMES[21]), true, randomNum(10));
         // C22="No Partenaire",I,0,1
         if (Math.random() > .2)
            tag.addCriterion(index.getCategory(CAT_NAMES[22]), true, randomNum(7));
         // C23="No Intervention CTRL",I,0,1
         if (Math.random() > .05)
            tag.addCriterion(index.getCategory(CAT_NAMES[23]), true, randomNum(5));
         // C24="No Allocataire",I,0,1
         // C25="Code utilisateur indexation",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[25]), true, "ur" + randomNum(8));
         // C26="Code utilisateur stockage",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[26]), true, "ur" + randomNum(8));
         // C27="No Dossier",I,0,1
         if (Math.random() > .05) {
            // C28="Date verification",I,0,1
            tag.addCriterion(index.getCategory(CAT_NAMES[28]), true, generateDate());
            // C29="Agent verificateur",I,0,1
            tag.addCriterion(index.getCategory(CAT_NAMES[29]), true, "ur" + randomNum(8));
            // C30="Code verification",I,0,1
            tag.addCriterion(index.getCategory(CAT_NAMES[30]), true, randomNum(3));
         }
         // C31="Date de classement GED",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[31]), true, generateDate());
         // C32="No de compte Interne",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[32]), true, randomNum(7));
         // C33="Ancien numero de compte",I,0,1
         if (Math.random() > .05)
            tag.addCriterion(index.getCategory(CAT_NAMES[33]), true, randomNum(10));
         // C34="Type de document d archivage",IE,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[34]), true, generateTypeDoc());
         // C35="No Id Archivage",I,0,1
         if (Math.random() > .4)
            tag.addCriterion(index.getCategory(CAT_NAMES[35]), true, "ur" + randomNum(18));
         // C36="Certificat",I,0,1
         // C37="Code organisme mutualise",IE,0,1
         if (Math.random() > .1)
            tag.addCriterion(index.getCategory(CAT_NAMES[37]), true, "ur" + randomNum(3));
         if (Math.random() > .05) {
            // C38="Nom naissance salarie",I,0,1
            tag.addCriterion(index.getCategory(CAT_NAMES[38]), true, randomString(10));
            // C39="Nom marital salarie",I,0,1
            tag.addCriterion(index.getCategory(CAT_NAMES[39]), true, randomString(10));
            // C40="Prenom salarie",I,0,1
            tag.addCriterion(index.getCategory(CAT_NAMES[40]), true, randomString(10));
            // C41="Code interne salarie",I,0,1
            tag.addCriterion(index.getCategory(CAT_NAMES[41]), true, randomAlphaNum(10));
            // C42="Nature de l emploi",I,0,1
            tag.addCriterion(index.getCategory(CAT_NAMES[42]), true, randomNum(5));
            // C43="Date naissance salarie",I,0,1
            tag.addCriterion(index.getCategory(CAT_NAMES[43]), true, generateDate());
         }
         // C44="No de Compostage",I,0,1
         if (Math.random() > .1)
            tag.addCriterion(index.getCategory(CAT_NAMES[44]), true, randomNum(11));
         // C45="Date de versement",I,0,1
         if (Math.random() > .1)
            tag.addCriterion(index.getCategory(CAT_NAMES[45]), true, generateDate());
         // C46="No Volet Social",I,0,1
         if (Math.random() > .05)
            tag.addCriterion(index.getCategory(CAT_NAMES[46]), true, randomAlphaNum(10));
         // C47="Fonction RFP",IE,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[47]), true,
               new Integer(rand.nextInt(10) + 1).toString());
         // C48="Activite RFP",IE,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[48]), true, randomAlphaNum(1));
         // C49="Nom de personne",I,0,1
         if (Math.random() > .05)
            tag.addCriterion(index.getCategory(CAT_NAMES[49]), true, randomString(50));
         // C50="Id Complementaire",I,0,1
         // C51="NNI employeur",I,0,1
         if (Math.random() > .01)
            tag.addCriterion(index.getCategory(CAT_NAMES[51]), true, randomNum(15));
         // C52="Numero de Recours",I,0,1

         DocumentStore store = new DocumentStore(tag, newDoc);
         Document doc = null;
         boolean error = false;
         try {
            doc = store.store(control);
         } catch (Exception exc) {
            LOGGER.error("Erreur sur le document " + i + " : " + exc.getMessage()
                  + ", on retente un autre");
            error = true;
            exc.printStackTrace();
         } finally {
            if (doc == null) {
               LOGGER.error("Erreur sur le document " + i + " : indeterminee, on retente un autre");
            }
            if (error || doc == null) {
               i--;
               continue;
            }
         }
      }
   }

   public static String generateTypeDoc() {
      int nb = rand.nextInt(4) + 1;
      String typeDoc = "";
      for (int i = 0; i < nb; i++) {
         if (!typeDoc.isEmpty())
            typeDoc = typeDoc + ".";
         typeDoc = typeDoc + rand.nextInt(10);
      }
      return typeDoc;
   }

   public static String generateAppliSource() {
      if (Math.random() > .15)
         return "lad";
      if (Math.random() > .5)
         return "ged";
      if (Math.random() > .5)
         return "watt";
      return "bcc";
   }

   public static String generateDate() {
      int annee = rand.nextInt(10) + 1;
      int mois = rand.nextInt(12) + 1;
      int jour = rand.nextInt(28) + 1;
      String date = "20" + String.format("%02d-%02d-%02d", annee, mois, jour);
      return date;
   }

   public static String randomNum(int len) {
      StringBuilder sb = new StringBuilder(len);
      for (int i = 0; i < len; i++) {
         int index = rand.nextInt(NUMERIC.length());
         sb.append(NUMERIC.substring(index, index + 1));
      }
      return sb.toString();
   }

   public static String randomString(int len) {
      StringBuilder sb = new StringBuilder(len);
      for (int i = 0; i < len; i++) {
         int index = rand.nextInt(ALPHA.length());
         sb.append(ALPHA.substring(index, index + 1));
      }
      return sb.toString();
   }

   public static String randomAlphaNum(int len) {
      StringBuilder sb = new StringBuilder(len);
      for (int i = 0; i < len; i++) {
         int index = rand.nextInt(ALPHA_NUM.length());
         sb.append(ALPHA_NUM.substring(index, index + 1));
      }
      return sb.toString();
   }

   public static File getAFile() throws Exception {
      File newDoc = File.createTempFile("doc" + System.nanoTime(), ".txt");
      FileOutputStream fos = new FileOutputStream(newDoc);
      fos.write(DOC_CONTENT);
      fos.flush();
      fos.close();
      newDoc.deleteOnExit();
      return newDoc;
   }

   public static ConcurrentMap<String, List<String>> generatedStrings = new ConcurrentHashMap<String, List<String>>();

   public static String generateString(String id, int longueur, int nbValue) {
      List<String> list = generatedStrings.get(id);
      if (list == null) {
         list = new ArrayList<String>();
         generatedStrings.put(id, list);
      }

      String result = null;
      if (list.size() < nbValue) {
         // Génère un nouvel élément
         String s = randomString(longueur);
         list.add(s);
         result = s;
      } else {
         // Choisi un élément au hasard dans la liste
         int index = rand.nextInt(list.size());
         result = list.get(index);
      }
      if (result == null) {
         result = generateString(id, longueur, nbValue);
      }
      return result;
   }

   private static volatile UserSession session = null;

   public static UserSession getSession(String login, String pwd) throws RheaToolkitException {
      if (session == null) {
         Locale[] list = new Locale[2];
         list[0] = Locale.FRENCH;
         list[1] = Locale.ENGLISH;

         File path = new File(System.getProperty("java.io.tmpdir") + "/" + login);
         // try {
         session = UserSessionFactory.connect(login, pwd, list, path);
         // } catch (RheaToolkitException e) {}
      }
      return session;
   }

   public static UserSession getAdminSession() throws RheaToolkitException {
      return getSession("_ADMIN", "DOCUBASE");
   }

   public static UserBase getBase(Integer domainId) throws RheaToolkitException {
      return getAdminSession().getBaseManager().getBase(DomainFactory.getDomain(domainId), "NCOTI");
   }

   public static void testCall() throws Exception {
      Locale[] list = new Locale[2];
      list[0] = Locale.FRENCH;
      list[1] = Locale.ENGLISH;

      File path = new File(System.getProperty("java.io.tmpdir") + "/_ADMIN");
      // try {
      UserSession sess = UserSessionFactory.connect("_ADMIN", "DOCUBASE", list, path);

      UserBase base = sess.getBaseManager().getBase(DomainFactory.getDomain(1), "NCOTI");
      BaseIndex index = base.getBaseDefinition().getIndex();

      MyTagControlError control = new MyTagControlError(base.getBaseDefinition().getIndex());
      File newDoc = getAFile();
      /*
       * Note pour plus tard pour les recherches : c0 catégorie très peu
       * discriminante. c3 fortement discriminante.
       */

      DocumentTag tag = new DocumentTag(base);
      // tag.setDocTitle("Document n " + i);

      tag.setDocType(rand.nextDouble() > 0.5 ? "TXT" : "DOC");
      tag.addCriterion(index.getCategory(CAT_NAMES[0]), true, rand.nextDouble() > 0.5 ? ".txt"
            : ".doc");

      tag.addCriterion(index.getCategory(CAT_NAMES[1]), true,
            generateString("libTypeDoc".toLowerCase(), 50, 1000));
      tag.addCriterion(index.getCategory(CAT_NAMES[2]), true, randomNum(9)); // Num
                                                                             // compte

      tag.addCriterion(index.getCategory(CAT_NAMES[3]), true, randomNum(13)); // Siret
      if (Math.random() > .9) {
         tag.addCriterion(index.getCategory(CAT_NAMES[4]), true, randomNum(15)); // NNI
                                                                                 // salarié
      }
      // Période
      if (Math.random() > .2) {
         tag.addCriterion(index.getCategory(CAT_NAMES[5]), true, randomNum(4));
      }
      // C6="Code organisme du compte",IE,1,1
      tag.addCriterion(index.getCategory(CAT_NAMES[6]), true, "ur" + randomNum(3));
      // C7="Code URSSAF",IE,1,1
      tag.addCriterion(index.getCategory(CAT_NAMES[7]), true, "ur" + randomNum(3));
      // C8="No Personne",I,0,1
      if (Math.random() > .95) {
         tag.addCriterion(index.getCategory(CAT_NAMES[8]), true, randomNum(9));
      }
      // C9="No Affaire",I,0,1
      if (Math.random() > .95) {
         tag.addCriterion(index.getCategory(CAT_NAMES[9]), true, randomNum(6));
      }
      // C10="Denomination du compte",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[10]), true, randomString(50));
      // C11="No Piece",I,0,1
      if (Math.random() > .5) {
         tag.addCriterion(index.getCategory(CAT_NAMES[11]), true, randomAlphaNum(12));
      }
      // C12="Application Source",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[12]), true, generateAppliSource());
      // C13="No Lot",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[13]), true, randomNum(6));
      // C14="Code Commune",I,0,1
      if (Math.random() > .95) {
         tag.addCriterion(index.getCategory(CAT_NAMES[14]), true, randomAlphaNum(6));
      }
      // C15="Categorie",I,0,1
      if (Math.random() > .98) {
         tag.addCriterion(index.getCategory(CAT_NAMES[15]), true, randomNum(5));
      }
      // C16="No Groupe",I,0,1
      if (Math.random() > .2) {
         tag.addCriterion(index.getCategory(CAT_NAMES[16]), true, randomNum(18));
      }
      // C17="Date d origine",I,1,1
      tag.addCriterion(index.getCategory(CAT_NAMES[17]), true, generateDate());
      // C18="Date Traitement",I,0,1
      if (Math.random() > .2) {
         tag.addCriterion(index.getCategory(CAT_NAMES[18]), true, generateDate());
      }
      // C19="Date d Effet",I,0,1
      if (Math.random() > .2)
         tag.addCriterion(index.getCategory(CAT_NAMES[19]), true, generateDate());
      // C20="Journee Comptable",I,0,1
      // C21="No Structure CNTX",I,0,1
      if (Math.random() > .2)
         tag.addCriterion(index.getCategory(CAT_NAMES[21]), true, randomNum(10));
      // C22="No Partenaire",I,0,1
      if (Math.random() > .2)
         tag.addCriterion(index.getCategory(CAT_NAMES[22]), true, randomNum(7));
      // C23="No Intervention CTRL",I,0,1
      if (Math.random() > .05)
         tag.addCriterion(index.getCategory(CAT_NAMES[23]), true, randomNum(5));
      // C24="No Allocataire",I,0,1
      // C25="Code utilisateur indexation",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[25]), true, "ur" + randomNum(8));
      // C26="Code utilisateur stockage",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[26]), true, "ur" + randomNum(8));
      // C27="No Dossier",I,0,1
      if (Math.random() > .05) {
         // C28="Date verification",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[28]), true, generateDate());
         // C29="Agent verificateur",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[29]), true, "ur" + randomNum(8));
         // C30="Code verification",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[30]), true, randomNum(3));
      }
      // C31="Date de classement GED",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[31]), true, generateDate());
      // C32="No de compte Interne",I,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[32]), true, randomNum(7));
      // C33="Ancien numero de compte",I,0,1
      if (Math.random() > .05)
         tag.addCriterion(index.getCategory(CAT_NAMES[33]), true, randomNum(10));
      // C34="Type de document d archivage",IE,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[34]), true, generateTypeDoc());
      // C35="No Id Archivage",I,0,1
      if (Math.random() > .4)
         tag.addCriterion(index.getCategory(CAT_NAMES[35]), true, "ur" + randomNum(18));
      // C36="Certificat",I,0,1
      // C37="Code organisme mutualise",IE,0,1
      if (Math.random() > .1)
         tag.addCriterion(index.getCategory(CAT_NAMES[37]), true, "ur" + randomNum(3));
      if (Math.random() > .05) {
         // C38="Nom naissance salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[38]), true, randomString(10));
         // C39="Nom marital salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[39]), true, randomString(10));
         // C40="Prenom salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[40]), true, randomString(10));
         // C41="Code interne salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[41]), true, randomAlphaNum(10));
         // C42="Nature de l emploi",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[42]), true, randomNum(5));
         // C43="Date naissance salarie",I,0,1
         tag.addCriterion(index.getCategory(CAT_NAMES[43]), true, generateDate());
      }
      // C44="No de Compostage",I,0,1
      if (Math.random() > .1)
         tag.addCriterion(index.getCategory(CAT_NAMES[44]), true, randomNum(11));
      // C45="Date de versement",I,0,1
      if (Math.random() > .1)
         tag.addCriterion(index.getCategory(CAT_NAMES[45]), true, generateDate());
      // C46="No Volet Social",I,0,1
      if (Math.random() > .05)
         tag.addCriterion(index.getCategory(CAT_NAMES[46]), true, randomAlphaNum(10));
      // C47="Fonction RFP",IE,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[47]), true,
            new Integer(rand.nextInt(10) + 1).toString());
      // C48="Activite RFP",IE,0,1
      tag.addCriterion(index.getCategory(CAT_NAMES[48]), true, randomAlphaNum(1));
      // C49="Nom de personne",I,0,1
      if (Math.random() > .05)
         tag.addCriterion(index.getCategory(CAT_NAMES[49]), true, randomString(50));
      // C50="Id Complementaire",I,0,1
      // C51="NNI employeur",I,0,1
      if (Math.random() > .01)
         tag.addCriterion(index.getCategory(CAT_NAMES[51]), true, randomNum(15));
      // C52="Numero de Recours",I,0,1

      DocumentStore store = new DocumentStore(tag, newDoc);

      try {
         store.store(control);
      } catch (Exception exc) {
         exc.printStackTrace();
      }
   }
}