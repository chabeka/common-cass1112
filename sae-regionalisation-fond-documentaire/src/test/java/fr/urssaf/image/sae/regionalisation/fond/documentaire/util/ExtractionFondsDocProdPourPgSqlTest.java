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
//@Ignore
public class ExtractionFondsDocProdPourPgSqlTest {

  @Autowired
  private CassandraSupport cassandraSupport;

  @Autowired
  private DocInfoDao infoDao;

  @Autowired
  private Properties cassandraConf;


  @Test
  public void extraitDoc() throws IOException {

    // Paramétrage du TU
    // TODO: à adapter selon le besoin
    final String repertoireSortie = "c:/divers";
    //      int dateArchivageDebut = 20120101;
    //      int dateArchivageFin = 20161226;
    // int dateArchivageDebut = 20161225;
    // int dateArchivageFin = 20170108;
    final int dateArchivageDebut = 20120101;
    final int dateArchivageFin = 20190211;

    final boolean unFichierParMois = false;


    // Timestamp courant pour le calcul du temps d'exécution
    final DateTime dateDebut = new DateTime();
    System.out.println("Début du traitement: " + dateDebut.toString("dd/MM/yyyy hh'h'mm ss's' SSS'ms'"));

    // Liste des métadonnées que l'on va lire
    final List<String> reqMetas = new ArrayList<String>();
    reqMetas.add("SM_BASE_ID");
    reqMetas.add("SM_UUID");
    reqMetas.add("cog");
    reqMetas.add("cop");
    reqMetas.add("nce");
    reqMetas.add("nci");
    reqMetas.add("npe");
    reqMetas.add("SM_ARCHIVAGE_DATE");
    reqMetas.add("SM_DOCUMENT_TYPE");

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(repertoireSortie);
    if (!rep.exists()) {
      rep.mkdir();
    }

    // Récupère le nom de la base DFCE sur laquelle travailler
    final String nomBaseDfceAttendue = cassandraConf.getProperty("db.baseName");


    final Map<String, Writer> writers = new HashMap<String, Writer>();
    try {

      cassandraSupport.connect();

      final AllRowsQuery<DocInfoKey, String> query = infoDao.getQuery(reqMetas
                                                                      .toArray(new String[0]));
      final CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
          query);

      Map<String, String> map;

      int nbDocsTraites = 0;
      int nbDocsSortis = 0;
      int nbJournauxSae = 0;

      String idDoc;
      String cog;
      String nomBaseDfce;
      String dateArchivage;
      int anneeMoisJourArchivage;
      String nce;
      String nci;
      String npe;
      String codeRnd;
      Writer writer;

      // Si un gros fichier, on créé le writer global
      if (!unFichierParMois) {
        final File fichier = new File(repertoireSortie, "fonds_doc.csv");
        writers.put("all", new FileWriter(fichier));
      }

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
              anneeMoisJourArchivage>=dateArchivageDebut &&
              anneeMoisJourArchivage<=dateArchivageFin) {

            nce = StringUtils.trimToEmpty(map.get("nce"));
            nci = StringUtils.trimToEmpty(map.get("nci"));
            npe = StringUtils.trimToEmpty(map.get("npe"));
            codeRnd = StringUtils.trimToEmpty(map.get("SM_DOCUMENT_TYPE"));

            if (
                // Exclusion des journaux SAE.
                !StringUtils.equals(codeRnd,"7.7.8.8.1")) 
            {

              writer = getWriter(unFichierParMois, repertoireSortie, dateArchivage, writers);

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

            } else {
              nbJournauxSae++;
            }

          }

        }

        nbDocsTraites++;
        if (nbDocsTraites%1000==0) {
          System.out.println("Nombre de docs traités : " + nbDocsTraites);
        }

      }

      System.out.println("Nombre total de docs traités : " + (nbDocsTraites-1));
      System.out.println("Nombre de journaux SAE : " + nbJournauxSae);
      System.out.println("Nombre total de docs sortis dans le fichier : " + nbDocsSortis);


    } catch (final IOException exception) {
      System.err.println(exception);

    } finally {
      closeWriters(writers);
      cassandraSupport.disconnect();
    }

    // Timestamp courant pour le calcul du temps d'exécution
    final DateTime dateFin = new DateTime();
    System.out.println("Fin du traitement: " + dateFin.toString("dd/MM/yyyy hh'h'mm ss's' SSS'ms'"));
    final Period tempsExec = new Period(dateDebut, dateFin);
    final PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
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
    System.out.println("Temps d'exécution: " + tempsExec.toString(periodFormatter));

  }

  private void closeWriters(final Map<String, Writer> writers) {
    for (final Map.Entry<String, Writer> entry : writers.entrySet()) {
      closeWriter(entry.getValue());
    }
  }

  private void closeWriter(final Writer writer) {
    try {
      if (writer != null) {
        writer.close();
      }
    } catch (final IOException exception) {
      System.err.println("impossible de fermer le flux");
    }
  }


  private Writer getWriter(final boolean unFichierParMois, final String repertoireSortie, final String date, final Map<String, Writer> writers) {

    if (unFichierParMois) {
      return getWriter(repertoireSortie, date, writers);
    } else {
      return writers.get("all");
    }

  }


  private Writer getWriter(final String repertoireSortie, final String date, final Map<String, Writer> writers) {

    final String anneeMois = StringUtils.left(date, 6);

    Writer writer = writers.get(anneeMois);
    if (writer==null) {
      final File fichier = new File(repertoireSortie, String.format("fonds_doc_%s.csv", anneeMois));
      try {
        writer = new FileWriter(fichier);
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
      writers.put(anneeMois, writer);

    }

    return writer;

  }

}
