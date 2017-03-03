package fr.urssaf.image.sae.lotinstallmaj.service.utils;

import java.util.Arrays;
import java.util.List;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.lotinstallmaj.modele.FormatProfil;
import fr.urssaf.image.sae.lotinstallmaj.serializer.FormatProfilSerializer;
import fr.urssaf.image.sae.lotinstallmaj.serializer.ListSerializer;

/**
 * Classe utilitaire pour assurer les service d'update des CF référentiels.
 * 
 */
public class ReferentielServiceUtils {

   /**
    * Logger
    */
   private static final Logger LOG = LoggerFactory
         .getLogger(ReferentielServiceUtils.class);

   /**
    * Autorisé en GED
    */
   private static final String AUTORISE_GED = "autoriseGED";

   /**
    * Initialisation du référentiel des événements en V1
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV1(Keyspace keyspace) {

      LOG.info("Initialisation du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // WS_RECHERCHE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_RECHERCHE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_CAPTURE_MASSE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_CAPTURE_MASSE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_CAPTURE_UNITAIRE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_CAPTURE_UNITAIRE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_CONSULTATION|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_CONSULTATION|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_PING_SECURE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_PING_SECURE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // CAPTURE_MASSE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("CAPTURE_MASSE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // DFCE_DEPOT_DOC|OK
      // dans le journal des événements SAE avec all_infos
      updater = cfTmpl.createUpdater("DFCE_DEPOT_DOC|OK");
      CassandraUtils.addColumn("JOURN_EVT", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // DFCE_SUPPRESSION_DOC|OK
      // dans le journal des événements SAE avec all_infos
      updater = cfTmpl.createUpdater("DFCE_SUPPRESSION_DOC|OK");
      CassandraUtils.addColumn("JOURN_EVT", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_LOAD_CERTS_ACRACINE|OK
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_LOAD_CERTS_ACRACINE|OK");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_LOAD_CRLS|OK
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_LOAD_CRLS|OK");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V2 Ajout de l'évenement MAJ_VERSION_RND|OK
    * (Automatisation RND)
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV2(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // MAJ_VERSION_RND|OK
      // dans le journal des événements SAE avec all_infos
      updater = cfTmpl.createUpdater("MAJ_VERSION_RND|OK");
      CassandraUtils.addColumn("JOURN_EVT", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      updater = cfTmpl.createUpdater("DFCE_MODIF_DOC|OK");
      CassandraUtils.addColumn("JOURN_EVT", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);

      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V3 Ajout de l'évenement ORDO_ECDE_DISPO|KO
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV3(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // ORDO_ECDE_DISPO|KO
      // dans le registre de surveillance technique avec all_infos
      // dans le registre d'exploitation avec all_infos
      updater = cfTmpl.createUpdater("ORDO_ECDE_DISPO|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      CassandraUtils.addColumn("REG_EXPLOITATION", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V4 Ajout des évenements IGC_LOAD_CRLS|KO,
    * WS_LOAD_CRLS|KO, ERREUR_IDENT_FORMAT_FICHIER|INFO et
    * ERREUR_VALID_FORMAT_FICHIER |INFO
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV4(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // IGC_LOAD_CRLS|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("IGC_LOAD_CRLS|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_LOAD_CRLS|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_LOAD_CRLS|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // ERREUR_IDENT_FORMAT_FICHIER|INFO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("ERREUR_IDENT_FORMAT_FICHIER|INFO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // ERREUR_VALID_FORMAT_FICHIER|INFO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("ERREUR_VALID_FORMAT_FICHIER|INFO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // META_VAL_ESPACE|INFO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("META_VAL_ESPACE|INFO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V5 Ajout des évenements : <li>
    * DFCE_TRANSFERT_DOC|OK</li> <li>WS_TRANSFERT|KO</li>
    * 
    * @param keyspace
    *           Keyspace
    * 
    * @since 06/10/2014
    * @author Michael PAMBO OGNANA
    */
   public static void addReferentielEvenementV5(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // -- DFCE_TRANSFERT_DOC|OK
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("DFCE_TRANSFERT_DOC|OK");
      CassandraUtils.addColumn("JOURN_EVT", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // -- WS_TRANSFERT|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_TRANSFERT|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);
   }

   /**
    * Référentiel des événements en V6 Ajout des évenements : <li>
    * WS_SUPPRESSION|KO</li> <li>WS_MODIFICATION|KO</li> <li>
    * WS_RECUPERATION_METAS|KO</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV6(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // -- WS_SUPPRESSION|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_SUPPRESSION|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // -- WS_MODIFICATION|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_MODIFICATION|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // -- WS_RECUPERATION_METAS|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_RECUPERATION_METAS|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);
   }

   /**
    * Référentiel des événements en V7 Ajout des évenements : <li>
    * WS_AJOUT_NOTE|KO</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV7(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // -- WS_AJOUT_NOTE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_AJOUT_NOTE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V8 Ajout des évenements : <li>
    * WS_GET_DOC_FORMAT_ORIGINE|KO</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV8(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // -- WS_GET_DOC_FORMAT_ORIGINE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_GET_DOC_FORMAT_ORIGINE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V9 Ajout des évenements : <li>
    * DFCE_DEPOT_ATTACHC|OK</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV9(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // DFCE_DEPOT_ATTACH|OK
      // dans le journal des événements SAE avec all_infos
      updater = cfTmpl.createUpdater("DFCE_DEPOT_ATTACH|OK");
      CassandraUtils.addColumn("JOURN_EVT", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V10 Ajout des évenements : <li>
    * WS_SUPPRESSION_MASSE|KO</li> <li>WS_RESTORE_MASSE|KO</li> <li>
    * SUPPRESSION_MASSE|KO</li> <li>RESTORE_MASSE_KO</li> <li>
    * DFCE_CORBEILLE_DOC|OK</li> <li>DFCE_RESTORE_DOC|OK</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV10(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // WS_SUPPRESSION_MASSE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_SUPPRESSION_MASSE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_RESTORE_MASSE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_RESTORE_MASSE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // SUPPRESSION_MASSE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("SUPPRESSION_MASSE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // RESTORE_MASSE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("RESTORE_MASSE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // DFCE_CORBEILLE_DOC|OK
      // dans le journal des événements SAE avec all_infos
      updater = cfTmpl.createUpdater("DFCE_CORBEILLE_DOC|OK");
      CassandraUtils.addColumn("JOURN_EVT", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // DFCE_RESTORE_DOC|OK
      // dans le journal des événements SAE avec all_infos
      updater = cfTmpl.createUpdater("DFCE_RESTORE_DOC|OK");
      CassandraUtils.addColumn("JOURN_EVT", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V11 Ajout des évenements : <li>
    * WS_ETAT_TRAITEMENTS_MASSE|KO</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielEvenementV11(Keyspace keyspace) {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // WS_ETAT_TRAITEMENTS_MASSE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_ETAT_TRAITEMENTS_MASSE|KO");
      CassandraUtils.addColumn("REG_TECHNIQUE", allInfos,
            StringSerializer.get(), ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Ajout des données dans le référentiel des formats : <li>fmt/354</li> <li>
    * crtl/1</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielFormat(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = null;

      String formatKey = "fmt/354";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey, "PDF/A 1b",
            "pdf", "application/pdf", Boolean.TRUE, null, "pdfaIdentifierImpl",
            "pdfaValidatorImpl", null);

      formatKey = "crtl/1";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Journal SAE, XML basé sur XSD, compressé en tar.gz", "tar.gz",
            "application/x-gzip", Boolean.FALSE);

   }

   /**
    * Ajout des données dans le référentiel des formats en V2 : <li>fmt/353</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielFormatV2(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = null;

      LOG.info("Mise à jour du référentiel des formats");

      String formatKey = "fmt/353";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier TIFF", "tif", "image/tiff", Boolean.FALSE, null, null,
            null,
            "tiffToPdfConvertisseurImpl");

   }

   /**
    * Ajout des données dans le référentiel des formats en V3 : <li>x-fmt/111</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielFormatV3(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = null;

      String nouveauFormat = "x-fmt/111";

      addNewColumnToReferentielFormat(cfTmpl, updater, nouveauFormat,
            "Fichier TXT (par exemple cold)", "txt", "text/plain", Boolean.TRUE);

   }

   /**
    * Ajout des données dans le référentiel des formats en V4 : <li>pdf</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielFormatV4(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = null;

      String nouveauFormat = "pdf";

      addNewColumnToReferentielFormat(cfTmpl, updater, nouveauFormat,
            "Tous fichiers PDF sans précision de version", "pdf",
            "application/pdf", Boolean.TRUE, null, null, null,
            "pdfSplitterImpl");
   }

   /**
    * Ajout des données dans le référentiel des formats en V5 : <li>fmt/13</li>
    * <li>fmt/44</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielFormatV5(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = null;

      String formatPNG = "fmt/13";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatPNG,
            "Fichier PNG", "png", "image/png", Boolean.TRUE);

      String formatJPG = "fmt/44";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatJPG,
            "Fichier JPG", "jpg", "image/jpeg", Boolean.TRUE);

   }

   /**
    * Ajout des données dans le référentiel des formats en V6 : <li>migrationW2</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielFormatV6(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = null;

      String formatKey = "migrationW2";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier migration WAT2", "*", "application/octet-stream",
            Boolean.FALSE);


      formatKey = "doc";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier MS Word version 97/2003", "doc", "application/msword",
            Boolean.TRUE);

      formatKey = "docx";
      addNewColumnToReferentielFormat(
            cfTmpl,
            updater,
            formatKey,
            "Fichier MS Word version 2007 et +",
            "docx",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            Boolean.TRUE);

      formatKey = "docm";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier macro MS Word", "docm",
            "application/vnd.ms-word.document.macroEnabled.12", Boolean.FALSE);

      formatKey = "xls";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier MS Excel version 97/2003", "xls",
            "application/vnd.ms-excel", Boolean.TRUE);

      formatKey = "xlsx";     
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier MS Excel version 2007 et +", "xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Boolean.TRUE);

      formatKey = "xlsm";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier macro MS Excel", "xlsm",
            "application/vnd.ms-excel.sheet.macroEnabled.12", Boolean.FALSE);

      formatKey = "xlsb";      
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier binaire MS Excel", "xlsb",
            "application/vnd.ms-excel.sheet.binary.macroEnabled.12", Boolean.FALSE);

      formatKey = "csv";     
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier image numerique", "gif",
            "image/gif", Boolean.TRUE);

      formatKey = "gif";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier tableur (Comma Separated Values)", "csv",
            "text/csv", Boolean.TRUE);

      formatKey = "ppt";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier MS PowerPoint version 97/2003", "ppt",
            "application/vnd.ms-powerpoint", Boolean.TRUE);      

      formatKey = "xml";      
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier à langage de balisage extensible (eXtensible Markup Language)", "xml",
            "application/xhtml+xml", Boolean.TRUE); 

      formatKey = "zip";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier archive ZIP", "zip",
            "application/zip", Boolean.FALSE); 

      formatKey = "html";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier à langage de balisage d'hypertexte (HyperText Markup Language)", "html",
            "text/html", Boolean.TRUE); 
   }



   /**
    * Ajout des données dans le référentiel des formats en V6 : <li>migrationW2</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addReferentielFormatV6Bis(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater = null;

      String formatKey = "pptx";
      addNewColumnToReferentielFormat(
            cfTmpl,
            updater,
            formatKey,
            "Fichier MS PowerPoint version 2007 et +",
            "pptx",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            Boolean.TRUE, Boolean.FALSE);

      formatKey = "pptm";
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey,
            "Fichier MS PowerPoint macro", "pptm",
            "application/vnd.ms-powerpoint.presentation.macroEnabled.12",
            Boolean.FALSE, Boolean.FALSE);

   }


   /**
    * Modification des données dans le référentiel des formats : Ajout d'un
    * convertisseur pour le format <li>fmt/354</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void modifyReferentielFormatFmt354(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      LOG.info("Mise à jour du référentiel des formats");

      updater = cfTmpl.createUpdater("fmt/354");
      CassandraUtils.addColumn("convertisseur", "pdfSplitterImpl",
            StringSerializer.get(), StringSerializer.get(), updater);
      cfTmpl.update(updater);
      LOG.info("Format modifié : fmt/354");
   }

   /**
    * Modification des données dans le référentiel des formats : Ajout d'un
    * convertisseur pour le format <li>fmt/353</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void modifyReferentielFormatFmt353(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      LOG.info("Mise à jour du référentiel des formats");

      updater = cfTmpl.createUpdater("fmt/353");
      CassandraUtils.addColumn("extension", "tif,tiff", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      LOG.info("Format modifié : fmt/353");
   }

   /**
    * Modification des données dans le référentiel des formats : Ajout d'un
    * convertisseur pour le format <li>fmt/44</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void modifyReferentielFormatFmt44(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      LOG.info("Mise à jour du référentiel des formats");

      updater = cfTmpl.createUpdater("fmt/44");
      CassandraUtils.addColumn("extension", "jpg,jpeg", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      LOG.info("Format modifié : fmt/44");
   }

   /**
    * Modification des données dans le référentiel des formats : Ajout d'un
    * convertisseur pour le format <li>crtl/1</li>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void modifyReferentielFormatCrtl1(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      LOG.info("Mise à jour du référentiel des formats");

      updater = cfTmpl.createUpdater("crtl/1");
      CassandraUtils.addColumn("extension", "tar.gz,gz",
            StringSerializer.get(), StringSerializer.get(), updater);
      cfTmpl.update(updater);
      CassandraUtils.addColumn(AUTORISE_GED, Boolean.FALSE,
            StringSerializer.get(), BooleanSerializer.get(), updater);
      cfTmpl.update(updater);
      LOG.info("Format modifié : crtl/1");
   }

   /**
    * Les profils de controle pour les formats.<br/>
    * Ajout de 3 éléments pour le format fmt/354:
    * <ul>
    * <li>Identification seule</li>
    * <li>Validation seule</li>
    * <li>Identification et validation</li>
    * </ul>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addFormatControleProfil(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitFormatControlProfil", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      updater = cfTmpl.createUpdater("IDENT_FMT_354");
      CassandraUtils
      .addColumn(
            "description",
            "format de controle gérant exclusivement l'identification du fmt/354",
            StringSerializer.get(), StringSerializer.get(), updater);

      FormatProfil profil = new FormatProfil();
      profil.setFileFormat("fmt/354");
      profil.setFormatIdentification(true);
      profil.setFormatValidation(false);
      profil.setFormatValidationMode("STRICT");
      CassandraUtils.addColumn("controlProfil", profil, StringSerializer.get(),
            FormatProfilSerializer.get(), updater);
      cfTmpl.update(updater);

      updater = cfTmpl.createUpdater("VALID_FMT_354");
      CassandraUtils.addColumn("description",
            "format de controle gérant exclusivement la validation du fmt/354",
            StringSerializer.get(), StringSerializer.get(), updater);

      profil = new FormatProfil();
      profil.setFileFormat("fmt/354");
      profil.setFormatIdentification(false);
      profil.setFormatValidation(true);
      profil.setFormatValidationMode("STRICT");
      CassandraUtils.addColumn("controlProfil", profil, StringSerializer.get(),
            FormatProfilSerializer.get(), updater);
      cfTmpl.update(updater);

      updater = cfTmpl.createUpdater("VALID_FMT_354");
      CassandraUtils.addColumn("description",
            "format de controle gérant exclusivement la validation du fmt/354",
            StringSerializer.get(), StringSerializer.get(), updater);

      updater = cfTmpl.createUpdater("IDENT_VALID_FMT_354");
      profil = new FormatProfil();
      profil.setFileFormat("fmt/354");
      profil.setFormatIdentification(true);
      profil.setFormatValidation(true);
      profil.setFormatValidationMode("STRICT");
      CassandraUtils.addColumn("controlProfil", profil, StringSerializer.get(),
            FormatProfilSerializer.get(), updater);
      cfTmpl.update(updater);
   }

   /**
    * Methode permettant de d'ajouter une nouvelle colonne à la CF
    * ReferentielFormat.
    * 
    * @param cfTmpl
    *           Template updater
    * @param updater
    *           Updater
    * @param visualisable
    *           True si visualisable, false sinon
    * @param typeMime
    *           Type mime document
    * @param extension
    *           Extension document
    * @param description
    *           Description document
    * @param formatKey
    *           Format de la clef
    * @param autoriseEnGed
    *           Autorisé en GED
    */
   private static void addNewColumnToReferentielFormat(
         ColumnFamilyTemplate<String, String> cfTmpl,
         ColumnFamilyUpdater<String, String> updater, String formatKey,
         String description, String extension, String typeMime,
         Boolean visualisable, Boolean autoriseEnGed) {
      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey, description,
            extension, typeMime, visualisable, autoriseEnGed, null, null, null);

   }

   /**
    * 
    * Methode permettant de d'ajouter une nouvelle colonne à la CF
    * ReferentielFormat.
    * 
    * @param cfTmpl
    *           Template updater
    * @param updater
    *           Updater
    * @param visualisable
    *           True si visualisable, false sinon
    * @param typeMime
    *           Type mime document
    * @param extension
    *           Extension document
    * @param description
    *           Description document
    * @param formatKey
    *           Format de la clef
    */
   private static void addNewColumnToReferentielFormat(
         ColumnFamilyTemplate<String, String> cfTmpl,
         ColumnFamilyUpdater<String, String> updater, String formatKey,
         String description, String extension, String typeMime,
         Boolean visualisable) {

      addNewColumnToReferentielFormat(cfTmpl, updater, formatKey, description,
            extension, typeMime, visualisable, null, null, null, null);
   }

   /**
    * Methode permettant de d'ajouter une nouvelle colonne à la CF
    * ReferentielFormat.
    * 
    * @param cfTmpl
    *           Template updater
    * @param updater
    *           Updater
    * @param visualisable
    *           True si visualisable, false sinon
    * @param typeMime
    *           Type mime document
    * @param extension
    *           Extension document
    * @param description
    *           Description document
    * @param formatKey
    *           Format de la clef
    * @param validator
    *           Validateur
    * @param identifier
    *           Identifier
    * @param convertisseur
    *           Convertisseur
    * @param autoriseEnGed
    *           Autorisé en GED
    */
   private static void addNewColumnToReferentielFormat(
         ColumnFamilyTemplate<String, String> cfTmpl,
         ColumnFamilyUpdater<String, String> updater, String formatKey,
         String description, String extension, String typeMime,
         Boolean visualisable, Boolean autoriseEnGed, String identifier,
         String validator,
         String convertisseur) {
      LOG.info("Mise à jour du référentiel des formats");
      if (cfTmpl == null || (formatKey == null)
            || (formatKey != null && !formatKey.isEmpty())) {
         LOG.error("Mise à jour impossible car l'updater ne peut être créer : formatKey = "
               + formatKey);
      }
      updater = cfTmpl.createUpdater(formatKey);
      CassandraUtils.addColumn("idFormat", formatKey, StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      if (description != null && !description.isEmpty()) {
         CassandraUtils.addColumn("description", description,
               StringSerializer.get(), StringSerializer.get(), updater);
         cfTmpl.update(updater);
      }

      if (extension != null && !extension.isEmpty()) {
         CassandraUtils.addColumn("extension", extension,
               StringSerializer.get(), StringSerializer.get(), updater);
         cfTmpl.update(updater);
      }

      if (typeMime != null && !typeMime.isEmpty()) {
         CassandraUtils.addColumn("typeMime", typeMime, StringSerializer.get(),
               StringSerializer.get(), updater);
         cfTmpl.update(updater);
      }

      if (visualisable != null) {
         CassandraUtils.addColumn("visualisable", visualisable,
               StringSerializer.get(), BooleanSerializer.get(), updater);
         cfTmpl.update(updater);
      }

      if (autoriseEnGed != null) {
         CassandraUtils.addColumn(AUTORISE_GED, autoriseEnGed,
               StringSerializer.get(), BooleanSerializer.get(), updater);
         cfTmpl.update(updater);
      }

      if (identifier != null && !identifier.isEmpty()) {
         CassandraUtils.addColumn("identifieur", identifier,
               StringSerializer.get(), StringSerializer.get(), updater);
         cfTmpl.update(updater);
      }

      if (validator != null && !validator.isEmpty()) {
         CassandraUtils.addColumn("validator", validator,
               StringSerializer.get(), StringSerializer.get(), updater);
         cfTmpl.update(updater);
      }

      if (convertisseur != null && !convertisseur.isEmpty()) {
         CassandraUtils.addColumn("convertisseur", convertisseur,
               StringSerializer.get(), StringSerializer.get(), updater);
         cfTmpl.update(updater);
      }

      LOG.info("Format ajouté : {}", formatKey);
   }

   /**
    * 
    * Methode permettant d'ajouter la ligne autorisé en GED dans le CF
    * ReferentielFormat.
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addColumnAutoriseGEDReferentielFormat(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());
      // tar.gz
      addColumnAutoriseGED("crtl/1", Boolean.TRUE, cfTmpl);
      // csv
      addColumnAutoriseGED("csv", Boolean.FALSE, cfTmpl);
      // doc
      addColumnAutoriseGED("doc", Boolean.FALSE, cfTmpl);
      // docm
      addColumnAutoriseGED("docm", Boolean.FALSE, cfTmpl);
      // docx
      addColumnAutoriseGED("docx", Boolean.FALSE, cfTmpl);
      // png
      addColumnAutoriseGED("fmt/13", Boolean.TRUE, cfTmpl);
      // tif
      addColumnAutoriseGED("fmt/353", Boolean.TRUE, cfTmpl);
      // pdf
      addColumnAutoriseGED("fmt/354", Boolean.TRUE, cfTmpl);
      // jpeg
      addColumnAutoriseGED("fmt/44", Boolean.TRUE, cfTmpl);
      // gif
      addColumnAutoriseGED("gif", Boolean.FALSE, cfTmpl);
      // html
      addColumnAutoriseGED("html", Boolean.FALSE, cfTmpl);
      // migrationW2
      addColumnAutoriseGED("migrationW2", Boolean.FALSE, cfTmpl);
      // pdf
      addColumnAutoriseGED("pdf", Boolean.TRUE, cfTmpl);
      // ppt
      addColumnAutoriseGED("ppt", Boolean.FALSE, cfTmpl);
      // xls
      addColumnAutoriseGED("xls", Boolean.TRUE, cfTmpl);
      // xlsb
      addColumnAutoriseGED("xlsb", Boolean.TRUE, cfTmpl);
      // xlsm
      addColumnAutoriseGED("xlsm", Boolean.TRUE, cfTmpl);
      // xlsx
      addColumnAutoriseGED("xlsx", Boolean.TRUE, cfTmpl);
      // xml
      addColumnAutoriseGED("xml", Boolean.FALSE, cfTmpl);
      // zip
      addColumnAutoriseGED("zip", Boolean.FALSE, cfTmpl);

      addColumnAutoriseGED("x-fmt/111", Boolean.TRUE, cfTmpl);

   }

   /**
    * 
    * Methode permettant de d'ajouter la ligne autorisé en GED dans la colonne
    * spécifiée.
    * 
    * @param identifiant
    *           identifiant colonne
    * @param value
    *           valeur dans la ligne
    * @param cfTmpl
    *           Template updater
    */
   private static void addColumnAutoriseGED(String identifiant, Boolean value,
         ColumnFamilyTemplate<String, String> cfTmpl) {
      LOG.info("Ajout colonne autoriseGED dans le référentiel des formats");
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(identifiant);
      HColumn<String, Boolean> column = HFactory.createColumn(AUTORISE_GED,
            value, StringSerializer.get(), BooleanSerializer.get());
      updater.setColumn(column);

      cfTmpl.update(updater);

      LOG.info("Colonne ajoutée pour le format {}", identifiant);
   }

}
