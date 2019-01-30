/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.datastax.driver.mapping.annotations.Transient;

/**
 * TODO (AC75095028) Description du type
 */
public class MetadataType extends GenericType {

  @Transient
  private String strKey;

  @Transient
  private String srtValue;

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
        srtValue = new String(arrayValue, "UTF-8");
      } else if ("length".equals(column1)) {
        final ByteBuffer bb = ByteBuffer.wrap(arrayValue);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        final int ss = bb.getInt();
        srtValue = Integer.toString(ss);
      } else {
        srtValue = new String(arrayValue, "UTF-8");
      }
    }
    catch (final UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return srtValue;
  }

  /**
   * @param srtValue
   *          the srtValue to set
   */
  public void setSrtValue(final String srtValue) {
    this.srtValue = srtValue;
  }

}
