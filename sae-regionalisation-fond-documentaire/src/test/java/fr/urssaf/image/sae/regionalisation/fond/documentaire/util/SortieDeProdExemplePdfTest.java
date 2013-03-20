package fr.urssaf.image.sae.regionalisation.fond.documentaire.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.com.bytecode.opencsv.CSVReader;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.iterator.CassandraIterator;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;

/**
 * Cette classe n'est pas une "vraie" classe de TU.
 * 
 * Elle contient des méthodes pour extraire le fonds documentaire du SAE de
 * PRODUCTION, et ventiler ce fonds sur différents critères
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
@Ignore
public class SortieDeProdExemplePdfTest {

   private DateFormat dateFormat = new SimpleDateFormat(
         "dd/MM/yyyy hh'h'mm ss's'");

   @Autowired
   private CassandraSupport cassandraSupport;

   @Autowired
   private DocInfoDao infoDao;

   @Autowired
   private Properties cassandraConf;

   private static String CHEMINREP = "c:/divers";
   private static String NOMFICHIER = "fonds_doc_prod_pour_ventilation.csv";

   @Test
   public void extraitFondsDoc() throws IOException {

      // Liste des métadonnées que l'on va lire
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.add("SM_BASE_ID");
      reqMetas.add("SM_UUID");
      reqMetas.add("SM_ARCHIVAGE_DATE");
      reqMetas.add("SM_DOCUMENT_TYPE");
      reqMetas.add("SM_TITLE");
      reqMetas.add("SM_SIZE");
      reqMetas.add("cse");
      reqMetas.add("apr");
      reqMetas.add("atr");
      reqMetas.add("cog");

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }

      // Récupère le nom de la base DFCE sur laquelle travailler
      String nomBaseDfceAttendue = cassandraConf.getProperty("db.baseName");

      Writer writer = null;
      try {
         cassandraSupport.connect();

         File fichier = new File(rep, NOMFICHIER);
         writer = new FileWriter(fichier);

         AllRowsQuery<DocInfoKey, String> query = infoDao.getQuery(reqMetas
               .toArray(new String[0]));
         CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
               query);

         Map<String, String> map;

         int nbDocsTraites = 0;
         int nbDocsSortis = 0;

         String idDoc;
         String cog;
         String nomBaseDfce;

         while (iterator.hasNext()) {
            map = iterator.next();

            idDoc = map.get("SM_UUID");
            cog = map.get("cog");
            nomBaseDfce = map.get("SM_BASE_ID");

            if (StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue)
                  && StringUtils.isNotBlank(idDoc)
                  && StringUtils.isNotBlank(cog)) {

               writer.write(idDoc);
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map
                     .get("SM_ARCHIVAGE_DATE")));
               writer.write(";");
               writer.write(StringUtils
                     .trimToEmpty(map.get("SM_DOCUMENT_TYPE")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("SM_TITLE")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("SM_SIZE")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("cse")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("apr")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("atr")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("cog")));
               writer.write("\n");

               nbDocsSortis++;

            }

            nbDocsTraites++;
            if ((nbDocsTraites % 1000) == 0) {
               System.out.println("Nombre de docs traités : " + nbDocsTraites);
            }

         }

         System.out.println("Nombre total de docs traités : "
               + (nbDocsTraites - 1));
         System.out.println("Nombre total de docs sortis dans le fichier : "
               + (nbDocsSortis - 1));

      } catch (IOException exception) {
         System.err.println(exception);

      } finally {
         closeWriter(writer);
         cassandraSupport.disconnect();
      }

   }

   private void closeWriter(Writer writer) {
      try {
         if (writer != null) {
            writer.close();
         }
      } catch (IOException exception) {
         System.err.println("impossible de fermer le flux");
      }
   }

   private String printDate() {
      return dateFormat.format(new Date());
   }

   @Test
   public void ventilation_ParTitreAvecUnIdDoc() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeRndEtTitre;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         codeRndEtTitre = nextLine[2] + ";" + nextLine[3];

         cptActuel = mapComptages.get(codeRndEtTitre);
         if (cptActuel == null) {
            mapComptages.put(codeRndEtTitre, 1L);
         } else {
            mapComptages.put(codeRndEtTitre, cptActuel + 1);
         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }

      }

      reader.close();

      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

      Map<String, String> treeMap = new TreeMap<String, String>();
      for (Map.Entry<String, Long> entry : mapComptages.entrySet()) {
         treeMap.put(entry.getKey(), entry.getValue().toString());
      }

      enrichitAvecPremierIdDoc(treeMap);

      for (Map.Entry<String, String> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   private void enrichitAvecPremierIdDoc(Map<String, String> map)
         throws IOException {

      System.out.println(printDate()
            + " - Enrichissement avec un id de document");

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');
      String[] nextLine;
      int cptDoc = 0;
      String[] datas;
      String codeRND;
      String titre;
      while ((nextLine = reader.readNext()) != null) {

         for (Map.Entry<String, String> entry : map.entrySet()) {

            if (!isDejaEnrichiAvecIdDoc(entry)) {

               datas = StringUtils.split(entry.getKey(), ';');
               codeRND = datas[0];
               titre = datas[1];

               if (codeRND.equals(nextLine[2]) && titre.equals(nextLine[3])) {

                  entry.setValue(entry.getValue() + ";" + nextLine[0]);

               }

            }

         }

         if (isDejaEnrichiAvecIdDoc(map)) {
            break;
         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }

      }

      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

   }

   private boolean isDejaEnrichiAvecIdDoc(Map.Entry<String, String> entry) {
      String[] datas = StringUtils.split(entry.getValue(), ';');
      return datas.length > 1;
   }

   private boolean isDejaEnrichiAvecIdDoc(Map<String, String> map) {
      for (Map.Entry<String, String> entry : map.entrySet()) {
         if (!isDejaEnrichiAvecIdDoc(entry))
            return false;
      }
      return true;
   }

   @Test
   public void listingMandatsSepa() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      Map<String, Long> mapSepa = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         if ("1.2.2.4.12".equals(nextLine[2])) {
            mapSepa.put(nextLine[0], new Long(nextLine[4]));
         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }

      }

      reader.close();

      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapSepa);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

}
