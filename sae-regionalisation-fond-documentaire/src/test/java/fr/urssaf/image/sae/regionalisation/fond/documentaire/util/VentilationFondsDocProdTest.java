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

import com.netflix.astyanax.query.AllRowsQuery;

import au.com.bytecode.opencsv.CSVReader;
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
// @Ignore
public class VentilationFondsDocProdTest {

  private final DateFormat dateFormat = new SimpleDateFormat(
      "dd/MM/yyyy hh'h'mm ss's'");

  @Autowired
  private CassandraSupport cassandraSupport;

  @Autowired
  private DocInfoDao infoDao;

  @Autowired
  private Properties cassandraConf;

  private static String CHEMINREP = "d:/divers";
  // private static String NOMFICHIER =
  // "20150611_fonds_doc_prod_pour_ventilation.csv";
  // private static String NOMFICHIER =
  // "20160502_fonds_doc_prod_gnt_pour_ventilation.csv";
  // private static String NOMFICHIER = "20190211_fonds_doc_cspp_gnt_pour_ventilation.csv";
  private static String NOMFICHIER = "GNS-20190211_fonds_doc_cspp_gnt_pour_ventilation.csv";

  private static String NOMFICHIERVENTIL = "20190211_ventilation_taille.csv";

  private static String NOMFICHIER_DOC_SANS_DOMAINE = "20190211_ventilation_doc_sans_domaine.csv";

  private static String NOMFICHIER_FORMAT_PDF = "20190211_ventilation_doc_format_pdf.csv";

  private static String NOMFICHIER_DOC_AVEC_DATE_RECEPTION = "20190211_docs_avec_date_reception.csv";

  private static String NOMFICHIER_DOC_AVEC_PERIODE = "20190211_docs_avec_periode.csv";

  private static String NOMFICHIER_CPT_DOC_PAR_SIREN = "20190211_nb_doc_par_siren.csv";

  private static String NOMFICHIER_DOC_TAILLE_SUP_1M = "20190211_doc_taille_sup_1M.csv";

  private static String NOMFICHIER_CODE_ORGA_GEST = "20190211_code_orga_gest.csv";

  private static String NOMFICHIER_CODE_ORGA_PROP = "20190211_code_orga_prop.csv";

  @Test
  // @Ignore
  public void extraitFondsDoc() throws IOException {

    // Liste des métadonnées que l'on va lire
    final List<String> reqMetas = new ArrayList<String>();
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
    reqMetas.add("cot");
    reqMetas.add("cpt");
    reqMetas.add("drh");
    reqMetas.add("dte");
    // Date de réception
    reqMetas.add("dre");
    // Période
    reqMetas.add("per");
    // Code produit V2
    reqMetas.add("cpr");
    // Code traitement V2
    reqMetas.add("ctr");
    // Domaine cotisant
    reqMetas.add("cot");
    // Domaine comptable
    reqMetas.add("cpt");
    // Domaine RH
    reqMetas.add("drh");
    // Domaine technique
    reqMetas.add("dte");
    // Statut WATT
    reqMetas.add("swa");

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }

    // Récupère le nom de la base DFCE sur laquelle travailler
    final String nomBaseDfceAttendue = cassandraConf.getProperty("db.baseName");

    Writer writer = null;
    try {
      cassandraSupport.connect();

      final File fichier = new File(rep, NOMFICHIER);
      writer = new FileWriter(fichier);

      final AllRowsQuery<DocInfoKey, String> query = infoDao.getQuery(reqMetas
                                                                      .toArray(new String[0]));
      final CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
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
          // 1 : Code orga gestionnaire
          writer.write(cog);
          writer.write(";");
          // 2 : Code orga propriétaire
          writer.write(map.get("cop"));
          writer.write(";");
          // 3 : Numéro de compte externe
          writer.write(StringUtils.trimToEmpty(map.get("nce")));
          writer.write(";");
          // 4 : Numéro de compte interne
          writer.write(StringUtils.trimToEmpty(map.get("nci")));
          writer.write(";");
          // 5 : Numéro de personne
          writer.write(StringUtils.trimToEmpty(map.get("npe")));
          writer.write(";");
          // 6 : Siret
          writer.write(StringUtils.trimToEmpty(map.get("srt")));
          writer.write(";");
          // 7 : Pseudo Siret
          writer.write(StringUtils.trimToEmpty(map.get("psi")));
          writer.write(";");
          // 8 : Siren
          writer.write(StringUtils.trimToEmpty(map.get("srn")));
          writer.write(";");
          // 9 : Application productrice
          writer.write(StringUtils.trimToEmpty(map.get("apr")));
          writer.write(";");
          // 10 : Application traitement
          writer.write(StringUtils.trimToEmpty(map.get("atr")));
          writer.write(";");
          // 11 : Contrat de service
          writer.write(StringUtils.trimToEmpty(map.get("cse")));
          writer.write(";");
          // 12 : Date d'archivage
          writer.write(StringUtils.trimToEmpty(map
                                               .get("SM_ARCHIVAGE_DATE")));
          writer.write(";");
          // 13 : Date de création
          writer.write(StringUtils.trimToEmpty(map.get("SM_CREATION_DATE")));
          writer.write(";");
          // 14 : Date de modification
          writer.write(StringUtils.trimToEmpty(map
                                               .get("SM_MODIFICATION_DATE")));
          writer.write(";");
          // 15 : Code RND
          writer.write(StringUtils.trimToEmpty(map.get("SM_DOCUMENT_TYPE")));
          writer.write(";");
          // 16 : Titre
          writer.write(StringUtils.trimToEmpty(map.get("SM_TITLE")));
          writer.write(";");
          // 17 : Taille fichier
          writer.write(StringUtils.trimToEmpty(map.get("SM_SIZE")));
          writer.write(";");
          // 18 : Format du fichier
          writer.write(StringUtils.trimToEmpty(map.get("ffi")));
          writer.write(";");
          // 19 : Domaine cotisant
          writer.write(StringUtils.trimToEmpty(map.get("cot")));
          writer.write(";");
          // 20 : Domaine comptable
          writer.write(StringUtils.trimToEmpty(map.get("cpt")));
          writer.write(";");
          // 21 : Domaine RH
          writer.write(StringUtils.trimToEmpty(map.get("drh")));
          writer.write(";");
          // 22 : Domaine technique
          writer.write(StringUtils.trimToEmpty(map.get("dte")));
          writer.write(";");
          // 23 : Date de réception
          writer.write(StringUtils.trimToEmpty(map.get("dre")));
          writer.write(";");
          // 24 : Période
          writer.write(StringUtils.trimToEmpty(map.get("pre")));
          writer.write(";");
          // 25 : Code produit V2
          writer.write(StringUtils.trimToEmpty(map.get("cpr")));
          writer.write(";");
          // 26 : Code traitement V2
          writer.write(StringUtils.trimToEmpty(map.get("ctr")));
          writer.write(";");
          // 27 : Statut WATT
          writer.write(StringUtils.trimToEmpty(map.get("swa")));

          writer.write("\n");

          nbDocsSortis++;

        }

        nbDocsTraites++;
        if (nbDocsTraites % 1000 == 0) {
          System.out.println("Nombre de docs traités : " + nbDocsTraites);
        }

      }

      System.out.println("Nombre total de docs traités : "
          + (nbDocsTraites - 1));
      System.out.println("Nombre total de docs sortis dans le fichier : "
          + (nbDocsSortis - 1));

    } catch (final IOException exception) {
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
    final List<String> reqMetas = new ArrayList<String>();
    reqMetas.add("SM_BASE_ID");
    reqMetas.add("SM_UUID");
    reqMetas.add("SM_DOCUMENT_TYPE");

    // Nombre d'id Doc souhaités
    final int nbIdDoc = 80000;
    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }

    // Récupère le nom de la base DFCE sur laquelle travailler
    final String nomBaseDfceAttendue = cassandraConf.getProperty("db.baseName");

    Writer writer = null;
    try {
      cassandraSupport.connect();

      final File fichier = new File(rep, NOMFICHIER);
      writer = new FileWriter(fichier);

      final AllRowsQuery<DocInfoKey, String> query = infoDao.getQuery(reqMetas
                                                                      .toArray(new String[0]));
      final CassandraIterator<DocInfoKey> iterator = new CassandraIterator<DocInfoKey>(
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
        if (nbDocsTraites % 1000 == 0) {
          System.out.println("Nombre de docs traités : " + nbDocsTraites);
        }

      }

      System.out
      .println("Nombre total de docs traités : " + nbDocsTraites);
      System.out.println("Nombre total de docs sortis dans le fichier : "
          + nbDocsSortis);

    } catch (final IOException exception) {
      System.err.println(exception);

    } finally {
      closeWriter(writer);
      cassandraSupport.disconnect();
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

  private String printDate() {
    return dateFormat.format(new Date());
  }

  @Test
  public void ventilation_ParTitre() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String codeRndEtTitre;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      codeRndEtTitre = nextLine[15] + ";" + nextLine[16];

      cptActuel = mapComptages.get(codeRndEtTitre);
      if (cptActuel == null) {
        mapComptages.put(codeRndEtTitre, 1L);
      } else {
        mapComptages.put(codeRndEtTitre, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParFormatFichier() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String formatFichier;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      formatFichier = nextLine[18];

      cptActuel = mapComptages.get(formatFichier);
      if (cptActuel == null) {
        mapComptages.put(formatFichier, 1L);
      } else {
        mapComptages.put(formatFichier, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParCodeRND() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String codeRnd;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      codeRnd = nextLine[15];

      cptActuel = mapComptages.get(codeRnd);
      if (cptActuel == null) {
        mapComptages.put(codeRnd, 1L);
      } else {
        mapComptages.put(codeRnd, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParApplicationProductrice() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String applTrait;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      applTrait = nextLine[9];

      cptActuel = mapComptages.get(applTrait);
      if (cptActuel == null) {
        mapComptages.put(applTrait, 1L);
      } else {
        mapComptages.put(applTrait, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParApplicationProductriceEtCodeRnd()
      throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String applTrait;
    String codeRnd;
    Long cptActuel;
    final Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
    while ((nextLine = reader.readNext()) != null) {

      applTrait = nextLine[9];
      codeRnd = nextLine[15];

      final Map<String, Long> mapAppliTrait = mapComptages.get(applTrait);

      if (mapAppliTrait == null) {
        final Map<String, Long> map = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
        mapComptages);

    for (final Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey());

      final Map<String, Long> treeMapAppl = entry.getValue();

      for (final Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
        System.out.println(entry2.getKey() + ";" + entry2.getValue());
      }
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParContratService() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String contratService;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      contratService = nextLine[11];

      cptActuel = mapComptages.get(contratService);
      if (cptActuel == null) {
        mapComptages.put(contratService, 1L);
      } else {
        mapComptages.put(contratService, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParContratServiceEtCodeRnd() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String contratService;
    String codeRnd;
    Long cptActuel;
    final Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
    while ((nextLine = reader.readNext()) != null) {

      contratService = nextLine[11];
      codeRnd = nextLine[15];

      final Map<String, Long> mapAppliTrait = mapComptages.get(contratService);

      if (mapAppliTrait == null) {
        final Map<String, Long> map = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
        mapComptages);

    for (final Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey());

      final Map<String, Long> treeMapAppl = entry.getValue();

      for (final Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
        System.out.println(entry2.getKey() + ";" + entry2.getValue());
      }
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParContratServiceAppliProdAppliTraitEtCodeRnd()
      throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String CSApplProdEtTrait;
    String codeRnd;
    Long cptActuel;
    final Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
    while ((nextLine = reader.readNext()) != null) {
      CSApplProdEtTrait = nextLine[11] + ";" + nextLine[9] + ";"
          + nextLine[10];
      ;
      codeRnd = nextLine[15];

      final Map<String, Long> mapAppliTrait = mapComptages.get(CSApplProdEtTrait);

      if (mapAppliTrait == null) {
        final Map<String, Long> map = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
        mapComptages);

    for (final Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey());

      final Map<String, Long> treeMapAppl = entry.getValue();

      for (final Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
        System.out.println(entry2.getKey() + ";" + entry2.getValue());
      }
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParApplicationTraitement() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String applProd;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      applProd = nextLine[10];

      cptActuel = mapComptages.get(applProd);
      if (cptActuel == null) {
        mapComptages.put(applProd, 1L);
      } else {
        mapComptages.put(applProd, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParApplicationProdEtCodeRnd() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String applProd;
    String codeRnd;
    Long cptActuel;
    final Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
    while ((nextLine = reader.readNext()) != null) {

      applProd = nextLine[10];
      codeRnd = nextLine[15];

      final Map<String, Long> mapAppliTrait = mapComptages.get(applProd);

      if (mapAppliTrait == null) {
        final Map<String, Long> map = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
        mapComptages);

    for (final Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey());

      final Map<String, Long> treeMapAppl = entry.getValue();

      for (final Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
        System.out.println(entry2.getKey() + ";" + entry2.getValue());
      }
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParApplicationTraitementEtProductrice()
      throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String applProdEtTrait;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      applProdEtTrait = nextLine[9] + ";" + nextLine[10];

      cptActuel = mapComptages.get(applProdEtTrait);
      if (cptActuel == null) {
        mapComptages.put(applProdEtTrait, 1L);
      } else {
        mapComptages.put(applProdEtTrait, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParApplicationProdEtApplicationTraitEtCodeRnd()
      throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String applProdEtTrait;

    String codeRnd;
    Long cptActuel;
    final Map<String, Map<String, Long>> mapComptages = new HashMap<String, Map<String, Long>>();
    while ((nextLine = reader.readNext()) != null) {

      applProdEtTrait = nextLine[9] + ";" + nextLine[10];
      codeRnd = nextLine[15];

      final Map<String, Long> mapAppliTraitEtProd = mapComptages
          .get(applProdEtTrait);

      if (mapAppliTraitEtProd == null) {
        final Map<String, Long> map = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Map<String, Long>> treeMap = new TreeMap<String, Map<String, Long>>(
        mapComptages);

    for (final Map.Entry<String, Map<String, Long>> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey());

      final Map<String, Long> treeMapAppl = entry.getValue();

      for (final Map.Entry<String, Long> entry2 : treeMapAppl.entrySet()) {
        System.out.println(entry2.getKey() + ";" + entry2.getValue());
      }
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParCodeOrganismeProprietaire() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String codeOrgaProp;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      codeOrgaProp = nextLine[2];

      cptActuel = mapComptages.get(codeOrgaProp);
      if (cptActuel == null) {
        mapComptages.put(codeOrgaProp, 1L);
      } else {
        mapComptages.put(codeOrgaProp, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParCodeOrganismeGestionnaire() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String codeOrgaGest;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      codeOrgaGest = nextLine[1];

      cptActuel = mapComptages.get(codeOrgaGest);
      if (cptActuel == null) {
        mapComptages.put(codeOrgaGest, 1L);
      } else {
        mapComptages.put(codeOrgaGest, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParContratServiceCodeOrganismeGestionnaireDateArchivage()
      throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String codeOrgaGest;
    String contratService;
    String dateArchivage;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();

    final File fichier = new File(CHEMINREP, NOMFICHIER_CODE_ORGA_GEST);
    final Writer writer = new FileWriter(fichier);

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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }
    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
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

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String codeOrgaProp;
    String contratService;
    String dateArchivage;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();

    final File fichier = new File(CHEMINREP, NOMFICHIER_CODE_ORGA_PROP);
    final Writer writer = new FileWriter(fichier);

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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
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

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }
    final File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
    final Writer writer = new FileWriter(fichier);

    String[] nextLine;
    int cptDoc = 0;
    String dateArchivage;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      dateArchivage = StringUtils.left(nextLine[12], 6);

      cptActuel = mapComptages.get(dateArchivage);
      if (cptActuel == null) {
        mapComptages.put(dateArchivage, 1L);
      } else {
        mapComptages.put(dateArchivage, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
      // System.out.println(entry.getKey() + ";" + entry.getValue());

    }

    closeWriter(writer);

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParMoisArchivage() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }
    final File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
    final Writer writer = new FileWriter(fichier);

    String[] nextLine;
    int cptDoc = 0;
    String dateArchivage;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      dateArchivage = StringUtils.left(nextLine[12], 6);

      cptActuel = mapComptages.get(dateArchivage);
      if (cptActuel == null) {
        mapComptages.put(dateArchivage, 1L);
      } else {
        mapComptages.put(dateArchivage, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
      // System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    closeWriter(writer);

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParMoisDateModification() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }
    final File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
    final Writer writer = new FileWriter(fichier);

    String[] nextLine;
    int cptDoc = 0;
    String dateModification;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      dateModification = StringUtils.left(nextLine[14], 6);

      cptActuel = mapComptages.get(dateModification);
      if (cptActuel == null) {
        mapComptages.put(dateModification, 1L);
      } else {
        mapComptages.put(dateModification, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
      // System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    closeWriter(writer);

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParMoisDateCreation() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }
    final File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
    final Writer writer = new FileWriter(fichier);

    String[] nextLine;
    int cptDoc = 0;
    String dateCreation;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
    while ((nextLine = reader.readNext()) != null) {

      dateCreation = StringUtils.left(nextLine[13], 6);

      cptActuel = mapComptages.get(dateCreation);
      if (cptActuel == null) {
        mapComptages.put(dateCreation, 1L);
      } else {
        mapComptages.put(dateCreation, cptActuel + 1);
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
      // System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    closeWriter(writer);

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParMoisArchivageAppProdAppTrait() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }
    final File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
    final Writer writer = new FileWriter(fichier);

    String[] nextLine;
    int cptDoc = 0;
    String appProdAppTraitDateArchivage;
    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
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

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }
    final File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
    final Writer writer = new FileWriter(fichier);

    String[] nextLine;
    int cptDoc = 0;
    String appProdAppTraitdateArchivage;
    String dateArchivage;
    String dateMin;
    final Map<String, String> mapDateDebut = new HashMap<String, String>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, String> treeMap = new TreeMap<String, String>(mapDateDebut);
    for (final Map.Entry<String, String> entry : treeMap.entrySet()) {
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

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }
    final File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
    final Writer writer = new FileWriter(fichier);

    String[] nextLine;
    int cptDoc = 0;
    String appProdAppTraitdateArchivageRnd;
    String dateArchivage;
    final String dateMin;
    Long cptActuel;
    final Map<String, Long> mapDateDebut = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapDateDebut);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
      // System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    closeWriter(writer);

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void ventilation_ParDomaine() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    final File fichier = new File(CHEMINREP, NOMFICHIER_DOC_SANS_DOMAINE);
    final Writer writer = new FileWriter(fichier);

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

    final Map<String, Long> mapComptages = new HashMap<String, Long>();

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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    closeWriter(writer);

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  /**
   * Ecrit dans un fichier la liste des documents dont le format de fichier est
   * égal à PDF
   * 
   * @throws IOException
   */
  @Test
  public void ventilation_ParFormatEgalPDF() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    final File fichier = new File(CHEMINREP, NOMFICHIER_FORMAT_PDF);
    final Writer writer = new FileWriter(fichier);

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
      if (cptDoc % 30000 == 0) {
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

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    final File fichier = new File(CHEMINREP, NOMFICHIER_DOC_AVEC_DATE_RECEPTION);
    final Writer writer = new FileWriter(fichier);

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
      if (cptDoc % 30000 == 0) {
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

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    final File fichier = new File(CHEMINREP, NOMFICHIER_DOC_AVEC_PERIODE);
    final Writer writer = new FileWriter(fichier);

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
      if (cptDoc % 30000 == 0) {
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

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');
    // String titreRecherche = "MICRO SOC. VOLET 3 ARTIS. COMMERCANTS MEN";
    final String titreRecherche = "1.2.3.B.X";
    String[] nextLine;
    int cptDoc = 0;
    String titre;
    String codeOrga;
    String compte;
    final Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }
    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void listeDocAvecSiretVideEtSirenRenseigne() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
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

    final Map<String, Long> mapComptages = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }
    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  // Nb de Document Scribe de 2015
  @Test
  public void nbDocScribe2015() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
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
      if (cptDoc % 30000 == 0) {
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
    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');
    final File fichier = new File(CHEMINREP, NOMFICHIER_CPT_DOC_PAR_SIREN);
    final Writer writer = new FileWriter(fichier);

    String[] nextLine;
    int cptDoc = 0;
    String siren;

    final Map<String, Long> mapComptages = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }
    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {

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
    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');
    final File fichier = new File(CHEMINREP, NOMFICHIER_DOC_TAILLE_SUP_1M);
    final Writer writer = new FileWriter(fichier);

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
      if (cptDoc % 30000 == 0) {
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

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    final File fichier = new File(CHEMINREP, NOMFICHIER_DOC_SANS_DOMAINE);
    final Writer writer = new FileWriter(fichier);

    String[] nextLine;
    int cptDoc = 0;
    String domaineCompta;
    String dateArchivage;
    String contratService;
    Long cptCompta;

    final Map<String, Long> mapComptages = new HashMap<String, Long>();

    while ((nextLine = reader.readNext()) != null) {

      domaineCompta = nextLine[21];

      if (domaineCompta.equals("true")) {
        contratService = nextLine[11];
        if ("CS_INJECTEUR".equals(contratService)) {
          dateArchivage = nextLine[12];
          // date archivage : AAAAMMJJHHMMSSsss
          // On fait le comptage par heure : AAAAMMJJHH
          final String dateArchivageHeure = StringUtils.left(dateArchivage, 10);
          cptCompta = mapComptages.get(dateArchivageHeure);
          if (mapComptages.get(dateArchivageHeure) == null) {
            mapComptages.put(dateArchivageHeure, 1L);
          } else {
            mapComptages.put(dateArchivageHeure, cptCompta + 1);
          }
        }

      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    closeWriter(writer);

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

  @Test
  public void extractionParTailleFichier() throws IOException {

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String tailleFichier;
    final Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();

    // Création du répertoire de sortie s'il n'existe pas déjà
    final File rep = new File(CHEMINREP);
    if (!rep.exists()) {
      rep.mkdir();
    }
    final File fichier = new File(CHEMINREP, NOMFICHIERVENTIL);
    final Writer writer = new FileWriter(fichier);

    while ((nextLine = reader.readNext()) != null) {

      tailleFichier = nextLine[17];

      if (Integer.parseInt(tailleFichier) > 100000
          && Integer.parseInt(tailleFichier) < 200000) {
        writer.write(nextLine[0] + ";" + tailleFichier + "\n");
      }

      cptDoc++;
      if (cptDoc % 30000 == 0) {
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

    final CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP,
                                                                   NOMFICHIER)), ';');

    String[] nextLine;
    int cptDoc = 0;
    String codeOrgaProp;
    String statutWATT;
    String domaineCotisant;

    Long cptActuel;
    final Map<String, Long> mapComptages = new HashMap<String, Long>();
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
      if (cptDoc % 30000 == 0) {
        System.out.println(printDate()
                           + " - Nombre de documents traités : " + cptDoc);
      }

    }

    System.out.println(printDate() + " - Nombre de documents traités : "
        + cptDoc);
    System.out.println();

    final Map<String, Long> treeMap = new TreeMap<String, Long>(mapComptages);
    for (final Map.Entry<String, Long> entry : treeMap.entrySet()) {
      System.out.println(entry.getKey() + ";" + entry.getValue());
    }

    System.out.println();
    System.out.println("Opération terminée");

  }

}
