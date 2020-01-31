package fr.urssaf.javaDriverTest;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;

import fr.urssaf.javaDriverTest.dao.CassandraSessionFactory;
import fr.urssaf.javaDriverTest.helper.Dumper;

/**
 * TODO (ac75007394) Description du type
 */
public class SaeDumpTest {

   CqlSession session;

   PrintStream sysout;

   Dumper dumper;

   @Before
   public void init() throws Exception {
      String servers;
      // servers = "cnp69saecas1,cnp69saecas2,cnp69saecas3";
      // servers = "cnp69saecas4.cer69.recouv, cnp69saecas5.cer69.recouv, cnp69saecas6.cer69.recouv";
      // servers = "cnp69gntcas1,cnp69gntcas2,cnp69gntcas3";
      // servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv,cnp69intgntcas3.gidn.recouv";
      // servers = "cnp69pregntcas1, cnp69pregntcas2";
      // servers = "cnp69givngntcas1, cnp69givngntcas2";
      // servers = "hwi69gincleasaecas1.cer69.recouv,hwi69gincleasaecas2.cer69.recouv";
      // servers = "hwi69gincleasaecas1.cer69.recouv,hwi69gincleasaecas2.cer69.recouv";
      // servers = "cnp69pprodsaecas1,cnp69pprodsaecas2,cnp69pprodsaecas3"; //Préprod
      // servers = "cnp69pprodsaecas6"; //Préprod
      // servers = "cnp69pregnscas1.cer69.recouv,cnp69pregnscas1.cer69.recouv,cnp69pregnscas1.cer69.recouv"; // Vrai préprod
      // servers = "10.213.82.56";
      servers = "cnp6gnscvecas01.cve.recouv,cnp3gnscvecas01.cve.recouv,cnp7gnscvecas01.cve.recouv"; // Charge
      // servers = "cnp31miggntcas3.cer31.recouv,cnp31miggntcas4.cer31.recouv"; // Migration cql
      // servers = "cnp3gntcvecas1.cve.recouv,cnp6gntcvecas1.cve.recouv,cnp7gntcvecas1.cve.recouv"; // Charge GNT
      // servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv,cnp69intgntcas3.gidn.recouv";
      // servers = "cer69imageint9.cer69.recouv";
      // servers = "cer69imageint10.cer69.recouv";
      // servers = "10.207.81.29";
      // servers = "hwi69givnsaecas1.cer69.recouv,hwi69givnsaecas2.cer69.recouv";
      // servers = "hwi69devsaecas1.cer69.recouv,hwi69devsaecas2.cer69.recouv";
      // servers = "hwi69ginsaecas2.cer69.recouv";
      // servers = "cer69-saeint3";
      // servers = "cnp69devgntcas1.gidn.recouv, cnp69devgntcas2.gidn.recouv";
      // servers = "cnp69dev2gntcas1.gidn.recouv, cnp69dev2gntcas2.gidn.recouv";
      // servers = "cnp69miggntcas1.gidn.recouv,cnp69miggntcas2.gidn.recouv"; // Migration cassandra V2
      // servers = "cnp69dev2gntcas1.gidn.recouv";
      // servers = "cnp69devgntcas1.gidn.recouv,cnp69devgntcas2.gidn.recouv";
      // servers = "hwi69intgnscas1.gidn.recouv,hwi69intgnscas2.gidn.recouv";
      // servers = "cnp31devpicgntcas1.gidn.recouv,cnp31devpicgntcas2.gidn.recouv";
      // servers = "cnp69gincleagntcas1.cer69.recouv,cnp69gincleagntcas2.cer69.recouv";
      // servers = "hwi69progednatgnspaj1bocas1,hwi69progednatgnspaj1bocas2";

      // final String cassandraLocalDC = "DC1";
      final String cassandraLocalDC = "LYON_SP";
      session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);

      sysout = new PrintStream(System.out, true, "UTF-8");
      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("c:/temp/out.txt");
      dumper = new Dumper(sysout);
   }

   @After
   public void close() throws Exception {
      session.close();
   }

   @Test
   public void test() {
      final ResultSet rs = session.execute("select count(*) from \"SAE\".\"JobRequest\"");
      dumper.dumpRows(rs);
   }

   // INSERT INTO modeapi (cfname,mode) VALUES ('traceregsecuriteindexcql','DUAL_MODE');

   // Table droitactionunitairecql

   @Test // 1 fois
   public void createTable_droitactionunitairecqlTest() throws Exception {
      session.execute("CREATE TABLE IF NOT EXISTS \"SAE\".droitactionunitairecql (\r\n" +
            "  code text,\r\n" +
            "  description text,\r\n" +
            "  PRIMARY KEY (code)\r\n" +
            ");");
   }

   @Test
   public void truncateTable_droitactionunitairecqlTest() throws Exception {
      session.execute("TRUNCATE  \"SAE\".droitactionunitairecql");
   }

   @Test
   public void insert_deblocage_droitactionunitairecqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".droitactionunitairecql (code,description)"
            + " VALUES ('deblocage','deblocage de traitement de masse');");
   }

   @Test
   public void dump_droitactionunitairecqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".droitactionunitairecql limit 100");
      dumper.dumpRows(rs);
   }

   // Table droitcontratservicecql

   @Test // 1 fois
   public void createTable_droitcontratservicecqlTest() throws Exception {
      session.execute("CREATE TABLE IF NOT EXISTS  \"SAE\".droitcontratservicecql (\r\n" +
            "\r\n" +
            "  code text,\r\n" +
            "  description text,\r\n" +
            "  libelle text,\r\n" +
            "  listPki list<text>,\r\n" +
            "  listeCert list<text>,\r\n" +
            "  verifNommage boolean,\r\n" +
            "  viDuree bigint,\r\n" +
            "  PRIMARY KEY (code)\r\n" +
            ");");
   }

   @Test
   public void truncateTable_droitcontratservicecqlTest() throws Exception {
      session.execute("TRUNCATE  \"SAE\".droitcontratservicecql");
   }

   @Test
   public void insert_CS_SAEL_droitcontratservicecqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".droitcontratservicecql "
            + "(code, description, libelle, listPki, listeCert, verifNommage, viDuree)"
            + " VALUES ('CS_SAEL','Contrat de service pour le client SAEL','CS_SAEL',"
            + "['CN=IGC/A','CN=ACOSS_Reseau_des_URSSAF'],['CN=SAEL'],true,7200);");
   }

   @Test
   public void dump_droitcontratservicecqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".droitcontratservicecql limit 100");
      dumper.dumpRows(rs);
   }

   // Table droitformatcontrolprofilcql
   @Test // 1 fois
   public void createTable_droitformatcontrolprofilcqlTest() throws Exception {
      session.execute("CREATE TABLE IF NOT EXISTS  \"SAE\".droitformatcontrolprofilcql (\r\n" +
            "  formatCode text,\r\n" +
            "  description  text,\r\n" +
            "  controlProfil text,\r\n" +
            "  PRIMARY KEY (formatCode)\r\n" +
            ");");
   }

   @Test
   public void truncateTable_droitformatcontrolprofilcqlTest() throws Exception {
      session.execute("TRUNCATE  \"SAE\".droitformatcontrolprofilcql");
   }

   @Test
   public void insert_VALID_FMT_354_droitformatcontrolprofilcqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".droitformatcontrolprofilcql (formatCode,controlProfil,description)"
            + " VALUES ('VALID_FMT_354','{\"formatIdentification\":false,\"formatValidation\":true,\"formatValidationMode\":\"STRICT\""
            + ",\"fileFormat\":\"fmt/354\"} ','format de controle gérant exclusivement la validation du fmt/354');");
   }

   @Test
   public void testDump_droitformatcontrolprofilcql() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".droitformatcontrolprofilcql limit 100");
      dumper.dumpRows(rs);
   }

   // Table droitpagmcql
   @Test // 1 fois
   public void createTable_droitpagmcqlTest() throws Exception {

      session.execute("CREATE TABLE IF NOT EXISTS  \"SAE\".droitpagmcql (\r\n" +
            " code text,\r\n" +
            " codePagm text,\r\n" +
            "  description text,\r\n" +
            "  pagma text,\r\n" +
            "  pagmp text,\r\n" +
            "  pagmf text,\r\n" +
            "  parametres map<text,text>,\r\n" +
            "  compressionPdfActive boolean,\r\n" +
            "  seuilCompressionPdf int,\r\n" +
            "  PRIMARY KEY (code,codePagm)\r\n" +
            ");");
   }

   @Test
   public void truncateTable_droitpagmcqlTest() throws Exception {
      session.execute("TRUNCATE \"SAE\".droitpagmcql");
   }

   @Test
   public void insert_CS_CIME_droitpagmcqlTest() throws Exception {
      final PreparedStatement prepared = session.prepare(
            "INSERT INTO \"SAE\".droitpagmcql(code,codePagm,description, pagma, pagmp, pagmf,parametres,compressionPdfActive,seuilCompressionPdf)"
                  + " VALUES (?,?,?,?,?,?,?,?,?)");

      final BoundStatement bound = prepared.bind("CS_RECHERCHE_DOCUMENTAIRE",
            "PAGM_RECHERCHE_DOCUMENTAIRE_GNS",
            "Droits de la recherche documentaire",
            "PAGM_RECHERCHE_DOCUMENTAIRE_GNS_PAGMa",
            "PAGM_RECHERCHE_DOCUMENTAIRE_RECHERCHE_GNS_PAGMp",
            "",
            null,
            false,
            0);
      session.execute(bound);


   }

   @Test
   public void dump_droitpagmcqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".droitpagmcql limit 100");
      dumper.dumpRows(rs);
   }

   // Table droitpagmpcql
   /**
    * Création de la table droitpagmacql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_droitpagmacqlTest() throws Exception {
      session.execute("CREATE TABLE IF NOT EXISTS  \"SAE\".droitpagmacql (\r\n" +
            "  code text,\r\n" +
            "  actionUnitaires List<text>,\r\n" +
            "  PRIMARY KEY (code)\r\n" +
            ");");
   }

   /**
    * Suppression des données de la table droitpagmacql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_droitpgamcqlTest() throws Exception {
      session.execute("TRUNCATE  \"SAE\".droitpagmacql");
   }

   /**
    * Insertion dans la table droitpagmacql
    * 
    * @throws Exception
    */
   @Test
   public void insert__droitpagmacqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".droitpagmacql \r\n" +
            "    (code, actionUnitaires)\r\n" +
            "      VALUES ('PAGM_V2_ARCHIVAGE_PD12R_PD22_L07_PAGMa',\r\n" +
            "    ['archivage_masse','reprise_masse']);");
   }

   /**
    * Récupération des données de la table droitpagmacql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_droitpagmacqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".droitpagmacql limit 100");
      dumper.dumpRows(rs);
   }

   // Table droitpagmpcql
   /**
    * Création de la table droitpagmpcql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_droitpagmpcqlTest() throws Exception {
      session.execute("CREATE TABLE IF NOT EXISTS  \"SAE\".droitpagmpcql (\r\n" +
            "  code text,\r\n" +
            "  description text,\r\n" +
            "  prmd text,\r\n" +
            "  PRIMARY KEY (code)\r\n" +
            ");");
   }

   /**
    * Suppression des données de la table droitpgampcql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_droitpagmpcqlTest() throws Exception {
      session.execute("TRUNCATE  \"SAE\".droitpagmpcql");
   }

   /**
    * Insertion dans la table droitpgampcql
    * 
    * @throws Exception
    */
   @Test
   public void insert__droitpagmpcqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".droitpagmpcql \r\n" +
            "    (code, description, prmd)\r\n" +
            "      VALUES ('PAGM_V2_ARCHIVAGE_QD12K_QD12_L07_PAGMp',\r\n" +
            "   'QD12K QD12.L07','PRMD_V2_QD12K_QD12_L07');");
   }

   /**
    * Récupération des données de la table droitpagmpcql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_droitpagmpcqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".droitpagmpcql limit 100");
      dumper.dumpRows(rs);
   }

   // Table droitprmdcql
   /**
    * Création de la table droitpagmpcql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_droitprmdcqlTest() throws Exception {
      session.execute("CREATE TABLE IF NOT EXISTS  \"SAE\".droitprmdcql (\r\n" +
            "  code text,\r\n" +
            "  bean text,\r\n" +
            "  description text,\r\n" +
            "  lucene text,\r\n" +
            "  metadata map<text,frozen<list<text>>>,\r\n" +
            "  PRIMARY KEY (code)\r\n" +
            ");");
   }

   // pagm frozen<map<text,frozen<map<text,text>>>>,\r\n
   /**
    * Suppression des données de la table droitprmdcql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_droitprmdcqlTest() throws Exception {
      session.execute(" TRUNCATE \"SAE\".droitprmdcql");
   }

   /**
    * Insertion dans la table droitprmdcql
    * 
    * @throws Exception
    */
   @Test
   public void insert__droitprmdcqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".droitprmdcql \r\n" +
            "    (code,bean,description, lucene,metadata)\r\n" +
            "      VALUES ('PRMD_V2_PCA1D_PCA1_L04',\r\n" +
            "      '',\r\n" +
            "     'V2 - PCA1D PCA1.L04',\r\n" +
            "     '(CodeRND:3.2.2.A.X OR CodeRND:3.2.4.C.X OR CodeRND:3.2.1.5.2 OR CodeRND:3.2.3.2.2 OR CodeRND:3.2.2.3.1 OR CodeRND:3.2.1.5.1" +
            "     OR CodeRND:3.2.5.2.3) AND ApplicationProductrice:ADELAIDE AND DomaineCotisant:true',\r\n" +
            "   {'CodeRND':['3\\.2\\.2\\.A\\.X','3\\.2\\.4\\.C\\.X','3\\.2\\.1\\.5\\.2','3\\.2\\.3\\.2\\.2','3\\.2\\.2\\.3\\.1','3\\.2\\.1\\.5\\.1','3\\.2\\.5\\.2\\.3'],"
            + " 'FormatFichier':['fmt/354'], 'ApplicationProductrice':['ADELAIDE'],'DomaineCotisant':['true']});");

      /*
       * final PreparedStatement prepared = session.prepare(
       * "INSERT INTO \"SAE\".droitprmdcql(code,bean,description, lucene, metadata)"
       * + " VALUES (?,?,?,?,?)");
       * final Map<String, Object> metadata = new HashMap<>();
       * final List<String> list = new ArrayList<>();
       * list.add("3\\\\.2\\\\.1\\\\.5\\\\.1");
       * list.add("3\\\\.2\\\\.1\\\\.5\\\\.1");
       * metadata.put("CodeRND", list);
       * final BoundStatement bound = prepared.bind("PRMD_V2_PI06I_PI06_L09",
       * "",
       * "V2 - PI06I PI06.L09",
       * "CodeRND:2.1.4.5.1 AND ApplicationProductrice:ADELAIDE AND DomaineCotisant:true",
       * metadata);
       * session.execute(bound);
       */
   }

   /**
    * Récupération des données de la table droitprmdcql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_droitprmdcqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".droitpagmpcql limit 100");
      dumper.dumpRows(rs);
   }

   // Table droitpagmfcql
   /**
    * Création de la table droitpagmfcql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_droitpagmfcqlTest() throws Exception {
      session.execute("CREATE TABLE IF NOT EXISTS  \"SAE\".droitpagmfcql (\r\n" +
            "  codePagmf text,\r\n" +
            "  description text,\r\n" +
            "  codeFormatControlProfil text,\r\n" +
            "  PRIMARY KEY (codePagmf)\r\n" +
            ");");
   }

   /**
    * Suppression des données de la table droitpgamfcql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_droitpagmfcqlTest() throws Exception {
      session.execute("TRUNCATE  \"SAE\".droitpagmfcql");
   }

   /**
    * Insertion dans la table droitpgamfcql
    * 
    * @throws Exception
    */
   @Test
   public void insert__droitpagmfcqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".droitpagmfcql \r\n" +
            "    (codePagmf, description, codeFormatControlProfil)\r\n" +
            "      VALUES ('PAGMF000',\r\n" +
            "   'CODE PAGMF000','VALID_FMT_354');");
   }

   /**
    * Récupération des données de la table droitpagmfcql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_droitpagmfcqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".droitpagmfcql limit 100");
      dumper.dumpRows(rs);
   }

   // Table parameters
   /**
    * Création de la table parameterscql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_parameterscqlTest() throws Exception {
      session.execute("CREATE TABLE IF NOT EXISTS  \"SAE\".parameterscql (\r\n" +
            "  typeParameters text,\r\n" +
            "  parameters map<text,text>,\r\n" +
            "  PRIMARY KEY (typeParameters)\r\n" +
            ");");
   }

   /**
    * Suppression des données de la table parameterscql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_parameterscqlTest() throws Exception {
      session.execute("DROP TABLE  \"SAE\".parameterscql");
   }

   /**
    * Insertion dans la table parameterscql
    * 
    * @throws Exception
    */
   @Test
   public void insert__droitparameterscqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".parameterscql \r\n" +
            "    (typeParameters, parameters)\r\n" +
            "      VALUES ('parametresRnd',\r\n" +
            "   {'VERSION_RND_DATE_MAJ':'Tue Mar 05 00:38:12 CET 2019','VERSION_RND_NUMERO':'13.11'});");
   }

   /**
    * Récupération des données de la table parameterscql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_parameterscqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".parameterscql limit 100");
      dumper.dumpRows(rs);
   }

   // Table metadata
   /**
    * Création de la table metadatacql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_metadatacqlTest() throws Exception {
      session.execute("CREATE TABLE \"SAE\".metadatacql (\r\n" +
            "  codeRND text,\r\n" +
            "  arch  boolean,\r\n" +
            "  cons boolean,\r\n" +
            "  defCons boolean,\r\n" +
            "  descr text,\r\n" +
            "  dictName text,\r\n" +
            "  dispo boolean,\r\n" +
            "  hasDict boolean,\r\n" +
            "  indexs boolean,\r\n" +
            "  int boolean,\r\n" +
            "  label text,\r\n" +
            "  leftTrim boolean,\r\n" +
            "  length int,\r\n" +
            "  pattern text,\r\n" +
            "  reqArch boolean,\r\n" +
            "  reqStor boolean,\r\n" +
            "  rightTrim boolean,\r\n" +
            "  sCode text,\r\n" +
            "  search boolean,\r\n" +
            "  transf boolean,\r\n" +
            "  type text,\r\n" +
            "  updates boolean,\r\n" +
            "  modif boolean,\r\n" +
            "  PRIMARY KEY (codeRND));");
   }
   /**
    * Suppression des données de la table metadatacql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_metadatacqlTest() throws Exception {
      session.execute("TRUNCATE \"SAE\".metadatacql");
   }
   /**
    * Insertion dans la table metadatacql
    * 
    * @throws Exception
    */
   @Test
   public void insert__metadatacqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".metadatacql \r\n" +
            "    (codeRND, arch, cons, defCons, descr, dictName, dispo, hasDict, indexs, int, label, leftTrim, length, pattern, reqArch, reqStor, rightTrim, "
            + " sCode, search, transf, type, updates, modif)  \r\n"
            + "      VALUES ('ApplicationProductrice', true, true, false, 'Code de l''application qui a produit le fichier','', true, false, false, false,"
            + "  'Application Productrice du document', true, 15, '', true, true, true, 'apr', true, true, '', false, false); \r\n");
   }

   /**
    * Récupération des données de la table metadatacql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_metadatacqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".metadatacql limit 100");
      dumper.dumpRows(rs);
   }

   // Table referentielformatcql
   /**
    * Création de la table parameterscql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_referentielformatcqlTest() throws Exception {
      session.execute("CREATE TABLE \"SAE\".referentielformatcql (\r\n" +
            "  codeFormat text,\r\n" +
            "  autoriseGED boolean,\r\n" +
            "  convertisseur text,\r\n" +
            "  description text,\r\n" +
            "  extension text,\r\n" +
            "  idFormat  text,\r\n" +
            "  identifieur text,\r\n" +
            "  typeMime text,\r\n" +
            "  validator text,\r\n" +
            "  visualisable boolean,\r\n" +
            "  PRIMARY KEY (codeFormat)\r\n" +
            ");");
   }

   /**
    * Suppression des données de la table parameterscql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_referentielformatcqlTest() throws Exception {
      session.execute("TRUNCATE  \"SAE\".referentielformatcql");
   }

   /**
    * Insertion dans la table parameterscql
    * 
    * @throws Exception
    */
   @Test
   public void insert__referentielformatcqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".referentielformatcql \r\n" +
            "    (codeFormat, autoriseGED, convertisseur, description,  extension, idFormat, identifieur, typeMime,validator, visualisable )\r\n" +
            "      VALUES ('fmt/354',true, 'pdfSplitterImpl', 'PDF/A 1b', 'pdf', 'fmt/354', 'pdfaIdentifierImpl',  'application/pdf', 'pdfaValidatorImpl', true  \r\n"
            +
            "   );");
   }

   /**
    * Récupération des données de la table parameterscql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_referentielformatcqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".referentielformatcql limit 100");
      dumper.dumpRows(rs);
   }

   // Table rndcql
   /**
    * Création de la table rndcql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_rndcqlTest() throws Exception {
      session.execute("CREATE TABLE \"SAE\".rndcql (\r\n" +
            "  code text,\r\n" +
            "  codeFonction int,\r\n" +
            "  codeActivite int,\r\n" +
            "  dureeConservation bigint,\r\n" +
            "  cloture boolean,\r\n" +
            "  type text,\r\n" +
            "  libelleEnd text,\r\n" +
            "  PRIMARY KEY (code)\r\n" +
            ");");
   }

   /**
    * Suppression des données de la table rndcql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_rndcqlTest() throws Exception {
      session.execute("TRUNCATE TABLE  \"SAE\".rndcql");
   }

   /**
    * Insertion dans la table rndcql
    * 
    * @throws Exception
    */
   @Test
   public void insert__rndcqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".rndcql \r\n" +
            "    (code, codeFonction, codeActivite, dureeConservation,  cloture, type, libelleEnd)\r\n" +
            "      VALUES ('1.2.1.1.3',1,2, 2555, false, 'ARCHIVABLE_AED', 'LIASSE AFFILIATION ACT UR COMPETENTE' \r\n" +
            "   );");
   }

   /**
    * Récupération des données de la table rndcql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_rndcqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".rndcql limit 100");
      dumper.dumpRows(rs);
   }

   // Table correspondancesrndcql
   /**
    * Création de la table correspondancesrndcql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_correspondancesrndcqlTest() throws Exception {
      session.execute("CREATE TABLE  \"SAE\".correspondancesrndcql (\r\n" +
            "  codeTemporaire text,\r\n" +
            "  codeDefinitif text,\r\n" +
            "  versionCourante text,\r\n" +
            "  etat text,\r\n" +
            "  dateDebutMaj timestamp,\r\n" +
            "  dateFinMaj timestamp,\r\n" +
            "  PRIMARY KEY (codeTemporaire)\r\n" +
            ");");
   }

   /**
    * Suppression des données de la table correspondancesrndcql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_correspondancesrndcqlTest() throws Exception {
      session.execute("TRUNCATE TABLE  \"SAE\".correspondancesrndcql");
   }

   /**
    * Insertion dans la table correspondancesrndcql
    * 
    * @throws Exception
    */
   @Test
   public void insert__correspondancesrndcqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".correspondancesrndcql \r\n" +
            "    (codeTemporaire, codeDefinitif, versionCourante, etat,  dateDebutMaj, dateFinMaj)\r\n" +
            "      VALUES ('CODETEMP','CODEDEF','VERSION1', 'CREATED', 1559044863263, 1559044865000\r\n" +
            "   );");
   }

   /**
    * Récupération des données de la table correspondancesrndcql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_correspondancerndcqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".correspondancesrndcql limit 100");
      dumper.dumpRows(rs);
   }

   // Table dictionarycql
   /**
    * Création de la table dictionarycql
    * 
    * @throws Exception
    */
   @Test // 1 fois
   public void createTable_dictionarycqlTest() throws Exception {
      session.execute("CREATE TABLE \"SAE\".dictionarycql (\r\n" +
            "  code text,\r\n" +
            "  valeurSpecifique map<text,text>,\r\n" +
            "  PRIMARY KEY (code)\r\n" +
            ");");

   }

   /**
    * Suppression des données de la table dictionarycql
    * 
    * @throws Exception
    */
   @Test
   public void truncateTable_dictionarycqlTest() throws Exception {
      session.execute("TRUNCATE TABLE  \"SAE\".dictionarycql");
   }

   /**
    * Insertion dans la table dictionarycql
    * 
    * @throws Exception
    */
   @Test
   public void insert__dictionarycqlTest() throws Exception {

      session.execute("INSERT INTO \"SAE\".dictionarycql \r\n" +
            "    (code, valeurSpecifique)\r\n" +
            "      VALUES ('dicCog',{'UR750': '', 'UR730':'' }\r\n" +
            "   );");
   }

   /**
    * Récupération des données de la table dictionarycql
    * 
    * @throws Exception
    */
   @Test
   public void testDump_dictionarycqlTest() throws Exception {
      final ResultSet rs = session.execute("select * from \"SAE\".dictionarycql limit 100");
      dumper.dumpRows(rs);
   }

}
