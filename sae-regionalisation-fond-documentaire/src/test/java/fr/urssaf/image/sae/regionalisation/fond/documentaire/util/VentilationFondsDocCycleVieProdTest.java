package fr.urssaf.image.sae.regionalisation.fond.documentaire.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
 * Elle contient des méthodes pour extraire le fonds 
 * documentaire du SAE de PRODUCTION, et ventiler ce fonds
 * sur différents critères
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
//@Ignore
public class VentilationFondsDocCycleVieProdTest {

   private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh'h'mm ss's'");
   
   @Autowired
   private CassandraSupport cassandraSupport;
   
   @Autowired
   private DocInfoDao infoDao;
   
   @Autowired
   private Properties cassandraConf;
   
   
   private static String CHEMINREP = "c:/divers";
   private static String NOMFICHIER = "fonds_doc_prod_cycle_vie.csv";
   
   
   @Test
   public void extraitFondsDoc() throws IOException {

      // Liste des métadonnées que l'on va lire
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.add("SM_BASE_ID");
      reqMetas.add("SM_UUID");
      reqMetas.add("SM_LIFE_CYCLE_REFERENCE_DATE");
      reqMetas.add("SM_ARCHIVAGE_DATE");
      reqMetas.add("dfc");
      reqMetas.add("SM_FINAL_DATE");
      reqMetas.add("SM_DISPOSAL_DATE");
      
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
         String nomBaseDfce;
         
         while (iterator.hasNext()) {
            map = iterator.next();
            
            idDoc = map.get("SM_UUID");
            nomBaseDfce = map.get("SM_BASE_ID");
            
            if ( 
                  StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue) && 
                  StringUtils.isNotBlank(idDoc)) { 
               
               writer.write(idDoc);
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("SM_LIFE_CYCLE_REFERENCE_DATE")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("SM_ARCHIVAGE_DATE")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("dfc")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("SM_FINAL_DATE")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("SM_DISPOSAL_DATE")));
               
               writer.write("\n");
               
               nbDocsSortis++;
               
            }
            
            nbDocsTraites++;
            if ((nbDocsTraites%1000)==0) {
               System.out.println("Nombre de docs traités : " + nbDocsTraites);
            }
            
         }
         
         System.out.println("Nombre total de docs traités : " + (nbDocsTraites-1));
         System.out.println("Nombre total de docs sortis dans le fichier : " + (nbDocsSortis-1));

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
   public void compte_FinalDateVide() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String finalDate;
      long nbDocWithoutFinalDate = 0;
      while ((nextLine = reader.readNext()) != null) {
         
         finalDate = nextLine[4];
         
         if (finalDate == null || finalDate.trim().equals("")) {
            nbDocWithoutFinalDate++;
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println(printDate() + " - Nombre de documents sans final date : " + nbDocWithoutFinalDate);
      
      System.out.println();
      System.out.println("Opération terminée");
   }
   
   @Test
   public void compte_DisposalDateVide() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String disposalDate;
      long nbDocWithoutDisposalDate = 0;
      while ((nextLine = reader.readNext()) != null) {
         
         disposalDate = nextLine[5];
         
         if (disposalDate == null || disposalDate.trim().equals("")) {
            nbDocWithoutDisposalDate++;
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println(printDate() + " - Nombre de documents sans disposal date : " + nbDocWithoutDisposalDate);
      
      System.out.println();
      System.out.println("Opération terminée");
   }
   
   @Test
   public void compte_NbDocASupprimer() throws IOException, ParseException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');
      
      DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
      DateTime now = new DateTime();

      String[] nextLine;
      int cptDoc = 0;
      String dateFinConservation;
      long nbDocWithoutDateFinConservation = 0;
      long nbDocASupprimer = 0;
      while ((nextLine = reader.readNext()) != null) {
         
         dateFinConservation = nextLine[3];
         
         if (dateFinConservation == null || dateFinConservation.trim().equals("")) {
            nbDocWithoutDateFinConservation++;
         } else {
            DateTime date = formatter.parseDateTime(dateFinConservation);
            if (now.isAfter(date)) {
               nbDocASupprimer++;
            }
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println(printDate() + " - Nombre de documents sans date de fin de conservation : " + nbDocWithoutDateFinConservation);
      System.out.println(printDate() + " - Nombre de documents a supprimer : " + nbDocASupprimer);
      
      System.out.println();
      System.out.println("Opération terminée");
   }
}
