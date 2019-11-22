/**
 *  TODO (AC75095351) Description du fichier
 */
package fr.urssaf.image.sae.droit.model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.mapping.annotations.Transient;

/**
 * Cette classe est utilisée juste pour l'extraction des données dans les tables thrift avec une requete cql.
 * La classe est utilisée que pour la migration des données pour les tables <b>Trace</b>.<br>
 * Le schema de la table thrift doit correspondre au schema suivant:<br>
 * <ul>
 * <li><b>CREATE TABLE "Keysapce"."NomDeLaTable" (</b></li>
 * <li>-- key blob,</li>
 * <li>-- column1 text,</li>
 * <li>-- value blob</li>
 * <li><b>);</b></li>
 * </ul>
 */

public class GenericDroitType {

  protected ByteBuffer key;

  protected String column1;

  protected ByteBuffer value;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(GenericDroitType.class);

  @Transient
  private String strKey;

  @Transient
  private String strValue;

  /**
   * @return the key
   */
  public ByteBuffer getKey() {
    return key;
  }

  /**
   * @param key
   *           the key to set
   */
  public void setKey(final ByteBuffer key) {
    this.key = key;
  }

  /**
   * @return the column1
   */
  public String getColumn1() {
    return column1;
  }

  /**
   * @param column1
   *           the column1 to set
   */
  public void setColumn1(final String column1) {
    this.column1 = column1;
  }

  /**
   * @return the value
   */
  public ByteBuffer getValue() {
    return value;
  }

  /**
   * @param value
   *           the value to set
   */
  public void setValue(final ByteBuffer value) {
    this.value = value;
  }

  /**
   * @return the strKey
   */
  public String getStrKey() {
    // Extraction de la clé
    final byte[] arrayKey = key.array();
    try {
      strKey = new String(arrayKey, "UTF-8");
    }
    catch (final UnsupportedEncodingException e) {
      LOGGER.error(e.getMessage());
    }
    return strKey;
  }

  /**
   * @param strKey
   *           the strKey to set
   */
  public void setStrKey(final String strKey) {
    this.strKey = strKey;
  }

  /**
   * @return the srtValue
   */
  public String getSrtValue() {
    // extraction de la value

    final byte[] arrayValue = value.array();
    try {
      String str1 = "";
      if (arrayValue.length == 1) {
        final byte firstValue = arrayValue[0];
        str1 = Byte.toString(firstValue);
        strValue = new String(arrayValue, "UTF-8");
      } else if ("length".equals(column1)) {
        final ByteBuffer bb = ByteBuffer.wrap(arrayValue);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        final int ss = bb.getInt();
        strValue = Integer.toString(ss);
      } else {
        strValue = new String(arrayValue, "UTF-8");
      }
    }
    catch (final UnsupportedEncodingException e) {
      LOGGER.error(e.getMessage());
    }
    return strValue;
  }

  /**
   * @param srtValue
   *           the srtValue to set
   */
  public void setSrtValue(final String srtValue) {
    strValue = srtValue;
  }

}
