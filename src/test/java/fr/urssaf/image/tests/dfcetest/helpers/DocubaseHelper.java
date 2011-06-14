package fr.urssaf.image.tests.dfcetest.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.docubase.toolkit.exception.ged.CustomTagControlException;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.base.BaseCategory;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.tests.dfcetest.Categories;
import fr.urssaf.image.tests.dfcetest.NcotiCategories;

public final class DocubaseHelper {
   public static final String NUMERIC = "0123456789";
   public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
   public static final String ALPHA_NUM = ALPHA + NUMERIC;
   private static final byte[] DOC_CONTENT = "Ceci est un petit document!".getBytes();

   private static Random rand = new Random();
   private static final Logger LOGGER = LoggerFactory.getLogger(DocubaseHelper.class);

   private DocubaseHelper() {
   }

   public static Document insertOneDoc(Base base, String appliSource) {
      Document doc = null;
      List<Document> docs = insertManyDocs(1, base, appliSource);

      if (docs != null) {
         doc = docs.get(0);
      }
      return doc;
   }

   public static List<Document> insertManyDocs(int num, Base base, String appliSource) {
      List<Document> docs = null;
      try {
         docs = new ArrayList<Document>();

         for (int i = 0; i < num; i++) {
            Document doc = storeOneDocWithRandomCategories(base, appliSource);
            docs.add(doc);
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
      return docs;
   }
   
   public static Document storeOneDoc(Base base, String title, String type, Map<String, Object> catValues) {
      Document document = ToolkitFactory.getInstance().createDocumentTag(base);

      // document.setDocTitle(title);
      document.setDocType(type);
      storeThisDoc(base, document, catValues);
      return document;
   }
   
   public static Document storeOneDoc(Base base, Map<String, Object> catValues) {
      String type = Math.random() > 0.5 ? "PDF" : "DOC";
      return storeOneDoc(base, catValues.get(Categories.TITRE.toString()).toString(), type, catValues);
   }   

   public static Document storeOneDocWithRandomCategories(Base base, String title) throws Exception {
      String type = Math.random() > 0.5 ? "PDF" : "DOC";
      Map<String, Object> catValues = getCategoriesRandomValues(title);
      return storeOneDoc(base, title, type, catValues);
   }

   /**
    * @param document
    * @param catValues
    * @throws CustomTagControlException 
    */
   public static boolean storeThisDoc(Base base, Document document, Map<String, Object> catValues) {
      for (Entry<String, Object> ent : catValues.entrySet()) {
         BaseCategory baseCategory = base.getBaseCategory(ent.getKey());
         document.addCriterion(baseCategory, ent.getValue());
      }
      return ServiceProvider.getStoreService().storeDocument(document, getAFile());
   }   
   
   /**
    * @param title
    * @return
    */
   public static Map<String, Object> getCategoriesRandomValues(String title) {
      Map<String, Object> catValues = new HashMap<String, Object>();

      // Catégories obligatoires
      //
      catValues.put(Categories.TITRE.toString(), title);
      catValues.put(Categories.TYPE_DOC.toString(), Math.random() > 0.5 ? ".pdf" : ".doc");
      catValues.put(Categories.DATETIME.toString(), new Date());
      // On met le titre dans l'application source pour être rétro compatible
      // avec les anciens tests
      catValues.put(Categories.APPLI_SOURCE.toString(), title);

      // Catégories facultatives
      if (Math.random() > .2)
         catValues.put(Categories.DATE.toString(), new Date());

      if (Math.random() > .2)
         catValues.put(Categories.INTEGER.toString(), rand.nextInt());

      if (Math.random() > .75)
         catValues.put(Categories.BOOLEAN.toString(), rand.nextBoolean());

      // Ce n'est pas une catégorie obligatoire mais cela permet d'avoir une distribution normale
      // lors de l'injection en masse de valeur aléatoire
      catValues.put(Categories.DECIMAL.toString(), rand.nextGaussian());      
      return catValues;
   }
   
   
   /**
    * @param title
    *           Le titre du document est affecté à la catégorie
    *           "application source"
    * @return nom de la catégorie => valeur générée aléatoirement
    */
   public static Map<String, String> getNcotiCategoriesRandomValues(String title) {
      Map<String, String> catValues = new HashMap<String, String>();
      
      catValues.put(NcotiCategories.TYPE_DOC.toString(), Math.random() > 0.5 ? ".pdf" : ".doc"); // 0
      catValues.put(NcotiCategories.TYPE_DOC_LIBELLE.toString(),
            generateString("libTypeDoc".toLowerCase(), 50, 1000)); // 1
      catValues.put(NcotiCategories.NUM_CPT.toString(), randomNum(9)); // 2
      catValues.put(NcotiCategories.SIRET.toString(), randomNum(13)); // 3
      if (Math.random() > .9)
         catValues.put(NcotiCategories.NNI.toString(), randomNum(15)); // 4
      if (Math.random() > .2)
         catValues.put(NcotiCategories.PERIODE.toString(), randomNum(4)); // 5
      catValues.put(NcotiCategories.CODE_ORGA_CPT.toString(), "ur" + randomNum(3)); // 6
      catValues.put(NcotiCategories.CODE_UR.toString(), "ur" + randomNum(3)); // 7
      if (Math.random() > .95)
         catValues.put(NcotiCategories.NUM_PERS.toString(), randomNum(9)); // 8
      if (Math.random() > .95)
         catValues.put(NcotiCategories.NUM_AFFAIRE.toString(), randomNum(6)); // 9
      catValues.put(NcotiCategories.DENOM_CPT.toString(), randomString(50)); // 10
      if (Math.random() > .5)
         catValues.put(NcotiCategories.NUM_PIECE.toString(), randomAlphaNum(12)); // 11
      catValues.put(NcotiCategories.APPLI_SOURCE.toString(), title); // 12
      catValues.put(NcotiCategories.NUM_LOT.toString(), randomNum(6)); // 13
      if (Math.random() > .95)
         catValues.put(NcotiCategories.CODE_COMMUNE.toString(), randomAlphaNum(6)); // 14
      if (Math.random() > .98)
         catValues.put(NcotiCategories.CATEGORIE.toString(), randomNum(5)); // 15
      if (Math.random() > .2)
         catValues.put(NcotiCategories.NUM_GROUPE.toString(), randomNum(18)); // 16
      catValues.put(NcotiCategories.DATE_ORIGINE.toString(), generateDate()); // 17
      if (Math.random() > .2)
         catValues.put(NcotiCategories.DATE_TRAITEMENT.toString(), generateDate()); // 18
      if (Math.random() > .2)
         catValues.put(NcotiCategories.DATE_EFFET.toString(), generateDate()); // 19
      // catValues.put(NcotiCategories.JOURNEE_COMPTABLE.toString(), AAA); // 20
      if (Math.random() > .2)
         catValues.put(NcotiCategories.NUM_STRUCT_CTX.toString(), randomNum(10)); // 21
      if (Math.random() > .2)
         catValues.put(NcotiCategories.NUM_PARTENAIRE.toString(), randomNum(7)); // 22
      if (Math.random() > .05)
         catValues.put(NcotiCategories.NUM_INTER_CTX.toString(), randomNum(5)); // 23
      // catValues.put(NcotiCategories.NUM_ALLOCATAIRE.toString(), AAA); // 24
      catValues.put(NcotiCategories.CODE_USER_INDEX.toString(), "ur" + randomNum(8)); // 25
      catValues.put(NcotiCategories.CODE_USER_STOCK.toString(), "ur" + randomNum(8)); // 26
      // catValues.put(NcotiCategories.NUM_DOSSIER.toString(), AAA); // 27
      if (Math.random() > .05) {
         catValues.put(NcotiCategories.DATE_VERIF.toString(), generateDate()); // 28
         catValues.put(NcotiCategories.AGENT_VERIF.toString(), "ur" + randomNum(8)); // 29
         catValues.put(NcotiCategories.CODE_VERIF.toString(), randomNum(3)); // 30
      }
      catValues.put(NcotiCategories.DATE_CLASSEMENT_GED.toString(), generateDate()); // 31
      catValues.put(NcotiCategories.NUM_CPT_INT.toString(), randomNum(7)); // 32
      if (Math.random() > .05)
         catValues.put(NcotiCategories.NUM_CPT_ANCIEN.toString(), randomNum(10)); // 33
      catValues.put(NcotiCategories.TYPE_DOC_ARCHIVAGE.toString(), generateTypeDoc()); // 34
      if (Math.random() > .4)
         catValues.put(NcotiCategories.NUM_ID_ARCHIVAGE.toString(), "ur" + randomNum(18)); // 35
      // catValues.put(NcotiCategories.CERTIFICAT.toString(), AAA); // 36
      if (Math.random() > .1)
         catValues.put(NcotiCategories.CODE_ORGA_MUTUALISE.toString(), "ur" + randomNum(3)); // 37
      if (Math.random() > .05) {
         catValues.put(NcotiCategories.NOM_NAISS_SALARIE.toString(), randomString(10)); // 38
         catValues.put(NcotiCategories.NOM_MARITAL_SALARIE.toString(), randomString(10)); // 39
         catValues.put(NcotiCategories.PRENOM_SALARIE.toString(), randomString(10)); // 40
         catValues.put(NcotiCategories.CODE_INT_SALARIE.toString(), randomAlphaNum(10)); // 41
         catValues.put(NcotiCategories.NATURE_EMPLOI.toString(), randomNum(5)); // 42
         catValues.put(NcotiCategories.DATE_NAISS_SALARIE.toString(), generateDate()); // 43
      }
      if (Math.random() > .1)
         catValues.put(NcotiCategories.NUM_COMPOSTAGE.toString(), randomNum(11)); // 44
      if (Math.random() > .1)
         catValues.put(NcotiCategories.DATE_VERSEMENT.toString(), generateDate()); // 45
      if (Math.random() > .05)
         catValues.put(NcotiCategories.NUM_VOLET_SOCIAL.toString(), randomAlphaNum(10)); // 46
      catValues.put(NcotiCategories.RFP_FONCTION.toString(),
            new Integer(rand.nextInt(10) + 1).toString()); // 47
      catValues.put(NcotiCategories.RFP_ACTIVITE.toString(), randomAlphaNum(1)); // 48
      if (Math.random() > .05)
         catValues.put(NcotiCategories.NOM_PERS.toString(), randomString(50)); // 49
      // catValues.put(NcotiCategories.ID_COMPLEMENTAIRE.toString(), AAA); // 50
      if (Math.random() > .01)
         catValues.put(NcotiCategories.NNI_EMPLOYEUR.toString(), randomNum(15)); // 51
      // catValues.put(NcotiCategories.NUM_RECOURS.toString(), AAA); // 52
      return catValues;
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

   public static File getAFile() {
      File newDoc = null;
      try {
         newDoc = File.createTempFile("doc" + System.nanoTime(), ".tiff");
         FileOutputStream fos = new FileOutputStream(newDoc);
         fos.write(DOC_CONTENT);
         String randomPart = randomString(10);
         //System.out.println(randomPart);
         fos.write(randomPart.getBytes());
         fos.flush();
         fos.close();
         newDoc.deleteOnExit();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
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

   /**
    * Arrête la base puis la supprime. (null safe)
    */
   public static void dropBase(Base base) {
      if (base != null) {
         ServiceProvider.getBaseAdministrationService().stopBase(base);
         ServiceProvider.getBaseAdministrationService().deleteBase(base);
      }
   }

   /**
    * Démarre la base. (null safe)
    */
   public static void startBase(Base base) {
      if (base != null) {
         ServiceProvider.getBaseAdministrationService().startBase(base);
      }
   }

   public static Base getBase(String baseId) {
      Base base = ServiceProvider.getBaseAdministrationService().getBase(baseId);

      if (base == null) {
         throw new RuntimeException("Base " + baseId + " introuvable");
      }
      return base;
   }

   /**
    * @param baseId
    */
   public static void dropBase(String baseId) {
      try {
         Base base = getBase(baseId);
         dropBase(base);
      } catch (Exception e) {
         LOGGER.error("Impossible de supprimer la base " + baseId, e);
      }
   }
}