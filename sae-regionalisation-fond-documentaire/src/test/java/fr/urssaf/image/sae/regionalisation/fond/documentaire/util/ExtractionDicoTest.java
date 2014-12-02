package fr.urssaf.image.sae.regionalisation.fond.documentaire.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.IOUtils;
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
 * <br>
 * Elle contient une méthode pour extraire les dictionnaires des 
 * métadonnées, c'est à dire la liste des valeurs dédoublonnées des métadonnées.
 * <br><br>
 * <b>Le traitement se fait en 2 parties :</b><br>
 * <ol>
 *    <li>
 *       Dump des valeurs des métadonnées, <b>non dédoublonnées</b>, avec 1 fichier par métadonnée, en
 *       utilisant le TU : <code>extraitDico()</code><br>
 *       Rechercher dans le code le TODO qui indique l'emplacement où renseigner les codes des
 *       métadonnées que l'on souhaite dumper, ainsi que le répertoire de sortie. 
 *    </li>
 *    <li>
 *       <b>Exécution de 2 commandes linux pour trier puis dédoublonner les fichiers *.dico générés par le TU :</b><br>
 *       <ul>
 *          <li>Uploader les fichiers sur un serveur Linux</li>
 *          <li>Se connecter en ssh, et se placer dans le répertoire où les fichiers ont été uploadés</li>
 *          <li><code>ls *.dico | xargs -n 1 sh -c 'sort $1 -o $1.tmp; mv $1.tmp $1' sh</code></li>
 *          <li><code>ls *.dico | xargs -n 1 sh -c 'uniq $1 $1.tmp; mv $1.tmp $1' sh</code></li>
 *       </ul>
 *    </li>
 * </ol>
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-regionalisation-cassandra-test.xml" })
@Ignore
public class ExtractionDicoTest {

   @Autowired
   private CassandraSupport cassandraSupport;
   
   @Autowired
   private DocInfoDao infoDao;
   
   @Autowired
   private Properties cassandraConf;
   
   @Test
   @SuppressWarnings("unchecked")
   public void extraitDico() throws IOException {
      
      // TODO Renseigner les valeurs
      String repertoireSortie = "c:/divers/extraction/";
      String[] metas = new String[] {"cog","cop","SM_DOCUMENT_TYPE","den","nce","sac","srt","SM_TITLE"};
      int nbMaxDocAparcourir = Integer.MAX_VALUE;
      // int nbMaxDocAparcourir = 50000;
      // Fin des valeurs
      
      // Affiche la configuration Cassandra dans la console
      afficheConfiguration();
      
      // Timestamp courant pour le calcul du temps d'exécution
      DateTime dateDebut = new DateTime();
      System.out.println("Début du traitement: " + dateDebut.toString("dd/MM/yyyy HH'h'mm ss's' SSS'ms'"));
      System.out.println("");
      
      // Liste des métadonnées que l'on va lire
      List<String> reqMetas = new ArrayList<String>();
      reqMetas.add("SM_BASE_ID");
      reqMetas.add("SM_UUID");
      reqMetas = ListUtils.union(reqMetas, Arrays.asList(metas));
            
      // Création du répertoire de sortie s'il n'existe pas déjà
      File repSortie = new File(repertoireSortie);
      if (!repSortie.exists()) {
         repSortie.mkdir();
      }
      
      // Récupère le nom de la base DFCE sur laquelle travailler
      String nomBaseDfceAttendue = cassandraConf.getProperty("db.baseName");
      
      // Connection à Cassandra
      cassandraSupport.connect();
      try {
         
         // Préparation des fichiers de sortie
         OutputStream[] fichiersOut = prepareResultats(metas, repSortie);
         try {
         
            // Construction de l'itérateur Cassandra
            AllRowsQuery<DocInfoKey, String> query = infoDao.getQuery(reqMetas
                  .toArray(new String[0]));
            CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
                  query);
            
            // Variables utilisées au sein de l'itérateur
            Map<String, String> mapCassandra;
            int nbDocsTraites = 0;
            int nbDocsDansBase = 0;
            String idDoc;
            String nomBaseDfce;
                     
            // Itère sur les rows de DocInfo
            while (iterator.hasNext()) {
               
               // Lecture de la ligne dans Cassandra
               mapCassandra = iterator.next();
               
               // Vérifie que l'on se trouve bien sur un document
               // de la base documentaire attendu 
               idDoc = mapCassandra.get("SM_UUID");
               nomBaseDfce = mapCassandra.get("SM_BASE_ID");
               if (
                  StringUtils.equals(nomBaseDfce, nomBaseDfceAttendue) && 
                  StringUtils.isNotBlank(idDoc)) {
               
                  // Traite la ligne
                  traiteRow(mapCassandra, metas, fichiersOut);
                  nbDocsDansBase += 1 ;
                  
               }
               
               // Compteurs
               nbDocsTraites++;
               if ((nbDocsTraites%10000)==0) {
                  System.out.print(new DateTime().toString("dd/MM/yyyy HH'h'mm ss's'"));
                  System.out.print(" => Nombre de docs traités : " + nbDocsTraites);
                  System.out.print(" (dont " + nbDocsDansBase + " dans la base " + nomBaseDfceAttendue + ")");
                  System.out.print(" => ");
                  afficheTempsExecution(dateDebut);
                  System.out.println("");
               }
               
               // Sort de la boucle si on a dépassé le nombre max de doc à parcourir
               if (nbDocsDansBase>=nbMaxDocAparcourir) {
                  break;
               }
               
            }
            
            // Affichage des compteurs totaux
            System.out.println("");
            System.out.println("=> Nombre de docs traités : " + nbDocsTraites);
            System.out.println("=> Nombre de docs analysés dans la base " + nomBaseDfceAttendue + " : " + nbDocsDansBase);
            
         
         } finally {
            closeStreams(fichiersOut);
         }

      } finally {
         cassandraSupport.disconnect();
      }
      
      // Timestamp courant pour le calcul du temps d'exécution
      DateTime dateFin = new DateTime();
      System.out.println("");
      System.out.println("Fin du traitement: " + dateFin.toString("dd/MM/yyyy HH'h'mm ss's' SSS'ms'"));
      System.out.println("");
      afficheTempsExecution(dateDebut);
      System.out.println("");
      
   }
   

   private void closeStreams(OutputStream[] fichiersOut) {
      if (fichiersOut!=null) {
         for(OutputStream outStream: fichiersOut) {
            try {
               outStream.close();
            } catch (IOException e) {
               System.err.println(e);
            }
         }
      }
   }
   
   private void traiteRow(Map<String, String> mapCassandra, String[] metas, OutputStream[] fichiersOut) throws IOException {
      
      String valMeta;
      for (int i=0;i<metas.length;i++) {
         
         valMeta = StringUtils.trimToEmpty(mapCassandra.get(metas[i]));
         
         if (StringUtils.isNotBlank(valMeta)) {
            
            IOUtils.write(valMeta, fichiersOut[i]);
            IOUtils.write("\r\n", fichiersOut[i]);
            
         }
         
      }
           
   }
   
   private void afficheTempsExecution(DateTime dateDebut) {
      
      DateTime dateEnCours = new DateTime(); 
      
      Period tempsExec = new Period(dateDebut, dateEnCours);
      PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
         .appendHours()
         .appendSuffix("h", "h")
         .appendSeparator(" ")
         .appendMinutes()
         .appendSuffix("mn", "mn")
         .appendSeparator(" ")
         .appendSeconds()
         .appendSuffix("s", "s")
//         .appendSeparator(" ")
//         .appendMillis()
//         .appendSuffix("ms", "ms")
         .toFormatter();
      
      System.out.print("Temps d'exécution: " + tempsExec.toString(periodFormatter));
      
   }
   
   private void afficheConfiguration() {
      
      System.out.println("==================================================================================");
      System.out.println("Configuration Cassandra");
      System.out.println("");
      System.out.println("Noeud(s): " + cassandraConf.getProperty("cassandra.servers"));
      System.out.println("Numéro de port: " + cassandraConf.getProperty("cassandra.port"));
      System.out.println("Username: " + cassandraConf.getProperty("cassandra.user"));
      System.out.println("Keyspace: " + cassandraConf.getProperty("cassandra.keyspace"));
      System.out.println("UUID de la base GED des documents: " + cassandraConf.getProperty("cassandra.baseUuid"));
      System.out.println("==================================================================================");
      System.out.println("");
      
   }
   
   
   private OutputStream[] prepareResultats(String[] metas, File repSortie) throws FileNotFoundException {
      
      OutputStream[] resultats = new OutputStream[metas.length];
      
      for(int i=0;i<metas.length;i++) {
         
         File fileSortie = new File(repSortie, metas[i] + ".dico");
         FileOutputStream fos = new FileOutputStream(fileSortie);
         resultats[i] = fos;
         
      }
      
      return resultats;
      
   }
   
}
