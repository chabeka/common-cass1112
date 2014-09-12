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
public class VentilationFondsDocProdTest {

   private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh'h'mm ss's'");
   
   @Autowired
   private CassandraSupport cassandraSupport;
   
   @Autowired
   private DocInfoDao infoDao;
   
   @Autowired
   private Properties cassandraConf;
   
   
   private static String CHEMINREP = "c:/divers";
   private static String NOMFICHIER = "20140408_fonds_doc_prod_pour_ventilation.csv";
   private static String NOMFICHIERVENTIL = "20140408_ventilation_date_archivage.csv";
   
   @Test
   //@Ignore
   public void extraitFondsDoc() throws IOException {

      // Liste des métadonnées que l'on va lire
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.add("SM_BASE_ID");
      reqMetas.add("SM_UUID");
      reqMetas.add("cog");
      reqMetas.add("cop");
      reqMetas.add("nce");
      reqMetas.add("npe");
      reqMetas.add("nci");
      reqMetas.add("srt");
      reqMetas.add("psi");
      reqMetas.add("srn");
      reqMetas.add("apr");
      reqMetas.add("atr");
      reqMetas.add("cse");
      reqMetas.add("SM_ARCHIVAGE_DATE");
      reqMetas.add("SM_CREATION_DATE");
      reqMetas.add("SM_MODIFICATION_DATE");
      reqMetas.add("SM_DOCUMENT_TYPE");
      reqMetas.add("SM_TITLE");
      reqMetas.add("SM_SIZE");
      reqMetas.add("ffi");
      
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
            
            
            if ( 
                  StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue) && 
                  StringUtils.isNotBlank(idDoc) && 
                  StringUtils.isNotBlank(cog)) { 
               
               //0
               writer.write(idDoc);
               writer.write(";");
               //1
               writer.write(cog);
               writer.write(";");
               //2
               writer.write(map.get("cop"));
               writer.write(";");
               //3
               writer.write(StringUtils.trimToEmpty(map.get("nce")));
               writer.write(";");
               //4
               writer.write(StringUtils.trimToEmpty(map.get("nci")));
               writer.write(";");
               //5
               writer.write(StringUtils.trimToEmpty(map.get("npe")));
               writer.write(";");
               //6
               writer.write(StringUtils.trimToEmpty(map.get("srt")));
               writer.write(";");
               //7
               writer.write(StringUtils.trimToEmpty(map.get("psi")));
               writer.write(";");
               //8
               writer.write(StringUtils.trimToEmpty(map.get("srn")));
               writer.write(";");
               //9
               writer.write(StringUtils.trimToEmpty(map.get("apr")));
               writer.write(";");
               //10
               writer.write(StringUtils.trimToEmpty(map.get("atr")));
               writer.write(";");
               //11
               writer.write(StringUtils.trimToEmpty(map.get("cse")));
               writer.write(";");
               //12
               writer.write(StringUtils.trimToEmpty(map.get("SM_ARCHIVAGE_DATE")));
               writer.write(";");
               //13
               writer.write(StringUtils.trimToEmpty(map.get("SM_CREATION_DATE")));
               writer.write(";");
               //14
               writer.write(StringUtils.trimToEmpty(map.get("SM_MODIFICATION_DATE")));
               writer.write(";");
               //15
               writer.write(StringUtils.trimToEmpty(map.get("SM_DOCUMENT_TYPE")));
               writer.write(";");
               //16
               writer.write(StringUtils.trimToEmpty(map.get("SM_TITLE")));
               writer.write(";");
               //17
               writer.write(StringUtils.trimToEmpty(map.get("SM_SIZE")));
               writer.write("\n");
               //18
               writer.write(StringUtils.trimToEmpty(map.get("ffi")));
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
   
   
   /**
    * Récupère un nombre donné d'ID de document
    * @throws IOException
    */
   @Test
   public void extraitIdDoc() throws IOException {

      // Liste des métadonnées que l'on va lire
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.add("SM_BASE_ID");
      reqMetas.add("SM_UUID");
      reqMetas.add("SM_DOCUMENT_TYPE");
      
      // Nombre d'id Doc souhaités
      int nbIdDoc = 80000;
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
         String rnd;
         while (iterator.hasNext() && nbDocsSortis<nbIdDoc) {
            map = iterator.next();
            
            nomBaseDfce = map.get("SM_BASE_ID");
            idDoc = map.get("SM_UUID");
            rnd = map.get("SM_DOCUMENT_TYPE");
                       
            if (StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue) && StringUtils.isNotBlank(idDoc)) { 
               
               //0
               writer.write(idDoc);  
               writer.write(" / ");
               writer.write(rnd); 
               writer.write("\n");
               nbDocsSortis++;
               
            }
            
            nbDocsTraites++;
            if ((nbDocsTraites%1000)==0) {
               System.out.println("Nombre de docs traités : " + nbDocsTraites);
            }
            
         }
         
         System.out.println("Nombre total de docs traités : " + (nbDocsTraites));
         System.out.println("Nombre total de docs sortis dans le fichier : " + (nbDocsSortis));

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
   public void ventilation_ParTitre() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeRndEtTitre;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         codeRndEtTitre = nextLine[15] + ";" + nextLine[16] ;
         
         cptActuel = mapComptages.get(codeRndEtTitre);
         if (cptActuel==null) {
            mapComptages.put(codeRndEtTitre, 1L);
         } else {
            mapComptages.put(codeRndEtTitre, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   
   @Test
   public void ventilation_ParCodeRND() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeRnd;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         codeRnd = nextLine[15] ;
         
         cptActuel = mapComptages.get(codeRnd);
         if (cptActuel==null) {
            mapComptages.put(codeRnd, 1L);
         } else {
            mapComptages.put(codeRnd, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
 
   @Test
   public void ventilation_ParApplicationProductrice() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applTrait;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         applTrait = nextLine[9] ;
         
         cptActuel = mapComptages.get(applTrait);
         if (cptActuel==null) {
            mapComptages.put(applTrait, 1L);
         } else {
            mapComptages.put(applTrait, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   @Test
   public void ventilation_ParApplicationProductriceEtCodeRnd() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applTrait;
      String codeRnd;
      Long cptActuel;
      Map<String, Map<String,Long>> mapComptages = new HashMap<String, Map<String,Long>>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         applTrait = nextLine[9] ;
         codeRnd = nextLine[15] ;
         
         Map<String, Long> mapAppliTrait = mapComptages.get(applTrait);

         if (mapAppliTrait==null) {
            Map<String,Long> map = new HashMap<String,Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(applTrait, map);
         } else {
            cptActuel = mapAppliTrait.get(codeRnd);
         
            if (cptActuel==null) {
               mapAppliTrait.put(codeRnd, 1L);
            } else {
               mapAppliTrait.put(codeRnd, cptActuel+1);
               // mapComptages.put(applTrait, cptActuel+1);
            }
            mapComptages.put(applTrait, mapAppliTrait);
         }
         
        cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(mapComptages);
      
      for(Map.Entry<String,Map<String,Long>> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey());
         
         Map<String, Long> treeMapAppl = entry.getValue();
         
         for(Map.Entry<String,Long> entry2 : treeMapAppl.entrySet()) {
            System.out.println(entry2.getKey() + ";" + entry2.getValue());
         }
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   @Test
   public void ventilation_ParContratService() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String contratService;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         contratService = nextLine[11] ;
         
         cptActuel = mapComptages.get(contratService);
         if (cptActuel==null) {
            mapComptages.put(contratService, 1L);
         } else {
            mapComptages.put(contratService, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   @Test
   public void ventilation_ParContratServiceEtCodeRnd() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String contratService;
      String codeRnd;
      Long cptActuel;
      Map<String, Map<String,Long>> mapComptages = new HashMap<String, Map<String,Long>>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         contratService = nextLine[11] ;
         codeRnd = nextLine[15] ;
         
         Map<String, Long> mapAppliTrait = mapComptages.get(contratService);

         if (mapAppliTrait==null) {
            Map<String,Long> map = new HashMap<String,Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(contratService, map);
         } else {
            cptActuel = mapAppliTrait.get(codeRnd);
         
            if (cptActuel==null) {
               mapAppliTrait.put(codeRnd, 1L);
            } else {
               mapAppliTrait.put(codeRnd, cptActuel+1);
               // mapComptages.put(applTrait, cptActuel+1);
            }
            mapComptages.put(contratService, mapAppliTrait);
         }
         
        cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(mapComptages);
      
      for(Map.Entry<String,Map<String,Long>> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey());
         
         Map<String, Long> treeMapAppl = entry.getValue();
         
         for(Map.Entry<String,Long> entry2 : treeMapAppl.entrySet()) {
            System.out.println(entry2.getKey() + ";" + entry2.getValue());
         }
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   @Test
   public void ventilation_ParApplicationTraitement() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applProd;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         applProd = nextLine[10] ;
         
         cptActuel = mapComptages.get(applProd);
         if (cptActuel==null) {
            mapComptages.put(applProd, 1L);
         } else {
            mapComptages.put(applProd, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   @Test
   public void ventilation_ParApplicationProdEtCodeRnd() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applProd;
      String codeRnd;
      Long cptActuel;
      Map<String, Map<String,Long>> mapComptages = new HashMap<String, Map<String,Long>>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         applProd = nextLine[10] ;
         codeRnd = nextLine[15] ;
         
         Map<String, Long> mapAppliTrait = mapComptages.get(applProd);

         if (mapAppliTrait==null) {
            Map<String,Long> map = new HashMap<String,Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(applProd, map);
         } else {
            cptActuel = mapAppliTrait.get(codeRnd);
         
            if (cptActuel==null) {
               mapAppliTrait.put(codeRnd, 1L);
            } else {
               mapAppliTrait.put(codeRnd, cptActuel+1);
               // mapComptages.put(applTrait, cptActuel+1);
            }
            mapComptages.put(applProd, mapAppliTrait);
         }
         
        cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(mapComptages);
      
      for(Map.Entry<String,Map<String,Long>> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey());
         
         Map<String, Long> treeMapAppl = entry.getValue();
         
         for(Map.Entry<String,Long> entry2 : treeMapAppl.entrySet()) {
            System.out.println(entry2.getKey() + ";" + entry2.getValue());
         }
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
 
   
   @Test
   public void ventilation_ParApplicationTraitementEtProductrice() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applProdEtTrait;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         applProdEtTrait = nextLine[9] + ";" +  nextLine[10];
         
         cptActuel = mapComptages.get(applProdEtTrait);
         if (cptActuel==null) {
            mapComptages.put(applProdEtTrait, 1L);
         } else {
            mapComptages.put(applProdEtTrait, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   @Test
   public void ventilation_ParApplicationProdEtApplicationTraitEtCodeRnd() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applProdEtTrait;

      String codeRnd;
      Long cptActuel;
      Map<String, Map<String,Long>> mapComptages = new HashMap<String, Map<String,Long>>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         applProdEtTrait = nextLine[9] + ";" + nextLine[10];
         codeRnd = nextLine[15] ;
         
         Map<String, Long> mapAppliTraitEtProd = mapComptages.get(applProdEtTrait);

         if (mapAppliTraitEtProd==null) {
            Map<String,Long> map = new HashMap<String,Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(applProdEtTrait, map);
         } else {
            cptActuel = mapAppliTraitEtProd.get(codeRnd);
         
            if (cptActuel==null) {
               mapAppliTraitEtProd.put(codeRnd, 1L);
            } else {
               mapAppliTraitEtProd.put(codeRnd, cptActuel+1);
               // mapComptages.put(applTrait, cptActuel+1);
            }
            mapComptages.put(applProdEtTrait, mapAppliTraitEtProd);
         }
         
        cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(mapComptages);
      
      for(Map.Entry<String,Map<String,Long>> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey());
         
         Map<String, Long> treeMapAppl = entry.getValue();
         
         for(Map.Entry<String,Long> entry2 : treeMapAppl.entrySet()) {
            System.out.println(entry2.getKey() + ";" + entry2.getValue());
         }
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }

   
   @Test
   public void ventilation_ParCodeOrganismeProprietaire() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeOrgaProp;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         codeOrgaProp = nextLine[2] ;
         
         cptActuel = mapComptages.get(codeOrgaProp);
         if (cptActuel==null) {
            mapComptages.put(codeOrgaProp, 1L);
         } else {
            mapComptages.put(codeOrgaProp, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   
   @Test
   public void ventilation_ParCodeOrganismeGestionnaire() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeOrgaProp;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         codeOrgaProp = nextLine[1] ;
         
         cptActuel = mapComptages.get(codeOrgaProp);
         if (cptActuel==null) {
            mapComptages.put(codeOrgaProp, 1L);
         } else {
            mapComptages.put(codeOrgaProp, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   @Test
   public void ventilation_ParDateArchivage() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
      Writer writer = new FileWriter(fichier);
      
      String[] nextLine;
      int cptDoc = 0;
      String dateArchivage;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         dateArchivage = StringUtils.left(nextLine[12],8);
         
         cptActuel = mapComptages.get(dateArchivage);
         if (cptActuel==null) {
            mapComptages.put(dateArchivage, 1L);
         } else {
            mapComptages.put(dateArchivage, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
         //System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      closeWriter(writer);
   
      System.out.println();
      System.out.println("Opération terminée");
      
   }
 
   
   @Test
   public void ventilation_ParMoisArchivage() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
      Writer writer = new FileWriter(fichier);
      
      String[] nextLine;
      int cptDoc = 0;
      String dateArchivage;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         dateArchivage = StringUtils.left(nextLine[12],6);
         
         cptActuel = mapComptages.get(dateArchivage);
         if (cptActuel==null) {
            mapComptages.put(dateArchivage, 1L);
         } else {
            mapComptages.put(dateArchivage, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
         //System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      closeWriter(writer);
   
      System.out.println();
      System.out.println("Opération terminée");
      
   }
 
   @Test
   public void ventilation_ParMoisDateModification() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
      Writer writer = new FileWriter(fichier);
      
      String[] nextLine;
      int cptDoc = 0;
      String dateModification;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         dateModification = StringUtils.left(nextLine[14],6);
         
         cptActuel = mapComptages.get(dateModification);
         if (cptActuel==null) {
            mapComptages.put(dateModification, 1L);
         } else {
            mapComptages.put(dateModification, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
         //System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      closeWriter(writer);
   
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   @Test
   public void ventilation_ParMoisDateCreation() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
      Writer writer = new FileWriter(fichier);
      
      String[] nextLine;
      int cptDoc = 0;
      String dateCreation;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         dateCreation = StringUtils.left(nextLine[13],6);
         
         cptActuel = mapComptages.get(dateCreation);
         if (cptActuel==null) {
            mapComptages.put(dateCreation, 1L);
         } else {
            mapComptages.put(dateCreation, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
         //System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      closeWriter(writer);
   
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   @Test
   public void ventilation_ParMoisArchivageAppProdAppTrait() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
      Writer writer = new FileWriter(fichier);
      
      String[] nextLine;
      int cptDoc = 0;
      String appProdAppTraitDateArchivage;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         
         appProdAppTraitDateArchivage = nextLine[9] + ";" + nextLine[10] + ";" + StringUtils.left(nextLine[12],6);
         
         cptActuel = mapComptages.get(appProdAppTraitDateArchivage);
         if (cptActuel==null) {
            mapComptages.put(appProdAppTraitDateArchivage, 1L);
         } else {
            mapComptages.put(appProdAppTraitDateArchivage, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
         //System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      closeWriter(writer);
   
      System.out.println();
      System.out.println("Opération terminée");
      
   }

   // Récupère pour chaque couple AppTrait/AppProd la date du 1er archivage dans le SAE
   @Test
   public void ventilation_ParDateArchivageEtAppTraitEtAppProd() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
      Writer writer = new FileWriter(fichier);
      
      String[] nextLine;
      int cptDoc = 0;
      String appProdAppTraitdateArchivage;
      String dateArchivage;
      String  dateMin;
      Map<String, String> mapDateDebut = new HashMap<String, String>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         appProdAppTraitdateArchivage = nextLine[9] + ";" + nextLine[10];
         dateArchivage = StringUtils.left(nextLine[12],8);
         dateMin = mapDateDebut.get(appProdAppTraitdateArchivage);
         if(StringUtils.isEmpty(dateMin)) {
            mapDateDebut.put(appProdAppTraitdateArchivage, dateArchivage);
         } else {
            if (dateArchivage.compareTo(dateMin) < 0) {
               mapDateDebut.put(appProdAppTraitdateArchivage, dateArchivage);
            }
         }

         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      System.out.println();
      
      Map<String, String> treeMap = new TreeMap<String, String>(mapDateDebut);
      for(Map.Entry<String, String> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
         //System.out.println(entry.getKey() + ";" + entry.getValue());
      }
      
      closeWriter(writer);
   
      System.out.println();
      System.out.println("Opération terminée");
      
   }
   
   

   
}
