package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf;

import java.util.UUID;

import com.netflix.astyanax.annotations.Component;

/**
 * Classe de mapping entre les objets et les classes CASSANDRA
 * 
 * 
 */
public class TermInfoRangeUuidKey {

   @Component(ordinal = 0)
   private byte[] separator1 = new byte[0];
   @Component(ordinal = 1)
   private String categoryName;
   @Component(ordinal = 2)
   private UUID baseUUID;
   @Component(ordinal = 3)
   private byte[] separator2 = new byte[] { 0 };

   /**
    * Constructeur
    * 
    * @param categoryName
    *           le nom de la catégorie
    * @param baseUUID
    *           l'uuid de la base
    */
   public TermInfoRangeUuidKey(String categoryName, UUID baseUUID) {
      this.categoryName = categoryName;
      this.baseUUID = baseUUID;
   }

}
