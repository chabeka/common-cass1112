package fr.urssaf.astyanaxtest.dao;

import java.util.UUID;

import com.netflix.astyanax.annotations.Component;
import com.netflix.astyanax.serializers.ShortSerializer;

/**
 * Classe servant à la définir les éléments composant les clés 
 * pour les CF TermInfoRangeXXX
 *
 */
public class TermInfoRangeKey {

   private @Component(ordinal=0) byte[]   separator1 = new byte[0];
   private @Component(ordinal=1) String   categoryName;
   private @Component(ordinal=2) UUID     baseUUID;
   private @Component(ordinal=3) byte[]	  rangeId;
   
   public TermInfoRangeKey(String categoryName, UUID baseUUID, int rangeId) {
      this.categoryName = categoryName;
      this.baseUUID = baseUUID;
      if (rangeId < 128) {
    	  this.rangeId = new byte[] {(byte)rangeId};  
      }
      else {
    	  this.rangeId = ShortSerializer.get().toBytes((short)rangeId);
      }
      
   }

   public String getCategoryName() {
	   return categoryName;
   }

   public UUID getBaseUUID() {
		return baseUUID;
   }

}
