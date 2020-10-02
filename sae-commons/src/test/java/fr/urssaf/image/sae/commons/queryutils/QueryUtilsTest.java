/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.commons.queryutils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;


/**
 * TODO (AC75095028) Description du type
 */
public class QueryUtilsTest {


  @Test
  public void get_simple_key_field_name() {
    // test de recupération de l'id sur la classe contenant la clé de partition
    final Field partitionkey = ColumnUtil.getSimplePartionKeyField(Test1.class);
    final String keyName = partitionkey.getName();
    Assert.assertEquals("identifiant", keyName);

    // test de recupération de la clé depuis la classe héritant de la classe contenant la clé de partition
    final Field partitionkeyTest2 = ColumnUtil.getSimplePartionKeyField(Test2.class);
    final String keyNameTest2 = partitionkeyTest2.getName();
    Assert.assertEquals("identifiant", keyNameTest2);
  }

  @Test
  public void getCompositeKeyFieldName() {
    // test de recupération de l'id sur la classe contenant la clé de partition
    final List<Field> partitionkeys = ColumnUtil.getCompositePartionKeyField(TestAvecCompsiteKey.class);
    Assert.assertEquals(2, partitionkeys.size());
    Assert.assertEquals("identifiant0", partitionkeys.get(0).getName());
    Assert.assertEquals("identifiant1", partitionkeys.get(1).getName());

    // test de recupération de la clé depuis la classe héritant de la classe contenant la clé de partition
    final List<Field> partitionkeyTest2 = ColumnUtil.getCompositePartionKeyField(TestAvecCompsiteKey1.class);
    Assert.assertEquals(2, partitionkeyTest2.size());
    Assert.assertEquals("identifiant0", partitionkeyTest2.get(0).getName());
    Assert.assertEquals("identifiant1", partitionkeyTest2.get(1).getName());
  }

  @Test
  public void get_column_family_name() {
    final String columnFamilly = ColumnUtil.getColumnFamily(Test2.class);
    Assert.assertEquals("test2", columnFamilly);
  }
}

@Table(name = "test1")
class Test1 {

  @PartitionKey
  private String identifiant;

  private Date timestamp;

  private String codeEvt;

  private String login;

  private String contratService;

  private final List<String> pagms = new ArrayList<>();

  private Map<String, Object> infos;

  /**
   * @return the identifiant
   */
  public String getIdentifiant() {
    return identifiant;
  }

  /**
   * @param identifiant
   *           the identifiant to set
   */
  public void setIdentifiant(final String identifiant) {
    this.identifiant = identifiant;
  }

  /**
   * @return the timestamp
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(final Date timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * @return the codeEvt
   */
  public String getCodeEvt() {
    return codeEvt;
  }

  /**
   * @param codeEvt the codeEvt to set
   */
  public void setCodeEvt(final String codeEvt) {
    this.codeEvt = codeEvt;
  }

  /**
   * @return the login
   */
  public String getLogin() {
    return login;
  }

  /**
   * @param login the login to set
   */
  public void setLogin(final String login) {
    this.login = login;
  }

  /**
   * @return the contratService
   */
  public String getContratService() {
    return contratService;
  }

  /**
   * @param contratService the contratService to set
   */
  public void setContratService(final String contratService) {
    this.contratService = contratService;
  }

  /**
   * @return the pagms
   */
  public List<String> getPagms() {
    return pagms;
  }

  /**
   * @return the infos
   */
  public Map<String, Object> getInfos() {
    return infos;
  }

  /**
   * @param infos the infos to set
   */
  public void setInfos(final Map<String, Object> infos) {
    this.infos = infos;
  }

}

@Table(name = "test2")
class Test2 extends Test1 {

  private String contexte;

  private String action;

  /**
   * @return the contexte
   */
  public String getContexte() {
    return contexte;
  }

  /**
   * @param contexte the contexte to set
   */
  public void setContexte(final String contexte) {
    this.contexte = contexte;
  }

  /**
   * @return the action
   */
  public String getAction() {
    return action;
  }

  /**
   * @param action the action to set
   */
  public void setAction(final String action) {
    this.action = action;
  }
}

@Table(name = "TestAvecCompsiteKey")
class TestAvecCompsiteKey {

  @PartitionKey(0)
  private String identifiant0;

  @PartitionKey(1)
  private String identifiant1;

  private String codeEvt;

  private String login;

  private String contratService;

  private final List<String> pagms = new ArrayList<>();

  private Map<String, Object> infos;

  /**
   * @return the identifiant0
   */
  public String getIdentifiant0() {
    return identifiant0;
  }

  /**
   * @param identifiant0
   *           the identifiant0 to set
   */
  public void setIdentifiant0(final String identifiant0) {
    this.identifiant0 = identifiant0;
  }

  /**
   * @return the identifiant1
   */
  public String getIdentifiant1() {
    return identifiant1;
  }

  /**
   * @param identifiant1
   *           the identifiant1 to set
   */
  public void setIdentifiant1(final String identifiant1) {
    this.identifiant1 = identifiant1;
  }

  /**
   * @return the codeEvt
   */
  public String getCodeEvt() {
    return codeEvt;
  }

  /**
   * @param codeEvt the codeEvt to set
   */
  public void setCodeEvt(final String codeEvt) {
    this.codeEvt = codeEvt;
  }

  /**
   * @return the login
   */
  public String getLogin() {
    return login;
  }

  /**
   * @param login the login to set
   */
  public void setLogin(final String login) {
    this.login = login;
  }

  /**
   * @return the contratService
   */
  public String getContratService() {
    return contratService;
  }

  /**
   * @param contratService the contratService to set
   */
  public void setContratService(final String contratService) {
    this.contratService = contratService;
  }

  /**
   * @return the pagms
   */
  public List<String> getPagms() {
    return pagms;
  }

  /**
   * @return the infos
   */
  public Map<String, Object> getInfos() {
    return infos;
  }

  /**
   * @param infos the infos to set
   */
  public void setInfos(final Map<String, Object> infos) {
    this.infos = infos;
  }

}

@Table(name = "TestAvecCompsiteKey1")
class TestAvecCompsiteKey1 extends TestAvecCompsiteKey {

}
