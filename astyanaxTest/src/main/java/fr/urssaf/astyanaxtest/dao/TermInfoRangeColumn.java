package fr.urssaf.astyanaxtest.dao;

import java.util.UUID;

import com.netflix.astyanax.annotations.Component;
import com.netflix.astyanax.serializers.StringSerializer;

/**
 * Classe servant à la définir les éléments composant les noms de colonnes 
 * pour la CF TermInfoRangeString
 *
 */
public class TermInfoRangeColumn {

   private @Component(ordinal=0) byte[]   categoryValue;
   private @Component(ordinal=1) UUID     documentUUID;
   private @Component(ordinal=2) String   documentVersion;
   
   public void setCategoryValue(byte[] categoryValue) {
      this.categoryValue = categoryValue;
   }
   public void setCategoryValue(String categoryValue) {
      this.categoryValue = StringSerializer.get().toBytes(categoryValue);
   }

   public byte[] getCategoryValue() {
      return categoryValue;
   }
   public String getCategoryValueAsString() {
	   return StringSerializer.get().fromBytes(categoryValue);
   }

   public void setDocumentUUID(UUID documentUUID) {
      this.documentUUID = documentUUID;
   }
   public UUID getDocumentUUID() {
      return documentUUID;
   }
   public void setDocumentVersion(String documentVersion) {
      this.documentVersion = documentVersion;
   }
   public String getDocumentVersion() {
      return documentVersion;
   }

}
