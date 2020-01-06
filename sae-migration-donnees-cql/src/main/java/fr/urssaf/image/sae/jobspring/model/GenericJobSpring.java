package fr.urssaf.image.sae.jobspring.model;

import java.nio.ByteBuffer;

/**
 * (AC75095028) Classe générique JobSpring
 */
public class GenericJobSpring {

  protected ByteBuffer key;

  protected Long column1;

  protected ByteBuffer value;

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
  public Long getColumn1() {
    return column1;
  }

  /**
   * @param column1
   *          the column1 to set
   */
  public void setColumn1(final Long column1) {
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

}
