/**
 * 
 */
package fr.urssaf.image.administration.modele;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("columnFamily")
public class ColumnFamily {

   private String name;
   private String keyType;
   private String comparatorType;
   private String defaultColumnValueType;

   /**
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * @param name
    *           the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return the keyType
    */
   public String getKeyType() {
      return keyType;
   }

   /**
    * @param keyType
    *           the keyType to set
    */
   public void setKeyType(String keyType) {
      this.keyType = keyType;
   }

   /**
    * @return the comparatorType
    */
   public String getComparatorType() {
      return comparatorType;
   }

   /**
    * @param comparatorType
    *           the comparatorType to set
    */
   public void setComparatorType(String comparatorType) {
      this.comparatorType = comparatorType;
   }

   /**
    * @return the defaultColumnValueType
    */
   public String getDefaultColumnValueType() {
      return defaultColumnValueType;
   }

   /**
    * @param defaultColumnValueType
    *           the defaultColumnValueType to set
    */
   public void setDefaultColumnValueType(String defaultColumnValueType) {
      this.defaultColumnValueType = defaultColumnValueType;
   }

}
