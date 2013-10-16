package fr.urssaf.image.sae.regionalisation.fond.documentaire.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.iterator.CassandraIterator;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.CassandraSupport;

/**
 * Cette classe n'est pas une "vraie" classe de TU.
 * 
 * Elle contient des méthodes pour extraire le fonds 
 * documentaire du SAE de PRODUCTION, afin de réaliser
 * les traitements de régionalisation
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
@Ignore
public class ExtractionFondsDocProdPourPgSqlTest {

   @Autowired
   private CassandraSupport cassandraSupport;
   
   @Autowired
   private DocInfoDao infoDao;
   
   @Autowired
   private Properties cassandraConf;
   
   /**
    * Chemin du répertoire de sortie des fichiers CVS
    * TODO: Adapter la valeur à son poste
    */
   private static String CHEMINREP = "c:/divers";
   
   /**
    * Premier mois d'archivage à sortir en CSV (format AAAAMM)
    * TODO: Adapter la valeur selon les données que l'on veut sortir
    */
   private int MIN_MOIS = 201201;
   
   /**
    * Dernier mois d'archivage à sortir en CSV (format AAAAMM)
    * TODO: Adapter la valeur selon les données que l'on veut sortir
    */
   private int MAX_MOIS = 201309;

   /**
    * Date de début pour la sortie en CSV (format AAAAMMJJ)
    * TODO: Adapter la valeur selon les données que l'on veut sortir
    */
   private int MIN_DATE = 20120101;
   
   /**
    * Date de fin de la sortir en CSV (format AAAAMMJJ)
    * TODO: Adapter la valeur selon les données que l'on veut sortir
    */
   private int MAX_DATE = 20130731;
   
   private static PeriodFormatter PERIOD_FORMATTER = new PeriodFormatterBuilder()
      .appendHours()
      .appendSuffix("h", "h")
      .appendSeparator(" ")
      .appendMinutes()
      .appendSuffix("mn", "mn")
      .appendSeparator(" ")
      .appendSeconds()
      .appendSuffix("s", "s")
      .appendSeparator(" ")
      .appendMillis()
      .appendSuffix("ms", "ms")
      .toFormatter();
  
   @Test
   public void extraitFondsDocUnFichierParMois() throws IOException {

      // Timestamp courant pour le calcul du temps d'exécution
      DateTime dateDebut = new DateTime();
      System.out.println("Début du traitement: " + dateDebut.toString("dd/MM/yyyy hh'h'mm ss's' SSS'ms'"));
      
      // Liste des métadonnées que l'on va lire
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.add("SM_BASE_ID");
      reqMetas.add("SM_UUID");
      reqMetas.add("cog");
      reqMetas.add("cop");
      reqMetas.add("nce");
      reqMetas.add("nci");
      reqMetas.add("npe");
      reqMetas.add("SM_ARCHIVAGE_DATE");
      
      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      // Récupère le nom de la base DFCE sur laquelle travailler
      String nomBaseDfceAttendue = cassandraConf.getProperty("db.baseName");
      

      Map<String, Writer> writers = new HashMap<String, Writer>();
      try {

         cassandraSupport.connect();

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
         String dateArchivage;
         int anneeMoisArchivage;
         String nce;
         String nci;
         String npe;
         
         Writer writer;
         while (iterator.hasNext()) {
            map = iterator.next();
            
            idDoc = map.get("SM_UUID");
            cog = map.get("cog");
            nomBaseDfce = map.get("SM_BASE_ID");
            dateArchivage = StringUtils.trimToEmpty(map.get("SM_ARCHIVAGE_DATE"));
            
            if ( 
                  StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue) && 
                  StringUtils.isNotBlank(idDoc) && 
                  StringUtils.isNotBlank(cog) && 
                  StringUtils.isNotBlank(dateArchivage))  { 
               
               anneeMoisArchivage = Integer.parseInt(StringUtils.left(dateArchivage, 6));
               
               if (
                     (anneeMoisArchivage>=MIN_MOIS) &&
                     (anneeMoisArchivage<=MAX_MOIS)) {

                  nce = StringUtils.trimToEmpty(map.get("nce"));
                  nci = StringUtils.trimToEmpty(map.get("nci"));
                  npe = StringUtils.trimToEmpty(map.get("npe"));
                  
                  if (
                        StringUtils.isNotBlank(nce) || 
                        StringUtils.isNotBlank(nci) || 
                        StringUtils.isNotBlank(npe)) {
                  
                     writer = getWriter(dateArchivage, writers); 
                     
                     writer.write(idDoc);
                     writer.write(";");
                     writer.write(cog);
                     writer.write(";;0;");
                     writer.write(map.get("cop"));
                     writer.write(";;0;");
                     writer.write(nce);
                     writer.write(";");
                     writer.write(nci);
                     writer.write(";");
                     writer.write(npe);
                     
                     writer.write("\n");
                     
                     nbDocsSortis++;
                  
                  }
               
               }
               
            }
            
            nbDocsTraites++;
            if ((nbDocsTraites%1000)==0) {
               System.out.println("Nombre de docs traités : " + nbDocsTraites);
            }
            
         }
         
         System.out.println("Nombre total de docs traités : " + (nbDocsTraites-1));
         System.out.println("Nombre total de docs sortis dans le fichier : " + nbDocsSortis);

      } catch (IOException exception) {
         System.err.println(exception);

      } finally {
         closeWriters(writers);
         cassandraSupport.disconnect();
      }
      
      // Timestamp courant pour le calcul du temps d'exécution
      DateTime dateFin = new DateTime();
      System.out.println("Fin du traitement: " + dateFin.toString("dd/MM/yyyy hh'h'mm ss's' SSS'ms'"));
      Period tempsExec = new Period(dateDebut, dateFin);
      System.out.println("Temps d'exécution: " + tempsExec.toString(PERIOD_FORMATTER));

   }
   
   
   private void closeWriters(Map<String, Writer> writers) {
      for (Map.Entry<String, Writer> entry : writers.entrySet()) {
         closeWriter(entry.getValue());
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
   
   
   private Writer getWriter(String date, Map<String, Writer> writers) {
      
      String anneeMois = StringUtils.left(date, 6);
      
      Writer writer = writers.get(anneeMois);
      if (writer==null) {
         File fichier = new File(CHEMINREP, String.format("fonds_doc_%s.csv", anneeMois));
         try {
            writer = new FileWriter(fichier);
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
         writers.put(anneeMois, writer);
         
      }
      
      return writer;
      
   }
   
   @Test
   public void extraitFondsDoc() throws IOException {

      // Timestamp courant pour le calcul du temps d'exécution
      DateTime dateDebut = new DateTime();
      System.out.println("Début du traitement: " + dateDebut.toString("dd/MM/yyyy hh'h'mm ss's' SSS'ms'"));
      
      // Liste des métadonnées que l'on va lire
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.add("SM_BASE_ID");
      reqMetas.add("SM_UUID");
      reqMetas.add("cog");
      reqMetas.add("cop");
      reqMetas.add("nce");
      reqMetas.add("nci");
      reqMetas.add("npe");
      reqMetas.add("SM_ARCHIVAGE_DATE");
      
      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      // Récupère le nom de la base DFCE sur laquelle travailler
      String nomBaseDfceAttendue = cassandraConf.getProperty("db.baseName");
      

      Map<String, Writer> writers = new HashMap<String, Writer>();
      try {

         cassandraSupport.connect();

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
         String dateArchivage;
         int anneeMoisJourArchivage;
         String nce;
         String nci;
         String npe;
         
         File fichier = new File(CHEMINREP, "fonds_doc.csv");
         Writer writer = new FileWriter(fichier);
         writers.put("all", writer);
         
         while (iterator.hasNext()) {
            map = iterator.next();
            
            idDoc = map.get("SM_UUID");
            cog = map.get("cog");
            nomBaseDfce = map.get("SM_BASE_ID");
            dateArchivage = StringUtils.trimToEmpty(map.get("SM_ARCHIVAGE_DATE"));
            
            if ( 
                  StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue) && 
                  StringUtils.isNotBlank(idDoc) && 
                  StringUtils.isNotBlank(cog) && 
                  StringUtils.isNotBlank(dateArchivage))  { 
               
               anneeMoisJourArchivage = Integer.parseInt(StringUtils.left(dateArchivage, 8));
               
               if (
                     (anneeMoisJourArchivage>=MIN_DATE) &&
                     (anneeMoisJourArchivage<=MAX_DATE)) {

                  nce = StringUtils.trimToEmpty(map.get("nce"));
                  nci = StringUtils.trimToEmpty(map.get("nci"));
                  npe = StringUtils.trimToEmpty(map.get("npe"));
                  
                  if (
                        StringUtils.isNotBlank(nce) || 
                        StringUtils.isNotBlank(nci) || 
                        StringUtils.isNotBlank(npe)) {
                  
                     writer.write(idDoc);
                     writer.write(";");
                     writer.write(cog);
                     writer.write(";;0;");
                     writer.write(map.get("cop"));
                     writer.write(";;0;");
                     writer.write(nce);
                     writer.write(";");
                     writer.write(nci);
                     writer.write(";");
                     writer.write(npe);
                      
                     writer.write("\n");
                     
                     nbDocsSortis++;
                  
                  }
               
               }
               
            }
            
            nbDocsTraites++;
            if ((nbDocsTraites%1000)==0) {
               System.out.println("Nombre de docs traités : " + nbDocsTraites);
            }
            
         }
         
         System.out.println("Nombre total de docs traités : " + (nbDocsTraites-1));
         System.out.println("Nombre total de docs sortis dans le fichier : " + nbDocsSortis);

      } catch (IOException exception) {
         System.err.println(exception);

      } finally {
         closeWriters(writers);
         cassandraSupport.disconnect();
      }
      
      // Timestamp courant pour le calcul du temps d'exécution
      DateTime dateFin = new DateTime();
      System.out.println("Fin du traitement: " + dateFin.toString("dd/MM/yyyy hh'h'mm ss's' SSS'ms'"));
      Period tempsExec = new Period(dateDebut, dateFin);
      System.out.println("Temps d'exécution: " + tempsExec.toString(PERIOD_FORMATTER));

   }   
}
