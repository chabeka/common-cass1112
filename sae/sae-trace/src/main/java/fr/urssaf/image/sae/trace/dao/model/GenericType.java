/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;

/**
 * TODO (AC75095028) Description du type
 */
@Table(name = "TraceJournalEvt")
public class GenericType {

  protected ByteBuffer key;

  protected String column1;

  protected ByteBuffer value;

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
   *          the key to set
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
   *          the column1 to set
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
   *          the value to set
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
      e.printStackTrace();
    }
    return strKey;
  }

  /**
   * @param strKey
   *          the strKey to set
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
      e.printStackTrace();
    }
    return strValue;
  }

  /**
   * @param srtValue
   *          the srtValue to set
   */
  public void setSrtValue(final String srtValue) {
    this.strValue = srtValue;
  }

}