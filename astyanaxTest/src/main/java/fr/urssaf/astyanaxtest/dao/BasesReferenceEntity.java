package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.mapping.Column;
import com.netflix.astyanax.mapping.Id;

/**
 * Entité pour le mapping de la CF BasesReference 
 *
 */
public class BasesReferenceEntity {
   @Id("ID")
   private String baseName;

   @Column("uuid")
   private byte[] baseUUID;

   public void setBaseName(String baseName) {
      this.baseName = baseName;
   }

   public String getBaseName() {
      return baseName;
   }

   public void setBaseUUID(byte[] baseUUID) {
      this.baseUUID = baseUUID;
   }

   public byte[] getBaseUUID() {
      return baseUUID;
   }

}
