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
 * Elle contient des méthodes pour extraire le fonds documentaire du SAE de
 * PRODUCTION, et ventiler ce fonds sur différents critères
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
//@Ignore
public class VentilationFondsDocProdTest {

   private DateFormat dateFormat = new SimpleDateFormat(
         "dd/MM/yyyy hh'h'mm ss's'");

   @Autowired
   private CassandraSupport cassandraSupport;

   @Autowired
   private DocInfoDao infoDao;

   @Autowired
   private Properties cassandraConf;

   private static String CHEMINREPGNS = "c:/sav/GNS";
   private static String NOMFICHIERGNS = "extraction-GNS.csv";
 
   private static String CHEMINREPGNT = "c:/sav/GNT";
   private static String NOMFICHIERGNT = "extraction-GNT.csv";

   private static String CHEMINREP = "c:/divers";

   private static String NOMFICHIER = "20170217-fonds_doc_prod_pour_ventilation.csv";
   private static String NOMFICHIERID = "20170213_fonds_doc_id.csv";
   private static String NOMFICHIERVENTILPARDATEARCHIVAGE = "20170213_ventilation_date_archivage.csv";
   private static String NOMFICHIERVENTILPARMOISARCHIVAGE = "20170213_ventilation_mois_archivage.csv";
   private static String NOMFICHIERVENTILPARMOISDATEMODIFICATION = "20170213_ventilation_mois_modification.csv";
   private static String NOMFICHIERVENTILPARMOISCREATION = "20170213_ventilation_mois_creation.csv";
   private static String NOMFICHIERVENTILPARMOISARCHIVAGEAPPPRODAPPTRAIT = "20170213_ventilation_mois_archivage_productrive_traitement.csv";
   private static String NOMFICHIERVENTILPARDATEARCHIVAGEETAPPTRAITETAPPPROD = "20170213_ventilation_date_archivage_traitement_productrive.csv";
   

   @Test
   // @Ignore
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

      reqMetas.add("ame");
      reqMetas.add("cpr");
      reqMetas.add("ctr");
      reqMetas.add("nno");
      
      reqMetas.add("cot");
      reqMetas.add("cpt");
      reqMetas.add("drh");
      reqMetas.add("dte");
      reqMetas.add("drs");

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

               // 0 : UUID document
               writer.write(idDoc);
               writer.write(";");

               //1 CodeOrganismeGestionnaire
               writer.write(cog);
               writer.write(";");
               //2 CodeOrganismeProprietaire
               writer.write(map.get("cop"));
               writer.write(";");
               //3 NumeroCompteExterne
               writer.write(StringUtils.trimToEmpty(map.get("nce")));
               writer.write(";");
               //4 NumeroCompteInterne
               writer.write(StringUtils.trimToEmpty(map.get("nci")));
               writer.write(";");
               //5 NumeroPersonne
               writer.write(StringUtils.trimToEmpty(map.get("npe")));
               writer.write(";");
               //6 Siret
               writer.write(StringUtils.trimToEmpty(map.get("srt")));
               writer.write(";");
               //7 PseudoSiret
               writer.write(StringUtils.trimToEmpty(map.get("psi")));
               writer.write(";");
               //8 Siren
               writer.write(StringUtils.trimToEmpty(map.get("srn")));
               writer.write(";");
               //9 ApplicationProductrice
               writer.write(StringUtils.trimToEmpty(map.get("apr")));
               writer.write(";");
               //10 ApplicationTraitement
               writer.write(StringUtils.trimToEmpty(map.get("atr")));
               writer.write(";");
               //11 ContratDeService
               writer.write(StringUtils.trimToEmpty(map.get("cse")));
               writer.write(";");
               //12 DateArchivage
               writer.write(StringUtils.trimToEmpty(map.get("SM_ARCHIVAGE_DATE")));
               writer.write(";");
               //13 DateCreation
               writer.write(StringUtils.trimToEmpty(map.get("SM_CREATION_DATE")));
               writer.write(";");
               //14 DateModification
               writer.write(StringUtils.trimToEmpty(map.get("SM_MODIFICATION_DATE")));
               writer.write(";");
               //15 CodeRND
               writer.write(StringUtils.trimToEmpty(map.get("SM_DOCUMENT_TYPE")));
               writer.write(";");
               //16 Titre
               writer.write(StringUtils.trimToEmpty(map.get("SM_TITLE")));
               writer.write(";");
               //17 TailleFichier
               writer.write(StringUtils.trimToEmpty(map.get("SM_SIZE")));
               writer.write(";");
               //18 FormatFichier
               writer.write(StringUtils.trimToEmpty(map.get("ffi")));
               writer.write(";");
               //19 ApplicationMetier
               writer.write(StringUtils.trimToEmpty(map.get("ame")));
               writer.write(";");
              //20 CodeTraitementV2
               writer.write(StringUtils.trimToEmpty(map.get("ctr")));
               writer.write(";");                              
               //21 CodeProduitV2
               writer.write(StringUtils.trimToEmpty(map.get("cpr")));
               writer.write(";");               
               //22 NumeroNotification
               writer.write(StringUtils.trimToEmpty(map.get("nno")));
               writer.write(";");

               //23 DomaineCotisant                
               writer.write(StringUtils.trimToEmpty(map.get("cot")));
               writer.write(";");               
               //24 DomaineComptable   
               writer.write(StringUtils.trimToEmpty(map.get("cpt")));
               writer.write(";");               
               //25 DomaineRH  
               writer.write(StringUtils.trimToEmpty(map.get("drh")));
               writer.write(";");               
               //26 DomaineTechnique   
               writer.write(StringUtils.trimToEmpty(map.get("dte")));
               writer.write(";");               
               //27 DomaineRSI   
               writer.write(StringUtils.trimToEmpty(map.get("drs")));
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
   /**
    * Récupère un nombre donné d'ID de document
    * 
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
         while (iterator.hasNext() && nbDocsSortis < nbIdDoc) {
            map = iterator.next();

            nomBaseDfce = map.get("SM_BASE_ID");
            idDoc = map.get("SM_UUID");
            rnd = map.get("SM_DOCUMENT_TYPE");

            if (StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue)
                  && StringUtils.isNotBlank(idDoc)) {

               // 0
               writer.write(idDoc);
               writer.write(" / ");
               writer.write(rnd);
               writer.write("\n");
               nbDocsSortis++;

            }

            nbDocsTraites++;
            if ((nbDocsTraites % 1000) == 0) {
               System.out.println("Nombre de docs traités : " + nbDocsTraites);
            }

         }

         System.out
               .println("Nombre total de docs traités : " + (nbDocsTraites));
         System.out.println("Nombre total de docs sortis dans le fichier : "
               + (nbDocsSortis));

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

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeRndEtTitre;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         codeRndEtTitre = nextLine[15] + ";" + nextLine[16];

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

      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParFormatFichier() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String formatFichier;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         formatFichier = nextLine[18];

         cptActuel = mapComptages.get(formatFichier);
         if (cptActuel == null) {
            mapComptages.put(formatFichier, 1L);
         } else {
            mapComptages.put(formatFichier, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParCodeRND() throws IOException {

      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeRnd;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         codeRnd = nextLine[15];

         cptActuel = mapComptages.get(codeRnd);
         if (cptActuel == null) {
            mapComptages.put(codeRnd, 1L);
         } else {
            mapComptages.put(codeRnd, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }
   
   @Test
   public void ventilation_ParCodeRND_APR_ATR_CPR_CTR_CMO() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeRnd;
      Long cptActuel;
     
      Map<String, Long> mapComptages = new HashMap<String, Long>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         //CodeRND 15 Titre 16 AppliProductrice 9 AppliTraitement 10 Contratdeservice 11 traitementV2 18 produit V2 19 
         codeRnd = nextLine[15] + ";" + nextLine[16]+ ";" + nextLine[9] + ";" + nextLine[10]+ ";" + nextLine[11] + ";" + nextLine[18] +  ";" + nextLine[19] + ";" + nextLine[26] +  ";" + nextLine[25];       
         
         cptActuel = mapComptages.get(codeRnd);
         if (cptActuel==null) {
            mapComptages.put(codeRnd, 1L);
         } else {
            mapComptages.put(codeRnd, cptActuel+1);
         }
         
         cptDoc++;
         if ((cptDoc % 30000)==0) {
            System.out.println(printDate() + " ventilation_ParCodeRND_APR_ATR_CPR_CTR - Nombre de documents traités : " + cptDoc);
         }
         
      }
      
      System.out.println(printDate() + " ventilation_ParCodeRND_APR_ATR_CPR_CTR - Nombre de documents traités : " + cptDoc);
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

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applTrait;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         applTrait = nextLine[9];

         cptActuel = mapComptages.get(applTrait);
         if (cptActuel == null) {
            mapComptages.put(applTrait, 1L);
         } else {
            mapComptages.put(applTrait, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParApplicationProductriceEtCodeRnd()
         throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applTrait;
      String codeRnd;
      Long cptActuel;
      Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
      while ((nextLine = reader.readNext()) != null) {

         applTrait = nextLine[9];
         codeRnd = nextLine[15];

         Map<String, Long> mapAppliTrait = mapComptages.get(applTrait);

         if (mapAppliTrait == null) {
            Map<String, Long> map = new HashMap<String, Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(applTrait, map);
         } else {
            cptActuel = mapAppliTrait.get(codeRnd);

            if (cptActuel == null) {
               mapAppliTrait.put(codeRnd, 1L);
            } else {
               mapAppliTrait.put(codeRnd, cptActuel + 1);
               // mapComptages.put(applTrait, cptActuel+1);
            }
            mapComptages.put(applTrait, mapAppliTrait);
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

      Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
            mapComptages);

      for (Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey());

         Map<String, Long> treeMapAppl = entry.getValue();

         for (Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
            System.out.println(entry2.getKey() + ";" + entry2.getValue());
         }
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParContratService() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String contratService;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         contratService = nextLine[11];

         cptActuel = mapComptages.get(contratService);
         if (cptActuel == null) {
            mapComptages.put(contratService, 1L);
         } else {
            mapComptages.put(contratService, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParContratServiceEtCodeRnd() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String contratService;
      String codeRnd;
      Long cptActuel;
      Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
      while ((nextLine = reader.readNext()) != null) {

         contratService = nextLine[11];
         codeRnd = nextLine[15];

         Map<String, Long> mapAppliTrait = mapComptages.get(contratService);

         if (mapAppliTrait == null) {
            Map<String, Long> map = new HashMap<String, Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(contratService, map);
         } else {
            cptActuel = mapAppliTrait.get(codeRnd);

            if (cptActuel == null) {
               mapAppliTrait.put(codeRnd, 1L);
            } else {
               mapAppliTrait.put(codeRnd, cptActuel + 1);
               // mapComptages.put(applTrait, cptActuel+1);
            }
            mapComptages.put(contratService, mapAppliTrait);
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

      Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
            mapComptages);

      for (Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey());

         Map<String, Long> treeMapAppl = entry.getValue();

         for (Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
            System.out.println(entry2.getKey() + ";" + entry2.getValue());
         }
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParContratServiceAppliProdAppliTraitEtCodeRnd()
         throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String CSApplProdEtTrait;
      String codeRnd;
      Long cptActuel;
      Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
      while ((nextLine = reader.readNext()) != null) {
         CSApplProdEtTrait = nextLine[11] + ";" + nextLine[9] + ";"
               + nextLine[10];
         ;
         codeRnd = nextLine[15];

         Map<String, Long> mapAppliTrait = mapComptages.get(CSApplProdEtTrait);

         if (mapAppliTrait == null) {
            Map<String, Long> map = new HashMap<String, Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(CSApplProdEtTrait, map);
         } else {
            cptActuel = mapAppliTrait.get(codeRnd);

            if (cptActuel == null) {
               mapAppliTrait.put(codeRnd, 1L);
            } else {
               mapAppliTrait.put(codeRnd, cptActuel + 1);
               // mapComptages.put(applTrait, cptActuel+1);
            }
            mapComptages.put(CSApplProdEtTrait, mapAppliTrait);
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

      Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
            mapComptages);

      for (Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey());

         Map<String, Long> treeMapAppl = entry.getValue();

         for (Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
            System.out.println(entry2.getKey() + ";" + entry2.getValue());
         }
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParApplicationTraitement() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applProd;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         applProd = nextLine[10];

         cptActuel = mapComptages.get(applProd);
         if (cptActuel == null) {
            mapComptages.put(applProd, 1L);
         } else {
            mapComptages.put(applProd, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParApplicationProdEtCodeRnd() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applProd;
      String codeRnd;
      Long cptActuel;
      Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
      while ((nextLine = reader.readNext()) != null) {

         applProd = nextLine[10];
         codeRnd = nextLine[15];

         Map<String, Long> mapAppliTrait = mapComptages.get(applProd);

         if (mapAppliTrait == null) {
            Map<String, Long> map = new HashMap<String, Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(applProd, map);
         } else {
            cptActuel = mapAppliTrait.get(codeRnd);

            if (cptActuel == null) {
               mapAppliTrait.put(codeRnd, 1L);
            } else {
               mapAppliTrait.put(codeRnd, cptActuel + 1);
               // mapComptages.put(applTrait, cptActuel+1);
            }
            mapComptages.put(applProd, mapAppliTrait);
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

      Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
            mapComptages);

      for (Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey());

         Map<String, Long> treeMapAppl = entry.getValue();

         for (Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
            System.out.println(entry2.getKey() + ";" + entry2.getValue());
         }
      }

      System.out.println();
      System.out.println("Opération terminée");

   }
   @Test
   public void ventilation_ParApplicationTraitementEtProductrice()
         throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applProdEtTrait;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         applProdEtTrait = nextLine[9] + ";" + nextLine[10];

         cptActuel = mapComptages.get(applProdEtTrait);
         if (cptActuel == null) {
            mapComptages.put(applProdEtTrait, 1L);
         } else {
            mapComptages.put(applProdEtTrait, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParApplicationProdEtApplicationTraitEtCodeRnd()
         throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applProdEtTrait;

      String codeRnd;
      Long cptActuel;
      Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
      while ((nextLine = reader.readNext()) != null) {

         applProdEtTrait = nextLine[9] + ";" + nextLine[10];
         codeRnd = nextLine[15];

         Map<String, Long> mapAppliTraitEtProd = mapComptages
               .get(applProdEtTrait);

   
         if (mapAppliTraitEtProd==null) {
            Map<String,Long> map = new HashMap<String,Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(applProdEtTrait, map);
         } else {
            cptActuel = mapAppliTraitEtProd.get(codeRnd);

            if (cptActuel == null) {
               mapAppliTraitEtProd.put(codeRnd, 1L);
            } else {
               mapAppliTraitEtProd.put(codeRnd, cptActuel + 1);
               // mapComptages.put(applTrait, cptActuel+1);
            }
            mapComptages.put(applProdEtTrait, mapAppliTraitEtProd);
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

      Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
            mapComptages);

      for (Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey());

         Map<String, Long> treeMapAppl = entry.getValue();

         for (Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
            System.out.println(entry2.getKey() + ";" + entry2.getValue());
         }
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParApplicationProdEtApplicationTraitEtCodeRnd_CPR_CTR_CMO() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String applProdEtTraitCPRCTR;

      String codeRnd;
      Long cptActuel;
      Map<String, Map<String,Long>> mapComptages = new HashMap<String, Map<String,Long>>(); 
      while ((nextLine = reader.readNext()) != null) {
         
         applProdEtTraitCPRCTR = nextLine[9] + ";" + nextLine[10] + ";" + nextLine[21] + ";" + nextLine[20];
         codeRnd = nextLine[15] ;
         
         Map<String, Long> mapAppliTraitEtProdCPRCTR = mapComptages.get(applProdEtTraitCPRCTR);

         if (mapAppliTraitEtProdCPRCTR==null) {
            Map<String,Long> map = new HashMap<String,Long>();
            map.put(codeRnd, 1L);
            mapComptages.put(applProdEtTraitCPRCTR, map);
         } else {
            cptActuel = mapAppliTraitEtProdCPRCTR.get(codeRnd);
         
            if (cptActuel==null) {
               mapAppliTraitEtProdCPRCTR.put(codeRnd, 1L);
            } else {
               mapAppliTraitEtProdCPRCTR.put(codeRnd, cptActuel+1);
            }
            mapComptages.put(applProdEtTraitCPRCTR, mapAppliTraitEtProdCPRCTR);
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

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeOrgaProp;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         codeOrgaProp = nextLine[2];

         cptActuel = mapComptages.get(codeOrgaProp);
         if (cptActuel == null) {
            mapComptages.put(codeOrgaProp, 1L);
         } else {
            mapComptages.put(codeOrgaProp, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParCodeOrganismeGestionnaire() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeOrgaGest;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         codeOrgaGest = nextLine[1];

         cptActuel = mapComptages.get(codeOrgaGest);
         if (cptActuel == null) {
            mapComptages.put(codeOrgaGest, 1L);
         } else {
            mapComptages.put(codeOrgaGest, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParContratServiceCodeOrganismeGestionnaireDateArchivage()
         throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeOrgaGest;
      String contratService;
      String dateArchivage;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();

      File fichier = new File(CHEMINREP, NOMFICHIER_CODE_ORGA_GEST);
      Writer writer = new FileWriter(fichier);

      while ((nextLine = reader.readNext()) != null) {

         codeOrgaGest = nextLine[1];
         contratService = nextLine[11];
         dateArchivage = StringUtils.left(nextLine[12], 6);

         cptActuel = mapComptages.get(contratService + ";" + codeOrgaGest + ";"
               + dateArchivage);

         if (cptActuel == null) {
            mapComptages.put(contratService + ";" + codeOrgaGest + ";"
                  + dateArchivage, 1L);
         } else {
            mapComptages.put(contratService + ";" + codeOrgaGest + ";"
                  + dateArchivage, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         // System.out.println(entry.getKey() + ";" + entry.getValue());
         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
      }

      reader.close();
      writer.close();

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParContratServiceCodeOrganismePropDateArchivage()
         throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeOrgaProp;
      String contratService;
      String dateArchivage;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();

      File fichier = new File(CHEMINREP, NOMFICHIER_CODE_ORGA_PROP);
      Writer writer = new FileWriter(fichier);

      while ((nextLine = reader.readNext()) != null) {

         codeOrgaProp = nextLine[2];
         contratService = nextLine[11];
         dateArchivage = StringUtils.left(nextLine[12], 6);

         cptActuel = mapComptages.get(contratService + ";" + codeOrgaProp + ";"
               + dateArchivage);

         if (cptActuel == null) {
            mapComptages.put(contratService + ";" + codeOrgaProp + ";"
                  + dateArchivage, 1L);
         } else {
            mapComptages.put(contratService + ";" + codeOrgaProp + ";"
                  + dateArchivage, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         // System.out.println(entry.getKey() + ";" + entry.getValue());
         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
      }

      reader.close();
      writer.close();

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParDateArchivage() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTILPARDATEARCHIVAGE);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String dateArchivage;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         dateArchivage = StringUtils.left(nextLine[12], 6);

         cptActuel = mapComptages.get(dateArchivage);
         if (cptActuel == null) {
            mapComptages.put(dateArchivage, 1L);
         } else {
            mapComptages.put(dateArchivage, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
         // System.out.println(entry.getKey() + ";" + entry.getValue());

      }

      closeWriter(writer);

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParMoisArchivage() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTILPARMOISARCHIVAGE);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String dateArchivage;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         dateArchivage = StringUtils.left(nextLine[12], 6);

         cptActuel = mapComptages.get(dateArchivage);
         if (cptActuel == null) {
            mapComptages.put(dateArchivage, 1L);
         } else {
            mapComptages.put(dateArchivage, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
         // System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      closeWriter(writer);

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParMoisDateModification() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTILPARMOISDATEMODIFICATION);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String dateModification;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         dateModification = StringUtils.left(nextLine[14], 6);

         cptActuel = mapComptages.get(dateModification);
         if (cptActuel == null) {
            mapComptages.put(dateModification, 1L);
         } else {
            mapComptages.put(dateModification, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
         // System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      closeWriter(writer);

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParMoisDateCreation() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTILPARMOISCREATION);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String dateCreation;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         dateCreation = StringUtils.left(nextLine[13], 6);

         cptActuel = mapComptages.get(dateCreation);
         if (cptActuel == null) {
            mapComptages.put(dateCreation, 1L);
         } else {
            mapComptages.put(dateCreation, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
         // System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      closeWriter(writer);

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParMoisArchivageAppProdAppTrait() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTILPARMOISARCHIVAGEAPPPRODAPPTRAIT);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String appProdAppTraitDateArchivage;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         appProdAppTraitDateArchivage = nextLine[9] + ";" + nextLine[10] + ";"
               + StringUtils.left(nextLine[12], 6);

         cptActuel = mapComptages.get(appProdAppTraitDateArchivage);
         if (cptActuel == null) {
            mapComptages.put(appProdAppTraitDateArchivage, 1L);
         } else {
            mapComptages.put(appProdAppTraitDateArchivage, cptActuel + 1);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
         // System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      closeWriter(writer);

      System.out.println();
      System.out.println("Opération terminée");

   }

   // Récupère pour chaque couple AppTrait/AppProd la date du 1er archivage dans
   // le SAE
   @Test
   public void ventilation_ParDateArchivageEtAppTraitEtAppProd()
         throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTILPARDATEARCHIVAGEETAPPTRAITETAPPPROD);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String appProdAppTraitdateArchivage;
      String dateArchivage;
      String dateMin;
      Map<String, String> mapDateDebut = new HashMap<String, String>();
      while ((nextLine = reader.readNext()) != null) {

         appProdAppTraitdateArchivage = nextLine[9] + ";" + nextLine[10];
         dateArchivage = StringUtils.left(nextLine[12], 8);
         dateMin = mapDateDebut.get(appProdAppTraitdateArchivage);
         if (StringUtils.isEmpty(dateMin)) {
            mapDateDebut.put(appProdAppTraitdateArchivage, dateArchivage);
         } else {
            if (dateArchivage.compareTo(dateMin) < 0) {
               mapDateDebut.put(appProdAppTraitdateArchivage, dateArchivage);
            }
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

      Map<String, String> treeMap = new TreeMap<String, String>(mapDateDebut);
      for (Map.Entry<String, String> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
         // System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      closeWriter(writer);

      System.out.println();
      System.out.println("Opération terminée");

   }

   // Récupère pour chaque couple CS/AppTrait/AppProd et code RND la date du 1er
   // archivage dans
   // le SAE
   @Test
   public void ventilation_ParDateArchivageEtAppTraitEtAppProdEtCodeRnd()
         throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String appProdAppTraitdateArchivageRnd;
      String dateArchivage;
      String dateMin;
      Long cptActuel;
      Map<String, Long> mapDateDebut = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         appProdAppTraitdateArchivageRnd = nextLine[11] + ";" + nextLine[9]
               + ";" + nextLine[10] + ";" + nextLine[15] + ";"
               + StringUtils.left(nextLine[12], 8);

         // if (appProdAppTraitdateArchivageRnd
         // .equals("CS_ANCIEN_SYSTEME;ADELAIDE;SATURNE;2.1.2.5.9;20141125")) {
         // dateArchivage = StringUtils.left(nextLine[12], 4);
         // if (dateArchivage.equals("2015")) {
         // System.out.println(StringUtils.left(nextLine[12], 8));
         // }
         // }

         dateArchivage = StringUtils.left(nextLine[12], 8);

         cptActuel = mapDateDebut.get(appProdAppTraitdateArchivageRnd);
         if (cptActuel == null) {
            mapDateDebut.put(appProdAppTraitdateArchivageRnd, 1L);
         } else {

            mapDateDebut.put(appProdAppTraitdateArchivageRnd, cptActuel + 1);

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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapDateDebut);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
         // System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      closeWriter(writer);

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParDomaine() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      File fichier = new File(CHEMINREP, NOMFICHIER_DOC_SANS_DOMAINE);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String domaineCotisant;
      String domaineCompta;
      String domaineRH;
      String domaineTechnique;

      String uuidDoc;

      Long cptCotisant;
      Long cptCompta;
      Long cptRH;
      Long cptTechnique;
      Long cptAucunDomaine;

      Map<String, Long> mapComptages = new HashMap<String, Long>();

      while ((nextLine = reader.readNext()) != null) {

         domaineCotisant = nextLine[19];
         domaineCompta = nextLine[20];
         domaineRH = nextLine[21];
         domaineTechnique = nextLine[22];
         uuidDoc = nextLine[0];

         if (domaineCotisant.equals("true")) {
            cptCotisant = mapComptages.get("domaineCotisant");
            if (cptCotisant == null) {
               mapComptages.put("domaineCotisant", 1L);
            } else {
               mapComptages.put("domaineCotisant", cptCotisant + 1);
            }
         } else if (domaineCompta.equals("true")) {
            cptCompta = mapComptages.get("domaineCompta");
            if (cptCompta == null) {
               mapComptages.put("domaineCompta", 1L);
            } else {
               mapComptages.put("domaineCompta", cptCompta + 1);
            }
         } else if (domaineRH.equals("true")) {
            cptRH = mapComptages.get("domaineRH");
            if (cptRH == null) {
               mapComptages.put("domaineRH", 1L);
            } else {
               mapComptages.put("domaineRH", cptRH + 1);
            }
         } else if (domaineTechnique.equals("true")) {
            cptTechnique = mapComptages.get("domaineTechnique");
            if (cptTechnique == null) {
               mapComptages.put("domaineTechnique", 1L);
            } else {
               mapComptages.put("domaineTechnique", cptTechnique + 1);
            }
         } else {
            writer.write(uuidDoc + "\n");
            cptAucunDomaine = mapComptages.get("aucunDomaine");
            if (cptAucunDomaine == null) {
               mapComptages.put("aucunDomaine", 1L);
            } else {
               mapComptages.put("aucunDomaine", cptAucunDomaine + 1);
            }
         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }

      }

      closeWriter(writer);

      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }
   

   @Test
   public void ventilation_DateArchivageMin_nno() throws IOException {
      
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');
      
      // Création du répertoire de sortie s'il n'existe pas déjà
      //File rep = new File(CHEMINREPGNS);
      File rep = new File(CHEMINREPGNT);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      //File fichier = new File(CHEMINREPGNS, "GNS-DateArchivageMin-nno.csv");
      File fichier = new File(CHEMINREPGNT, "GNT-DateArchivageMin-nno.csv");
      Writer writer = new FileWriter(fichier);
      
   String[] nextLine;
   int cptDoc = 0;
   String RNDappProdAppTraitdateArchivage;
   String dateArchivage;
   String  dateMin;
   Map<String, String> mapDateDebut = new HashMap<String, String>();
     
   while ((nextLine = reader.readNext()) != null) {
      
    RNDappProdAppTraitdateArchivage = nextLine[15] + ";" + nextLine[16] + ";" + nextLine[9] + ";" + nextLine[10]  + ";" + nextLine[11] + ";" + nextLine[20] +  ";" + nextLine[21] +  ";" + nextLine[19] +  ";" + nextLine[22];
    dateArchivage = StringUtils.left(nextLine[12],8);

      
      dateMin = mapDateDebut.get(RNDappProdAppTraitdateArchivage);
      if(StringUtils.isEmpty(dateMin)) {
         mapDateDebut.put(RNDappProdAppTraitdateArchivage, dateArchivage);
      } else {
         if (dateArchivage.compareTo(dateMin) < 0) {
            mapDateDebut.put(RNDappProdAppTraitdateArchivage, dateArchivage);
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
   }
   

   /**
    * Ecrit dans un fichier la liste des documents dont le format de fichier est
    * égal à PDF
    * 
    * @throws IOException
    */
   @Test
   public void ventilation_ParFormatEgalPDF() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      File fichier = new File(CHEMINREP, NOMFICHIER_FORMAT_PDF);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String dateArchi;
      String appliProd;
      String appliTrait;
      String contratService;
      String format;

      String uuidDoc;

      Long cptFormatPDF = 0L;

      while ((nextLine = reader.readNext()) != null) {

         dateArchi = nextLine[12];
         appliProd = nextLine[9];
         appliTrait = nextLine[10];
         contratService = nextLine[11];
         format = nextLine[18];
         uuidDoc = nextLine[0];

         if (format.equals("PDF")) {
            // if (appliProd.equals("SCRIBE") &&
            // contratService.equals("CS_ANCIEN_SYSTEME")) {
            cptFormatPDF++;

            writer.write(uuidDoc + ";" + dateArchi + ";" + appliProd + ";"
                  + appliTrait + ";" + contratService + ";" + format + "\n");

         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }

      }

      closeWriter(writer);

      System.out.println(printDate()
            + " - Nombre de documents avec format = PDF :" + cptFormatPDF);
      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

   closeWriter(writer);

      System.out.println();
      System.out.println("Opération terminée");

   }

   /**
    * Ecrit dans un fichier les documents dont la date de réception est
    * renseignée
    * 
    * @throws IOException
    */
   @Test
   public void rechercheDocAvecDateReception() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      File fichier = new File(CHEMINREP, NOMFICHIER_DOC_AVEC_DATE_RECEPTION);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String dateReception;
      String numeroCompte;
      String siret;

      String uuidDoc;

      Long cpt = 0L;

      while ((nextLine = reader.readNext()) != null) {

         dateReception = nextLine[23];
         numeroCompte = nextLine[3];
         siret = nextLine[6];
         uuidDoc = nextLine[0];

         if (StringUtils.isNotEmpty(dateReception)) {
            cpt++;

            writer.write(uuidDoc + ";" + dateReception + ";" + numeroCompte
                  + ";" + siret + "\n");

         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }

      }

      closeWriter(writer);

      System.out.println(printDate()
            + " - Nombre de documents avec date de reception :" + cpt);
      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

      System.out.println();
      System.out.println("Opération terminée");

   }

   /**
    * Ecrit dans un fichier les documents dont la date de réception est
    * renseignée
    * 
    * @throws IOException
    */
   @Test
   public void rechercheDocAvecPeriode() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      File fichier = new File(CHEMINREP, NOMFICHIER_DOC_AVEC_PERIODE);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String periode;
      String numeroCompte;
      String siret;

      String uuidDoc;

      Long cpt = 0L;

      while ((nextLine = reader.readNext()) != null) {

         periode = nextLine[24];
         numeroCompte = nextLine[3];
         siret = nextLine[6];
         uuidDoc = nextLine[0];

         if (StringUtils.isNotEmpty(periode)) {
            cpt++;

            writer.write(uuidDoc + ";" + periode + ";" + numeroCompte + ";"
                  + siret + "\n");

         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }

      }

      closeWriter(writer);

      System.out.println(printDate() + " - Nombre de documents avec période :"
            + cpt);
      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void ventilation_ParTitre2() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');
      // String titreRecherche = "MICRO SOC. VOLET 3 ARTIS. COMMERCANTS MEN";
      String titreRecherche = "1.2.3.B.X";
      String[] nextLine;
      int cptDoc = 0;
      String titre;
      String codeOrga;
      String compte;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         titre = nextLine[13];
         codeOrga = nextLine[1];
         compte = nextLine[3];

         if (titre.equals(titreRecherche)) {
            System.out.println(titre + ";" + codeOrga + ";" + compte);
         }

         /*
          * cptActuel = mapComptages.get(titre); if (cptActuel == null) {
          * mapComptages.put(titre, 1L); } else { mapComptages.put(titre,
          * cptActuel + 1); }
          */
         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }
      }

      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void listeDocAvecSiretVideEtSirenRenseigne() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');
      String[] nextLine;
      int cptDoc = 0;
      String siret;
      String siren;
      String uuid;
      String appProd;
      String appTrait;
      String dateArchi;
      String numCompte;

      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         siret = nextLine[6];
         siren = nextLine[8];
         uuid = nextLine[0];
         appProd = nextLine[9];
         appTrait = nextLine[10];
         dateArchi = nextLine[12];
         numCompte = nextLine[3];

         if (siret.isEmpty() && !siren.isEmpty()
               && dateArchi.startsWith("2012")) {
            // if (siret.isEmpty() && !siren.isEmpty()) {
            System.out.println(uuid + ";" + numCompte + ";" + siren + ";"
                  + appProd + ";" + appTrait + ";" + dateArchi);
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   // Nb de Document Scribe de 2015
   @Test
   public void nbDocScribe2015() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      int cptDicScribe = 0;
      String domaineRH;
      String dateCreation;

      while ((nextLine = reader.readNext()) != null) {

         domaineRH = nextLine[21];
         dateCreation = nextLine[13];

         if (domaineRH.equals("true")
               && StringUtils.substring(dateCreation, 0, 4).equals("2015")) {
            cptDicScribe++;
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

      System.out.println("Nombre de document SCRIBE de 2015 : " + cptDicScribe);

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void comptageNbDocParSiren() throws IOException {
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');
      File fichier = new File(CHEMINREP, NOMFICHIER_CPT_DOC_PAR_SIREN);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String siren;

      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         siren = nextLine[8];

         if (!siren.isEmpty()) {

            if (mapComptages.get(siren) == null) {
               mapComptages.put(siren, 1L);
            } else {
               mapComptages.put(siren, mapComptages.get(siren) + 1);
            }

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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {

         writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
         // System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      reader.close();
      writer.close();
      System.out.println();
      System.out.println("Opération terminée");
   }

   @Test
   public void uuidDocTailleSup1MHorsJournaux() throws IOException {
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');
      File fichier = new File(CHEMINREP, NOMFICHIER_DOC_TAILLE_SUP_1M);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String tailleFichier;
      String contratDeService;
      String appProd;
      String appTrait;

      while ((nextLine = reader.readNext()) != null) {

         tailleFichier = nextLine[17];

         if (Integer.parseInt(tailleFichier) >= 1000000) {
            contratDeService = nextLine[11];

            if (!"SAE".equals(contratDeService)) {
               appProd = nextLine[9];
               appTrait = nextLine[10];
               writer.write(nextLine[0] + ";" + tailleFichier + ";"
                     + contratDeService + ";" + appProd + ";" + appTrait + ";"
                     + nextLine[12] + "\n");
            }
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

      reader.close();
      writer.close();
      System.out.println();
      System.out.println("Opération terminée");
   }

   @Test
   public void ventilation_DureeInjectionSicomor() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      File fichier = new File(CHEMINREP, NOMFICHIER_DOC_SANS_DOMAINE);
      Writer writer = new FileWriter(fichier);

      String[] nextLine;
      int cptDoc = 0;
      String domaineCompta;
      String dateArchivage;
      String contratService;
      Long cptCompta;

      Map<String, Long> mapComptages = new HashMap<String, Long>();

      while ((nextLine = reader.readNext()) != null) {

         domaineCompta = nextLine[21];

         if (domaineCompta.equals("true")) {
            contratService = nextLine[11];
            if ("CS_INJECTEUR".equals(contratService)) {
               dateArchivage = nextLine[12];
               // date archivage : AAAAMMJJHHMMSSsss
               // On fait le comptage par heure : AAAAMMJJHH
               String dateArchivageHeure = StringUtils.left(dateArchivage, 10);
               cptCompta = mapComptages.get(dateArchivageHeure);
               if (mapComptages.get(dateArchivageHeure) == null) {
                  mapComptages.put(dateArchivageHeure, 1L);
               } else {
                  mapComptages.put(dateArchivageHeure, cptCompta + 1);
               }
            }

         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }

      }

      closeWriter(writer);

      System.out.println(printDate() + " - Nombre de documents traités : "
            + cptDoc);
      System.out.println();

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

   @Test
   public void extractionParTailleFichier() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String tailleFichier;
      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
      Writer writer = new FileWriter(fichier);

      while ((nextLine = reader.readNext()) != null) {

         tailleFichier = nextLine[17];

         if (Integer.parseInt(tailleFichier) > 100000
               && Integer.parseInt(tailleFichier) < 200000) {
            writer.write(nextLine[0] + ";" + tailleFichier + "\n");
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

      closeWriter(writer);

      System.out.println();
      System.out.println("Opération terminée");

   }

   /**
    * Ventilation des documents avec domaine cotisant à true et statutWATT à
    * pret par code orga propriétaire
    * 
    * @throws IOException
    */
   @Test
   public void ventilation_ParCodeOrganismeProprietairePourSatutWattPretDomaineCotisantTrue() throws IOException {

      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
            NOMFICHIER)), ';');

      String[] nextLine;
      int cptDoc = 0;
      String codeOrgaProp;
      String statutWATT;
      String domaineCotisant;

      Long cptActuel;
      Map<String, Long> mapComptages = new HashMap<String, Long>();
      while ((nextLine = reader.readNext()) != null) {

         codeOrgaProp = nextLine[2];
         domaineCotisant = nextLine[19];
         statutWATT = nextLine[27];

         if ("true".equals(domaineCotisant) && "PRET".equals(statutWATT)) {
            cptActuel = mapComptages.get(codeOrgaProp);

            if (cptActuel == null) {
               mapComptages.put(codeOrgaProp, 1L);
            } else {
               mapComptages.put(codeOrgaProp, cptActuel + 1);
            }
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

      Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
      for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
         System.out.println(entry.getKey() + ";" + entry.getValue());
      }

      System.out.println();
      System.out.println("Opération terminée");

   }

}


   @Test
   public void ventilation_DateArchivageMax_nno() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREPGNS);
      //File rep = new File(CHEMINREPGNT);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      File fichier = new File(CHEMINREPGNS, "GNS-DateArchivageMax-nno.csv");
      //File fichier = new File(CHEMINREPGNT, "GNT-DateArchivageMax-nno.csv");      
      Writer writer = new FileWriter(fichier);
      
   String[] nextLine;
   int cptDoc = 0;
   String RNDappProdAppTraitdateArchivage;
   String dateArchivage;
   String  dateMax;
   Map<String, String> mapDateFin = new HashMap<String, String>();
     
   while ((nextLine = reader.readNext()) != null) {
      
      RNDappProdAppTraitdateArchivage = nextLine[15] + ";" + nextLine[16] + ";" + nextLine[9] + ";" + nextLine[10]  + ";" + nextLine[11] + ";" + nextLine[20] +  ";" + nextLine[21] +  ";" + nextLine[19] +  ";" + nextLine[22];
    dateArchivage = StringUtils.left(nextLine[12],8);
      
      dateMax = mapDateFin.get(RNDappProdAppTraitdateArchivage);
      if(StringUtils.isEmpty(dateMax)) {
         mapDateFin.put(RNDappProdAppTraitdateArchivage, dateArchivage);
      } else {
         if (dateArchivage.compareTo(dateMax) > 0) {
            mapDateFin.put(RNDappProdAppTraitdateArchivage, dateArchivage);
         }
      }
     
      cptDoc++;
      if ((cptDoc % 30000)==0) {
         System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      }
      
   }
   
   System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
   System.out.println();
   
   Map<String, String> treeMap = new TreeMap<String, String>(mapDateFin);
   for(Map.Entry<String, String> entry : treeMap.entrySet()) {
      writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
   }
   
   closeWriter(writer);

   System.out.println();
   System.out.println("Opération terminée");
   
}
   
   @Test
   public void ventilation_CodeRND_nno() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREPGNS);
      //File rep = new File(CHEMINREPGNT);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      File fichier = new File(CHEMINREPGNS, "GNS-CodeRND-nno.csv");
      //File fichier = new File(CHEMINREPGNT, "GNT-CodeRND-nno.csv");
      Writer writer = new FileWriter(fichier);
      
   String[] nextLine;
   int cptDoc = 0;
   String RNDappProdAppTraitdateArchivage;
   Long cptCodeRND;
   Map<String, Long> mapCodeRND = new HashMap<String, Long>();
     
   while ((nextLine = reader.readNext()) != null) {
      
      RNDappProdAppTraitdateArchivage = nextLine[15] + ";" + nextLine[16] + ";" + nextLine[9] + ";" + nextLine[10]  + ";" + nextLine[11] + ";" + nextLine[20] +  ";" + nextLine[21] +  ";" + nextLine[19] +  ";" + nextLine[22];

      cptCodeRND = mapCodeRND.get(RNDappProdAppTraitdateArchivage);
      if (cptCodeRND==null) {
         mapCodeRND.put(RNDappProdAppTraitdateArchivage, 1L);
      } else {
         mapCodeRND.put(RNDappProdAppTraitdateArchivage, cptCodeRND+1);
      }
      
      
      cptDoc++;
      if ((cptDoc % 30000)==0) {
         System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      }
      
   }
   
   System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
   System.out.println();
   
   Map<String, Long> treeMap = new TreeMap<String, Long>(mapCodeRND);
   for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
      writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
   }
   
   closeWriter(writer);

   System.out.println();
   System.out.println("Opération terminée");
   
}
   

   @Test
   public void ventilation_DateArchivageMin() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREPGNS);
      //File rep = new File(CHEMINREPGNT);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      File fichier = new File(CHEMINREPGNS, "GNS-DateArchivageMin.csv");
      //File fichier = new File(CHEMINREPGNT, "GNT-DateArchivageMin.csv");
      Writer writer = new FileWriter(fichier);
      
   String[] nextLine;
   int cptDoc = 0;
   String RNDappProdAppTraitdateArchivage;
   String dateArchivage;
   String  dateMin;
   Map<String, String> mapDateDebut = new HashMap<String, String>();
     
   while ((nextLine = reader.readNext()) != null) {
      
    RNDappProdAppTraitdateArchivage = nextLine[15] + ";" + nextLine[16] + ";" + nextLine[9] + ";" + nextLine[10]  + ";" + nextLine[11] + ";" + nextLine[20] +  ";" + nextLine[21] +  ";" + nextLine[19];
    dateArchivage = StringUtils.left(nextLine[12],8);

      
      dateMin = mapDateDebut.get(RNDappProdAppTraitdateArchivage);
      if(StringUtils.isEmpty(dateMin)) {
         mapDateDebut.put(RNDappProdAppTraitdateArchivage, dateArchivage);
      } else {
         if (dateArchivage.compareTo(dateMin) < 0) {
            mapDateDebut.put(RNDappProdAppTraitdateArchivage, dateArchivage);
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
   }
   
   closeWriter(writer);

   System.out.println();
   System.out.println("Opération terminée");
   
}


   @Test
   public void ventilation_DateArchivageMax() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREPGNS);
      //File rep = new File(CHEMINREPGNT);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      File fichier = new File(CHEMINREPGNS, "GNS-DateArchivageMax.csv");
      //File fichier = new File(CHEMINREPGNT, "GNT-DateArchivageMax.csv");
      Writer writer = new FileWriter(fichier);
      
   String[] nextLine;
   int cptDoc = 0;
   String RNDappProdAppTraitdateArchivage;
   String dateArchivage;
   String  dateMax;
   Map<String, String> mapDateFin = new HashMap<String, String>();
     
   while ((nextLine = reader.readNext()) != null) {
      
      RNDappProdAppTraitdateArchivage = nextLine[15] + ";" + nextLine[16] + ";" + nextLine[9] + ";" + nextLine[10]  + ";" + nextLine[11] + ";" + nextLine[20] +  ";" + nextLine[21] +  ";" + nextLine[19];
    dateArchivage = StringUtils.left(nextLine[12],8);
      
      dateMax = mapDateFin.get(RNDappProdAppTraitdateArchivage);
      if(StringUtils.isEmpty(dateMax)) {
         mapDateFin.put(RNDappProdAppTraitdateArchivage, dateArchivage);
      } else {
         if (dateArchivage.compareTo(dateMax) > 0) {
            mapDateFin.put(RNDappProdAppTraitdateArchivage, dateArchivage);
         }
      }
     
      cptDoc++;
      if ((cptDoc % 30000)==0) {
         System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      }
      
   }
   
   System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
   System.out.println();
   
   Map<String, String> treeMap = new TreeMap<String, String>(mapDateFin);
   for(Map.Entry<String, String> entry : treeMap.entrySet()) {
      writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
   }
   
   closeWriter(writer);

   System.out.println();
   System.out.println("Opération terminée");
   
}
   
   @Test
   public void ventilation_CodeRND() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');

      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREPGNS);
      //File rep = new File(CHEMINREPGNT);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      File fichier = new File(CHEMINREPGNS, "GNS-CodeRND.csv");
      //File fichier = new File(CHEMINREPGNT, "GNT-CodeRND.csv");
      Writer writer = new FileWriter(fichier);
      
   String[] nextLine;
   int cptDoc = 0;
   String RNDappProdAppTraitdateArchivage;
   Long cptCodeRND;
   Map<String, Long> mapCodeRND = new HashMap<String, Long>();
     
   while ((nextLine = reader.readNext()) != null) {
      
      RNDappProdAppTraitdateArchivage = nextLine[15] + ";" + nextLine[16] + ";" + nextLine[9] + ";" + nextLine[10]  + ";" + nextLine[11] + ";" + nextLine[20] +  ";" + nextLine[21] +  ";" + nextLine[19];

      cptCodeRND = mapCodeRND.get(RNDappProdAppTraitdateArchivage);
      if (cptCodeRND==null) {
         mapCodeRND.put(RNDappProdAppTraitdateArchivage, 1L);
      } else {
         mapCodeRND.put(RNDappProdAppTraitdateArchivage, cptCodeRND+1);
      }
      
      
      cptDoc++;
      if ((cptDoc % 30000)==0) {
         System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
      }
      
   }
   
   System.out.println(printDate() + " - Nombre de documents traités : " + cptDoc);
   System.out.println();
   
   Map<String, Long> treeMap = new TreeMap<String, Long>(mapCodeRND);
   for(Map.Entry<String, Long> entry : treeMap.entrySet()) {
      writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
   }
   
   closeWriter(writer);

   System.out.println();
   System.out.println("Opération terminée");
   
}
   
   
}