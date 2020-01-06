package fr.urssaf.image.sae.piletravaux.model;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Cette classe est utilisée pour l'extraction des données dans les tables thrift avec une requete cql.<br>
 * A l'extraction des données, les seront mapper avec cette pour former des entités de type {@link GenericJobType}
 * qui pourront être dircetement manipulé.<br>
 * La classe est principalement utilisée pour la migration des données.<br>
 * Pour que le mapping puisse être possible, le schema de la table thrift doit être de la forme:<br>
 * <ul>
 * <li><b>CREATE TABLE "Keysapce"."NomDeLaTable" (</b></li>
 * <li>-- key blob,</li>
 * <li>-- column1 timeuuid,</li>
 * <li>-- value blob</li>
 * <li><b>);</b></li>
 * </ul>
 */
public class GenericJobType {

  protected ByteBuffer key;

  protected UUID column1;

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
  public UUID getColumn1() {
    return column1;
  }

  /**
   * @param column1
   *          the column1 to set
   */
  public void setColumn1(final UUID column1) {
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
