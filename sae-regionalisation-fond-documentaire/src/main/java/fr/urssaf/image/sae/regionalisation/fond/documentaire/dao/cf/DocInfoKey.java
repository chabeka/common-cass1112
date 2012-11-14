package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf;

import java.util.UUID;

import com.netflix.astyanax.annotations.Component;

/**
 * Classe de mapping entre les objets et les classes CASSANDRA
 * 
 * 
 */
public class DocInfoKey {

   @Component(ordinal = 0)
   private UUID documentUuid;
   @Component(ordinal = 1)
   private String version;

   /**
    * @return the documentUuid
    */
   public final UUID getDocumentUuid() {
      return documentUuid;
   }

   /**
    * @param documentUuid
    *           the documentUuid to set
    */
   public final void setDocumentUuid(UUID documentUuid) {
      this.documentUuid = documentUuid;
   }

   /**
    * @return the version
    */
   public final String getVersion() {
      return version;
   }

   /**
    * @param version
    *           the version to set
    */
   public final void setVersion(String version) {
      this.version = version;
   }

}
