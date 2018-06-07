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
public class VentilationFondsDocMOE {

   private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh'h'mm ss's'");
   
   @Autowired
   private CassandraSupport cassandraSupport;
   
   @Autowired
   private DocInfoDao infoDao;
   
   @Autowired
   private Properties cassandraConf;
   
   private static String CHEMINREPGNT = "d:/sav/GNT";
   private static String NOMFICHIERGNT = "20180605-extraction-GNT.csv";
   
   private static String CHEMINREPGNS = "d:/sav/GNS";
   private static String NOMFICHIERGNS = "20180604-extraction-GNS.csv";
   
   
   private static String CHEMINREP = "d:/divers";
   //private static String NOMFICHIER = "20170706_fonds_doc_prod_pour_ventilation.csv";
   private static String NOMFICHIER = "GNT-20180605_fonds_doc_prod_pour_ventilation.csv";
   private static String NOMFICHIERID = "20180605_fonds_doc_id.csv";
   private static String NOMFICHIERVENTILPARDATEARCHIVAGE = "20180605_ventilation_date_archivage.csv";
   private static String NOMFICHIERVENTILPARMOISARCHIVAGE = "20180605_ventilation_mois_archivage.csv";
   private static String NOMFICHIERVENTILPARMOISDATEMODIFICATION = "20180605_ventilation_mois_modification.csv";
   private static String NOMFICHIERVENTILPARMOISCREATION = "20180605_ventilation_mois_creation.csv";
   private static String NOMFICHIERVENTILPARMOISARCHIVAGEAPPPRODAPPTRAIT = "20180605_ventilation_mois_archivage_productrive_traitement.csv";
   private static String NOMFICHIERVENTILPARDATEARCHIVAGEETAPPTRAITETAPPPROD = "20180605_ventilation_date_archivage_traitement_productrive.csv";
   

   @Test
   // @Ignore
   public void extraitFondsDocMOE() throws IOException {

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
   reader.close();
   
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
   reader.close();
   
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
   reader.close();
   
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
   reader.close();
}
   @Test
   public void ventilation_DateArchivageMin_nno() throws IOException {
      
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');
      
      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREPGNS);
      //File rep = new File(CHEMINREPGNT);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      File fichier = new File(CHEMINREPGNS, "GNS-DateArchivageMin-nno.csv");
      //File fichier = new File(CHEMINREPGNT, "GNT-DateArchivageMin-nno.csv");
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
   closeWriter(writer);
   reader.close();
   
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
      reader.close();
      
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
      reader.close();
      
   }
   
   @Test
   public void comptageNbDocRH() throws IOException {
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');

     
      String[] nextLine;
      long cptDoc = 0;
      long cptDocRH = 0;
      long tailleDocRH = 0;
      String domaineRH;

      while ((nextLine = reader.readNext()) != null) {

         domaineRH = nextLine[25];
         //dateCreation = nextLine[13];

         if (domaineRH.equals("true")) {
            cptDocRH++;
//            System.out.println("taille nb doc :  "+ cptDocRH + " taille : " + nextLine[17] + " tailleDocRH : "+tailleDocRH  + " tailleDocRH Mo: "+tailleDocRH/1048576);
                  
            tailleDocRH = tailleDocRH + Long.parseLong(nextLine[17]);
            System.out.println("taille nb doc :  "+ cptDocRH + " taille : " + nextLine[17] + " tailleDocRH : "+tailleDocRH  + " tailleDocRH Mo: "+tailleDocRH/1048576 + " TailleRH : " + tailleDocRH);
         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }
      }

      System.out.println(printDate() + " - Nombre de documents traités : "+ cptDoc);
      System.out.println();
      System.out.println("Nombre de document RH : " + cptDocRH);
      System.out.println();
      
      System.out.println("Taille des documents RH : " + String.valueOf(tailleDocRH/1048576) + "Mo" );
      System.out.println("Taille des documents RH : " + String.valueOf(tailleDocRH/1073741824) + "Go" );
      System.out.println();      
      System.out.println("Opération terminée");

      reader.close();
   }
 
   
   @Test
   public void comptageNbDocRHFic() throws IOException {
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');

     
      String[] nextLine;
      long cptDoc = 0;
      long cptDocRH = 0;
      long tailleDocRH = 0;
      String domaineRH;
      Map<Long, Long> mapRH = new HashMap<Long, Long>();
      
      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREPGNS);
      //File rep = new File(CHEMINREPGNT);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      File fichier = new File(CHEMINREPGNS, "GNS-CptDocRH.csv");
      //File fichier = new File(CHEMINREPGNT, "GNT-CptDocRH.csv");
      Writer writer = new FileWriter(fichier);
      
      while ((nextLine = reader.readNext()) != null) {

         domaineRH = nextLine[25];
         
         if (domaineRH.equals("true")) {
            cptDocRH++;
//            System.out.println("taille nb doc :  "+ cptDocRH + " taille : " + nextLine[17] + " tailleDocRH : "+tailleDocRH  + " tailleDocRH Mo: "+tailleDocRH/1048576);
                  
            tailleDocRH = tailleDocRH + Long.parseLong(nextLine[17]);
//            System.out.println("taille nb doc :  "+ cptDocRH + " taille : " + nextLine[17] + " tailleDocRH : "+tailleDocRH  + " tailleDocRH Mo: "+tailleDocRH/1048576 + " TailleRH : " + tailleDocRH);
            
            mapRH.put(cptDocRH,tailleDocRH);
         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }
      }

      System.out.println(printDate() + " - Nombre de documents traités : "+ cptDoc);
      System.out.println();
      System.out.println("Nombre de document RH : " + cptDocRH);
      System.out.println();
      
      System.out.println("Taille des documents RH : " + String.valueOf(tailleDocRH/1048576) + "Mo" );
      System.out.println("Taille des documents RH : " + String.valueOf(tailleDocRH/1073741824) + "Go" );
      
      Map<Long, Long> treeMap = new TreeMap<Long, Long>(mapRH);
      for(Map.Entry<Long, Long> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
      }
      
      closeWriter(writer);
      reader.close();
      
      System.out.println();      
      System.out.println("Opération terminée");

   }

   @Test
   public void numeroCompteExterne480() throws IOException {
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNS, NOMFICHIERGNS)), ';');
      //CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREPGNT, NOMFICHIERGNT)), ';');

     
      String[] nextLine;
      long cptDoc = 0;
      long cptDoc480 = 0;
      String numeroCompteExterne;
      String valCode = "480";
      String debutNumeroCompteExterne;
      Map<String, String> mapCptExt = new HashMap<String, String>();
      
      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREPGNS);
      //File rep = new File(CHEMINREPGNT);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      File fichier = new File(CHEMINREPGNS, "GNS-numeroCompteExterne.csv");
      //File fichier = new File(CHEMINREPGNT, "GNT-numeroCompteExterne.csv");
      Writer writer = new FileWriter(fichier);
      
      while ((nextLine = reader.readNext()) != null) {

         numeroCompteExterne = nextLine[3];
         if (!StringUtils.isEmpty(numeroCompteExterne)) {
            debutNumeroCompteExterne = numeroCompteExterne.substring(0,3);
         }else {
         debutNumeroCompteExterne = "";
         }
         if (debutNumeroCompteExterne.compareTo( valCode ) == 0) {
            cptDoc480++;
            mapCptExt.put(nextLine[0],numeroCompteExterne);
         }

         cptDoc++;
         if ((cptDoc % 30000) == 0) {
            System.out.println(printDate()
                  + " - Nombre de documents traités : " + cptDoc);
         }
      }

      System.out.println(printDate() + " - Nombre de documents traités : "+ cptDoc);
      System.out.println();
      System.out.println("Nombre de document 480 : " + cptDoc480);
      System.out.println();
      
      Map<String, String> treeMap = new TreeMap<String, String>(mapCptExt);
      for(Map.Entry<String, String> entry : treeMap.entrySet()) {
         writer.write(entry.getKey() + ";" + entry.getValue() +"\n");
      }
      
      reader.close();
      closeWriter(writer);
      System.out.println();      
      System.out.println("Opération terminée");

   }
 
}