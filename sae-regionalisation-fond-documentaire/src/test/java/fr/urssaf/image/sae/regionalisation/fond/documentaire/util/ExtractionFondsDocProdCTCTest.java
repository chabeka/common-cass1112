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
 * Elle contient des méthodes pour extraire une partie du fonds 
 * documentaire du SAE de PRODUCTION, pour des besoins sur CTC
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
@Ignore
public class ExtractionFondsDocProdCTCTest {

   private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh'h'mm ss's'");
   
   @Autowired
   private CassandraSupport cassandraSupport;
   
   @Autowired
   private DocInfoDao infoDao;
   
   @Autowired
   private Properties cassandraConf;
   
   
   private static String CHEMINREP = "c:/divers";
   private static String NOMFICHIER = "fonds_doc_prod_pour_ctc.csv";
   
   /**
    * Extraction des archives dont le contrat de service est "CS_CTC" 
    */
   @Test
   public void extraitFondsDocCTC() throws IOException {

      // Liste des métadonnées que l'on va lire
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.add("SM_BASE_ID");
      reqMetas.add("SM_UUID");
      reqMetas.add("cog");
      reqMetas.add("cop");
      reqMetas.add("nce");
      reqMetas.add("SM_ARCHIVAGE_DATE");
      reqMetas.add("srt");
      reqMetas.add("psi");
      reqMetas.add("cse");
      reqMetas.add("SM_DOCUMENT_TYPE");
      
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
         String contratService;
                  
         while (iterator.hasNext()) {
            map = iterator.next();
            
            idDoc = map.get("SM_UUID");
            cog = map.get("cog");
            nomBaseDfce = map.get("SM_BASE_ID");
            contratService = map.get("cse");
            
            // Vérifie que l'on se trouve bien sur un document
            // de la base documentaire attendu 
            if (
               StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue) && 
               StringUtils.isNotBlank(idDoc) && 
               StringUtils.isNotBlank(cog) &&
               StringUtils.equals(contratService, "CS_CTC")) {
            
               writer.write(idDoc);
               writer.write(";");
               writer.write(cog);
               writer.write(";");
               writer.write(map.get("cop"));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("nce")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("SM_ARCHIVAGE_DATE")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("srt")));
               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("psi")));
               writer.write(";");
//               writer.write(StringUtils.trimToEmpty(map.get("cse")));
//               writer.write(";");
               writer.write(StringUtils.trimToEmpty(map.get("SM_DOCUMENT_TYPE")));
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
   
   
   @Test
   public void ventilationParJour() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');
      
      String[] nextLine;
      
      int cptDoc = 0;
      
      Map<String,Integer> mapDtArchive = new HashMap<String,Integer>();
      
      String dtArchive;
      Integer nbParDate;
      
      while ((nextLine = reader.readNext()) != null) {
         
         dtArchive = nextLine[4].substring(0, 8);
         
         nbParDate = mapDtArchive.get(dtArchive);
         if (nbParDate==null) {
            mapDtArchive.put(dtArchive, 1);
         } else {
            mapDtArchive.put(dtArchive, nbParDate+1);
         }
            
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      
      System.out.println();
      System.out.println(printDate() + " - Ventilation par date d'archivage");
      
      Map<String, Integer> treeMap = new TreeMap<String, Integer>(mapDtArchive);
      for(Map.Entry<String, Integer> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   
   private String printDate() {
      
      return dateFormat.format(new Date());
      
   }
   
}
