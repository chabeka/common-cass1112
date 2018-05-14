/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.commons.queryutils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import fr.urssaf.image.sae.commons.utils.ColumnUtil;
import fr.urssaf.image.sae.commons.utils.Utils;
import junit.framework.Assert;

/**
 * TODO (AC75095028) Description du type
 */
public class QueryUtilsTest {

  @Test
  public void get_simple_key_field_name() {
    // test de recupération de la l'id sur la classe contenant la clé de partition
    final Field partitionkey = ColumnUtil.getKeyField(Test1.class);
    final String keyName = partitionkey.getName();
    Assert.assertEquals("identifiant", keyName);

    // test de recupération de la clé depuis la classe héritant de la classe contenant la clé de partition
    final Field partitionkeyTest2 = ColumnUtil.getKeyField(Test2.class);
    final String keyNameTest2 = partitionkeyTest2.getName();
    Assert.assertEquals("identifiant", keyNameTest2);
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
}

class Test1 {
  @PartitionKey
  private UUID identifiant;

  private Date timestamp;

  private String codeEvt;

  private String login;

  private String contratService;

  private final List<String> pagms = new ArrayList<String>();

  private Map<String, Object> infos;
}

@Table(name = "test2")
class Test2 extends Test1 {
  private String contexte;

  private String action;
}
