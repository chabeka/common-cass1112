package fr.urssaf.image.sae.storage.model.storagedocument;

import org.apache.commons.io.IOUtils;

/**
 * Objet représentant le type de stockage d'un fichier une fois inséré
 * 
 */
public class StorageContentRepository {

   /**
    * Nom du type de stockage.
    */
   private String name;
   
   /**
    * Nom de la column family utilisé pour le stockage. 
    */
   private String columnFamily;
   
   /**
    * Etat du stockage: 
    * 0 - en attente
    * 1 - Monté
    */
   private int state;

   /** 
    * Getter permettant de récupérer le nom du stockage.
    * @return String
    */
   public final String getName() {
      return name;
   }

   /**
    * Setter permettant de modifier le nom du stockage.
    * @param name nom du stockage
    */
   public final void setName(final String name) {
      this.name = name;
   }

   /**
    * Getter permettant de récupérer le nom de la column family.
    * @return String
    */
   public final String getColumnFamily() {
      return columnFamily;
   }

   /**
    * Setter permettant de modfier le nom de la column family.
    * @param columnFamily nom de la column family
    */
   public final void setColumnFamily(final String columnFamily) {
      this.columnFamily = columnFamily;
   }

   /**
    * Getter permettant de récupérer l'état du type de stockage.
    * @return int
    */
   public final int getState() {
      return state;
   }

   /**
    * Setter permettant de modifier l'état du type de stockage.
    * @param state état du type de stockage
    */
   public final void setState(int state) {
      this.state = state;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {

      StringBuffer buffer = new StringBuffer();
      buffer.append("nom : ");
      buffer.append(name);
      buffer.append(IOUtils.LINE_SEPARATOR);
      buffer.append("nom de la column family : ");
      buffer.append(columnFamily);
      buffer.append(IOUtils.LINE_SEPARATOR);
      buffer.append("etat du type de stockage : ");
      buffer.append(state);
      return buffer.toString();
   }
}
