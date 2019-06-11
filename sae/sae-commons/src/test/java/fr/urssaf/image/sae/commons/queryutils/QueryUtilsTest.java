/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.commons.queryutils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;
import fr.urssaf.image.commons.cassandra.utils.QueryUtils;
import fr.urssaf.image.commons.cassandra.utils.Utils;
import junit.framework.Assert;

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

   @Test
   public void get_all_fields_of_entity() {
      final List<Field> fields = Utils.getEntityFileds(Test1.class);
      Assert.assertEquals(7, fields.size());
      //
      final List<Field> fields2 = Utils.getEntityFileds(Test2.class);
      Assert.assertEquals(9, fields2.size());
   }

   @Test
   public void createDeleteQuerywithSimpleKeyTest() {
      final String query = "DELETE FROM keyspace_tu.test1 WHERE identifiant='test1';";
      final Test1 test1 = new Test1();
      test1.setIdentifiant("test1");
      final Delete delete = QueryBuilder.delete().from("keyspace_tu", "test1");
      QueryUtils.createDeleteQuery(Test1.class, delete, test1);
      Assert.assertEquals(query, delete.toString());
   }

   @Test
   public void createDeleteQuerywithComsiteKeyTest() {
      final String query = "DELETE FROM keyspace_tu.TestAvecCompsiteKey WHERE identifiant0='test1' AND identifiant1='test2';";
      final TestAvecCompsiteKey test2 = new TestAvecCompsiteKey();
      test2.setIdentifiant0("test1");
      test2.setIdentifiant1("test2");
      final Delete delete = QueryBuilder.delete().from("keyspace_tu", "TestAvecCompsiteKey");
      QueryUtils.createDeleteQuery(TestAvecCompsiteKey.class, delete, test2);
      Assert.assertEquals(query, delete.toString());
   }
}

class Test1 {

   @PartitionKey
   private String identifiant;

   private Date timestamp;

   private String codeEvt;

   private String login;

   private String contratService;

   private final List<String> pagms = new ArrayList<String>();

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

}

@Table(name = "test2")
class Test2 extends Test1 {

   private String contexte;

   private String action;
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

   private final List<String> pagms = new ArrayList<String>();

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

}

@Table(name = "TestAvecCompsiteKey1")
class TestAvecCompsiteKey1 extends TestAvecCompsiteKey {

}
