package fr.urssaf.javaDriverTest;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

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
    // servers = "cnp6gnscvecas01.cve.recouv,cnp3gnscvecas01.cve.recouv,cnp7gnscvecas01.cve.recouv"; // Charge
    servers = "cnp31miggntcas3.cer31.recouv,cnp31miggntcas4.cer31.recouv"; // Migration cql
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

    final String cassandraLocalDC = "DC1";
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
        " " +
        "  PRIMARY KEY (code)\r\n" +
        ");");
    /*
     * "  description text,\r\n" +
     * "  pagma text,\r\n" +
     * "  pagmp text,\r\n" +
     * "  pagmf text,\r\n" +
     * "  parametres map<text,text>,\r\n" +
     * "  compressionPdfActive boolean,\r\n" +
     * "  seuilCompressionPdf int,\r\n" +
     */

    // compressionPdfActive seuilCompressionPdf
  }

  @Test
  public void truncateTable_droitpagmcqlTest() throws Exception {
    session.execute("TRUNCATE  \"SAE\".droitpagmcql");
  }

  @Test
  public void insert_CS_CIME_droitpagmcqlTest() throws Exception {
    final PreparedStatement prepared = session.prepare(
        "INSERT INTO \"SAE\".droitpagmcql(code,pagm) VALUES (?,?)");
    final Map<String, String> map1 = new HashMap<>();
    final Map<String, String> map2 = new HashMap<>();
    final Map<String, Object> pagm = new HashMap<>();
    map1.put("code", "PAGM_RECHERCHE_DOCUMENTAIRE_GNS");
    map1.put("description", "Droits de la recherche documentaire");
    map1.put("pagma", "PAGM_RECHERCHE_DOCUMENTAIRE_GNS_PAGMa");
    map1.put("pagmp", "PAGM_RECHERCHE_DOCUMENTAIRE_RECHERCHE_GNS_PAGMp");
    map1.put("parametres", "{}");
    pagm.put("PAGM_RECHERCHE_DOCUMENTAIRE_GNS", map1);
    map2.put("code", "PAGM_RECHERCHE_DOCUMENTAIRE_RECHERCHE_CONSULTATION");
    map2.put("description", "Recherche et consultation de tous les documents cotisants");
    map2.put("pagma", "PAGM_RECHERCHE_DOCUMENTAIRE_RECHERCHE_CONSULTATION_PAGMa");
    map2.put("pagmp", "PAGM_RECHERCHE_DOCUMENTAIRE_RECHERCHE_CONSULTATION_PAGMp");
    map2.put("parametres", "{}");
    pagm.put("PAGM_RECHERCHE_DOCUMENTAIRE_RECHERCHE_CONSULTATION", map2);

    final BoundStatement bound = prepared.bind("CS_RECHERCHE_DOCUMENTAIRE", pagm);
    session.execute(bound);

    /*
     * session.execute("INSERT INTO \"SAE\".droitpagmcql(code,pagma,pagmp,pagmf,parametres) VALUES (?, ?, ?, ?, ?)",
     * "CS_CIME",
     * "",
     * "",
     * "",
     * "{'description':'DUEFAX - Cas d'utilisation GNS','code':'PAGM_DUEFAX_GNS','pagma':'PAGM_DUEFAX_GNS_PAGMa','pagmp':'PAGM_DUEFAX_GNS_PAGMp'}");
     */
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
        "  metadata frozen<map<text,frozen<list<text>>>>,\r\n" +
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
    session.execute("DROP TABLE \"SAE\".droitprmdcql");
  }

  /**
   * Insertion dans la table droitprmdcql
   * 
   * @throws Exception
   */
  @Test
  public void insert__droitprmdcqlTest() throws Exception {

    session.execute("INSERT INTO \"SAE\".droitprmdcql \r\n" +
        "    (code, bean, description, prmd)\r\n" +
        "    VALUES ('PRMD_V2_PI06I_PI06_L09'," +
        "   '',\r\n" +
        "   'V2 - PI06I PI06.L09',\r\n" +
        "   'CodeRND:2.1.4.5.1 AND ApplicationProductrice:ADELAIDE AND DomaineCotisant:true',\r\n" +
        "   {'CodeRND': ['3\\.2\\.1\\.5\\.1', '3\\.2\\.1\\.5\\.1']});");
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
}
